package com.tokopedia.topupbills.telco.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import timber.log.Timber

/**
 * Created by nabillasabbaha on 11/04/19.
 *
 * applink
 * tokopedia-android-internal://digital/telco
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.TELCO_DIGITAL)
 */

class TelcoProductActivity : BaseTelcoActivity() {

    override fun getNewFragment(): Fragment {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID) ?: TelcoComponentType.TELCO_PREPAID.toString()
        digitalTelcoExtraParam.categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID) ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER) ?: ""
        return DigitalTelcoFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.digital_title_telco_page))
        intent?.handleExtra()
    }

    companion object {

        val PARAM_MENU_ID = "menu_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"
        val PARAM_CATEGORY_ID = "category_id"

        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"


        fun newInstance(context: Context, menuId: String, categoryId: String, productId: String = "", clientNumber: String = ""): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            intent.putExtra(PARAM_MENU_ID, menuId)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            intent.putExtra(PARAM_CLIENT_NUMBER, clientNumber)
            return intent
        }
    }

    override fun onBackPressed() {
        (fragment as DigitalTelcoFragment).onBackPressed()
        super.onBackPressed()
    }

    /* This Method is use to tracking Action click when user click TelcoProduct
    */

    private fun Intent.handleExtra(){
        if(intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            if (trackingClick!=null){
            Timber.w("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
            }
        }
    }
}