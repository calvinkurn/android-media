package com.tokopedia.feedcomponent.view.share

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on October 18, 2022
 */
class FeedProductTagSharingHelper(
    private val fragmentManager: FragmentManager,
    private val fragment: Fragment,
    private val listener: Listener,
) {
    private var productTagShareModel: Model? = null
    private var shareData: LinkerData? = null

    private val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
        init(object: ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                val linkerShareData = DataMapper().getLinkerShareData(shareData)

                LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult?) {
                            val productTagShareModel = productTagShareModel
                            val shareData = shareData

                            if(productTagShareModel == null || shareData == null) return

                            val shareLink = linkerShareData?.url ?: shareData.uri
                            val shareString = getString(
                                R.string.feed_product_tag_share_template,
                                productTagShareModel.productName,
                                shareLink,
                                productTagShareModel.shopName,
                                productTagShareModel.priceFmt
                            )

                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareData,
                                activity,
                                view,
                                shareString
                            )

                            dismiss()
                        }

                        override fun onError(linkerError: LinkerError?) {
                            listener.onErrorCreatingUrl(linkerError)
                            dismiss()
                        }
                    })
                )
            }

            override fun onCloseOptionClicked() {
                /** No handling needed so far */
            }
        })
    }

    fun show(
        productTagShareModel: Model,
        shareData: LinkerData,
    ) {
        this.productTagShareModel = productTagShareModel
        this.shareData = shareData

        if(!universalShareBottomSheet.isAdded) {
            universalShareBottomSheet.setMetaData(
                tnTitle = productTagShareModel.title,
                tnImage = productTagShareModel.imageUrl,
            )
            universalShareBottomSheet.show(fragmentManager, fragment)
        }
    }

    data class Model(
        val title: String,
        val imageUrl: String,
        val productName: String,
        val shopName: String,
        val priceFmt: String,
    ) {
        companion object {
            fun map(item: ProductPostTagModelNew) = Model(
                title = item.text,
                imageUrl = item.imgUrl,
                productName = item.product.name,
                shopName = item.shopName,
                priceFmt = item.priceFmt,
            )
        }
    }

    interface Listener {
        fun onErrorCreatingUrl(linkerError: LinkerError?)
    }
}
