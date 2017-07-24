package com.tokopedia.core.manage.people.bank.domain;

import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;

import rx.Observable;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public interface IBcaOneClickRepository {

    Observable<BcaOneClickData> getBcaOneClickAccessToken();

}
