package com.tokopedia.product.manage.item.category.data.source.cloud;

import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.MerlinApi;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.request.CategoryRecommRequest;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.request.Data;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.request.Parcel;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommdata.CategoryRecommDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class CategoryRecommCloud {
    private final MerlinApi api;

    @Inject
    public CategoryRecommCloud(MerlinApi api) {
        this.api = api;
    }


    public Observable<CategoryRecommDataModel> fetchData(String title,
                                                         int row) {
        List<Parcel> parcelList = new ArrayList<>();
        parcelList.add(new Parcel(new Data(title)));

        CategoryRecommRequest request = new CategoryRecommRequest(parcelList, row);

        return api.getCategoryRecomm(request);
    }
}
