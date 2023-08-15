package com.tokopedia.payment.setting.authenticate.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard
import com.tokopedia.payment.setting.databinding.ItemAuthenticateViewHolderBinding

class AuthenticateCCViewHolder(
    private val onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<TypeAuthenticateCreditCard>,
    view: View
) : AbstractViewHolder<TypeAuthenticateCreditCard>(view) {

    private val binding = ItemAuthenticateViewHolderBinding.bind(itemView)

    override fun bind(element: TypeAuthenticateCreditCard?) {
        with(binding) {
            radioSelectedAuth.isChecked = element?.isSelected ?: false
            descriptionAuthType.text = element?.description
            titleAuthenticate.text = element?.title
            radioSelectedAuth.setOnClickListener { onAdapterInteractionListener.onItemClicked(element) }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_authenticate_view_holder
    }
}
