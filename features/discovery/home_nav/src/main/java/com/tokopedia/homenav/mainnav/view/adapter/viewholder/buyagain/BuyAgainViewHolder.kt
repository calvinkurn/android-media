package com.tokopedia.homenav.mainnav.view.adapter.viewholder.buyagain

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionBuyAgainBinding
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class BuyAgainViewHolder constructor(
    view: View,
    private val listener: Listener
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

        binding?.lstProduct?.addOnImpressionListener(element) {
            listener.onBuyAgainWidgetImpression(element.data, bindingAdapterPosition)
        }
    }

    private fun setupAdapter(element: BuyAgainUiModel) {
        val listener = object : BuyAgainView.Listener {
            override fun onProductCardClicked(model: BuyAgainModel) {
                listener.onProductCardClicked(model, bindingAdapterPosition)
            }

            override fun onAtcButtonClicked(model: BuyAgainModel) {
                listener.onAtcButtonClicked(model, bindingAdapterPosition)
            }
        }

        mAdapter = if (mAdapter == null) {
            ProductBuyAgainAdapter(element.data, listener)
        } else {
            binding?.lstProduct?.adapter as? ProductBuyAgainAdapter
        }

        binding?.lstProduct?.adapter = mAdapter
    }

    interface Listener {
        fun onProductCardClicked(model: BuyAgainModel, position: Int)
        fun onAtcButtonClicked(model: BuyAgainModel, position: Int)
        fun onBuyAgainWidgetImpression(models: List<BuyAgainModel>, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_buy_again

        private const val EDGE_MARGIN = 16f
        private const val SPACING_BETWEEN = 8f
    }
}
