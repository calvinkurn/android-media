package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.balance.item

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.analytics.v2.BalanceWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.databinding.LayoutDynamicBalanceItemBinding
import com.tokopedia.home.R as homeR
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class BalanceItemViewHolder(
    v: View,
    private val listener: HomeCategoryListener
): AbstractViewHolder<BalanceItemUiModel>(v) {
    companion object {
        @LayoutRes
        val LAYOUT = homeR.layout.layout_dynamic_balance_item
    }

    private val binding: LayoutDynamicBalanceItemBinding? by viewBinding()

    override fun bind(
        model: BalanceItemUiModel,
    ) {
        val binding = binding ?: return
        binding.textBalance.text = model.text
        binding.iconBalance.loadImage(model.imageUrl)
        binding.containerBalance.handleClick(model)
    }

    private fun View.handleClick(model: BalanceItemUiModel) {
        setOnClickListener {
            when(model.contentType) {
                is BalanceItemVisitable.ContentType.GoPay -> {
                    if (model.contentType.isLinked) {
                        BalanceWidgetTracking.sendClickGopayLinkedWidgetTracker(
                            balancePoints = model.text,
                            userId = listener.userId
                        )
                    } else {
                        BalanceWidgetTracking.sendClickGopayNotLinkedWidgetTracker(
                            userId = listener.userId
                        )
                    }
                    listener.onSectionItemClicked(model.url)
                }
                is BalanceItemVisitable.ContentType.Rewards -> {
                    BalanceWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(
                        listener.userId
                    )
                    listener.actionTokoPointClicked(
                        model.applink,
                        model.url,
                        "Tokopedia"
                    )
                }

                else -> { }
            }
        }
    }
}
