package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.model.data.Etalase;
import com.tokopedia.topads.dashboard.domain.TopAdsEtalaseListRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsEtalaseListUseCase extends UseCase<List<Etalase>> {

    public static final String SHOP_ID = "shop_id";
    public static final String USER_ID = "user_id";
    public static final String DEVICE_ID = "device_id";
    private final TopAdsEtalaseListRepository topAdsEtalaseListRepository;

    public TopAdsEtalaseListUseCase(TopAdsEtalaseListRepository topAdsEtalaseListRepository) {
        super();
        this.topAdsEtalaseListRepository = topAdsEtalaseListRepository;
    }

    @Override
    public Observable<List<Etalase>> createObservable(RequestParams requestParams) {
        return topAdsEtalaseListRepository.getEtalaseList(requestParams.getString(SHOP_ID, ""),
                requestParams.getString(USER_ID, ""),
                requestParams.getString(DEVICE_ID, ""));
    }

    public static RequestParams createRequestParams(String shopId, String userId, String deviceId) {
        RequestParams params = RequestParams.create();
        params.putString(SHOP_ID, shopId);
        params.putString(USER_ID, userId);
        params.putString(DEVICE_ID, deviceId);
        return params;
    }
}
