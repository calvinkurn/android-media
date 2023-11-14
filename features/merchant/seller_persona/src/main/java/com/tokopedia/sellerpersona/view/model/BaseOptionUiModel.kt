package com.tokopedia.sellerpersona.view.model

import androidx.compose.runtime.Immutable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.view.adapter.factory.OptionAdapterFactory

/**
 * Created by @ilhamsuaib on 02/02/23.
 */

@Immutable
sealed class BaseOptionUiModel(
    open val value: String,
    open val title: String,
    open var isSelected: Boolean
) : Visitable<OptionAdapterFactory> {

    abstract fun copyData(isSelected: Boolean): BaseOptionUiModel

    @Immutable
    data class QuestionOptionSingleUiModel(
        override val value: String = String.EMPTY,
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false
    ) : BaseOptionUiModel(value, title, isSelected) {

        companion object {
            private const val SINGLE_OPTION_TEXT_FORMAT = "%s. %s"
        }

        override fun type(typeFactory: OptionAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun copyData(isSelected: Boolean): BaseOptionUiModel = this.copy(isSelected = isSelected)

        fun getFormattedText(): String {
            return String.format(SINGLE_OPTION_TEXT_FORMAT, value.uppercase(), title)
        }
    }

    @Immutable
    data class QuestionOptionMultipleUiModel(
        override val value: String = String.EMPTY,
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false
    ) : BaseOptionUiModel(value, title, isSelected) {

        override fun type(typeFactory: OptionAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun copyData(isSelected: Boolean): BaseOptionUiModel = this.copy(isSelected = isSelected)
    }
}