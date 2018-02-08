package com.tokopedia.core.drawer2.domain;

import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public interface TokoCashRepository {

    Observable<TokoCashModel> getTokoCashFromNetwork(TKPDMapParam<String, Object> params);

    Observable<TokoCashModel> getTokoCashFromLocal();


}