package com.tokopedia.chatbot.chatbot2.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter.MediaRetryBottomSheetAdapter
import com.tokopedia.chatbot.databinding.BottomsheetChatbotRetryUploadMediaBinding
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ChatbotMediaRetryBottomSheet(private val element: SendableUiModel, private val onBottomSheetItemClicked: (position: Int) -> Unit) : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetChatbotRetryUploadMediaBinding>()
    private var retryAdapter: MediaRetryBottomSheetAdapter? = null

    init {
        showCloseIcon = false
        showKnob = true
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetChatbotRetryUploadMediaBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)

        if (element is ImageUploadUiModel) {
            setTitle(
                context?.resources?.getString(R.string.chatbot_retry_image_upload_bottom_sheet_title).toBlankOrString()
            )
        } else {
            setTitle(
                context?.resources?.getString(R.string.chatbot_retry_video_upload_bottom_sheet_title).toBlankOrString()
            )
        }

        retryAdapter = MediaRetryBottomSheetAdapter(onBottomSheetItemClicked)
        retryAdapter?.setList(
            listOf(
                context?.resources?.getString(R.string.chatbot_delete).toBlankOrString(),
                context?.resources?.getString(R.string.chatbot_resend).toBlankOrString()
            )
        )

        binding?.rvMediaRetry?.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            adapter = retryAdapter
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
