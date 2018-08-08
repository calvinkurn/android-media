package com.tokopedia.tkpd.deeplink.source;


import com.tokopedia.tkpd.deeplink.source.entity.BranchIOAndroidDeepLink;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public interface BranchIODataRepository {

    Observable<BranchIOAndroidDeepLink> getDeepLink(HashMap<String, Object> params);
}
