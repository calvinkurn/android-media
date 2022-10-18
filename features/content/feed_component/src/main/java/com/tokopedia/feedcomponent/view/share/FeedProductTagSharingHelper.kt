package com.tokopedia.feedcomponent.view.share

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.util.util.DataMapper
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagViewModelNew
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
) {
    private var productPost: ProductPostTagViewModelNew? = null
    private var shareData: LinkerData? = null

    private val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
        init(object: ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                val linkerShareData = DataMapper().getLinkerShareData(shareData)

                LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult?) {

                            val productPost = productPost
                            val shareData = shareData

                            if(productPost == null || shareData == null) return

                            val shareLink = linkerShareData?.shareUri.orEmpty()
                            val shareString = getString(
                                R.string.feed_product_tag_share_template,
                                productPost.product.name,
                                shareLink,
                                productPost.shopName,
                                productPost.priceFmt
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
                            /** TODO : handle this */
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
        productPost: ProductPostTagViewModelNew,
        shareData: LinkerData,
    ) {
        this.productPost = productPost
        this.shareData = shareData

        if(!universalShareBottomSheet.isAdded) {
            universalShareBottomSheet.setMetaData(
                tnTitle = productPost.text,
                tnImage = productPost.imgUrl,
            )
            universalShareBottomSheet.show(fragmentManager, fragment)
        }
    }
}
