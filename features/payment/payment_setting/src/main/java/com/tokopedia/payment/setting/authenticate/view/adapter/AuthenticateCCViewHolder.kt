package com.tokopedia.payment.setting.authenticate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import kotlinx.android.synthetic.main.item_authenticate_view_holder.view.*

class AuthenticateCCViewHolder(val onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<TypeAuthenticateCreditCard>, view: View?)
    : AbstractViewHolder<TypeAuthenticateCreditCard>(view) {

    override fun bind(element: TypeAuthenticateCreditCard?) {
        itemView.radioSelectedAuth.isChecked =  element?.isSelected ?: false
        itemView.descriptionAuthType.text = element?.description
        itemView.titleAuthenticate.text = element?.title
        itemView.radioSelectedAuth.setOnClickListener { onAdapterInteractionListener.onItemClicked(element)}
    }

    companion object {
        val LAYOUT = R.layout.item_authenticate_view_holder
    }
}
