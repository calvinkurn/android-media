package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.profile.view.adapter.viewholder.ProfileEmptyViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.EmptyAffiliateViewModel
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel

/**
 * @author by yfsx on 14/03/19.
 */
class ProfileEmptyTypeFactoryImpl(private val viewListener: ProfileEmptyContract.View)
    : BaseAdapterTypeFactory(), ProfileTypeFactory {

    override fun type(viewModel: ProfileHeaderViewModel): Int {
        return ProfileHeaderViewHolder.LAYOUT
    }

    override fun type(viewModel: ProfileEmptyViewModel): Int {
        return 0
    }

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return 0
    }

    override fun type(emptyAffiliateViewModel: EmptyAffiliateViewModel): Int {
        return 0
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            ProfileHeaderViewHolder.LAYOUT ->
                ProfileHeaderViewHolder(parent, viewListener) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)
        }
    }
}