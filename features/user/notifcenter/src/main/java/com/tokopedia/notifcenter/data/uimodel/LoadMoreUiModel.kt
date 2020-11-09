package com.tokopedia.notifcenter.data.uimodel

import androidx.annotation.IntDef
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory

class LoadMoreUiModel constructor(
        @LoadMoreType val type: Int
) : Visitable<NotificationTypeFactory> {

    var loading = false

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @IntDef(value = [
        LoadMoreType.NEW,
        LoadMoreType.EARLIER
    ])
    annotation class LoadMoreType {
        companion object {
            const val NEW: Int = 1
            const val EARLIER: Int = 2
        }
    }
}