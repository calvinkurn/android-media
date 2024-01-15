package com.tokopedia.shareexperience.domain

object ShareExConstants {
    object RemoteConfigKey {
        const val SOCIAL_MEDIA_ORDERING = "android_universal_sharing_order"
    }

    object DefaultValue {
        const val DEFAULT_TITLE = "Bagikan ke teman kamu"
    }

    object BranchKey {
        const val CUSTOM_META_TAGS = "\$custom_meta_tags"
        const val OG_TITLE = "\$og_title"
        const val OG_DESCRIPTION = "\$og_description"
        const val OG_URL = "\$og_url"
        const val OG_IMAGE_URL = "\$og_image_url"
        const val ANDROID_URL = "\$android_url"
        const val IOS_URL = "\$ios_url"
        const val DESKTOP_URL = "\$desktop_url"
        const val ANDROID_DEEPLINK_PATH = "\$android_deeplink_path"
        const val IOS_DEEPLINK_PATH = "\$ios_deeplink_path"
        const val AN_MIN_VERSION = "an_min_version"
        const val IOS_MIN_VERSION = "ios_min_version"
        const val SOURCE = "source"
    }

    object BranchValue {
        const val SOURCE = "android"
    }
}
