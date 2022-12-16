package com.tokopedia.mvc.presentation.intro.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvc.presentation.intro.adapter.viewholder.IntroCouponViewHolder
import com.tokopedia.mvc.presentation.intro.uimodel.IntroCouponUiModel

class MvcIntroAdapterFactoryImpl: BaseAdapterTypeFactory(), MvcIntroAdapterFactory {
    override fun type(uimodel: IntroCouponUiModel): Int {
       return IntroCouponViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            IntroCouponViewHolder.RES_LAYOUT -> IntroCouponViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
