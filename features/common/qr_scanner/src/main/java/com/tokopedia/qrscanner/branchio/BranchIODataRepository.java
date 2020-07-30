package com.tokopedia.qrscanner.branchio;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by sandeepgoyal on 19/03/18.
 */

public interface BranchIODataRepository {

    Observable<BranchIOAndroidDeepLink> getDeepLink(HashMap<String, Object> params);
}
