package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class TopupBillsFavoriteNumberListAdapter (
        val favoriteNumberListener: OnFavoriteNumberClickListener,
        val emptyStateListener: FavoriteNumberEmptyStateListener,
        var clientNumbers: List<TopupBillsSeamlessFavNumberItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FAVORITE_NUMBER) {
            val binding = ItemTopupBillsFavoritNumberBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
            FavoriteNumberViewHolder(binding)
        } else {
            val binding = ItemTopupBillsFavoriteNumberEmptyStateBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false)
            FavoriteNumberEmptyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            VIEW_TYPE_FAVORITE_NUMBER -> {
                val data = clientNumbers[position] as TopupBillsSeamlessFavNumberItem
                (holder as FavoriteNumberViewHolder).bind(data)
            }
            VIEW_TYPE_EMPTY_STATE -> {
                (holder as FavoriteNumberEmptyViewHolder).bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (clientNumbers.isEmpty()) {
            VIEW_TYPE_EMPTY_STATE
        } else {
            VIEW_TYPE_FAVORITE_NUMBER
        }
    }

    override fun getItemCount(): Int {
        return if (clientNumbers.isEmpty()) 1 else clientNumbers.size
    }

    fun setNumbers(clientNumbers: List<TopupBillsSeamlessFavNumberItem>) {
        this.clientNumbers = clientNumbers
        notifyDataSetChanged()
    }

    inner class FavoriteNumberViewHolder(
            private val binding: ItemTopupBillsFavoritNumberBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(favClientNumber: TopupBillsSeamlessFavNumberItem) {
            binding.run {

                if (favClientNumber.clientName.isNotEmpty()) {
                    commonTopupbillsFavoriteNumberClientName.show()
                    commonTopupbillsFavoriteNumberClientNumber.show()
                    commonTopupbillsFavoriteNumberClientName.text = favClientNumber.clientName
                    commonTopupbillsFavoriteNumberClientNumber.text = favClientNumber.clientNumber
                } else {
                    commonTopupbillsFavoriteNumberClientName.show()
                    commonTopupbillsFavoriteNumberClientNumber.hide()
                    commonTopupbillsFavoriteNumberClientName.text = favClientNumber.clientNumber
                }

                if (favClientNumber.iconUrl.isNotEmpty()) {
                    commonTopupbillsFavoriteNumberIcon.setImageUrl(favClientNumber.iconUrl)
                }
                commonTopupbillsFavoriteNumberContainer.setOnClickListener {
                    favoriteNumberListener.onFavoriteNumberClick(favClientNumber)
                }
            }
        }
    }

    inner class FavoriteNumberEmptyViewHolder(
            private val binding: ItemTopupBillsFavoriteNumberEmptyStateBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.run {
                commonTopupbillsEmptyStateButton.setOnClickListener {
                    emptyStateListener.onContinueClicked()
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_FAVORITE_NUMBER = 1
        private const val VIEW_TYPE_EMPTY_STATE = 2
    }
}