package com.tokopedia.vouchercreation.create.view.customview.bottomsheet

import android.content.Context
import androidx.annotation.LayoutRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.customview.BaseVoucherCustomView
import kotlinx.android.synthetic.main.mvc_create_promo_code_bottom_sheet_view.view.*

class CreatePromoCodeBottomSheetView @JvmOverloads constructor(
        context: Context,
        onNextClick: (String) -> Unit = { _ -> },
        @LayoutRes layoutResource: Int = R.layout.mvc_create_promo_code_bottom_sheet_view
) : BaseVoucherCustomView(context, layoutResource = layoutResource), VoucherBottomView {

    override fun setupAttributes() {

    }

    override fun setupView() {
        createPromoCodeTextField?.setPlaceholder("tes")
    }

    override val title: String? = resources.getString(R.string.mvc_create_target_create_promo_code_bottomsheet_title)
}