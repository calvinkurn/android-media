package com.tokopedia.logisticorder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.BottomsheetDriverInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared

class DriverInfoBottomSheet : BottomSheetUnify() {

    private var binding by autoCleared<BottomsheetDriverInfoBinding>()

    init {
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
        binding = BottomsheetDriverInfoBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDriverInfoBottomSheet()
    }

    private fun setupDriverInfoBottomSheet() {
        binding.apply {
            imgInfoDriver.urlSrc = "https://images.tokopedia.net/img/android/tipping/illus-tipping-retry.png"
            tvInfoDriver.text = getString(R.string.info_driver_title)
            tvInfoDriverDetail.text = getString(R.string.info_driver_detail)
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