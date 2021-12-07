package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 07/12/21
 */
class PlayUserReportSheetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container,  R.id.cl_user_report_sheet) {


    interface Listener{
        fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent)
        fun onItemReportClick(view: PlayUserReportSheetViewComponent)
    }
}