package com.tokopedia.tokomember_seller_dashboard.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object Tracker {

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    object Constants {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val TM_BUSINESS_UNIT = "tokomember"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val CURRENT_SITE = "currentSite"
        const val ECOMMERCE = "ecommerce"
    }

    object Event {
        const val EVENT_VIEW_BGP_IRIS = "viewBGPIris"
        const val EVENT_CLICK_BGP_IRIS = "clickBGP"
    }

    object Category {
        const val TM_DASHBOARD_HOME = "tokomember dashboard - homepage"
        const val TM_DASHBOARD_DETAIL_PROGRAM = "tokomember dashboard - detail program"
        const val TM_DASHBOARD_CHANGE_PROGRAM = "tokomember dashboard - ubah program"
        const val TM_DASHBOARD_EXTEND_PROGRAM = "tokomember dashboard - perpanjang program"
        const val TM_DASHBOARD_CREATE_COUPON = "tokomember dashboard - buat kupon"
        const val TM_DASHBOARD_DAFTAR = "tokomember dashboard - daftar tokomember"
        const val TM_DASHBOARD_INTRO = "tokomember dashboard - introduction"
    }

    object Action {

        //Intro
        const val VIEW_INTRO_PAGE = "intro - view page"
        const val CLICK_INTRO_DAFTAR = "intro - click daftar"
        const val CLICK_INTRO_LANJUT = "intro - click pelajari lebih lanjut"
        const val CLICK_DISMISS_BS_NON_OS = "bottomsheet_non_os - click close"
        const val CLICK_BUTTON_BS_NON_OS = "bottomsheet_non_os - click pelajari os"
        const val VIEW_BS_NON_OS = "bottomsheet_no_access - view bottomsheet"
        const val CLICK_BS_NON_OS_BACK = "bottomsheet_no_access - click back home"


        //CARD
        const val CLICK_CARD_CREATION_BACK = "card_creation - click back"
        const val CLICK_CARD_CREATION_BUTTON = "card_creation - click buat kartu"
        const val CLICK_CARD_CANCEL_PRIMARY = "popup_cancel_card - click lanjut pengaturan"
        const val CLICK_CARD_CANCEL_SECONDARY = "popup_cancel_card_ - click batalkan kartu"

        //Confusing tracker 12,13

        //Program
        const val CLICK_PROGRAM_CREATION_BACK = "program_creation - click back"
        const val CLICK_PROGRAM_CREATION_BUTTON = "program_creation - click buat program"

        //PROGRAM + EXtension
        const val CLICK_PROGRAM_CREATION_EXTENSION_BUTTON = "program_extension - click buat program"
        const val CLICK_PROGRAM_CREATION_EXTENSION_BACK = "program_extension - click back"
        const val CLICK_PROGRAM_POP_UP_CANCEL_EXTENSION_PRIMARY =
            "popup_cancel_program_extension - click lanjut"
        const val CLICK_PROGRAM_POP_UP_CANCEL_EXTENSION_SECONDARY =
            "popup_cancel_program_extension - click batalkan perpanjangan"

        //COUPON
        const val CLICK_COUPON_CREATION_BACK = "coupon_creation - click back"
        const val CLICK_COUPON_CREATION_BUTTON = "coupon_creation - click buat kupon"

        //Preview
        const val CLICK_SUMMARY_BACK = "summary - click back"
        const val CLICK_SUMMARY_BUTTON = "summary - click selesai"

        //HOME
        const val VIEW_BOTTOM_SHEET = "homepage_bottomsheet - view bottomsheet"
        const val CLICK_DISMISS_BOTTOM_SHEET = "homepage_bottomsheet - click balik or close"
        const val VIEW_HOME_TAB_SECTION = "home_tab - view section"

        //Program List
        const val VIEW_PROGRAM_LIST_TAB_SECTION = "program_tab - view section"
        const val CLICK_PROGRAM_LIST_BUTTON = "program_tab - click buat program"
        const val CLICK_PROGRAM_THREE_DOT = "program_tab - click 3 dots (active program)"
        const val CLICK_PROGRAM_BS_OPTION = "bottomsheet_active_program - click perpanjang program"
        const val CLICK_PROGRAM_ITEM_BUTTON =
            "program_tab - click perpanjang program (active program)"

        //Program List + Program Creation
        const val CLICK_BUTTON_PROGRAM_CREATION_FROM_P_LIST =
            "program_creation - click buat program"
        const val CLICK_BACK_PROGRAM_CREATION_FROM_P_LIST = "program_creation - click back"
        const val CLICK_PROGRAM_POPUP_PRIMARY = "popup_cancel_program_creation - click lanjut"
        const val CLICK_PROGRAM_POPUP_SECONDARY = "popup_cancel_program_creation - click bata"

        //Program List + COUPON creation
        const val CLICK_COUPON_CREATION_BUTTON_FROM_P_LIST = "coupon_creation - click buat kupon"
        const val CLICK_COUPON_CREATION_BACK_FROM_P_LIST = "coupon_creation - click back"

        //Coupon List
        const val VIEW_COUPON_LIST_TAB_SECTION = "coupon_tab - view section"
        const val CLICK_BACK_COUPON_LIST = "coupon_tab - click buat kupon"
        const val CLICK_OPTION_BS_QUOTA = "bottomsheet_active_coupon - click tambah kuota"
        const val CLICK_COUPON_ITEM_BUTTON = "coupon_tab - click tambah kuota (kuota habis)"
        const val CLICK_BS_ADD_QUOTA_BUTTON = "bottomsheet_add_coupon_quota - click simpan"

        //Coupon List + Single Coupon creation
        const val CLICK_COUPON_CANCEL_POP_UP_PRIMARY = "popup_cancel_coupon - click lanjut buat"
        const val CLICK_COUPON_CANCEL_POP_UP_SECONDARY = "popup_cancel_coupon - click batal"

        //Program Detail
        const val VIEW_PROGRAM_DETAIL = "detail_program_page - view page"

    }

    fun fillCommonItems(map: MutableMap<String, Any>) {
        map[Constants.BUSINESS_UNIT] = Constants.TM_BUSINESS_UNIT
        map[Constants.CURRENT_SITE] = Constants.TOKOPEDIA_MARKETPLACE
    }
}

