package com.tokopedia.useridentification.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.useridentification.view.listener.UserIdentificationUploadImage;
import com.tokopedia.useridentification.view.viewmodel.AttachmentImageModel;
import com.tokopedia.useridentification.view.viewmodel.ImageUploadModel;
import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvinatin on 21/11/18.
 */

public class UserIdentificationUploadImagePresenter extends BaseDaggerPresenter<UserIdentificationUploadImage.View>
        implements UserIdentificationUploadImage.Presenter{

    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private static final int MAX_FILE_SIZE = 15360;
    private static final int MINIMUM_HEIGHT = 100;
    private static final int MINIMUM_WIDTH = 300;
    private static final long DEFAULT_ONE_MEGABYTE = 1024;
    private CompositeSubscription compositeSubscription;

    private final UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;
    private final UserSession userSession;

    public UserIdentificationUploadImagePresenter(UploadImageUseCase<AttachmentImageModel>
                                                          uploadImageUseCase,
                                                  UserSession userSession) {
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
    }

    @Override
    public void uploadImage(UserIdentificationStepperModel model) {
        List<ImageUploadModel> attachments = parseToModel(model);
        compositeSubscription.add(Observable.from(attachments)
                .flatMap(new Func1<ImageUploadModel, Observable<ImageUploadModel>>() {
                    @Override
                    public Observable<ImageUploadModel> call(ImageUploadModel imageUploadModel) {
                        return Observable.zip(Observable.just(imageUploadModel),
                                uploadImageUseCase.createObservable(
                                        createParam(imageUploadModel.getFilePath())
                                ), new Func2<ImageUploadModel, ImageUploadDomainModel<AttachmentImageModel>, ImageUploadModel>() {
                                    @Override
                                    public ImageUploadModel call(ImageUploadModel imageUploadModel, ImageUploadDomainModel<AttachmentImageModel> uploadDomainModel) {
                                        String url = uploadDomainModel.getDataResultImageUpload().getData().getPicSrc();
                                        if (url.contains(DEFAULT_RESOLUTION)) {
                                            url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_300);
                                        }
                                        imageUploadModel.setUrl(url);
                                        return imageUploadModel;
                                    }
                                });
                    }
                }).toList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ImageUploadModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<ImageUploadModel> imageUploadModels) {
                        getView().onSuccessUpload();
                    }
                })
        );
    }

    private List<ImageUploadModel> parseToModel(UserIdentificationStepperModel model) {
        List<ImageUploadModel> list = new ArrayList<>();
        list.add(new ImageUploadModel(model.getKtpFile()));
        list.add(new ImageUploadModel(model.getFaceFile()));
        return list;
    }

    private RequestParams createParam(String cameraLoc) {
        Map<String, RequestBody> maps = new HashMap<String, RequestBody>();
        RequestBody webService = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody resolution = RequestBody.create(MediaType.parse("text/plain"), RESOLUTION_300);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userSession.getUserId() + UUID.randomUUID() + System.currentTimeMillis());
        maps.put(PARAM_WEB_SERVICE, webService);
        maps.put(PARAM_ID, id);
        maps.put(PARAM_RESOLUTION, resolution);
        return uploadImageUseCase.createRequestParam(cameraLoc, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, maps);
    }
}
