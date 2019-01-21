package com.tokopedia.referral

interface Constants {

    interface PackageName {
        companion object {
            const val BLACKBERRY = "com.bbm"
            const val TWITTER = "com.twitter.android"
            const val INSTAGRAM = "com.instagram.android"
            const val FACEBOOK = "com.facebook.katana"
            const val WECHAT = "con.tencent.mm"
            const val WHATSAPP = "com.whatsapp"
            const val PINTEREST = "com.pinterest"
            const val GPLUS = "com.google.android.apps.plus"
            const val LINE = "jp.naver.line.android"
            const val TYPE_IMAGE = "image/jpeg"
            const val TYPE_IMAGE_2 = "image/*"
            const val TYPE_TEXT = "text/plain"
            const val GMAIL = "com.google.android.gm"
            const val SMS = "com.google.android.apps.messaging"
        }
    }

    interface Label {
        companion object {
            const val BBM = "BBM"
            const val FACEBOOK = "Facebook"
            const val TWITTER = "Twitter"
            const val WHATSHAPP = "Whatsapp"
            const val LINE = "Line"
            const val PINTEREST = "Pinterest"
            const val INSTAGRAM = "Instagram"
            const val GOOGLE_PLUS = "Google Plus"
            const val GMAIL = "Gmail"
            const val SMS = "Sms"
            const val OTHER = "Other"
        }
    }

    interface Key {
        companion object {
            const val REFERRAL_CODE = "REFERRAL_CODE"
            const val TYPE = "TYPE"
            const val NAME = "NAME"
            const val SHARING_CONTENT = "SHARING_CONTENT"
            const val URI = "URI"
            const val URL = "URL"
            const val MEDIA = "MEDIA"
            const val CODE = "code"
            const val OWNER = "owner";
        }
    }

    interface Placeholder {
        companion object {
            const val USER = "%user";
            const val OWNER = "%sender"
        }
    }

    interface Values {
        companion object {
            const val REFERRAL = "REFERRAL"
            const val SELECT_CHANNEL = "select channel"
            const val APP_SHARE_TYPE = "App"
            const val REFERRAL_TYPE = "Referral"
            const val ENCODING = "UTF-8"
            /**
             * Format for twitter : "http://www.twitter.com/intent/tweet?url=YOURURL&text=YOURTEXT"
             */
            const val TWITTER_DEFAULT = "http://www.twitter.com/intent/tweet?"
            const val WEB_PLAYSTORE_BUYER_APP_URL = "https://play.google.com/store/apps/details?id=com.tokopedia.tkpd"
            const val GUEST_USER_ADDRESSAL = "memberi";
        }
    }

    interface Action {
        companion object {
            const val CLICK_SHARE_TEMAN = "click ajak teman"
            const val CLICK_COPY_REFERRAL_CODE = "click copy referral code"
            const val CLICK_SHARE_CODE = "click share code"
            const val CLICK_EXPLORE_TOKOPEDIA = "click explore tokopedia"
            const val CLICK_KNOW_MORE = "click know more"
            const val CLICK_HOW_IT_WORKS = "click how it works"
            const val CLICK_WHAT_IS_TOKOCASH = "click apa itu tokocash"
            const val ACTION_GET_REFERRAL_CODE = 1;
            const val ACTION_GET_REFERRAL_CODE_IF_EXIST = 2;
        }
    }

    interface AppLinks {
        companion object {
            const val REFERRAL_WELCOME = "tokopedia://referral/{code}/{owner}"
            const val REFERRAL = "tokopedia://referral"
        }
    }

    interface ReferralApiPath {
        companion object {
            const val PATH_GET_REFERRAL_VOUCHER_CODE = "galadriel/promos/v2/referral/code"
        }
    }

    interface EventLabel {
        companion object {
            const val HOME = "homepage"
            const val CLICK_APP_SHARE_REFERRAL = "clickReferral"
        }
    }

    interface Category {
        companion object {
            const val REFERRAL = "Referral"
        }
    }

    interface ErrorCode{
        companion object {
            const val REFERRAL_API_ERROR = -1;
        }
    }
}
