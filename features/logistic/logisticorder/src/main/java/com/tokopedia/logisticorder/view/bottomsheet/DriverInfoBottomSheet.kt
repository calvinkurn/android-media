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
            imgInfoDriver.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_illus_tipping_retry) })
            tvInfoDriver.text = "Driver bisa berganti\nsewaktu-waktu"
            tvInfoDriverDetail.text = "Jika driver berganti, tip akan diberikan\nke driver yang mengantar pesanan"
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