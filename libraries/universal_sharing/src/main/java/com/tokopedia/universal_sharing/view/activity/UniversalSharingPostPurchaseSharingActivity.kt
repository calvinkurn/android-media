package com.tokopedia.universal_sharing.view.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication.PAGE_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalShare
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker.Companion.TYPE_GENERAL
import com.tokopedia.universal_sharing.util.CurrencyUtil.toRupiahFormat
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UniversalSharingPostPurchaseSharingActivity :
    BaseActivity(),
    UniversalSharingPostPurchaseBottomSheetListener {

    @Inject
    lateinit var analytics: UniversalSharebottomSheetTracker

    @Inject
    lateinit var userSession: UserSessionInterface

    private var bottomSheet: UniversalSharingPostPurchaseBottomSheet? = null
    private var source: String = ""
    private var pageType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setupBottomSheet()
        showBottomSheet()
    }

    private fun initInjector() {
        ActivityComponentFactory.instance.createActivityComponent(
            applicationContext as Application
        ).inject(this)
    }

    @SuppressLint("DeprecatedMethod")
    private fun setupBottomSheet() {
        source = intent.getStringExtra(ApplinkConstInternalCommunication.SOURCE) ?: ""
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ApplinkConstInternalCommunication.PRODUCT_LIST_DATA,
                UniversalSharingPostPurchaseModel::class.java
            ) ?: UniversalSharingPostPurchaseModel()
        } else {
            intent.getParcelableExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA)
                ?: UniversalSharingPostPurchaseModel()
        }
        pageType = intent.getStringExtra(PAGE_TYPE).orEmpty()

        bottomSheet = UniversalSharingPostPurchaseBottomSheet.newInstance(data)
        bottomSheet?.setListener(this)
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        if (bottomSheet?.isAdded == false) { // only 1 bottom sheet allowed
            bottomSheet?.show(
                supportFragmentManager,
                ::UniversalSharingPostPurchaseSharingActivity.name
            )
        }
    }

    override fun onOpenShareBottomSheet(
        orderId: String,
        shopName: String,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        if (isUsingShareEx()) {
            openShareEx(product, orderId)
        } else {
            openUniversalShareBottomSheet(orderId, shopName, product)
        }
    }

    private fun openShareEx(
        product: UniversalSharingPostPurchaseProductResponse,
        orderId: String
    ) {
        currentFocus?.hideKeyboard()
        val intent = RouteManager.getIntent(this, getShareExApplink())
        intent.putExtra(ApplinkConstInternalShare.Param.DEFAULT_URL, product.url)
        intent.putExtra(ApplinkConstInternalShare.Param.PRODUCT_ID, product.productId)
        intent.putExtra(ApplinkConstInternalShare.Param.UTM_CAMPAIGN, "Thankyou - {share_id} - $orderId - $pageType")
        intent.putExtra(ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_AFFILIATE_REGISTRATION, "{share_id} - ${product.productId} - $orderId - $pageType")
        intent.putExtra(ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_CLOSE_ICON, "{share_id} - ${product.productId} - $orderId - $pageType")
        intent.putExtra(ApplinkConstInternalShare.Param.LABEL_ACTION_CLICK_CHANNEL, "{channel} - {share_id} - ${product.productId} - $orderId - $pageType")
        intent.putExtra(ApplinkConstInternalShare.Param.LABEL_IMPRESSION_BOTTOMSHEET, "{share_id} - ${product.productId} - $orderId - $pageType")
        startActivityForResult(intent, REQUEST_SHARE_EXPERIENCE)
    }

    private fun getShareExApplink(): String {
        return "${ApplinkConst.ShareExperience.SHARE_EXPERIENCE}?${ApplinkConstInternalShare.Param.PAGE_TYPE}=$PAGE_TYPE_THANK_YOU"
    }

    private fun openUniversalShareBottomSheet(
        orderId: String,
        shopName: String,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        val bottomSheetShare = UniversalShareBottomSheet.createInstance(
            dismissAfterShare = false
        )
        configShareBottomSheet(
            orderId = orderId,
            shopName = shopName,
            bottomSheet = bottomSheetShare,
            product = product
        )
        if (!bottomSheetShare.isAdded) { // only 1 bottomsheet allowed
            bottomSheetShare.show(
                supportFragmentManager,
                ::UniversalSharingPostPurchaseSharingActivity.name
            )
            analytics.onViewSharingChannelBottomSheetSharePostPurchase(
                userShareType = TYPE_GENERAL,
                productId = product.productId,
                orderId = orderId.toIntOrZero().toString() // If the order ID contains any characters/strings, they should be replaced with 0
            )
        }
    }

    private fun configShareBottomSheet(
        orderId: String,
        shopName: String,
        bottomSheet: UniversalShareBottomSheet,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        val desc = getString(
            R.string.universal_sharing_post_purchase_og_desc,
            product.productPrice.toRupiahFormat()
        )
        bottomSheet.apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    analytics.onClickSharingChannelBottomSheetSharePostPurchase(
                        channel = shareModel.channel ?: "",
                        userShareType = TYPE_GENERAL,
                        productId = product.productId,
                        orderId = orderId.toIntOrZero().toString() // If the order ID contains any characters/strings, they should be replaced with 0
                    )
                }

                override fun onCloseOptionClicked() {
                    analytics.onClickCloseBottomSheetSharePostPurchase(
                        userShareType = TYPE_GENERAL,
                        productId = product.productId,
                        orderId = orderId.toIntOrZero().toString() // If the order ID contains any characters/strings, they should be replaced with 0
                    )
                }
            })
            setMetaData(
                tnTitle = product.productName,
                tnImage = product.images.firstOrNull()?.imageUrl ?: "",
                previewImgUrl = "",
                imageList = product.getImageList()
            )
            enableDefaultShareIntent()
            setShareText("$desc %s")
            setSubject(product.productName)
            setLinkProperties(
                LinkProperties(
                    linkerType = LinkerData.PRODUCT_TYPE,
                    id = product.productId,
                    ogTitle = "${product.productName} - ${product.productPrice.toRupiahFormat()}",
                    ogDescription = "$shopName - ${product.desc}",
                    ogImageUrl = "${product.images.firstOrNull()?.imageUrl}",
                    deeplink = generateApplinkPDP(product.productId),
                    desktopUrl = product.url
                )
            )
            setUtmCampaignData(
                pageName = source,
                userId = userSession.userId,
                pageId = "${product.productId}-$orderId", // Product Id & Order Id
                feature = "share"
            )
            setOnDismissListener {
                finish()
            }
            setCloseClickListener {
                finish()
            }
        }
    }

    override fun onDismiss(shouldClosePage: Boolean) {
        if (shouldClosePage) {
            finish()
        }
    }

    override fun onClickClose() {
        finish()
    }

    private fun generateApplinkPDP(productId: String): String {
        return UriUtil.buildUri(ApplinkConst.PRODUCT_INFO, productId)
    }

    private fun isUsingShareEx(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            ROLLENCE_SHARE_EX_TY_PAGE,
            ""
        ) == ROLLENCE_SHARE_EX_TY_PAGE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SHARE_EXPERIENCE) {
            forwardIntentResult(resultCode, data)
            finish()
        }
    }

    private fun forwardIntentResult(resultCode: Int, intent: Intent?) {
        val message = intent?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_SUCCESS_COPY_LINK).orEmpty()
        val errorMessage = intent?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_FAIL_GENERATE_AFFILIATE_LINK).orEmpty()
        val action = intent?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_CTA_COPY_LINK).orEmpty()
        val shortLink = intent?.getStringExtra(ApplinkConstInternalShare.ActivityResult.PARAM_FALLBACK_SHORT_LINK).orEmpty()

        val intentResult = Intent()
        intentResult.putExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_SUCCESS_COPY_LINK, message)
        intentResult.putExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_MESSAGE_FAIL_GENERATE_AFFILIATE_LINK, errorMessage)
        intentResult.putExtra(ApplinkConstInternalShare.ActivityResult.PARAM_TOASTER_CTA_COPY_LINK, action)
        intentResult.putExtra(ApplinkConstInternalShare.ActivityResult.PARAM_FALLBACK_SHORT_LINK, shortLink)
        setResult(resultCode, intentResult)
        finish()
    }

    companion object {
        private const val PAGE_TYPE_THANK_YOU = 8
        private const val REQUEST_SHARE_EXPERIENCE = 101
        private const val ROLLENCE_SHARE_EX_TY_PAGE = "shareex_an_typv1"
    }
}
