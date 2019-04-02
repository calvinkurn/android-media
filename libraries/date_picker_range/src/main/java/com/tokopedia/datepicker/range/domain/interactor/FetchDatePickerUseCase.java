package com.tokopedia.datepicker.range.domain.interactor;

import com.tokopedia.datepicker.range.domain.DatePickerRepository;
import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class FetchDatePickerUseCase extends UseCase<DatePickerDomainModel> {
    private DatePickerRepository datePickerRepository;

    @Inject
    public FetchDatePickerUseCase(DatePickerRepository datePickerRepository) {
        super();
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<DatePickerDomainModel> createObservable(RequestParams requestParams) {
        return datePickerRepository.fetchSetting();
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }

}
