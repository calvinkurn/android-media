package com.tokopedia.travelcalendar.domain;

import com.tokopedia.travelcalendar.view.model.HolidayResult;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class GetHolidayUseCase extends UseCase<List<HolidayResult>> {

    private ITravelCalendarRepository repository;

    public GetHolidayUseCase(ITravelCalendarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<HolidayResult>> createObservable(RequestParams requestParams) {
        return repository.getHolidayResultsLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<List<HolidayResult>>>() {
                    @Override
                    public Observable<List<HolidayResult>> call(Throwable throwable) {
                        return repository.getHolidayResults();
                    }
                });
    }
}
