package com.tokopedia.seller_migration_common.presentation.widget

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.KEY_SHOULD_DISMISS_AFTER_RESTORE
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSession
import com.tokopedia.unifyprinciples.Typography

class SellerMigrationAccountBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationAccountBottomSheet {
            return SellerMigrationAccountBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_account_bottom_sheet,null)
                setChild(view)
            }
        }
    }
    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = UserSession(context).userId
        setupPadding()
        val sellerMigrationAccountBottomSheetImage: ImageView? = view.findViewById(R.id.sellerMigrationAccountBottomSheetImage)
        val sellerMigrationBottomSheetContent: Typography? = view.findViewById(R.id.sellerMigrationBottomSheetContent)
        val sellerMigrationAccountBottomSheetLink: Typography? = view.findViewById(R.id.sellerMigrationAccountBottomSheetLink)
        val sellerMigrationAccountBottomSheetButton: UnifyButton? = view.findViewById(R.id.sellerMigrationAccountBottomSheetButton)
        if(listOf(sellerMigrationAccountBottomSheetImage, sellerMigrationBottomSheetContent, sellerMigrationAccountBottomSheetLink, sellerMigrationAccountBottomSheetButton).any { it == null }) {
            this.dismiss()
            return
        }
        sellerMigrationAccountBottomSheetImage?.loadImage(SellerMigrationConstants.SELLER_MIGRATION_ACCOUNT_IMAGE_LINK)
        sellerMigrationBottomSheetContent?.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_account_home_bottom_sheet_content)).spannedString }
        sellerMigrationAccountBottomSheetLink?.apply {
            text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_account_home_bottom_sheet_footer)).spannedString }
            setOnTouchListener(SellerMigrationTouchListener {
                goToInformationWebview(it)
            })
        }
        sellerMigrationAccountBottomSheetButton?.setOnClickListener {
            goToSellerApp()
        }
        setupWarningCard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SHOULD_DISMISS_AFTER_RESTORE, true)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState?.getBoolean(KEY_SHOULD_DISMISS_AFTER_RESTORE, false) == true) {
            dismissOnRestore()
        }
    }

    private fun dismissOnRestore() {
        dismiss()
        parentFragment?.childFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun setupWarningCard() {
        val remoteConfigDate = getSellerMigrationDate(context)
        if(remoteConfigDate.isNotBlank()) {
            val sellerMigrationWarningDate: Typography? = view?.findViewById(R.id.sellerMigrationWarningDate)
            val sellerMigrationAccountWarning: View? = view?.findViewById(R.id.sellerMigrationAccountWarning)
            sellerMigrationAccountWarning?.show()
            sellerMigrationWarningDate?.text = remoteConfigDate
        }
    }

    private fun setupPadding() {
        setShowListener {
            val headerMargin = 16.toPx()
            bottomSheetWrapper.setPadding(0,0,0,0)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(headerMargin,headerMargin,headerMargin,headerMargin)
        }
    }

    private fun goToSellerApp() {
        with(SellerMigrationConstants) {
            try {
                val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
                if(intent != null) {
                    intent.putExtra(SELLER_MIGRATION_KEY_AUTO_LOGIN, true)
                    activity?.startActivity(intent)
                    trackGoToSellerApp()
                } else {
                    activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
                    trackGoToPlayStore()
                }
            } catch (anfe: ActivityNotFoundException) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
                trackGoToPlayStore()
            }
        }
    }

    private fun trackGoToSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

    private fun trackGoToPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_ACCOUNT)
    }

    private fun goToInformationWebview(link: String) : Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }
}