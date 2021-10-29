package com.tokopedia.sellerorder.orderextension.presentation.model

interface OrderExtensionRequestInfoUpdater {
    fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel

    class OnCommentChange(
        private val changedComment: OrderExtensionRequestInfoUiModel.CommentUiModel
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy().apply { updateItems(this) }
        }

        private fun updateItems(newData: OrderExtensionRequestInfoUiModel) {
            newData.items = newData.items.map {
                if (isChangedComment(it)) {
                    changedComment.copy().apply {
                        validateComment()
                    }
                } else it
            }
        }

        private fun isChangedComment(
            item: OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem
        ): Boolean {
            return item is OrderExtensionRequestInfoUiModel.CommentUiModel
                    && item.optionCode == changedComment.optionCode
        }
    }

    class OnSelectedOptionChange(
        private val selectedOption: OrderExtensionRequestInfoUiModel.OptionUiModel
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy().apply { updateItems(this) }
        }

        private fun updateItems(newData: OrderExtensionRequestInfoUiModel) {
            newData.items = newData.items.map {
                if (it is OrderExtensionRequestInfoUiModel.OptionUiModel && isPreviouslySelectedOption(it)) {
                    it.copy(selected = false)
                } else if (it is OrderExtensionRequestInfoUiModel.CommentUiModel) {
                    if (it.isForSelectedOption()) {
                        it.updateToShow()
                    } else {
                        it.updateToHide()
                    }
                    it
                } else it
            }
        }

        private fun isPreviouslySelectedOption(
            item: OrderExtensionRequestInfoUiModel.OptionUiModel
        ): Boolean {
            return item != selectedOption && item.selected
        }

        private fun OrderExtensionRequestInfoUiModel.CommentUiModel.isForSelectedOption(): Boolean {
            return optionCode == selectedOption.code
        }
    }

    class OnStartSendingOrderExtensionRequest(
        private val action: (OrderExtensionRequestInfoUiModel) -> Unit
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = true).also {
                action(it)
            }
        }
    }

    class OnFailedSendingOrderExtensionRequest(private val shouldDismiss: Boolean) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, completed = shouldDismiss)
        }
    }

    class OnSuccessSendingOrderExtensionRequest: OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, completed = true)
        }
    }
}
