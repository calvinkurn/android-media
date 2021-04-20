package com.tokopedia.home.explore.data.repository;

import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel;

import java.util.List;

import rx.Observable;

/**
 * Created by errysuprayogi on 2/2/18.
 */

public interface ExploreRepository {

    Observable<List<ExploreSectionDataModel>> getExploreData(String userId);

}
