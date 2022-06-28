package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.databinding.BottomsheetProductDetailLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductDetailBottomSheet(private val clickListener: OnProductDetailClickListener) :
    BottomSheetUnify() {

    interface OnProductDetailClickListener {
        fun onAtcButtonClicked(productListItem: ProductListItem, cardPositions: Pair<Int, Int>)
        fun onIncreaseQtyButtonClicked(
            productId: String,
            quantity: Int,
            cardPositions: Pair<Int, Int>
        )

        fun onNavigateToOrderCustomizationPage(
            cartId: String,
            productUiModel: ProductUiModel,
            productPosition: Int
        )
    }

    companion object {

        private const val BUNDLE_KEY_PRODUCT_UI_MODEL = "PRODUCT_UI_MODEL"

        @JvmStatic
        fun createInstance(
            productUiModel: ProductUiModel,
            clickListener: OnProductDetailClickListener
        ): ProductDetailBottomSheet {
            return ProductDetailBottomSheet(clickListener).apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL, productUiModel)
                }
            }
        }
    }

    private var binding: BottomsheetProductDetailLayoutBinding? = null

    private var listener: Listener? = null

    private val productUiModel: ProductUiModel by lazy {
        arguments?.getParcelable(BUNDLE_KEY_PRODUCT_UI_MODEL) ?: ProductUiModel()
    }

    private var sentMerchantTracker: (() -> Unit)? = null

    private var cardPositions: Pair<Int, Int>? = null

    private var productListItem: ProductListItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = BottomsheetProductDetailLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(productUiModel, binding)
        renderData(productUiModel)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(
        productUiModel: ProductUiModel,
        binding: BottomsheetProductDetailLayoutBinding?
    ) {
        binding?.atcButton?.setOnClickListener {
            sentMerchantTracker?.invoke()
            this.cardPositions?.run {
                if (productUiModel.isCustomizable) {
                    clickListener.onNavigateToOrderCustomizationPage(
                        cartId = "",
                        productUiModel = productUiModel,
                        productPosition = first
                    )
                } else if (!productUiModel.isAtc) {
                    productListItem?.let { productListItem ->
                        clickListener.onAtcButtonClicked(
                            productListItem = productListItem,
                            cardPositions = this
                        )
                    }
                } else {
                    clickListener.onIncreaseQtyButtonClicked(
                        productId = productUiModel.id,
                        quantity = productUiModel.orderQty + 1,
                        cardPositions = this
                    )
                }
            }
            dismiss()
        }
        binding?.iuShareButton?.setOnClickListener {
            listener?.onFoodItemShareClicked(productUiModel)
        }
    }

    private fun renderData(productUiModel: ProductUiModel) {
        val imageUrl = productUiModel.imageURL
        binding?.cuProductImage?.isVisible = productUiModel.imageURL.isNotBlank()
        binding?.cuProductImageMarginBottom?.isVisible = productUiModel.imageURL.isNotBlank()
        binding?.iuProductImage?.setImageUrl(imageUrl)
        binding?.productImageOverlay?.isVisible = productUiModel.isOutOfStock
        binding?.tpgOutOfStock?.isVisible = productUiModel.isOutOfStock
        binding?.productNameLabel?.text = productUiModel.name
        binding?.productDescriptionLabel?.text = productUiModel.description
        binding?.productPrice?.text = productUiModel.priceFmt
        binding?.slashPriceInfo?.isVisible = productUiModel.isSlashPriceVisible
        binding?.productSlashPrice?.isVisible = productUiModel.isSlashPriceVisible
        if (productUiModel.isSlashPriceVisible) {
            binding?.productSlashPrice?.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = productUiModel.slashPriceFmt
            }
        }
        if (productUiModel.isShopClosed || productUiModel.isOutOfStock) binding?.atcButton?.isEnabled =
            false
        if (productUiModel.customOrderDetails.size.isMoreThanZero()) {
            binding?.atcButton?.text =
                getString(com.tokopedia.tokofood.R.string.action_add_custom_product)
        }
    }

    fun setSelectedCardPositions(cardPositions: Pair<Int, Int>) {
        this.cardPositions = cardPositions
    }

    fun setProductListItem(productListItem: ProductListItem) {
        this.productListItem = productListItem
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun sendTrackerInMerchantPage(sentMerchantTracker: () -> Unit) {
        this.sentMerchantTracker = sentMerchantTracker
    }

    interface Listener {
        fun onFoodItemShareClicked(productUiModel: ProductUiModel)
    }
}