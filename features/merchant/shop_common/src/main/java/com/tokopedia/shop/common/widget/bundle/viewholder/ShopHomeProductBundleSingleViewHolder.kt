package com.tokopedia.shop.common.widget.bundle.viewholder

import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemShopHomeProductBundleSingleWidgetBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleSingleAdapter
import com.tokopedia.shop.common.widget.bundle.adapter.SingleBundleVariantSelectedListener
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeProductBundleSingleViewHolder(
        itemView: View,
        private val singleProductBundleClickListener: SingleProductBundleClickListener
): RecyclerView.ViewHolder(itemView), SingleBundleVariantSelectedListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_single_widget
    }

    private var viewBinding: ItemShopHomeProductBundleSingleWidgetBinding? by viewBinding()
    private var typographyBundleName: Typography? = null
    private var typographyBundlePreorder: Typography? = null
    private var typographyBundleProductName: Typography? = null
    private var typographyBundleProductDisplayPrice: Typography? = null
    private var typographyBundleProductOriginalPrice: Typography? = null
    private var typographyBundleProductSavingAmount: Typography? = null
    private var buttonAtc: UnifyButton? = null
    private var labelBundleDiscount: Label? = null
    private var imageBundleProduct: ImageUnify? = null
    private var rvBundleDetails: RecyclerView? = null
    private var selectedSingleBundle: ShopHomeProductBundleDetailUiModel = ShopHomeProductBundleDetailUiModel()

    init {
        viewBinding?.apply {
            typographyBundleName = tvBundleName
            typographyBundlePreorder = tvBundlePreorder
            typographyBundleProductName = tvBundleProductSingleName
            typographyBundleProductDisplayPrice = tvBundleDisplayPrice
            typographyBundleProductOriginalPrice = tvBundleOriginalPrice
            typographyBundleProductSavingAmount = tvSavingAmountPriceWording
            buttonAtc = btnBundleAtc
            labelBundleDiscount = labelDiscountBundle
            imageBundleProduct = ivBundleImage
            rvBundleDetails = rvBundleSinglePackage
        }
        initBundleDetailsRecyclerView()
    }

    override fun onSingleVariantSelected(selectedBundle: ShopHomeProductBundleDetailUiModel) {
        selectedSingleBundle = selectedBundle
        renderBundlePriceDetails(selectedBundle)
    }

    fun bind(bundle: ShopHomeProductBundleItemUiModel) {
        val singleBundleProduct = bundle.bundleProducts.firstOrNull() ?: ShopHomeBundleProductUiModel()
        selectedSingleBundle = bundle.bundleDetails.firstOrNull() ?: ShopHomeProductBundleDetailUiModel()

        // bundle card item details
        renderBundlePriceDetails(selectedSingleBundle)
        typographyBundleName?.text = bundle.bundleName
        typographyBundlePreorder?.apply {
            shouldShowWithAction(selectedSingleBundle.isPreOrder.orFalse()) { text = selectedSingleBundle.preOrderInfo }
        }

        // single bundle product detail
        imageBundleProduct?.loadImage(singleBundleProduct.productImageUrl)
        imageBundleProduct?.setOnClickListener {
            singleProductBundleClickListener.onSingleBundleProductClicked(singleBundleProduct.productId.toString())
        }
        typographyBundleProductName?.text = singleBundleProduct.productName
        typographyBundleProductName?.setOnClickListener {
            singleProductBundleClickListener.onSingleBundleProductClicked(singleBundleProduct.productId.toString())
        }

        // bundle variant package list
        (rvBundleDetails?.adapter as ShopHomeProductBundleSingleAdapter).updateDataSet(bundle.bundleDetails)

        // bind listeners
        buttonAtc?.setOnClickListener {
            singleProductBundleClickListener.addSingleBundleToCart(selectedSingleBundle, singleBundleProduct)
        }
    }

    private fun initBundleDetailsRecyclerView() {
        rvBundleDetails?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ShopHomeProductBundleSingleAdapter(
                    singleBundleVariantSelectedListener = this@ShopHomeProductBundleSingleViewHolder
            )
        }
    }

    private fun renderBundlePriceDetails(bundle: ShopHomeProductBundleDetailUiModel) {
        itemView.context?.let { ctx ->
            labelBundleDiscount?.setLabel("${bundle.discountPercentage}%")
            typographyBundleProductDisplayPrice?.text = bundle.displayPrice
            typographyBundleProductOriginalPrice?.apply {
                text = bundle.originalPrice
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            typographyBundleProductSavingAmount?.text = HtmlLinkHelper(ctx, bundle.savingAmountWording).spannedString
        }
    }

}

interface SingleProductBundleClickListener {
    fun onSingleBundleProductClicked(productId: String)
    fun addSingleBundleToCart(selectedBundle: ShopHomeProductBundleDetailUiModel, bundleProducts: ShopHomeBundleProductUiModel)
}