package com.tokopedia.core.analytics.data;

import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.base.domain.RequestParams;

import rx.Observable;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public interface UserAttributesRepository {

    Observable<UserAttribute.Data> getUserAttributes(RequestParams parameters);

}
