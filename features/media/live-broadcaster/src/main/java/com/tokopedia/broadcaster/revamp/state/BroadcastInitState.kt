package com.tokopedia.broadcaster.revamp.state

import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException

/**
 * Created by meyta.taliti on 19/03/22.
 */
sealed class BroadcastInitState {

    abstract val tag: String

    object Initialized: BroadcastInitState() {
        override val tag: String
            get() = INITIALIZED
    }

    object Uninitialized: BroadcastInitState() {
        override val tag: String
            get() = UNINITIALIZED
    }

    data class Error(val cause: BroadcasterException): BroadcastInitState() {
        override val tag: String
            get() = "$ERROR, cause: ${cause.message}"
    }

    companion object {
        private const val INITIALIZED = "Initialized"
        private const val UNINITIALIZED = "Uninitialized"
        private const val ERROR = "ERROR"
    }
}

