package com.tokopedia.sellerpersona.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.view.adapter.OptionAdapterFactory

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

sealed class BaseOptionUiModel(
    open val value: String,
    open val title: String,
    open var isSelected: Boolean
) : Visitable<OptionAdapterFactory> {

    data class QuestionOptionSingleUiModel(
        override val value: String = String.EMPTY,
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false
    ) : BaseOptionUiModel(value, title, isSelected) {

        override fun type(typeFactory: OptionAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    class QuestionOptionMultipleUiModel(
        override val value: String = String.EMPTY,
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false
    ) : BaseOptionUiModel(value, title, isSelected) {

        override fun type(typeFactory: OptionAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}