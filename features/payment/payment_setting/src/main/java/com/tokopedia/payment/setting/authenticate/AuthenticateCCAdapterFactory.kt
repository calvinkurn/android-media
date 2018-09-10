package com.tokopedia.payment.setting.authenticate

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.payment.setting.authenticate.model.TypeAuthenticateCreditCard

class AuthenticateCCAdapterFactory(val onAdapterInteractionListener: BaseListAdapter.OnAdapterInteractionListener<TypeAuthenticateCreditCard>) : BaseAdapterTypeFactory() {
    fun type(typeAuthenticateCreditCard: TypeAuthenticateCreditCard) : Int {
        return AuthenticateCCViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if(type == AuthenticateCCViewHolder.LAYOUT){
            return AuthenticateCCViewHolder(onAdapterInteractionListener, parent)
        }
        return super.createViewHolder(parent, type)
    }

}
