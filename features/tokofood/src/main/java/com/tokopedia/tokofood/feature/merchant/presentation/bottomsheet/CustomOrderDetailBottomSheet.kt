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
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderDetailViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify

class CustomOrderDetailBottomSheet(private val clickListener: OnCustomOrderDetailClickListener)
    : BottomSheetUnify(), OrderDetailViewHolder.OnOrderDetailItemClickListener {

    interface OnCustomOrderDetailClickListener {
        fun onDeleteCustomOrderButtonClicked(cartId: String, productId: String)
        fun onUpdateCustomOrderQtyButtonClicked(productId: String, quantity: Int, customOrderDetail: CustomOrderDetail)
        fun onNavigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel)
    }

    companion object {
        @JvmStatic
        fun createInstance(clickListener: OnCustomOrderDetailClickListener): CustomOrderDetailBottomSheet {
            return CustomOrderDetailBottomSheet(clickListener)
        }
    }

    private var binding: BottomsheetOrderInfoLayoutBinding? = null

    private var adapter: OrderDetailAdapter = OrderDetailAdapter(this)

    private var productUiModel: ProductUiModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetOrderInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        productUiModel?.run { setTitle(this.name) }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        productUiModel?.run { renderData(this.customOrderDetails) }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetOrderInfoLayoutBinding?) {
        binding?.buttonAddCustom?.setOnClickListener {
            productUiModel?.run {
                clickListener.onNavigateToOrderCustomizationPage(cartId = "", productUiModel = this)
            }
        }
        binding?.rvOrderInfo?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun renderData(customOrderDetails: List<CustomOrderDetail>) {
        adapter.setCustomOrderDetails(customOrderDetails)
    }

    fun setProductUiModel(productUiModel: ProductUiModel) {
        this.productUiModel = productUiModel
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

    override fun onEditButtonClicked(cartId: String) {
        productUiModel?.run {
            clickListener.onNavigateToOrderCustomizationPage(cartId = cartId, productUiModel = this)
        }
    }

    override fun onDeleteButtonClicked(dataSetPosition: Int, adapterPosition: Int, cartId: String) {
        productUiModel?.run {
            adapter.removeCustomProduct(dataSetPosition, adapterPosition)
            clickListener.onDeleteCustomOrderButtonClicked(cartId, this.id)
            if (adapter.getCustomOrderDetails().isEmpty()) dismiss()
        }
    }

    override fun onIncreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        productUiModel?.run {
            clickListener.onUpdateCustomOrderQtyButtonClicked(
                    customOrderDetail = customOrderDetail,
                    quantity = quantity,
                    productId = this.id
            )
        }
    }

    override fun onDecreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        productUiModel?.run {
            clickListener.onUpdateCustomOrderQtyButtonClicked(
                    customOrderDetail = customOrderDetail,
                    quantity = quantity,
                    productId = this.id
            )
        }
    }
}