package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcFilterTabWidgetBinding
import com.tokopedia.sellerhomecommon.domain.model.TabModel
import com.tokopedia.sellerhomecommon.presentation.model.FilterTabWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class FilterTabViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<FilterTabWidgetUiModel>(view) {

    private val binding by lazy { ShcFilterTabWidgetBinding.bind(itemView) }

    override fun bind(element: FilterTabWidgetUiModel) {
        with(binding) {
            val selectedTab =
                element.filterTabs?.firstOrNull { it.page == element.selectedFilterPage }
            selectedTab?.let { tab ->
                val filterTabMessage = HtmlUtil.fromHtml(element.filterTabMessage)
                tvFilterTabTitle.text = filterTabMessage
                showChevronIcon()

                root.setOnClickListener {
                    listener.onFilterClicked(
                        element.filterTabs,
                        tab.page,
                        filterTabMessage.toString()
                    )
                }
            }
        }
    }

    private fun showChevronIcon() {
        with(binding) {
            tvFilterTabTitle.clearUnifyDrawableEnd()
            tvFilterTabTitle.setUnifyDrawableEnd(
                iconId = IconUnify.CHEVRON_DOWN,
                colorIcon = root.context.getResColor(unifyprinciplesR.color.Unify_NN950),
                width = root.context.dpToPx(18),
                height = root.context.dpToPx(18)
            )
        }
    }

    interface Listener : BaseViewHolderListener {
        fun onFilterClicked(
            tabs: List<TabModel>?,
            selectedPage: String?,
            title: String
        ) {
        }
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_filter_tab_widget
    }

}
