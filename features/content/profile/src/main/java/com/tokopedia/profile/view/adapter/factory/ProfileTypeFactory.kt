package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel

/**
 * @author by milhamj on 9/20/18.
 */
interface ProfileTypeFactory {
    fun type(viewModel: ProfileHeaderViewModel): Int

    fun type(viewModel: ProfileEmptyViewModel): Int

    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun createViewHolder(parent: View, type: Int) : AbstractViewHolder<Visitable<*>>
}