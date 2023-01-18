package com.tokopedia.sellerorder.orderextension.presentation.model

import androidx.annotation.ColorRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory.OrderExtensionRequestInfoAdapterTypeFactory
import java.util.Date

data class OrderExtensionRequestInfoUiModel(
    var items: List<BaseOrderExtensionRequestInfoItem> = emptyList(),
    val orderExtentionDate: OrderExtentionDate = OrderExtentionDate(),
    var processing: Boolean,
    var success: Boolean,
    var completed: Boolean,
    var refreshOnDismiss: Boolean,
    var message: String,
    var throwable: Throwable?
) {
    private fun getCommentUiModelForOption(optionCode: Int): CommentUiModel? {
        return items.find {
            it is CommentUiModel && it.optionCode == optionCode
        } as? CommentUiModel
    }

    private fun getSelectedOptionUiModel(): OptionUiModel? {
        return items.find {
            it is OptionUiModel && it.selected
        } as? OptionUiModel
    }

    private fun hasValidCommentForSelectedReason(optionCode: Int): Boolean {
        return getCommentUiModelForOption(optionCode)?.run {
            errorCheckers.all { validator ->
                !validator.isError(value)
            }
        }.orTrue()
    }

    fun getSelectedOptionCode(): Int {
        return getSelectedOptionUiModel()?.code.orZero()
    }

    fun getComment(selectedOptionCode: Int): String {
        return getCommentUiModelForOption(selectedOptionCode)?.value.orEmpty()
    }

    fun isValid(): Boolean {
        return getSelectedOptionUiModel()?.let { selectedOption ->
            if (selectedOption.mustComment) {
                hasValidCommentForSelectedReason(selectedOption.code)
            } else true
        }.orFalse()
    }

    fun isLoadingOrderExtensionRequestInfo(): Boolean {
        return items.any {
            it is DescriptionShimmerUiModel || it is OptionShimmerUiModel
        }
    }

    data class OrderExtentionDate(
        val deadLineTime: Date = Date(),
        val eligbleDates: List<EligbleDateUIModel> = listOf()
    ){
        data class EligbleDateUIModel(
            val date: Date,
            val extensionTime: Int = 0
        )
    }

    data class DescriptionUiModel(
        val alignment: DescriptionAlignment = DescriptionAlignment.TEXT_ALIGNMENT_INHERIT,
        @ColorRes val fontColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_N700_68,
        val typographyType: DescriptionTextType = DescriptionTextType.BODY_3,
        val description: StringComposer,
        override var show: Boolean = true,
        override var hideKeyboardOnClick: Boolean = true,
        override var requestFocus: Boolean = false,
        var id: Int
    ) : BaseOrderExtensionRequestInfoItem {
        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        enum class DescriptionAlignment {
            TEXT_ALIGNMENT_INHERIT,
            TEXT_ALIGNMENT_GRAVITY,
            TEXT_ALIGNMENT_CENTER,
            TEXT_ALIGNMENT_TEXT_START,
            TEXT_ALIGNMENT_TEXT_END,
            TEXT_ALIGNMENT_VIEW_START,
            TEXT_ALIGNMENT_VIEW_END
        }

        enum class DescriptionTextType {
            HEADING_1,
            HEADING_2,
            HEADING_3,
            HEADING_4,
            HEADING_5,
            HEADING_6,
            BODY_1,
            BODY_2,
            BODY_3,
            SMALL
        }
    }

    data class OptionUiModel(
        val code: Int,
        val name: String,
        var selected: Boolean,
        val mustComment: Boolean,
        override var show: Boolean = true,
        override var hideKeyboardOnClick: Boolean = true,
        override var requestFocus: Boolean = false
    ) : BaseOrderExtensionRequestInfoItem {
        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        fun select() {
            selected = true
        }

        fun deselect() {
            selected = false
        }
    }

    data class CommentUiModel(
        var optionCode: Int,
        var value: String = "",
        var error: Boolean = false,
        var defaultMessage: StringComposer = StringComposer { "" },
        var showedMessage: StringComposer = StringComposer { "" },
        var hasFocus: Boolean = true,
        val errorCheckers: List<ErrorChecker> = emptyList(),
        override var show: Boolean,
        override var hideKeyboardOnClick: Boolean = false,
        override var requestFocus: Boolean = false
    ) : BaseOrderExtensionRequestInfoItem {
        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        fun updateToShow() {
            show = true
            requestFocus = true
            hasFocus = true
        }

        fun updateToHide() {
            show = false
            requestFocus = false
            hasFocus = false
        }

        fun validateComment() {
            var newMessage = defaultMessage
            var isError = false
            if (value.isNotEmpty() || !hasFocus) {
                errorCheckers.forEach {
                    if (!isError && it.isError(value)) {
                        newMessage = it.errorMessage
                        isError = true
                    }
                }
            }
            showedMessage = newMessage
            error = isError
        }

        data class ErrorChecker(
            val regex: String,
            val errorMessage: StringComposer
        ) {
            fun isError(value: String): Boolean {
                return Regex(regex).containsMatchIn(value)
            }
        }
    }

    class PickTimeUiModel : BaseOrderExtensionRequestInfoItem {
        var timeText:String = String.EMPTY
        override var show: Boolean = true
        override var hideKeyboardOnClick: Boolean = true
        override var requestFocus: Boolean = false

        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    class DescriptionShimmerUiModel(val width: DimenRes) : BaseOrderExtensionRequestInfoItem {
        override var show: Boolean = true
        override var hideKeyboardOnClick: Boolean = true
        override var requestFocus: Boolean = false

        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    class OptionShimmerUiModel : BaseOrderExtensionRequestInfoItem {
        override var show: Boolean = true
        override var hideKeyboardOnClick: Boolean = true
        override var requestFocus: Boolean = false

        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    class PickTimeShimmerUiModel : BaseOrderExtensionRequestInfoItem {
        override var show: Boolean = true
        override var hideKeyboardOnClick: Boolean = true
        override var requestFocus: Boolean = false

        override fun type(typeFactory: OrderExtensionRequestInfoAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    interface BaseOrderExtensionRequestInfoItem : Visitable<OrderExtensionRequestInfoAdapterTypeFactory> {
        var show: Boolean
        var hideKeyboardOnClick: Boolean
        var requestFocus: Boolean
    }
}
