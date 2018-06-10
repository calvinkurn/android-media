package com.tokopedia.tokocash.autosweepmf.data.source;

import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;

import rx.Observable;

public interface AutoSweepDetailDataStore {
    Observable<AutoSweepDetailDomain> autoSweepDetail();
}
