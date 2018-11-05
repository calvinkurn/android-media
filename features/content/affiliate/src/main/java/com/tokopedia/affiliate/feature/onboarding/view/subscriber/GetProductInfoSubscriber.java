package com.tokopedia.affiliate.feature.onboarding.view.subscriber;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.viewmodel.RecommendProductViewModel;
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
        .TopAdsPdpAffiliate;
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
        .TopAdsPdpAffiliate.Data.PdpAffiliate;

import rx.Subscriber;

/**
 * @author by milhamj on 10/16/18.
 */
public class GetProductInfoSubscriber extends Subscriber<TopAdsPdpAffiliate> {
    private final RecommendProductContract.View view;

    public GetProductInfoSubscriber(RecommendProductContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }
        view.hideLoading();
        view.onErrorGetProductInfo(ErrorHandler.getErrorMessage(view.getContext(), e));
    }

    @Override
    public void onNext(TopAdsPdpAffiliate topAdsPdpAffiliate) {
        view.hideLoading();
        if (topAdsPdpAffiliate != null && topAdsPdpAffiliate.getData() != null
                && topAdsPdpAffiliate.getData().getAffiliate() != null
                && !topAdsPdpAffiliate.getData().getAffiliate().isEmpty()
                && topAdsPdpAffiliate.getData().getAffiliate().get(0)  != null) {

            PdpAffiliate pdpAffiliate = topAdsPdpAffiliate.getData().getAffiliate().get(0);
            view.onSuccessGetProductInfo(new RecommendProductViewModel(
                    pdpAffiliate.getAdTitle(),
                    pdpAffiliate.getImage(),
                    pdpAffiliate.getCommissionValueDisplay(),
                    String.valueOf(pdpAffiliate.getAdId())
            ));
        } else {
            onError(new RuntimeException());
        }
    }
}
