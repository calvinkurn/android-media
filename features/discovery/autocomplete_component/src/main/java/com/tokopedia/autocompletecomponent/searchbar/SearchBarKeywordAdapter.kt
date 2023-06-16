package com.tokopedia.autocompletecomponent.searchbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteKeywordChipBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

class SearchBarKeywordAdapter(
    private val listener: SearchBarKeywordListener,
) : ListAdapter<SearchBarKeyword, SearchBarKeywordAdapter.KeywordViewHolder>(
    DEFAULT_DIFF_UTIL_CALLBACK
) {

    private val keywordListener = object : KeywordViewHolder.KeywordListener {
        override fun onKeywordRemoved(position: Int) {
            if (position != RecyclerView.NO_POSITION) listener.onKeywordRemoved(getItem(position))
        }

        override fun onKeywordSelected(position: Int) {
            if (position != RecyclerView.NO_POSITION) listener.onKeywordSelected(getItem(position))
        }

        override fun showCoachMark(view: View) {
            listener.showAddedKeywordCoachMark(view)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeywordViewHolder {
        val view = LayoutAutocompleteKeywordChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return KeywordViewHolder(view.root, keywordListener)
    }

    override fun onBindViewHolder(
        holder: KeywordViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }


    companion object {
        private val DEFAULT_DIFF_UTIL_CALLBACK =
            object : DiffUtil.ItemCallback<SearchBarKeyword>() {
                override fun areItemsTheSame(
                    oldItem: SearchBarKeyword,
                    newItem: SearchBarKeyword
                ): Boolean {
                    return oldItem.position == newItem.position
                }

                override fun areContentsTheSame(
                    oldItem: SearchBarKeyword,
                    newItem: SearchBarKeyword
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    class KeywordViewHolder(
        view: View,
        private val listener: KeywordListener,
    ) : RecyclerView.ViewHolder(view) {

        private val binding: LayoutAutocompleteKeywordChipBinding? by viewBinding()

        init {
            binding?.root?.apply {
                setOnClickListener {
                    listener.onKeywordSelected(bindingAdapterPosition)
                }
                setOnRemoveListener {
                    listener.onKeywordRemoved(bindingAdapterPosition)
                }
            }
        }

        private fun String.ellipsize(
            maxLength: Int = KEYWORD_LENGTH_LIMIT
        ): String {
            return if (length <= maxLength) {
                this
            } else {
                substring(0, maxLength) + "…"
            }
        }

        fun bind(data: SearchBarKeyword) {
            val view = binding?.root ?: return
            view.chipText = data.keyword.ellipsize()
            view.chipType = if (data.isSelected) {
                ChipsUnify.TYPE_DISABLE
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            view.chip_right_icon.setImageResource(R.drawable.unify_chips_ic_close)
            if (data.shouldShowCoachMark) {
                listener.showCoachMark(view)
            }
        }

        interface KeywordListener {
            fun onKeywordRemoved(position: Int)
            fun onKeywordSelected(position: Int)
            fun showCoachMark(view: View)
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.layout_autocomplete_keyword_chip

            private const val KEYWORD_LENGTH_LIMIT = 30
        }
    }
}
