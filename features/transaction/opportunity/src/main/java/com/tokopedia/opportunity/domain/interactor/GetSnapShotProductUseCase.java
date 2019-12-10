package com.tokopedia.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.opportunity.data.mapper.OpportunityProductMapper;
import com.tokopedia.opportunity.domain.entity.OpportunityDetail;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.opportunity.domain.repository.ProductRepository;
import com.tokopedia.opportunity.domain.repository.ReplacementRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by nakama on 30/03/18.
 */

public class GetSnapShotProductUseCase extends UseCase<ProductDetailData> {
    private static final String PARAM_REPLACEMENT_ID = "replacement_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_PRODUCT_KEY = "product_key";
    private static final String PARAM_SHOP_DOMAIN = "shop_domain";

    private final ReplacementRepository replacementRepository;
    private final ProductRepository productRepository;

    @Inject
    public GetSnapShotProductUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ReplacementRepository replacementRepository,
                                     ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.replacementRepository = replacementRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductDetailData> createObservable(RequestParams requestParams) {
        return Observable.zip(productRepository.getProduct(getProductParams(requestParams)),
                replacementRepository.getOpportunityDetail(getOpportunityParams(requestParams)),
                new OpportunityProductMapper());
    }

    public static RequestParams getRequestParams(ProductPass productPass, String opportunityId){
        RequestParams params = RequestParams.create();
        params.putString(PARAM_REPLACEMENT_ID, opportunityId);
        params.putString(PARAM_PRODUCT_ID, productPass.getProductId());
        params.putString(PARAM_PRODUCT_KEY, productPass.getProductKey());
        params.putString(PARAM_SHOP_DOMAIN, productPass.getShopDomain());
        return params;
    }

    private RequestParams getOpportunityParams(RequestParams params){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_REPLACEMENT_ID, params.getString(PARAM_REPLACEMENT_ID, ""));
        return requestParams;
    }

    private RequestParams getProductParams(RequestParams params){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PRODUCT_ID, params.getString(PARAM_PRODUCT_ID, ""));
        requestParams.putString(PARAM_PRODUCT_KEY, params.getString(PARAM_PRODUCT_KEY, ""));
        requestParams.putString(PARAM_SHOP_DOMAIN, params.getString(PARAM_SHOP_DOMAIN, ""));
        return requestParams;
    }
}
