package com.tokopedia.kyc;

public interface Constants {
    interface Values {
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
        String UPGRADE_OVO_SCR = "/upgradeovo";
        String NOMOR_KTP = "Nomor KTP";
        String PASSPORT_NUMBER = "Passport Number";
        String MOTHERS_MAIDEN_NAME = "Mother's Maiden Name";
        String NAMA_GADIS = "Nama Gadis Ibu Kandung";
        String TAG_CAMERA_PAGE = "card_id_camera";
        String SUCCESS = "success";
        int MAIDEN_NAME_LENGTH = 30;
    }
    interface AppLinks{
        String OVOUPGRADE = "tokopedia://ovoupgrade";
        String OVOUPGRADE_COMPLETE = "tokopedia://ovoupgradedone";
    }

    interface Keys{
        String DOCUMENT_FILE = "document_file";
        String DOCUMENT_TYPE = "document_type";
        String KYC_REQUEST_ID = "kyc_request_id";
        int KYC_CARDID_CAMERA = 1;
        int KYC_SELFIEID_CAMERA = 2;
        String PERSISTENT_STORE = "persistent_store";
        String DOCID_CARD = "docid_card";
        String DOCID_SELFIE = "docid_selfie";
        String DOCTYPE = "doctype";
        String MOTHERS_MAIDEN_NAME = "mothers_maiden_name";
        String DOC_NUMBER = "doc_number";
        String KYC_REQ_ID = "kyc_req_id";
        String CARD_IMG_PATH = "card_img_path";
        String FLIP_CARD_IMG = "flip_card_img";
        String SELFIE_IMG_PATH = "selfie_img_path";
        String FLIP_SELFIE_IMG = "flip_selfie_img";
        String CONFIRM_DATA_REQ_CONTAINER = "confirm_data_req_container";

    }

    interface URLs{
        String KYC_UPLOAD_IMAGE = "https://goalapi-staging.tokopedia.com/api/v1/kyc/upload_document";
        String KYC_CONFIRM = "https://goal.tokopedia.com/api/v1/kyc/confirm";
    }

    interface RegEx{
        String name = "^[a-zA-Z\\s]+";
    }
}
