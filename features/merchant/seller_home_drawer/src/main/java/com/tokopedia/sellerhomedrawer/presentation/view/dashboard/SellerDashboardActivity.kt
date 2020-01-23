package com.tokopedia.sellerhomedrawer.presentation.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.core.ManageGeneral
import com.tokopedia.core.gcm.FCMCacheManager
import com.tokopedia.core.gcm.NotificationModHandler
import com.tokopedia.core.util.SessionHandler
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.constant.SellerHomeState
import com.tokopedia.sellerhomedrawer.data.GoldGetPmOsStatus
import com.tokopedia.sellerhomedrawer.drawer.BaseSellerReceiverDrawerActivity
import com.tokopedia.sellerhomedrawer.firebase.SellerFirebaseRemoteAppUpdate
import com.tokopedia.sellerhomedrawer.presentation.view.SellerHomeDashboardContract
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import kotlinx.android.synthetic.main.sh_drawer_layout.*
import javax.inject.Inject

class SellerDashboardActivity<T>: BaseSellerReceiverDrawerActivity<T>(), SellerHomeDashboardContract.View{

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

        inflateView(R.layout.sh_activity_simple_fragment)
        if (savedInstanceState != null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SellerDashboardFragment.newInstance(), TAG)
                    .commit()
        }

        checkAppUpdate()

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val context: Context
        get() = this

    override fun getLayoutId(): Int = 0

    override fun updateDrawerData() {
        if (userSession.isLoggedIn)
            setDataDrawer()
    }

    private fun initInjector() {
        //TODO: Inject

        sellerHomeDashboardDrawerPresenter.attachView(this)
    }
}