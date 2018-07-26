package com.tokopedia.shop.etalase.data.repository;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.etalase.data.source.cloud.ShopEtalaseCloudDataSource;
import com.tokopedia.shop.etalase.data.source.cloud.model.EtalaseModel;
import com.tokopedia.shop.etalase.data.source.cloud.model.PagingListOther;
import com.tokopedia.shop.etalase.domain.model.ShopEtalaseRequestModel;
import com.tokopedia.shop.etalase.domain.repository.ShopEtalaseRepository;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nathan on 2/15/18.
 */

public class ShopEtalaseRepositoryImpl implements ShopEtalaseRepository {

    private ShopEtalaseCloudDataSource shopEtalaseCloudDataSource;

    @Inject
    public ShopEtalaseRepositoryImpl(ShopEtalaseCloudDataSource shopEtalaseCloudDataSource) {
        this.shopEtalaseCloudDataSource = shopEtalaseCloudDataSource;
    }

    @Override
    public Observable<PagingListOther<EtalaseModel>> getShopEtalaseList(ShopEtalaseRequestModel shopProductRequestModel) {
        return shopEtalaseCloudDataSource.getShopEtalaseList(shopProductRequestModel)
                .map(new Func1<Response<DataResponse<PagingListOther<EtalaseModel>>>, PagingListOther<EtalaseModel>>() {
                    @Override
                    public PagingListOther<EtalaseModel> call(Response<DataResponse<PagingListOther<EtalaseModel>>> dataResponseResponse) {
                        return dataResponseResponse.body().getData();
                    }
                });
    }
}
