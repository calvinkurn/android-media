package com.tokopedia.search.result.presentation.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.model.TotalSearchCountViewModel
import com.tokopedia.search.result.presentation.view.adapter.viewholder.profile.EmptySearchProfileViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.profile.ProfileViewHolder
import com.tokopedia.search.result.presentation.view.adapter.viewholder.profile.TotalSearchCountViewHolder
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.search.result.presentation.view.listener.ProfileListener

class ProfileListTypeFactoryImpl(
    private val profileListListener : ProfileListener,
    private val emptyStateListener : EmptyStateListener
) : BaseAdapterTypeFactory() , ProfileListTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ProfileViewHolder.LAYOUT -> ProfileViewHolder(parent, profileListListener)
            EmptySearchProfileViewHolder.LAYOUT -> EmptySearchProfileViewHolder(parent, emptyStateListener)
            TotalSearchCountViewHolder.LAYOUT -> TotalSearchCountViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(profileViewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun type(emptySearchProfileModel: EmptySearchProfileViewModel): Int {
        return EmptySearchProfileViewHolder.LAYOUT
    }

    override fun type(totalSearchCountViewModel: TotalSearchCountViewModel): Int {
        return TotalSearchCountViewHolder.LAYOUT
    }
}