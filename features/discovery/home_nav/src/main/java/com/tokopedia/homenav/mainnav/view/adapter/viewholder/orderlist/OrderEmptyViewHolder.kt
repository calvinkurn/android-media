package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionEmptyBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OrderEmptyViewHolder(itemView: View, val mainNavListener: MainNavListener) :
    AbstractViewHolder<OrderEmptyModel>(itemView) {
    private var binding: HolderTransactionEmptyBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_empty
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_transaction.png"
    }

    override fun bind(element: OrderEmptyModel) {
        binding?.cardEmptyTransaction?.cardType = CardUnify2.TYPE_BORDER
        binding?.orderEmptyImage?.setImageUrl(EMPTY_IMAGE_LINK)
    }
}