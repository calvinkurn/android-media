package com.tokopedia.play.view.type

/**
 * @author by astidhiyaa on 18/02/22
 */
enum class PlayUpcomingBellStatus {
    On,
    Off,
    Unknown
}

fun PlayUpcomingBellStatus.reversed(): PlayUpcomingBellStatus =
    if (this == PlayUpcomingBellStatus.Off) PlayUpcomingBellStatus.On
    else PlayUpcomingBellStatus.Off