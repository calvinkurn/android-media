package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductLastFilterViewHolderBinding
import com.tokopedia.search.result.presentation.model.LastFilterDataView
import com.tokopedia.search.result.presentation.view.listener.LastFilterListener
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class LastFilterViewHolder(
    itemView: View,
    private val lastFilterListener: LastFilterListener,
): AbstractViewHolder<LastFilterDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_last_filter_view_holder
    }

    private var binding: SearchResultProductLastFilterViewHolderBinding? by viewBinding()

    override fun bind(element: LastFilterDataView) {
        val binding = binding ?: return

        binding.root.addOnImpressionListener(element.impressHolder) {
            lastFilterListener.onImpressedLastFilter(element)
        }
        binding.searchLastFilterHeaderView.safeSetBackground(getSearchLastFilterHeader())
        binding.searchLastFilterTitle.text = element.title
        binding.searchLastFilterName.text = element.optionNames()
        binding.searchLastFilterUseLastFilter.setOnClickListener {
            lastFilterListener.applyLastFilter(element)
        }
        binding.searchLastFilterClose.setOnClickListener {
            lastFilterListener.closeLastFilter(element)
        }
    }

    private fun View.safeSetBackground(drawable: Drawable?) {
        try {
            background = drawable
        } catch (throwable: Throwable) {
            Timber.w(throwable)
        }
    }

    private fun getSearchLastFilterHeader() =
        ContextCompat.getDrawable(
            itemView.context,
            R.drawable.search_last_filter_card_header
        )
}