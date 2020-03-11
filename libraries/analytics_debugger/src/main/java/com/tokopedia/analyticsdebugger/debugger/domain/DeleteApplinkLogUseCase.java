package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogLocalRepository;
import com.tokopedia.analyticsdebugger.debugger.data.repository.ApplinkLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DeleteApplinkLogUseCase extends UseCase<Boolean> {
    private ApplinkLogRepository applinkLogRepository;

    @Inject
    DeleteApplinkLogUseCase(ApplinkLogLocalRepository applinkLogRepository) {
        this.applinkLogRepository = applinkLogRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return applinkLogRepository.removeAll();
    }
}
