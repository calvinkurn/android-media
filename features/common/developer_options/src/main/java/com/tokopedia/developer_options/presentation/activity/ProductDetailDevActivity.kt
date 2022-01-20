package com.tokopedia.developer_options.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Yehezkiel on 19/08/21
 */
class ProductDetailDevActivity : BaseActivity() {

    companion object {
        const val PDP_LAYOUT_ID_TEST_SHARED_PREF_KEY = "layoutIdSharedPref"
        const val PDP_LAYOUT_ID_STRING_KEY = "layoutIdTest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (GlobalConfig.isAllowDebuggingTools()) {
            setContentView(R.layout.activity_product_detail_dev)
            setupView()
        } else {
            finish()
        }
    }

    private fun setupView() {
        val layoutIdEditText = findViewById<TextFieldUnify>(R.id.pdp_layout_id_edittext)
        findViewById<UnifyButton>(R.id.pdp_layout_id_btn).setOnClickListener { v: View? ->
            getSharedPreferences(PDP_LAYOUT_ID_TEST_SHARED_PREF_KEY, MODE_PRIVATE).edit()
                    .putString(PDP_LAYOUT_ID_STRING_KEY, layoutIdEditText.textFieldInput.text.toString()).apply()

            val messageSuccess = "Layout ID PDP Applied : " + layoutIdEditText.textFieldInput.text.toString()
            Toast.makeText(this@ProductDetailDevActivity, messageSuccess, Toast.LENGTH_SHORT).show()
        }

        val productIdEditText = findViewById<TextFieldUnify>(R.id.pdp_productid_text)
        findViewById<UnifyButton>(R.id.pdp_route_to_pdp_btn).setOnClickListener {
            RouteManager.route(this, ApplinkConst.PRODUCT_INFO, productIdEditText.textFieldInput.text.toString())
        }

        val productIdVbsEditText = findViewById<TextFieldUnify>(R.id.pdp_productid_vbs)
        val shopIdVbsEditText = findViewById<TextFieldUnify>(R.id.pdp_shopid_vbs)
        val pageSourceVbsEditText = findViewById<TextFieldUnify>(R.id.pdp_pagesource_vbs)
        val toggleTokoNow = findViewById<CheckBox>(R.id.toggle_is_tokonow)

        findViewById<UnifyButton>(R.id.pdp_vbs_btn).setOnClickListener {
            val isTokonow = toggleTokoNow.isChecked

            val intent = RouteManager.getIntent(this,
                    ApplinkConstInternalMarketplace.ATC_VARIANT,
                    productIdVbsEditText.textFieldInput.text.toString(),
                    shopIdVbsEditText.textFieldInput.text.toString(),
                    pageSourceVbsEditText.textFieldInput.text.toString(),
                    isTokonow.toString(),
                    "")

            startActivityForResult(intent, 19202)
        }
    }
}