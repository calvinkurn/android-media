package com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetWarehouseBinding
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.WarehouseAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class WarehouseBottomSheet(val warehouses: List<WarehouseUiModel>) : BottomSheetUnify() {

    companion object {
        private const val TAG = "WarehouseBottomSheet"
    }

    private var binding by autoClearedNullable<SsfsBottomsheetWarehouseBinding>()
    private var warehouseAdapter = WarehouseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomsheetWarehouseBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Pilih lokasi")
        binding?.rvWarehouses?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.rvWarehouses?.adapter = warehouseAdapter
        warehouseAdapter.setItems(warehouses)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

}