package com.tokopedia.expresscheckout.view.profile.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

interface CheckoutProfileTypeFactory {

    fun type(viewModel: ProfileViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

}