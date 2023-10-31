package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipsItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.LoadingModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipsBinding

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class ChipsViewHolder private constructor(
    binding: ItemFeedBrowseChipsBinding,
    listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    private var mSlotId: String = ""

    private val adapter = FeedBrowseChipAdapter(
        object : ChipViewHolder.Listener {
            override fun onChipImpressed(model: FeedBrowseChipUiModel, position: Int) {

            }

            override fun onChipClicked(model: FeedBrowseChipUiModel) {

            }

            override fun onChipSelected(model: FeedBrowseChipUiModel, position: Int) {

            }

            override fun onChipClicked(model: WidgetMenuModel) {
                listener.onChipClicked(this@ChipsViewHolder, mSlotId, model)
            }

            override fun onChipSelected(model: WidgetMenuModel, position: Int) {
                listener.onChipSelected(this@ChipsViewHolder, mSlotId, model)
            }
        }
    )

    init {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(FeedBrowseChipsItemDecoration(binding.root.resources))
    }

    fun bind(item: FeedBrowseItemListModel.Chips) {
        mSlotId = item.slotId

        when (item) {
            is FeedBrowseItemListModel.Chips.Item -> bindItem(item.chips)
            FeedBrowseItemListModel.Chips.Placeholder -> bindPlaceholder()
        }
    }

    fun bindPayloads(item: FeedBrowseItemListModel.Chips, payloads: FeedBrowsePayloads) {
        bind(item)
    }

    private fun bindItem(chips: List<ChipsModel>) {
        adapter.submitList(chips)
    }

    private fun bindPlaceholder() {
        adapter.submitList(List(6) { LoadingModel })
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
        ): ChipsViewHolder {
            return ChipsViewHolder(
                ItemFeedBrowseChipsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onChipClicked(
            viewHolder: ChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        )

        fun onChipSelected(
            viewHolder: ChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        )
    }
}
