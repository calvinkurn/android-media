package com.tokopedia.imagepicker.picker.instagram.domain.interactor;

import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class SaveCookiesInstagramUseCase extends UseCase<String> {
    private InstagramRepository instagramRepository;

    @Inject
    public SaveCookiesInstagramUseCase(InstagramRepository instagramRepository) {
        this.instagramRepository = instagramRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return instagramRepository.saveCookies(requestParams.getString(InstagramConstant.COOKIES_KEY, ""));
    }

    public static RequestParams createRequestParams(String cookies){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(InstagramConstant.COOKIES_KEY, cookies);
        return requestParams;
    }
}
