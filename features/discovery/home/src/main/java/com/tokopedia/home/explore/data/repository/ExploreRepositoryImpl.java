package com.tokopedia.home.explore.data.repository;

import android.content.Context;

import com.tokopedia.home.explore.data.source.ExploreDataSource;
import com.tokopedia.home.explore.view.adapter.viewmodel.ExploreSectionViewModel;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public class ExploreRepositoryImpl implements ExploreRepository {

    private ExploreDataSource dataSource;
    private Context context;

    public ExploreRepositoryImpl(Context context, ExploreDataSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public Observable<List<ExploreSectionViewModel>> getExploreData(String userId) {
        return dataSource.getExploreData(userId);
    }

    public Observable<List<ExploreSectionViewModel>> getExploreDataCache() {
        return dataSource.getDataCache();
    }
}
