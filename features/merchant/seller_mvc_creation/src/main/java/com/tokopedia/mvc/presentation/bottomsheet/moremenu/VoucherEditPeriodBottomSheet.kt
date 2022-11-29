package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetEditPeriodBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VoucherEditPeriodBottomSheet: BottomSheetUnify() {

    private var voucher: Voucher? = null

    private var binding by autoClearedNullable<SmvcBottomsheetEditPeriodBinding>()

    init {
        isFullpage = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetEditPeriodBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.getString(R.string.edit_period_title) ?: "")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding?.apply {
            voucher?.let {

                edtMvcStartDate.run {
                    editText.run {
                        setOnClickListener {

                        }
                        labelText.text = context?.getString(R.string.edit_period_start_date).toBlankOrString()
                        disableText(this)
                    }
                }

                edtMvcEndDate.run {
                    editText.run {
                        setOnClickListener {

                        }
                        labelText.text = context?.getString(R.string.edit_period_end_date).toBlankOrString()
                        disableText(this)
                    }
                }
            }
        }

        setAction(context?.getString(R.string.edit_period_reset).toBlankOrString()) {

        }

    }

    private fun disableText(autoCompleteTextView: AutoCompleteTextView) {
        autoCompleteTextView.apply {
            isFocusable = false
            isClickable = true
            keyListener = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(voucher: Voucher): VoucherEditPeriodBottomSheet {
            return VoucherEditPeriodBottomSheet().apply {
                this.voucher = voucher
            }
        }

    }

}
