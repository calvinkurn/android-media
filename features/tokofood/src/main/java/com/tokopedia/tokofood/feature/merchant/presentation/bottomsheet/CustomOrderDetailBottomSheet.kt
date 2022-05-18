package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.tokofood.databinding.BottomsheetOrderInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.OrderDetailAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.unifycomponents.BottomSheetUnify

class CustomOrderDetailBottomSheet : BottomSheetUnify() {

    companion object {

        private const val PRODUCT_NAME = "PRODUCT_NAME"
        private const val CUSTOM_ORDER_DETAILS = "CUSTOM_ORDER_DETAILS"

        @JvmStatic
        fun createInstance(productName: String, customOrderDetails: List<CustomOrderDetail>): CustomOrderDetailBottomSheet {
            return CustomOrderDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(PRODUCT_NAME, productName)
                    putParcelableArrayList(CUSTOM_ORDER_DETAILS, ArrayList(customOrderDetails))
                }
            }
        }
    }

    private var binding: BottomsheetOrderInfoLayoutBinding? = null

    private var adapter: OrderDetailAdapter = OrderDetailAdapter()

    private val productName: String by lazy {
        arguments?.getString(PRODUCT_NAME) ?: ""
    }

    private val customOrderDetails: ArrayList<CustomOrderDetail> by lazy {
        arguments?.getParcelableArrayList(CUSTOM_ORDER_DETAILS) ?: arrayListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetOrderInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        setTitle(productName)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        renderData(customOrderDetails)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetOrderInfoLayoutBinding?) {
        binding?.buttonAddCustom?.setOnClickListener {
            // TODO: navigate to customization page
        }
        binding?.rvOrderInfo?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun renderData(customOrderDetails: ArrayList<CustomOrderDetail>) {
        adapter.setCustomOrderDetails(customOrderDetails.toList())
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }
}