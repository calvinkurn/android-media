package com.tokopedia.creation.common.consts

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

/**
 * Created By : Muhammad Furqan on 15/09/23
 */
object ContentCreationConsts {
    const val TITLE = "title"
    const val MAX_MULTI_SELECT_ALLOWED = "max_multi_select"
    const val APPLINK_AFTER_CAMERA_CAPTURE = "link_cam"
    const val APPLINK_FOR_GALLERY_PROCEED = "link_gall"
    const val IS_CREATE_POST_AS_BUYER = "is_create_post_as_buyer"

    const val KEY_IS_OPEN_FROM = "key_is_open_from"
    const val VALUE_IS_OPEN_FROM_USER_PROFILE = "is_open_from_user_profile"
    const val VALUE_IS_OPEN_FROM_SHOP_PAGE = "is_open_from_shop_page"

    const val VALUE_MAX_MULTI_SELECT_ALLOWED = 5

    fun getPostIntent(
        context: Context?,
        asABuyer: Boolean,
        title: String,
        sourcePage: String
    ): Intent = RouteManager.getIntent(
        context,
        ApplinkConst.IMAGE_PICKER_V2
    ).apply {
        putExtra(IS_CREATE_POST_AS_BUYER, asABuyer)
        putExtra(APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
        putExtra(MAX_MULTI_SELECT_ALLOWED, VALUE_MAX_MULTI_SELECT_ALLOWED)
        putExtra(TITLE, title)
        putExtra(APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
        if (sourcePage.isNotEmpty()) putExtra(KEY_IS_OPEN_FROM, sourcePage)
    }

}
