package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionBuyAgainItemBinding
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainView
import com.tokopedia.utils.view.binding.viewBinding

class ProductBuyAgainAdapter constructor(
    private val data: List<BuyAgainModel> = emptyList(),
    private val listener: BuyAgainView.Listener? = null
) : RecyclerView.Adapter<ProductBuyAgainAdapter.BuyAgainItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainItemViewHolder {
        return BuyAgainItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: BuyAgainItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    class BuyAgainItemViewHolder constructor(
        view: View,
        private val listener: BuyAgainView.Listener?
    ) : ViewHolder(view) {

        private val binding: HolderTransactionBuyAgainItemBinding? by viewBinding()

        fun bind(data: BuyAgainModel) {
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
