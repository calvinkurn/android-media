package com.tokopedia.imagepicker.picker.instagram.domain.interactor;

import com.tokopedia.imagepicker.picker.instagram.data.model.ResponseListMediaInstagram;
import com.tokopedia.imagepicker.picker.instagram.domain.InstagramRepository;
import com.tokopedia.imagepicker.picker.instagram.domain.mapper.InstagramMediaMapper;
import com.tokopedia.imagepicker.picker.instagram.util.InstagramConstant;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class GetListMediaInstagramUseCase extends UseCase<InstagramMediaResponse> {
    private InstagramRepository instagramRepository;
    private InstagramMediaMapper instagramMediaMapper;
    private GetAccessTokenInstagramUseCase getAccessTokenInstagramUseCase;

    @Inject
    public GetListMediaInstagramUseCase(InstagramRepository instagramRepository,
                                        InstagramMediaMapper instagramMediaMapper,
                                        GetAccessTokenInstagramUseCase getAccessTokenInstagramUseCase) {
        this.instagramRepository = instagramRepository;
        this.instagramMediaMapper = instagramMediaMapper;
        this.getAccessTokenInstagramUseCase = getAccessTokenInstagramUseCase;
    }

    @Override
    public Observable<InstagramMediaResponse> createObservable(final RequestParams requestParams) {
        return getAccessTokenInstagramUseCase.createObservable(
                getAccessTokenInstagramUseCase.createRequestParams(requestParams.getString(InstagramConstant.CODE_KEY, "")))
                .flatMap(new Func1<String, Observable<InstagramMediaResponse>>() {
                    @Override
                    public Observable<InstagramMediaResponse> call(String accessToken) {
                        return instagramRepository.getListMedia(accessToken,
                                requestParams.getString(InstagramConstant.NEXT_MAX_ID_KEY, ""),
                                InstagramConstant.PER_PAGE_MEDIA)
                                .map(new Func1<ResponseListMediaInstagram, InstagramMediaResponse>() {
                                    @Override
                                    public InstagramMediaResponse call(ResponseListMediaInstagram responseListMediaInstagram) {
                                        return instagramMediaMapper.convertToInstagramMediaModel(responseListMediaInstagram);
                                    }
                                });
                    }
                });
    }

    public RequestParams createRequestParams(String code, String nextMediaId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(InstagramConstant.NEXT_MAX_ID_KEY, nextMediaId);
        requestParams.putString(InstagramConstant.CODE_KEY, code);
        return requestParams;
    }
}
