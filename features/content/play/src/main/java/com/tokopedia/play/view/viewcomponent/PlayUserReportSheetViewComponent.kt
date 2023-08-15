package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.content.common.R as commonR
import com.tokopedia.content.common.report_content.adapter.UserReportReasoningAdapter
import com.tokopedia.content.common.report_content.adapter.itemdecoration.ReasoningListItemDecoration
import com.tokopedia.content.common.report_content.adapter.viewholder.UserReportReasoningViewHolder
import com.tokopedia.content.common.report_content.model.PlayUserReportSectionType
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.PlayUserReportSection
import com.tokopedia.play.view.uimodel.PlayUserReportUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 07/12/21
 */
class PlayUserReportSheetViewComponent(
    container: ViewGroup,
    listener: Listener
) : ViewComponent(container,  commonR.id.cl_user_report_sheet) {

    private val globalError: GlobalError = findViewById(commonR.id.global_error_user_report)
    private val rvCategory: RecyclerView = findViewById(commonR.id.rv_category)

    private val tvHeader = PlayUserReportSection(
        type = PlayUserReportSectionType.Header,
        title = commonR.string.play_user_report_header,
        isUrl = false
    )

    private val tvFooter = PlayUserReportSection(
        type = PlayUserReportSectionType.Footer,
        title = commonR.string.content_user_report_footer,
        isUrl = true,
        onClick = { listener.onFooterClicked(this@PlayUserReportSheetViewComponent) }
    )

    private val categoryAdapter = UserReportReasoningAdapter(object : UserReportReasoningViewHolder.Listener {
        override fun onItemCategoryClicked(item: PlayUserReportReasoningUiModel.Reasoning) {
            listener.onItemReportClick(this@PlayUserReportSheetViewComponent, item)
        }
    })

    init {
        findViewById<ImageView>(commonR.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@PlayUserReportSheetViewComponent)
            }

        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title).text = getString(R.string.play_kebab_report_title)

        rvCategory.apply {
            adapter = categoryAdapter
            addItemDecoration(ReasoningListItemDecoration(rvCategory.context))
        }
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
        globalError.show()

        globalError.setActionClickListener {
            onError()
        }

        globalError.setType(
            if (isConnectionError) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR
        )
    }

    fun showPlaceholder(){
        categoryAdapter.setItemsAndAnimateChanges(getPlaceholderModel().reasoningList)
    }

    fun showView(){
        show()
    }

    private fun getPlaceholderModel() = PlayUserReportUiModel.Loaded(
        reasoningList = List(5){ PlayUserReportReasoningUiModel.Placeholder},
        resultState = ResultState.Loading
    )

    interface Listener{
        fun onCloseButtonClicked(view: PlayUserReportSheetViewComponent)
        fun onItemReportClick(view: PlayUserReportSheetViewComponent, item: PlayUserReportReasoningUiModel.Reasoning)
        fun onFooterClicked(view: PlayUserReportSheetViewComponent)
    }
}
