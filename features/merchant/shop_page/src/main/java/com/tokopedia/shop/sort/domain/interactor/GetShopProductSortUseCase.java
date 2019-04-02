package com.tokopedia.shop.sort.domain.interactor;

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort;
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductSortUseCase extends UseCase<List<ShopProductSort>> {

    private ShopProductSortRepository shopProductFilterRepository;

    @Inject
    public GetShopProductSortUseCase(ShopProductSortRepository shopProductFilterRepository) {
        this.shopProductFilterRepository = shopProductFilterRepository;
    }

    @Override
    public Observable<List<ShopProductSort>> createObservable(RequestParams requestParams) {
        return shopProductFilterRepository.getShopProductFilter();
    }
}
