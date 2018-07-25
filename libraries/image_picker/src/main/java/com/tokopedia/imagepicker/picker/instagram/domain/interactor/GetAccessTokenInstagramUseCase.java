package com.tokopedia.imagepicker.picker.instagram.domain.interactor;

import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class GetAccessTokenInstagramUseCase extends UseCase<String> {
    private InstagramRepository instagramRepository;

    @Inject
    public GetAccessTokenInstagramUseCase(InstagramRepository instagramRepository) {
        this.instagramRepository = instagramRepository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return instagramRepository.getAccessToken(requestParams.getString(InstagramConstant.CODE_KEY, ""))
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String saveAccessToken) {
                        return instagramRepository.saveAccessToken(saveAccessToken);
                    }
                });
    }

    public RequestParams createRequestParams(String code){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(InstagramConstant.CODE_KEY, code);
        return requestParams;
    }
}
