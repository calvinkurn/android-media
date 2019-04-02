package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public class TopAdsMoveProductGroupToExistGroupUseCase extends UseCase<Boolean> {
    private static final String AD_ID = "AD_ID";
    private static final String GROUP_ID = "GROUP_ID";
    private static final String SHOP_ID = "SHOP_ID";

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    public TopAdsMoveProductGroupToExistGroupUseCase(TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super();
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.moveProductGroup(requestParams.getString(AD_ID, ""),
                requestParams.getString(GROUP_ID, ""), requestParams.getString(SHOP_ID, ""))
                .map(new Func1<ProductAdBulkAction, Boolean>() {
                    @Override
                    public Boolean call(ProductAdBulkAction productAdBulkAction) {
                        if(productAdBulkAction != null){
                            return true;
                        }else{
                            return false;
                        }
                    }
                });
    }

    public static RequestParams createRequestParams(String adId, String groupId, String shopId) {
        RequestParams params = RequestParams.create();
        params.putString(AD_ID, adId);
        params.putString(GROUP_ID, groupId);
        params.putString(SHOP_ID, shopId);
        return params;
    }
}
