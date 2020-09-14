package com.tokopedia.seller_migration_common.presentation.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.fragment.bottomsheet.SellerMigrationCommunicationBottomSheet
import com.tokopedia.seller_migration_common.presentation.model.CommunicationInfo
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.parcel.Parcelize

@SuppressLint("ClickableViewAccessibility")
internal fun Fragment.setupMigrationFooter(view: View?,
                                           trackGoToSellerApp: () -> Unit = {},
                                           trackGoToPlayStore: () -> Unit = {},
                                           trackLearnMore: () -> Unit = {},
                                           goToSellerAppFeature: () -> Unit = { goToSellerApp(trackGoToSellerApp, trackGoToPlayStore) }) {
    val sellerMigrationBottomSheetButton: UnifyButton? = view?.findViewById(R.id.sellerMigrationBottomSheetButton)
    sellerMigrationBottomSheetButton?.setOnClickListener {
        goToSellerAppFeature()
    }
    val sellerMigrationBottomSheetLink: Typography? = view?.findViewById(R.id.sellerMigrationBottomSheetLink)
    sellerMigrationBottomSheetLink?.text = context?.let { HtmlLinkHelper(it, getString(R.string.seller_migration_bottom_sheet_footer)).spannedString }
    sellerMigrationBottomSheetLink?.setOnTouchListener(SellerMigrationTouchListener {
        goToInformationWebview(it, trackLearnMore)
    })
}

private fun Fragment.goToSellerApp(trackGoToSellerApp: () -> Unit,
                                   trackGoToPlayStore: () -> Unit) {
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

private fun Fragment.goToInformationWebview(link: String,
                                            trackLearnMore: () -> Unit): Boolean {
    trackLearnMore()
    return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
}

@Parcelize
data class BenefitPoints(val benefitPointsList: List<CharSequence>): Parcelable

/**
 * Fragment extension function to initialize phase 3 redirection ticker, which will show bottomsheet if spanned string is clicked
 *
 * @param   bottomSheet         SellerMigrationCommunicationBottomSheet to be displayed
 * @param   ticker              ticker view that should display migration info
 * @param   communicationInfo   type of migration communication (broadcast chat, topads, etc.)
 */
fun Fragment.initializeSellerMigrationCommunicationTicker(bottomSheet: SellerMigrationCommunicationBottomSheet?,
                                                          ticker: Ticker?,
                                                          communicationInfo: CommunicationInfo,
                                                          tickerAction: () -> Unit = { ticker?.show() }) {
    ticker?.run {
        if (isSellerMigrationEnabled(context)) {
            tickerTitle = context?.getString(R.string.seller_migration_ticker_title).orEmpty()
            val remoteConfigDate = getSellerMigrationDate(context).let { date ->
                if (date.isEmpty()) {
                    getString(R.string.seller_migration_bottom_sheet_redirected_dates_abbv)
                } else {
                    date
                }
            }
            val featureString = context?.getString(communicationInfo.tickerMessagePrefixRes).orEmpty()
            setHtmlDescription(context?.getString(R.string.seller_migration_ticker_desc, featureString, remoteConfigDate).orEmpty())
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDismiss() {}
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    openSellerMigrationBottomSheet(bottomSheet)
                }
            })
            tickerAction()
        } else {
            gone()
        }
    }
}

private fun Fragment.openSellerMigrationBottomSheet(bottomSheet: SellerMigrationCommunicationBottomSheet?) {
    bottomSheet?.show(childFragmentManager, SellerMigrationCommunicationBottomSheet::class.java.name)
}

/**
 * FragmentActivity extension function to initialize phase 3 redirection ticker, which will show bottomsheet if spanned string is clicked
 *
 * @param   bottomSheet         SellerMigrationCommunicationBottomSheet to be displayed
 * @param   ticker              ticker view that should display migration info
 * @param   communicationInfo   type of migration communication (broadcast chat, topads, etc.)
 */
fun FragmentActivity.initializeSellerMigrationCommunicationTicker(bottomSheet: SellerMigrationCommunicationBottomSheet?,
                                                                  ticker: Ticker?,
                                                                  communicationInfo: CommunicationInfo,
                                                                  tickerAction: () -> Unit = { ticker?.show() }) {
    ticker?.run {
        if (isSellerMigrationEnabled(context)) {
            tickerTitle = context?.getString(R.string.seller_migration_ticker_title).orEmpty()
            val remoteConfigDate = getSellerMigrationDate(context).let { date ->
                if (date.isEmpty()) {
                    getString(R.string.seller_migration_bottom_sheet_redirected_dates_abbv)
                } else {
                    date
                }
            }
            val featureString = context?.getString(communicationInfo.tickerMessagePrefixRes).orEmpty()
            setHtmlDescription(context?.getString(R.string.seller_migration_ticker_desc, featureString, remoteConfigDate).orEmpty())
            setDescriptionClickEvent(object: TickerCallback {
                override fun onDismiss() {}
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    openSellerMigrationBottomSheet(bottomSheet)
                }
            })
            tickerAction()
        } else {
            gone()
        }
    }
}

private fun FragmentActivity.openSellerMigrationBottomSheet(bottomSheet: SellerMigrationCommunicationBottomSheet?) {
    bottomSheet?.show(supportFragmentManager, SellerMigrationCommunicationBottomSheet::class.java.name)
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
