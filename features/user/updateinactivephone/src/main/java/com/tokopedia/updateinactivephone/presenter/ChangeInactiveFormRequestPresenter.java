package com.tokopedia.updateinactivephone.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;

import javax.inject.Inject;

public class ChangeInactiveFormRequestPresenter extends BaseDaggerPresenter<ChangeInactiveFormRequest.View>
        implements ChangeInactiveFormRequest.Presenter  {

    private String photoIdImagePath;
    private String accountImagePath;

    @Inject
    public ChangeInactiveFormRequestPresenter() {

    }


    public void setPhotoIdImagePath(String imagePath) {
        this.photoIdImagePath = imagePath;
    }

    public void uploadPhotoIdImage() {
        getView().showLoading();
    }

    public boolean isValidPhotoIdPath() {
        return !TextUtils.isEmpty(photoIdImagePath);
    }

    public void setAccountPhotoImagePath(String imagePath) {
        this.accountImagePath = imagePath;
    }
}
