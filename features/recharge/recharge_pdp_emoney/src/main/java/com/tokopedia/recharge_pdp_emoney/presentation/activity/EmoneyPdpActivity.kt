package com.tokopedia.recharge_pdp_emoney.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.common_digital.common.presentation.model.DigitalCategoryDetailPassData
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.di.DaggerEmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.di.EmoneyPdpComponent
import com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet.EmoneyMenuBottomSheets
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class EmoneyPdpActivity : BaseSimpleActivity(), HasComponent<EmoneyPdpComponent>,
        EmoneyMenuBottomSheets.MenuListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    var promoCode = ""
    private var rechargeParamFromSlice = ""

    private var passData: DigitalCategoryDetailPassData? = null

    lateinit var menuEmoney: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        val uriData = intent.data
        if (intent.extras != null && intent.extras?.getParcelable<Parcelable>(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA) != null) {
            passData = intent.extras?.getParcelable(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA)
        } else {
            var isFromWidget = false
            if (!TextUtils.isEmpty(uriData!!.getQueryParameter(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET))) {
                isFromWidget = java.lang.Boolean.valueOf(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_IS_FROM_WIDGET))
            }
            var isCouponApplied = false
            if (!TextUtils.isEmpty(uriData.getQueryParameter(KEY_IS_COUPON_APPLIED_APPLINK))) {
                isCouponApplied = Objects.requireNonNull(uriData.getQueryParameter(KEY_IS_COUPON_APPLIED_APPLINK)) == "1"
            }
            val passData = DigitalCategoryDetailPassData.Builder()
                    .appLinks(uriData.toString())
                    .categoryId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_CATEGORY_ID))
                    .operatorId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_OPERATOR_ID))
                    .productId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_PRODUCT_ID))
                    .clientNumber(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_CLIENT_NUMBER))
                    .menuId(uriData.getQueryParameter(DigitalCategoryDetailPassData.PARAM_MENU_ID))
                    .isFromWidget(isFromWidget)
                    .isCouponApplied(isCouponApplied)
                    .build()
            this.passData = passData
        }
        if (intent.data != null) {
            rechargeParamFromSlice = intent.getStringExtra(EXTRA_RECHARGE_SLICE) ?: ""
        }
        super.onCreate(savedInstanceState)

        toolbar.elevation = 0f
    }

    override fun getNewFragment(): Fragment {
        passData?.let { return EmoneyPdpFragment.newInstance(it) }
        return EmoneyPdpFragment()
    }

    override fun getComponent(): EmoneyPdpComponent {
        val component = DaggerEmoneyPdpComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
        component.inject(this)
        return component
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

    override fun getLayoutRes(): Int {
        return R.layout.activity_emoney
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.emoney_toolbar
    }

    companion object {
        const val REQUEST_CODE_LOGIN_EMONEY = 10000

        const val TAG_EMONEY_MENU = "menu_emoney"
        const val PATH_SUBSCRIPTIONS = "subscribe/"

        private val KEY_IS_COUPON_APPLIED_APPLINK = "is_coupon_applied"
        private val EXTRA_RECHARGE_SLICE = "RECHARGE_PRODUCT_EXTRA"

        fun newInstance(context: Context, passData: DigitalCategoryDetailPassData): Intent {
            return Intent(context, EmoneyPdpActivity::class.java)
                    .putExtra(DigitalExtraParam.EXTRA_CATEGORY_PASS_DATA, passData)
        }
    }
}