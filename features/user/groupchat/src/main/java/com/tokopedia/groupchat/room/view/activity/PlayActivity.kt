package com.tokopedia.groupchat.room.view.activity

import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.DisplayMetrics
import android.util.Rational
import android.view.View
import android.view.WindowManager
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.applink.ApplinkConstant
import com.tokopedia.groupchat.room.di.DaggerPlayComponent
import com.tokopedia.groupchat.room.view.adapter.FragmentPagerAdapter
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author : Steven 11/02/19
 */
open class PlayActivity : BaseSimpleActivity() {

    private lateinit var fragmentContainer: View
    private lateinit var pagerAdapter: FragmentPagerAdapter

    @Inject
    lateinit var analytics: GroupChatAnalytics

    var channelId: String? = null

    var pipDuration = 0L
    var pipStartTime = 0L
    var pipEndTime = 0L

    private val mPictureInPictureParamsBuilder
            = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PictureInPictureParams.Builder()
    } else {
        null
    }

    protected var remoteConfig: RemoteConfig? = null

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.play_activity
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(this, screenName)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun getScreenName(): String {
        return if (intent != null && intent.extras != null) {
            val roomName = intent.extras!!.getString(EXTRA_CHANNEL_UUID, "")
            GroupChatAnalytics.SCREEN_CHAT_ROOM + roomName
        } else {
            GroupChatAnalytics.SCREEN_CHAT_ROOM
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelId = intent?.extras?.getString(ApplinkConstant.PARAM_CHANNEL_ID)
        if(channelId == null) {
            channelId = intent?.extras?.getString(EXTRA_CHANNEL_UUID)
        }
        initInjector()
        initView()
    }

    private fun initInjector() {
        val playComponent = DaggerPlayComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        playComponent.inject(this)
    }

    private fun initView() {
        setFragment()
        remoteConfig = FirebaseRemoteConfigImpl(this)
    }

    private fun setFragment() {

        fragmentContainer = findViewById(R.id.fragment_container)

        val fragmentList = ArrayList<Fragment>()

        val bundle = Bundle()
        val useGCP = intent?.extras?.getString(EXTRA_USE_GCP, "false")
        useGCP?.run{
            bundle.putBoolean(EXTRA_USE_GCP, this.toBoolean())
        }
        bundle.putString(PlayActivity.EXTRA_CHANNEL_UUID, channelId)

        pagerAdapter = FragmentPagerAdapter(supportFragmentManager, fragmentList)

        val playFragment = PlayFragment.createInstance(bundle)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, playFragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        checkFragmentisPlayFragment()?.let {
            it.backPress()
            return
        }
        super.onBackPressed()
    }

    fun changeHomeDrawableColor(resId: Int) {
        supportActionBar?.let {
            val drawable = MethodChecker.getDrawable(this, R.drawable.ic_action_back)
            val wrapped = DrawableCompat.wrap(drawable)
            drawable.mutate()
            DrawableCompat.setTint(wrapped, MethodChecker.getColor(this, resId))
            it.setHomeAsUpIndicator(drawable)
        }

        invalidateOptionsMenu()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        minimize()
    }

    private fun minimize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isPipActivated()) {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            checkFragmentisPlayFragment()?.let {
                if(it.hasVideoVertical() && it.isChannelActive()) {
                    mPictureInPictureParamsBuilder?.let {
                        it.setAspectRatio(Rational(9, 16))
                        enterPictureInPictureMode(it.build())
                    }
                }
            }
        }
    }

    private fun isPipActivated(): Boolean {
        remoteConfig?.let {
            return it.getBoolean(RemoteConfigKey.PLAY_PIP)
        }
        return true
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        val decorView = window.decorView
        if(isInPictureInPictureMode) {
            analytics.eventChannelToPip(channelId)
            fragmentContainer.hide()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            } else {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        } else {
            analytics.eventPipToChannel(channelId)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            fragmentContainer.show()

        }
    }

    private fun checkFragmentisPlayFragment(): PlayFragment? {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return if (currentFragment is PlayFragment) {
            currentFragment
        } else {
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(isInPictureInPictureMode) {
                analytics.eventPipClosed(channelId)
            }
        }
    }

    companion object {

        val TOTAL_VIEW = "total_view"
        val EXTRA_CHANNEL_UUID = "CHANNEL_UUID"
        val EXTRA_CHANNEL_INFO = "CHANNEL_INFO"
        val EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM"
        const val EXTRA_USE_GCP = "use_gcp"
        val EXTRA_POSITION = "position"

        @JvmStatic
        fun getCallingIntent(context: Context, channelViewModel: ChannelViewModel, position: Int): Intent {
            val intent = Intent(context, PlayActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CHANNEL_INFO, channelViewModel)
            bundle.putString(EXTRA_CHANNEL_UUID, channelViewModel.channelUrl)
            bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, false)
            bundle.putInt(EXTRA_POSITION, position)
            intent.putExtras(bundle)
            return intent
        }

        /**
         * @param channelId can also be substitued by channelUrl
         * @return Intent
         */
        @JvmStatic
        fun getCallingIntent(context: Context, channelId: String): Intent {
            val intent = Intent(context, PlayActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_CHANNEL_UUID, channelId)
            bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, true)
            intent.putExtras(bundle)
            return intent
        }

        /**
         * @param channelId can also be substitued by channelUrl
         * @param applinkData if applink contains tab id for access chat/vote/info fragment
         * @return Intent
         */

        @JvmStatic
        fun getCallingIntent(context: Context, channelId: String, useGCP: Boolean): Intent {
            val intent = Intent(context, PlayActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_CHANNEL_UUID, channelId)
            bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, true)
            bundle.putBoolean(EXTRA_USE_GCP, useGCP)
            intent.putExtras(bundle)
            return intent
        }

        val KICK_THRESHOLD_TIME = TimeUnit.MINUTES.toMillis(5)
    }

    object DeepLickIntents {

        @JvmStatic
        @DeepLink(ApplinkConstant.GROUPCHAT_ROOM)
        fun getCallingTaskStack(context: Context, extras: Bundle): Intent {
            val id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID, "")
            val useGcp : String = extras.getString(ApplinkConstant.PARAM_GCP, "false")
            return getCallingIntent(context, id, useGcp.toBoolean())
        }
    }
}