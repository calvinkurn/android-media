package com.tokopedia.changephonenumber.domain;

import com.tokopedia.changephonenumber.view.viewmodel.WarningViewModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


/**
 * Created by milhamj on 27/12/17.
 */

public interface ChangePhoneNumberRepository {

    Observable<WarningViewModel> getWarning(HashMap<String, Object> parameters);

    Observable<Boolean> validateNumber(HashMap<String, Object> parameters);

    Observable<Boolean> validateOtpStatus(Map<String, Object> parameters);
}
