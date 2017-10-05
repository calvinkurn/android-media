package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import rx.Subscriber;

/**
 * @author ricoharisin on 7/4/16.
 * Modified by Nabilla Sabbaha on 08/07/2017
 */
public interface RechargeNetworkInteractor {

    void getCategoryData(Subscriber<CategoryData> subscriber);

    void getStatus(Subscriber<Status> subscriber);

    void getStatusResume(Subscriber<Status> subscriber);

    void onDestroy();

}
