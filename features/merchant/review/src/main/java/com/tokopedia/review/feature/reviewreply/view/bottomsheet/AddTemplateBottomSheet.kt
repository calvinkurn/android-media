package com.tokopedia.review.feature.reviewreply.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.review.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class AddTemplateBottomSheet(private val mActivity: FragmentActivity?,
                             private val titleBottomSheet: String,
                             private val listener: (title: String, desc: String) -> Unit) : BottomSheetUnify() {

    private var tvTitleTemplate: TextFieldUnify? = null
    private var tvDescTemplate: TextFieldUnify? = null
    private var btnSubmitTemplate: UnifyButton? = null

    init {
        val contentView = View.inflate(mActivity, R.layout.bottom_sheet_insert_template, null)
        contentView.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        tvTitleTemplate = contentView?.findViewById(R.id.tfuTitleTemplate)
        tvDescTemplate = contentView?.findViewById(R.id.tfuDescTemplate)
        btnSubmitTemplate = contentView?.findViewById(R.id.btnSubmitTemplate)
        setTitle(titleBottomSheet)
        setCloseClickListener {
            dismiss()
        }
        setChild(contentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitAddTemplate()
        showCloseIcon = true
        isFullpage = true
    }

    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, titleBottomSheet)
        }
    }

    private fun submitAddTemplate() {

        tvTitleTemplate?.textFieldInput?.afterTextChanged {
            if (it.isEmpty()) {
                tvTitleTemplate?.setMessage(getString(R.string.empty_title_add_template_label))
            }
        }

        tvDescTemplate?.textFieldInput?.afterTextChanged {
            if(it.isEmpty()) {
                tvDescTemplate?.setMessage(getString(R.string.empty_desc_add_template_label))
            }
        }

        btnSubmitTemplate?.setOnClickListener {
            val title = tvTitleTemplate?.textFieldInput?.text?.trim().toString()
            val desc = tvDescTemplate?.textFieldInput?.text?.trim().toString()

            if (title.isNotEmpty() && desc.isNotEmpty()) {
                btnSubmitTemplate?.isLoading = true
                listener.invoke(title, desc)
            } else {
                if (title.isEmpty()) {
                    tvTitleTemplate?.setMessage(getString(R.string.empty_title_add_template_label))
                }
                if (desc.isEmpty()) {
                    tvDescTemplate?.setMessage(getString(R.string.empty_desc_add_template_label))
                }
            }
        }
    }
}