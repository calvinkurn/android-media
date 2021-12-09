package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play.ui.userreport.adapter.UserReportReasoningAdapter
import com.tokopedia.play.ui.userreport.viewholder.UserReportReasoningViewHolder
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 07/12/21
 */
class PlayUserReportSheetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container,  R.id.cl_user_report_sheet) {

    private val rvCategory: RecyclerView = findViewById(R.id.rv_category)

    private val categoryAdapter = UserReportReasoningAdapter(object : UserReportReasoningViewHolder.Listener {
        override fun onItemCategoryClicked(item: PlayUserReportReasoningUiModel.Reasoning) {
            listener.onItemReportClick(this@PlayUserReportSheetViewComponent, item)
        }
    })

    private val layoutManagerCategoryList = object : LinearLayoutManager(rvCategory.context, RecyclerView.VERTICAL, false) {
        override fun onLayoutCompleted(state: RecyclerView.State?) {
            super.onLayoutCompleted(state)
        }
    }

    init {
        findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@PlayUserReportSheetViewComponent)
            }

        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title).text = getString(R.string.play_kebab_report_title)

        rvCategory.apply {
            adapter = categoryAdapter
            layoutManager = layoutManagerCategoryList
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

    fun setReportSheet(list: PlayUserReportUiModel.Loaded){
        categoryAdapter.setItemsAndAnimateChanges(list.reasoningList)
    }

    interface Listener{
        fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent)
        fun onItemReportClick(view: PlayUserReportSheetViewComponent, item: PlayUserReportReasoningUiModel.Reasoning)
    }
}