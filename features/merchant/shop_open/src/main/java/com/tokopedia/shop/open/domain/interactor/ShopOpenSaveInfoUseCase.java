package com.tokopedia.shop.open.domain.interactor;

import android.text.TextUtils;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.product.manage.item.common.util.ShopSettingNetworkConstant;
import com.tokopedia.shop.open.data.model.UploadShopImageModel;
import com.tokopedia.shop.open.domain.ShopOpenSaveInfoRepository;
import com.tokopedia.shop.open.domain.model.ShopOpenSaveInfoRequestDomainModel;
import com.tokopedia.shop.open.domain.model.ShopOpenSaveInfoResponseModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ShopOpenSaveInfoUseCase extends UseCase<ShopOpenSaveInfoResponseModel> {
    public static final String PATH_FILE_IMAGE = "PATH_FILE_IMAGE";
    public static final String SHOP_DESCRIPTION = "SHOP_DESCRIPTION";
    public static final String TAG_LINE = "tag_line";
    public static final String STEP_INFO_1 = "1";
    public static final String LOGO = "logo";
    public static final String SERVER_ID = "server_id";
    public static final String PHOTO_OBJ = "photo_obj";
    public static final String SHORT_DESC = "short_desc";
    public static final String TAG_LINE_REQUEST_CLOUD = "tag_line";
    public static final String STEP = "step";
    public static final String URL_IMAGE_CLOUD = "URL_IMAGE_CLOUD";
    public static final String SHOP_ID = "shop_id";

    private ShopOpenSaveInfoRepository shopOpenSaveInfoRepository;
    private UploadImageUseCase<UploadShopImageModel> uploadImageUseCase;
    private UserSessionInterface userSession;

    @Inject
    public ShopOpenSaveInfoUseCase(ShopOpenSaveInfoRepository shopOpenSaveInfoRepository,
                                   UploadImageUseCase<UploadShopImageModel> uploadImageUseCase,
                                   @ImageUploaderQualifier UserSessionInterface userSession) {
        this.shopOpenSaveInfoRepository = shopOpenSaveInfoRepository;
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
    }

    public static RequestParams createRequestParams(String uriPathImage, String description, String shopSlogan,
                                                    String urlImageCloud, String serverId,
                                                    String picObj) {
        RequestParams params = RequestParams.create();
        if (uriPathImage != null) {
            params.putString(PATH_FILE_IMAGE, uriPathImage);
        }
        params.putString(SHOP_DESCRIPTION, description);
        params.putString(TAG_LINE_REQUEST_CLOUD, shopSlogan);
        if (urlImageCloud != null) {
            params.putString(URL_IMAGE_CLOUD, urlImageCloud);
        }
        if (serverId != null) {
            params.putString(SERVER_ID, serverId);
        }
        if (picObj != null) {
            params.putString(PHOTO_OBJ, picObj);
        }
        return params;
    }

    @Override
    public Observable<ShopOpenSaveInfoResponseModel> createObservable(final RequestParams requestParams) {
        if (!TextUtils.isEmpty(requestParams.getString(PATH_FILE_IMAGE, ""))) {
            return uploadImageUseCase.getExecuteObservable(createParamUploadImage(requestParams.getString(PATH_FILE_IMAGE, "")))
                    .flatMap(new Func1<ImageUploadDomainModel<UploadShopImageModel>,Observable<ShopOpenSaveInfoRequestDomainModel>>() {
                        @Override
                        public Observable<ShopOpenSaveInfoRequestDomainModel> call(final ImageUploadDomainModel<UploadShopImageModel> imageUploadDomainModel) {
                            final UploadShopImageModel uploadShopImageModel = imageUploadDomainModel.getDataResultImageUpload();
                            if (uploadShopImageModel == null) {
                                throw new RuntimeException();
                            }
                            List<String> errorMessageList = uploadShopImageModel.getMessageError();
                            if (errorMessageList != null && errorMessageList.size() > 0 && !TextUtils.isEmpty(errorMessageList.get(0))) {
                                throw new ErrorMessageException(errorMessageList.get(0));
                            }
                            ShopOpenSaveInfoRequestDomainModel dataRequest = new ShopOpenSaveInfoRequestDomainModel();
                            dataRequest.setPicSrc(uploadShopImageModel.getData().getImage().getPicSrc());
                            dataRequest.setPicUploaded(uploadShopImageModel.getData().getImage().getPicCode());
                            dataRequest.setServerId(imageUploadDomainModel.getServerId());
                            return Observable.just(dataRequest);
                        }
                    })
                    .flatMap(new Func1<ShopOpenSaveInfoRequestDomainModel, Observable<ShopOpenSaveInfoResponseModel>>() {
                        @Override
                        public Observable<ShopOpenSaveInfoResponseModel> call(final ShopOpenSaveInfoRequestDomainModel shopOpenSaveInfoRequestDomainModel) {
                            return shopOpenSaveInfoRepository.saveShopSetting(getImageRequest(
                                    shopOpenSaveInfoRequestDomainModel.getPicSrc(),
                                    shopOpenSaveInfoRequestDomainModel.getServerId(), shopOpenSaveInfoRequestDomainModel.getPicUploaded(),
                                    requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE_REQUEST_CLOUD, ""))).
                                    flatMap(new Func1<Boolean, Observable<ShopOpenSaveInfoResponseModel>>() {
                                        @Override
                                        public Observable<ShopOpenSaveInfoResponseModel> call(Boolean isSuccess) {
                                            ShopOpenSaveInfoResponseModel shopOpenSaveInfoResponseModel = new ShopOpenSaveInfoResponseModel();
                                            shopOpenSaveInfoResponseModel.setIsSaveShopSuccess(isSuccess);
                                            shopOpenSaveInfoResponseModel.setPicSrc(shopOpenSaveInfoRequestDomainModel.getPicSrc());
                                            shopOpenSaveInfoResponseModel.setShopDesc(requestParams.getString(SHOP_DESCRIPTION, ""));
                                            shopOpenSaveInfoResponseModel.setShopTagLine(requestParams.getString(TAG_LINE_REQUEST_CLOUD, ""));
                                            return Observable.just(shopOpenSaveInfoResponseModel);
                                        }
                                    });
                        }
                    });
        } else {
            return shopOpenSaveInfoRepository.saveShopSetting(getImageRequest(requestParams.getString(URL_IMAGE_CLOUD, "")
                    , requestParams.getString(SERVER_ID, ""), requestParams.getString(PHOTO_OBJ, ""),
                    requestParams.getString(SHOP_DESCRIPTION, ""), requestParams.getString(TAG_LINE_REQUEST_CLOUD, ""))).
                    flatMap(new Func1<Boolean, Observable<ShopOpenSaveInfoResponseModel>>() {
                        @Override
                        public Observable<ShopOpenSaveInfoResponseModel> call(Boolean isSuccess) {
                            ShopOpenSaveInfoResponseModel shopOpenSaveInfoResponseModel = new ShopOpenSaveInfoResponseModel();
                            shopOpenSaveInfoResponseModel.setIsSaveShopSuccess(isSuccess);
                            shopOpenSaveInfoResponseModel.setPicSrc(requestParams.getString(URL_IMAGE_CLOUD, ""));
                            shopOpenSaveInfoResponseModel.setShopDesc(requestParams.getString(SHOP_DESCRIPTION, ""));
                            shopOpenSaveInfoResponseModel.setShopTagLine(requestParams.getString(TAG_LINE_REQUEST_CLOUD, ""));
                            return Observable.just(shopOpenSaveInfoResponseModel);
                        }
                    });
        }
    }

    private HashMap<String, String> getImageRequest(String imageSrc, String serverId, String picObj, String shopDesc, String tagLine) {
        HashMap<String, String> params = new HashMap<>();
        params.put(LOGO, imageSrc);
        params.put(SERVER_ID, serverId);
        params.put(PHOTO_OBJ, picObj);
        params.put(SHORT_DESC, shopDesc);
        params.put(TAG_LINE_REQUEST_CLOUD, tagLine);
        params.put(STEP, STEP_INFO_1);
        return params;
    }

    public RequestParams createParamUploadImage(String pathFileImage) {
        Map<String, RequestBody> maps =  new HashMap<String, RequestBody>();
        RequestBody shopId = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId());
        RequestBody newAdd = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.GOLANG_VALUE);
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), ShopSettingNetworkConstant.RESOLUTION_DEFAULT_VALUE);
        maps.put(SHOP_ID, shopId);
        maps.put(ShopSettingNetworkConstant.SERVER_LANGUAGE, newAdd);
        maps.put(ShopSettingNetworkConstant.RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(pathFileImage, ShopSettingNetworkConstant.UPLOAD_SHOP_IMAGE_PATH, ShopSettingNetworkConstant.LOGO_FILENAME_IMAGE_JPG, maps);
    }
}
