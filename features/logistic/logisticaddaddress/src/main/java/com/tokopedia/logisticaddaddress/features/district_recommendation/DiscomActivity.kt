package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.design.R
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking.onClickCloseKotaKecamatan
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.newInstance
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Irfan Khoirul on 17/11/18.
 * Deeplink: DISTRICT_RECOMMENDATION_SHOP_SETTINGS
 */
class DiscomActivity : BaseSimpleActivity(), HasComponent<Any?>, DiscomFragment.ActionListener {
    private var analytics: CheckoutAnalyticsChangeAddress? = null
    private var isLocalization = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = CheckoutAnalyticsChangeAddress()
        if (supportActionBar != null) {
            supportActionBar!!.elevation = 0f
            toolbar.setNavigationIcon(R.drawable.ic_close_thin)
        }
    }

    override fun getNewFragment(): Fragment? {
        val token = intent.getParcelableExtra<Token>(DiscomContract.Constant.ARGUMENT_DATA_TOKEN)
        isLocalization = intent.getBooleanExtra(DiscomContract.Constant.IS_LOCALIZATION, false)
        return if (token == null) {
            newInstance(isLocalization)
        } else {
            newInstance(token, isLocalization)
        }
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    override fun onBackPressed() {
        val userSession: UserSessionInterface = UserSession(this)
        if (isLocalization) onClickCloseKotaKecamatan(userSession.userId) else gtmOnBackPressClicked()
        super.onBackPressed()
    }

    override fun gtmOnBackPressClicked() {
        analytics!!.eventClickShippingCartChangeAddressClickXPojokKiriKotaAtauKecamatanPadaTambahAddress()
    }

    override fun gtmOnDistrictDropdownSelectionItemClicked(districtName: String) {
        analytics!!.eventClickShippingCartChangeAddressClickChecklistKotaAtauKecamatanPadaTambahAddress(districtName)
    }

    override fun gtmOnClearTextDistrictRecommendationInput() {
        analytics!!.eventClickShippingCartChangeAddressClickXPojokKananKotaAtauKecamatanPadaTambahAddress()
    }

    companion object {
        fun newInstance(activity: Activity?, token: Token?, isLocalization: Boolean?): Intent {
            val intent = Intent(activity, DiscomActivity::class.java)
            intent.putExtra(DiscomContract.Constant.ARGUMENT_DATA_TOKEN, token)
            intent.putExtra(DiscomContract.Constant.IS_LOCALIZATION, isLocalization)
            return intent
        }
    }
}
