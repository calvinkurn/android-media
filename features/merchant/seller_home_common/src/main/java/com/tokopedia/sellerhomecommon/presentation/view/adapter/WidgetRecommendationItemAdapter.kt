package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemRecommendationBinding
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationItemUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class WidgetRecommendationItemAdapter(
    private val items: List<RecommendationItemUiModel>,
    private val onItemClick: (RecommendationItemUiModel) -> Unit
) : RecyclerView.Adapter<WidgetRecommendationItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShcItemRecommendationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClick)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ShcItemRecommendationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RecommendationItemUiModel,
            onItemClick: (RecommendationItemUiModel) -> Unit
        ) {
            with(binding) {
                tvShcRecommendationItem.text = item.text.parseAsHtml()
                setupIconType(item)

                if (item.appLink.isBlank()) {
                    tvShcRecommendationItem.clearUnifyDrawableEnd()
                } else {
                    val dp24 = root.context.resources.getDimension(
                        com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                    )
                    tvShcRecommendationItem.setUnifyDrawableEnd(
                        IconUnify.CHEVRON_RIGHT,
                        width = dp24,
                        height = dp24
                    )
                }

                root.setOnClickListener {
                    if (item.appLink.isNotBlank()) {
                        RouteManager.route(root.context, item.appLink)
                    }
                    onItemClick(item)
                }
            }
        }

        private fun setupIconType(item: RecommendationItemUiModel) = with(binding) {
            when (item.type) {
                RecommendationItemUiModel.TYPE_POSITIVE -> {
                    val iconColor = root.context.getResColor(R.color.shc_static_g400_dms)
                    icShcRecommendationItemType.setImage(
                        newIconId = IconUnify.CHECK,
                        newLightEnable = iconColor
                    )
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_positive)
                }
                RecommendationItemUiModel.TYPE_NEGATIVE -> {
                    val iconColor = root.context.getResColor(R.color.shc_static_r500_dms)
                    icShcRecommendationItemType.setImage(
                        newIconId = IconUnify.WARNING,
                        newLightEnable = iconColor
                    )
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_negative)
                    val padding = root.context.resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
                    )
                    icShcRecommendationItemType.setPadding(padding, padding, padding, padding)
                }
                else -> {
                    val iconColor = root.context.getResColor(R.color.shc_static_n700_68_dms)
                    icShcRecommendationItemType.setImage(
                        newIconId = IconUnify.INFORMATION,
                        newLightEnable = iconColor
                    )
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_no_data)
                    val padding = root.context.resources.getDimensionPixelSize(
                        com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2
                    )
                    icShcRecommendationItemType.setPadding(padding, padding, padding, padding)
                }
            }
        }
    }
}