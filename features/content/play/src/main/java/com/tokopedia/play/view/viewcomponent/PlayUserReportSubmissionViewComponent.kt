package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by astidhiyaa on 08/12/21
 */
class PlayUserReportSubmissionViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_user_report_submission_sheet) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val btnBack: IconUnify = findViewById(com.tokopedia.play_common.R.id.iv_sheet_close)

    private val tvTitle: TextView = findViewById(R.id.tv_user_report_title)
    private val tvDesc: TextView = findViewById(R.id.tv_user_report_desc)
    private val etSubmission: TextAreaUnify2 = findViewById(R.id.et_detail_report)
    private val btnSubmit: UnifyButton = findViewById(R.id.btn_action)

    init {
        btnBack.setImage(IconUnify.ARROW_BACK)

        btnBack
            .setOnClickListener {
                listener.onCloseButtonClicked(this@PlayUserReportSubmissionViewComponent)
            }

        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title).text = getString(R.string.play_kebab_report_title)
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

    fun setView(item: PlayUserReportReasoningUiModel.Reasoning){
        tvTitle.text = item.title
        tvDesc.text = item.detail
        etSubmission.setLabel(item.submissionData.label)
        val charMin = getString(R.string.play_user_report_text_area_min, item.submissionData.min)
        etSubmission.setMessage(charMin)

        btnSubmit.setOnClickListener {
            listener.onSubmitUserReport(this@PlayUserReportSubmissionViewComponent)
        }
    }

    interface Listener {
        fun onCloseButtonClicked(view: PlayUserReportSubmissionViewComponent)
        fun onSubmitUserReport(view: PlayUserReportSubmissionViewComponent)
    }
}