package com.tokopedia.changephonenumber.domain;

import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


/**
 * Created by milhamj on 27/12/17.
 */

public interface ChangePhoneNumberRepository {

    Observable<WarningUIModel> getWarning(HashMap<String, Object> parameters);

    Observable<Boolean> validateNumber(HashMap<String, Object> parameters);

    Observable<Boolean> validateOtpStatus(Map<String, Object> parameters);
}
