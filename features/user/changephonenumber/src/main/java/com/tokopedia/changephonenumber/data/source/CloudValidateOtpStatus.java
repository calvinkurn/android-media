package com.tokopedia.changephonenumber.data.source;

import com.tokopedia.changephonenumber.data.api.ChangePhoneNumberApi;
import com.tokopedia.changephonenumber.data.mapper.ValidateOtpStatusMapper;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by alvinatin on 11/05/18.
 */

public class CloudValidateOtpStatus {
    private final ChangePhoneNumberApi changePhoneNumberApi;
    private final ValidateOtpStatusMapper mapper;

    @Inject
    public CloudValidateOtpStatus(ChangePhoneNumberApi changePhoneNumberApi,
                                  ValidateOtpStatusMapper mapper) {
        this.changePhoneNumberApi = changePhoneNumberApi;
        this.mapper = mapper;
    }

    public Observable<Boolean> validateOtpStatus(Map<String, Object> param) {
        return changePhoneNumberApi.validateOtpStatus(param).map(mapper);
    }
}
