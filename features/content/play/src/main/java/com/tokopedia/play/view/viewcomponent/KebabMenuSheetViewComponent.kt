package com.tokopedia.play.view.viewcomponent

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.R
import com.tokopedia.play.view.adapter.PlayMoreActionAdapter
import com.tokopedia.play.view.type.PlayMoreActionType
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
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
    private val rvActionList: RecyclerView = findViewById(R.id.rv_action_list)
    private val moreActionAdapter = PlayMoreActionAdapter()

    private val reportAction = PlayMoreActionUiModel(
        type = PlayMoreActionType.Report,
        iconRes = 0,
        isIconAvailable = false,
        subtitleRes = R.string.play_kebab_report_title,
        onClick = { listener.onReportClick(this@KebabMenuSheetViewComponent) }
    )

    init {
        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@KebabMenuSheetViewComponent)
            }

        findViewById<ImageView>(com.tokopedia.play_common.R.id.tv_sheet_title).hide()

        rvActionList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moreActionAdapter
        }

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

        /** Need to improve if there's watch mode - need to put it under observer*/
        setActionList()
    }

    private fun setActionList(){
        val actionList = mutableListOf<PlayMoreActionUiModel>().apply {
            add(reportAction)
        }
        if (actionList.isNotEmpty()) {
            moreActionAdapter.setItemsAndAnimateChanges(actionList)
        }
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