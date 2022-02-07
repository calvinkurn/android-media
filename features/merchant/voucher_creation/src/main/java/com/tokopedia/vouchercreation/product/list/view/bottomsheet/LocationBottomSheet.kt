package com.tokopedia.vouchercreation.product.list.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcWarehouseLocationFilterLayoutBinding
import com.tokopedia.vouchercreation.product.list.view.adapter.LocationListAdapter
import com.tokopedia.vouchercreation.product.list.view.model.WarehouseLocationSelection

class LocationBottomSheet : BottomSheetUnify() {

    interface OnApplyButtonClickListener {
        fun onApplyWarehouseLocationFilter(selectedWarehouseLocation: WarehouseLocationSelection)
    }

    companion object {

        private const val WAREHOUSE_SELECTIONS = "WAREHOUSE_SELECTIONS"

        @JvmStatic
        fun createInstance(warehouseLocationsSelections: List<WarehouseLocationSelection>,
                           clickListener: OnApplyButtonClickListener): LocationBottomSheet {
            return LocationBottomSheet().apply {
                this.clickListener = clickListener
                arguments = Bundle().apply {
                    putParcelableArrayList(WAREHOUSE_SELECTIONS, ArrayList(warehouseLocationsSelections))
                }
            }
        }
    }

    private var listAdapter: LocationListAdapter? = LocationListAdapter()

    private var binding: BottomsheetMvcWarehouseLocationFilterLayoutBinding? = null

    private var clickListener: OnApplyButtonClickListener? = null

    private val warehouseLocationSelections: ArrayList<WarehouseLocationSelection> by lazy {
        arguments?.getParcelableArrayList(WAREHOUSE_SELECTIONS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetMvcWarehouseLocationFilterLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setTitle(getString(R.string.mvc_location))
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        listAdapter?.setWarehouseLocationSelections(warehouseLocationSelections)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetMvcWarehouseLocationFilterLayoutBinding?) {
        binding?.rvSellerLocationList?.let {
            it.adapter = listAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        binding?.applyButton?.setOnClickListener {
            val selectedWarehouseLocation = listAdapter?.getSelectedWarehouseLocation()
                    ?: WarehouseLocationSelection()
            clickListener?.onApplyWarehouseLocationFilter(selectedWarehouseLocation)
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}