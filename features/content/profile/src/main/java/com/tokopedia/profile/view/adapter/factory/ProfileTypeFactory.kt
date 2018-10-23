package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel
import com.tokopedia.profile.view.viewmodel.ProfilePostViewModel

/**
 * @author by milhamj on 9/20/18.
 */
interface ProfileTypeFactory {
    fun type(viewModel: ProfileHeaderViewModel): Int

    fun type(viewModel: ProfilePostViewModel): Int

    fun createViewHolder(parent: View, type: Int) : AbstractViewHolder<Visitable<*>>
}