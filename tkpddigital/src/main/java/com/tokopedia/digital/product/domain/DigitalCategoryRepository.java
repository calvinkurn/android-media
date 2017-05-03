package com.tokopedia.digital.product.domain;

import android.support.annotation.NonNull;

import com.tokopedia.core.network.apiservices.digital.DigitalEndpointService;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.data.entity.ResponseBanner;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.product.data.mapper.IProductDigitalMapper;
import com.tokopedia.digital.product.model.BannerData;
import com.tokopedia.digital.product.model.CategoryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class DigitalCategoryRepository implements IDigitalCategoryRepository {
    private final DigitalEndpointService digitalEndpointService;
    private final IProductDigitalMapper productDigitalMapper;

    public DigitalCategoryRepository(DigitalEndpointService digitalEndpointService,
                                     IProductDigitalMapper productDigitalMapper) {
        this.digitalEndpointService = digitalEndpointService;
        this.productDigitalMapper = productDigitalMapper;
    }

    @Override
    public Observable<CategoryData> getCategory(
            String categoryId, TKPDMapParam<String, String> param
    ) {
        return digitalEndpointService.getApi()
                .getCategory(categoryId, param)
                .map(getFuncTransformCategoryData());
    }

    @Override
    public Observable<List<BannerData>> getBanner(
            TKPDMapParam<String, String> param
    ) {
        return digitalEndpointService.getApi()
                .getBanner(param)
                .map(getFuncTransformBannerListData());
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

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, List<BannerData>> getFuncTransformBannerListData() {
        return new Func1<Response<TkpdDigitalResponse>, List<BannerData>>() {
            @Override
            public List<BannerData> call(
                    Response<TkpdDigitalResponse> tkpdDigitalResponseResponse
            ) {
                List<BannerData> bannerDataList = new ArrayList<>();
                List<ResponseBanner> responseBannerList =
                        tkpdDigitalResponseResponse.body()
                                .convertDataList(ResponseBanner[].class);
                for (ResponseBanner data : responseBannerList) {
                    bannerDataList.add(productDigitalMapper.transformBannerData(data));
                }
                return bannerDataList;
            }
        };
    }
}
