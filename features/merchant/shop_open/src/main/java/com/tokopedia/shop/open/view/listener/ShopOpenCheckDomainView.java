package com.tokopedia.shop.open.view.listener;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public interface ShopOpenCheckDomainView extends CustomerView {

    void onSuccessCheckReserveDomain(ResponseIsReserveDomain object);

    void onErrorCheckReserveDomain(Throwable t);

    Activity getActivity();
}