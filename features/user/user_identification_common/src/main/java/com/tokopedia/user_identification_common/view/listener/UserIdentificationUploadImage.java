package com.tokopedia.user_identification_common.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber;
import com.tokopedia.user_identification_common.view.viewmodel.ImageUploadModel;
import com.tokopedia.user_identification_common.view.viewmodel.UserIdentificationStepperModel;

import java.util.List;

import rx.Observable;

/**
 * @author by alvinatin on 21/11/18.
 */

public interface UserIdentificationUploadImage  {
    interface View extends CustomerView {
        Context getContext();

        void onSuccessUpload();

        void onErrorUpload(String error);

        GetKtpStatusSubscriber.GetKtpStatusListener getKtpStatusListener();

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void uploadImage(UserIdentificationStepperModel model, int projectid);
        void checkKtp(String image);

        Observable<ImageUploadModel> uploadImageUseCase(ImageUploadModel imageUploadModel);
        Observable<ImageUploadModel> uploadIdentificationUseCase(List<ImageUploadModel> imageUploadModels, int projectId);
        Observable<Boolean> registerIdentificationUseCase(int projectId);
        Observable<Boolean> isAllMutationSuccess(List<ImageUploadModel> imageUploadModels);
        RequestParams createParam(String cameraLoc);
    }
}
