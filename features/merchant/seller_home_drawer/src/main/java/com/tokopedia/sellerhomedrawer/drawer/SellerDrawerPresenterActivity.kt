package com.tokopedia.sellerhomedrawer.drawer

import android.app.Activity
import android.content.BroadcastReceiver
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.core.app.BasePresenterActivity
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.helper.SellerHomeDrawerHelper
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sh_custom_action_bar_title.view.*
import kotlinx.android.synthetic.main.sh_drawer_activity.*

open class SellerDrawerPresenterActivity<T> : BasePresenterActivity<T>()
{
    private val MAX_NOTIF = 999

    lateinit var sellerDrawerHelper: SellerDrawerHelper
    lateinit var userSession: UserSession
    lateinit var drawerCache: LocalCacheHandler
    protected var drawerDataManager: DrawerDataManager? = null
    private var isLogin: Boolean? = null
    private var drawerActivityBroadcastReceiver: BroadcastReceiver? = null
    private var broadcastReceiverTokoPoint: BroadcastReceiver? = null
    private var broadcastReceiverPendingTokocash: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO : Is this context true ?
        userSession = UserSession(applicationContext)
        drawerCache = LocalCacheHandler(this, SellerHomeDrawerHelper.DRAWER_CACHE)
        setupDrawer()
    }

//    override fun getNewFragment(): Fragment? {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }

    fun setupDrawer() {
        //TODO : Inject these
        sellerDrawerHelper = SellerDrawerHelper(this, userSession, drawerCache)
        sellerDrawerHelper.initDrawer(this)
    }

    fun getDrawerHelper(): SellerHomeDrawerHelper? = null

    fun getActivity() : Activity = this

    protected open fun updateDrawerData() {
        if (userSession.isLoggedIn) {
            setDataDrawer()
//            getDrawerSellerAttrUseCase(userSession)
        }
    }

    protected fun setDataDrawer() {
        sellerDrawerHelper.sellerDrawerAdapter?.drawerItemData?.clear()
        val dataDrawer = sellerDrawerHelper.createDrawerData()
        sellerDrawerHelper.sellerDrawerAdapter?.drawerItemData = dataDrawer

    }

    override fun initVar() {

    }

    override fun initView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun initialPresenter() {
    }

    override fun setActionVar() {

    }

    override fun setViewListener() {

    }

    override fun setupBundlePass(extras: Bundle?) {

    }

    override fun setupURIPass(data: Uri?) {

    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun getContentId(): Int {
        return R.layout.sh_drawer_activity
    }

    override fun setupToolbar() {
        app_bar.apply {
            removeAllViews()
            initNotificationMenu()
            initTitle()
        }
        setSupportActionBar(app_bar)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
            setHomeButtonEnabled(false)
        }
    }


    private fun Toolbar.initNotificationMenu() {
        val notif = layoutInflater.inflate(R.layout.sh_custom_actionbar_drawer_notification, app_bar, false)
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
        val title = layoutInflater.inflate(R.layout.sh_custom_action_bar_title, app_bar, false)
        title.actionbar_title.text = getTitle()
        this.addView(title)
    }
}