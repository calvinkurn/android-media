package com.tokopedia.tokocash.autosweepmf.data.repository;

import com.tokopedia.tokocash.autosweepmf.data.source.cloud.AutoSweepDataDetailCloud;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.AutoSweepLimitDataCloud;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.tokocash.autosweepmf.domain.repository.AutoSweepRepository;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;

public class AutoSweepRepositoryImpl implements AutoSweepRepository {
    private AutoSweepDataDetailCloud mAutoSweepDataDetailCloud;
    private AutoSweepLimitDataCloud mAutoSweepLimitDataCloud;

    @Inject
    public AutoSweepRepositoryImpl(AutoSweepDataDetailCloud autoSweepDataDetailCloud,
                                   AutoSweepLimitDataCloud autoSweepLimitDataCloud) {
        this.mAutoSweepDataDetailCloud = autoSweepDataDetailCloud;
        this.mAutoSweepLimitDataCloud = autoSweepLimitDataCloud;
    }

    @Override
    public Observable<AutoSweepDetailDomain> getAutoSweepDetail() {
        return mAutoSweepDataDetailCloud.autoSweepDetail();
    }

    @Override
    public Observable<AutoSweepLimitDomain> postAutoSweepLimit(RequestParams requestParams) {
        return mAutoSweepLimitDataCloud.autoSweepLimit(requestParams);
    }
}
