package com.tokopedia.kolcomponent.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kolcomponent.view.viewmodel.DynamicPostViewModel

/**
 * @author by milhamj on 03/12/18.
 */
class DynamicPostTypeFactoryImpl : BaseAdapterTypeFactory(), DynamicPostTypeFactory {
    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return 0
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}