package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant.AND_SYMBOL
import com.tokopedia.shop.score.common.ShopScoreConstant.AND_TEXT
import com.tokopedia.shop.score.common.ShopScoreConstant.SHOP_AGE_SIXTY
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_detail_shop_performance.view.*
import timber.log.Timber

class ItemDetailPerformanceViewHolder(view: View,
                                      private val itemShopPerformanceListener: ItemShopPerformanceListener)
    : AbstractViewHolder<ItemDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_shop_performance
        const val PERCENT = "%"
        const val MINUS_SIGN = "-"
    }

    override fun bind(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            setupItemDetailPerformance(element)

            val titleBottomSheet = if (element?.titleDetailPerformance?.startsWith(getString(R.string.desc_calculation_open_seller_app)) == true) {
                getString(R.string.desc_calculation_open_seller_app)
            } else {
                if (element?.titleDetailPerformance?.contains(AND_TEXT) == true) {
                    element.titleDetailPerformance.replace(AND_TEXT, AND_SYMBOL)
                } else {
                    element?.titleDetailPerformance.orEmpty()
                }
            }
            setOnClickListener {
                if (element?.shopAge.orZero() < SHOP_AGE_SIXTY) {
                    itemShopPerformanceListener.onItemClickedToFaqClicked()
                } else {
                    itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                            titleBottomSheet,
                            element?.identifierDetailPerformance.orEmpty()
                    )
                }
            }
            ic_item_performance_right?.setOnClickListener {
                if (element?.shopAge.orZero() < SHOP_AGE_SIXTY) {
                    itemShopPerformanceListener.onItemClickedToFaqClicked()
                } else {
                    itemShopPerformanceListener.onItemClickedToDetailBottomSheet(
                            titleBottomSheet,
                            element?.identifierDetailPerformance.orEmpty()
                    )
                }
            }
        }
    }

    private fun setupItemDetailPerformance(element: ItemDetailPerformanceUiModel?) {
        with(itemView) {
            setContainerBackground()
            separatorItemDetail?.showWithCondition(element?.isDividerHide == false)

            if (element?.isDividerHide == true) {
                setCardItemDetailPerformanceBackground()
                cardItemDetailShopPerformance?.setPadding(16.toPx(), 0.toPx(), 16.toPx(), 16.toPx())
            } else {
                cardItemDetailShopPerformance?.setPadding(16.toPx(), 0.toPx(), 16.toPx(), 0.toPx())
            }
            tvTitlePerformanceProgress?.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue?.text =
                    if (element?.valueDetailPerformance == MINUS_SIGN) {
                        element.valueDetailPerformance
                    } else {
                        if (element?.parameterValueDetailPerformance == PERCENT)
                            StringBuilder("${element.valueDetailPerformance}${element.parameterValueDetailPerformance}")
                        else
                            StringBuilder("${element?.valueDetailPerformance} ${element?.parameterValueDetailPerformance}")
                    }
            if (element?.colorValueDetailPerformance?.isNotBlank() == true && element.valueDetailPerformance != MINUS_SIGN) {
                tvPerformanceValue.setTextColorUnifyParameterDetail(element.colorValueDetailPerformance)
            }
            tvPerformanceTarget?.text = getString(R.string.item_detail_performance_target, element?.targetDetailPerformance.orEmpty())
        }
    }

    private fun setContainerBackground() {
        try {
            with(itemView) {
                context?.let {
                    cardItemDetailShopPerformance?.setBackgroundColor(it.getResColor(R.color.shop_score_penalty_dms_container))
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun setCardItemDetailPerformanceBackground() {
        try {
            with(itemView) {
                context?.let {
                    cardItemDetailShopPerformance?.background = ContextCompat.getDrawable(context, R.drawable.corner_rounded_performance_list)
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun Typography.setTextColorUnifyParameterDetail(colorValueDetailPerformance: String) {
        try {
            when (colorValueDetailPerformance) {
                getColorHexString(R.color.shop_score_item_parameter_dms_grey) -> {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
                }
                getColorHexString(R.color.shop_score_item_parameter_dms_grey) -> {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
                }
                getColorHexString(R.color.shop_score_item_parameter_dms_green) -> {
                    setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun Typography.getColorHexString(idColor: Int): String {
        val colorHexInt = ContextCompat.getColor(context, idColor)
        return "#${Integer.toHexString(colorHexInt)}"
    }
}