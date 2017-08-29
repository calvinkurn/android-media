package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.data.repository.ShopRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListUseCase extends UseCase<ProductModel> {
    ShopRepository shopRepository;

    public GetProductListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ShopRepository shopRepository) {
        super(threadExecutor, postExecutionThread);

        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ProductModel> createObservable(RequestParams requestParams) {
        return shopRepository.getProductList(requestParams);
    }
}
