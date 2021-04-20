package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationItemUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_item_recommendation.view.*

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class WidgetRecommendationItemAdapter(
        private val items: List<RecommendationItemUiModel>,
        private val onItemClick: (RecommendationItemUiModel) -> Unit
) : RecyclerView.Adapter<WidgetRecommendationItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.shc_item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClick)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RecommendationItemUiModel, onItemClick: (RecommendationItemUiModel) -> Unit) {
            with(itemView) {
                tvShcRecommendationItem.text = item.text.parseAsHtml()
                setupIconType(item)

                if (item.appLink.isBlank()) {
                    tvShcRecommendationItem.clearUnifyDrawableEnd()
                } else {
                    val dp24 = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
                    tvShcRecommendationItem.setUnifyDrawableEnd(IconUnify.CHEVRON_RIGHT, width = dp24, height = dp24)
                    setOnClickListener {
                        RouteManager.route(context, item.appLink)
                        onItemClick(item)
                    }
                }
            }
        }

        private fun setupIconType(item: RecommendationItemUiModel) = with(itemView) {
            when (item.type) {
                RecommendationItemUiModel.TYPE_POSITIVE -> {
                    val iconColor = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
                    icShcRecommendationItemType.setImage(newIconId = IconUnify.CHECK, newLightEnable = iconColor)
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_positive)
                }
                RecommendationItemUiModel.TYPE_NEGATIVE -> {
                    val iconColor = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R500)
                    icShcRecommendationItemType.setImage(newIconId = IconUnify.WARNING, newLightEnable = iconColor)
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_negative)
                    val padding = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                    icShcRecommendationItemType.setPadding(padding, padding, padding, padding)
                }
                else -> {
                    val iconColor = context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                    icShcRecommendationItemType.setImage(newIconId = IconUnify.INFORMATION, newLightEnable = iconColor)
                    icShcRecommendationItemType.setBackgroundResource(R.drawable.bg_recommendation_no_data)
                    val padding = context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                    icShcRecommendationItemType.setPadding(padding, padding, padding, padding)
                }
            }
        }
    }
}