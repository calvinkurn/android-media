package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 07/12/21
 */
class PlayUserReportSheetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container,  R.id.cl_user_report_sheet) {

    init {
        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@PlayUserReportSheetViewComponent)
            }
    }

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }
        show()
    }

    interface Listener{
        fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent)
        fun onItemReportClick(view: PlayUserReportSheetViewComponent)
    }
}