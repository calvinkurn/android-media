package com.tokopedia.developer_options.presentation.activity

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.developer_options.R
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Yehezkiel on 19/08/21
 */
class ProductDetailDevActivity : BaseActivity() {

    companion object {
        const val PDP_LAYOUT_ID_TEST_SHARED_PREF_KEY = "layoutIdSharedPref"
        const val PDP_LAYOUT_ID_STRING_KEY = "layoutIdTest"

        const val PDP_COMPONENT_FILTER_SHARED_PREF_KEY = "componentFilterSharedPref"
        const val PDP_COMPONENT_FILTER_VALUE = "componentFilterValue"
        const val PDP_COMPONENT_FILTER_CONDITION = "componentFilterCondition"
        const val PDP_COMPONENT_FILTER_OPTION = "componentFilterOption"
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
        val saveAfterCloseEditText = findViewById<TextFieldUnify>(R.id.pdp_save_after_close_atc_vbs)
        val dismissAfterAtc = findViewById<TextFieldUnify>(R.id.pdp_dismiss_after_atc_vbs)
        val extParamsEditText = findViewById<TextFieldUnify>(R.id.pdp_ext_param_vbs)
        val toggleTokoNow = findViewById<CheckBox>(R.id.toggle_is_tokonow)
        val toggleQtyEditor = findViewById<CheckBox>(R.id.toggle_show_qty_editor)
        val spinnerPageSource = findViewById<Spinner>(R.id.page_source_spinner)

        val listOfPageSource: List<String> = VariantPageSource.values().toList().map {
            it.source
        }

        val envSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.customized_spinner_item, listOfPageSource)
        spinnerPageSource?.adapter = envSpinnerAdapter
        spinnerPageSource?.setSelection(0)

        findViewById<UnifyButton>(R.id.pdp_vbs_btn).setOnClickListener {
            val isTokonow = toggleTokoNow.isChecked
            val showQtyEditor = toggleQtyEditor.isChecked
            val dismissAfterAtcData = dismissAfterAtc.textFieldInput.text.toString().toBoolean()
            val saveAfterClose = saveAfterCloseEditText.textFieldInput.text.toString().toBoolean()

            val productId = productIdVbsEditText.textFieldInput.text.toString()
            val extParams = extParamsEditText.textFieldInput.text.toString()
            val shopId = shopIdVbsEditText.textFieldInput.text.toString()
            val pageSource = VariantPageSource.values().firstOrNull {
                it.source == spinnerPageSource.selectedItem.toString()
            } ?: VariantPageSource.PDP_PAGESOURCE

            AtcVariantHelper.goToAtcVariant(
                    this,
                    productId = productId,
                    pageSource = pageSource,
                    isTokoNow = isTokonow,
                    shopId = shopId,
                    trackerCdListName = "",
                    dismissAfterTransaction = dismissAfterAtcData,
                    saveAfterClose = saveAfterClose,
                    extParams = extParams,
                    showQuantityEditor = showQtyEditor,
                    startActivitResult = { data, _ ->
                        startActivity(data)
                    }
            )
        }

        val educationalType = findViewById<TextFieldUnify>(R.id.pdp_educational_type)
        findViewById<UnifyButton>(R.id.pdp_educational_btn).setOnClickListener {
            val typeData = educationalType.textFieldInput.text.toString()
            RouteManager.route(this, ApplinkConst.PRODUCT_EDUCATIONAL, typeData)
        }

        val componentSharedPref = getSharedPreferences(PDP_COMPONENT_FILTER_SHARED_PREF_KEY, MODE_PRIVATE)
        val savedComponentValue = componentSharedPref.getString(PDP_COMPONENT_FILTER_VALUE, "")
        val savedComponentCondition = componentSharedPref.getString(PDP_COMPONENT_FILTER_CONDITION, "filter out")
        val savedComponentOption = componentSharedPref.getString(PDP_COMPONENT_FILTER_OPTION, "type")

        val componentValue = findViewById<TextFieldUnify>(R.id.pdp_component_name)
        val componentConditionFilter = findViewById<RadioButton>(R.id.pdp_component_condition_filter)
        val componentConditionOnly = findViewById<RadioButton>(R.id.pdp_component_condition_only)
        val componentOptionType = findViewById<RadioButton>(R.id.pdp_component_filter_type)
        val componentOptionName = findViewById<RadioButton>(R.id.pdp_component_filter_name)

        if (savedComponentValue?.isNotBlank() == true) {
            componentValue.textFieldInput.setText(savedComponentValue)
        }

        if (savedComponentCondition.equals("filter out", true)) {
            componentConditionFilter.isChecked = true
        } else if (savedComponentCondition.equals("show only", true)) {
            componentConditionOnly.isChecked = true
        }

        if(savedComponentOption.equals("type", true)){
            componentOptionType.isChecked = true
        } else if(savedComponentOption.equals("name", true)){
            componentOptionName.isChecked = true
        }

        findViewById<UnifyButton>(R.id.pdp_component_btn).setOnClickListener {

            val checkedConditionId = findViewById<RadioGroup>(R.id.pdp_component_group_condition).checkedRadioButtonId
            val checkedCondition = findViewById<RadioButton>(checkedConditionId).text.toString()

            val checkedOptionId = findViewById<RadioGroup>(R.id.pdp_component_group_filter).checkedRadioButtonId
            val checkedOption = findViewById<RadioButton>(checkedOptionId).text.toString()

            val componentValueString = componentValue.textFieldInput.text.toString()

            componentSharedPref.edit()
                .putString(PDP_COMPONENT_FILTER_VALUE, componentValueString)
                .putString(PDP_COMPONENT_FILTER_CONDITION, checkedCondition)
                .putString(PDP_COMPONENT_FILTER_OPTION, checkedOption)
                .apply()


            val messageSuccess = "$checkedCondition Component $checkedOption: $componentValueString"
            Toast.makeText(this@ProductDetailDevActivity, messageSuccess, Toast.LENGTH_SHORT).show()
        }
    }
}
