package com.tokopedia.shop.flashsale.presentation.creation.highlight.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetManageHighlightedProductInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ManageHighlightedProductInfoBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "ManageHighlightedProductInfoBottomSheet"
    }

    private var binding by autoClearedNullable<SsfsBottomSheetManageHighlightedProductInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            SsfsBottomSheetManageHighlightedProductInfoBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        setupContent()
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    @SuppressLint("ResourcePackage")
    private fun setupView() {
        setTitle(getString(R.string.manage_highlight_product_title))
        showCloseIcon = true
    }

    private fun setupContent() {
        binding?.run {
            tgManageHighlightedProductInfoDescription.text =
                getString(R.string.manage_highlight_product_description)
        }
    }
}