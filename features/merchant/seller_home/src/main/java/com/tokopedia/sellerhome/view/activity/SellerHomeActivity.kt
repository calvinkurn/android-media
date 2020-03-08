package com.tokopedia.sellerhome.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.data.ShopStatusModel
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.domain.firebase.SellerFirebaseRemoteAppUpdate
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.presentation.view.BaseSellerReceiverDrawerActivity
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class SellerHomeActivity: BaseSellerReceiverDrawerActivity(), SellerHomeDashboardContract.View,
        SellerHomeFragment.PageRefreshListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

    private val sellerHomeFragment by lazy { SellerHomeFragment.newInstance() }
    private var sellerHomeDashboardDrawerPresenter: SellerHomeDashboardDrawerPresenter? = null

    override val isSellerHome: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        checkAppUpdate()

        sellerHomeFragment.setOnPageRefreshedListener(this)
    }

    override fun getNewFragment(): Fragment? {
        return OtherMenuFragment.createInstance()
    }

    override fun onResume() {
        sellerHomeDashboardDrawerPresenter?.attachView(this)
        super.onResume()
    }

    override fun setDrawerPosition(): Int {
        return SellerHomeState.DrawerPosition.SELLER_INDEX_HOME
    }

    override fun onSuccessGetFlashSaleSellerStatus(isVisible: Boolean) {
        val sellerDrawerAdapter = sellerDrawerHelper.sellerDrawerAdapter
        if (isVisible != sellerDrawerAdapter?.isFlashSaleVisible) {
            sellerDrawerAdapter?.isFlashSaleVisible = isVisible
            findViewById<RecyclerView>(R.id.left_drawer).post {
                sellerDrawerAdapter?.renderFlashSaleDrawer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sellerHomeDashboardDrawerPresenter?.unsubscribe()
    }

    override fun onRefreshPage() {
        updateDrawerData()
    }

    private fun checkAppUpdate() {
        val appUpdate: ApplicationUpdate = SellerFirebaseRemoteAppUpdate(this)
        appUpdate.checkApplicationUpdate(object : ApplicationUpdate.OnUpdateListener {
            override fun onNeedUpdate(detail: DetailUpdate?) {
                if (detail != null && !isFinishing) {
                    AppUpdateDialogBuilder(
                            this@SellerHomeActivity,
                            detail,
                            object : AppUpdateDialogBuilder.Listener {
                                override fun onPositiveButtonClicked(detail: DetailUpdate?) {

                                }

                                override fun onNegativeButtonClicked(detail: DetailUpdate?) {

                                }
                            }).alertDialog.show()
                }
            }

            override fun onError(e: Exception?) {
                e?.printStackTrace()
            }

            override fun onNotNeedUpdate() {

            }
        })
    }

    override fun onSuccessGetShopInfo(goldGetPmOsStatus: GoldGetPmOsStatus) {
        val shopStatusModel = goldGetPmOsStatus.result.data
        val sellerDrawerAdapter = sellerDrawerHelper.sellerDrawerAdapter
        val isGoldMerchant = shopStatusModel.isPowerMerchantActive()
        val isOfficialStore = shopStatusModel.isOfficialStore()
        sellerDrawerAdapter?.isGoldMerchant = isGoldMerchant
        sellerDrawerAdapter?.isOfficialStore = isOfficialStore
        userSession.setIsGoldMerchant(isGoldMerchant)
        setShopStatus(shopStatusModel)
    }

    private fun setShopStatus(shopStatus: ShopStatusModel) {
        val mShopStatus: ShopStatus = when {
            shopStatus.isOfficialStore() -> ShopStatus.OFFICIAL_STORE
            shopStatus.isPowerMerchantActive() || shopStatus.isPowerMerchantIdle() -> ShopStatus.POWER_MERCHANT
            else -> ShopStatus.REGULAR_MERCHANT
        }

        sellerHomeFragment.setShopStatus(mShopStatus)
    }

    override fun updateDrawerData() {
        if (userSession.isLoggedIn) {
            setDataDrawer()
            getDrawerSellerAttrUseCase(userSession)
            sellerHomeDashboardDrawerPresenter?.getFlashSaleSellerStatus()
            sellerHomeDashboardDrawerPresenter?.isGoldMerchantAsync()
        }

    }

    private fun getDrawerSellerAttrUseCase(userSession: UserSessionInterface) {
        drawerDataManager?.getSellerUserAttributes(userSession)
    }

    private fun initInjector() {
        //Dagger injecting still fails, will do manual instantiation
        val userSession: UserSessionInterface = UserSession(this)
        val graphqlUseCase = GraphqlUseCase()
        val getShopStatusUseCase = GetShopStatusUseCase(graphqlUseCase, GraphqlHelper.loadRawString(resources, R.raw.gold_merchant_status))
        val flashSaleGetSellerStatusUseCase = FlashSaleGetSellerStatusUseCase(graphqlUseCase)
        sellerHomeDashboardDrawerPresenter = SellerHomeDashboardDrawerPresenter(getShopStatusUseCase, flashSaleGetSellerStatusUseCase, userSession, this)

        sellerHomeDashboardDrawerPresenter?.attachView(this)
    }

    override fun setupToolbar() {
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

    override fun setupDrawerStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(context, com.tokopedia.design.R.color.white_95)
            }
        }
    }

    override fun setToolbarTitle(title: String) {
        toolbar.findViewById<TextView>(R.id.actionbar_title).text = title
    }

    override fun Toolbar.initNotificationMenu() {
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

    override fun Toolbar.initTitle() {
        toolbarTitle = layoutInflater.inflate(R.layout.sh_custom_action_bar_title, null)
        this.addView(toolbarTitle)
    }

}