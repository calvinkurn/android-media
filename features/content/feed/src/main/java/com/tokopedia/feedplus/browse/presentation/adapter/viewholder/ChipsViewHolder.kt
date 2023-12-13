package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.FeedBrowseChipsItemDecoration
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipsBinding

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class ChipsViewHolder private constructor(
    private val binding: ItemFeedBrowseChipsBinding,
    pool: RecycledViewPool,
    listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private var mItem: FeedBrowseItemListModel.Chips? = null

    private val adapter = FeedBrowseChipAdapter(
        object : ChipViewHolder.Listener {
            override fun onChipImpressed(viewHolder: ChipViewHolder, model: WidgetMenuModel) {
                val data = mItem as? FeedBrowseItemListModel.Chips.Item ?: return
                listener.onChipImpressed(
                    this@ChipsViewHolder,
                    data,
                    model,
                    viewHolder.absoluteAdapterPosition
                )
            }

            override fun onChipClicked(viewHolder: ChipViewHolder, model: WidgetMenuModel) {
                val data = mItem as? FeedBrowseItemListModel.Chips.Item ?: return
                listener.onChipClicked(
                    this@ChipsViewHolder,
                    data,
                    model,
                    viewHolder.absoluteAdapterPosition
                )
            }

            override fun onChipSelected(viewHolder: ChipViewHolder, model: WidgetMenuModel) {
                val data = mItem as? FeedBrowseItemListModel.Chips.Item ?: return
                listener.onChipSelected(this@ChipsViewHolder, data, model)
            }
        }
    )

    init {
        binding.root.adapter = adapter
        binding.root.setRecycledViewPool(pool)
        binding.root.setHasFixedSize(true)
        binding.root.addItemDecoration(FeedBrowseChipsItemDecoration(binding.root.resources))
    }

    fun bind(item: FeedBrowseItemListModel.Chips) {
        mItem = item

        when (item) {
            is FeedBrowseItemListModel.Chips.Item -> bindItem(item.chips)
            FeedBrowseItemListModel.Chips.Placeholder -> bindPlaceholder()
        }
    }

    fun bindPayloads(item: FeedBrowseItemListModel.Chips, payloads: FeedBrowsePayloads) {
        bind(item)
    }

    private fun bindItem(chips: List<ChipsModel>) {
        adapter.submitList(chips) {
            binding.root.invalidateItemDecorations()
        }
    }

    private fun bindPlaceholder() {
        adapter.setLoading()
    }

    companion object {
        fun create(
            parent: ViewGroup,
            pool: RecycledViewPool,
            listener: Listener
        ): ChipsViewHolder {
            return ChipsViewHolder(
                ItemFeedBrowseChipsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                pool,
                listener
            )
        }
    }

    interface Listener {

        fun onChipImpressed(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        )
        fun onChipClicked(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        )

        fun onChipSelected(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel
        )

        companion object {
            val Default get() = object : Listener {
                override fun onChipImpressed(
                    viewHolder: ChipsViewHolder,
                    widgetModel: FeedBrowseItemListModel.Chips.Item,
                    chip: WidgetMenuModel,
                    chipPosition: Int
                ) {}

                override fun onChipClicked(
                    viewHolder: ChipsViewHolder,
                    widgetModel: FeedBrowseItemListModel.Chips.Item,
                    chip: WidgetMenuModel,
                    chipPosition: Int
                ) {}

                override fun onChipSelected(
                    viewHolder: ChipsViewHolder,
                    widgetModel: FeedBrowseItemListModel.Chips.Item,
                    chip: WidgetMenuModel
                ) {}
            }
        }
    }
}
