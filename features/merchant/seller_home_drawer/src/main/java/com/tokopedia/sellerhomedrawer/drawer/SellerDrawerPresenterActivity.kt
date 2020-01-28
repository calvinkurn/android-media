package com.tokopedia.sellerhomedrawer.drawer

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.constant.SellerDrawerActivityBroadcastReceiverConstant
import com.tokopedia.sellerhomedrawer.constant.SellerHomeFragmentBroadcastReceiverConstant
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerDeposit
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerTopPoints
import com.tokopedia.sellerhomedrawer.di.DaggerSellerHomeDashboardComponent
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardComponent
import com.tokopedia.sellerhomedrawer.di.module.SellerHomeDashboardModule
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManager
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManagerImpl
import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader.SellerDrawerHeader
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sh_custom_action_bar_title.view.*
import kotlinx.android.synthetic.main.sh_custom_actionbar_drawer_notification.view.*
import rx.Observable
import javax.inject.Inject

abstract class SellerDrawerPresenterActivity : BaseSimpleActivity(),
        SellerDrawerDataListener
{
    private val MAX_NOTIF = 999
    private val HELLO_STRING = "Halo, "

    lateinit var sellerDrawerHelper: SellerDrawerHelper
    lateinit var userSession: UserSession
    lateinit var drawerCache: LocalCacheHandler
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    lateinit var toolbarTitle: View

    protected var drawerDataManager: SellerDrawerDataManager? = null
    private var isLogin: Boolean? = null
    private var drawerActivityBroadcastReceiver: BroadcastReceiver? = null
    private var broadcastReceiverTokoPoint: BroadcastReceiver? = null
    private var broadcastReceiverPendingTokocash: BroadcastReceiver? = null

    @Inject
    lateinit var getSellerHomeUserAttributesUseCase: GetSellerHomeUserAttributesUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(applicationContext)
        drawerCache = LocalCacheHandler(this, SellerDrawerHelper.DRAWER_CACHE)
        remoteConfig = FirebaseRemoteConfigImpl(this)

        injectDependency()
        setupDrawer()
        setupToolbar()
    }

    fun injectDependency() {
        val component: SellerHomeDashboardComponent = DaggerSellerHomeDashboardComponent.builder()
                .sellerHomeDashboardModule(SellerHomeDashboardModule(this))
                .build()
        component.inject(this)
    }

    fun setupDrawer() {

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
        sellerDrawerHelper.sellerDrawerAdapter?.drawerItemData?.clear()
        val dataDrawer = sellerDrawerHelper.createDrawerData()
        sellerDrawerHelper.sellerDrawerAdapter?.drawerItemData = dataDrawer

    }

    override fun getLayoutRes(): Int {
        return R.layout.sh_drawer_activity
    }


//    override fun getContentId(): Int {
//        return R.layout.sh_drawer_activity
//    }

    override fun onErrorGetDeposit(errorMessage: String) {

    }

    override fun onErrorGetNotificationDrawer(errorMessage: String) {
        setDataDrawer()
    }

    override fun onErrorGetNotificationTopchat(errorMessage: String) {

    }

    override fun onGetDeposit(drawerDeposit: SellerDrawerDeposit) {

        if (sellerDrawerHelper.sellerDrawerAdapter?.list?.get(0) is SellerDrawerHeader) {
            (sellerDrawerHelper.sellerDrawerAdapter?.list?.get(0) as SellerDrawerHeader).sellerDrawerDeposit = drawerDeposit
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
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.get(0) as SellerDrawerHeader).sellerDrawerTopPoints = drawerTopPoints
        sellerDrawerHelper.notifyDataSetChanged()

    }

    override fun onErrorGetTopPoints(errorMessage: String) {

    }

    override fun onGetProfile(drawerProfile: SellerDrawerProfile) {
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.get(0) as SellerDrawerHeader).sellerDrawerProfile = drawerProfile
        sellerDrawerHelper.notifyDataSetChanged()
        sellerDrawerHelper.setFooterData(drawerProfile)

        val title = HELLO_STRING + drawerProfile.userName
        setToolbarTitle(title)
    }

    override fun onErrorGetProfile(errorMessage: String) {

    }

    override fun getActivity(): Activity = this

    override fun onErrorGetProfileCompletion(errorMessage: String) {

    }

    override fun onSuccessGetProfileCompletion(completion: Int) {
        (sellerDrawerHelper.sellerDrawerAdapter?.list?.get(0) as SellerDrawerHeader).profileCompletion = completion
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

    private fun Toolbar.initNotificationMenu() {
        val notif = layoutInflater.inflate(R.layout.sh_custom_actionbar_drawer_notification, null)
        val drawerToggle = notif.findViewById<ImageView>(R.id.toggle_but_ab)
        drawerToggle.setOnClickListener {
            if (sellerDrawerHelper.isOpened())
                sellerDrawerHelper.closeDrawer()
            else sellerDrawerHelper.openDrawer()
        }
        this.addView(notif)
        this.navigationIcon = null
    }

    private fun Toolbar.initTitle() {
        toolbarTitle = layoutInflater.inflate(R.layout.sh_custom_action_bar_title, null)
        toolbarTitle.actionbar_title.text = getTitle()
        this.addView(toolbarTitle)
    }

    protected fun setToolbarTitle(title: String) {
        toolbarTitle.actionbar_title.text = title
    }

    private fun setupToolbar() {
        toolbar.apply {
            removeAllViews()
            initNotificationMenu()
            initTitle()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
        }
    }


}