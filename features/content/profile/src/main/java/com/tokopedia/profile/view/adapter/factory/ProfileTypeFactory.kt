package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.profile.view.viewmodel.*

/**
 * @author by milhamj on 9/20/18.
 */
interface ProfileTypeFactory {
    fun type(viewModel: ProfileHeaderViewModel): Int

    fun type(viewModel: ProfileEmptyViewModel): Int

    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun type(noPostCardViewModel: NoPostCardViewModel): Int

    fun type(otherRelatedProfileViewModel: OtherRelatedProfileViewModel): Int

    fun type(titleViewModel: TitleViewModel): Int

    fun type(emptyAffiliateViewModel: EmptyAffiliateViewModel): Int

    fun createViewHolder(parent: View, type: Int) : AbstractViewHolder<Visitable<*>>

}