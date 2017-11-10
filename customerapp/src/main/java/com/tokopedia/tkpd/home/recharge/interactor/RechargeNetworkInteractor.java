package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.status.Status;

import java.util.List;

import rx.Subscriber;

/**
 * @author ricoharisin on 7/4/16.
 * Modified by Nabilla Sabbaha on 08/07/2017
 */
public interface RechargeNetworkInteractor {

    void getCategoryData(Subscriber<List<Category>> subscriber, boolean isUseCache);

    void getStatus(Subscriber<Status> subscriber);

    void onDestroy();

}
