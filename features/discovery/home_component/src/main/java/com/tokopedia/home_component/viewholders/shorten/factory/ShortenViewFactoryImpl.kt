package com.tokopedia.home_component.viewholders.shorten.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.visitable.shorten.DealsUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

class ShortenViewFactoryImpl : BaseAdapterTypeFactory(), ShortenViewFactory {

    override fun type(model: DealsUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(model: MissionWidgetUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}
