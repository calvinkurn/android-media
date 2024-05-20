package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareThumbnailWidgetBinding
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.internal.TWO_SQUARE_LIMIT
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.ItemContentCardAdapter
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType
import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ThumbnailWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?,
    private val listener: ContainerMultiTwoSquareListener
) : AbstractViewHolder<ThumbnailWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareThumbnailWidgetBinding? by viewBinding()
    private var mAdapter: ItemContentCardAdapter? = null

    init {
        if (pool != null) {
            binding?.lstCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: ThumbnailWidgetUiModel?) {
        if (element == null) return

        binding?.txtHeader?.text = element.header.name
        mAdapter?.submitList(element.data)
    }

    private fun setupRecyclerView() {
        mAdapter = ItemContentCardAdapter(ItemTwoSquareType.Thumbnail, listener)
        binding?.lstCard?.layoutManager = GridLayoutManager(itemView.context, TWO_SQUARE_LIMIT)
        binding?.lstCard?.adapter = mAdapter
        binding?.lstCard?.setHasFixedSize(true)
    }

    companion object {
        val LAYOUT = R.layout.global_component_2square_thumbnail_widget
    }
}
