package com.tokopedia.sellerhomedrawer.presentation.view.drawer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerActivityBroadcastReceiverConstant
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeFragmentBroadcastReceiverConstant
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerDeposit
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerTopPoints
import com.tokopedia.sellerhomedrawer.di.component.DaggerSellerHomeDrawerComponent
import com.tokopedia.sellerhomedrawer.di.component.SellerHomeDrawerComponent
import com.tokopedia.sellerhomedrawer.di.module.SellerHomeDashboardModule
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManager
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManagerImpl
import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader.SellerDrawerHeader
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sah_custom_action_bar_title.view.*
import kotlinx.android.synthetic.main.sh_custom_actionbar_drawer_notification.view.*
import rx.Observable
import javax.inject.Inject

abstract class SellerDrawerPresenterActivity : BaseSimpleActivity(),
        SellerDrawerDataListener
{
    companion object {
        const val MAX_NOTIF = 999
    }

    lateinit var sellerDrawerHelper: SellerDrawerHelper

    lateinit var toolbarTitle: View

    protected var drawerDataManager: SellerDrawerDataManager? = null
    private var isLogin: Boolean? = null
    private var drawerActivityBroadcastReceiver: BroadcastReceiver? = null
    private var broadcastReceiverTokoPoint: BroadcastReceiver? = null
    private var broadcastReceiverPendingTokocash: BroadcastReceiver? = null

    @Inject
    lateinit var getSellerHomeUserAttributesUseCase: GetSellerHomeUserAttributesUseCase
    @Inject
    lateinit var userSession: UserSession
    @Inject
    lateinit var drawerCache: LocalCacheHandler
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isSellerHome && GlobalConfig.isSellerApp()) {
            setTheme(R.style.Theme_Green_NoOverlay)
        }
        super.onCreate(savedInstanceState)

        injectDependency()

        if (GlobalConfig.isSellerApp()) {
            setupDrawer()
            setupToolbar()
            setupParentViewLayout()
            setupDrawerStatusBar()
        }
    }

    private fun injectDependency() {
        val component: SellerHomeDrawerComponent = DaggerSellerHomeDrawerComponent.builder()
                .sellerHomeDashboardModule(SellerHomeDashboardModule(this))
                .build()
        component.inject(this)
    }

    private fun setupDrawer() {

        val sellerTokoCashObservable = Observable.just(SellerTokoCashData())
        val sellerTokoCashUseCase = SellerTokoCashUseCase(sellerTokoCashObservable)

        sellerDrawerHelper = SellerDrawerHelper(this, userSession, drawerCache, remoteConfig)
        sellerDrawerHelper.selectedPosition = setDrawerPosition()
        sellerDrawerHelper.initDrawer(this)
        drawerDataManager = SellerDrawerDataManagerImpl(this, this, sellerTokoCashUseCase, getSellerHomeUserAttributesUseCase)
    }

    protected abstract fun setDrawerPosition(): Int

    protected open fun updateDrawerData() {
        if (userSession.isLoggedIn) {
            setDataDrawer()
            drawerDataManager?.getSellerUserAttributes(userSession)
        }
    }

    protected fun setDataDrawer() {
        val dataDrawer = sellerDrawerHelper.createDrawerData()
        val visitableList = mutableListOf<Visitable<*>>().apply {
            add(sellerDrawerHelper.sellerDrawerHeader)
            addAll(dataDrawer)
        }
        sellerDrawerHelper.sellerDrawerAdapter?.setVisitables(visitableList)
        sellerDrawerHelper.setExpand()
    }

    override fun getLayoutRes(): Int {
        return R.layout.sh_drawer_activity
    }

    override fun onErrorGetDeposit(errorMessage: String) {

    }

    override fun onErrorGetNotificationDrawer(errorMessage: String) {
        setDataDrawer()
    }

    override fun onErrorGetNotificationTopchat(errorMessage: String) {

    }

    override fun onGetDeposit(drawerDeposit: SellerDrawerDeposit) {

        if (sellerDrawerHelper.sellerDrawerAdapter?.list?.getOrNull(0) is SellerDrawerHeader) {
            (sellerDrawerHelper.sellerDrawerAdapter?.list?.getOrNull(0) as SellerDrawerHeader).sellerDrawerDeposit = drawerDeposit
        }
        sellerDrawerHelper.sellerDrawerAdapter?.notifyDataSetChanged()

    }

    override fun onGetNotificationDrawer(drawerNotification: SellerDrawerNotification) {
        onSuccessGetTopChatNotification(drawerNotification.inboxMessage)
        val notificationCount = drawerCache.getInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF)

        val notifRed = toolbar.toggle_count_notif

        if (notifRed != null) {
            if (notificationCount <= 0)
                notifRed.visibility = View.GONE
            else {
                notifRed.visibility = View.VISIBLE
                val totalNotif =
                        when {
                            drawerCache.getInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF) > MAX_NOTIF ->
                                getString(R.string.max_notif)
                            else -> drawerCache.getInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF).toString()
                        }
                notifRed.text = totalNotif
            }
        }

        var notifDrawable = R.drawable.sh_red_circle
        if (drawerNotification.isUnread) {
            notifDrawable = R.drawable.sh_green_circle
        }
        MethodChecker.setBackground(notifRed, resources.getDrawable(notifDrawable))

        setDataDrawer()
    }

    override fun onGetTokoCash(drawerTokoCash: SellerDrawerTokoCash) {
        sendDrawerActivityIntent(drawerTokoCash)
        sendHomeFragmentIntent(drawerTokoCash)
    }

    private fun sendDrawerActivityIntent(drawerTokoCash: SellerDrawerTokoCash) {
        val intentDrawerActivity = Intent(SellerDrawerActivityBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP)
        intentDrawerActivity.putExtra(SellerDrawerActivityBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER, SellerDrawerActivityBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA)
        intentDrawerActivity.putExtra(SellerDrawerActivityBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA, drawerTokoCash)
        sendBroadcast(intentDrawerActivity)
    }

    private fun sendHomeFragmentIntent(drawerTokoCash: SellerDrawerTokoCash) {
        val intentHomeFragment = Intent(SellerHomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP)
        intentHomeFragment.putExtra(SellerHomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                SellerHomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA)
        intentHomeFragment.putExtra(SellerHomeFragmentBroadcastReceiverConstant.EXTRA_TOKOCASH_DRAWER_DATA,
                drawerTokoCash.homeHeaderWalletAction)
        sendBroadcast(intentHomeFragment)
    }

    override fun onErrorGetTokoCash(errorMessage: String) {
        sendHomeFragmentIntentError(errorMessage)
    }

    private fun sendHomeFragmentIntentError(errorMessage: String) {
        val intentHomeFragment = Intent(SellerHomeFragmentBroadcastReceiverConstant.INTENT_ACTION_MAIN_APP)
        intentHomeFragment.putExtra(SellerHomeFragmentBroadcastReceiverConstant.EXTRA_ACTION_RECEIVER,
                SellerHomeFragmentBroadcastReceiverConstant.ACTION_RECEIVER_RECEIVED_TOKOCASH_DATA_ERROR)
        sendBroadcast(intentHomeFragment)
    }

    override fun onGetTopPoints(drawerTopPoints: SellerDrawerTopPoints) {
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.getOrNull(0) as? SellerDrawerHeader)?.sellerDrawerTopPoints = drawerTopPoints
        sellerDrawerHelper.notifyDataSetChanged()

    }

    override fun onErrorGetTopPoints(errorMessage: String) {

    }

    override fun onGetProfile(drawerProfile: SellerDrawerProfile) {
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.getOrNull(0) as? SellerDrawerHeader)?.sellerDrawerProfile = drawerProfile
        sellerDrawerHelper.notifyDataSetChanged()
        sellerDrawerHelper.setFooterData(drawerProfile)
        setupSellerHomeToolbarTitle(drawerProfile.shopName)
    }

    override fun onErrorGetProfile(errorMessage: String) {

    }

    override fun getActivity(): Activity = this

    override fun onErrorGetProfileCompletion(errorMessage: String) {

    }

    override fun onSuccessGetProfileCompletion(completion: Int) {
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.getOrNull(0) as? SellerDrawerHeader)?.profileCompletion = completion
        sellerDrawerHelper.notifyDataSetChanged()
    }

    override fun onSuccessGetTopChatNotification(notifUnreads: Int) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SellerDrawerHelper.REQUEST_LOGIN && requestCode == Activity.RESULT_OK)
            setDataDrawer()
    }

    override fun onResume() {
        super.onResume()
        updateDrawerData()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (drawerDataManager != null)
            drawerDataManager?.unsubscribe()
    }

    override fun onBackPressed() {
        if (sellerDrawerHelper.isOpened())
            sellerDrawerHelper.closeDrawer()
        super.onBackPressed()
    }

    open fun Toolbar.initNotificationMenu() {
        val notif = layoutInflater.inflate(R.layout.sah_custom_actionbar_drawer_notification, null)
        val drawerToggle = notif.findViewById<ImageView>(R.id.toggle_but_ab)
        drawerToggle.setOnClickListener {
            if (sellerDrawerHelper.isOpened())
                sellerDrawerHelper.closeDrawer()
            else sellerDrawerHelper.openDrawer()
        }
        this.addView(notif)
        this.navigationIcon = null
    }

    open fun Toolbar.initTitle() {
        toolbarTitle = layoutInflater.inflate(R.layout.custom_action_bar_title, null)
        this.addView(toolbarTitle)
    }

    open fun setToolbarTitle(title: String) {
        toolbar.actionbar_title.text = getTitle()
    }

    open fun setupToolbar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById<AppBarLayout>(R.id.app_bar_layout).outlineProvider = null
        }

        toolbar.apply {
            removeAllViews()
            initNotificationMenu()
            initTitle()
            background = ColorDrawable(ContextCompat.getColor(context, R.color.tkpd_main_green))
        }

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
        }
    }

    open fun setupDrawerStatusBar() {

    }

    protected open fun getParentViewLayoutId(): Int? {
        return null
    }

    protected open val isSellerHome = false

    private fun setupParentViewLayout() {
        val resId = getParentViewLayoutId()
        if (resId != null) {
            val inflatedView = View.inflate(this, resId, null)
            val frameLayout = findViewById<FrameLayout>(R.id.parent_view)
            frameLayout.addView(inflatedView)
        }
    }

    private fun setupSellerHomeToolbarTitle(shopName: String?) {
        val helloString = resources?.getString(R.string.seller_home_toolbar_title)
        val title = "$helloString$shopName"
        setToolbarTitle(title)
    }

}