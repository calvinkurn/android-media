package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.kotlin.extensions.view.hide

class TopupBillsFavoriteNumberListAdapter (
        val listener: OnFavoriteNumberClickListener,
        var clientNumbers: List<TopupBillsFavNumberItem>
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

    fun setNumbers(clientNumbers: List<TopupBillsFavNumberItem>) {
        this.clientNumbers = clientNumbers
    }

    inner class FavoriteNumberViewHolder(
            private val binding: ItemTopupBillsFavoritNumberBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(favClientNumber: TopupBillsFavNumberItem) {
            val name = "Misael Jonathan"
            binding.run {
                if (name.isNullOrEmpty()) {
                    commonTopupbillsFavoriteNumberClientName.hide()
                } else {
                    commonTopupbillsFavoriteNumberClientName.text = name
                }
                commonTopupbillsFavoriteNumberClientNumber.text = favClientNumber.clientNumber
                // TODO: [Misael] tambah gambar
            }

            binding.commonTopupbillsFavoriteNumberContainer.setOnClickListener {
                listener.onFavoriteNumberClick(favClientNumber)
            }
        }
    }
}