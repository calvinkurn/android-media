package com.tokopedia.tokocash.accountsetting.domain;

import com.tokopedia.tokocash.accountsetting.data.AccountSettingRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/27/18.
 */

public class PostUnlinkTokoCashUseCase extends UseCase<Boolean> {

    public static final String REVOKE_TOKEN = "revoke_token";
    public static final String IDENTIFIER = "identifier";
    public static final String IDENTIFIER_TYPE = "identifier_type";

    private AccountSettingRepository accountSettingRepository;

    public PostUnlinkTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        this.accountSettingRepository = accountSettingRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return accountSettingRepository.unlinkAccountTokoCash(requestParams.getParamsAllValueInString());
    }
}
