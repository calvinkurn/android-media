package com.tokopedia.imagepicker.picker.instagram.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.GetListMediaInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaResponse;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class ImagePickerInstagramPresenter extends BaseDaggerPresenter<ImagePickerInstagramContract.View> implements ImagePickerInstagramContract.Presenter {

    private GetListMediaInstagramUseCase getListMediaInstagramUseCase;

    public ImagePickerInstagramPresenter(GetListMediaInstagramUseCase getListMediaInstagramUseCase) {
        this.getListMediaInstagramUseCase = getListMediaInstagramUseCase;
    }

    @Override
    public void getListMediaInstagram(String code, String nextMediaId) {
        getListMediaInstagramUseCase.execute(getListMediaInstagramUseCase.createRequestParams(code, nextMediaId), getSubscriberListMediaInstagram());
    }

    private Subscriber<InstagramMediaResponse> getSubscriberListMediaInstagram() {
        return new Subscriber<InstagramMediaResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof ShouldLoginInstagramException){
                    getView().onErrorShouldLoginInstagram();
                }else{
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(InstagramMediaResponse instagramMediaResponse) {
                getView().renderList(instagramMediaResponse.getInstagramMediaModels(),
                        !TextUtils.isEmpty(instagramMediaResponse.getNextMaxIdPage()), instagramMediaResponse.getNextMaxIdPage());
            }
        };
    }
}
