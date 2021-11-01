package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcCardWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created By @ilhamsuaib on 19/05/20
 */

class CardViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<CardWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_card_widget
    }

    private val binding by lazy {
        ShcCardWidgetBinding.bind(itemView)
    }

    override fun bind(element: CardWidgetUiModel) {
        observeState(element)

        binding.tvCardTitle.text = element.title
    }

    private fun observeState(element: CardWidgetUiModel) {
        val data = element.data
        when {
            null == data -> {
                showViewComponent(element, false)
                showOnError(false)
                showShimmer(true)
            }
            data.error.isNotBlank() -> {
                showShimmer(false)
                showOnError(true)
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> {
                showOnError(false)
                showShimmer(false)
                showViewComponent(element, true)
            }
        }
    }

    private fun showViewComponent(element: CardWidgetUiModel, isShown: Boolean) {
        var shouldLoadAnimation = false
        with(binding) {
            val visibility = if (isShown) View.VISIBLE else View.INVISIBLE
            tvCardTitle.visibility = visibility
            tvCardValue.visibility = visibility
            tvCardSubValue.visibility = visibility
            val value = element.data?.value
            val previousValue = element.data?.previousValue
            if (isShown) {
                val shownValue = if (value.isNullOrBlank()) "0" else value
                element.data?.previousValue = shownValue
                if (previousValue?.equals(value) == false) {
                    shouldLoadAnimation = true
                    tvCardValue.invisible()
                    shcCardValueCountdownView.run {
                        visible()
                        setValue(previousValue, shownValue)
                    }
                } else {
                    shouldLoadAnimation = false
                    tvCardValue.visible()
                    shcCardValueCountdownView.invisible()
                    tvCardValue.text = shownValue.parseAsHtml()
                }
            }
        }

        setTagNotification(element.tag)

        if (!isShown) return

        with(binding) {
            if (element.appLink.isNotBlank()) {
                val selectableItemBg = TypedValue()
                root.context.theme.resolveAttribute(
                    android.R.attr.selectableItemBackground,
                    selectableItemBg, true
                )
                containerCard.setBackgroundResource(selectableItemBg.resourceId)
            } else {
                containerCard.setBackgroundColor(root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }

            tvCardTitle.text = element.title
            if (shouldLoadAnimation) {
                tvCardValue.invisible()
            } else {
                tvCardValue.visible()
                tvCardValue.text = (element.data?.value ?: "0").parseAsHtml()
            }
            tvCardSubValue.text = element.data?.description?.parseAsHtml()
            root.addOnImpressionListener(element.impressHolder) {
                listener.sendCardImpressionEvent(element)
            }

            root.setOnClickListener {
                if (element.appLink.isNotBlank()) {
                    if (RouteManager.route(root.context, element.appLink)) {
                        listener.sendCardClickTracking(element)
                    }
                }
            }

            showCardState(element.data)
        }
    }

    private fun showCardState(data: CardDataUiModel?) {
        with(binding.imgShcCardState) {
            when (data?.state) {
                CardDataUiModel.State.WARNING, CardDataUiModel.State.DANGER -> {
                    visible()
                    loadImage(R.drawable.bg_shc_card_stata_warning)
                }
                CardDataUiModel.State.NORMAL -> gone()
            }
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagCard.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagCard.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun showOnError(isError: Boolean) {
        if (!isError) return
        with(binding) {
            tvCardTitle.visible()
            tvCardValue.visible()
            tvCardValue.text = root.context.getString(R.string.shc_load_failed)
            tvCardSubValue.text = ""
        }
    }

    private fun showShimmer(isLoading: Boolean) {
        with(binding) {
            val visibility = if (isLoading) View.VISIBLE else View.GONE
            shimmerCardTitle.visibility = visibility
            shimmerCardValue.visibility = visibility
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendCardImpressionEvent(model: CardWidgetUiModel) {}

        fun sendCardClickTracking(model: CardWidgetUiModel) {}
    }
}