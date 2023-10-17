package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.databinding.BottomsheetReportBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.BottomSheetUnify

class ReportBottomSheet : BottomSheetUnify() {

    private var _binding: BottomsheetReportBinding? = null
    private val binding: BottomsheetReportBinding
        get() = _binding!!

    private var onReportOptionsClick: OnReportOptionsClick? = null
    private var isClicked = 0
    private var reasonType: String = ""
    private var reasonDesc: String = ""
    var onDismiss: (() -> Unit)? = null
    var onClosedClicked: (() -> Unit)? = null
    private var dismissedByClosing = false

    companion object {
        private const val SPAM = 1
        private const val ABUSE = 2
        private const val INAPPROPRIATE = 3
        private const val TYPE1 = 1
        private const val TYPE2 = 2
        private const val TYPE3 = 3
        fun newInstance(context: OnReportOptionsClick): ReportBottomSheet {
            return ReportBottomSheet().apply {
                this.onReportOptionsClick = context
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetReportBinding.inflate(layoutInflater)
        setChild(binding.root)
        setTitle(getString(com.tokopedia.content.common.R.string.feed_report_comment))

        return binding.root
    }

    private fun sendReport() {
        getReason()
        onReportOptionsClick?.onReportAction(reasonType, reasonDesc)
    }

    private fun getReason() {
        when (isClicked) {
            TYPE1 -> {
                reasonType = getString(com.tokopedia.content.common.R.string.feed_common_reason_type_spam)
                reasonDesc = getString(com.tokopedia.content.common.R.string.feed_common_reason_desc_spam)
            }
            TYPE2 -> {
                reasonType = getString(com.tokopedia.content.common.R.string.feed_common_reason_type_abuse)
                reasonDesc = getString(com.tokopedia.content.common.R.string.feed_common_reason_desc_abuse)
            }
            TYPE3 -> {
                reasonType = getString(com.tokopedia.content.common.R.string.feed_common_reason_type_inappropriate)
                reasonDesc = getString(com.tokopedia.content.common.R.string.feed_common_reason_desc_inappropriate)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            reportSubtext1.setOnClickListener {
                setSpamCase()
            }
            reportSubtext1Icon.setOnClickListener {
                setSpamCase()
            }
            reportSubtext2.setOnClickListener {
                setAbuseCase()
            }
            reportSubtext2Icon.setOnClickListener {
                setAbuseCase()
            }
            reportSubtext3.setOnClickListener {
                setInappropriateCase()
            }
            reportSubtext3Icon.setOnClickListener {
                setInappropriateCase()
            }
            setCloseClickListener {
                dismissedByClosing = true
                onClosedClicked?.invoke()
                dismiss()
            }
            setOnDismissListener {
                if (!dismissedByClosing) onDismiss?.invoke()
            }
        }
    }

    private fun setSpamCase() {
        isClicked = SPAM
        sendReport()
    }

    private fun setAbuseCase() {
        isClicked = ABUSE
        sendReport()
    }

    private fun setInappropriateCase() {
        isClicked = INAPPROPRIATE
        sendReport()
    }

     fun setFinalView() = with(binding) {
        layout1.gone()
        layout2.root.gone()
        layout3.root.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnReportOptionsClick {
        fun onReportAction(reasonType: String, reasonDesc: String)
    }
}
