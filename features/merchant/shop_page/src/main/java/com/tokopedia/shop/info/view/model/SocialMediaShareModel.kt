package com.tokopedia.shop.info.view.model

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * @author by Rafli Syam on 2020-07-10
 */

sealed class SocialMediaShareModel {

    abstract var socialMediaIcon: Drawable?
    abstract var socialMediaName: String?
    abstract var packageName: String?
    abstract var appIntent: Intent?

    data class CopyLink(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Instagram(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Facebook(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Whatsapp(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Line(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Twitter(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Telegram(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

    data class Others(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : SocialMediaShareModel()

}