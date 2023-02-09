package com.tokopedia.sellerpersona.data.remote.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerpersona.data.remote.model.PersonaListModel
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

class PersonaListMapper @Inject constructor() {

    fun mapToUiModel(personaList: List<PersonaListModel>): List<PersonaUiModel> {
        return personaList.mapIndexed { i, it ->
            PersonaUiModel(
                name = it.value,
                headerTitle = it.header.title,
                headerSubTitle = it.header.subtitle,
                avatarImage = it.header.image,
                backgroundImage = it.header.backgroundImage,
                bodyTitle = it.body.title,
                itemList = it.body.itemList,
                isSelected = i == Int.ZERO
            )
        }
    }
}