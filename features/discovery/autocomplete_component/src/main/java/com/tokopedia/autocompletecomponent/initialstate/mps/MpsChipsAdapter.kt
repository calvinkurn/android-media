package com.tokopedia.autocompletecomponent.initialstate.mps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteMpsChipBinding
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteMpsChipItemBinding
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsChipsAdapter.MpsChipViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.binding.viewBinding

class MpsChipsAdapter(
    listener: MpsChipCallback,
    itemCallback: DiffUtil.ItemCallback<MpsChipDataView> = DEFAULT_ITEM_CALLBACK
) : ListAdapter<MpsChipDataView, MpsChipViewHolder>(itemCallback),
    MpsChipCallback by listener {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MpsChipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_autocomplete_mps_chip, parent, false)
        return MpsChipViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: MpsChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DEFAULT_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<MpsChipDataView>() {
                override fun areItemsTheSame(
                    oldItem: MpsChipDataView,
                    newItem: MpsChipDataView
                ): Boolean {
                    return oldItem.applink == newItem.applink
                }

                override fun areContentsTheSame(
                    oldItem: MpsChipDataView,
                    newItem: MpsChipDataView
                ): Boolean {
                    return oldItem.title == newItem.title
                }
            }
    }

    class MpsChipViewHolder(
        view: View,
        private val listener: MpsChipCallback
    ) : ViewHolder(view) {
        private var binding: LayoutAutocompleteMpsChipBinding? by viewBinding()
        private var itemBinding: LayoutAutocompleteMpsChipItemBinding =
            LayoutAutocompleteMpsChipItemBinding.inflate(
                LayoutInflater.from(itemView.context),
                null,
                false
            )

        init {
            binding?.mpsChip?.apply {
                setOnClickListener {
                    listener.onItemClicked(bindingAdapterPosition)
                }
                addCustomView(itemBinding.root)
            }
        }

        fun bind(element: MpsChipDataView) {
            bindText(element, itemBinding)
            itemBinding.icAdd.isEnabled = !element.disableAddButton
        }

        private fun bindText(
            item: MpsChipDataView,
            binding: LayoutAutocompleteMpsChipItemBinding
        ) {
            binding.tgTitle.text = item.title
        }
    }
}
