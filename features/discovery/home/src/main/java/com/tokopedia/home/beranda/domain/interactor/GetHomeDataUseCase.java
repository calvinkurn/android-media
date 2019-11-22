package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class GetHomeDataUseCase extends UseCase<HomeViewModel> {
    private final HomeRepository repository;

    public GetHomeDataUseCase(HomeRepository homeRepository) {
        this.repository = homeRepository;
    }

    @Override
    public Observable<HomeViewModel> createObservable(RequestParams requestParams) {
//        return repository.getAllHomeData();
        return Observable.just(null);
    }
}
