package com.tokopedia.kyc.view.presenter;

public interface IDocumentUploadListener {
    void makeDocumentUploadRequest(String imagePath, String docType, int kycReqId);
}
