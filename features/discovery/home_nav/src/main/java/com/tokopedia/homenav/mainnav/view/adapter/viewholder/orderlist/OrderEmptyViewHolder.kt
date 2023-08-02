package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyStateRevampBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */

@MePage(MePage.Widget.TRANSACTION)
class OrderEmptyViewHolder(itemView: View, val mainNavListener: MainNavListener) :
    AbstractViewHolder<OrderEmptyModel>(itemView) {
    private var binding: HolderEmptyStateRevampBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_state_revamp
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_transaction_1.png"
    }

    override fun bind(element: OrderEmptyModel) {
        val context = itemView.context
        binding?.cardEmpty?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.emptyImage
        imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView?.setImageUrl(EMPTY_IMAGE_LINK)
        binding?.emptyText?.text = context.getText(R.string.transaction_empty)
    }
}
