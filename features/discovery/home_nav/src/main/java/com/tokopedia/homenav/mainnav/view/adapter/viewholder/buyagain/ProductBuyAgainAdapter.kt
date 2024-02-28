package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.homenav.databinding.HolderTransactionBuyAgainItemBinding
import com.tokopedia.homenav.R
import com.tokopedia.utils.view.binding.viewBinding

class ProductBuyAgainAdapter constructor(
    private val data: List<Pair<String, String>> = emptyList(),
    private val listener: BuyAgainView.Listener? = null
) : RecyclerView.Adapter<ProductBuyAgainAdapter.BuyAgainItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainItemViewHolder {
        return BuyAgainItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: BuyAgainItemViewHolder, position: Int) {
        val (name, price) = data[position]
        holder.bind(name, price)
    }

    override fun getItemCount() = data.size

    class BuyAgainItemViewHolder constructor(
        view: View,
        private val listener: BuyAgainView.Listener?
    ) : ViewHolder(view) {

        private val binding: HolderTransactionBuyAgainItemBinding? by viewBinding()

        fun bind(bannerUrl: String, price: String) {
            val data: BuyAgainView.Data

            if (bindingAdapterPosition == 0) {
                data = BuyAgainView.Data(
                    productId = "",
                    bannerUrl = bannerUrl,
                    price = price,
                    slashPrice = "Rp.123..",
                    discount = "40%"
                )
            } else {
                data = BuyAgainView.Data(
                    productId = "",
                    bannerUrl = bannerUrl,
                    price = price,
                    slashPrice = "",
                    discount = ""
                )
            }

            binding?.productCard?.bind(data)

            listener?.let {
                binding?.productCard?.setListener(it)
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.holder_transaction_buy_again_item

            fun create(parent: ViewGroup, listener: BuyAgainView.Listener?): BuyAgainItemViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return BuyAgainItemViewHolder(view, listener)
            }
        }
    }
}
