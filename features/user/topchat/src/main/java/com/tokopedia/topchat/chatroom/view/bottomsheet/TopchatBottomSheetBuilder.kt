package com.tokopedia.topchat.chatroom.view.bottomsheet

import android.content.Context
import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

object TopchatBottomSheetBuilder {

    fun getErrorUploadImageBs(
            context: Context,
            onRetryClicked: () -> Unit,
            onDeleteClicked: () -> Unit
    ): BottomSheetUnify {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(
                context, R.layout.bs_topchat_upload_image_error_action, null
        )
        val retryBtn = view.findViewById<Typography>(R.id.tp_topchat_retry_send_img)
        val deleteBtn = view.findViewById<Typography>(R.id.tp_topchat_delete_image)
        retryBtn?.setOnClickListener {
            onRetryClicked()
            bottomSheetUnify.dismiss()
        }
        deleteBtn?.setOnClickListener {
            onDeleteClicked()
            bottomSheetUnify.dismiss()
        }
        bottomSheetUnify.apply {
            showCloseIcon = false
            showHeader = false
            setChild(view)
        }
        return bottomSheetUnify
    }
}