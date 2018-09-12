package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetPhoneCodeUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.subscriber.AutoCompleteInputListener;
import com.tokopedia.flight.common.subscriber.AutoCompleteKeywordListener;
import com.tokopedia.flight.common.subscriber.AutoCompleteKeywordSubscriber;
import com.tokopedia.usecase.RequestParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodePresenterImpl extends BaseDaggerPresenter<FlightBookingPhoneCodeView>
        implements FlightBookingPhoneCodePresenter, AutoCompleteKeywordListener {

    private AutoCompleteInputListener inputListener;
    private final FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase;

    @Inject
    public FlightBookingPhoneCodePresenterImpl(FlightBookingGetPhoneCodeUseCase flightBookingGetPhoneCodeUseCase) {
        this.flightBookingGetPhoneCodeUseCase = flightBookingGetPhoneCodeUseCase;
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                inputListener = new AutoCompleteInputListener() {
                    @Override
                    public void onQuerySubmit(String query) {
                        subscriber.onNext(String.valueOf(query));
                    }
                };
            }
        }).debounce(250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AutoCompleteKeywordSubscriber(this));
    }

    @Override
    public void getPhoneCodeList() {
        flightBookingGetPhoneCodeUseCase.execute(RequestParams.create(), getSubscriberPhoneCode());
    }

    @Override
    public void getPhoneCodeList(String text) {
        if (inputListener != null) {
            inputListener.onQuerySubmit(text);
        }
    }

    @Override
    public void onDestroyView() {
        detachView();
    }

    private Subscriber<List<FlightBookingPhoneCodeViewModel>> getSubscriberPhoneCode() {
        return new Subscriber<List<FlightBookingPhoneCodeViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels) {
                if (isViewAttached()) {
                    getView().renderList(flightBookingPhoneCodeViewModels);
                }
            }
        };
    }

    @Override
    public void onTextReceive(String keyword) {
        flightBookingGetPhoneCodeUseCase.execute(flightBookingGetPhoneCodeUseCase.createRequest(keyword), getSubscriberPhoneCode());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
