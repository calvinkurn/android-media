package com.tokopedia.expresscheckout.view.profile.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileActionListener
import com.tokopedia.expresscheckout.view.profile.viewholder.ProfileViewHolder
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfileAdapterTypeFactory(val listener: CheckoutProfileActionListener) : BaseAdapterTypeFactory(), CheckoutProfileTypeFactory {

    override fun type(viewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            ProfileViewHolder.LAYOUT -> ProfileViewHolder(view, listener)
            else -> super.createViewHolder(view, viewType)
        }
    }
}