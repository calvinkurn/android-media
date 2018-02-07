package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.digital.tokocash.entity.AccountWalletListEntity;
import com.tokopedia.digital.tokocash.entity.OAuthInfoEntity;
import com.tokopedia.digital.tokocash.model.AccountWalletItem;
import com.tokopedia.digital.tokocash.model.OAuthInfo;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 10/26/17.
 */

public class OAuthInfoMapper implements Func1<OAuthInfoEntity, OAuthInfo> {

    @Override
    public OAuthInfo call(OAuthInfoEntity oAuthInfoEntity) {
        if (oAuthInfoEntity != null) {
            OAuthInfo oAuthInfo = new OAuthInfo();
            if (oAuthInfoEntity.getAccount_list() != null && oAuthInfoEntity.getAccount_list().size() > 0) {
                List<AccountWalletItem> accountWalletList = new ArrayList<>();
                for (AccountWalletListEntity accountWalletListEntity: oAuthInfoEntity.getAccount_list()) {
                    AccountWalletItem accountWalletItem = new AccountWalletItem();
                    accountWalletItem.setClientId(accountWalletListEntity.getClient_id());
                    accountWalletItem.setClientName(accountWalletListEntity.getClient_name());
                    accountWalletItem.setIdentifier(accountWalletListEntity.getIdentifier());
                    accountWalletItem.setIdentifierType(accountWalletListEntity.getIdentifier_type());
                    accountWalletItem.setAuthDateFmt(accountWalletListEntity.getAuth_date_fmt());
                    accountWalletItem.setImgUrl(accountWalletListEntity.getImg_url());
                    accountWalletItem.setRefreshToken(accountWalletListEntity.getRefresh_token());
                    accountWalletList.add(accountWalletItem);
                }
                oAuthInfo.setAccountList(accountWalletList);
            }

            oAuthInfo.setEmail(oAuthInfoEntity.getEmail());
            oAuthInfo.setName(oAuthInfoEntity.getName());
            oAuthInfo.setEmail(oAuthInfoEntity.getEmail());
            oAuthInfo.setMobile(oAuthInfoEntity.getMobile());
            oAuthInfo.setTokopediaUserId(oAuthInfoEntity.getTokopedia_user_id());
            return oAuthInfo;
        }
        return null;
    }
}
