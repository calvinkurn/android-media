package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class BuyerSettingBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context): BuyerSettingBottomSheet =
            BuyerSettingBottomSheet().apply {
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_buyer_setting,
                    null
                )
                setChild(view)
            }

        private const val TAG = "BuyerSettingBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        setTitle(getString(R.string.buyer_setting_title))

        showCloseIcon = true
    }

}