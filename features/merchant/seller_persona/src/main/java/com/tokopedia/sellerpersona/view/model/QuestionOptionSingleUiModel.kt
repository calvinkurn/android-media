package com.tokopedia.sellerpersona.view.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.view.adapter.OptionAdapterFactory

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

data class QuestionOptionSingleUiModel(
    val value: String = String.EMPTY,
    val title: String = String.EMPTY,
    var isSelected: Boolean = false
) : BaseOptionUiModel {

    override fun type(typeFactory: OptionAdapterFactory): Int {
        return typeFactory.type(this)
    }
}