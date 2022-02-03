package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.util.TypedValue
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
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
        private const val ZERO_STR = "0"
    }

    private val binding by lazy {
        ShcCardWidgetBinding.bind(itemView)
    }

    override fun bind(element: CardWidgetUiModel) {
        binding.tvCardTitle.text = element.title
        observeState(element)
    }

    private fun setupTag(element: CardWidgetUiModel) {
        with(binding) {
            val isTagVisible = element.tag.isNotBlank()
            notifTagCard.isVisible = isTagVisible
            if (isTagVisible) {
                notifTagCard.setNotification(
                    element.tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun observeState(element: CardWidgetUiModel) {
        val data = element.data
        when {
            null == data -> showLoadingState(element)
            data.error.isNotBlank() -> {
                showShimmer(false)
                showOnError(true)
                listener.setOnErrorWidget(adapterPosition, element, data.error)
                setupTag(element)
            }
            else -> {
                showOnError(false)
                showShimmer(false)
                showViewComponent(element, true)
                setupTag(element)
            }
        }
    }

    private fun showLoadingState(element: CardWidgetUiModel) {
        showViewComponent(element, false)
        showOnError(false)
        showShimmer(true)
    }

    private fun showViewComponent(element: CardWidgetUiModel, isShown: Boolean) {
        var shouldLoadAnimation = false
        with(binding) {
            tvCardTitle.isVisible = isShown
            tvCardValue.isVisible = isShown
            tvCardSubValue.isVisible = isShown
            val value = element.data?.value
            val previousValue = element.data?.previousValue
            if (isShown) {
                val shownValue = if (value.isNullOrBlank()) ZERO_STR else value
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
                setupRefreshButton(element)
            }
        }

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

            if (shouldLoadAnimation) {
                tvCardValue.invisible()
            } else {
                tvCardValue.visible()
                tvCardValue.text = (element.data?.value ?: ZERO_STR).parseAsHtml()
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

    private fun setupRefreshButton(element: CardWidgetUiModel) {
        with(binding) {
            element.data?.lastUpdated?.let {
                val shouldShowRefreshButton = it.shouldShow.orFalse()
                icShcRefreshCard.isVisible = shouldShowRefreshButton && it.isEnabled
                icShcRefreshCard.setOnClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: CardWidgetUiModel) {
        showLoadingState(element)
        listener.onReloadWidget(element)
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
            if (isLoading) {
                icShcRefreshCard.gone()
            }
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendCardImpressionEvent(model: CardWidgetUiModel) {}

        fun sendCardClickTracking(model: CardWidgetUiModel) {}
    }
}
