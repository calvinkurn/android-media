package com.tokopedia.play.broadcaster.ui.model

import android.view.View
import androidx.annotation.StringRes
import com.tokopedia.play.broadcaster.R

/**
 * Created by Jonathan Darwin on 20 March 2024
 */
sealed interface LiveMenuCoachMarkType {

    val view: View
    val titleRes: Int
    val descriptionRes: Int

    data class Game(
        override val view: View,
        @StringRes override val titleRes: Int = 0,
        @StringRes override val descriptionRes: Int = R.string.play_interactive_broadcast_onboarding_subtitle,
    ) : LiveMenuCoachMarkType

    data class Statistic(
        override val view: View,
        @StringRes override val titleRes: Int = 0,
        @StringRes override val descriptionRes: Int = R.string.play_broadcaster_statistic_icon_coachmark,
    ) : LiveMenuCoachMarkType
}
