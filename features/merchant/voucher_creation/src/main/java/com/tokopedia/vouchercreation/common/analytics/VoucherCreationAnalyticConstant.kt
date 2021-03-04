package com.tokopedia.vouchercreation.common.analytics

object VoucherCreationAnalyticConstant {

    object Key {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val ID = "id"
        const val NAME = "name"
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val CURRENT_SITE = "currentSite"
        const val SHOP_ID = "shopId"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val PAGE_SOURCE = "pageSource"
    }

    object Values {
        const val TOKOPEDIA_SELLER_APP = "tokopedia seller app"
        const val TOKOPEDIA_SELLER = "tokopediaseller"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val PHYSICAL_GOODS = "physical goods"
        const val COMMUNICATION = "communication"
    }

    object ScreenName {

        object VoucherCreation {
            const val TARGET = "/vouchercreation - target"
            const val TYPE_BUDGET = "/vouchercreation - type & budget"
            const val PERIOD = "/vouchercreation - period"
            const val REVIEW = "/vouchercreation - review"
        }

        object VoucherList {
            const val ACTIVE = "/voucherlistactive"
            const val HISTORY = "/voucherlisthistory"
        }

        object VoucherDetail {
            const val UPCOMING = "/voucherdetailpage - mendatang"
            const val ONGOING = "/voucherdetailpage - berlangsung"
            const val ENDED = "/voucherdetailpage - berakhir"
            const val CANCELLED = "/voucherdetailpage - dibatalkan"
        }

        const val VOUCHER_DUPLICATE_REVIEW = "/voucherduplicate - review"
    }

    object PageSource {
        const val DETAIL = "/voucherdetailpage - berlangsung"
        const val LIST = "/voucherlistactive"
    }

    object Event {
        const val OPEN_SCREEN = "openScreen"
        const val CLICK_ADS_PROMO = "clickAdsPromo"
        const val CLICK_ADS_CREATION = "clickAdsCreation"
        const val VIEW_ADS_CREATION_IRIS = "viewAdsCreationIris"
        const val CLICK_VOUCHER_LIST = "clickVoucherList"
        const val VIEW_VOUCHER_LIST_IRIS = "viewVoucherListIris"
        const val CLICK_VOUCHER_DETAIL = "clickVoucherDetail"
        const val VIEW_VOUCHER_DETAIL_IRIS = "viewVoucherDetailIris"
        const val CLICK_SHARE = "clickShare"
        const val CLICK_VOUCHER_DUPLICATE = "clickVoucherDuplicate"
        const val CLICK_MERCHANT_VOUCHER = "clickMerchantVoucher"
    }

    object EventCategory {

        object VoucherCreation {
            const val TARGET = "voucher creation - target"
            const val TYPE_BUDGET = "voucher creation - type & budget"
            const val PERIOD = "voucher creation - period"
            const val REVIEW = "voucher creation - review"
            const val CREATE = "merchant voucher code - create"
        }

        object VoucherList {
            const val ACTIVE = "voucher list active"
            const val HISTORY = "voucher list history"
        }

        object VoucherDetail {
            const val UPCOMING = "voucher detail page - mendatang"
            const val ONGOING = "voucher detail page - berlangsung"
            const val ENDED = "voucher detail page - berakhir"
            const val CANCELLED = "voucher detail page - dibatalkan"
        }

        const val SHARE_SHEET = "share sheet"
        const val VOUCHER_DUPLICATE_REVIEW = "voucher duplicate - review"
    }

    object EventAction {

        object Impression {
            const val VOUCHER_DETAIL_DISPLAY_PUBLIC = "impression tampilan voucher detail - public"
            const val VOUCHER_DETAIL_DISPLAY_PRIVATE = "impression tampilan voucher detail - private"
            const val DISPLAY_PERIOD = "impression periode tampil"
            const val ERROR_STATE = "impression error state"
            const val NO_RESULT = "impression no result"
            const val VOUCHER_DETAIL_DISPLAY = "impression tampilan voucher detail"
            const val DOWNLOAD_VOUCHER = "impression download voucher"
        }

        object Click {
            const val LAMP_ICON = "click lamp icon"
            const val VOUCHER_TARGET_PUBLIC = "click voucher target - public"
            const val VOUCHER_TARGET_PRIVATE = "click voucher target - private"
            const val VOUCHER_DISPLAY_PUBLIK = "click tampilan voucher - publik"
            const val VOUCHER_DISPLAY_PUBLIC = "click tampilan voucher - public"
            const val VOUCHER_DISPLAY_PRIVATE = "click tampilan voucher - private"
            const val SAVE_PRIVATE = "click simpan - private"
            const val CLOSE_PRIVATE = "click close - private"
            const val EDIT_PROMO_PRIVATE = "click edit promo code - private"
            const val CONTINUE = "click continue"
            const val TIPS_TRICK_VOUCHER_NAME = "tips & tricks - voucher name"
            const val TIPS_TRICK_VOUCHER_TYPE = "tips & trick - voucher type"
            const val TIPS_TRICK_CASHBACK_TYPE = "tips & trick - cashback type"
            const val TIPS_TRICK_SPENDING_ESTIMATION = "tips & trick - spending estimation"
            const val VOUCHER_TYPE_SHIPPING = "click voucher type - shipping"
            const val VOUCHER_TYPE_CASHBACK = "click voucher type - cashback"
            const val CLOSE_INFO_SECTION = "close info section"
            const val TOOLTIP_SPENDING_ESTIMATION = "click tooltip spending estimation"
            const val CASHBACK_TYPE_RUPIAH = "cashback type - rupiah"
            const val CASHBACK_TYPE_PERCENTAGE = "cashback type - persentase"
            const val PRICE_SUGGESTION_EDIT_NOW = "click price suggestion - edit now"
            const val CALENDAR_START = "click calendar start"
            const val CALENDAR_END = "click calendar end"
            const val BACK_BUTTON = "click back button"
            const val EDIT_INFO_VOUCHER = "click edit info voucher"
            const val EDIT_VOUCHER_BENEFIT = "click edit voucher benefit"
            const val EDIT_PERIOD = "click edit period"
            const val ADD_VOUCHER = "click add voucher"
            const val TNC = "click tnc"
            const val VOUCHER_SUCCESS_SHARE_NOW = "voucher success - share now"
            const val VOUCHER_SUCCESS_CLICK_BACK_BUTTON = "voucher success - click back button"
            const val VOUCHER_SUCCESS_DOWNLOAD = "voucher success - download"
            const val FAILED_POP_UP_TRY_AGAIN = "failed pop up - try again"
            const val FAILED_POP_UP_HELP = "failed pop up - help"
            const val CANCEL_VOUCHER_CREATION_CANCELLED = "cancel voucher creation - cancelled"
            const val CANCEL_VOUCHER_CREATION_BACK = "cancel voucher creation - back"
            const val REVIEW_PROCESS_CLICK_CANCEL_BUTTON = "review process - click cancel button"
            const val REVIEW_PROCESS_CLICK_BACK_BUTTON = "review process - click back button"
            const val REVIEW_PROCESS_CLICK_IN_HERE = "review process - click in here"
            const val ERROR_CREATION_EDIT_PROMO_CODE = "error creation - edit promo code"
            const val ERROR_CREATION_BACK_BUTTON = "error creation - back button"
            const val EMPTY_VOUCHER_CREATE_VOUCHER = "empty voucher - create voucher"
            const val EMPTY_VOUCHER_HISTORY = "empty voucher - history"
            const val ADD_BUTTON = "click add button"
            const val HISTORY_BUTTON = "click history button"
            const val VOUCHER_ICON = "click voucher icon"
            const val VOUCHER_SHARE = "click voucher share"
            const val CHANGE_QUOTA = "click change quota"
            const val BURGER_BUTTON_ONGOING = "click burger button - berlangsung"
            const val BURGER_BUTTON_UPCOMING = "click burger button - mendatang"
            const val EDIT_QUOTA_UPCOMING = "click edit quota - mendatang"
            const val CHANGE_PERIOD_UPCOMING = "click change period - mendatang"
            const val DETAIL_AND_EDIT_UPCOMING = "click detail dan edit - mendatang"
            const val DOWNLOAD_UPCOMING = "download - mendatang"
            const val DUPLICATE_UPCOMING = "duplicate - mendatang"
            const val CANCEL_UPCOMING = "cancel - mendatang"
            const val EDIT_QUOTA_ONGOING = "click edit quota - berlangsung"
            const val DETAIL_AND_EDIT_ONGOING = "click detail dan edit - berlangsung"
            const val SHARE_ONGOING = "click share - berlangsung"
            const val DOWNLOAD_ONGOING = "download - berlangsung"
            const val DUPLICATE_ONGOING = "duplicate - berlangsung"
            const val CANCEL_ONGOING = "cancel - berlangsung"
            const val SHARE_UPCOMING = "share - mendatang"
            const val TRY_AGAIN_ERROR_STATE = "click coba lagi - error state"
            const val CREATE_VOUCHER = "click create voucher"
            const val SEARCH_BAR = "click search bar"
            const val SORT_BUTTON = "click sort button"
            const val FILTER_BY_TYPE = "click filter by type"
            const val APPLY = "click terapkan"
            const val FILTER_TYPE_CASHBACK = "click filter type cashback"
            const val FILTER_TYPE_SHIPPING = "click filter type shipping"
            const val FILTER_TYPE_PUBLIC = "click filter type public"
            const val FILTER_TYPE_PRIVATE = "click filter type private"
            const val DUPLICATE_VOUCHER = "click duplicate voucher"
            const val BURGER_BUTTON = "click burger button"
            const val DUPLICATE_VOUCHER_BOTTOM_SHEET = "click duplicate voucher - bottom sheet"
            const val VOUCHER_DETAIL_BOTTOM_SHEET = "click voucher detail - bottom sheet"
            const val COPY_PROMO_CODE = "click salin promo code"
            const val IN_HERE = "click in here"
            const val TOOLTIP_CURRENT_SPENDING = "click tooltip current spending"
            const val TIPS_TRICKS = "click tips and tricks"
            const val SHARE_VOUCHER = "click share voucher"
            const val DOWNLOAD_VOUCHER = "click download voucher"
            const val DETAIL_STAT = "click detail stat"
            const val TOOLTIP_TOTAL_SPENDING = "click tooltip total spending"
            const val CHOOSE_VOUCHER_SIZE_1 = "choose voucher size 1"
            const val CHOOSE_VOUCHER_SIZE_2 = "choose voucher size 2"
            const val CHOOSE_VOUCHER_SIZE_3 = "choose voucher size 3"
            const val CHOOSE_VOUCHER_SIZE_1_DROPDOWN = "choose voucher size 1 - drop down"
            const val CHOOSE_VOUCHER_SIZE_2_DROPDOWN = "choose voucher size 2 - drop down"
            const val CHOOSE_VOUCHER_SIZE_3_DROPDOWN = "choose voucher size 3 - drop down"
            const val SHARE_COPY_LINK = "share - copy link"
            const val SHARE_INSTAGRAM = "share - instagram"
            const val SHARE_FACEBOOK = "share - facebook"
            const val SHARE_FB_MESSENGER = "share - fb messenger"
            const val SHARE_WHATSAPP = "share - whatsapp"
            const val SHARE_LINE = "share - line"
            const val SHARE_TWITTER = "share - twitter"
            const val SHARE_BROADCAST = "share - broadcast"
            const val SHARE_OTHERS = "share - lainnya"
            const val PERIOD = "click periode"
        }
    }

    object EventLabel {
        const val PUBLIC = "public"
        const val PRIVATE = "private"

        const val CASHBACK = "cashback"
        const val SHIPPING = "shipping"

        const val ONGOING = "berlangsung"
        const val UPCOMING = "mendatang"

        const val NEWEST = "terkini"
        const val OLDEST = "terlama"

        const val ENDED = "berakhir"
        const val CANCELLED = "dibatalkan"

        const val WITH_RECOMMENDATION = "with recommendation - "
        const val EDITED_RECOMMENDATION = "edited recommendation - "
        const val NO_RECOMMENDATION = "no recommendation - "
    }

}