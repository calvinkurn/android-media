package com.tokopedia.broadcaster.revamp.state

/**
 * Created by meyta.taliti on 19/03/22.
 */
sealed class BroadcastInitState {
    object Initialized: BroadcastInitState()
    object Uninitialized: BroadcastInitState()
    data class Error(val cause: Throwable): BroadcastInitState()
}

