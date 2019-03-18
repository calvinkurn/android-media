package com.tokopedia.kyc.view.interfaces;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;

public interface DocumentUploadView extends CustomerView {
    void uploadSuccess(KYCDocumentUploadResponse kycDocumentUploadResponse);
    void uploadFailure(KYCDocumentUploadResponse kycDocumentUploadResponse);
    void showHideProgressBar(boolean showProgressBar);
    Activity getActivity();
}
