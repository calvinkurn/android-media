package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.model.RichListDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RichListAdapter

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListViewHolder(
    itemView: View, private val listener: Listener
) : AbstractViewHolder<RichListWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_widget
        private const val EVENT_LABEL_FORMAT = "%s - %s - %s"
    }

    private var element: RichListWidgetUiModel? = null
    private val binding by lazy {
        ShcRichListWidgetBinding.bind(itemView)
    }
    private val richlistAdapter by lazy { RichListAdapter(getAdapterListener()) }
    private val layoutManager by lazy {
        object : LinearLayoutManager(itemView.context) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun bind(element: RichListWidgetUiModel) {
        this.element = element
        setupRecyclerView()
        val data = element.data
        when {
            null == data || element.showLoadingState -> showLoadingState()
            data.error.isNotBlank() -> {
                showErrorState()
                listener.setOnErrorWidget(absoluteAdapterPosition, element, data.error)
            }
            else -> showSuccessState(element)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            if (rvShcRichList.layoutManager == null || rvShcRichList.adapter == null) {
                rvShcRichList.layoutManager = layoutManager
                rvShcRichList.adapter = richlistAdapter
            }
        }
    }

    private fun showSuccessState(element: RichListWidgetUiModel) {
        val items = element.data?.richListData.orEmpty()
        showItems(items)
        setupLastUpdated(element.data?.lastUpdated.orEmpty())
        showTitle(
            title = element.data?.title.orEmpty(), subtitle = element.data?.subtitle.orEmpty()
        )
        sendImpressionTrackingEvent(element)
    }

    private fun sendImpressionTrackingEvent(element: RichListWidgetUiModel) {
        element.data?.let {
            binding.root.addOnImpressionListener(element.impressHolder) {
                listener.sendRichListImpressionEvent(getEventLabel(it))
            }
        }
    }

    private fun getEventLabel(data: RichListDataUiModel): String {
        return if (getIsEligibleSeller(data)) {
            val rank = getRank(data.richListData)
            val ranking = rank?.rankValue.orEmpty()
            val point = rank?.title.orEmpty()
            String.format(EVENT_LABEL_FORMAT, point, ranking, data.lastUpdated)
        } else {
            String.EMPTY
        }
    }

    private fun getRank(richListData: List<BaseRichListItem>): BaseRichListItem.RankItemUiModel? {
        return richListData.firstOrNull {
            it is BaseRichListItem.RankItemUiModel
        } as? BaseRichListItem.RankItemUiModel
    }

    private fun getIsEligibleSeller(data: RichListDataUiModel?): Boolean {
        return !data?.richListData?.any { it is BaseRichListItem.TickerItemUiModel }.orFalse()
    }

    private fun showTitle(title: String, subtitle: String) {
        with(binding) {
            if (title.isNotBlank()) {
                tvShcRichListTitle.visible()
                tvShcRichListSubTitle.visible()
                tvShcRichListTitle.text = title
                tvShcRichListSubTitle.text = subtitle.parseAsHtml()
            }
        }
    }

    private fun setupLastUpdated(lastUpdated: String) {
        with(binding) {
            val lastUpdatedFmt =
                root.context.getString(R.string.shc_rich_list_last_updated, lastUpdated)
            if (lastUpdated.isBlank()) {
                tvShcRichListUpdated.gone()
            } else {
                tvShcRichListUpdated.visible()
                tvShcRichListUpdated.text = lastUpdatedFmt
            }
        }
    }

    private fun showErrorState() {
        with(binding) {
            tvShcRichListUpdated.gone()
            tvShcRichListTitle.gone()
            tvShcRichListSubTitle.gone()
        }
        showItems(listOf(BaseRichListItem.ErrorStateUiModel))
    }

    private fun showLoadingState() {
        with(binding) {
            tvShcRichListUpdated.gone()
            tvShcRichListTitle.gone()
            tvShcRichListSubTitle.gone()
        }
        showItems(listOf(BaseRichListItem.LoadingStateUiModel))
    }

    private fun showItems(items: List<BaseRichListItem>) {
        if (richlistAdapter.data != items) {
            richlistAdapter.clearAllElements()
            richlistAdapter.addElement(items)
            richlistAdapter.notifyItemRangeChanged(Int.ZERO, items.size.minus(Int.ONE))
        }
    }

    private fun getAdapterListener(): RichListAdapter.Listener {
        return object : RichListAdapter.Listener {
            override fun setOnCtaClicked(appLink: String) {
                RouteManager.route(itemView.context, appLink)
                element?.data?.let {
                    listener.sendRichListCtaClickEvent(getEventLabel(it))
                }
            }

            override fun setOnTooltipClicked(tooltip: TooltipUiModel) {
                listener.onTooltipClicked(tooltip)
            }

            override fun setOnReloadClicked() {
                element?.let {
                    listener.onReloadWidget(it)
                }
            }
        }
    }

    interface Listener : BaseViewHolderListener {
        fun sendRichListImpressionEvent(eventLabel: String) {}
        fun sendRichListCtaClickEvent(eventLabel: String) {}
    }
}