package com.tokopedia.kol.feature.createpost.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.kol.KolComponentInstance;
import com.tokopedia.kol.common.di.DaggerKolComponent;
import com.tokopedia.kol.common.di.KolComponent;
import com.tokopedia.kol.feature.createpost.di.CreatePostModule;
import com.tokopedia.kol.feature.createpost.di.DaggerCreatePostComponent;
import com.tokopedia.kol.feature.createpost.view.viewmodel.AttachmentImageModel;
import com.tokopedia.kol.feature.createpost.view.viewmodel.MediaUploadViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by yfsx on 20/06/18.
 */
public class CreatePostImageEditorActivity extends ImageEditorActivity {
    private static final String DEFAULT_RESOLUTION = "100-square";
    private static final String RESOLUTION_300 = "300";
    private static final String PARAM_ID = "id";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String PARAM_RESOLUTION = "param_resolution";
    private static final String DEFAULT_UPLOAD_PATH = "/upload/attachment";
    private static final String DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg";
    private CompositeSubscription compositeSubscription;

    @Inject
    UploadImageUseCase<AttachmentImageModel> uploadImageUseCase;

    UserSessionInterface userSession;

    private static final int CREATE_FORM_REQUEST = 1234;

    public static Intent getInstance(Context context, ArrayList<String> imageUrls,
                                    ArrayList<String> imageDescription,
                                    int minResolution,
                                    @ImageEditActionTypeDef int[] imageEditActionType,
                                    ImageRatioTypeDef defaultRatio,
                                    boolean isCirclePreview,
                                    long maxFileSize,
                                    ArrayList<ImageRatioTypeDef> ratioOptionList,
                                     String urlForm) {
        Intent intent = new Intent(context, CreatePostImageEditorActivity.class);
        intent.putExtra(EXTRA_IMAGE_URLS, imageUrls);
        intent.putExtra(EXTRA_IMAGE_DESCRIPTION_LIST, imageDescription);
        intent.putExtra(EXTRA_MIN_RESOLUTION, minResolution);
        intent.putExtra(EXTRA_EDIT_ACTION_TYPE, imageEditActionType);
        intent.putExtra(EXTRA_RATIO, defaultRatio);
        intent.putExtra(EXTRA_IS_CIRCLE_PREVIEW, isCirclePreview);
        intent.putExtra(EXTRA_MAX_FILE_SIZE, maxFileSize);
        intent.putExtra(EXTRA_RATIO_OPTION_LIST, ratioOptionList);
        intent.putExtra(CreatePostActivity.FORM_URL, urlForm);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.compositeSubscription = new CompositeSubscription();
        userSession = new UserSession(this);
        initInjector();
    }

    private void initInjector() {
        KolComponent kolComponent = DaggerKolComponent.builder().baseAppComponent(
                ((BaseMainApplication)getApplicationContext()).getBaseAppComponent())
                .build();
        DaggerCreatePostComponent.builder().kolComponent(kolComponent).build();
        DaggerCreatePostComponent.builder()
                .kolComponent(KolComponentInstance.getKolComponent(getApplication()))
                .createPostModule(new CreatePostModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onFinishEditingImage(ArrayList<String> imageUrlOrPathList) {
        uploadImage(imageUrlOrPathList);
    }

    @Override
    protected Intent getFinishIntent(ArrayList<String> imageUrlOrPathList){
        String fullPathUrl = getIntent().getStringExtra(CreatePostActivity.FORM_URL) + Uri.encode(imageUrlOrPathList.get(0));
        Intent intent = CreatePostActivity.getInstanceWebView(
                this, fullPathUrl);
        return intent;
    }

    private void uploadImage(ArrayList<String> imageUrlOrPathList) {
        progressDialog.show();
        List<MediaUploadViewModel> modelList = convertDataToModel(imageUrlOrPathList);
        compositeSubscription.add(Observable.from(modelList).flatMap((Func1<MediaUploadViewModel, Observable<MediaUploadViewModel>>) mediaUploadViewModel -> null).toList().subscribe());
        compositeSubscription.add(Observable.from(modelList)
                .flatMap((Func1<MediaUploadViewModel, Observable<MediaUploadViewModel>>) mediaUploadViewModel -> Observable.zip(Observable.just(mediaUploadViewModel),
                        uploadImageUseCase.createObservable(
                                createParam(mediaUploadViewModel.getMediaPath())
                        ), (mediaUploadViewModel1, uploadDomainModel) -> {
                            String url = uploadDomainModel.getDataResultImageUpload().getData().getPicSrc();
                            if (url.contains(DEFAULT_RESOLUTION)) {
                                url = url.replaceFirst(DEFAULT_RESOLUTION, RESOLUTION_300);
                            }
                            mediaUploadViewModel1.setMediaPath(url);
                            return mediaUploadViewModel1;
                        }))
                .toList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MediaUploadViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        NetworkErrorHelper.createSnackbarWithAction(CreatePostImageEditorActivity.this,
                                ErrorHandler.getErrorMessage(CreatePostImageEditorActivity.this, e),
                                new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                uploadImage(imageUrlOrPathList);
                            }
                        });
                    }

                    @Override
                    public void onNext(List<MediaUploadViewModel> mediaUploadViewModels) {
                        progressDialog.dismiss();
                        Intent intent = getFinishIntent(getResult(mediaUploadViewModels));
                        startActivityForResult(intent, CREATE_FORM_REQUEST);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
        super.onDestroy();
    }

    private List<MediaUploadViewModel> convertDataToModel(ArrayList<String> imageUrlOrPathList) {
        List<MediaUploadViewModel> modelList = new ArrayList<>();
        for (String mediaPath : imageUrlOrPathList) {
            MediaUploadViewModel model = new MediaUploadViewModel();
            model.setMediaPath(mediaPath);
            model.setVideo(mediaPath.contains(".mp4"));
            modelList.add(model);
        }
        return modelList;
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

    private ArrayList<String> getResult(List<MediaUploadViewModel> mediaUploadViewModels) {
        ArrayList<String> resultString = new ArrayList<>();
        resultString.add(mediaUploadViewModels.get(0).getMediaPath());
        return resultString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (requestCode == CREATE_FORM_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE);
                ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
                setResult(Activity.RESULT_OK);
                finish();
            }
        }

    }
}
