package com.tokopedia.buy_more_get_more.minicart.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.diffutil.MiniCartDiffUtilCallback
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.itemdecoration.BmgmMiniCartItemDecoration
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 26/01/24.
 */

class StickyGiftView(
    context: Context, attrs: AttributeSet?
) : RecyclerView(context, attrs) {

    init {
        overScrollMode = OVER_SCROLL_NEVER
    }

    private var onItemClicked: (() -> Unit)? = null

    private val stickyAdapter by lazy {
        BmgmMiniCartAdapter(object : BmgmMiniCartAdapter.Listener {
            override fun setOnItemClickedListener() {
                onItemClicked?.invoke()
            }
        }).also(::setupRecyclerView)
    }

    fun setOnItemClickedListener(onItemClicked: () -> Unit) {
        this.onItemClicked = onItemClicked
    }

    fun submitList(items: List<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>) {
        val oldList = stickyAdapter.data.toList()
        val diffCallback = MiniCartDiffUtilCallback(oldList, items)
        val diffUtil = DiffUtil.calculateDiff(diffCallback)
        stickyAdapter.data.clear()
        stickyAdapter.data.addAll(items)
        diffUtil.dispatchUpdatesTo(stickyAdapter)
    }

    private fun setupRecyclerView(miniCartAdapter: BmgmMiniCartAdapter) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = miniCartAdapter
        if (itemDecorationCount == Int.ZERO) {
            addItemDecoration(BmgmMiniCartItemDecoration())
        }
    }
}