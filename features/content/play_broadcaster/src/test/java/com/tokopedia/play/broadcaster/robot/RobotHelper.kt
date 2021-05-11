package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.ViewModel

/**
 * Created by jegul on 11/05/21
 */
//typealias RobotWithValue<R, T> = Pair<R, T>
data class RobotWithValue<R: Robot, T>(val first: R, val second: T)

infix fun <R: Robot, T> R.andWhen(
        fn: R.() -> T
): RobotWithValue<R, T> {
    return RobotWithValue(this, run(fn))
}

infix fun <R: Robot> R.andThen(
        fn: R.() -> Unit
): R {
    return apply(fn)
}

infix fun <R: Robot, T> RobotWithValue<R, T>.andThen(
        fn: R.(T) -> Unit
): R {
    return first.apply { fn(second) }
}

infix fun <R: Robot> R.thenVerify(
        fn: R.() -> Unit
): R {
    apply { fn() }
    return this
}

infix fun <R: Robot, T> RobotWithValue<R, T>.thenVerify(
        fn: R.(T) -> Unit
): R {
    apply { first.fn(second) }
    return first
}