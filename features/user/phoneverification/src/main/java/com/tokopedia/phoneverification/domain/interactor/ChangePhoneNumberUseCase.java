package com.tokopedia.phoneverification.domain.interactor;


import com.tokopedia.phoneverification.data.model.ChangePhoneNumberViewModel;
import com.tokopedia.phoneverification.data.source.ChangeMsisdnSource;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberUseCase extends UseCase<ChangePhoneNumberViewModel> {

    private static final String PARAM_MSISDN = "msisdn";
    private final ChangeMsisdnSource changeMsisdnSource;
    private final UserSession userSession;

    @Inject
    public ChangePhoneNumberUseCase(ChangeMsisdnSource changeMsisdnSource,
                                    UserSession userSession) {
        this.changeMsisdnSource = changeMsisdnSource;
        this.userSession = userSession;
    }

    @Override
    public Observable<ChangePhoneNumberViewModel> createObservable(final RequestParams requestParams) {
        return changeMsisdnSource.changePhoneNumber(requestParams.getParameters())
                .flatMap(addPhoneNumberToViewModel(requestParams))
                .doOnNext(savePhoneNumber());
    }

    private Func1<ChangePhoneNumberViewModel, Observable<ChangePhoneNumberViewModel>> addPhoneNumberToViewModel(final RequestParams requestParams) {
        return new Func1<ChangePhoneNumberViewModel, Observable<ChangePhoneNumberViewModel>>() {
            @Override
            public Observable<ChangePhoneNumberViewModel> call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                changePhoneNumberViewModel.setPhoneNumber(
                        requestParams.getString(PARAM_MSISDN, ""));
                return Observable.just(changePhoneNumberViewModel);
            }
        };
    }

    private Action1<ChangePhoneNumberViewModel> savePhoneNumber() {
        return new Action1<ChangePhoneNumberViewModel>() {
            @Override
            public void call(ChangePhoneNumberViewModel changePhoneNumberViewModel) {
                if (changePhoneNumberViewModel.isSuccess()) {
                    userSession.setPhoneNumber(changePhoneNumberViewModel.getPhoneNumber());
                }
            }
        };
    }

    public static RequestParams getParam(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_MSISDN, phoneNumber);
        return params;
    }
}
