package com.tokopedia.tokochat.view.chatroom.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.tokochat.databinding.TokochatGeneralUnavailableBottomsheetBinding
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.tokochat.R as tokochatR

class TokoChatGeneralUnavailableBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatGeneralUnavailableBottomsheetBinding>()

    private var buttonAction: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            tokochatR.layout.tokochat_general_unavailable_bottomsheet,
            container,
            false
        )
        binding = TokochatGeneralUnavailableBottomsheetBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding?.tokochatBsImgUnavailableGeneral?.setImageUrl(
            TokoChatUrlUtil.IMAGE_UNAVAILABLE_GENERAL_URL)
        setBottomSheetCloseButton()
        setBottomSheetButton()
    }

    private fun setBottomSheetCloseButton() {
        this.overlayClickDismiss = false
        this.showCloseIcon = true
        this.setCloseClickListener {
            buttonAction?.invoke()
        }
    }

    private fun setBottomSheetButton() {
        binding?.tokochatBsBtnUnavailableGeneral?.setOnClickListener {
            buttonAction?.invoke()
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun setListener(buttonAction: () -> Unit) {
        this.buttonAction = buttonAction
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.buttonAction = null
    }

    companion object {
        private val TAG = TokoChatGeneralUnavailableBottomSheet::class.simpleName
    }
}
