package com.tokopedia.changephonenumber.data.source;

import com.tokopedia.changephonenumber.data.api.ChangePhoneNumberApi;
import com.tokopedia.changephonenumber.data.mapper.GetWarningMapper;
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;


/**
 * Created by milhamj on 27/12/17.
 */

public class CloudGetWarningSource {
    private final ChangePhoneNumberApi changePhoneNumberApi;
    private GetWarningMapper getWarningMapper;

    @Inject
    public CloudGetWarningSource(ChangePhoneNumberApi changePhoneNumberApi, GetWarningMapper
            getWarningMapper) {
        this.changePhoneNumberApi = changePhoneNumberApi;
        this.getWarningMapper = getWarningMapper;
    }

    public Observable<WarningUIModel> getWarning(HashMap<String, Object> params) {
        return changePhoneNumberApi.getWarning(params)
                .map(getWarningMapper);
    }
}
