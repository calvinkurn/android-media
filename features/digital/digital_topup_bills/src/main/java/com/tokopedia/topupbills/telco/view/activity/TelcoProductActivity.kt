package com.tokopedia.topupbills.telco.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoFragment
import com.tokopedia.topupbills.telco.view.model.DigitalTelcoExtraParam

/**
 * Created by nabillasabbaha on 11/04/19.
 */
class TelcoProductActivity : BaseTelcoActivity() {

    override fun getNewFragment(): Fragment {
        val digitalTelcoExtraParam = DigitalTelcoExtraParam()
        val bundle = intent.extras
        if (!TextUtils.isEmpty(bundle.getString(PARAM_MENU_ID))) {
            digitalTelcoExtraParam.menuId = bundle.getString(PARAM_MENU_ID)
        }
        if (!TextUtils.isEmpty(bundle.getString(PARAM_CATEGORY_ID))) {
            digitalTelcoExtraParam.categoryId = bundle.getString(PARAM_CATEGORY_ID)
        }
        if (!TextUtils.isEmpty(bundle.getString(PARAM_PRODUCT_ID))) {
            digitalTelcoExtraParam.productId = bundle.getString(PARAM_PRODUCT_ID)
        }
        if (!TextUtils.isEmpty(bundle.getString(PARAM_CLIENT_NUMBER))) {
            digitalTelcoExtraParam.clientNumber = bundle.getString(PARAM_CLIENT_NUMBER)
        }
        return DigitalTelcoFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.digital_title_telco_page))
    }

    companion object {

        val PARAM_MENU_ID = "menu_id"
        val PARAM_PRODUCT_ID = "product_id"
        val PARAM_CLIENT_NUMBER = "client_number"
        val PARAM_CATEGORY_ID = "category_id"

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, TelcoProductActivity::class.java)
            return intent
        }

    }

    override fun onBackPressed() {
        finish()
    }
}