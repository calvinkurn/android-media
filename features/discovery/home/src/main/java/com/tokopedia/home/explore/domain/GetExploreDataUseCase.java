package com.tokopedia.home.explore.domain;

import android.content.Context;

import com.tokopedia.home.constant.ConstantKey;
import com.tokopedia.home.explore.data.repository.ExploreRepositoryImpl;
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class GetExploreDataUseCase extends UseCase<List<ExploreSectionDataModel>> {

    private final ExploreRepositoryImpl repository;
    private final Context context;

    public GetExploreDataUseCase(ExploreRepositoryImpl repository, Context context) {
        this.repository = repository;
        this.context = context;
    }

    @Override
    public Observable<List<ExploreSectionDataModel>> createObservable(RequestParams requestParams) {
        String userId = requestParams.getString(ConstantKey.RequestKey.USER_ID,
                ConstantKey.RequestKey.DEFAULT_USER_ID);
        return repository.getExploreData(userId);
    }
}
