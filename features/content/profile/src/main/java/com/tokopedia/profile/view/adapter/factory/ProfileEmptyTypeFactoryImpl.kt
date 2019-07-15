package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.profile.view.adapter.viewholder.ProfileEmptyViewHolder
import com.tokopedia.profile.view.adapter.viewholder.ProfileHeaderViewHolder
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.*

/**
 * @author by yfsx on 14/03/19.
 */
class ProfileEmptyTypeFactoryImpl(private val viewListener: ProfileEmptyContract.View)
    : BaseAdapterTypeFactory(), ProfileTypeFactory {

    override fun type(viewModel: ProfileHeaderViewModel): Int {
        return ProfileHeaderViewHolder.LAYOUT
    }

    override fun type(viewModel: ProfileEmptyViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun type(emptyAffiliateViewModel: EmptyAffiliateViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun type(noPostCardViewModel: NoPostCardViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun type(otherRelatedProfileViewModel: OtherRelatedProfileViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    override fun type(emptyAffiliateViewModel: TitleViewModel): Int {
        return HideViewHolder.LAYOUT
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