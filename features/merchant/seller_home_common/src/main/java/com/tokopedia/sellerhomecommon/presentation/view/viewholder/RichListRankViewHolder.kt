package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListRankItemBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.model.TooltipListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListRankViewHolder(
    itemView: View,
    private val onTooltipClicked: (TooltipUiModel) -> Unit
) : AbstractViewHolder<BaseRichListItem.RankItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_rank_item
        private const val DOWN_TREND_ROTATION = 90f
        private const val UP_TREND_ROTATION = 270f
    }

    private val binding: ShcRichListRankItemBinding? by viewBinding()

    override fun bind(element: BaseRichListItem.RankItemUiModel) {
        setupView(element)
    }

    private fun setupView(element: BaseRichListItem.RankItemUiModel) {
        binding?.run {
            tvShcRankTitle.text = element.title
            tvShcRankSubTitle.text = element.subTitle
            tvShcRankSubTitle.text = element.subTitle
            tvShcRankNote.text = element.rankNote
            tvShcRankValue.text = element.rankValue
            setupTrendView(element.rankTrend)
            setupTooltip(element.tooltip, element.rankTrend)
        }
    }

    private fun setupTooltip(
        tooltip: TooltipUiModel?,
        rankTrend: BaseRichListItem.RankItemUiModel.RankTrend,
    ) {
        binding?.run {
            if (tooltip == null) {
                tvShcRankSubTitle.clearUnifyDrawableEnd()
            } else {
                val iconColor =
                    if (rankTrend == BaseRichListItem.RankItemUiModel.RankTrend.DISABLED) {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    } else {
                        com.tokopedia.unifyprinciples.R.color.Unify_NN900
                    }
                tvShcRankSubTitle.setUnifyDrawableEnd(
                    iconId = IconUnify.INFORMATION,
                    colorIcon = root.context.getResColor(iconColor)
                )
                tvShcRankSubTitle.setOnClickListener {
                    onTooltipClicked(tooltip)
                }
            }
        }
    }

    private fun setupTrendView(trend: BaseRichListItem.RankItemUiModel.RankTrend) {
        binding?.run {
            when (trend) {
                BaseRichListItem.RankItemUiModel.RankTrend.UP -> {
                    showEnabledMode()
                    imgShcRankTrend.visible()
                    imgShcRankTrend.setImage(
                        newIconId = IconUnify.PLAY,
                        newLightEnable = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                    )
                    imgShcRankTrend.rotation = UP_TREND_ROTATION
                }
                BaseRichListItem.RankItemUiModel.RankTrend.DOWN -> {
                    showEnabledMode()
                    imgShcRankTrend.visible()
                    imgShcRankTrend.setImage(
                        newIconId = IconUnify.PLAY,
                        newLightEnable = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
                    )
                    imgShcRankTrend.rotation = DOWN_TREND_ROTATION
                }
                BaseRichListItem.RankItemUiModel.RankTrend.NONE -> {
                    showEnabledMode()
                    imgShcRankTrend.gone()
                }
                BaseRichListItem.RankItemUiModel.RankTrend.DISABLED -> {
                    showDisabledMode()
                    imgShcRankTrend.gone()
                }
            }
        }
    }

    private fun showDisabledMode() {
        binding?.run {
            tvShcRankNote.setTextColor(
                root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
            tvShcRankValue.setTextColor(
                root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        }
    }

    private fun showEnabledMode() {
        binding?.run {
            tvShcRankNote.setTextColor(
                root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
            tvShcRankValue.setTextColor(
                root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_NN600
                )
            )
        }
    }
}
