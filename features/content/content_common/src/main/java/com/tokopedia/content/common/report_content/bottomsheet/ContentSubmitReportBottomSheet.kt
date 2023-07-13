package com.tokopedia.content.common.report_content.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.databinding.ViewUserReportSubmissionBinding
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlin.math.roundToInt
import com.tokopedia.content.common.R as commonR

/**
 * @author by astidhiyaa on 31/05/23
 */
class ContentSubmitReportBottomSheet : BottomSheetUnify() {

    private var _binding: ViewUserReportSubmissionBinding? = null
    private val binding: ViewUserReportSubmissionBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private var minChar: Int = 0

    private var item: PlayUserReportReasoningUiModel.Reasoning = PlayUserReportReasoningUiModel.Reasoning.Empty

    private val maxSheetHeight by lazyThreadSafetyNone {
        (getScreenHeight() * HEIGHT_PERCENTAGE).roundToInt()
    }

    private val textWatcher by lazyThreadSafetyNone {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.etDetailReport.isInputError = s.isEmpty() || s.length < minChar
                binding.btnAction.isEnabled = s.isNotEmpty() && s.length >= minChar
                binding.etDetailReport.setMessage(getFieldMessage(binding.etDetailReport.isInputError))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismiss()
    }

    private fun initBottomSheet() {
        clearContentPadding = false
        showHeader = false

        _binding = ViewUserReportSubmissionBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        setOnDismissListener {
            mListener?.onBackButtonListener()
        }
    }

    private fun setupView() {
        binding.headerContentReportSubmission.title = getString(commonR.string.content_user_report_header)
        binding.headerContentReportSubmission.icon = IconUnify.ARROW_BACK
        binding.headerContentReportSubmission.closeListener = View.OnClickListener {
            dismiss()
            mListener?.onBackButtonListener()
        }
        binding.tvUserReportFooter.text =
            MethodChecker.fromHtml(getString(commonR.string.content_user_report_footer))
        binding.tvUserReportFooter.setOnClickListener {
            mListener?.onFooterClicked()
        }

        minChar = item.submissionData.min

        with(binding) {
            etDetailReport.editText.addTextChangedListener(textWatcher)

            tvUserReportTitle.text = item.title
            tvUserReportDesc.text = item.detail
            etDetailReport.setCounter(item.submissionData.max)

            etDetailReport.setLabel(item.submissionData.label)
            etDetailReport.setMessage(getFieldMessage(false))
            etDetailReport.editText.text.clear()

            etDetailReport.setOnClickListener {
                etDetailReport.requestFocus()
            }

            btnAction.setOnClickListener {
                etDetailReport.clearFocus()

                val desc = etDetailReport.editText.text.toString()
                mListener?.onSubmitReport(desc)
            }
        }
    }

    private fun getFieldMessage(isError: Boolean): String {
        val prefix = if (isError) ERROR_FIELD_PREFIX else ""
        return getString(commonR.string.content_user_report_text_area_min, prefix, minChar)
    }

    fun setData(mData: PlayUserReportReasoningUiModel.Reasoning) {
        item = mData
    }

    override fun onResume() {
        binding.clUserReportSubmissionContent.layoutParams = binding.clUserReportSubmissionContent.layoutParams.apply {
            height = maxSheetHeight
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        mListener = null
    }

    interface Listener {
        fun onBackButtonListener()
        fun onFooterClicked()
        fun onSubmitReport(desc: String)
    }

    companion object {
        const val TAG = "ContentSubmitReportBottomSheet"
        private const val HEIGHT_PERCENTAGE = 0.75
        private const val ERROR_FIELD_PREFIX: String = "Isi"

        fun get(fragmentManager: FragmentManager): ContentSubmitReportBottomSheet? {
            return fragmentManager.findFragmentByTag(TAG) as? ContentSubmitReportBottomSheet
        }

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ContentSubmitReportBottomSheet {
            return get(fragmentManager) ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ContentSubmitReportBottomSheet::class.java.name
            ) as ContentSubmitReportBottomSheet
        }
    }
}
