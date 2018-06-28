package com.tokopedia.train.common;

import com.tokopedia.train.passenger.viewmodel.ProfileBuyerInfo;

import rx.Observable;

/**
 * Created by nabillasabbaha on 28/06/18.
 */
public interface TrainRouter {

    Observable<ProfileBuyerInfo> getProfileInfo();
}
