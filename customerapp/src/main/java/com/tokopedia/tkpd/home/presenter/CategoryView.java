package com.tokopedia.tkpd.home.presenter;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.entity.home.Banner;
import com.tokopedia.digital.tokocash.model.CashBackData;

/**
 * Created by ricoharisin on 3/29/16.
 */
public interface CategoryView {

    void onSuccessFetchBanners(Banner banner);

    void onReceivePendingCashBack(CashBackData cashBackData);

    void onSuccessFetchTokoCashDataFromCache(TokoCashModel tokoCashModel);

    void onErrorFetchTokoCashDataFromCache(String message);

}
