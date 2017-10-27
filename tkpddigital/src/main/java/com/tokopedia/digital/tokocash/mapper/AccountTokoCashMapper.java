package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.AccountTokoCashEntity;
import com.tokopedia.digital.tokocash.entity.AccountTokoCashListEntity;
import com.tokopedia.digital.tokocash.model.AccountTokoCash;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/27/17.
 */

public class AccountTokoCashMapper implements Func1<AccountTokoCashListEntity, List<AccountTokoCash>> {

    @Override
    public List<AccountTokoCash> call(AccountTokoCashListEntity accountTokoCashListEntity) {
        if (null != accountTokoCashListEntity && accountTokoCashListEntity.getAccount_list().size() > 0) {
            List<AccountTokoCash> accountTokoCashList = new ArrayList<>();

            for (AccountTokoCashEntity accountTokoCashEntity: accountTokoCashListEntity.getAccount_list()) {
                AccountTokoCash accountTokoCash = new AccountTokoCash();
                accountTokoCash.setClientId(accountTokoCashEntity.getClient_id());
                accountTokoCash.setIdentifier(accountTokoCashEntity.getIdentifier());
                accountTokoCash.setImgUrl(accountTokoCashEntity.getImg_url());
                accountTokoCash.setAuthDateFmt(accountTokoCashEntity.getAuth_date_fmt());
                accountTokoCashList.add(accountTokoCash);
            }
            return accountTokoCashList;
        }
        return null;
    }
}
