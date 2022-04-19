package com.tokopedia.withdraw.auto_withdrawal.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

private const val EVENT_NAME = "viewWithdrawalIris"
private const val EVENT_NAME_CLICK_WITHDRAWAL = "clickWithdrawal"

private const val EVENT_CAT_AUTO_WD_PAGE = "autowd page"

private const val EVENT_ACTION_IS_SCHEDULE_ACTIVATED = "is schedule activated"
private const val EVENT_ACTION_IS_REKENING_PREMIUM = "is rekening premium"
private const val EVENT_ACTION_CLICK_CHECK_BOX = "click checkbox autowd"
private const val EVENT_ACTION_CLICK_SIMPAN = "click simpan"
private const val EVENT_ACTION_GABUNG_RP_BOTTOMSHEET = "gabung rp bottomsheet"
private const val EVENT_ACTION_CLICK_INFO = "click info"
private const val EVENT_ACTION_GABUNG_RP_INFO = "gabung rp info"
private const val EVENT_ACTION_CLICK_MORE_INFO = "click selengkapnya info"
private const val EVENT_ACTION_CLICK_TNC = "click syarat dan ketentuan"
private const val EVENT_ACTION_VIEW_SUCCESS_TOASTER = "view success toaster autowd"
private const val EVENT_ACTION_VIEW_OPT_OUT_DIALOG = "view dialog opt out"
private const val EVENT_ACTION_CLICK_OPT_OUT_DIALOG = "click dialog opt out"

private const val EVENT_LABEL_TRUE = "true"
private const val EVENT_LABEL_FALSE = "false"

private const val EVENT_LABEL_TICK = "tick"
private const val EVENT_LABEL_UN_TICK = "untick"

private const val EVENT_LABEL_SUCCEED = "succeed"
private const val EVENT_LABEL_BLOCKED = "blocked"

const val EVENT_LABEL_CONTINUE = "continue"
const val EVENT_LABEL_DISMISS = "dismiss"

const val EVENT_LABEL_AUTO_WD_SCH_INSERTED = "insert"
const val EVENT_LABEL_AUTO_WD_SCH_UPDATED = "update"
const val EVENT_LABEL_AUTO_WD_SCH_DELETE = "delete"


class AutoWithdrawAnalytics @Inject constructor() {

    /**
     * only for first load and ignore all consecutive reload
     * */
    fun onAutoSettingLoaded(isScheduleActivated: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_IS_SCHEDULE_ACTIVATED,
                if (isScheduleActivated) EVENT_LABEL_TRUE else EVENT_LABEL_FALSE
        ))
    }

    /**
     * only for first load and ignore all consecutive reload
     * */
    fun isUserRekeningPremiumEvent(isRekeningPrem: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_IS_REKENING_PREMIUM,
                if (isRekeningPrem) EVENT_LABEL_TRUE else EVENT_LABEL_FALSE
        ))
    }


    fun onSettingCheckBoxToggled(isChecked: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_CHECK_BOX,
                if (isChecked) EVENT_LABEL_TICK else EVENT_LABEL_UN_TICK)
        )
    }

    /**
     * onclick save autowd setting event*/
    fun onSimpanButtonClick(isSuccess: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_SIMPAN,
                if (isSuccess) EVENT_LABEL_SUCCEED else EVENT_LABEL_BLOCKED)
        )
    }

    fun onOpenJoinRekeningBottomSheetClick() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_GABUNG_RP_BOTTOMSHEET,
                ""))
    }

    fun onClickInfoMenu() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_INFO,
                ""))
    }


    fun clickOnGabungRPInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_GABUNG_RP_INFO,
                ""))
    }


    fun onClickMoreInfoOnInfoBottomSheet() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_MORE_INFO,
                ""))
    }

    fun onClickViewTermsCondition() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_TNC,
                ""))
    }

    fun onViewAutoWdSuccessToaster(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_VIEW_SUCCESS_TOASTER,
                label))
    }

    fun onViewOptOutDialog() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_VIEW_OPT_OUT_DIALOG,
                ""))
    }

    fun onOptOutDialogAction(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(EVENT_NAME_CLICK_WITHDRAWAL,
                EVENT_CAT_AUTO_WD_PAGE,
                EVENT_ACTION_CLICK_OPT_OUT_DIALOG,
                label))
    }


}