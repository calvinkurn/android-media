package com.tokopedia.play.view.viewcomponent

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.R
import com.tokopedia.content.common.R as commonR
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by astidhiyaa on 08/12/21
 */
class PlayUserReportSubmissionViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, commonR.id.cl_user_report_submission_sheet) {

    private val btnBack: IconUnify = findViewById(commonR.id.iv_sheet_close)

    private val tvTitle: TextView = findViewById(commonR.id.tv_user_report_title)
    private val tvDesc: TextView = findViewById(commonR.id.tv_user_report_desc)
    private val etSubmission: TextAreaUnify2 = findViewById(commonR.id.et_detail_report)
    private val btnSubmit: UnifyButton = findViewById(commonR.id.btn_action)
    private val tvFooter: TextView = findViewById(commonR.id.tv_user_report_footer)

    private val errorFieldPrefix: String = "Isi"

    private var minChar : Int = 0

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            etSubmission.isInputError = s.isEmpty() || s.length < minChar
            btnSubmit.isEnabled = s.isNotEmpty() && s.length >= minChar
            etSubmission.setMessage(getFieldMessage(etSubmission.isInputError))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    init {
        btnBack.setImage(IconUnify.ARROW_BACK)

        btnBack
            .setOnClickListener {
                listener.onCloseButtonClicked(this@PlayUserReportSubmissionViewComponent)
            }

        findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title).text = getString(R.string.play_kebab_report_title)

        tvFooter.text = MethodChecker.fromHtml(getString(commonR.string.content_user_report_footer))
        tvFooter.setOnClickListener {
            listener.onFooterClicked(this@PlayUserReportSubmissionViewComponent)
        }
    }

    fun setView(item: PlayUserReportReasoningUiModel.Reasoning){
        minChar = item.submissionData.min

        etSubmission.editText.addTextChangedListener(textWatcher)

        tvTitle.text = item.title
        tvDesc.text = item.detail
        etSubmission.setCounter(item.submissionData.max)
        etSubmission.setLabel(item.submissionData.label)
        etSubmission.setMessage(getFieldMessage(false))
        etSubmission.editText.text.clear()

        etSubmission.setOnClickListener {
            etSubmission.requestFocus()
        }

        btnSubmit.setOnClickListener {
            etSubmission.clearFocus()

            val desc = etSubmission.editText.text.toString()
            listener.onShowVerificationDialog(this@PlayUserReportSubmissionViewComponent, reasonId = item.reasoningId, description = desc)
        }
    }

    private fun getFieldMessage(isError: Boolean) : String{
        return if(isError){
            getString(commonR.string.content_user_report_text_area_min, errorFieldPrefix, minChar)
        }else{
            getString(commonR.string.content_user_report_text_area_min, "", minChar)
        }
    }

    fun showView(height: Int){
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }
        show()
    }

    interface Listener {
        fun onCloseButtonClicked(view: PlayUserReportSubmissionViewComponent)
        fun onFooterClicked(view: PlayUserReportSubmissionViewComponent)
        fun onShowVerificationDialog(view: PlayUserReportSubmissionViewComponent, reasonId: Int, description: String)
    }
}
