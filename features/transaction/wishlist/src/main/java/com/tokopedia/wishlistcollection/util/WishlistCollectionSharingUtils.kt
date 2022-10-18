package com.tokopedia.wishlistcollection.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.universal_sharing.model.WishlistCollectionParamModel
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionSharingDataResponse
import java.io.File

class WishlistCollectionSharingUtils() {
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

    companion object {
        private const val WISHLIST_COLLECTION = "WISHLIST_COLLECTION"
        private const val PRODUCT_COUNT_2 = 2
        private const val PRODUCT_COUNT_3 = 3
        private const val PRODUCT_COUNT_4 = 4
        private const val PRODUCT_COUNT_5 = 5
        private const val PRODUCT_COUNT_6 = 6
    }
}
