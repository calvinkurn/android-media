package com.tokopedia.digital.newcart.data;

import com.tokopedia.digital.newcart.domain.DigitalDealsRepository;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.digital.newcart.domain.model.mapper.DealCategoryViewModelMapper;
import com.tokopedia.digital.newcart.domain.model.mapper.DealProductViewModelMapper;

import java.util.List;
import java.util.Map;

import rx.Observable;

public class DigitalDealsRepositoryImpl implements DigitalDealsRepository {
    private DigitalDealsDataSourceFactory dataSourceFactory;
    private DealCategoryViewModelMapper mapper;
    private DealProductViewModelMapper productViewModelMapper;

    public DigitalDealsRepositoryImpl(DigitalDealsDataSourceFactory dataSourceFactory,
                                      DealCategoryViewModelMapper mapper,
                                      DealProductViewModelMapper productViewModelMapper) {
        this.dataSourceFactory = dataSourceFactory;
        this.mapper = mapper;
        this.productViewModelMapper = productViewModelMapper;
    }


    @Override
    public Observable<List<DealCategoryViewModel>> getDealsCategory(Map<String, Object> params) {
        return dataSourceFactory.getCategories(params)
                .map(entities -> mapper.transform(entities));
    }

    @Override
    public Observable<DealProductsViewModel> getProducts(String url, String categoryName) {
        return dataSourceFactory.getProducts(url)
                .map(dealProductsResponse ->
                        productViewModelMapper.transformDealProduct(dealProductsResponse, categoryName)
                );
    }
}
