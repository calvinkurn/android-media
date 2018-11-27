package com.tokopedia.datepicker.range.view.presenter;

import com.tokopedia.datepicker.range.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.datepicker.range.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.datepicker.range.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerPresenterImpl<T extends DatePickerView> extends DatePickerPresenter<T> {

    private FetchDatePickerUseCase fetchDatePickerUseCase;
    private SaveDatePickerUseCase saveDatePickerUseCase;
    private ClearDatePickerUseCase clearDatePickerUseCase;

    public DatePickerPresenterImpl(
            FetchDatePickerUseCase fetchDatePickerUseCase,
            SaveDatePickerUseCase saveDatePickerUseCase,
            ClearDatePickerUseCase clearDatePickerUseCase) {
        this.fetchDatePickerUseCase = fetchDatePickerUseCase;
        this.saveDatePickerUseCase = saveDatePickerUseCase;
        this.clearDatePickerUseCase = clearDatePickerUseCase;
    }

    @Override
    public void fetchDatePickerSetting() {
        fetchDatePickerUseCase.execute(FetchDatePickerUseCase.createRequestParams(), new Subscriber<DatePickerDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorLoadDatePicker(e);
            }

            @Override
            public void onNext(DatePickerDomainModel datePickerDomainModel) {
                 getView().onSuccessLoadDatePicker(new DatePickerViewModel(datePickerDomainModel));
            }
        });
    }

    @Override
    public void saveDateSetting(DatePickerViewModel datePickerViewModel) {
        saveDatePickerUseCase.executeSync(SaveDatePickerUseCase.createRequestParams(datePickerViewModel), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorSaveDatePicker(e);
            }

            @Override
            public void onNext(Boolean mBoolean) {
                getView().onSuccessSaveDatePicker();
            }
        });
    }

    @Override
    public void clearDatePickerSetting() {
        clearDatePickerUseCase.execute(ClearDatePickerUseCase.createRequestParams(), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorClearDatePicker(e);
            }

            @Override
            public void onNext(Boolean mBoolean) {
                getView().onSuccessClearDatePicker();
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchDatePickerUseCase.unsubscribe();
        clearDatePickerUseCase.unsubscribe();
        saveDatePickerUseCase.unsubscribe();
    }
}