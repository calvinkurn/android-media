package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChannelItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipsItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowsePayloads
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipsBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 25/09/23
 */
internal class FeedBrowseChipsViewHolder private constructor(
    binding: ItemFeedBrowseChipsBinding,
    listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    private var mSlotId: String = ""

    private val adapter = FeedBrowseChipAdapter(
        object : FeedBrowseChipViewHolder.Listener {
            override fun onChipImpressed(model: FeedBrowseChipUiModel, position: Int) {

            }

            override fun onChipClicked(model: FeedBrowseChipUiModel) {

            }

            override fun onChipSelected(model: FeedBrowseChipUiModel, position: Int) {

            }

            override fun onChipClicked(model: WidgetMenuModel) {
                listener.onChipClicked(this@FeedBrowseChipsViewHolder, mSlotId, model)
            }

            override fun onChipSelected(model: WidgetMenuModel, position: Int) {
                listener.onChipSelected(this@FeedBrowseChipsViewHolder, mSlotId, model)
            }
        }
    )

    init {
        binding.root.adapter = adapter
        binding.root.addItemDecoration(FeedBrowseChipsItemDecoration(binding.root.resources))
    }

    fun bind(item: FeedBrowseItemListModel.Chips) {
        mSlotId = item.slotId
        adapter.submitList(item.chips)
    }

    fun bindPayloads(item: FeedBrowseItemListModel.Chips, payloads: FeedBrowsePayloads) {
        bind(item)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
        ): FeedBrowseChipsViewHolder {
            return FeedBrowseChipsViewHolder(
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
            viewHolder: FeedBrowseChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        )

        fun onChipSelected(
            viewHolder: FeedBrowseChipsViewHolder,
            slotId: String,
            chip: WidgetMenuModel
        )
    }
}
