package com.tokopedia.tokofood.feature.search.searchresult.presentation.adapter.viewholder

import android.view.View
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
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.AdditionalData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.common.domain.response.PriceLevel
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.common.util.TokofoodExt.clickWithDebounce
import com.tokopedia.tokofood.databinding.ItemTokofoodSearchSrpCardBinding
import com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel.MerchantSearchResultUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import java.lang.StringBuilder

class MerchantSearchResultViewHolder(
    itemView: View,
    private val listener: TokoFoodMerchantSearchResultListener? = null,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
) : AbstractViewHolder<MerchantSearchResultUiModel>(itemView) {

    private val binding: ItemTokofoodSearchSrpCardBinding? by viewBinding()

    override fun bind(element: MerchantSearchResultUiModel) {
        setMerchantLayout(element.merchant)
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
        setOtherBranchButton(merchant)
        addImpressionListener(merchant)
        setOnClickListener(merchant)
    }

    private fun setImageMerchant(imageUrl: String) {
        binding?.imgTokofoodItemSrpMerchant?.setImageUrl(imageUrl)
    }

    private fun setTitleMerchant(title: String) {
        if (title.isBlank()) {
            binding?.tgTokofoodItemSrpMerchantTitle?.hide()
        } else {
            binding?.tgTokofoodItemSrpMerchantTitle?.show()
            binding?.tgTokofoodItemSrpMerchantTitle?.text = title
        }
    }

    private fun setPromoInfo(promo: String, additionalData: AdditionalData) {
        setPromoRibbonLabel(additionalData.topTextBanner)
        binding?.run {
            if (promo.isBlank()) {
                ivItemSrpMerchantDiscount.hide()
                tvItemSrpMerchantPromoDetail.hide()
            } else {
                ivItemSrpMerchantDiscount.run {
                    show()
                    setImageUrl(additionalData.discountIcon)
                }
                tvItemSrpMerchantPromoDetail.run {
                    show()
                    text = promo
                }
            }
        }
    }

    private fun setPromoRibbonLabel(topTextBanner: String) {
        binding?.run {
            if (topTextBanner.isBlank()) {
                ribbonTokofoodPromo.hide()
            } else {
                ribbonTokofoodPromo.run {
                    show()
                    setRibbonText(topTextBanner)
                }
            }
        }
    }

    private fun setMerchantDistance(distance: String, eta: String) {
        val etaDistance = StringBuilder()
        if (distance.isNotEmpty()) {
            etaDistance.append(distance)
        }

        if (eta.isNotEmpty()) {
            etaDistance.append(" - ")
            etaDistance.append(eta)
        }

        if (etaDistance.toString().isEmpty()) {
            binding?.tgTokofoodItemSrpMerchantDistance?.hide()
        } else {
            binding?.tgTokofoodItemSrpMerchantDistance?.show()
            binding?.tgTokofoodItemSrpMerchantDistance?.text = etaDistance
        }
    }

    private fun setMerchantRating(rating: String, ratingDouble: Double) {
        if (rating.isBlank() && ratingDouble <= 0f) {
            binding?.tgTokofoodItemSrpMerchantRating?.hide()
            binding?.imgTokofoodItemSrpMerchantRating?.hide()
        } else {
            binding?.tgTokofoodItemSrpMerchantRating?.show()
            binding?.imgTokofoodItemSrpMerchantRating?.show()
            binding?.tgTokofoodItemSrpMerchantRating?.text = rating
        }
    }

    private fun setViewDividerCategoryPriceLevel(categories: List<String>, priceLevel: PriceLevel) {
        val price = getPriceLevelString(priceLevel)
        val category = getCategoryString(categories)
        if (category.isEmpty() || price.isEmpty() || priceLevel.fareCount <= 0) {
            binding?.viewDividerTokofoodItemSrpMerchant?.hide()
        } else {
            binding?.viewDividerTokofoodItemSrpMerchant?.show()
        }
    }

    private fun setMerchantCategory(categories: List<String>, priceLevel: PriceLevel) {
        val category = getCategoryString(categories)
        if (category.isEmpty()) {
            binding?.tgTokofoodItemSrpMerchantCategory?.hide()
        } else {
            binding?.tgTokofoodItemSrpMerchantCategory?.show()
            binding?.tgTokofoodItemSrpMerchantCategory?.text = category

            binding?.tgTokofoodItemSrpMerchantCategory?.run {
                if (priceLevel.fareCount <= Int.ZERO) {
                    setNoPriceLevel()
                } else {
                    setExistedPriceLevel()
                }
            }
        }
    }

    private fun Typography.setNoPriceLevel() {
        setMargin(
            getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        )
        setNoPriceLevelLayoutParams()
    }

    private fun Typography.setExistedPriceLevel() {
        setMargin(
            getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
            getDimens(com.tokopedia.unifyprinciples.R.dimen.unify_space_0)
        )
        setExistedPriceLevelLayoutParams()
    }

    private fun Typography.setNoPriceLevelLayoutParams() {
        val labelParams = this.layoutParams as? ConstraintLayout.LayoutParams

        labelParams?.topToTop = ConstraintLayout.LayoutParams.UNSET
        labelParams?.startToEnd = ConstraintLayout.LayoutParams.UNSET
        labelParams?.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
        labelParams?.startToEnd =
            binding?.imgTokofoodItemSrpMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
        labelParams?.topToBottom =
            binding?.tgTokofoodItemSrpMerchantTitle?.id ?: ConstraintLayout.LayoutParams.UNSET

        layoutParams = labelParams
    }

    private fun Typography.setExistedPriceLevelLayoutParams() {
        val labelParams = this.layoutParams as? ConstraintLayout.LayoutParams

        labelParams?.startToEnd = ConstraintLayout.LayoutParams.UNSET
        labelParams?.topToBottom = ConstraintLayout.LayoutParams.UNSET
        labelParams?.topToTop =
            binding?.viewDividerTokofoodItemSrpMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
        labelParams?.startToEnd =
            binding?.viewDividerTokofoodItemSrpMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET
        labelParams?.bottomToBottom =
            binding?.viewDividerTokofoodItemSrpMerchant?.id ?: ConstraintLayout.LayoutParams.UNSET

        layoutParams = labelParams
    }

    private fun setPriceLevel(priceLevel: PriceLevel) {
        val price = getPriceLevelString(priceLevel)
        if (price.isEmpty() || priceLevel.fareCount <= 0) {
            binding?.tgTokofoodItemSrpMerchantPriceScale?.hide()
        } else {
            binding?.tgTokofoodItemSrpMerchantPriceScale?.show()
            binding?.tgTokofoodItemSrpMerchantPriceScale?.text = MethodChecker.fromHtml(price)
        }
    }

    private fun setMerchantClosed(isClosed: Boolean) {
        if (isClosed) {
            binding?.labelItemSrpMerchantClosed?.show()
        } else {
            binding?.labelItemSrpMerchantClosed?.hide()
        }
    }

    private fun setOtherBranchButton(merchant: Merchant) {
        binding?.btnTokofoodItemSrpBranch?.run {
            showWithCondition(merchant.hasBranch)
            clickWithDebounce {
                listener?.onBranchButtonClicked(merchant)
            }
        }
    }

    private fun addImpressionListener(merchant: Merchant) {
        binding?.root?.addAndReturnImpressionListener(merchant, tokofoodScrollChangedListener) {
            listener?.onImpressMerchant(merchant, bindingAdapterPosition)
        }
    }

    private fun setOnClickListener(merchant: Merchant) {
        binding?.root?.clickWithDebounce {
            listener?.onClickMerchant(merchant, bindingAdapterPosition)
        }
    }

    private fun getCategoryString(categories: List<String>): String {
        return categories.joinToString()
    }

    private fun getPriceLevelString(priceLevel: PriceLevel): String {
        val price = StringBuilder()
        val color = "#" + Integer.toHexString(
            ContextCompat.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        ).substring(
            GET_STRING_COLOR
        )

        if (priceLevel.fareCount.isMoreThanZero()) {
            price.append("<font color=$color>")
        }

        for (i in Int.ZERO..COUNT_MAX_LEVEL_PRICE) {
            price.append(priceLevel.icon)
            if (i == (priceLevel.fareCount - Int.ONE) && priceLevel.fareCount.isMoreThanZero()) {
                price.append("</font>")
            }
        }

        return price.toString()
    }

    interface TokoFoodMerchantSearchResultListener {
        fun onClickMerchant(merchant: Merchant, position: Int)
        fun onImpressMerchant(merchant: Merchant, position: Int)
        fun onBranchButtonClicked(merchant: Merchant)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_search_srp_card

        private const val COUNT_MAX_LEVEL_PRICE = 3
        private const val GET_STRING_COLOR = 2
    }

}
