package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.tokocash.WalletUserSession;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class AutoSweepInterceptor extends TkpdAuthInterceptor {

    private final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";

    private WalletUserSession walletUserSession;

    @Inject
    public AutoSweepInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter,
                                WalletUserSession walletUserSession) {
        super(context, abstractionRouter);
        this.walletUserSession = walletUserSession;
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> header = new HashMap<>();
        header.put(AUTHORIZATION, BEARER + " " + walletUserSession.getTokenWallet());
        return header;
    }
}