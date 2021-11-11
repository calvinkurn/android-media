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
            return if (selectedOption.selected) oldData else oldData.copy()
                .apply { updateItems(this) }
        }

        private fun updateItems(newData: OrderExtensionRequestInfoUiModel) {
            newData.items = newData.items.map {
                if (it is OrderExtensionRequestInfoUiModel.OptionUiModel) {
                    if (isPreviouslySelectedOption(it)) {
                        it.copy(selected = false)
                    } else if (isCurrentlySelectedOption(it)) {
                        it.copy(selected = true)
                    } else it
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
            return item.code != selectedOption.code && item.selected
        }

        private fun isCurrentlySelectedOption(
            item: OrderExtensionRequestInfoUiModel.OptionUiModel
        ): Boolean {
            return item.code == selectedOption.code
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

    class OnFailedSendingOrderExtensionRequest : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false)
        }
    }

    class OnSuccessSendingOrderExtensionRequest: OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, completed = true)
        }
    }

    class OnRequestDismissBottomSheet: OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, completed =  true)
        }
    }
}
