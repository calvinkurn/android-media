package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogLocalRepository;
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetApplinkLogUseCase extends UseCase<List<Visitable>> {
    private ApplinkLogRepository applinkLogRepository;

    @Inject
    GetApplinkLogUseCase(ApplinkLogLocalRepository applinkLogRepository) {
        this.applinkLogRepository = applinkLogRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return applinkLogRepository.get(requestParams);
    }
}
