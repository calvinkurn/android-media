package com.tokopedia.sellerpersona.view.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.EMPTY
import kotlinx.parcelize.Parcelize

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

@Parcelize
data class PersonaUiModel(
    val name: String = String.EMPTY,
    val headerTitle: String = String.EMPTY,
    val headerSubTitle: String = String.EMPTY,
    val avatarImage: String = String.EMPTY,
    val backgroundImage: String = String.EMPTY,
    val bodyTitle: String = String.EMPTY,
    val itemList: List<String> = listOf(),
    var isSelected: Boolean = false
) : Parcelable