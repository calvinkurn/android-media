package com.tokopedia.sellerorder.orderextension.presentation.model

interface OrderExtensionRequestInfoUpdater {
    fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel

    class OnCommentChange(
        private val changedComment: OrderExtensionRequestInfoUiModel.CommentUiModel
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, message = "", success = true).apply { updateItems(this) }
        }

        private fun updateItems(newData: OrderExtensionRequestInfoUiModel) {
            newData.items = newData.items.map {
                if (it.isChangedComment()) {
                    changedComment.copy().apply {
                        validateComment()
                    }
                } else it
            }
        }

        private fun OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem.isChangedComment(): Boolean {
            return this is OrderExtensionRequestInfoUiModel.CommentUiModel
                    && this.optionCode == changedComment.optionCode
        }
    }

    class OnSelectedOptionChange(
        private val selectedOption: OrderExtensionRequestInfoUiModel.OptionUiModel
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return if (selectedOption.selected) oldData else oldData.copy(
                processing = false,
                message = "",
                success = true
            ).apply { updateItems(this) }
        }

        private fun updateItems(newData: OrderExtensionRequestInfoUiModel) {
            newData.items = newData.items.map {
                if (it is OrderExtensionRequestInfoUiModel.OptionUiModel) {
                    if (it.isPreviouslySelectedOption()) {
                        it.copy(selected = false)
                    } else if (it.isCurrentlySelectedOption()) {
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

        private fun OrderExtensionRequestInfoUiModel.OptionUiModel.isPreviouslySelectedOption(): Boolean {
            return code != selectedOption.code && selected
        }

        private fun OrderExtensionRequestInfoUiModel.OptionUiModel.isCurrentlySelectedOption(): Boolean {
            return code == selectedOption.code
        }

        private fun OrderExtensionRequestInfoUiModel.CommentUiModel.isForSelectedOption(): Boolean {
            return optionCode == selectedOption.code
        }
    }

    class OnSuccessGetOrderExtensionRequest(
        private val newData: OrderExtensionRequestInfoUiModel
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return newData
        }
    }

    class OnFailedGetOrderExtensionRequest(
        private val errorMessage: String
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, message = errorMessage, success = false, completed = true, refreshOnDismiss = false)
        }
    }

    class OnStartSendingOrderExtensionRequest(
        private val action: (OrderExtensionRequestInfoUiModel) -> Unit
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return if (oldData.processing || oldData.completed) {
                oldData
            } else {
                oldData.copy(processing = true, message = "", success = true).also {
                    action(it)
                }
            }
        }
    }

    class OnFailedSendingOrderExtensionRequest(
        private val errorMessage: String
    ) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, message = errorMessage, success = false)
        }
    }

    class OnSuccessSendingOrderExtensionRequest(private val message: String) : OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, message = message, success = true, completed = true, refreshOnDismiss = true)
        }
    }

    class OnRequestDismissBottomSheet: OrderExtensionRequestInfoUpdater {
        override fun execute(oldData: OrderExtensionRequestInfoUiModel): OrderExtensionRequestInfoUiModel {
            return oldData.copy(processing = false, message = "", success = true, completed = true, refreshOnDismiss = false)
        }
    }
}
