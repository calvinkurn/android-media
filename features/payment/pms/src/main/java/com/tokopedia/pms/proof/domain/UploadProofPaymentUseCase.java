package com.tokopedia.pms.proof.domain;

import android.content.res.Resources;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.pms.R;
import com.tokopedia.pms.common.Constant;
import com.tokopedia.pms.proof.model.DataUploadProof;
import com.tokopedia.pms.proof.model.ResponseUploadImageProof;
import com.tokopedia.pms.proof.model.UploadProof;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public class UploadProofPaymentUseCase extends UseCase<UploadProof> {

    public static final String IMAGE_PATH = "image_path";
    public static final String LOGO_FILENAME_IMAGE_JPG = "payment_image\"; filename=\"image.jpg";
    public static final String PATH_UPLOAD = "/web-service/v4/action/upload-image/upload_proof_image.pl";
    public static final String FILE_PATH = "filePath";
    public static final String NEW_ADD = "new_add";
    public static final String PAYMENT_ID = "payment_id";
    public static final String TOKEN = "token";

    UploadImageUseCase<ResponseUploadImageProof> uploadImageUseCase;
    GraphqlUseCase uploadProofUseCase;
    private Resources resources;

    public UploadProofPaymentUseCase(UploadImageUseCase<ResponseUploadImageProof> uploadImageUseCase, GraphqlUseCase uploadProofUseCase, Resources resources) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.uploadProofUseCase = uploadProofUseCase;
        this.resources = resources;
    }

    @Override
    public Observable<UploadProof> createObservable(RequestParams requestParams) {
        return uploadImageUseCase.getExecuteObservable(createParamUploadImage(requestParams.getString(IMAGE_PATH, ""),
                requestParams.getString(Constant.TRANSACTION_ID, "")))
                .flatMap(
                        imageUploadDomainModel -> {
                            Map<String, Object> variables = new HashMap<>();
                            variables.put(Constant.TRANSACTION_ID, requestParams.getString(Constant.TRANSACTION_ID, ""));
                            variables.put(Constant.MERCHANT_CODE, requestParams.getString(Constant.MERCHANT_CODE, ""));
                            variables.put(FILE_PATH, imageUploadDomainModel.getDataResultImageUpload().getResult().getPicSrc());

                            GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
                                    R.raw.upload_proof), DataUploadProof.class, variables, false);
                            uploadProofUseCase.clearRequest();
                            uploadProofUseCase.addRequest(graphqlRequest);
                            return uploadProofUseCase.createObservable(requestParams)
                                    .map(graphqlResponse -> {
                                        DataUploadProof cancelDetail = graphqlResponse.getData(DataUploadProof.class);
                                        return cancelDetail.getUploadProof();
                                    });
                        }
                );
    }

    public RequestParams createRequestParams(String transactionId, String merchantCode, String imageUrl) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Constant.TRANSACTION_ID, transactionId);
        requestParams.putString(Constant.MERCHANT_CODE, merchantCode);
        requestParams.putString(IMAGE_PATH, imageUrl);
        return requestParams;
    }

    public RequestParams createParamUploadImage(String pathFileImage, String transactionId) {
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody transactionIdBody = RequestBody.create(MediaType.parse("text/plain"), transactionId);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), "");

        maps.put(NEW_ADD, newAdd);
        maps.put(PAYMENT_ID, transactionIdBody);
        maps.put(TOKEN, token);
        return uploadImageUseCase.createRequestParam(pathFileImage, PATH_UPLOAD, LOGO_FILENAME_IMAGE_JPG, maps);
    }
}
