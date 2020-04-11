package com.tokopedia.kyc_centralized;

import com.google.gson.Gson;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.kyc_centralized.domain.usecase.RegisterIdentificationUseCase;
import com.tokopedia.kyc_centralized.domain.usecase.UploadIdentificationUseCase;
import com.tokopedia.kyc_centralized.util.TestSchedulerProvider;
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage;
import com.tokopedia.kyc_centralized.view.presenter.UserIdentificationUploadImagePresenter;
import com.tokopedia.kyc_centralized.view.viewmodel.AttachmentImageModel;
import com.tokopedia.kyc_centralized.view.viewmodel.ImageUploadModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.subscriptions.CompositeSubscription;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserIdentificationUploadImagePresenterTest {

    //data mock
    private UploadImageUseCase<AttachmentImageModel> uploadImageUseCase = mock(MockUploadImageUseCase.class);
    private UploadIdentificationUseCase uploadIdentificationUseCase = mock(UploadIdentificationUseCase.class);
    private RegisterIdentificationUseCase registerIdentificationUseCase = mock(RegisterIdentificationUseCase.class);
    private UserSessionInterface userSession = mock(UserSessionInterface.class);

    //presenter
    private UserIdentificationUploadImage.Presenter presenter;

    //rx utilities
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private TestSubscriber genericResponseSubscriber;

    @Before public void setUp() {
        presenter = new UserIdentificationUploadImagePresenter(uploadImageUseCase,
                uploadIdentificationUseCase,
                registerIdentificationUseCase,
                userSession,
                compositeSubscription,
                new TestSchedulerProvider());

        genericResponseSubscriber = TestSubscriber.create();
    }

    @Test public void testUploadImageUseCase() {
        ImageUploadModel imageUploadModel = imageUploadModelMock();
        Observable<ImageUploadDomainModel<AttachmentImageModel>> imageUploadModelObservable = Observable.just(new ImageUploadDomainModel<>(AttachmentImageModel.class));
        when(userSession.getUserId()).thenReturn("1");
        RequestParams params = presenter.createParam(imageUploadModel.getFilePath());
        when(uploadImageUseCase.createObservable(params)).thenReturn(imageUploadModelObservable);
        Observable<ImageUploadModel> uploadImageUseCaseObservable = presenter.uploadImageUseCase(imageUploadModel);
        uploadImageUseCaseObservable.subscribe(genericResponseSubscriber);
        genericResponseSubscriber.assertCompleted();
        genericResponseSubscriber.assertNoErrors();
    }

    private ImageUploadModel imageUploadModelMock() {
        ImageUploadModel imageUploadModel = new ImageUploadModel(0, "");
        imageUploadModel.setPicObjKyc("");
        imageUploadModel.setError("");
        imageUploadModel.setFileName("");
        imageUploadModel.setIsSuccess(0);
        return imageUploadModel;
    }

    private abstract class MockUploadImageUseCase extends UploadImageUseCase<AttachmentImageModel> {
        public MockUploadImageUseCase(UploadImageRepository uploadImageRepository,
                                      GenerateHostRepository generateHostRepository,
                                      Gson gson,
                                      UserSessionInterface userSession,
                                      Class<AttachmentImageModel> imageUploadResultModel,
                                      ImageUploaderUtils imageUploaderUtils) {
            super(uploadImageRepository, generateHostRepository, gson, userSession, imageUploadResultModel, imageUploaderUtils);
        }
    }

}