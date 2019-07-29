package com.tokopedia.common.travel.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.common.travel.domain.GetPhoneCodeUseCase;
import com.tokopedia.common.travel.domain.subscriber.AutoCompleteInputListener;
import com.tokopedia.common.travel.domain.subscriber.AutoCompleteKeywordListener;
import com.tokopedia.common.travel.domain.subscriber.AutoCompleteKeywordSubscriber;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;
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

public class PhoneCodePickerPresenterImpl extends BaseDaggerPresenter<PhoneCodePickerView>
        implements PhoneCodePickerPresenter, AutoCompleteKeywordListener {

    private AutoCompleteInputListener inputListener;
    private final GetPhoneCodeUseCase getPhoneCodeUseCase;

    @Inject
    public PhoneCodePickerPresenterImpl(GetPhoneCodeUseCase getPhoneCodeUseCase) {
        this.getPhoneCodeUseCase = getPhoneCodeUseCase;
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
        getPhoneCodeUseCase.execute(RequestParams.create(), getSubscriberPhoneCode());
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

    private Subscriber<List<CountryPhoneCode>> getSubscriberPhoneCode() {
        return new Subscriber<List<CountryPhoneCode>>() {
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
            public void onNext(List<CountryPhoneCode> flightBookingPhoneCodes) {
                if (isViewAttached()) {
                    getView().renderList(flightBookingPhoneCodes);
                }
            }
        };
    }

    @Override
    public void onTextReceive(String keyword) {
        getPhoneCodeUseCase.execute(getPhoneCodeUseCase.createRequest(keyword), getSubscriberPhoneCode());
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
