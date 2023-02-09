package com.tokopedia.sellerpersona.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerpersona.view.adapter.viewholder.PersonaTypeViewHolder
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

class PersonaTypeFactoryImpl(
    private val listener: PersonaTypeViewHolder.Listener
) : BaseAdapterTypeFactory(), PersonaTypeFactory {

    override fun type(model: PersonaUiModel): Int {
        return PersonaTypeViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PersonaTypeViewHolder.RES_LAYOUT -> PersonaTypeViewHolder(listener, parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}