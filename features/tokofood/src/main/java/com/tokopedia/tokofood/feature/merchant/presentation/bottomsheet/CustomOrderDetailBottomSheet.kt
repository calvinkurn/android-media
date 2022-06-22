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

class CustomOrderDetailBottomSheet :
    BottomSheetUnify(), OrderDetailViewHolder.OnOrderDetailItemClickListener {

    interface OnCustomOrderDetailClickListener {
        fun onDeleteCustomOrderButtonClicked(cartId: String, productId: String)
        fun onUpdateCustomOrderQtyButtonClicked(
            productId: String,
            quantity: Int,
            customOrderDetail: CustomOrderDetail
        )

        fun onNavigateToOrderCustomizationPage(
            cartId: String,
            productUiModel: ProductUiModel,
            productPosition: Int
        )
    }

    companion object {

        const val BUNDLE_KEY_PRODUCT_UI_MODEL = "bundle_key_custom_order_detail"
        const val BUNDLE_KEY_PRODUCT_POSITION = "bundle_key_product_position_custom_order_detail"

        @JvmStatic
        fun createInstance(
            bundle: Bundle?,
            clickListener: OnCustomOrderDetailClickListener?
        ): CustomOrderDetailBottomSheet {
            return if (bundle == null) {
                CustomOrderDetailBottomSheet().apply {
                    this.clickListener = clickListener
                }
            } else {
                CustomOrderDetailBottomSheet().apply {
                    arguments = bundle
                    this.clickListener = clickListener
                }
            }
        }
    }


    private var binding: BottomsheetOrderInfoLayoutBinding? = null

    private var adapter: OrderDetailAdapter = OrderDetailAdapter(this)

    private var productPosition: Int? = null

    private var productUiModel: ProductUiModel? = null

    private var clickListener: OnCustomOrderDetailClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = BottomsheetOrderInfoLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        clearContentPadding = true
        productUiModel?.name?.let { setTitle(it) }
        setChild(viewBinding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        productUiModel?.let { renderData(it.customOrderDetails) }
    }

    override fun onDestroyView() {
        binding = null
        productUiModel = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetOrderInfoLayoutBinding?) {
        binding?.buttonAddCustom?.setOnClickListener {
            productUiModel?.let { productUiModel ->
                clickListener?.onNavigateToOrderCustomizationPage(
                    cartId = "", productUiModel = productUiModel,
                    productPosition.orZero()
                )
            }
        }
        binding?.rvOrderInfo?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(it.context)
        }
    }

    private fun renderData(customOrderDetails: List<CustomOrderDetail>) {
        adapter.setCustomOrderDetails(customOrderDetails)
    }

    private fun setData() {
        val productPosition = arguments?.getInt(BUNDLE_KEY_PRODUCT_POSITION).orZero()
        this.productPosition = productPosition
        this.productUiModel = arguments?.getParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, this::class.java.simpleName)
        }
    }

    override fun onEditButtonClicked(cartId: String) {
        productUiModel?.let {
            clickListener?.onNavigateToOrderCustomizationPage(
                cartId = cartId,
                productUiModel = it,
                productPosition.orZero()
            )
        }
    }

    override fun onDeleteButtonClicked(dataSetPosition: Int, adapterPosition: Int, cartId: String) {
        adapter.removeCustomProduct(dataSetPosition, adapterPosition)
        productUiModel?.let { clickListener?.onDeleteCustomOrderButtonClicked(cartId, it.id) }
        if (adapter.getCustomOrderDetails().isEmpty()) dismiss()
    }

    override fun onIncreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        productUiModel?.let {
            clickListener?.onUpdateCustomOrderQtyButtonClicked(
                customOrderDetail = customOrderDetail,
                quantity = quantity,
                productId = it.id
            )
        }
    }

    override fun onDecreaseQtyButtonClicked(quantity: Int, customOrderDetail: CustomOrderDetail) {
        productUiModel?.let {
            clickListener?.onUpdateCustomOrderQtyButtonClicked(
                customOrderDetail = customOrderDetail,
                quantity = quantity,
                productId = it.id
            )
        }
    }
}