package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView
import kotlinx.android.synthetic.main.mvc_create_promo_code_bottom_sheet_view.*


class CreatePromoCodeBottomSheet(val onNextClick: (String) -> Unit = {}) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        fun createInstance(context: Context?, onNextClick: (String) -> Unit) : CreatePromoCodeBottomSheet {
            return CreatePromoCodeBottomSheet(onNextClick).apply {
                context?.run {
                    val view = View.inflate(this, R.layout.mvc_create_promo_code_bottom_sheet_view, null)
                    setChild(view)
                    setTitle(title.toBlankOrString())
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPromoCodeTextField?.textFieldInput?.run {
            setOnFocusChangeListener { v, hasFocus ->
                activity?.let {
                    if (hasFocus) {
                        KeyboardHandler.showSoftKeyboard(it)
                    } else {
                        KeyboardHandler.hideSoftKeyboard(it)
                    }
                }
            }
            requestFocus()
        }

        createPromoCodeSaveButton?.setOnClickListener {
            onNextClick(createPromoCodeTextField?.textFieldInput?.text?.toString().toBlankOrString())
        }

    }

    override val title: String? = context?.resources?.getString(R.string.mvc_create_target_create_promo_code_bottomsheet_title)

}