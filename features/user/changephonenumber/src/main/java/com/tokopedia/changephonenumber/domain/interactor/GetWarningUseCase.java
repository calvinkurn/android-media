package com.tokopedia.changephonenumber.domain.interactor;

import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningUseCase extends UseCase<WarningUIModel> {
    private static final String PARAM_OS_TYPE = "theme";
    private static final String OS_TYPE_ANDROID = "mobile";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public GetWarningUseCase(ChangePhoneNumberRepository changePhoneNumberRepository) {
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<WarningUIModel> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.getWarning(requestParams.getParameters());
    }

    public static RequestParams getGetWarningParam() {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }
}
