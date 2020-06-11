package com.tokopedia.seller_migration_common.presentation.widget

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_seller_migration_account_bottom_sheet.*

class SellerMigrationAccountBottomSheet : BottomSheetUnify() {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationAccountBottomSheet {
            return SellerMigrationAccountBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_account_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPadding()
        sellerMigrationAccountBottomSheetImage.loadImage(SellerMigrationConstants.SELLER_MIGRATION_ACCOUNT_IMAGE_LINK)
        sellerMigrationBottomSheetContent.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_account_home_bottom_sheet_content)).spannedString }
        sellerMigrationAccountBottomSheetLink.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_account_home_bottom_sheet_footer)).spannedString }
        sellerMigrationAccountBottomSheetLink.setOnTouchListener(SellerMigrationTouchListener {
            goToInformationWebview(it)
        })
        sellerMigrationAccountBottomSheetButton.setOnClickListener {
            goToSellerApp()
        }
        setupWarningCard()
    }

    private fun setupWarningCard() {
        val remoteConfigDate = getSellerMigrationDate(context)
        if(remoteConfigDate.isNotBlank()) {
            val sellerMigrationWarningDate: Typography? = view?.findViewById(R.id.sellerMigrationWarningDate)
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
                } else {
                    activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)))
                }
            } catch (anfe: ActivityNotFoundException) {
                activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)))
            }
        }
    }

    private fun goToInformationWebview(link: String) : Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }
}