package com.tokopedia.editshipping.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.BottomsheetInfoGocarBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared

class GocarInfoBottomSheet : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetInfoGocarBinding>()

    init {
        showCloseIcon = false
        setCloseClickListener {
            dismiss()
        }

        setOnDismissListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        binding = BottomsheetInfoGocarBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        setTitle(getString(R.string.gocar_info_bottomsheet_title))
        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, "")
        }
    }
}