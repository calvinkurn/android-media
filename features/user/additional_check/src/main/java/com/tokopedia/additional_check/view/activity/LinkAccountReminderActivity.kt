package com.tokopedia.additional_check.view.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.image.ImageUtils
import javax.inject.Inject

class LinkAccountReminderActivity: BaseActivity() {

    @Inject
    lateinit var tracker: TwoFactorTracker

    private fun initComponents() {
        DaggerAdditionalCheckComponents.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .additionalCheckModules(AdditionalCheckModules())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
        showLinkAccountReminderDialog()
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tracker.viewAccountLinkingReminder()
    }

    private fun showLinkAccountReminderDialog() {
        try {
            val contentView = View.inflate(this, com.tokopedia.additional_check.R.layout.bottom_sheet_link_account_reminder, null)
            val imgView = contentView.findViewById<ImageUnify>(R.id.link_account_bs_reminder_image)
            ImageUtils.loadImageWithoutPlaceholderAndError(imgView, LINK_ACC_MAIN_IMG)
            val bottomSheetUnify = BottomSheetUnify().apply {
                val btn = contentView.findViewById<UnifyButton>(R.id.link_account_bs_reminder_primary_btn)
                btn.setOnClickListener {
                    tracker.clickAccountLinkingReminder(TwoFactorTracker.Companion.Label.LINK_ACCOUNT)
                    gotoLinkAccount()
                    dismiss()
                    activity?.finish()
                }
                setChild(contentView)
                setCloseClickListener {
                    tracker.clickAccountLinkingReminder(TwoFactorTracker.Companion.Label.CLOSE)
                    dismiss()
                }
                setOnDismissListener {
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

    companion object {
        private const val LINK_ACC_MAIN_IMG = "https://images.tokopedia.net/img/android/user/additional_check/img_link_acc_cloud.png"
    }
}