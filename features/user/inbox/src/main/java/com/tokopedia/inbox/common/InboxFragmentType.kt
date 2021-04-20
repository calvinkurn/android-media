package com.tokopedia.inbox.common

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@IntDef(value = [
    InboxFragmentType.NONE,
    InboxFragmentType.NOTIFICATION,
    InboxFragmentType.CHAT,
    InboxFragmentType.DISCUSSION
])
annotation class InboxFragmentType {
    companion object {
        const val NONE: Int = -1
        const val NOTIFICATION: Int = 0
        const val CHAT: Int = 1
        const val DISCUSSION: Int = 2
    }
}