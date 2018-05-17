package com.tokopedia.imagepicker.picker.instagram.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.GetListMediaInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.data.source.exception.ShouldLoginInstagramException;
import com.tokopedia.imagepicker.picker.instagram.domain.interactor.SaveCookiesInstagramUseCase;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaResponse;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class ImagePickerInstagramPresenter extends BaseDaggerPresenter<ImagePickerInstagramContract.View> implements ImagePickerInstagramContract.Presenter {

    private GetListMediaInstagramUseCase getListMediaInstagramUseCase;
    private SaveCookiesInstagramUseCase saveCookiesInstagramUseCase;

    public ImagePickerInstagramPresenter(GetListMediaInstagramUseCase getListMediaInstagramUseCase,
                                         SaveCookiesInstagramUseCase saveCookiesInstagramUseCase) {
        this.getListMediaInstagramUseCase = getListMediaInstagramUseCase;
        this.saveCookiesInstagramUseCase = saveCookiesInstagramUseCase;
    }

    @Override
    public void getListMediaInstagram(String code, String nextMediaId) {
        getListMediaInstagramUseCase.execute(getListMediaInstagramUseCase.createRequestParams(code, nextMediaId), getSubscriberListMediaInstagram());
    }

    @Override
    public void saveCookies(String cookies) {
        saveCookiesInstagramUseCase.execute(SaveCookiesInstagramUseCase.createRequestParams(cookies), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        });
    }

    private Subscriber<InstagramMediaResponse> getSubscriberListMediaInstagram() {
        return new Subscriber<InstagramMediaResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showGetListError(e);
            }

            @Override
            public void onNext(InstagramMediaResponse instagramMediaResponse) {
                getView().renderList(instagramMediaResponse.getInstagramMediaModels(),
                        !TextUtils.isEmpty(instagramMediaResponse.getNextMaxIdPage()), instagramMediaResponse.getNextMaxIdPage());
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        saveCookiesInstagramUseCase.unsubscribe();
        getListMediaInstagramUseCase.unsubscribe();
    }
}
