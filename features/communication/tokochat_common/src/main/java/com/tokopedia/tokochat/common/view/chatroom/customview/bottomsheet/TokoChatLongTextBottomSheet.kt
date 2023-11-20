package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat_common.databinding.TokochatLongMessageBottomsheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TokoChatLongTextBottomSheet(
    private val longMessage: String,
    private val senderName: String
): BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatLongMessageBottomsheetBinding>()

    init {
        this.overlayClickDismiss = true
        this.showCloseIcon = false
        this.isDragable = true
        this.isHideable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.tokochat_common.R.layout.tokochat_long_message_bottomsheet, container, false)
        binding = TokochatLongMessageBottomsheetBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
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

    private fun setPeakHeight() {
        try {
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / PEAK_DIVIDER
        } catch (ignored: Throwable) {}
    }

    private fun setBottomSheetTitle() {
        this.setTitle(senderName)
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

