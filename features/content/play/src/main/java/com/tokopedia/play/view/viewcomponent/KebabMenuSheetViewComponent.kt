package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.R
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 08/12/21
 */
class KebabMenuSheetViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_kebab_menu_sheet) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)
    private val clContent: ConstraintLayout = findViewById(R.id.cl_user_kebab_menu_sheet_content)
    private val tvReportVideo: TextView = findViewById(R.id.tv_report)

    init {
        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@KebabMenuSheetViewComponent)
            }

        tvReportVideo.setOnClickListener {
            listener.onReportClick(this@KebabMenuSheetViewComponent)
        }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.tv_sheet_title).hide()

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clContent.setPadding(clContent.paddingLeft, clContent.paddingTop, clContent.paddingRight, insets.systemWindowInsetBottom)

            insets
        }

    }

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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    interface Listener {
        fun onReportClick(view: KebabMenuSheetViewComponent)
        fun onCloseButtonClicked(view: KebabMenuSheetViewComponent)
    }
}