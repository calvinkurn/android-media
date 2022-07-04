package com.tokopedia.shop.flashsale.presentation.creation.manage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetWarehouseBinding
import com.tokopedia.shop.flashsale.presentation.creation.manage.adapter.WarehouseAdapter
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class WarehouseBottomSheet: BottomSheetUnify() {

    companion object {
        private const val TAG = "WarehouseBottomSheet"
        private const val KEY_WAREHOUSES = "WAREHOUSES"

        fun newInstance(warehouses: List<WarehouseUiModel>): WarehouseBottomSheet {
            val fragment = WarehouseBottomSheet()
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(KEY_WAREHOUSES,  ArrayList(warehouses))
            }
            return fragment
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetWarehouseBinding>()
    private var warehouseAdapter = WarehouseAdapter()
    private val warehouses: ArrayList<WarehouseUiModel>? by lazy {
        arguments?.getParcelableArrayList(KEY_WAREHOUSES)
    }
    private var onSubmitlistener: (List<WarehouseUiModel>) -> Unit = {}

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
        setTitle(getString(R.string.editproduct_bs_warehouse_title))
        setupContents()
        setupListAdapter()
    }

    private fun setupListAdapter() {
        val warehouseList = warehouses?.toList().orEmpty()
        warehouseAdapter.setItems(warehouseList)
    }

    private fun setupContents() {
        binding?.run {
            rvWarehouses.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvWarehouses.adapter = warehouseAdapter
            btnSubmit.setOnClickListener {
                onSubmitlistener(warehouseAdapter.getItems())
                dismiss()
            }
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setOnSubmitListener(listener: (List<WarehouseUiModel>) -> Unit) {
        onSubmitlistener = listener
    }
}