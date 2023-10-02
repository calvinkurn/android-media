package com.tokopedia.universal_sharing.view.activity

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.linker.model.LinkerData
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
import javax.inject.Inject

class UniversalSharingPostPurchaseSharingActivity :
    BaseActivity(),
    UniversalSharingPostPurchaseBottomSheetListener {

    @Inject
    lateinit var analytics: UniversalSharebottomSheetTracker

    private var bottomSheet: UniversalSharingPostPurchaseBottomSheet? = null

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
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ApplinkConstInternalCommunication.PRODUCT_LIST_DATA,
                UniversalSharingPostPurchaseModel::class.java
            ) ?: UniversalSharingPostPurchaseModel()
        } else {
            intent.getParcelableExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA)
                ?: UniversalSharingPostPurchaseModel()
        }

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
                orderId = orderId
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
                        orderId = orderId
                    )
                }

                override fun onCloseOptionClicked() {
                    analytics.onClickCloseBottomSheetSharePostPurchase(
                        userShareType = TYPE_GENERAL,
                        productId = product.productId,
                        orderId = orderId
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
        return "tokopedia://product/$productId"
    }
}
