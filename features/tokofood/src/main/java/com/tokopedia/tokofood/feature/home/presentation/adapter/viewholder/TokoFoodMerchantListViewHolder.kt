package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodMerchantListCardBinding
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.tokofood.feature.home.domain.data.PriceLevel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.StringBuilder

class TokoFoodMerchantListViewHolder (
    itemView: View,
    private val listener: TokoFoodMerchantListListener? = null
): AbstractViewHolder<TokoFoodMerchantListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_merchant_list_card
        const val COUNT_MAX_LEVEL_PRICE = 3
    }

    private var binding: ItemTokofoodMerchantListCardBinding? by viewBinding()
    private var imgTokoFoodMerchant: ImageUnify? = null
    private var tgTokoFoodMerchantTitle: Typography? = null
    private var tgTokoFoodMerchantPriceScale: Typography? = null
    private var viewDividerTokoFoodMerchant: View? = null
    private var tgTokoFoodMerchantCategory: Typography? = null
    private var labelMerchantDiskon: Label? = null
    private var tgTokoFoodMerchantDistance: Typography? = null
    private var tgTokoFoodMerchantRating: Typography? = null
    private var labelMerchantClosed: Label? = null

    override fun bind(element: TokoFoodMerchantListUiModel) {
        setupLayout()
        setMerchantLayout(element.merchant)
    }

    private fun setupLayout(){
        imgTokoFoodMerchant = binding?.imgTokofoodMerchant
        tgTokoFoodMerchantTitle = binding?.tgTokofoodMerchantTitle
        tgTokoFoodMerchantPriceScale = binding?.tgTokofoodMerchantPriceScale
        viewDividerTokoFoodMerchant = binding?.viewDividerTokofoodMerchant
        tgTokoFoodMerchantCategory = binding?.tgTokofoodMerchantCategory
        labelMerchantDiskon = binding?.labelMerchantDiskon
        tgTokoFoodMerchantDistance = binding?.tgTokofoodMerchantDistance
        tgTokoFoodMerchantRating = binding?.tgTokofoodMerchantRating
        labelMerchantClosed = binding?.labelMerchantClosed
    }

    private fun setMerchantLayout(merchant: Merchant) {
        setImageMerchant(merchant.imageURL)
        setTitleMerchant(merchant.name)
        setLabelDiscount(merchant.promo)
        setMerchantDistance(merchant.distanceFmt, merchant.etaFmt)
        setMerchantRating(merchant.ratingFmt)
        setMerchantCategory(merchant.merchantCategories)
        setPriceLevel(merchant.priceLevel)
        setMerchantClosed(merchant.isClosed)
        binding?.root?.setOnClickListener {
            listener?.onClickMerchant(merchant, adapterPosition)
        }
        itemView.addOnImpressionListener(merchant){
            listener?.onImpressMerchant(merchant, adapterPosition)
        }
    }

    private fun setImageMerchant(imageUrl: String) {
        imgTokoFoodMerchant?.loadImage(imageUrl)
    }

    private fun setTitleMerchant(title: String) {
        if (!title.isNullOrEmpty()){
            tgTokoFoodMerchantTitle?.show()
            tgTokoFoodMerchantTitle?.text = title
        } else {
            tgTokoFoodMerchantTitle?.hide()
        }
    }

    private fun setLabelDiscount(label: String) {
        if (!label.isNullOrEmpty()){
            labelMerchantDiskon?.show()
            labelMerchantDiskon?.text = label
        } else {
            labelMerchantDiskon?.hide()
        }
    }

    private fun setMerchantDistance(distance: String, eta: String) {
        var etaDistance = StringBuilder()
        if (!distance.isNullOrEmpty()) {
            etaDistance.append(distance)
        }

        if (!eta.isNullOrEmpty()) {
            etaDistance.append(" - ")
            etaDistance.append(eta)
        }

        if (!etaDistance.toString().isNullOrEmpty()){
            tgTokoFoodMerchantDistance?.show()
            tgTokoFoodMerchantDistance?.text = etaDistance
        } else {
            tgTokoFoodMerchantDistance?.hide()
        }
    }

    private fun setMerchantRating(rating: String) {
        if (!rating.isNullOrEmpty()){
            tgTokoFoodMerchantRating?.show()
            tgTokoFoodMerchantRating?.text = rating
        } else {
            tgTokoFoodMerchantRating?.hide()
        }
    }

    private fun setMerchantCategory(categories: List<String>){
        if (!categories.isNullOrEmpty()){
            tgTokoFoodMerchantCategory?.show()
            tgTokoFoodMerchantCategory?.text = getCategoryString(categories)
        } else tgTokoFoodMerchantCategory?.hide()
    }

    private fun setPriceLevel(priceLevel: PriceLevel) {
        val price = getPriceLevelString(priceLevel)
        tgTokoFoodMerchantPriceScale?.text =  MethodChecker.fromHtml(price)
    }

    private fun setMerchantClosed(isClosed: Boolean) {
        if (isClosed){
            labelMerchantClosed?.show()
        } else {
            labelMerchantClosed?.hide()
        }
    }

    private fun getCategoryString(categories: List<String>) : String {
        val category = StringBuilder()
        categories.forEachIndexed { index, it ->
            category.append(it)
            if (index != (categories.size - 1)) {
                category.append(", ")
            }
        }
        return category.toString()
    }

    private fun getPriceLevelString(priceLevel: PriceLevel): String {
        val price = StringBuilder()
        if (priceLevel.fareCount.isMoreThanZero()){
            price.append("<b>")
        }

        for (i in 0..COUNT_MAX_LEVEL_PRICE){
            price.append(priceLevel.icon)
            if(i == (priceLevel.fareCount - 1) && priceLevel.fareCount.isMoreThanZero()){
                price.append("</b>")
            }
        }

        return price.toString()
    }

    interface TokoFoodMerchantListListener {
        fun onClickMerchant(merchant: Merchant, position: Int)
        fun onImpressMerchant(merchant: Merchant, position: Int)
    }
}