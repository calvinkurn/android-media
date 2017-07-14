package com.tokopedia.core.drawer2.domain;

import com.tokopedia.core.drawer2.data.pojo.toppoints.TopPointsModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface TopPointsRepository {

    Observable<TopPointsModel> getTopPointsFromNetwork(TKPDMapParam<String, Object> params);

    Observable<TopPointsModel> getTopPointsFromLocal();


}
