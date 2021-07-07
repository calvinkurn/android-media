package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.smartbills.R
import kotlinx.android.synthetic.main.view_smart_bills_empty_state.view.*

class SmartBillsEmptyStateViewHolder(itemView: View) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_empty_state
        
        private const val NEW_SUBHOMEPAGE_RECHARGE = "tokopedia://recharge/home?platform_id=31"
    }

    override fun bind(element: EmptyModel) {
        with (itemView) {
            smart_bills_empty_state_button.setOnClickListener {
                RouteManager.route(context, NEW_SUBHOMEPAGE_RECHARGE)
            }
        }
    }
}