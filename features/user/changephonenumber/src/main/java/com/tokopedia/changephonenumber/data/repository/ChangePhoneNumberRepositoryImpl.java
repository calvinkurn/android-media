package com.tokopedia.changephonenumber.data.repository;


import com.tokopedia.changephonenumber.data.source.CloudGetWarningSource;
import com.tokopedia.changephonenumber.data.source.CloudValidateNumberSource;
import com.tokopedia.changephonenumber.data.source.CloudValidateOtpStatus;
import com.tokopedia.changephonenumber.domain.ChangePhoneNumberRepository;
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 27/12/17.
 */

public class ChangePhoneNumberRepositoryImpl implements ChangePhoneNumberRepository {
    private final CloudGetWarningSource cloudGetWarningSource;
    private final CloudValidateNumberSource cloudValidateNumberSource;
    private final CloudValidateOtpStatus cloudValidateOtpStatus;

    @Inject
    public ChangePhoneNumberRepositoryImpl(CloudGetWarningSource cloudGetWarningSource,
                                           CloudValidateNumberSource cloudValidateNumberSource,
                                           CloudValidateOtpStatus cloudValidateOtpStatus) {
        this.cloudGetWarningSource = cloudGetWarningSource;
        this.cloudValidateNumberSource = cloudValidateNumberSource;
        this.cloudValidateOtpStatus = cloudValidateOtpStatus;
    }

    @Override
    public Observable<WarningUIModel> getWarning(HashMap<String, Object> parameters) {
        return cloudGetWarningSource
                .getWarning(parameters);
    }

    @Override
    public Observable<Boolean> validateNumber(HashMap<String, Object> parameters) {
        return cloudValidateNumberSource.validateNumber(parameters);
    }

    @Override
    public Observable<Boolean> validateOtpStatus(Map<String, Object> parameters) {
        return cloudValidateOtpStatus.validateOtpStatus(parameters);
    }
}
