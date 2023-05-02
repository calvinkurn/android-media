package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class RichListFactoryImpl : BaseAdapterTypeFactory(), RichListFactory {

    override fun type(model: BaseRichListItemUiModel): Int {

    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            else -> super.createViewHolder(parent, type)
        }
    }
}