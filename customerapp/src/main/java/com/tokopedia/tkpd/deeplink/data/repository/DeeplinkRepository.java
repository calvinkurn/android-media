package com.tokopedia.tkpd.deeplink.data.repository;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.tkpd.deeplink.Deeplink;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 04/01/18.
 */

public interface DeeplinkRepository {

    Observable<List<Deeplink>> mapUrl();

}
