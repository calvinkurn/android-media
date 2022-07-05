package com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel

/**
 * Created by dhaba
 */
class BalanceWidgetTypeFactoryImpl : BaseAdapterTypeFactory(), BalanceWidgetTypeFactory {
    override fun type(dataModel: HomeHeaderDataModel): Int {
        return 1
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}