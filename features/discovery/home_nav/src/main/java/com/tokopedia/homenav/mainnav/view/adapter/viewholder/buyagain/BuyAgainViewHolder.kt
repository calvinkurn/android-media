package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionBuyAgainBinding
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.utils.view.binding.viewBinding

class BuyAgainViewHolder(view: View) : AbstractViewHolder<BuyAgainUiModel>(view) {

    private val binding: HolderTransactionBuyAgainBinding? by viewBinding()

    override fun bind(element: BuyAgainUiModel?) {
        if (element == null) return

        val adapter = ProductBuyAgainAdapter(element.data)

        binding?.lstProduct?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding?.lstProduct?.adapter = adapter
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_buy_again
    }
}
