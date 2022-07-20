package com.tokopedia.shop.score.performance.presentation.widget

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.databinding.ItemDetailPerformanceBinding
import com.tokopedia.shop.score.databinding.ItemDetailShopPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.BaseDetailPerformanceUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class DetailPerformanceWidget : ConstraintLayout {

    val binding: ItemDetailShopPerformanceBinding?
        get() = _binding

    private var _binding: ItemDetailShopPerformanceBinding? = null
    private var itemShopPerformanceListener: ItemShopPerformanceListener? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        _binding = ItemDetailShopPerformanceBinding.inflate(
            LayoutInflater.from(context), this, true
        )
    }

    fun setData(
        element: BaseDetailPerformanceUiModel?,
        shopPerformanceListener: ItemShopPerformanceListener,
        setCardItemDetailPerformanceBackground: () -> Unit
    ) {
        this.itemShopPerformanceListener = shopPerformanceListener
        binding?.run {
            setupItemDetailPerformance(element)
            val shopScore = element?.shopScore ?: ShopScoreConstant.SHOP_SCORE_NULL
            val titleBottomSheet =
                if (element?.titleDetailPerformance?.startsWith(
                        context.getString(R.string.desc_calculation_open_seller_app)
                    ) == true
                ) {
                    context.getString(R.string.desc_calculation_open_seller_app)
                } else {
                    if (element?.titleDetailPerformance?.contains(ShopScoreConstant.AND_TEXT) == true) {
                        element.titleDetailPerformance.replace(
                            ShopScoreConstant.AND_TEXT,
                            ShopScoreConstant.AND_SYMBOL
                        )
                    } else {
                        element?.titleDetailPerformance.orEmpty()
                    }
                }
            root.setOnClickListener {
                if (shopScore < ShopScoreConstant.SHOP_SCORE_ZERO) {
                    itemShopPerformanceListener?.onItemClickedToFaqClicked()
                } else {
                    itemShopPerformanceListener?.onItemClickedToDetailBottomSheet(
                        titleBottomSheet,
                        element?.identifierDetailPerformance.orEmpty()
                    )
                }
            }
            icItemPerformanceRight.setOnClickListener {
                if (shopScore < ShopScoreConstant.SHOP_SCORE_ZERO) {
                    itemShopPerformanceListener?.onItemClickedToFaqClicked()
                } else {
                    itemShopPerformanceListener?.onItemClickedToDetailBottomSheet(
                        titleBottomSheet,
                        element?.identifierDetailPerformance.orEmpty()
                    )
                }
            }
        }
        setContainerBackground()
        setCardItemDetailPerformanceBackground()
    }

    private fun setContainerBackground() {
        try {
            binding?.run {
                root.context?.let {
                    binding?.cardItemDetailShopPerformance?.setBackgroundColor(
                        it.getResColor(R.color.shop_score_penalty_dms_container)
                    )
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun setupItemDetailPerformance(
        element: BaseDetailPerformanceUiModel?
    ) {
        binding?.run {
            separatorItemDetail.showWithCondition(element?.isDividerHide == false)

            tvTitlePerformanceProgress.text = element?.titleDetailPerformance.orEmpty()
            tvPerformanceValue.text =
                if (element?.valueDetailPerformance == MINUS_SIGN) {
                    element.valueDetailPerformance
                } else {
                    if (element?.parameterValueDetailPerformance == DetailPerformanceWidget.PERCENT)
                        StringBuilder("${element.valueDetailPerformance}${element.parameterValueDetailPerformance}")
                    else
                        StringBuilder("${element?.valueDetailPerformance} ${element?.parameterValueDetailPerformance}")
                }
            if (element?.colorValueDetailPerformance?.isNotBlank() == true
                && element.valueDetailPerformance != MINUS_SIGN
            ) {
                tvPerformanceValue.setTextColorUnifyParameterDetail(element.colorValueDetailPerformance)
            }
            tvPerformanceTarget.text =
                if (element?.targetDetailPerformance.isNullOrBlank()) "" else {
                    context.getString(
                        R.string.item_detail_performance_target,
                        element?.targetDetailPerformance.orEmpty()
                    )
                }
        }
    }

    private fun Typography.setTextColorUnifyParameterDetail(colorValueDetailPerformance: String) {
        try {
            when (colorValueDetailPerformance) {
                getColorHexString(R.color.shop_score_item_parameter_dms_red) -> {
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_R600
                        )
                    )
                }
                getColorHexString(R.color.shop_score_item_parameter_dms_grey) -> {
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_N700
                        )
                    )
                }
                getColorHexString(R.color.shop_score_item_parameter_dms_green) -> {
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
                        )
                    )
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }

    private fun Typography.getColorHexString(idColor: Int): String {
        return try {
            val colorHexInt = ContextCompat.getColor(context, idColor)
            val colorToHexString = Integer.toHexString(colorHexInt).uppercase()
                .substring(START_INDEX_HEX_STRING)
            return "#$colorToHexString"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        const val PERCENT = "%"
        const val MINUS_SIGN = "-"
        const val START_INDEX_HEX_STRING = 2
        const val SIXTEEN_PADDING = 16
        const val ZERO_PADDING = 0
    }

}