package com.tokopedia.contactus.inboxtickets.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.contactus.databinding.CloseComplainBottomSheetLayoutBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CloseComplainBottomSheet : BottomSheetUnify() {

    var viewBinding by autoClearedNullable<CloseComplainBottomSheetLayoutBinding>()
    private var closeBottomSheetListener: CloseComplainBottomSheetListner? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showKnob = true
        showCloseIcon = false
        showHeader = false
        viewBinding = CloseComplainBottomSheetLayoutBinding.inflate(inflater).also {
            setChild(it.root)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    private fun setListener(){
        viewBinding?.tvNoButton?.setOnClickListener {
            closeBottomSheetListener?.onClickComplain(false)
        }
        viewBinding?.tvYesButton?.setOnClickListener {
            closeBottomSheetListener?.onClickComplain(true)
        }
    }

    fun setCloseListener(closeBottomSheetListener: CloseComplainBottomSheetListner){
        this.closeBottomSheetListener = closeBottomSheetListener
    }

    interface CloseComplainBottomSheetListner {
        fun onClickComplain(agreed: Boolean)
    }
}
