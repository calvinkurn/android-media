package com.tokopedia.groupchat.room.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.app.TaskStackBuilder
import android.support.v4.view.ViewPager
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.groupchat.GroupChatModuleRouter
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel
import com.tokopedia.groupchat.common.applink.ApplinkConstant
import com.tokopedia.groupchat.room.view.adapter.FragmentPagerAdapter
import com.tokopedia.groupchat.room.view.fragment.BlankFragment
import com.tokopedia.groupchat.room.view.fragment.PlayFragment

/**
 * @author : Steven 11/02/19
 */
open class PlayActivity : BaseSimpleActivity() {

    lateinit var rootView: View
    lateinit var viewPager: ViewPager
    val KEYBOARD_THRESHOLD = 100
    private lateinit var pagerAdapter: FragmentPagerAdapter

    override fun getNewFragment(): Fragment? {
//        val bundle = Bundle()
//        if (intent != null && intent.extras != null) {
//            bundle.putAll(intent.extras)
//        }
//        return PlayFragment.createInstance(bundle)
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.play_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        setupToolbar()

        setFragment()
    }

    private fun setFragment() {

        viewPager = findViewById<ViewPager>(R.id.view_pager_play)

        val fragmentList = ArrayList<Fragment>()

        val bundle = Bundle()
        val channelId = intent?.extras?.getString(EXTRA_CHANNEL_UUID)
        bundle.putString(PlayActivity.EXTRA_CHANNEL_UUID, channelId)
        fragmentList.add(BlankFragment.createInstance(bundle = Bundle()))
        fragmentList.add(PlayFragment.createInstance(bundle))

        pagerAdapter = FragmentPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem = 1

    }

    private fun setupToolbar() {
//        if (isLollipopOrNewer()) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//            window.statusBarColor = Color.TRANSPARENT
//        }

        if (isLollipopOrNewer()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }

//        if (isLollipopOrNewer()) {
//            TransparentStatusBarHelper.assistActivity(this)
//        }

//        removePaddingStatusBar()

//        toolbar = findViewById(R.id.toolbar)

//        if (isLollipopOrNewer()) {
////            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0)
//        }
//        setSupportActionBar(toolbar)

//        if (supportActionBar != null) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun isLollipopOrNewer(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    private fun removePaddingStatusBar() {

        rootView = findViewById<View>(R.id.root_view)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val heightDiff = rootView.rootView.height - rootView.height

            if (heightDiff > KEYBOARD_THRESHOLD) {
                removePaddingIfKeyboardIsShowing()
            } else {
                addPaddingIfKeyboardIsClosed()
            }
        }
    }

    private fun addPaddingIfKeyboardIsClosed() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
            val container = rootView.findViewById<View>(R.id.container)
            val params = container
                    .layoutParams as ConstraintLayout.LayoutParams
            params.setMargins(0, 0, 0, getSoftButtonsBarSizePort(this))
            container.layoutParams = params
        }
    }

    private fun removePaddingIfKeyboardIsShowing() {
        if (isLollipopOrNewer() && getSoftButtonsBarSizePort(this) > 0) {
            val container = rootView.findViewById<View>(R.id.container)
            val params = container.layoutParams as ConstraintLayout.LayoutParams
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


    companion object {

        val EXTRA_CHANNEL_UUID = "CHANNEL_UUID"
        val EXTRA_CHANNEL_INFO = "CHANNEL_INFO"
        val EXTRA_SHOW_BOTTOM_DIALOG = "SHOW_BOTTOM"
        val EXTRA_POSITION = "position"
        val APPLINK_DATA = "APPLINK_DATA"

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
        fun getCallingIntent(context: Context, channelId: String, applinkData: String?): Intent {
            val intent = Intent(context, PlayActivity::class.java)
            val bundle = Bundle()
            bundle.putString(EXTRA_CHANNEL_UUID, channelId)
            bundle.putBoolean(EXTRA_SHOW_BOTTOM_DIALOG, true)
            bundle.putString(APPLINK_DATA, applinkData)
            intent.putExtras(bundle)
            return intent
        }
    }

    object DeepLickIntents {

        @JvmStatic
        @DeepLink(ApplinkConstant.GROUPCHAT_ROOM)
        fun getCallingTaskStack(context: Context, extras: Bundle): TaskStackBuilder {
            val id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID)
            val homeIntent = (context.applicationContext as GroupChatModuleRouter).getHomeIntent(context)
            var detailsIntent = PlayActivity.getCallingIntent(context, id)
            if (extras.get(ApplinkConstant.PARAM_TAB) != null) {
                detailsIntent = PlayActivity.getCallingIntent(context, id, extras.getString(ApplinkConstant.PARAM_TAB))
            }
            val taskStackBuilder = TaskStackBuilder.create(context)
            taskStackBuilder.addNextIntent(homeIntent)
            taskStackBuilder.addNextIntent(detailsIntent)
            return taskStackBuilder
        }

        @JvmStatic
        @DeepLink(ApplinkConstant.GROUPCHAT_ROOM_VIA_LIST)
        fun getCallingTaskStackViaList(context: Context, extras: Bundle): TaskStackBuilder {
            val id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID)
            val homeIntent = (context.applicationContext as GroupChatModuleRouter).getHomeIntent(context)
            val detailsIntent = PlayActivity.getCallingIntent(context, id)
            val parentIntent = (context.applicationContext as GroupChatModuleRouter)
                    .getInboxChannelsIntent(context)

            val taskStackBuilder = TaskStackBuilder.create(context)
            taskStackBuilder.addNextIntent(homeIntent)
            taskStackBuilder.addNextIntent(parentIntent)
            taskStackBuilder.addNextIntent(detailsIntent)
            return taskStackBuilder
        }

        @JvmStatic
        @DeepLink(ApplinkConstant.GROUPCHAT_VOTE_VIA_LIST)
        fun getCallingTaskStackVoteViaList(context: Context, extras: Bundle): TaskStackBuilder {
            val id = extras.getString(ApplinkConstant.PARAM_CHANNEL_ID)
            val homeIntent = (context.applicationContext as GroupChatModuleRouter).getHomeIntent(context)
            val detailsIntent = PlayActivity.getCallingIntent(context, id)
            val parentIntent = (context.applicationContext as GroupChatModuleRouter)
                    .getInboxChannelsIntent(context)

            val taskStackBuilder = TaskStackBuilder.create(context)
            taskStackBuilder.addNextIntent(homeIntent)
            taskStackBuilder.addNextIntent(parentIntent)
            taskStackBuilder.addNextIntent(detailsIntent)
            return taskStackBuilder
        }
    }
}