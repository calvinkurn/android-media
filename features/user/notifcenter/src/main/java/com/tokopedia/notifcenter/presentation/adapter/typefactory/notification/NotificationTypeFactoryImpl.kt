package com.tokopedia.notifcenter.presentation.adapter.typefactory.notification

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class NotificationTypeFactoryImpl : BaseAdapterTypeFactory(), NotificationTypeFactory {
    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}