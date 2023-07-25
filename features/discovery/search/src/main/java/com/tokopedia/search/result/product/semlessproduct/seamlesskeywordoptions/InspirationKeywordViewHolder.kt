package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchInspirationSemlessKeywordBinding
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive

class InspirationKeywordViewHolder(
    itemView: View,
    private val inspirationCarouselListener: InspirationKeywordListener,
) : AbstractViewHolder<InspirationKeyboardCardView>(itemView), CoroutineScope {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_inspiration_semless_keyword
    }

    private var binding: SearchInspirationSemlessKeywordBinding? by viewBinding()
    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: InspirationKeyboardCardView?) {
        if (binding == null || element == null) return
        binding?.inspirationKeywordOptionList?.let {
            it.layoutManager = createLayoutManager()
            it.adapter = InspirationKeywordAdapter(element.optionsItems, inspirationCarouselListener)
        }
    }

    override fun onViewRecycled() {
        cancelJobs()
        super.onViewRecycled()
    }

    private fun cancelJobs() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.children.map { it.cancel() }
        }
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
    }
}
