package com.tokopedia.kyc;

public interface Constants {
    interface Values{
        String OVOUPGRADE_STEP_1_SCR = "/ovoupgrade_step_1";
        String OVOUPGRADE_STEP_2_SCR = "/ovoupgrade_step_2";
        String KYC_CARDID_UPLOAD_SCR = "/kyc_cardid_upload";
        String KYC_ERROR_SCR = "/kyc_error_page";
        String VERFICATION_FAILURE_SCR = "/verification_failure";
        String VERFICATION_SUCCESS_SCR = "/verification_success";
        String KYC_CC_FOLLOWUP_SCR = "/kyc_cc_followup_page";
        String OVO = "OVO";
        String OVOUPGRADE_STEP_2_TITLE = "Upgrade ke OVO Premier";
        String KYC_SELFIEID_PREVIEW_AND_UOLOAD_SCR = "/kyc_selfieid_preview_and_uoload";
        String KYC_TNC_ACCEPTANDSUBMIT_SCR = "/kyc_tnc_acceptandsubmit_scr";
    }
    interface AppLinks{
        String OVOUPGRADE = "tokopedia://ovoupgrade";
    }
}
