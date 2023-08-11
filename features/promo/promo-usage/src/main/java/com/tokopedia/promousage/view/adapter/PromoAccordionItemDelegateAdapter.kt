package com.tokopedia.promousage.view.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoBinding
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.unifycomponents.toPx

class PromoAccordionItemDelegateAdapter(
    private val onClickPromo: (PromoItem) -> Unit
) : DelegateAdapter<PromoItem, PromoAccordionItemDelegateAdapter.ViewHolder>(
    PromoItem::class.java
) {

    companion object {
        private const val NORMAL_MARGIN_TOP_IN_DP = 6
        private const val NORMAL_MARGIN_BOTTOM_IN_DP = 6
        private const val NORMAL_MARGIN_START_END_IN_DP = 16
        private const val LAST_RECOMMENDED_MARGIN_BOTTOM_IN_DP = 16
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemPromoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemPromoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PromoItem) {
            with(binding) {
                if (item.isRecommended) {
                    clPromoBackground.background = ColorDrawable(
                        ContextCompat
                            .getColor(root.context, android.R.color.transparent)
                    )
                    if (item.isLastRecommended) {
                        vcvPromo.setMargin(
                            NORMAL_MARGIN_START_END_IN_DP.toPx(),
                            NORMAL_MARGIN_TOP_IN_DP.toPx(),
                            NORMAL_MARGIN_START_END_IN_DP.toPx(),
                            LAST_RECOMMENDED_MARGIN_BOTTOM_IN_DP.toPx()
                        )
                    } else {
                        vcvPromo.setMargin(
                            NORMAL_MARGIN_START_END_IN_DP.toPx(),
                            NORMAL_MARGIN_TOP_IN_DP.toPx(),
                            NORMAL_MARGIN_START_END_IN_DP.toPx(),
                            NORMAL_MARGIN_BOTTOM_IN_DP.toPx()
                        )
                    }
                } else {
                    clPromoBackground.background = ColorDrawable(
                        ContextCompat
                            .getColor(root.context,
                                com.tokopedia.unifyprinciples.R.color.Unify_Background)
                    )
                    vcvPromo.setMargin(
                        NORMAL_MARGIN_START_END_IN_DP.toPx(),
                        NORMAL_MARGIN_TOP_IN_DP.toPx(),
                        NORMAL_MARGIN_START_END_IN_DP.toPx(),
                        NORMAL_MARGIN_BOTTOM_IN_DP.toPx()
                    )
                }
                vcvPromo.setOnClickListener {
                    when (item.state) {
                        is PromoItemState.Normal, is PromoItemState.Selected -> {
                            onClickPromo(item)
                        }

                        else -> {
                            // no-op
                        }
                    }
                }
                vcvPromo.bind(item)
                vcvPromo.isVisible = item.isExpanded && item.isVisible
            }
        }
    }
}
