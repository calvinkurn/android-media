package com.tokopedia.addon.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.addon.presentation.fragment.AddOnFragment
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetAddonBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AddOnBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetAddonBinding>()
    private var productId: Long = 0L
    private var pageSource: String = ""
    private var cartId: Long = 0L
    private var selectedAddonIds: List<String> = emptyList()
    private var isTokocabang: Boolean = false
    private var warehouseId: Long = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        overlayClickDismiss = false
        binding = BottomsheetAddonBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.addon_bottomsheet_title))
        val fragment = AddOnFragment.newInstance(
            productId,
            pageSource,
            cartId,
            selectedAddonIds,
            warehouseId,
            isTokocabang
        ).apply { setOnSuccessSaveAddonListener(::onSaveAddonSuccess) }
        childFragmentManager.beginTransaction()
            .replace(R.id.parent_view, fragment, "")
            .commit()
    }

    private fun onSaveAddonSuccess(aggregatedData: AddOnPageResult.AggregatedData) {
        dismiss()
    }

    fun setPageSource(pageSource: String) {
        this.pageSource = pageSource
    }

    fun setCartId(cartId: Long) {
        this.cartId = cartId
    }

    fun setSelectedAddonIds(selectedAddonIds: List<String>) {
        this.selectedAddonIds = selectedAddonIds
    }

    fun setProductId(productId: Long) {
        this.productId = productId
    }

    fun setWarehouseId(warehouseId: Long) {
        this.warehouseId = warehouseId
    }

    fun setIsTokocabang(isTokocabang: Boolean) {
        this.isTokocabang = isTokocabang
    }
}
