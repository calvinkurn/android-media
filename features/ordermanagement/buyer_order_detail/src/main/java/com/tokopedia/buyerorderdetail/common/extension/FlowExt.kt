package com.tokopedia.buyerorderdetail.common.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map

/**
 * Combines multiple flows with debounce so that the [transform] lambda is only called once in case
 * multiple flows is updated in almost the same time.
 *
 * Example case:
 *
 * ```
 * val someMutableStateFlow = MutableStateFlow<SomeData>(SomeData())
 * val flow = someMutableStateFlow.mapLatest { transform(it) }
 * val flow2 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow3 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow4 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow5 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow6 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow7 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow8 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow9 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow10 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow11 = someMutableStateFlow.mapLatest { transform(it) }
 * combine(
 *     flow,
 *     flow2,
 *     flow3,
 *     flow4,
 *     flow5,
 *     flow6,
 *     flow7,
 *     flow8,
 *     flow9,
 *     flow10,
 *     flow11,
 *     ::transform
 * )
 * ```
 * In the example above, the change on someMutableStateFlow will trigger changes on all the flows
 * used in the [kotlinx.coroutines.flow.combine] function which will cause the [kotlinx.coroutines.flow.combine] [transform]
 * lambda to be called multiple times each time the flows emit the newly transformed someMutableStateFlow value
 * ```
 * val someMutableStateFlow = MutableStateFlow<SomeData>(SomeData())
 * val flow = someMutableStateFlow.mapLatest { transform(it) }
 * val flow2 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow3 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow4 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow5 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow6 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow7 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow8 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow9 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow10 = someMutableStateFlow.mapLatest { transform(it) }
 * val flow11 = someMutableStateFlow.mapLatest { transform(it) }
 * combineThrottling(
 *     flow,
 *     flow2,
 *     flow3,
 *     flow4,
 *     flow5,
 *     flow6,
 *     flow7,
 *     flow8,
 *     flow9,
 *     flow10,
 *     flow11,
 *     ::transform
 * )
 * ```
 *
 * In the example above, the change on someMutableStateFlow will trigger changes on all the flows
 * used in the [combineThrottling] function which will cause the [combineThrottling] [transform]
 * lambda to be called only once as long as the time gap between each map process doesn't took more
 * than 100ms.
 *
 */
@Suppress("UNCHECKED_CAST", "MagicNumber")
fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> combineThrottling(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> R
): Flow<R> = kotlinx.coroutines.flow.combine(
    flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10, flow11, flow12
) { args: Array<*> -> args }.debounce(100).map { args ->
    transform(
        args[0] as T1, args[1] as T2, args[2] as T3, args[3] as T4, args[4] as T5, args[5] as T6,
        args[6] as T7, args[7] as T8, args[8] as T9, args[9] as T10, args[10] as T11, args[11] as T12
    )
}
