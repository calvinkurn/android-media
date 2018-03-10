package com.tokopedia.posapp.auth.login.data.source;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsBasicService;
import com.tokopedia.posapp.auth.login.data.mapper.PosTokenMapper;
import com.tokopedia.session.data.source.GetTokenDataSource;
import com.tokopedia.session.domain.pojo.token.TokenViewModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author okasurya on 3/9/18.
 */

public class PosGetTokenDataSource extends GetTokenDataSource {
    @Inject
    public PosGetTokenDataSource(AccountsBasicService basicService,
                                 PosTokenMapper tokenMapper,
                                 SessionHandler sessionHandler) {
        super(basicService, tokenMapper, sessionHandler);
    }

    @Override
    public Observable<TokenViewModel> getAccessToken(RequestParams params) {
        return super.getAccessToken(params).doOnNext(saveScope());
    }

    private Action1<? super TokenViewModel> saveScope() {
        return new Action1<TokenViewModel>() {
            @Override
            public void call(TokenViewModel tokenViewModel) {
                sessionHandler.setScope(tokenViewModel.getScope());
            }
        };
    }
}
