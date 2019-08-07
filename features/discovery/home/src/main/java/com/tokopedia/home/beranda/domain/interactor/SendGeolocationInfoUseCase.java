package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.presentation.view.adapter.TrackedVisitable;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by devarafikry on 27/06/19.
 */

public class SendGeolocationInfoUseCase extends UseCase<Response<String>> {
    private final HomeRepository repository;

    public SendGeolocationInfoUseCase(HomeRepository homeRepository) {
        this.repository = homeRepository;
    }

    @Override
    public Observable<Response<String>> createObservable(RequestParams requestParams) {
        return repository.sendGeolocationInfo();
    }
}
