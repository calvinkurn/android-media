package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderViewAllRevampBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionRevampModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OtherTransactionRevampViewHolder(itemView: View, val mainNavListener: MainNavListener) : AbstractViewHolder<OtherTransactionRevampModel>(itemView) {
    private var binding: HolderViewAllRevampBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_view_all_revamp
    }

    override fun bind(otherTransactionRevampModel: OtherTransactionRevampModel) {
        binding?.cardViewAll?.description = itemView.resources.getString(R.string.transaction_view_all_desc)
        binding?.cardViewAll?.setCta(itemView.resources.getString(R.string.transaction_view_all))

        binding?.cardViewAll?.cardView?.animateOnPress = CardUnify2.ANIMATE_OVERLAY

        binding?.cardViewAll?.cardView?.setOnClickListener {
            mainNavListener.onViewAllTransactionClicked()
        }
    }
}
