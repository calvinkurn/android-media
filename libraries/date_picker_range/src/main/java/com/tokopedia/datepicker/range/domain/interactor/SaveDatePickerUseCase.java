package com.tokopedia.datepicker.range.domain.interactor;

import com.tokopedia.datepicker.range.domain.DatePickerRepository;
import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/26/17.
 */

public class SaveDatePickerUseCase extends UseCase<Boolean> {

    private static final String DATE_PICKER_MODEL = "DATE_PICKER_MODEL";

    private DatePickerRepository datePickerRepository;

    @Inject
    public SaveDatePickerUseCase(DatePickerRepository datePickerRepository) {
        super();
        this.datePickerRepository = datePickerRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return datePickerRepository.saveSetting((DatePickerDomainModel) requestParams.getObject(DATE_PICKER_MODEL));
    }

    public static RequestParams createRequestParams(DatePickerViewModel datePickerViewModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(DATE_PICKER_MODEL, new DatePickerDomainModel(datePickerViewModel));
        return requestParams;
    }

}
