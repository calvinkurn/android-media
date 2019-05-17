package com.tokopedia.shop.page.view.listener;

import android.content.Context;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopPageView extends CustomerView {

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorGetShopInfo(Throwable e);

    void onSuccessGetReputation(ReputationSpeed reputationSpeed);

    void onErrorGetReputation(Throwable e);

    void onSuccessToggleFavourite(boolean successValue);

    void onErrorToggleFavourite(Throwable e);

    void onSuccessGetFeedWhitelist(Boolean isWhitelist, String createPostUrl);

    void stopPerformanceMonitor();

    void onErrorModerateListener(Throwable e);

    void onSuccessModerateListener();

    void onSuccessGetModerateInfo(ShopModerateRequestData shopModerateRequestData);

    Context getContext();

}
