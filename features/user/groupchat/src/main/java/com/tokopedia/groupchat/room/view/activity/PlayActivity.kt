package com.tokopedia.groupchat.room.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.common.applink.ApplinkConstant
import com.tokopedia.groupchat.common.util.NonSwipeableViewPager
import com.tokopedia.groupchat.common.util.TransparentStatusBarHelper
import com.tokopedia.groupchat.room.di.DaggerPlayComponent
import com.tokopedia.groupchat.room.view.adapter.FragmentPagerAdapter
import com.tokopedia.groupchat.room.view.fragment.BlankFragment
import com.tokopedia.groupchat.room.view.fragment.PlayFragment
import com.tokopedia.videoplayer.state.PlayerController
import com.tokopedia.videoplayer.state.PlayerException
import com.tokopedia.videoplayer.state.PlayerType
import com.tokopedia.videoplayer.state.RepeatMode
import com.tokopedia.groupchat.room.view.listener.PlayActivityContract
import com.tokopedia.groupchat.room.view.presenter.PlayActivityPresenter
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.videoplayer.utils.sendViewToBack
import com.tokopedia.videoplayer.view.player.TkpdVideoPlayer
import com.tokopedia.videoplayer.view.player.VideoPlayerListener
import kotlinx.android.synthetic.main.play_activity.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author : Steven 11/02/19
 */
open class PlayActivity : BaseSimpleActivity(), PlayerViewListener, PlayActivityContract.View {

    lateinit var rootView: View
    lateinit var viewPager: NonSwipeableViewPager
    private lateinit var pagerAdapter: FragmentPagerAdapter

    @Inject
    lateinit var analytics: GroupChatAnalytics

    @Inject
    lateinit var presenter: PlayActivityPresenter

    var channelId: String? = null

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
        if (intent != null && intent.extras != null) {
            val roomName = intent.extras!!.getString(EXTRA_CHANNEL_UUID, "")
            return GroupChatAnalytics.SCREEN_CHAT_ROOM + roomName
        } else {
            return GroupChatAnalytics.SCREEN_CHAT_ROOM
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    private fun initInjector() {
        val playComponent = DaggerPlayComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        playComponent.inject(this)
        presenter.attachView(this)
    }

    private fun initView() {
        setupToolbar()
        setFragment()
        presenter.getVideoStream(channelId, onSuccessGetVideoStream(), onErrorGetVideoStream())
    }

    override fun onPlayerActive(isActive: Boolean) {
        if (isActive) {
            sendViewToBack(playerView)
            val sourceMedia = "https://scontent-sin6-1.cdninstagram.com/vp/cb4297650b392eab52095d911a1a17dc/5D1C8FA4/t50.12441-16/53306725_332584844027284_3716503313000746737_n.mp4?_nc_ht=scontent-sin6-1.cdninstagram.com"

            var width = findViewById<View>(R.id.root_view).layoutParams.width
            var height = findViewById<View>(R.id.root_view).layoutParams.height
            val layoutParams = playerView.layoutParams
            layoutParams.width = width
            layoutParams.height = height

            playerView.layoutParams = layoutParams

            TkpdVideoPlayer.Builder()
                    .transaction(R.id.playerView, supportFragmentManager)
                    .videoSource(sourceMedia)
                    /* preventing seekTo, declare videoPlayer with live_stream mode */
                    .type(PlayerType.LIVE_STREAM)
                    /* if you have custom controller, turn it off */
                    .controller(PlayerController.OFF)
                    /* repeat video mode after finished */
                    .repeatMode(RepeatMode.REPEAT_MODE_ALL)
                    /* handle video player listener */
                    .listener(object : VideoPlayerListener {
                        override fun onPlayerStateChanged(playbackState: Int) {
                            //@references playBackState: com.google.android.exoplayer2.Player
                        }

                        override fun onPlayerError(error: PlayerException) {
                            //@references error: com.tokopedia.videoplayer.state.PlayerException
                        }
                    })
                    .build()
        }
    }

    private fun setFragment() {

        viewPager = findViewById(R.id.view_pager_play)

        val fragmentList = ArrayList<Fragment>()

        val bundle = Bundle()
        val useGCP = intent?.extras?.getString(EXTRA_USE_GCP, "false")
        useGCP?.run{
            bundle.putBoolean(EXTRA_USE_GCP, this.toBoolean())
        }
        bundle.putString(PlayActivity.EXTRA_CHANNEL_UUID, channelId)
        fragmentList.add(BlankFragment.createInstance(bundle = Bundle()))
        fragmentList.add(PlayFragment.createInstance(bundle))

        pagerAdapter = FragmentPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = pagerAdapter

        viewPager.currentItem = 1

        viewPager.swipeable = false


    }

    private fun setupToolbar() {

        if (isLollipopOrNewer()) {
            TransparentStatusBarHelper.assistActivity(this)

            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

        removePaddingStatusBar()
    }


    private fun removePaddingStatusBar() {

        rootView = findViewById(R.id.root_view)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height

            if (heightDiff > KEYBOARD_THRESHOLD) {
                removePaddingIfKeyboardIsShowing()
            } else {
                addPaddingIfKeyboardIsClosed()
            }
        }
    }

    private fun isLollipopOrNewer(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    private fun addPaddingIfKeyboardIsClosed() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
            val container = rootView.findViewById<View>(R.id.main_content)
            val params = container
                    .layoutParams as RelativeLayout.LayoutParams
            params.setMargins(0, 0, 0, getSoftButtonsBarSizePort(this))
            container.layoutParams = params
        }
    }

    private fun removePaddingIfKeyboardIsShowing() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
            val container = rootView.findViewById<View>(R.id.main_content)
            val params = container.layoutParams as RelativeLayout.LayoutParams
            params.setMargins(0, 0, 0, 0)
            container.layoutParams = params
        }
    }

    fun getSoftButtonsBarSizePort(activity: Activity): Int {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            activity.windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight)
                realHeight - usableHeight
            else
                0
        }
        return 0
    }

    override fun onBackPressed() {
        val currentFragment = pagerAdapter.getItem(viewPager.currentItem)
        if (currentFragment is PlayFragment) {
            currentFragment.backPress()
        } else {
            super.onBackPressed()
        }

    }

    fun changeHomeDrawableColor(resId: Int) {
        supportActionBar?.let {
            var drawable = MethodChecker.getDrawable(this, R.drawable.ic_action_back)
            val wrapped = DrawableCompat.wrap(drawable)
            drawable.mutate()
            DrawableCompat.setTint(wrapped, MethodChecker.getColor(this, resId))
            it.setHomeAsUpIndicator(drawable)
        }

        invalidateOptionsMenu()
    }

    fun setSwipeable(swipeable: Boolean) {
        if (!swipeable) {
        } else {
        }
    }

    private fun onSuccessGetVideoStream(): (VideoStreamViewModel) -> Unit {
        return {
        }
    }

    private fun onErrorGetVideoStream(): (String) -> Unit {
        return {

        }
    }

    companion object {

        private val KEYBOARD_THRESHOLD = 100

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