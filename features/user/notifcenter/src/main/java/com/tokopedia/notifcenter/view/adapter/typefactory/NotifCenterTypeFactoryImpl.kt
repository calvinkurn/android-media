package com.tokopedia.notifcenter.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterTypeFactoryImpl : BaseAdapterTypeFactory(), NotifCenterTypeFactory {
    override fun type(viewModel: NotifItemViewModel): Int {
        return 0
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}