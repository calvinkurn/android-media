package com.tokopedia.universal_sharing.view.model

import android.content.Intent
import android.graphics.drawable.Drawable

sealed class ShareModel {

    abstract var socialMediaIcon: Drawable?
    abstract var socialMediaName: String?
    abstract var packageName: String?
    abstract var appIntent: Intent?
    var ogImgUrl: String? = ""
    var channel: String? = ""
    var platform: String = "wa"
    var feature: String? = ""
    var campaign: String? = ""
    var shareOnlyLink: Boolean = false
    var savedImageFilePath: String = ""
    var subjectName: String = ""
    var isAffiliate: Boolean = false
    var socialMediaOrderingScore:Int = 100
    // this variable is used for personalized campaign that has duration of campaign
    var personalizedMessageFormat: String = ""


    /* source id from imagenerator contextual image */
    var sourceId: String = ""

    data class CopyLink(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Instagram(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Facebook(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Whatsapp(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Line(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Twitter(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Telegram(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class Others(
            override var socialMediaIcon: Drawable? = null,
            override var socialMediaName: String? = "",
            override var packageName: String? = "",
            override var appIntent: Intent? = null
    ) : ShareModel()

    data class SMS(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShareModel()

    data class Email(
        override var socialMediaIcon: Drawable? = null,
        override var socialMediaName: String? = "",
        override var packageName: String? = "",
        override var appIntent: Intent? = null
    ) : ShareModel()

}
