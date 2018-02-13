package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.digital.widget.view.model.category.Category;
import com.tokopedia.digital.widget.view.model.status.Status;

import java.util.List;

import rx.Subscriber;

/**
 * @author ricoharisin on 7/4/16.
 * Modified by Nabilla Sabbaha on 08/07/2017
 */
@Deprecated
public interface RechargeNetworkInteractor {

    void getCategoryData(Subscriber<List<Category>> subscriber);

    void getStatus(Subscriber<Status> subscriber);

    void onDestroy();

}
