package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

data class PersonaUiModel(
    val name: String = String.EMPTY,
    val headerTitle: String = String.EMPTY,
    val headerSubTitle: String = String.EMPTY,
    val avatarImage: String = String.EMPTY,
    val backgroundImage: String = String.EMPTY,
    val bodyTitle: String = String.EMPTY,
    val itemList: List<String> = listOf(),
    var isSelected: Boolean = false
)