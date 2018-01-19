package com.tokopedia.digital.common.data.source;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalEndpointService;
import com.tokopedia.digital.widget.data.entity.category.CategoryEntity;
import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.mapper.CategoryMapper;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 19/01/18.
 */

public class CategoryListDataSource {

    private DigitalEndpointService digitalEndpointService;
    private CategoryMapper categoryMapper;

    public CategoryListDataSource(DigitalEndpointService digitalEndpointService,
                                  CategoryMapper categoryMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.categoryMapper = categoryMapper;
    }

    public Observable<List<Category>> getCategoryList() {
        return digitalEndpointService.getApi().getCategoryList()
                .map(new Func1<Response<TkpdDigitalResponse>, List<CategoryEntity>>() {
                    @Override
                    public List<CategoryEntity> call(Response<TkpdDigitalResponse> tkpdDigitalResponseResponse) {
                        return tkpdDigitalResponseResponse.body().convertDataList(CategoryEntity[].class);
                    }
                })
                .map(categoryMapper);
    }

}
