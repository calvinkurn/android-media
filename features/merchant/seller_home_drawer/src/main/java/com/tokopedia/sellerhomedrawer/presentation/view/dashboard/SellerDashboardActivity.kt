package com.tokopedia.sellerhomedrawer.presentation.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.core.ManageGeneral
import com.tokopedia.core.gcm.FCMCacheManager
import com.tokopedia.core.gcm.NotificationModHandler
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.di.DaggerSellerHomeDashboardComponent
import com.tokopedia.sellerhomedrawer.di.module.SellerHomeDashboardModule
import com.tokopedia.sellerhomedrawer.drawer.BaseSellerReceiverDrawerActivity
import com.tokopedia.sellerhomedrawer.firebase.SellerFirebaseRemoteAppUpdate
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.sh_drawer_layout.*
import javax.inject.Inject

class SellerDashboardActivity: BaseSellerReceiverDrawerActivity(), SellerHomeDashboardContract.View{

    companion object {
        @JvmStatic
        val TAG = SellerDashboardActivity::class.java.simpleName

        @JvmStatic
        fun createInstance(context: Context) = Intent(context, SellerDashboardActivity::class.java)
    }

    @Inject
    lateinit var sellerHomeDashboardDrawerPresenter: SellerHomeDashboardDrawerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        sellerHomeDashboardDrawerPresenter.attachView(this)

//        inflateView(R.layout.sh_activity_simple_fragment)
        if (savedInstanceState != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SellerHomeFragment.newInstance(), TAG)
                    .commit()
        }

        checkAppUpdate()

    }

    override fun getNewFragment(): Fragment? {
        return SellerDashboardFragment.newInstance()
    }

    override fun onResume() {
        sellerHomeDashboardDrawerPresenter.attachView(this)
        super.onResume()

        //TODO: Remove core woi
        FCMCacheManager.checkAndSyncFcmId(applicationContext)
        NotificationModHandler.showDialogNotificationIfNotShowing(this,
                ManageGeneral.getCallingIntent(this, ManageGeneral.TAB_POSITION_MANAGE_APP))
    }

    override fun setDrawerPosition(): Int {
        return SellerHomeState.DrawerPosition.SELLER_INDEX_HOME
    }

    override fun onSuccessGetFlashSaleSellerStatus(isVisible: Boolean) {
        val sellerDrawerAdapter = sellerDrawerHelper.sellerDrawerAdapter
        if (isVisible != sellerDrawerAdapter?.isFlashSaleVisible) {
            sellerDrawerAdapter?.isFlashSaleVisible = isVisible
            left_drawer.post {
                sellerDrawerAdapter?.renderFlashSaleDrawer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sellerHomeDashboardDrawerPresenter.unsubscribe()
    }



    private fun checkAppUpdate() {
        val appUpdate: ApplicationUpdate = SellerFirebaseRemoteAppUpdate(this)
        appUpdate.checkApplicationUpdate(object : ApplicationUpdate.OnUpdateListener {
            override fun onNeedUpdate(detail: DetailUpdate?) {
                if (detail != null && !isFinishing) {
                    AppUpdateDialogBuilder(
                            this@SellerDashboardActivity,
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
    }

    override val context: Context
        get() = this

    override fun updateDrawerData() {
        if (userSession.isLoggedIn) {
            setDataDrawer()
            getDrawerSellerAttrUseCase(userSession)
            sellerHomeDashboardDrawerPresenter.getFlashSaleSellerStatus()
            sellerHomeDashboardDrawerPresenter.isGoldMerchantAsync()
        }

    }

    private fun getDrawerSellerAttrUseCase(userSession: UserSession) {
        drawerDataManager?.getSellerUserAttributes(userSession)
    }

    private fun initInjector() {

        //TODO: Inject
        val component = DaggerSellerHomeDashboardComponent.builder()
                .sellerHomeDashboardModule(SellerHomeDashboardModule(this))
                .build()
        component.inject(this)

        sellerHomeDashboardDrawerPresenter.attachView(this)
    }
}