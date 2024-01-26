package com.tokopedia.buy_more_get_more.minicart.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.diffutil.MiniCartDiffUtilCallback
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 26/01/24.
 */

class StickyGiftView(
    context: Context, attrs: AttributeSet?
) : RecyclerView(context, attrs), BmgmMiniCartAdapter.Listener {

    private val stickyAdapter by lazy { BmgmMiniCartAdapter(this).also(::setupRecyclerView) }
    private var onItemClicked: (() -> Unit)? = null

    override fun setOnItemClickedListener() {
        onItemClicked?.invoke()
    }

    fun setOnItemClickedListener(onItemClicked: () -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun submitList(item: BmgmMiniCartVisitable.GwpGiftWidgetUiModel) {
        val newList = listOf(item)
        val oldList = stickyAdapter.data.toList()
        val diffCallback = MiniCartDiffUtilCallback(oldList, newList)
        val diffUtil = DiffUtil.calculateDiff(diffCallback)
        stickyAdapter.data.clear()
        stickyAdapter.data.addAll(newList)
        diffUtil.dispatchUpdatesTo(stickyAdapter)
    }

    private fun setupRecyclerView(miniCartAdapter: BmgmMiniCartAdapter) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = miniCartAdapter
    }
}