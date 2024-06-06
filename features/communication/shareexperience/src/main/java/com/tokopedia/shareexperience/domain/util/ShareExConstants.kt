package com.tokopedia.shareexperience.domain.util

object ShareExConstants {

    object Rollence {
        const val ROLLENCE_SHARE_EX = "shareex_an_ma"
        const val ROLLENCE_SHARE_EX_SA = "shareex_an_sa"
        const val ROLLENCE_SHARE_EX_REVIEW = "an_sharexreview"
        const val ROLLENCE_SHARE_EX_TY = "shareex_an_typv1"
    }

    object RemoteConfigKey {
        const val SOCIAL_MEDIA_ORDERING = "android_universal_sharing_order"
    }

    object DefaultValue {
        const val SOURCE = "share"
        const val DEFAULT_TITLE = "Bagikan ke teman kamu"
    }

    object ShortLinkKey {
        const val CUSTOM_META_TAGS = "\$custom_meta_tags"
        const val OG_TITLE = "\$og_title"
        const val OG_TITLE_RAW = "og_title"
        const val OG_DESCRIPTION = "\$og_description"
        const val OG_DESCRIPTION_RAW = "og_description"
        const val OG_URL = "\$og_url"
        const val OG_IMAGE_URL = "\$og_image_url"
        const val OG_IMAGE_URL_RAW = "og_image_url"
        const val ANDROID_URL = "\$android_url"
        const val IOS_URL = "\$ios_url"
        const val DESKTOP_URL = "\$desktop_url"
        const val ANDROID_DEEPLINK_PATH = "\$android_deeplink_path"
        const val IOS_DEEPLINK_PATH = "\$ios_deeplink_path"
        const val AN_MIN_VERSION = "an_min_version"
        const val IOS_MIN_VERSION = "ios_min_version"
        const val SOURCE = "source"
    }

    object ShortLinkValue {
        const val TIMEOUT_LIMIT = 5000L
        const val SOURCE = "android"
        const val SOURCE_SHARING = "sharing"
    }

    object UTM {
        const val SOURCE_KEY = "utm_source"
        const val MEDIUM_KEY = "utm_medium"
        const val CAMPAIGN_KEY = "utm_campaign"

        const val MEDIUM_VALUE = "share"
    }

    object PackageName {
        const val WHATSAPP = "com.whatsapp"
        const val FACEBOOK = "com.facebook.katana"
        const val INSTAGRAM = "com.instagram.android"
        const val LINE = "jp.naver.line.android"
        const val TWITTER = "com.twitter.android"
        const val TELEGRAM = "org.telegram.messenger"
        const val GMAIL = "com.google.android.gm"
    }

    object SMS {
        const val URI = "smsto:"
        const val BODY = "sms_body"
    }

    object IntentAction {
        const val FB_STORY = "com.facebook.stories.ADD_TO_STORY"
        const val IG_FEED = "com.instagram.share.ADD_TO_FEED"
        const val IG_STORY = "com.instagram.share.ADD_TO_STORY"
    }
}
