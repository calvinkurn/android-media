package com.tokopedia.universal_sharing.util

object UniversalShareConst {

    // Package name to intent when click the channel
    object PackageChannel {
        const val PACKAGE_NAME_INSTAGRAM = "com.instagram.android"
        const val PACKAGE_NAME_FACEBOOK = "com.facebook.katana"
        const val FACEBOOK_FEED_ACTIVITY = "com.facebook.composer.shareintent.AddToStoryAlias"
        const val FACEBOOK_STORY_INTENT_ACTION = "com.facebook.stories.ADD_TO_STORY"
        const val PACKAGE_NAME_WHATSAPP = "com.whatsapp"
        const val PACKAGE_NAME_LINE = "jp.naver.line.android"
        const val PACKAGE_NAME_TWITTER = "com.twitter.android"
        const val PACKAGE_NAME_TELEGRAM = "org.telegram.messenger"
        const val PACKAGE_NAME_GMAIL = "com.google.android.gm"
    }

    // remote config Social media ordering keys
    object OrderingKey {
        const val KEY_IG_FEED = "IG_Feed"
        const val KEY_IG_STORY = "IG_Story"
        const val KEY_IG_DM = "IG_DM"
        const val KEY_FB_FEED = "FB_Feed"
        const val KEY_FB_STORY = "FB_Story"
        const val KEY_WHATSAPP = "WhatsApp"
        const val KEY_LINE = "Line"
        const val KEY_TWITTER = "Twitter"
        const val KEY_TELEGRAM = "Telegram"
    }

    // Remote Config Key on Universal Share Bottomsheet
    object RemoteConfigKey {
        const val GLOBAL_CUSTOM_SHARING_FEATURE_FLAG = "android_enable_custom_sharing"
        const val GLOBAL_SCREENSHOT_SHARING_FEATURE_FLAG = "android_enable_screenshot_sharing"
        const val GLOBAL_AFFILIATE_FEATURE_FLAG = "android_enable_affiliate_universal_sharing"
        const val GLOBAL_ENABLE_OG_IMAGE_TRANSFORM = "android_enable_og_image_transformation"
        const val SOCIAL_MEDIA_ORDERING = "android_universal_sharing_order"
    }

    // Image Type of the share link
    object ImageType {
        // no image set for the share link
        const val KEY_NO_IMAGE = "no_image"

        // using image that passing to the bottomsheet
        const val KEY_IMAGE_DEFAULT = "default"

        // using image that generated from the image generator
        const val KEY_CONTEXTUAL_IMAGE = "contextual_image"

        const val MEDIA_VALUE_PLACEHOLDER = "{media_image}"
    }

    object SizeScreenShoot {
        const val PREVIEW_IMG_SCREENSHOT_HEIGHT = 600
        const val PREVIEW_IMG_SCREENSHOT_WIDTH = 1080
        const val THUMBNAIL_IMG_SCREENSHOT_HEIGHT = 200
        const val THUMBNAIL_IMG_SCREENSHOT_WIDTH = 360
    }
}
