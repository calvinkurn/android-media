package com.tokopedia.review.feature.reputationhistory.data.source.cloud;

import com.tokopedia.review.feature.reputationhistory.data.mapper.ReputationReviewMapper;
import com.tokopedia.review.feature.reputationhistory.data.model.response.SellerReputationResponse;
import com.tokopedia.review.feature.reputationhistory.data.source.ReputationReviewDataSource;
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.apiservice.api.SellerReputationApi;

import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationUseCase.RequestParamFactory.KEY_REVIEW_REPUTATION_PARAM;


/**
 * @author normansyahputa on 3/16/17.
 */

public class CloudReputationReviewDataSource implements ReputationReviewDataSource {

    private SellerReputationApi sellerReputationApi;

    @Inject
    public CloudReputationReviewDataSource(
            SellerReputationApi sellerReputationApi) {
        this.sellerReputationApi = sellerReputationApi;
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(String shopId, Map<String, String> param) {
        return sellerReputationApi.getReputationHistory(shopId, param)
                .map(new Func1<Response<SellerReputationResponse>, SellerReputationDomain>() {
                    @Override
                    public SellerReputationDomain call(Response<SellerReputationResponse> sellerReputationResponseResponse) {
                        return ReputationReviewMapper.getSellerReputationDomains(sellerReputationResponseResponse.body());
                    }
                });
    }

    @Override
    public Observable<SellerReputationDomain> getReputationHistory(RequestParams requestParams) {
        return sellerReputationApi.getReputationHistory(
                requestParams.getString(ShopNetworkController.RequestParamFactory.KEY_SHOP_ID, ""),
                (Map<String, String>) requestParams.getObject(KEY_REVIEW_REPUTATION_PARAM))
                .map(new Func1<Response<SellerReputationResponse>, SellerReputationDomain>() {
                    @Override
                    public SellerReputationDomain call(Response<SellerReputationResponse> sellerReputationResponseResponse) {
                        return ReputationReviewMapper.getSellerReputationDomains(sellerReputationResponseResponse.body());
                    }
                });
    }
}
