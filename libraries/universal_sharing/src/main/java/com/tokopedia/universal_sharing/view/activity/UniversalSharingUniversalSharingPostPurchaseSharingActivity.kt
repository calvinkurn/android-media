package com.tokopedia.universal_sharing.view.activity

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.di.UniversalSharingComponentFactory
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.tracker.UniversalSharebottomSheetTracker
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

class UniversalSharingUniversalSharingPostPurchaseSharingActivity :
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
        UniversalSharingComponentFactory.instance.createComponent(
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
                ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
            )
        }
    }

    override fun onOpenShareBottomSheet(
        orderId: String,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        val view = this.currentFocus
        val bottomSheetShare = UniversalShareBottomSheet.createInstance(view)
        configShareBottomSheet(
            orderId = orderId,
            bottomSheet = bottomSheetShare,
            product = product
        )
        if (!bottomSheetShare.isAdded) { // only 1 bottomsheet allowed
            bottomSheetShare.show(
                supportFragmentManager,
                ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
            )
            analytics.onViewSharingChannelBottomSheetSharePostPurchase(
                userShareType = "",
                productId = product.productId,
                orderId = orderId,
                orderStatus = ""
            )
        }
    }

    private fun configShareBottomSheet(
        orderId: String,
        bottomSheet: UniversalShareBottomSheet,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        bottomSheet.apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    analytics.onClickSharingChannelBottomSheetSharePostPurchase(
                        channel = shareModel.channel ?: "",
                        userShareType = "",
                        productId = product.productId,
                        orderId = orderId,
                        orderStatus = ""
                    )
                }

                override fun onCloseOptionClicked() {
                    analytics.onClickCloseBottomSheetSharePostPurchase(
                        userShareType = "",
                        productId = product.productId,
                        orderId = orderId,
                        orderStatus = ""
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
            setLinkProperties(
                LinkProperties(
                    linkerType = "", // ask rama
                    id = "", // ask rama
                    ogTitle = "${product.productName} - ${product.shop.name}", // ask astrid
                    ogDescription = "Hai ${product.productName} - ${product.shop.name}", // ask astrid
                    ogImageUrl = "${product.images.firstOrNull()?.imageUrl}", // ask astrid
                    deeplink = "tokopedia://product/{${product.productId}}",
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
}
