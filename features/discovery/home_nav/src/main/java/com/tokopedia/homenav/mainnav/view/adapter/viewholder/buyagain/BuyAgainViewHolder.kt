package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionBuyAgainBinding
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainView
import com.tokopedia.utils.view.binding.viewBinding

class BuyAgainViewHolder constructor(
    view: View,
    private val listener: BuyAgainView.Listener
) : AbstractViewHolder<BuyAgainUiModel>(view) {

    private val binding: HolderTransactionBuyAgainBinding? by viewBinding()
    private var mAdapter: ProductBuyAgainAdapter? = null

    override fun bind(element: BuyAgainUiModel?) {
        if (element == null) return
        setupAdapter(element)

        binding?.lstProduct?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding?.lstProduct?.addItemDecoration(
            NavOrderSpacingDecoration(
                SPACING_BETWEEN.toDpInt(),
                EDGE_MARGIN.toDpInt()
            )
        )
    }

    private fun setupAdapter(element: BuyAgainUiModel) {
        mAdapter = if (mAdapter == null) {
            ProductBuyAgainAdapter(element.data, listener)
        } else {
            binding?.lstProduct?.adapter as? ProductBuyAgainAdapter
        }

        binding?.lstProduct?.adapter = mAdapter
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_buy_again

        private const val EDGE_MARGIN = 16f
        private const val SPACING_BETWEEN = 8f
    }
}
