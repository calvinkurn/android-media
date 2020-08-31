package com.tokopedia.loyalty.view.interactor;


import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author anggaprasetiyo on 03/01/18.
 */

public interface IPromoInteractor {

    void getPromoMenuList(TKPDMapParam<String, String> param, Subscriber<List<PromoMenuData>> subscriber);

    Observable<List<PromoData>> getPromoList(TKPDMapParam<String, String> param, Subscriber<List<PromoData>> subscriber);

}
