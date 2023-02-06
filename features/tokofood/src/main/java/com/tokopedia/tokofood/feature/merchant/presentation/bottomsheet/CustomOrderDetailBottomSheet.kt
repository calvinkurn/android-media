package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokofood.common.util.TokofoodExt.copyParcelable
import com.tokopedia.tokofood.databinding.BottomsheetOrderInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.adapter.OrderDetailAdapter
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderDetailViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify

class CustomOrderDetailBottomSheet :
    BottomSheetUnify(), OrderDetailViewHolder.OnOrderDetailItemClickListener {

    interface OnCustomOrderDetailClickListener {
        fun onDeleteCustomOrderButtonClicked(cartId: String)
        fun onUpdateCustomOrderQtyButtonClicked(
            productId: String,
            quantity: Int,
            customOrderDetail: CustomOrderDetail
        )

        fun onNavigateToOrderCustomizationPage(
            cartId: String,
            productUiModel: ProductUiModel,
            cardPositions: Pair<Int, Int>
        )
    }

    companion object {
        private const val TAG = "CustomOrderDetailBottomSheet"
        const val BUNDLE_KEY_PRODUCT_UI_MODEL = "bundle_key_custom_order_detail"
        const val BUNDLE_KEY_PRODUCT_POSITION = "bundle_key_product_position_custom_order_detail"
        const val BUNDLE_KEY_ADAPTER_POSITION = "bundle_key_adapter_position_custom_order_detail"

        @JvmStatic
        fun createInstance(
            bundle: Bundle?
        ): CustomOrderDetailBottomSheet {
            return if (bundle == null) {
                CustomOrderDetailBottomSheet()
            } else {
                CustomOrderDetailBottomSheet().apply {
                    arguments = bundle
                }
            }
        }
    }


    private var binding: BottomsheetOrderInfoLayoutBinding? = null

    private var adapter: OrderDetailAdapter = OrderDetailAdapter(this)

    private var productPosition: Int? = null

    private var adapterPosition: Int? = null

    private var productUiModel: ProductUiModel? = null

    private var clickListener: OnCustomOrderDetailClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDataFromArgs()
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
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetOrderInfoLayoutBinding?) {
        binding?.buttonAddCustom?.setOnClickListener {
            productUiModel?.let { productUiModel ->
                dismiss()
                clickListener?.onNavigateToOrderCustomizationPage(
                    cartId = "", productUiModel = productUiModel,
                    Pair(productPosition.orZero(), adapterPosition.orZero())
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

    private fun setDataFromArgs() {
        val productPosition = arguments?.getInt(BUNDLE_KEY_PRODUCT_POSITION).orZero()
        this.productPosition = productPosition
        this.adapterPosition = arguments?.getInt(BUNDLE_KEY_ADAPTER_POSITION).orZero()
        val productUiParcelable = arguments?.getParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
        this.productUiModel = productUiParcelable.copyParcelable()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }

    fun setClickListener(clickListener: OnCustomOrderDetailClickListener) {
        this.clickListener = clickListener
    }

    override fun onEditButtonClicked(cartId: String) {
        productUiModel?.let {
            dismiss()
            clickListener?.onNavigateToOrderCustomizationPage(
                cartId = cartId,
                productUiModel = it,
                Pair(productPosition.orZero(), adapterPosition.orZero())
            )
        }
    }

    override fun onDeleteButtonClicked(adapterPosition: Int, cartId: String) {
        adapter.removeCustomProduct(cartId, adapterPosition)
        clickListener?.onDeleteCustomOrderButtonClicked(cartId)
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

    override fun onUpdateQty(quantity: Int, customOrderDetail: CustomOrderDetail) {
        productUiModel?.let {
            clickListener?.onUpdateCustomOrderQtyButtonClicked(
                    customOrderDetail = customOrderDetail,
                    quantity = quantity,
                    productId = it.id
            )
        }
    }
}
