package com.tokopedia.home.beranda.domain.interactor;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by henrypriyono on 01/02/18.
 */

public class GetLocalHomeDataUseCase extends UseCase<List<Visitable>> {

    private final HomeRepository homeRepository;

    public GetLocalHomeDataUseCase(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return homeRepository.getHomeDataCache();
    }
}

