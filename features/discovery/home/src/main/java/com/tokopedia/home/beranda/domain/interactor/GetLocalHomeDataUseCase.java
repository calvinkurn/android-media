package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by henrypriyono on 01/02/18.
 */

public class GetLocalHomeDataUseCase extends UseCase<HomeViewModel> {

    private final HomeRepository homeRepository;

    public GetLocalHomeDataUseCase(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<HomeViewModel> createObservable(RequestParams requestParams) {
//        return homeRepository.getHomeDataCache();
        return Observable.just(null);
    }
}

