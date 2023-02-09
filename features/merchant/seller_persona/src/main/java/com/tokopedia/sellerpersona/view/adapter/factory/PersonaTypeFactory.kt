package com.tokopedia.sellerpersona.view.adapter.factory

import com.tokopedia.sellerpersona.view.model.PersonaTypeLoadingUiModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel

/**
 * Created by @ilhamsuaib on 09/02/23.
 */

interface PersonaTypeFactory {

    fun type(model: PersonaUiModel): Int

    fun type(model: PersonaTypeLoadingUiModel): Int
}