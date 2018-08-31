package com.tokopedia.notifcenter.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.notifcenter.view.listener.NotifCenterContract
import com.tokopedia.notifcenter.view.adapter.viewholder.NotifItemViewHolder
import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel

/**
 * @author by milhamj on 30/08/18.
 */

class NotifCenterTypeFactoryImpl(val viewListener : NotifCenterContract.View)
    : BaseAdapterTypeFactory(), NotifCenterTypeFactory {

    override fun type(viewModel: NotifItemViewModel): Int {
        return NotifItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            NotifItemViewHolder.LAYOUT ->
                NotifItemViewHolder(parent, viewListener) as AbstractViewHolder<Visitable<*>>
            else ->
                super.createViewHolder(parent, type)
        }
    }
}