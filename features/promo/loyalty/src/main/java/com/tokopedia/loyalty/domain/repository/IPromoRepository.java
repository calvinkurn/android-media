package com.tokopedia.loyalty.domain.repository;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import rx.Observable;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public interface IPromoRepository {

    Observable<List<PromoMenuData>> getPromoMenuDataList(TKPDMapParam<String, String> param);

    Observable<List<PromoData>> getPromoDataList(TKPDMapParam<String, String> param);
}
