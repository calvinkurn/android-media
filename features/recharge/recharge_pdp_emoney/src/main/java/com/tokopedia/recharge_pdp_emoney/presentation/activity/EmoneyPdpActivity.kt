package com.tokopedia.recharge_pdp_emoney.presentation.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.DaggerEmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet.EmoneyMenuBottomSheets
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class EmoneyPdpActivity : BaseSimpleActivity(), HasComponent<EmoneyPdpComponent>,
        EmoneyMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface
    var promoCode = ""

    lateinit var menuEmoney: Menu

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID)?.toIntOrNull() ?: 0
        val menuId = bundle?.getString(PARAM_MENU_ID)?.toIntOrNull() ?: 0
        val operatorId = bundle?.getString(PARAM_OPERATOR_ID)?.toIntOrNull() ?: 0
        val productId = bundle?.getString(PARAM_PRODUCT_ID)?.toIntOrNull() ?: 0
        val clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER) ?: ""
        return EmoneyPdpFragment.newInstance(categoryId, menuId, operatorId, productId, clientNumber)
    }

    override fun getComponent(): EmoneyPdpComponent {
        return DaggerEmoneyPdpComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.elevation = 0f
    }

    private fun showBottomMenus() {
        val menuBottomSheet = EmoneyMenuBottomSheets.newInstance()
        menuBottomSheet.listener = this
        menuBottomSheet.setShowListener {
            menuBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        menuBottomSheet.show(supportFragmentManager, TAG_EMONEY_MENU)
    }

    override fun onMenuOpened(featureId: Int, menu: Menu?): Boolean {
        showBottomMenus()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuEmoney = menu
            menuInflater.inflate(R.menu.menu_emoney, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ?: "" == R.id.emoney_action_overflow_menu) {
            showBottomMenus()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListClicked() {
        if (userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConst.DIGITAL_ORDER)
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_EMONEY)
        }
    }

    override fun onSubscriptionLanggananClicked() {
        RouteManager.route(this, TokopediaUrl.getInstance().PULSA + PATH_SUBSCRIPTIONS)
    }

    override fun onHelpClicked() {
        RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
    }

    companion object {
        val PARAM_CATEGORY_ID = "category_id"
        val PARAM_MENU_ID = "menu_id"
        val PARAM_OPERATOR_ID = "operator_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"

        const val REQUEST_CODE_LOGIN_EMONEY = 10000

        const val TAG_EMONEY_MENU = "menu_emoney"
        const val PATH_SUBSCRIPTIONS = "subscribe/"
    }
}