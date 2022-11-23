package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.AdditionalData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.domain.response.PriceLevel
import com.tokopedia.tokofood.common.presentation.customview.TokofoodPromoRibbonView
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodExt.clickWithDebounce
import com.tokopedia.tokofood.databinding.ItemTokofoodMerchantListCardBinding
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.StringBuilder
import com.tokopedia.unifyprinciples.R.dimen as unifyDimens

class TokoFoodMerchantListViewHolder (
    itemView: View,
    private val listener: TokoFoodMerchantListListener? = null,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener?
): AbstractViewHolder<TokoFoodMerchantListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_merchant_list_card
        const val COUNT_MAX_LEVEL_PRICE = 3
        const val GET_STRING_COLOR = 2
    }

    private var binding: ItemTokofoodMerchantListCardBinding? by viewBinding()
    private var imgTokoFoodMerchant: ImageUnify? = null
    private var tgTokoFoodMerchantTitle: Typography? = null
    private var tgTokoFoodMerchantPriceScale: Typography? = null
    private var viewDividerTokoFoodMerchant: View? = null
    private var tgTokoFoodMerchantCategory: Typography? = null
    private var ivMerchantDiskon: ImageUnify? = null
    private var tvMerchantDiskon: Typography? = null
    private var tgTokoFoodMerchantDistance: Typography? = null
    private var tgTokoFoodMerchantRating: Typography? = null
    private var imgTokoFoodMerchantRating: ImageView? = null
    private var labelMerchantClosed: Label? = null
    private var promoRibbon: TokofoodPromoRibbonView? = null

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
        ivMerchantDiskon = binding?.ivTokofoodMerchantDiscount
        tvMerchantDiskon = binding?.tvTokofoodMerchantPromoDetail
        tgTokoFoodMerchantDistance = binding?.tgTokofoodMerchantDistance
        tgTokoFoodMerchantRating = binding?.tgTokofoodMerchantRating
        imgTokoFoodMerchantRating = binding?.imgTokofoodMerchantRating
        labelMerchantClosed = binding?.labelMerchantClosed
        promoRibbon = binding?.ribbonTokofoodPromo
    }

    private fun setMerchantLayout(merchant: Merchant) {
        setImageMerchant(merchant.imageURL)
        setTitleMerchant(merchant.name)
        setPromoInfo(merchant.promo, merchant.additionalData)
        setMerchantDistance(merchant.distanceFmt, merchant.etaFmt)
        setMerchantRating(merchant.ratingFmt, merchant.rating)
        setMerchantCategory(merchant.merchantCategories, merchant.priceLevel)
        setPriceLevel(merchant.priceLevel)
        setViewDividerCategoryPriceLevel(merchant.merchantCategories, merchant.priceLevel)
        setMerchantClosed(merchant.isClosed)
        binding?.root?.clickWithDebounce {
            listener?.onClickMerchant(merchant, adapterPosition)
        }
        setImpressionListener(merchant)
    }

    private fun setImageMerchant(imageUrl: String) {
        imgTokoFoodMerchant?.loadImage(imageUrl)
    }

    private fun setTitleMerchant(title: String) {
        if (title.isNullOrEmpty()){
            tgTokoFoodMerchantTitle?.hide()
        } else {
            tgTokoFoodMerchantTitle?.show()
            tgTokoFoodMerchantTitle?.text = title
        }
    }

    private fun setPromoInfo(promo: String, additionalData: AdditionalData) {
        setPromoRibbonLabel(additionalData.topTextBanner)
        if (promo.isBlank()){
            ivMerchantDiskon?.hide()
            tvMerchantDiskon?.hide()
        } else {
            ivMerchantDiskon?.run {
                show()
                setImageUrl(additionalData.discountIcon)
            }
            tvMerchantDiskon?.run {
                show()
                text = promo
            }
        }
    }

    private fun setPromoRibbonLabel(topTextBanner: String) {
        if (topTextBanner.isBlank()) {
            promoRibbon?.hide()
        } else {
            promoRibbon?.run {
                show()
                setRibbonText(topTextBanner)
            }
        }
    }

    private fun setMerchantDistance(distance: String, eta: String) {
        val etaDistance = StringBuilder()
        if (!distance.isNullOrEmpty()) {
            etaDistance.append(distance)
        }

        if (!eta.isNullOrEmpty()) {
            etaDistance.append(" - ")
            etaDistance.append(eta)
        }

        if (etaDistance.toString().isNullOrEmpty()){
            tgTokoFoodMerchantDistance?.hide()
        } else {
            tgTokoFoodMerchantDistance?.show()
            tgTokoFoodMerchantDistance?.text = etaDistance
        }
    }

    private fun setMerchantRating(rating: String, ratingDouble: Double) {
        if (rating.isNullOrEmpty() || ratingDouble <= 0f){
            tgTokoFoodMerchantRating?.hide()
            imgTokoFoodMerchantRating?.hide()
        } else {
            tgTokoFoodMerchantRating?.show()
            imgTokoFoodMerchantRating?.show()
            tgTokoFoodMerchantRating?.text = rating
        }
    }

    private fun setViewDividerCategoryPriceLevel(categories: List<String>, priceLevel: PriceLevel){
        val price = getPriceLevelString(priceLevel)
        val category = getCategoryString(categories)
        if (category.isNullOrEmpty() || price.isNullOrEmpty() || priceLevel.fareCount <= 0){
            viewDividerTokoFoodMerchant?.hide()
        } else {
            viewDividerTokoFoodMerchant?.show()
        }
    }

    private fun setMerchantCategory(categories: List<String>, priceLevel: PriceLevel){
        val category = getCategoryString(categories)
        if (category.isNullOrEmpty()){
            tgTokoFoodMerchantCategory?.hide()
        } else {
            tgTokoFoodMerchantCategory?.show()
            tgTokoFoodMerchantCategory?.text = category

            tgTokoFoodMerchantCategory?.run {
                val labelParams = this.layoutParams as ConstraintLayout.LayoutParams

                if (priceLevel.fareCount <= 0) {

                    setMargin(
                        getDimens(unifyDimens.spacing_lvl3),
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0))

                    labelParams.topToTop = ConstraintLayout.LayoutParams.UNSET
                    labelParams.startToEnd = ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                    labelParams.startToEnd = imgTokoFoodMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToBottom = tgTokoFoodMerchantTitle?.id ?: ConstraintLayout.LayoutParams.UNSET
                } else {

                    setMargin(
                        getDimens(unifyDimens.spacing_lvl2),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0),
                        getDimens(unifyDimens.unify_space_0))

                    labelParams.startToEnd =  ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToBottom = ConstraintLayout.LayoutParams.UNSET
                    labelParams.topToTop = viewDividerTokoFoodMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
                    labelParams.startToEnd = viewDividerTokoFoodMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
                    labelParams.bottomToBottom = viewDividerTokoFoodMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
                }

                layoutParams = labelParams
            }
        }
    }

    private fun setPriceLevel(priceLevel: PriceLevel) {
        val price = getPriceLevelString(priceLevel)
        if (price.isNullOrEmpty() || priceLevel.fareCount <= 0){
            tgTokoFoodMerchantPriceScale?.hide()
        } else {
            tgTokoFoodMerchantPriceScale?.show()
            tgTokoFoodMerchantPriceScale?.text =  MethodChecker.fromHtml(price)
        }
    }

    private fun setMerchantClosed(isClosed: Boolean) {
        if (isClosed){
            labelMerchantClosed?.show()
        } else {
            labelMerchantClosed?.hide()
        }
    }

    private fun setImpressionListener(merchant: Merchant) {
        tokofoodScrollChangedListener?.let { scrollChangedListener ->
            itemView.addAndReturnImpressionListener(merchant, scrollChangedListener){
                listener?.onImpressMerchant(merchant, adapterPosition)
            }
        }
    }

    private fun getCategoryString(categories: List<String>) : String {
        val category = StringBuilder()
        categories.forEachIndexed { index, it ->
            category.append(it)
            if (index != (categories.size - Int.ONE)) {
                category.append(", ")
            }
        }
        return category.toString()
    }

    private fun getPriceLevelString(priceLevel: PriceLevel): String {
        val price = StringBuilder()
        val color = "#" + Integer.toHexString(ContextCompat.getColor(itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950)).substring(GET_STRING_COLOR)

        if (priceLevel.fareCount.isMoreThanZero()){
            price.append("<font color=$color>")
        }

        for (i in Int.ZERO..COUNT_MAX_LEVEL_PRICE){
            price.append(priceLevel.icon)
            if(i == (priceLevel.fareCount - Int.ONE) && priceLevel.fareCount.isMoreThanZero()){
                price.append("</font>")
            }
        }

        return price.toString()
    }

    interface TokoFoodMerchantListListener {
        fun onClickMerchant(merchant: Merchant, position: Int)
        fun onImpressMerchant(merchant: Merchant, position: Int)
    }
}
