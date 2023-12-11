package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatLongMessageBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.tokochat_common.R as tokochat_commonR

class TokoChatLongTextBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatLongMessageBottomsheetBinding>()
    private var longMessage: String = ""
    private var senderName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            tokochat_commonR.layout.tokochat_long_message_bottomsheet, container, false)
        binding = TokochatLongMessageBottomsheetBinding.bind(view)
        setupBottomSheetConfig()
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheetConfig() {
        this.overlayClickDismiss = true
        this.showCloseIcon = false
        this.isDragable = true
        this.isHideable = true
        this.clearContentPadding = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        setPeakHeight()
        setBottomSheetTitle()
        setLongMessageText()
        bottomSheetBehaviorKnob(view, true)
    }

    fun setMessage(
        longMessage: String,
        senderName: String
    ) {
        this.longMessage = longMessage
        this.senderName = senderName
    }

    private fun setPeakHeight() {
        try {
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / PEAK_DIVIDER
        } catch (ignored: Throwable) {}
    }

    private fun setBottomSheetTitle() {
        this.setTitle(
            getString(R.string.tokochat_long_text_bottomsheet_title, senderName)
        )
    }

    private fun setLongMessageText() {
        binding?.tokochatTvLongMessage?.text = longMessage
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    companion object {
        private val TAG = TokoChatLongTextBottomSheet::class.simpleName
        private const val PEAK_DIVIDER = 2
    }
}

