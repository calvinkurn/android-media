package com.tokopedia.vouchercreation.create.view.customview.bottomsheet

import android.content.Context
import androidx.annotation.LayoutRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.customview.BaseVoucherCustomView
import kotlinx.android.synthetic.main.mvc_create_promo_code_bottom_sheet_view.view.*

class CreatePromoCodeBottomSheetView @JvmOverloads constructor(
        context: Context,
        private val onNextClick: (String) -> Unit = { _ -> },
        @LayoutRes layoutResource: Int = R.layout.mvc_create_promo_code_bottom_sheet_view
) : BaseVoucherCustomView(context, layoutResource = layoutResource) {

    override fun setupAttributes() {

    }

    override fun setupView() {
        view?.run {
            createPromoCodeSaveButton?.setOnClickListener {
                onNextClick(createPromoCodeTextField?.textFieldInput?.text.toString())
            }
        }
    }
}