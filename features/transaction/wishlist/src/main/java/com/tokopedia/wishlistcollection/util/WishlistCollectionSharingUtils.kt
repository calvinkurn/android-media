package com.tokopedia.wishlistcollection.util

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.model.WishlistCollectionParamModel
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionSharingDataResponse
import java.io.File

class WishlistCollectionSharingUtils {
    private var collectionShareBottomSheet: UniversalShareBottomSheet? = null

    fun showUniversalShareWithMediaBottomSheet(
        activity: FragmentActivity,
        data: GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData.Data,
        paramImageGenerator: WishlistCollectionParamModel,
        userId: String,
        view: View,
        childFragmentManager: FragmentManager,
        fragment: Fragment
    ) {
        val shareListener = object : ShareBottomsheetListener {
            override fun onShareOptionClicked(shareModel: ShareModel) {
                val linkerShareResult = DataMapper.getLinkerShareData(
                    LinkerData().apply {
                        type = LinkerData.WISHLIST_COLLECTION_TYPE
                        uri = data.shareLink.redirectionUrl
                        id = data.collection.id.toString()
                        feature = shareModel.feature
                        channel = shareModel.channel
                        campaign = shareModel.campaign
                        ogTitle = "Koleksi ${data.collection.name}"
                        ogDescription = data.collection.owner.name
                        if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                            ogImageUrl = shareModel.ogImgUrl
                        }
                        isAffiliate = shareModel.isAffiliate
                        linkAffiliateType = WISHLIST
                    }
                )

                LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(
                        0,
                        linkerShareResult,
                        object : ShareCallback {
                            override fun urlCreated(linkerShareResult: LinkerShareResult?) {
                                val shareString = activity.getString(R.string.sharing_collection_desc, data.collection.name) + " ${linkerShareResult?.url}"
                                shareModel.subjectName = data.collection.owner.name
                                SharingUtil.executeShareIntent(
                                    shareModel,
                                    linkerShareResult,
                                    activity,
                                    view,
                                    shareString
                                )
                                collectionShareBottomSheet?.dismiss()
                                WishlistCollectionAnalytics.sendClickSharingChannelCollectionEvent(shareModel.socialMediaName ?: shareModel.platform, data.collection.id, userId)
                            }

                            override fun onError(linkerError: LinkerError?) {
                                activity.let {
                                    WishlistCollectionSharingUtils().openIntentShareDefaultUniversalSharing(
                                        file = null,
                                        shareProductName = data.collection.name,
                                        shareDescription = data.collection.owner.name,
                                        shareUrl = data.shareLink.redirectionUrl,
                                        context = it
                                    )
                                }
                            }
                        }
                    )
                )
            }

            override fun onCloseOptionClicked() {
                WishlistCollectionAnalytics.sendClickCloseShareButtonCollectionEvent(data.collection.id, userId)
            }
        }
        collectionShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            getImageFromMedia(true)
            setMediaPageSourceId(ImageGeneratorConstants.ImageGeneratorSourceId.WISHLIST_COLLECTION)
            setImageGeneratorParam(paramImageGenerator)
            init(shareListener)
            setUtmCampaignData("WISHLIST_COLLECTION", userId, data.collection.id.toString(), "share")
            val imgUrl = if (data.items.isNotEmpty()) data.items[0].imageUrl else data.emptyWishlistImageUrl
            setMetaData(
                tnTitle = data.collection.name,
                tnImage = imgUrl
            )
//            enableAffiliateCommission(createShareInput(data.collection.id.toString()))
        }
        collectionShareBottomSheet?.show(childFragmentManager, fragment)
        WishlistCollectionAnalytics.sendViewOnSharingChannelCollectionEvent(data.collection.id, userId)
    }

    fun mapParamImageGenerator(data: GetWishlistCollectionSharingDataResponse.GetWishlistCollectionSharingData.Data): WishlistCollectionParamModel {
        return WishlistCollectionParamModel(
            collectionName = data.collection.name,
            collectionOwner = data.collection.owner.name,
            productCount = data.totalItem,
            productImage1 = if (data.items.isNotEmpty()) data.items[0].imageUrl else "",
            productPrice1 = if (data.items.isNotEmpty()) data.items[0].price else 0L,
            productImage2 = if (data.items.size >= PRODUCT_COUNT_2) data.items[1].imageUrl else "",
            productPrice2 = if (data.items.size >= PRODUCT_COUNT_2) data.items[1].price else 0L,
            productImage3 = if (data.items.size >= PRODUCT_COUNT_3) data.items[2].imageUrl else "",
            productPrice3 = if (data.items.size >= PRODUCT_COUNT_3) data.items[2].price else 0L,
            productImage4 = if (data.items.size >= PRODUCT_COUNT_4) data.items[3].imageUrl else "",
            productPrice4 = if (data.items.size >= PRODUCT_COUNT_4) data.items[3].price else 0L,
            productImage5 = if (data.items.size >= PRODUCT_COUNT_5) data.items[4].imageUrl else "",
            productPrice5 = if (data.items.size >= PRODUCT_COUNT_5) data.items[4].price else 0L,
            productImage6 = if (data.items.size >= PRODUCT_COUNT_6) data.items[5].imageUrl else "",
            productPrice6 = if (data.items.size >= PRODUCT_COUNT_6) data.items[5].price else 0L
        )
    }

    fun openIntentShareDefaultUniversalSharing(
        file: File?,
        shareProductName: String = "",
        shareDescription: String = "",
        shareUrl: String = "",
        context: Context
    ) {
        openIntentShare(file, shareProductName, shareDescription, shareUrl, context)
    }

    private fun openIntentShare(
        file: File?,
        title: String?,
        shareContent: String,
        shareUri: String,
        context: Context
    ) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = if (file == null) "text/plain" else "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (file != null) {
                putExtra(Intent.EXTRA_STREAM, MethodChecker.getUri(context, file))
            }
            putExtra(Intent.EXTRA_REFERRER, shareUri)
            putExtra(Intent.EXTRA_HTML_TEXT, shareContent)
            putExtra(Intent.EXTRA_TITLE, title)
            putExtra(Intent.EXTRA_TEXT, shareContent)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        context.startActivity(Intent.createChooser(shareIntent, title))
    }

    private fun createShareInput(
        collectionId: String
    ): AffiliateInput {
        val pageDetail = PageDetail(
            pageId = collectionId,
            pageType = PageType.WISHLIST.value,
            siteId = SITE_ID,
            verticalId = VERTICAL_ID
        )

        return AffiliateInput(
            pageDetail = pageDetail,
            pageType = PageType.WISHLIST.value,
            product = Product(
                productID = "0"
            ),
            shop = Shop(
                shopID = "0"
            )
        )
    }

    companion object {
        private const val WISHLIST = "wishlist"
        private const val PRODUCT_COUNT_2 = 2
        private const val PRODUCT_COUNT_3 = 3
        private const val PRODUCT_COUNT_4 = 4
        private const val PRODUCT_COUNT_5 = 5
        private const val PRODUCT_COUNT_6 = 6

        private const val SITE_ID = "1"
        private const val VERTICAL_ID = "1"
    }
}
