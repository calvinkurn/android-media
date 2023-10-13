package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.presentation.adapter.FeedBrowseChipAdapter
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedCategoryInspirationModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipsBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowseInspirationCardBinding

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class FeedCategoryInspirationViewHolder private constructor() {

    class Chips private constructor(
        binding: ItemFeedBrowseChipsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = FeedBrowseChipAdapter(
            object : FeedBrowseChipViewHolder.Listener {
                override fun onChipImpressed(model: FeedBrowseChipUiModel, position: Int) {

                }

                override fun onChipClicked(model: FeedBrowseChipUiModel) {

                }

                override fun onChipSelected(model: FeedBrowseChipUiModel, position: Int) {

                }
            }
        )

        init {
            binding.root.adapter = adapter
        }

        fun bind(item: FeedCategoryInspirationModel.Chips) {
            adapter.submitList(item.chipList)
        }

        companion object {
            fun create(parent: ViewGroup): Chips {
                return Chips(
                    ItemFeedBrowseChipsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }

    class Card private constructor(
        private val binding: ItemFeedBrowseInspirationCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedCategoryInspirationModel.Card) {
            binding.tvTitle.text = item.title
            binding.tvPartnerName.text = item.partnerName
        }

        companion object {
            fun create(parent: ViewGroup): Card {
                return Card(
                    ItemFeedBrowseInspirationCardBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false,
                    )
                )
            }
        }
    }
}
