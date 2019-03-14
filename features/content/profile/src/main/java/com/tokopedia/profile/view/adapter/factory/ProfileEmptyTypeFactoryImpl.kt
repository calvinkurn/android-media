package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileEmptyViewHolder
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel

/**
 * @author by yfsx on 14/03/19.
 */
class ProfileEmptyTypeFactoryImpl(private val viewListener : ProfileEmptyContract.View
) : BaseAdapterTypeFactory(), ProfileEmptyTypeFactory {
    override fun type(viewModel: ProfileEmptyViewModel): Int {
        return ProfileEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProfileEmptyViewHolder.LAYOUT ->
                ProfileEmptyViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)
        }
    }
}