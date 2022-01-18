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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.userreport.adapter.UserReportReasoningAdapter
import com.tokopedia.play.ui.userreport.itemdecoration.ReasoningListItemDecoration
import com.tokopedia.play.ui.userreport.viewholder.UserReportReasoningViewHolder
import com.tokopedia.play.view.type.PlayUserReportSectionType
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportSection
import com.tokopedia.play.view.uimodel.PlayUserReportUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 07/12/21
 */
class PlayUserReportSheetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container,  R.id.cl_user_report_sheet) {

    private val clContent: ConstraintLayout = findViewById(R.id.cl_user_report_content)
    private val globalError: GlobalError = findViewById(R.id.global_error_user_report)
    private val vBottomOverlay: View = findViewById(R.id.v_bottom_overlay)
    private val rvCategory: RecyclerView = findViewById(R.id.rv_category)

    private val tvHeader = PlayUserReportSection(
        type = PlayUserReportSectionType.Header,
        title = R.string.play_user_report_header,
        isUrl = false
    )

    private val tvFooter = PlayUserReportSection(
        type = PlayUserReportSectionType.Footer,
        title = R.string.play_user_report_footer,
        isUrl = true,
        onClick = { listener.onFooterClicked(this@PlayUserReportSheetViewComponent) }
    )

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

    private val categoryScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
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
            addItemDecoration(ReasoningListItemDecoration(rvCategory.context))
            addOnScrollListener(categoryScrollListener)
        }

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->

            vBottomOverlay.layoutParams = vBottomOverlay.layoutParams.apply {
                height = insets.systemWindowInsetBottom
            }
            clContent.setPadding(clContent.paddingLeft, clContent.paddingTop, clContent.paddingRight, insets.systemWindowInsetBottom)

            insets
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
        if (list.reasoningList.isNotEmpty()){
            val actionList = mutableListOf<PlayUserReportReasoningUiModel>().apply {
                add(tvHeader)
                list.reasoningList.map { add(it) }
                add(tvFooter)
            }
            categoryAdapter.setItemsAndAnimateChanges(actionList)
        }
    }

    fun showError(isConnectionError: Boolean, onError: () -> Unit) {
        clContent.hide()
        globalError.show()

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
            if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    fun showPlaceholder(){
        clContent.show()
        categoryAdapter.setItemsAndAnimateChanges(getPlaceholderModel().reasoningList)
    }

    private fun getPlaceholderModel() = PlayUserReportUiModel.Loaded(
        reasoningList = List(5){PlayUserReportReasoningUiModel.Placeholder}
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rvCategory.removeOnScrollListener(categoryScrollListener)
    }

    interface Listener{
        fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent)
        fun onItemReportClick(view: PlayUserReportSheetViewComponent, item: PlayUserReportReasoningUiModel.Reasoning)
        fun onFooterClicked(view: PlayUserReportSheetViewComponent)
    }
}