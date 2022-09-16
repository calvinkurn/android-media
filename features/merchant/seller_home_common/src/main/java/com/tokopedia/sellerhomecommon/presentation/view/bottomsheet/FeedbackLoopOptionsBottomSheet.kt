package com.tokopedia.sellerhomecommon.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcBottomSheetFeedbackLoopBinding
import com.tokopedia.sellerhomecommon.presentation.model.FeedbackLoopOptionUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.FeedbackLoopOptionAdapter

/**
 * Created by @ilhamsuaib on 22/08/22.
 */

class FeedbackLoopOptionsBottomSheet : BaseBottomSheet<ShcBottomSheetFeedbackLoopBinding>() {

    companion object {
        private const val TAG = "FeedbackLoopOptionsBottomSheet"

        fun createInstance(): FeedbackLoopOptionsBottomSheet {
            return FeedbackLoopOptionsBottomSheet()
        }
    }

    private var feedbackItems: List<FeedbackLoopOptionUiModel> = emptyList()
    private var onSubmitClicked: (List<FeedbackLoopOptionUiModel>) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ShcBottomSheetFeedbackLoopBinding.inflate(inflater).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupView() = binding?.run {
        setupOptionItem()
        setTitle(root.context.getString(R.string.shc_feedback_bottom_sheet_title))
        tvShcFeedbackLoopTitle.text = root.context
            .getString(R.string.shc_feedback_content_bottom_sheet_title)
        btnShcFeedbackLoopOptions.setOnClickListener {
            onSubmitClicked(feedbackItems)
        }
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isVisible) {
            return
        }

        show(fm, TAG)
    }

    fun setOnSubmitClickedListener(onSubmitClicked: (List<FeedbackLoopOptionUiModel>) -> Unit) {
        this.onSubmitClicked = onSubmitClicked
    }

    private fun setupOptionItem() {
        binding?.rvShcFeedbackLoopOptions?.run {
            if (feedbackItems.isEmpty()) {
                feedbackItems = FeedbackLoopOptionUiModel.getOptions(context)
            }

            val mAdapter = FeedbackLoopOptionAdapter(feedbackItems) {
                setSendButtonEnabled()
            }

            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setSendButtonEnabled() {
        binding?.run {
            val isEligibleSubmit = feedbackItems.any {
                it.isSelected
            }
            btnShcFeedbackLoopOptions.isEnabled = isEligibleSubmit

            val otherItem = feedbackItems.firstOrNull {
                it is FeedbackLoopOptionUiModel.Other
            } as? FeedbackLoopOptionUiModel.Other

            otherItem?.let {
                if (it.isSelected) {
                    btnShcFeedbackLoopOptions.isEnabled = it.value.isNotBlank()
                }
            }
        }
    }
}