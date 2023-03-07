package com.tokopedia.shop.common.view.model

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * @author by Rafli Syam on 2020-07-10
 */

sealed class ShopShareModel {

    abstract var socialMediaIcon: Drawable?
    abstract var socialMediaName: String?
    abstract var packageName: String?
    abstract var appIntent: Intent?

    data class CopyLink(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Instagram(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Facebook(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Whatsapp(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Line(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Twitter(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Telegram(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()

    data class Others(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShopShareModel()
}
