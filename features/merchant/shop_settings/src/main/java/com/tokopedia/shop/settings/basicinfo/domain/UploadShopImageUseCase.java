package com.tokopedia.shop.settings.basicinfo.domain;

import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.shop.common.graphql.data.GraphQLSuccessMessage;
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

public class UploadShopImageUseCase extends UseCase<UploadShopEditImageModel> {

    public static final String LOGO_FILENAME_IMAGE_JPG = "logo\"; filename=\"image.jpg";
    public static final String PATH_UPLOAD = "/web-service/v4/action/upload-image/upload_shop_image.pl";
    public static final String EXTRA_IMAGE_PATH = "image_path";

    public static final String KEY_NEW_ADD = "new_add";
    public static final String KEY_RESOLUTION = "resolution";

    public static final String RESOLUTION_DEFAULT_VALUE = "300";
    public static final String GOLANG_VALUE = "2";

    UploadImageUseCase<UploadShopEditImageModel> uploadImageUseCase;
//    GraphqlUseCase uploadProofUseCase;
//    private Resources resources;
//
    @Inject
    public UploadShopImageUseCase(UploadImageUseCase<UploadShopEditImageModel> uploadImageUseCase) {
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<UploadShopEditImageModel> createObservable(RequestParams requestParams) {
        return uploadImageUseCase.getExecuteObservable(createParamUploadImage(requestParams.getString(EXTRA_IMAGE_PATH, "")))
//                .flatMap(
//                        imageUploadDomainModel -> {
//                            Map<String, Object> variables = new HashMap<>();
//                            variables.put(Constant.TRANSACTION_ID, requestParams.getString(Constant.TRANSACTION_ID, ""));
//                            variables.put(Constant.MERCHANT_CODE, requestParams.getString(Constant.MERCHANT_CODE, ""));
//                            variables.put(FILE_PATH, imageUploadDomainModel.getDataResultImageUpload().getResult().getPicSrc());
//
//                            GraphqlRequest graphqlRequest = new GraphqlRequest(GraphqlHelper.loadRawString(resources,
//                                    R.raw.upload_proof), DataUploadProof.class, variables);
//                            uploadProofUseCase.clearRequest();
//                            uploadProofUseCase.addRequest(graphqlRequest);
//                            return uploadProofUseCase.createObservable(requestParams)
//                                    .map(graphqlResponse -> {
//                                        DataUploadProof cancelDetail = graphqlResponse.getData(DataUploadProof.class);
//                                        return cancelDetail.getUploadProof();
//                                    });
//                        }
//                );
        .flatMap(new Func1<ImageUploadDomainModel<UploadShopEditImageModel>, Observable<UploadShopEditImageModel>>() {
            @Override
            public Observable<UploadShopEditImageModel> call(ImageUploadDomainModel<UploadShopEditImageModel> model) {
                return Observable.just(model.getDataResultImageUpload());
            }
        });
    }

    public static RequestParams createRequestParams(String imagePath) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(EXTRA_IMAGE_PATH, imagePath);
        return requestParams;
    }

    private RequestParams createParamUploadImage(String pathFileImage) {
        Map<String, RequestBody> maps = new HashMap<>();
        RequestBody newAddValue = RequestBody.create(MediaType.parse("text/plain"), GOLANG_VALUE);
        RequestBody resolutionValue = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_DEFAULT_VALUE);

        maps.put(KEY_NEW_ADD, newAddValue);
        maps.put(KEY_RESOLUTION, resolutionValue);
        return uploadImageUseCase.createRequestParam(pathFileImage, PATH_UPLOAD, LOGO_FILENAME_IMAGE_JPG, maps);
    }
}
