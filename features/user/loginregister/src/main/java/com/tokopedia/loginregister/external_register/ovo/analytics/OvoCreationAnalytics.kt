package com.tokopedia.loginregister.external_register.ovo.analytics

import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class OvoCreationAnalytics @Inject constructor(
        @Named(SessionModule.SESSION_MODULE)
        val userSession: UserSessionInterface)
{

    fun Map<String, Any>.appendAdditionalData(): Map<String, String> {
        val newData = mutableMapOf(
                BUSSINESS_UNIT to USER_ACCOUNT_BS,
                CURRENT_SITE to USER_ACCOUNT_CURRENT_SITE,
                USER_ID to userSession.userId
        )
        this.forEach {
            if(it.value is String) {
                newData[it.key] = it.value as String
            }
        }
        return newData
    }

    fun trackViewOvoRegisterDialog() {
        val data = TrackAppUtils.gtmData(
                EVENT_SCREEN_OVO,
                CATEGORY_OVO_CREATION,
                ACTION_VIEW_OVO_REGISTER,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickCreateOvo(){
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_CREATION,
                ACTION_CLICK_ON_CREATE_OVO,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickRegTkpdOnly(){
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_CREATION,
                ACTION_CLICK_ON_CREATE_TKPD_ONLY,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackViewOvoConnectDialog() {
        val data = TrackAppUtils.gtmData(
                EVENT_SCREEN_OVO,
                CATEGORY_OVO_CONNECT,
                ACTION_VIEW_OVO_CONNECT,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickConnectOvo(){
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_CONNECT,
                ACTION_CLICK_ON_CONNECT_OVO,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickConnectTkpdOnly(){
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_CONNECT,
                ACTION_CLICK_ON_CREATE_TKPD_ONLY,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackViewOvoSuccessPage() {
        val data = TrackAppUtils.gtmData(
                EVENT_SCREEN_OVO,
                CATEGORY_OVO_SUCCESS,
                ACTION_VIEW_SUCCESS_PAGE,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickOvoSuccessBtn() {
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_SUCCESS,
                ACTION_CLICK_SUCCESS_BUTTON,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackViewOvoFailPage() {
        val data = TrackAppUtils.gtmData(
                EVENT_SCREEN_OVO,
                CATEGORY_OVO_FAIL,
                ACTION_VIEW_FAIL_PAGE,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickOvoFailBtn() {
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_FAIL,
                ACTION_CLICK_FAIL_BUTTON,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackClickOvoFailBackBtn() {
        val data = TrackAppUtils.gtmData(
                EVENT_CLICK_OVO,
                CATEGORY_OVO_FAIL,
                ACTION_CLICK_FAIL_BACK_BUTTON,
                ""
        ).appendAdditionalData()
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {

        private val EVENT_CLICK_OVO = "clickOVO"
        private val EVENT_SCREEN_OVO = "viewOVOIris"

        private val ACTION_VIEW_OVO_REGISTER = "view ovo auto account creation"
        private val ACTION_VIEW_OVO_CONNECT = "view ovo auto account connection"
        private val ACTION_CLICK_ON_CREATE_OVO = "click on button create ovo"
        private val ACTION_CLICK_ON_CREATE_TKPD_ONLY = "click on create tokopedia only"
        val ACTION_CLICK_ON_CONNECT_OVO = "click on button connect ovo"
        val ACTION_VIEW_SUCCESS_PAGE = "view ovo auto account creation success page"
        val ACTION_CLICK_SUCCESS_BUTTON = "click on button mulai belanja"
        val ACTION_VIEW_FAIL_PAGE= "view ovo auto account creation fail page"
        val ACTION_CLICK_FAIL_BUTTON = "click on button lanjut daftar"
        val ACTION_CLICK_FAIL_BACK_BUTTON = "click on button close"


        private val CATEGORY_OVO_CREATION = "ovo auto account creation"
        private val CATEGORY_OVO_CONNECT = "ovo auto account connection"
        val CATEGORY_OVO_SUCCESS = "ovo auto account creation success page"
        val CATEGORY_OVO_FAIL = "ovo auto account creation fail page"

        private const val USER_ID = "userId"
        private const val CURRENT_SITE = "currentSite"
        private const val BUSSINESS_UNIT = "businessUnit"

        const val USER_ACCOUNT_BS = "user account"
        const val USER_ACCOUNT_CURRENT_SITE = "tokopediamarketplace"
    }

}