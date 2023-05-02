package com.tokopedia.wishlistcommon.util

import android.content.Context
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION_DETAIL_INTERNAL
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.wishlist_common.R
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OPEN_WISHLIST

object AddRemoveWishlistV2Handler {
    fun showAddToWishlistV2SuccessToaster(
        result: AddToWishlistV2Response.Data.WishlistAddV2,
        context: Context,
        view: View
    ) {
        val msg = result.message.ifEmpty {
            if (result.success) {
                context.getString(R.string.on_success_add_to_wishlist_msg)
            } else {
                context.getString(R.string.on_failed_add_to_wishlist_msg)
            }
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColor == WishlistV2CommonConsts.TOASTER_RED || !result.success) typeToaster = Toaster.TYPE_ERROR

        val ctaText = result.button.text.ifEmpty {
            context.getString(R.string.cta_success_add_to_wishlist)
        }

        Toaster.build(
            view,
            msg,
            Toaster.LENGTH_SHORT,
            typeToaster,
            actionText = ctaText
        ) {
            if (result.button.action == OPEN_WISHLIST) {
                goToWishlistCollectionDetailAll(context)
            }
        }.show()
    }

    fun showAddToWishlistV2SuccessToaster(
        result: ProductCardOptionsModel.WishlistResult,
        context: Context,
        view: View
    ) {
        val msg = result.messageV2.ifEmpty {
            if (result.isSuccess) {
                context.getString(R.string.on_success_add_to_wishlist_msg)
            } else {
                context.getString(R.string.on_failed_add_to_wishlist_msg)
            }
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColorV2 == WishlistV2CommonConsts.TOASTER_RED || !result.isSuccess) typeToaster = Toaster.TYPE_ERROR

        val ctaText = result.ctaTextV2.ifEmpty {
            context.getString(R.string.cta_ok_wishlist)
        }

        Toaster.build(
            view,
            msg,
            Toaster.LENGTH_SHORT,
            typeToaster,
            actionText = ctaText
        ) {
            if (result.ctaActionV2 == OPEN_WISHLIST) {
                goToWishlistCollectionDetailAll(context)
            }
        }.show()
    }

    fun showRemoveWishlistV2SuccessToaster(
        result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
        context: Context,
        view: View
    ) {
        val msg = result.message.ifEmpty {
            if (result.success) {
                context.getString(R.string.on_success_remove_from_wishlist_msg)
            } else {
                context.getString(R.string.on_failed_remove_from_wishlist_msg)
            }
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColor == WishlistV2CommonConsts.TOASTER_RED || !result.success) typeToaster = Toaster.TYPE_ERROR

        val ctaText = result.button.text.ifEmpty {
            context.getString(R.string.cta_ok_wishlist)
        }

        Toaster.build(
            view,
            msg,
            Toaster.LENGTH_SHORT,
            typeToaster,
            actionText = ctaText
        ) {
            if (result.button.action == OPEN_WISHLIST) {
                goToWishlistCollectionDetailAll(context)
            }
        }.show()
    }

    fun showRemoveWishlistV2SuccessToaster(
        result: ProductCardOptionsModel.WishlistResult,
        context: Context,
        view: View
    ) {
        val msg = result.messageV2.ifEmpty {
            if (result.isSuccess) {
                context.getString(R.string.on_success_remove_from_wishlist_msg)
            } else {
                context.getString(R.string.on_failed_remove_from_wishlist_msg)
            }
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColorV2 == WishlistV2CommonConsts.TOASTER_RED || !result.isSuccess) typeToaster = Toaster.TYPE_ERROR

        val ctaText = result.ctaTextV2.ifEmpty {
            context.getString(R.string.cta_ok_wishlist)
        }

        Toaster.build(
            view,
            msg,
            Toaster.LENGTH_SHORT,
            typeToaster,
            actionText = ctaText
        ) {
            if (result.ctaActionV2 == OPEN_WISHLIST) {
                goToWishlistCollectionDetailAll(context)
            }
        }.show()
    }

    fun showWishlistV2ErrorToaster(errorMsg: String, view: View) {
        Toaster.build(view, errorMsg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    fun showWishlistV2ErrorToasterWithCta(errorMsg: String, ctaText: String, ctaAction: String, view: View, context: Context) {
        Toaster.build(view, errorMsg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, ctaText) {
            if (ctaAction == OPEN_WISHLIST) {
                goToWishlistCollectionDetailAll(context)
            }
        }.show()
    }

    private fun goToWishlistPage(context: Context) {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    private fun goToWishlistCollectionPage(context: Context) {
        RouteManager.route(context, ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION)
    }

    private fun goToWishlistCollectionDetailAll(context: Context) {
        RouteManager.route(context, WISHLIST_COLLECTION_DETAIL_INTERNAL, "0")
    }
}
