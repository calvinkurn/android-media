package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheet3DotsMenuBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ThreeDotsMenuBottomSheet : BottomSheetUnify() {

    private var binding by autoClearedNullable<SmvcBottomsheet3DotsMenuBinding>()
    private var bottomSheetType = 0
    private var voucherName = ""
    companion object {
        @JvmStatic
        fun newInstance(): ThreeDotsMenuBottomSheet {
            return ThreeDotsMenuBottomSheet()
        }
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = SmvcBottomsheet3DotsMenuBinding.inflate(inflater, container, false)
        binding = viewBinding
        setChild(viewBinding.root)
        setLayout()
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setLayout() {
        setTitle(voucherName)
        binding?.run {
            when(bottomSheetType) {
                TYPE_1 -> {
                    tpgCancel.text = getString(R.string.smvc_cancel_label)
                }
                TYPE_2 -> {
                    tpgCancel.text = getString(R.string.smvc_stop_label)
                }
                else -> {
                    iconCancel.gone()
                    tpgCancel.gone()
                }
            }
        }
    }

    fun setData(voucherName: String, type: Int) {
        this.voucherName = voucherName
        this.bottomSheetType = type
    }
}
