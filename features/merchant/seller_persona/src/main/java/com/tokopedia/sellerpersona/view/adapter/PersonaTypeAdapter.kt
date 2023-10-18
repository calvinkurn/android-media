package com.tokopedia.sellerpersona.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerpersona.view.adapter.factory.PersonaTypeFactory
import com.tokopedia.sellerpersona.view.adapter.factory.PersonaTypeFactoryImpl
import com.tokopedia.sellerpersona.view.adapter.viewholder.PersonaTypeViewHolder
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 27/01/23.
 */

class PersonaTypeAdapter(
    listener: PersonaTypeViewHolder.Listener
) : BaseListAdapter<Visitable<PersonaTypeFactory>, PersonaTypeFactoryImpl>(
    PersonaTypeFactoryImpl(listener)
) {

    fun getItems(): List<PersonaUiModel> {
        return data.filterIsInstance<PersonaUiModel>()
    }
}