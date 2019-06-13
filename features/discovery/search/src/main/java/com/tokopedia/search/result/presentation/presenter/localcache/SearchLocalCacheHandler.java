package com.tokopedia.search.result.presentation.presenter.localcache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.newdynamicfilter.helper.DynamicFilterDbManager;

import java.lang.reflect.Type;
import java.util.List;

public class SearchLocalCacheHandler {

    // Save DynamicFilterModel locally as temporary solution,
    // to prevent TransactionTooLargeException when opening RevampedDynamicFilterActivity
    // This method will not be tested with Unit Test
    public void saveDynamicFilterModelLocally(String screenNameId, DynamicFilterModel dynamicFilterModel) {
        Type listType = new TypeToken<List<Filter>>() { }.getType();
        Gson gson = new Gson();
        String filterData = gson.toJson(dynamicFilterModel.getData().getFilter(), listType);

        DynamicFilterDbManager cache = new DynamicFilterDbManager();
        cache.setFilterID(screenNameId);
        cache.setFilterData(filterData);
        cache.store();
    }
}
