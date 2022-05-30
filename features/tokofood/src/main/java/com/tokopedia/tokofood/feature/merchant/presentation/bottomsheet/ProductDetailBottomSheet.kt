package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.databinding.BottomsheetProductDetailLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductDetailBottomSheet : BottomSheetUnify() {

    companion object {

        private const val PRODUCT_UI_MODEL = "PRODUCT_UI_MODEL"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel): ProductDetailBottomSheet {
            return ProductDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_UI_MODEL, productUiModel)
                }
            }
        }
    }

    private var binding: BottomsheetProductDetailLayoutBinding? = null

    private var listener: Listener? = null

    private val productUiModel: ProductUiModel by lazy {
        arguments?.getParcelable(PRODUCT_UI_MODEL) ?: ProductUiModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = BottomsheetProductDetailLayoutBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(binding)
        renderData(productUiModel)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupView(binding: BottomsheetProductDetailLayoutBinding?) {
        binding?.atcButton?.setOnClickListener {

        }
        binding?.iuShareButton?.setOnClickListener {
            listener?.onFoodItemShareClicked()
        }
    }

    private fun renderData(productUiModel: ProductUiModel) {
        val imageUrl = productUiModel.imageURL
        if (imageUrl.isNotBlank()) binding?.iuProductImage?.setImageUrl(imageUrl)
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
    }

    fun show(fragmentManager: FragmentManager) {
        showNow(fragmentManager, this::class.java.simpleName)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onFoodItemShareClicked()
    }
}