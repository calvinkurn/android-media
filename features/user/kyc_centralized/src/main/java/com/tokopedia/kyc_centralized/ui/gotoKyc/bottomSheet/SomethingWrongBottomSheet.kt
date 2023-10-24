package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.LayoutGotoKycSomethingWrongBinding
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SomethingWrongBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<LayoutGotoKycSomethingWrongBinding>()

    private var onClickButton: () -> Unit = {}

    private var type = ""
    private var title = ""
    private var description = ""
    private var button = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE).orEmpty()
            title = it.getString(TITLE).orEmpty()
            description = it.getString(DESCRIPTION).orEmpty()
            button = it.getString(BUTTON).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutGotoKycSomethingWrongBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        showCloseIcon = type.isConnectionIssue().not()
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initView()
    }

    private fun initView() {
        val imageUrl = if (type.isConnectionIssue()) {
            getString(R.string.img_goto_kyc_connection_issue)
        } else {
            getString(R.string.img_url_goto_kyc_status_submission_rejected)
        }
        binding?.ivSomethingWrong?.loadImageWithoutPlaceholder(
            imageUrl
        )

        val textTitle = if (type.isConnectionIssue()) {
            getString(R.string.goto_kyc_internet_issue_title)
        } else {
            title
        }
        binding?.tvHeader?.text = textTitle

        val textDescription = if (type.isConnectionIssue()) {
            getString(R.string.goto_kyc_internet_issue_description)
        } else {
            description
        }
        binding?.tvDescription?.text = textDescription

        val textButton = if (type.isConnectionIssue()) {
            getString(R.string.goto_kyc_try_again)
        } else {
            button
        }
        binding?.btnAction?.text = textButton
    }

    private fun String.isConnectionIssue() : Boolean {
        return this == TAG_CONNECTION_ISSUE
    }

    private fun initListener() {
        binding?.btnAction?.setOnClickListener {
            onClickButton()
        }
    }

    fun setOnClickRetryListener(listener: () -> Unit) {
        onClickButton = listener
    }

    companion object {
        private const val TYPE = "TYPE"
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val BUTTON = "BUTTON"
        fun newInstance(type: String, title: String = "", description: String = "", button: String = "") =
            SomethingWrongBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(TYPE, type)
                    putString(TITLE, title)
                    putString(DESCRIPTION, description)
                    putString(BUTTON, button)
                }
            }
        const val TAG_CONNECTION_ISSUE = "CONNECTION_ISSUE_BOTTOM_SHEET"
        const val TAG_SOMETHING_WRONG = "SOMETHING_WRONG_BOTTOM_SHEET"
    }

}
