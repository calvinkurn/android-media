package com.tokopedia.feedcomponent.view.viewmodel.statistic

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by jegul on 2019-11-25
 */
sealed class PostStatisticUiModel

data class PostStatisticDetailUiModel(
        @DrawableRes val iconRes: Int,
        val amountString: String,
        @StringRes val subtitleRes: Int,
        val actionTitle: String? = null
) : PostStatisticUiModel()

object PostStatisticPlaceholderUiModel : PostStatisticUiModel()