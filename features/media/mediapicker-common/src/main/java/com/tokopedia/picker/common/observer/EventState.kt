package com.tokopedia.picker.common.observer

interface EventState {
    object Idle : EventState {
        override val key: String
            get() = ""
    }

    val key: String

}
