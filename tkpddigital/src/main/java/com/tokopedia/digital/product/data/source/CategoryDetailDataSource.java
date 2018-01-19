package com.tokopedia.digital.product.data.source;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.apiservice.DigitalEndpointService;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.product.data.mapper.ProductDigitalMapper;
import com.tokopedia.digital.product.model.CategoryData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 19/01/18.
 */

public class CategoryDetailDataSource {

    private DigitalEndpointService digitalEndpointService;
    private ProductDigitalMapper productDigitalMapper;

    public CategoryDetailDataSource(DigitalEndpointService digitalEndpointService,
                                    ProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.productDigitalMapper = productDigitalMapper;
    }

    public Observable<CategoryData> getCategory(String categoryId, TKPDMapParam<String, String> param) {
        return getDataFromCloud(categoryId, param);
    }

    private Observable<CategoryData> getDataFromCloud(String categoryId, TKPDMapParam<String, String> param) {
        return digitalEndpointService.getApi()
                .getCategory(categoryId, param)
                .map(getFuncTransformCategoryData());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CategoryData> getFuncTransformCategoryData() {
        return new Func1<Response<TkpdDigitalResponse>, CategoryData>() {
            @Override
            public CategoryData call(
                    Response<TkpdDigitalResponse> response
            ) {
                return productDigitalMapper.transformCategoryData(
                        response.body().convertDataObj(ResponseCategoryDetailData.class),
                        response.body().convertIncludedList(ResponseCategoryDetailIncluded[].class)
                );
            }
        };
    }

}
