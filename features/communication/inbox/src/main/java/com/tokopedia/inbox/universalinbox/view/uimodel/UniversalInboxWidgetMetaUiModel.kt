package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxWidgetMetaUiModel(
    val widgetList: ArrayList<UniversalInboxWidgetUiModel> = arrayListOf(),
    var isError: Boolean = false
) {
    companion object {
        fun areContentsTheSame(
            oldItem: UniversalInboxWidgetMetaUiModel,
            newItem: UniversalInboxWidgetMetaUiModel
        ): Boolean {
            return (
                areListTheSame(oldItem.widgetList, newItem.widgetList) &&
                    oldItem.isError == newItem.isError
                )
        }

        private fun areListTheSame(
            oldList: List<UniversalInboxWidgetUiModel>,
            newList: List<UniversalInboxWidgetUiModel>
        ): Boolean {
            return (oldList.size == newList.size) &&
                oldList
                    .zip(newList) // [(O1, N1), (O2, N2), (O3, N3)]
                    .all { (oldItem, newItem) -> // All of item from zip should be the same
                        oldItem.icon == newItem.icon &&
                            oldItem.title == newItem.title &&
                            oldItem.subtext == newItem.subtext &&
                            oldItem.applink == newItem.applink &&
                            oldItem.counter == newItem.counter &&
                            oldItem.type == newItem.type &&
                            oldItem.isError == newItem.isError
                    }
        }
    }
}
