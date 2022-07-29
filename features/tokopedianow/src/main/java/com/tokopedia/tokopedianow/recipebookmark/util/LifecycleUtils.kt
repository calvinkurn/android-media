package com.tokopedia.tokopedianow.recipebookmark.util

/**
 * `this` [utils] is temporary
 * if [lifecycle.runtime] version has been increased to 2.4.0 then
 * remove `this` [utils] and use [repeatOnLifecycle] from that library.
 */

/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Runs the given [block] in a new coroutine when `this` [Lifecycle] is at least at [state] and
 * suspends the execution until `this` [Lifecycle] is [Lifecycle.State.DESTROYED].
 *
 * The [block] will cancel and re-launch as the lifecycle moves in and out of the target state.
 *
 * ```
 * class MyActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         /* ... */
 *         // Runs the block of code in a coroutine when the lifecycle is at least STARTED.
 *         // The coroutine will be cancelled when the ON_STOP event happens and will
 *         // restart executing if the lifecycle receives the ON_START event again.
 *         lifecycleScope.launch {
 *             lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
 *                 uiStateFlow.collect { uiState ->
 *                 updateUi(uiState)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * The best practice is to call this function when the lifecycle is initialized. For
 * example, `onCreate` in an Activity, or `onViewCreated` in a Fragment. Otherwise, multiple
 * repeating coroutines doing the same could be created and be executed at the same time.
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a
 * parameter will throw an [IllegalArgumentException].
 *
 * @param state [Lifecycle.State] in which `block` runs in a new coroutine. That coroutine
 * will cancel if the lifecycle falls below that state, and will restart if it's in that state
 * again.
 * @param block The block to run when the lifecycle is at least in [state] state.
 */
suspend fun Lifecycle.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) {
    require(state !== Lifecycle.State.INITIALIZED) {
        "repeatOnLifecycle cannot start work with the INITIALIZED lifecycle state."
    }

    if (currentState === Lifecycle.State.DESTROYED) {
        return
    }

    coroutineScope {
        withContext(Dispatchers.Main.immediate) {
            // Check the current state of the lifecycle as the previous check is not guaranteed
            // to be done on the main thread.
            if (currentState === Lifecycle.State.DESTROYED) return@withContext

            // Instance of the running repeating coroutine
            var launchedJob: Job? = null

            // Registered observer
            var observer: LifecycleEventObserver? = null

            try {
                // Suspend the coroutine until the lifecycle is destroyed or
                // the coroutine is cancelled
                suspendCancellableCoroutine<Unit> { cont ->
                    // Lifecycle observers that executes `block` when the lifecycle reaches certain state, and
                    // cancels when it moves falls below that state.
                    val startWorkEvent = upTo(state)
                    val cancelWorkEvent = downFrom(state)
                    observer = LifecycleEventObserver { _, event ->
                        if (event == startWorkEvent) {
                            // Launch the repeating work preserving the calling context
                            launchedJob = this@coroutineScope.launch(block = block)
                            return@LifecycleEventObserver
                        }
                        if (event == cancelWorkEvent) {
                            launchedJob?.cancel()
                            launchedJob = null
                        }
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            cont.resume(Unit)
                        }
                    }
                    this@repeatOnLifecycle.addObserver(observer as LifecycleEventObserver)
                }
            } finally {
                launchedJob?.cancel()
                observer?.let {
                    this@repeatOnLifecycle.removeObserver(it)
                }
            }
        }
    }
}

/**
 * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
 * leaving the specified [Lifecycle.State] to a lower state, or `null`
 * if there is no valid event that can move down from the given state.
 *
 * @param state the higher state that the returned event will transition down from
 * @return the event moving down the lifecycle phases from state
 */
@Nullable
fun downFrom(@NonNull state: Lifecycle.State?): Lifecycle.Event? {
    return when (state) {
        Lifecycle.State.CREATED -> Lifecycle.Event.ON_DESTROY
        Lifecycle.State.STARTED -> Lifecycle.Event.ON_STOP
        Lifecycle.State.RESUMED -> Lifecycle.Event.ON_PAUSE
        else -> null
    }
}

/**
 * Returns the [Lifecycle.Event] that will be reported by a [Lifecycle]
 * entering the specified [Lifecycle.State] from a lower state, or `null`
 * if there is no valid event that can move up to the given state.
 *
 * @param state the higher state that the returned event will transition up to
 * @return the event moving up the lifecycle phases to state
 */
fun upTo(state: Lifecycle.State): Lifecycle.Event? {
    return when (state) {
        Lifecycle.State.CREATED -> Lifecycle.Event.ON_CREATE
        Lifecycle.State.STARTED -> Lifecycle.Event.ON_START
        Lifecycle.State.RESUMED -> Lifecycle.Event.ON_RESUME
        else -> null
    }
}

/**
 * [LifecycleOwner]'s extension function for [Lifecycle.repeatOnLifecycle] to allow an easier
 * call to the API from LifecycleOwners such as Activities and Fragments.
 *
 * ```
 * class MyActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         /* ... */
 *         // Runs the block of code in a coroutine when the lifecycle is at least STARTED.
 *         // The coroutine will be cancelled when the ON_STOP event happens and will
 *         // restart executing if the lifecycle receives the ON_START event again.
 *         lifecycleScope.launch {
 *             repeatOnLifecycle(Lifecycle.State.STARTED) {
 *                 uiStateFlow.collect { uiState ->
 *                 updateUi(uiState)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @see Lifecycle.repeatOnLifecycle
 */
suspend fun LifecycleOwner.repeatOnLifecycle(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
): Unit = lifecycle.repeatOnLifecycle(state, block)