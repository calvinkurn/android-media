package com.tokopedia.kyc.domain;

import android.content.Context;

import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.kyc.Constants;
import com.tokopedia.kyc.model.KYCDocumentUploadResponse;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UploadDocumentUseCase extends RestRequestSupportInterceptorUseCase {

    private String imagePath;
    private String docType;
    private int kycReqId;

    public UploadDocumentUseCase(Interceptor interceptor, Context context, String imagePath, String docType,
                                 int kycReqId) {
        super(interceptor, context);
        this.imagePath = imagePath;
        this.docType = docType;
        this.kycReqId = kycReqId;
    }

    private HashMap<String, Object> generateRequestParams(String imagePath, String docType, int kycReqId) {
        HashMap<String, Object> requestBodyMap = new HashMap<>();
        RequestBody reqImgFile = RequestBody.create(MediaType.parse("image/*"), new File(imagePath));

        try {
            requestBodyMap.put(Constants.Keys.DOCUMENT_FILE+"\"; filename=\""+ URLEncoder.encode(imagePath,"UTF-8") , reqImgFile);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestBodyMap.put(Constants.Keys.DOCUMENT_TYPE,  RequestBody.create(MediaType.parse("text/plain"),docType));
        requestBodyMap.put(Constants.Keys.KYC_REQUEST_ID,RequestBody.create(MediaType.parse("text/plain"), String.valueOf(kycReqId)));
        return requestBodyMap;
    }

    private byte[] getFileByteArray(String path) {
        File file = new File(path);
        byte[] bytesArray = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytesArray);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        RestRequest restRequest1 = new RestRequest.Builder(Constants.URLs.KYC_UPLOAD_IMAGE, KYCDocumentUploadResponse.class)
                .setRequestType(RequestType.POST_MULTIPART).setBody(generateRequestParams(imagePath, docType, kycReqId)).build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
