package com.tokopedia.digital.newcart.data;

import com.tokopedia.digital.newcart.data.entity.DealCategoryEntity;
import com.tokopedia.digital.newcart.data.entity.DealProductsResponse;
import com.tokopedia.digital.newcart.domain.DigitalDealsRepository;
import com.tokopedia.digital.newcart.domain.model.DealCategoryViewModel;
import com.tokopedia.digital.newcart.domain.model.DealProductsViewModel;
import com.tokopedia.digital.newcart.domain.model.mapper.DealCategoryViewModelMapper;
import com.tokopedia.digital.newcart.domain.model.mapper.DealProductViewModelMapper;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

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
                .map(new Func1<List<DealCategoryEntity>, List<DealCategoryViewModel>>() {
                    @Override
                    public List<DealCategoryViewModel> call(List<DealCategoryEntity> entities) {
                        return mapper.transform(entities);
                    }
                });
    }

    @Override
    public Observable<DealProductsViewModel> getProducts(String url) {
        return dataSourceFactory.getProducts(url)
                .map(new Func1<DealProductsResponse, DealProductsViewModel>() {
                    @Override
                    public DealProductsViewModel call(DealProductsResponse dealProductsResponse) {
                        return productViewModelMapper.transformDealProduct(dealProductsResponse);
                    }
                });
    }
}
