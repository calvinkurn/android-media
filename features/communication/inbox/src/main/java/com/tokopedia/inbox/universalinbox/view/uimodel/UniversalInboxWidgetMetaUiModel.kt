package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxWidgetMetaUiModel(
    val widgetList: ArrayList<UniversalInboxWidgetUiModel> = arrayListOf(),
    val widgetError: UniversalInboxWidgetMetaErrorUiModel = UniversalInboxWidgetMetaErrorUiModel()
) : Visitable<UniversalInboxTypeFactory> {

    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun areContentsTheSame(
            oldItem: UniversalInboxWidgetMetaUiModel,
            newItem: UniversalInboxWidgetMetaUiModel
        ): Boolean {
            return (
                areListTheSame(oldItem.widgetList, newItem.widgetList) &&
                    oldItem.widgetError == newItem.widgetError &&
                    areErrorStateTheSame(oldItem.widgetError, newItem.widgetError)
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

        private fun areErrorStateTheSame(
            oldItem: UniversalInboxWidgetMetaErrorUiModel,
            newItem: UniversalInboxWidgetMetaErrorUiModel
        ): Boolean {
            return oldItem.isError == newItem.isError &&
                oldItem.isLocalLoadLoading == newItem.isLocalLoadLoading
        }
    }
}

data class UniversalInboxWidgetMetaErrorUiModel(
    var isError: Boolean = false,
    var isLocalLoadLoading: Boolean = false
)
