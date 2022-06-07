package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.orZero
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
        fun onNavigateToOrderCustomizationPage(cartId: String, productUiModel: ProductUiModel, productPosition: Int)
    }

    companion object {

        const val BUNDLE_KEY_PRODUCT_UI_MODEL = "PRODUCT_UI_MODEL"
        const val BUNDLE_KEY_PRODUCT_POSITION = "PRODUCT_POSITION"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel,
                           clickListener: OnCustomOrderDetailClickListener,
                           productPosition: Int
        ): CustomOrderDetailBottomSheet {
            return CustomOrderDetailBottomSheet(clickListener).apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
                    putInt(BUNDLE_KEY_PRODUCT_POSITION, productPosition)
                }
            }
        }
    }

    private var binding: BottomsheetOrderInfoLayoutBinding? = null

    private var adapter: OrderDetailAdapter = OrderDetailAdapter(this)

    private val productPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_PRODUCT_POSITION).orZero()
    }

    private val productUiModel: ProductUiModel by lazy {
        arguments?.getParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetOrderInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        setTitle(productUiModel.name)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        renderData(productUiModel.customOrderDetails)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetOrderInfoLayoutBinding?) {
        binding?.buttonAddCustom?.setOnClickListener {
            clickListener.onNavigateToOrderCustomizationPage(cartId = "", productUiModel = productUiModel, productPosition)
        }
        binding?.rvOrderInfo?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun renderData(customOrderDetails: List<CustomOrderDetail>) {
        adapter.setCustomOrderDetails(customOrderDetails)
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

    override fun onEditButtonClicked(cartId: String) {
        clickListener.onNavigateToOrderCustomizationPage(cartId = cartId, productUiModel = productUiModel, productPosition)
    }

    override fun onDeleteButtonClicked(cartId: String) {
        clickListener.onDeleteCustomOrderButtonClicked(cartId, productUiModel.id)
    }

    override fun onIncreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        clickListener.onUpdateCustomOrderQtyButtonClicked(
                customOrderDetail = customOrderDetail,
                quantity = quantity,
                productId = productUiModel.id
        )
    }

    override fun onDecreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        clickListener.onUpdateCustomOrderQtyButtonClicked(
                customOrderDetail = customOrderDetail,
                quantity = quantity,
                productId = productUiModel.id
        )
    }
}