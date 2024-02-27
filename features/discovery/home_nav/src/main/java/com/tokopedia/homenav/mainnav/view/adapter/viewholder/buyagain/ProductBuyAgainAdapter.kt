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
) : RecyclerView.Adapter<ProductBuyAgainAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val (name, price) = data[position]
        holder.bind(name, price)
    }

    override fun getItemCount() = data.size

    class Holder constructor(
        view: View,
        private val listener: BuyAgainView.Listener?
    ) : ViewHolder(view) {

        private val binding: HolderTransactionBuyAgainItemBinding? by viewBinding()

        fun bind(bannerUrl: String, price: String) {
            binding?.productCard?.bind(BuyAgainView.Data(
                productId = "",
                bannerUrl = bannerUrl,
                price = price,
                slashPrice = "",
                discount = ""
            ))

            listener?.let {
                binding?.productCard?.setListener(it)
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.holder_transaction_buy_again_item

            fun create(parent: ViewGroup, listener: BuyAgainView.Listener?): Holder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return Holder(view, listener)
            }
        }
    }
}
