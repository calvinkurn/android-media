package com.tokopedia.product.addedit.productlimitation.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.customview.TabletAdaptiveBottomSheet
import com.tokopedia.product.addedit.productlimitation.domain.constant.AddEditProductUrlConstants
import com.tokopedia.product.addedit.tracking.ProductLimitationTracking
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductLimitationNoActionBottomSheet(
    private val limitAmount: Int = 0,
    private val onBottomSheetResult: (String) -> Unit = {}
): TabletAdaptiveBottomSheet() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView: View? = View.inflate(context,
            R.layout.add_edit_product_product_limitation_bottom_sheet_nodata_content, null)
        val tvDesc: Typography? = contentView?.findViewById(R.id.tv_title_product_limitation)
        val btnDismiss: UnifyButton? = contentView?.findViewById(R.id.btn_dismiss)
        val btnLearn: UnifyButton? = contentView?.findViewById(R.id.btn_learn)

        tvDesc?.text = getString(R.string.title_product_limitation_bottomsheet_info_nodata, limitAmount)
        btnDismiss?.setOnClickListener {
            dismiss()
        }
        btnLearn?.setOnClickListener {
            ProductLimitationTracking.clickEduTicker()
            onBottomSheetResult.invoke(AddEditProductUrlConstants.URL_PRODUCT_LIMITATION_EDU)
            dismiss()
        }

        showCloseIcon = false
        setTitle(getString(R.string.title_product_limitation_add_product_rules_nodata))
        setChild(contentView)
    }
}