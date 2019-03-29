package com.tokopedia.changephonenumber.data.source;

import com.tokopedia.changephonenumber.data.api.ChangePhoneNumberApi;
import com.tokopedia.changephonenumber.data.mapper.ValidateNumberMapper;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 03/01/18.
 */

public class CloudValidateNumberSource {
    private final ChangePhoneNumberApi changePhoneNumberApi;
    private final ValidateNumberMapper validateNumberMapper;

    @Inject
    public CloudValidateNumberSource(ChangePhoneNumberApi changePhoneNumberApi,
                                     ValidateNumberMapper validateNumberMapper) {
        this.changePhoneNumberApi = changePhoneNumberApi;
        this.validateNumberMapper = validateNumberMapper;
    }

    public Observable<Boolean> validateNumber(HashMap<String, Object> params) {
        return changePhoneNumberApi.validateNumber(params)
                .map(validateNumberMapper);
    }
}
