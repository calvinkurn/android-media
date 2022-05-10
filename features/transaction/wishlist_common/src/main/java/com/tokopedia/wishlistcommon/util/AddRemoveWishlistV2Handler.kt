package com.tokopedia.wishlistcommon.util

import android.content.Context
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.wishlist_common.R
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.OPEN_WISHLIST

object AddRemoveWishlistV2Handler {
    fun showAddToWishlistV2SuccessToaster(result: AddToWishlistV2Response.Data.WishlistAddV2,
                                          context: Context, view: View) {
        val msg = result.message.ifEmpty {
            if (result.success) context.getString(R.string.on_success_add_to_wishlist_msg)
            else context.getString(R.string.on_failed_add_to_wishlist_msg)
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColor == WishlistV2CommonConsts.TOASTER_RED || !result.success) typeToaster = Toaster.TYPE_ERROR

        var ctaText = context.getString(R.string.cta_success_add_to_wishlist)
        if (result.button.text.isNotEmpty()) ctaText = result.button.text

        Toaster.build(view, msg, Toaster.LENGTH_SHORT, typeToaster,
            actionText = ctaText
        ) { if (result.button.action == OPEN_WISHLIST) goToWishlistPage(context) }.show()
    }

    fun showAddToWishlistV2SuccessToaster(result: ProductCardOptionsModel.WishlistResult,
                                          context: Context, view: View) {
        val msg = result.messageV2.ifEmpty {
            if (result.isSuccess) context.getString(R.string.on_success_add_to_wishlist_msg)
            else context.getString(R.string.on_failed_add_to_wishlist_msg)
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColorV2 == WishlistV2CommonConsts.TOASTER_RED || !result.isSuccess) typeToaster = Toaster.TYPE_ERROR

        var ctaText = context.getString(R.string.cta_success_add_to_wishlist)
        if (result.ctaTextV2.isNotEmpty()) ctaText = result.ctaTextV2

        Toaster.build(view, msg, Toaster.LENGTH_SHORT, typeToaster,
            actionText = ctaText
        ) { if (result.ctaActionV2 == OPEN_WISHLIST) goToWishlistPage(context) }.show()
    }

    fun showRemoveWishlistV2SuccessToaster(result: DeleteWishlistV2Response.Data.WishlistRemoveV2,
                                           context: Context, view: View) {
        val msg = result.message.ifEmpty {
            if (result.success) context.getString(R.string.on_success_add_to_wishlist_msg)
            else context.getString(R.string.on_failed_add_to_wishlist_msg)
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColor == WishlistV2CommonConsts.TOASTER_RED || !result.success) typeToaster = Toaster.TYPE_ERROR

        var ctaText = context.getString(R.string.cta_success_add_to_wishlist)
        if (result.button.text.isNotEmpty()) ctaText = result.button.text

        Toaster.build(view, msg, Toaster.LENGTH_SHORT, typeToaster,
            actionText = ctaText
        ) { goToWishlistPage(context) }.show()
    }

    fun showRemoveWishlistV2SuccessToaster(result: ProductCardOptionsModel.WishlistResult,
                                           context: Context, view: View) {
        val msg = result.messageV2.ifEmpty {
            if (result.isSuccess) context.getString(R.string.on_success_add_to_wishlist_msg)
            else context.getString(R.string.on_failed_add_to_wishlist_msg)
        }

        var typeToaster = Toaster.TYPE_NORMAL
        if (result.toasterColorV2 == WishlistV2CommonConsts.TOASTER_RED || !result.isSuccess) typeToaster = Toaster.TYPE_ERROR

        var ctaText = context.getString(R.string.cta_success_add_to_wishlist)
        if (result.ctaTextV2.isNotEmpty()) ctaText = result.ctaTextV2

        Toaster.build(view, msg, Toaster.LENGTH_SHORT, typeToaster,
            actionText = ctaText
        ) { if (result.ctaActionV2 == OPEN_WISHLIST) goToWishlistPage(context) }.show()
    }

    fun showWishlistV2ErrorToaster(errorMsg: String, view: View) {
        Toaster.build(view, errorMsg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun goToWishlistPage(context: Context) {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }
}