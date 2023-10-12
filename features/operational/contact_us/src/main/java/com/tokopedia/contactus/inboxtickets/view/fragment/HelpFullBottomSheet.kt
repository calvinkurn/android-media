package com.tokopedia.contactus.inboxtickets.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.contactus.databinding.HelpfullBottomSheetLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class HelpFullBottomSheet : BottomSheetUnify() {

    private var closeBottomSheetListener: CloseSHelpFullBottomSheet? = null
    var viewBinding by autoClearedNullable<HelpfullBottomSheetLayoutBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showKnob = true
        showCloseIcon = false
        showHeader = false
        viewBinding = HelpfullBottomSheetLayoutBinding.inflate(inflater).also {
            setChild(it.root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    fun setCloseListener(closeBottomSheetListener: CloseSHelpFullBottomSheet){
        this.closeBottomSheetListener = closeBottomSheetListener
    }

    private fun setListener(){
        viewBinding?.tvNoButton?.setOnClickListener {
            closeBottomSheetListener?.onClick(false)
        }
        viewBinding?.tvYesButton?.setOnClickListener {
            closeBottomSheetListener?.onClick(true)
        }
    }

    interface CloseSHelpFullBottomSheet {
        fun onClick(agreed: Boolean)
    }
}
