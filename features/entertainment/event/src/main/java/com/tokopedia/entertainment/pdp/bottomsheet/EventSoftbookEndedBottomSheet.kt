package com.tokopedia.entertainment.pdp.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.entertainment.databinding.BottomSheetEventSoftbookEndedBinding
import com.tokopedia.imageassets.TokopediaImageUrl.EVENT_SOFTBOOK_ENDED_BOTTOM_SHEET_IMG_URL
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.entertainment.R as entertainmentR

class EventSoftbookEndedBottomSheet : BottomSheetUnify() {

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
                activity?.finish()
                true
            }
            false
        }
        return dialog
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = false
        showCloseIcon = false
        overlayClickDismiss = false

        binding = BottomSheetEventSoftbookEndedBinding.inflate(LayoutInflater.from(context))
        setTitle(getString(entertainmentR.string.ent_event_checkout_softbook_ended_bottom_sheet_title))
        setChild(binding?.root)
    }

    private fun initView() {
        binding?.run {
            imgSoftbookEnded.setImageUrl(EVENT_SOFTBOOK_ENDED_BOTTOM_SHEET_IMG_URL)
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
        private const val TAG_EVENT_SOFTBOOK_ENDED_BOTTOM_SHEET = "event_softbook_ended_bottom_sheet"
    }
}
