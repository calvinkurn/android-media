package com.tokopedia.sellerhome.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.domain.firebase.SellerFirebaseRemoteAppUpdate
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.sellerhomedrawer.presentation.view.drawer.BaseSellerReceiverDrawerActivity
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class SellerHomeActivity: BaseSellerReceiverDrawerActivity(), SellerHomeDashboardContract.View{

    companion object {
        @JvmStatic
        val TAG = SellerHomeActivity::class.java.simpleName

        @JvmStatic
        fun createInstance(context: Context) = Intent(context, SellerHomeActivity::class.java)
    }

//    @Inject
    lateinit var sellerHomeDashboardDrawerPresenter: SellerHomeDashboardDrawerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        sellerHomeDashboardDrawerPresenter.attachView(this)

        if (savedInstanceState != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SellerHomeFragment.newInstance(), TAG)
                    .commit()
        }

        checkAppUpdate()

    }

    override fun getNewFragment(): Fragment? {
        return SellerHomeFragment.newInstance()
    }

    override fun onResume() {
        sellerHomeDashboardDrawerPresenter.attachView(this)
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
        sellerHomeDashboardDrawerPresenter.unsubscribe()
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

//        val component = DaggerSellerHomeDashboardComponent.builder()
//                .sellerHomeDashboardModule(SellerHomeDashboardModule(this))
//                .build()
//        component.inject(this)

        //Dagger injecting still fails, will do manual injection
        val userSession: UserSessionInterface = UserSession(context)
        val graphqlUseCase = GraphqlUseCase()
        val getShopStatusUseCase = GetShopStatusUseCase(graphqlUseCase, GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_status))
        val flashSaleGetSellerStatusUseCase = FlashSaleGetSellerStatusUseCase(graphqlUseCase)
        sellerHomeDashboardDrawerPresenter = SellerHomeDashboardDrawerPresenter(getShopStatusUseCase, flashSaleGetSellerStatusUseCase, userSession)

        sellerHomeDashboardDrawerPresenter.attachView(this)
    }
}