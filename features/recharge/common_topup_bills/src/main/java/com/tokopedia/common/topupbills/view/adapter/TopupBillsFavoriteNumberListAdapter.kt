package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class TopupBillsFavoriteNumberListAdapter (
        val listener: OnFavoriteNumberClickListener,
        var clientNumbers: List<TopupBillsSeamlessFavNumberItem>
): RecyclerView.Adapter<TopupBillsFavoriteNumberListAdapter.FavoriteNumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupBillsFavoriteNumberListAdapter.FavoriteNumberViewHolder {
        val binding = ItemTopupBillsFavoritNumberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        return FavoriteNumberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopupBillsFavoriteNumberListAdapter.FavoriteNumberViewHolder, position: Int) {
        holder.bind(clientNumbers[position])
    }

    override fun getItemCount(): Int = clientNumbers.size

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
                    listener.onFavoriteNumberClick(favClientNumber)
                }
                commonTopupbillsFavoriteNumberMenu.setOnClickListener {
                    listener.onFavoriteNumberMenuClick(favClientNumber)
                }
            }
        }
    }
}