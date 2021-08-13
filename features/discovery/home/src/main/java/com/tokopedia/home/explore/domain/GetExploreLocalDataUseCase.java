package com.tokopedia.home.explore.domain;

import com.tokopedia.home.explore.data.repository.ExploreRepositoryImpl;
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/5/18.
 */

public class GetExploreLocalDataUseCase extends UseCase<List<ExploreSectionDataModel>> {

    private final ExploreRepositoryImpl repository;

    public GetExploreLocalDataUseCase(ExploreRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<ExploreSectionDataModel>> createObservable(RequestParams requestParams) {
        return repository.getExploreDataCache();
    }

}
