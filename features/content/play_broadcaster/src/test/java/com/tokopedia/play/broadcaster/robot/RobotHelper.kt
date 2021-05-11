package com.tokopedia.play.broadcaster.robot

/**
 * Created by jegul on 11/05/21
 */
//typealias RobotWithValue<R, T> = Pair<R, T>
data class RobotWithValue<R: Robot, T>(val robot: R, val value: T)
sealed class RobotMaybe<R: Robot> {

    abstract val robot: R

    data class Value<R: Robot, T>(override val robot: R, val value: T) : RobotMaybe<R>()
    data class Throwable<R: Robot>(override val robot: R, val throwable: kotlin.Throwable) : RobotMaybe<R>()
}

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
    return robot.apply { fn(value) }
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
    apply { robot.fn(value) }
    return robot
}

infix fun <R: Robot, T> R.andMaybeWhen(
        fn: R.() -> T
): RobotMaybe<R> {
    return try {
        RobotMaybe.Value(this, run(fn))
    } catch (e: Throwable) {
        RobotMaybe.Throwable(this, e)
    }
}

infix fun <R: Robot> RobotMaybe<R>.thenExpectThrowable(
        fn: R.(Throwable) -> Unit
): R {
    robot.fn(when (this) {
        is RobotMaybe.Value<*, *> -> error("Expect throwable, but has value")
        is RobotMaybe.Throwable -> throwable
    })
    return robot
}

infix fun <R: Robot, T> RobotMaybe<R>.thenVerify(
        fn: R.(T) -> Unit
): R {
    robot.fn(when (this) {
        is RobotMaybe.Value<*, *> -> value as T
        is RobotMaybe.Throwable -> error("Expect value, but has exception")
    })
    return robot
}