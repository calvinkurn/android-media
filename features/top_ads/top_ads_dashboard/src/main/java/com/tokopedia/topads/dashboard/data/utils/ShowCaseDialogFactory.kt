package com.tokopedia.topads.dashboard.data.utils

import android.content.Context
import com.tokopedia.showcase.ShowCaseBuilder
import com.tokopedia.topads.dashboard.R

/**
 * Created by Hendry on 4/13/2017.
 */

object ShowCaseDialogFactory {

    @JvmStatic
    fun createTkpdShowCase(context: Context) = ShowCaseBuilder()
                .titleTextColorRes(com.tokopedia.topads.auto.R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(com.tokopedia.design.R.color.grey_400)
                .shadowColorRes(com.tokopedia.showcase.R.color.shadow)
                .backgroundContentColorRes(com.tokopedia.design.R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.topads_selector_circle_green)
                .prevStringRes(R.string.label_back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(true)
                .skipStringRes(R.string.title_skip)
                .build()
}
