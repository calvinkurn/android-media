package com.tokopedia.tokocash.autosweepmf.data.source;


import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface AutoSweepLimitDataStore {
    Observable<AutoSweepLimitDomain> autoSweepLimit(RequestParams requestParams);
}
