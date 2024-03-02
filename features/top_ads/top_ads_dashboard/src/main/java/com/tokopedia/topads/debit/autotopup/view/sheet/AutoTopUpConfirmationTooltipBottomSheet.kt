package com.tokopedia.topads.debit.autotopup.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.topads.dashboard.databinding.TopadsAutoConfirmationTooltipBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AutoTopUpConfirmationTooltipBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TopadsAutoConfirmationTooltipBottomSheetBinding>()

    private var tooltipTitle = ""
    private var tooltipDesc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setArgs()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setTitle(tooltipTitle)
        setChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTooltipDesc()
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) {
            show(fragmentManager, TAG)
        }
    }

    private fun setChildView(inflater: LayoutInflater, container: ViewGroup?) {
        binding = TopadsAutoConfirmationTooltipBottomSheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    private fun setArgs() {
        tooltipTitle = arguments?.getString(AUTO_TU_CONFIRMATION_TOOLTIP_TITLE).orEmpty()
        tooltipDesc = arguments?.getString(AUTO_TU_CONFIRMATION_TOOLTIP_DESC).orEmpty()
    }

    private fun setTooltipDesc() {
        binding?.tvAutoConfirmationTooltipDesc?.text = tooltipDesc
    }

    companion object {

        private val TAG = AutoTopUpConfirmationTooltipBottomSheet::class.java.simpleName

        private const val AUTO_TU_CONFIRMATION_TOOLTIP_TITLE = "auto_ta_confirmation_tooltip_title"
        private const val AUTO_TU_CONFIRMATION_TOOLTIP_DESC = "auto_ta_confirmation_tooltip_desc"

        fun newInstance(title: String, desc: String): AutoTopUpConfirmationTooltipBottomSheet {
            return AutoTopUpConfirmationTooltipBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(AUTO_TU_CONFIRMATION_TOOLTIP_TITLE, title)
                    putString(AUTO_TU_CONFIRMATION_TOOLTIP_DESC, desc)
                }
            }
        }
    }
}
