package com.tokopedia.datepicker.range.domain.interactor;

import com.tokopedia.datepicker.range.domain.DatePickerRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class ClearDatePickerUseCase extends UseCase<Boolean> {

    private DatePickerRepository datePickerRepository;

    @Inject
    public ClearDatePickerUseCase(DatePickerRepository datePickerRepository) {
        super();
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return datePickerRepository.clearSetting();
    }

    public static RequestParams createRequestParams() {
        return RequestParams.EMPTY;
    }

}
