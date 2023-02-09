package com.tokopedia.sellerpersona.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerpersona.view.adapter.factory.PersonaTypeFactory

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

object PersonaTypeLoadingUiModel : Visitable<PersonaTypeFactory> {

    override fun type(typeFactory: PersonaTypeFactory): Int {
        return typeFactory.type(this)
    }
}