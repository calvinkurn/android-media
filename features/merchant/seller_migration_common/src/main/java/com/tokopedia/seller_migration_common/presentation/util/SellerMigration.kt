package com.tokopedia.seller_migration_common.presentation.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.model.AccountSettingData
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

fun Fragment.goToSellerApp(trackGoToSellerApp: () -> Unit = {},
                           trackGoToPlayStore: () -> Unit = {}) {
    with(SellerMigrationConstants) {
        try {
            val intent = context?.packageManager?.getLaunchIntentForPackage(PACKAGE_SELLER_APP)
            if (intent != null) {
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

fun Fragment.goToInformationWebview(link: String,
                                    trackLearnMore: () -> Unit = {}): Boolean {
    trackLearnMore()
    return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
}

fun Fragment.initializeSellerMigrationAccountSettingTicker(ticker: Ticker?) {
    ticker?.run {
        if (isSellerMigrationEnabled(requireContext())) {
            tickerTitle = requireContext().getString(AccountSettingData.titleRes)
            setHtmlDescription(requireContext().getString(AccountSettingData.descRes))
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalSellerapp.SELLER_MENU)
                    intent.putExtra(SellerMigrationConstants.SELLER_MIGRATION_KEY_AUTO_ANCHOR_ACCOUNT_SHOP, true)
                    startActivity(intent)
                }

                override fun onDismiss() {}
            })
            show()
        } else {
            hide()
        }
    }
}

fun Typography.setOnClickLinkSpannable(htmlString: String,
                                       redirectPage: () -> Unit = {},
                                       trackGoToSellerApp: () -> Unit = {}) {
    val htmlLinkString = HtmlLinkHelper(context, htmlString)
    text = htmlLinkString.spannedString
    htmlLinkString.urlList.firstOrNull()?.setOnClickListener {
        trackGoToSellerApp()
        redirectPage()
    }
}

fun Fragment.setupBottomSheetFeedSellerMigration(goToCreateAffiliate: () -> Unit = {},
                                                 onGoToLink: Intent) {
    val viewBottomSheet = View.inflate(context, R.layout.bottom_sheet_feed_content_seller_migration, null)
    val postInSeller: LinearLayout = viewBottomSheet.findViewById(R.id.groupPostShopSeller)
    val postFavorite: Typography = viewBottomSheet.findViewById(R.id.tvPostFavorite)
    val bottomSheet = BottomSheetUnify()
    bottomSheet.setChild(viewBottomSheet)

    postFavorite.setOnClickListener {
        goToCreateAffiliate()
        bottomSheet.dismiss()
    }

    postInSeller.setOnClickListener {
        startActivity(onGoToLink)
        bottomSheet.dismiss()
    }

    bottomSheet.apply {
        showCloseIcon = true
        setCloseClickListener {
            dismiss()
        }
    }

    fragmentManager?.let {
        bottomSheet.show(it, "")
    }
}

fun getRegisteredMigrationApplinks(@SellerMigrationFeatureName featureName: String): ArrayList<String> =
    when(featureName) {
        SellerMigrationFeatureName.FEATURE_CHAT_SETTING -> {
            arrayListOf(
                ApplinkConstInternalSellerapp.SELLER_HOME_CHAT,
                ApplinkConstInternalMarketplace.CHAT_SETTING)
        }
        else -> arrayListOf()
    }