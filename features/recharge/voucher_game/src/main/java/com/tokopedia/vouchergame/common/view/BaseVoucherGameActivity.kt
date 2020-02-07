package com.tokopedia.vouchergame.common.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.common.di.DaggerVoucherGameComponent
import com.tokopedia.vouchergame.common.di.VoucherGameComponent
import com.tokopedia.vouchergame.common.view.widget.VoucherGameMenuBottomSheets
import javax.inject.Inject

/**
 * @author by resakemal on 26/08/19
 */
abstract class BaseVoucherGameActivity: BaseSimpleActivity(), VoucherGameMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
    }

    private fun initInjector() {
        getVoucherGameComponent().inject(this)
    }

    protected fun getVoucherGameComponent(): VoucherGameComponent {
        return DaggerVoucherGameComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        if (shouldShowOptionMenu()) {
            menuInflater.inflate(R.menu.voucher_game_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (shouldShowOptionMenu()) {
            if (item?.itemId ?: "" == R.id.action_overflow_menu) {
                showBottomMenus()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        if (userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
        } else {
            RouteManager.route(this, ApplinkConst.LOGIN)
        }
    }

    override fun onPromoClicked() {
        RouteManager.route(this, ApplinkConst.PROMO_LIST)
    }

    override fun onHelpClicked() {
        RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
    }

    private fun showBottomMenus() {
        val voucherGameMenuBottomSheets = VoucherGameMenuBottomSheets()
        voucherGameMenuBottomSheets.listener = this
        voucherGameMenuBottomSheets.show(supportFragmentManager, TAG_VOUCHER_GAME_MENU)
    }

    abstract fun shouldShowOptionMenu(): Boolean

    companion object {
        val TAG_VOUCHER_GAME_MENU = "voucherGameMenu"
    }
}