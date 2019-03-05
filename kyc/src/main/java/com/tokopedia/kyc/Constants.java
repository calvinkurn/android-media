package com.tokopedia.kyc;

public interface Constants {
    interface Values {
        String OVOUPGRADE_STEP_1_SCR = "/ovoupgrade_step_1";
        String ERROR_KYC_CONFIRM = "/error_kyc_confirm";
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
        String UPGRADE_OVO_SCR = "/upgradeovo";
        String NOMOR_KTP = "Nomor KTP";
        String PASSPORT_NUMBER = "Passport Number";
        String MOTHERS_MAIDEN_NAME = "Mother's Maiden Name";
        String NAMA_GADIS = "Nama Gadis Ibu Kandung";
        String TAG_CAMERA_PAGE = "card_id_camera";
        String SUCCESS = "success";
        int MAIDEN_NAME_LENGTH = 30;
        int KTP_NUMBER_LENGTH = 16;
        String WEB_VIEW_TITLE = "Sayarat dan Ketentuan";
        String SELFIE = "SELFIE";
    }
    interface AppLinks{
        String OVOUPGRADE = "tokopedia://ovoupgrade";
        String OVOUPGRADE_STATUS = "tokopedia://ovoupgradestatus/{status}";
    }

    interface Keys{
        String DOCUMENT_FILE = "document_file";
        String DOCUMENT_TYPE = "document_type";
        String KYC_REQUEST_ID = "kyc_request_id";
        int KYC_CARDID_CAMERA = 1;
        int KYC_SELFIEID_CAMERA = 2;
        String STATUS = "status";
        String FROM_RETAKE_FLOW = "from_retake_flow";
        String MESSAGE = "message";

    }

    interface URLs{
        String KYC_UPLOAD_IMAGE = "https://goalapi-staging.tokopedia.com/api/v1/kyc/upload_document";
        String KYC_CONFIRM = "https://goal.tokopedia.com/api/v1/kyc/confirm";
        String OVO_TNC_PAGE = "https://www.ovo.id/tnc";
    }

    interface RegEx{
        String name = "^[a-zA-Z\\s]+";
    }

    interface ErrorMsg{
        String KTP_NUMBER = "Nomor KTP harus 16 digit";
        String MOTHERS_NAME = "Nama gadis ibu kandung maksimal 30 karakter";
        String PASSPORT_NUMBER = "Passport number is not valid";
        String FORIEGNER_MOTHERS_NAME = "Mother's maiden name is not valid";
        String SOMETHING_WENT_WRONG = "Ada yang salah, harap coba lagi";
    }

    interface HintMsg{
        String KTP ="Masukkan 16 digit angka sesuai pada KTP";
        String NAMA_GADIAS ="Maksimal 30 karakter";
        String PASSPORT = "Input your passport number";
        String MOTHERS_NAME = "Maximum 30 characters";
        String EDTXT_KTP_NO = "Contoh: 320110108960XXXX";
        String EDTXT_PASSPORT_NO = "Example: 320110108960XXXX";
        String EDTXT_NAMA_GADIAS = "Contoh: Luna Maya";
        String EDTXT_MOTHERS_NAME= "Example: Luna Maya";
    }

    interface Status{
        String INPROGRESS = "inprogress";
        String SUCCESSFUL = "successful";
        String FAILED = "failed";
    }
}
