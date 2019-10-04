package com.tokopedia.gm.common.domain.repository;

import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;

import java.util.List;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface GMCommonRepository {

    Observable<Boolean> setCashback(String string, int cashback);

    Observable<List<GMGetCashbackModel>> getCashbackList(List<String> productIdList, String shopId);


}