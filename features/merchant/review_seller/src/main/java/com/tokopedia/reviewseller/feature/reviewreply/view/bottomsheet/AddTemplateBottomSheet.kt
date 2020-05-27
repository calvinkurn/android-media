package com.tokopedia.reviewseller.feature.reviewreply.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface

class AddTemplateBottomSheet(mActivity: FragmentActivity?,
                             private val titleBottomSheet: String,
                             private val viewModel: SellerReviewReplyViewModel,
                             private val userSession: UserSessionInterface) : BottomSheetUnify() {

    private var tvTitleTemplate: TextFieldUnify? = null
    private var tvDescTemplate: TextFieldUnify? = null
    private var btnSubmitTemplate: UnifyButton? = null

    init {
        val contentView = View.inflate(mActivity, R.layout.bottom_sheet_insert_template, null)
        tvTitleTemplate = contentView?.findViewById(R.id.tfuTitleTemplate)
        tvDescTemplate = contentView?.findViewById(R.id.tfuDescTemplate)
        btnSubmitTemplate = contentView?.findViewById(R.id.btnSubmitTemplate)
        showCloseIcon = true
        isDragable = true
        isFullpage = true
        setTitle(titleBottomSheet)
        setCloseClickListener {
            dismiss()
        }
        setChild(contentView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitAddTemplate()
    }

    fun showDialog() {
        fragmentManager?.let {
            show(it, titleBottomSheet)
        }
    }

    private fun submitAddTemplate() {
        btnSubmitTemplate?.setOnClickListener {
            val title = tvTitleTemplate?.textFieldInput?.text.toString()
            val desc = tvDescTemplate?.textFieldInput?.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()) {
                viewModel.insertTemplateReviewReply(userSession.shopId.toIntOrZero(), title, desc)
            } else {
                if (title.isEmpty()) {
                    tvTitleTemplate?.setMessage(getString(R.string.empty_message_add_template_label))
                }
                if (desc.isEmpty()) {
                    tvDescTemplate?.setMessage(getString(R.string.empty_message_add_template_label))
                }
            }
        }
    }
}