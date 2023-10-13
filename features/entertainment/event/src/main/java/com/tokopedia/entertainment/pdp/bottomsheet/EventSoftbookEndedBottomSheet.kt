package com.tokopedia.entertainment.pdp.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.entertainment.databinding.BottomSheetEventSoftbookEndedBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class EventSoftbookEndedBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomSheetEventSoftbookEndedBinding>()
    private var mListener: OnClickSoftbookEndedButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
        overlayClickDismiss = false

        binding = BottomSheetEventSoftbookEndedBinding.inflate(LayoutInflater.from(context))
        setTitle("Sesi Pemesanan Habis")
        setChild(binding?.root)

    }

    private fun initView() {
        binding?.run {
            imgSoftbookEnded.setImageUrl(IMG_URL)
            buttonEventSoftbookEnded.setOnClickListener {
                mListener?.onClickButton()
            }
        }
    }

    fun setOnClickSoftbookEndedButton(listener: OnClickSoftbookEndedButton) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG_EVENT_SOFTBOOK_ENDED_BOTTOM_SHEET)
    }

    fun interface OnClickSoftbookEndedButton {
        fun onClickButton()
    }

    companion object {
        private const val IMG_URL = "https://images.tokopedia.net/img/event/event_checkout_softbook_ended_bottom_sheet.png"
        private const val TAG_EVENT_SOFTBOOK_ENDED_BOTTOM_SHEET = "event_softbook_ended_bottom_sheet"
    }
}
