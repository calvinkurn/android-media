package com.tokopedia.cmhomewidget.presentation.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactory
import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactoryImpl
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable
import javax.inject.Inject

@CMHomeWidgetScope
class CMHomeWidgetAdapter @Inject constructor(
    private val typeFactory: CMHomeWidgetViewHolderTypeFactoryImpl
) :
    BaseAdapter<CMHomeWidgetViewHolderTypeFactory>(
        typeFactory,
        listOf<CMHomeWidgetVisitable>()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return typeFactory.createViewHolder(parent, viewType)
    }

    fun loadData(dataList: List<CMHomeWidgetVisitable>) {
        setElements(dataList)
    }
}