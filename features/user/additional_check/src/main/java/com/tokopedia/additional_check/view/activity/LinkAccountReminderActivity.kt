package com.tokopedia.additional_check.view.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.additional_check.R
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class LinkAccountReminderActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLinkAccountReminderDialog()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun showLinkAccountReminderDialog() {
        try {
            val contentView = View.inflate(this, com.tokopedia.additional_check.R.layout.bottom_sheet_link_account_reminder, null)
            val bottomSheetUnify = BottomSheetUnify().apply {
                val btn = contentView.findViewById<UnifyButton>(R.id.link_account_bs_reminder_primary_btn)
                btn.setOnClickListener {
                    RouteManager.route(this@LinkAccountReminderActivity, ApplinkConst.LINK_ACCOUNT)
                    dismiss()
                    activity?.finish()
                }
                setChild(contentView)
                setOnDismissListener {
                    dismiss()
                    activity?.finish()
                }
            }
            bottomSheetUnify.show(supportFragmentManager, "")
        }catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }

    private fun gotoLinkAccount() {
        RouteManager.route(this, ApplinkConst.LINK_ACCOUNT)
    }
}