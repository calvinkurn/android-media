package com.tokopedia.universal_sharing.view.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseProduct
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.postpurchase.UniversalSharingPostPurchaseBottomSheetListener
import com.tokopedia.universal_sharing.view.model.LinkProperties
import com.tokopedia.universal_sharing.view.model.ShareModel

class UniversalSharingUniversalSharingPostPurchaseSharingActivity :
    BaseActivity(),
    UniversalSharingPostPurchaseBottomSheetListener {

    private var bottomSheet: UniversalSharingPostPurchaseProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
        showBottomSheet()
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

        bottomSheet = UniversalSharingPostPurchaseProduct.newInstance(data)
        bottomSheet?.setListener(this)
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        bottomSheet?.show(
            supportFragmentManager,
            ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
        )
    }

    override fun onOpenShareBottomSheet(product: UniversalSharingPostPurchaseProductResponse) {
        val view = this.currentFocus
        val shareBottomSheet = UniversalShareBottomSheet.createInstance(view)
        configShareBottomSheet(shareBottomSheet, product)
        shareBottomSheet.show(
            supportFragmentManager,
            ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
        )
    }

    private fun configShareBottomSheet(
        bottomSheet: UniversalShareBottomSheet,
        product: UniversalSharingPostPurchaseProductResponse
    ) {
        bottomSheet.apply {
            init(object : ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    // todo tracker
                }

                override fun onCloseOptionClicked() {
                    // todo tracker
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
