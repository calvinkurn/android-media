package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogLocalRepository;
import com.tokopedia.analyticsdebugger.debugger.data.repository.FpmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetFpmAllDataUseCase extends UseCase<List<Visitable>> {
    private FpmLogRepository fpmLogRepository;

    @Inject
    GetFpmAllDataUseCase(FpmLogLocalRepository fpmLogRepository) {
        this.fpmLogRepository = fpmLogRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return fpmLogRepository.getAllData();
    }
}
