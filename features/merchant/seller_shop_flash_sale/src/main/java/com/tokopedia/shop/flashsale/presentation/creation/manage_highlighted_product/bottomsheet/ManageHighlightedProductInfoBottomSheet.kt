package com.tokopedia.shop.flashsale.presentation.creation.manage_highlighted_product.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class ManageHighlightedProductInfoBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): ManageHighlightedProductInfoBottomSheet =
            ManageHighlightedProductInfoBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_manage_highlighted_product_info,
                    null
                )
                setChild(view)
            }

        private const val TAG = "ManageHighlightedProductInfoBottomSheet"
    }

    private var highLightInfoDesc: Typography? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView(view: View) {
        setTitle(getString(R.string.manage_highlight_product_title))

        highLightInfoDesc = view.findViewById(R.id.tg_manage_highlighted_product_info_description)
        showCloseIcon = true
    }

    private fun setupContent() {
        highLightInfoDesc?.run {
            text = getString(R.string.manage_highlight_product_description)
        }
    }
}