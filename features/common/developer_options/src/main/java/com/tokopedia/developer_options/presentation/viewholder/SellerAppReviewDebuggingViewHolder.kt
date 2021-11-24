package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SellerAppReviewDebuggingUiModel
import com.tokopedia.developer_options.utils.SellerInAppReview.Companion.getSellerAppReviewDebuggingEnabled
import com.tokopedia.developer_options.utils.SellerInAppReview.Companion.setSellerAppReviewDebuggingEnabled
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class SellerAppReviewDebuggingViewHolder(
    itemView: View
): AbstractViewHolder<SellerAppReviewDebuggingUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_app_sellerapp_review_debugging
    }

    override fun bind(element: SellerAppReviewDebuggingUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.seller_app_review_debugging_cb)
        itemView.context.applicationContext.apply {
            cb.isChecked = getSellerAppReviewDebuggingEnabled(this)
            cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
                setSellerAppReviewDebuggingEnabled(this, state)
            }
        }
    }
}