package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.network.api.WalletUrl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 10/12/17.
 */

public class WalletAuthInterceptor extends TkpdAuthInterceptor {

    public final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";
    private final static String DEVICE = "android-";
    private static final String HEADER_DEVICE = "X-Device";

    private WalletUserSession walletUserSession;

    @Inject
    public WalletAuthInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter,
                                 WalletUserSession walletUserSession) {
        super(context, abstractionRouter);
        this.walletUserSession = walletUserSession;
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        if (isUserInactiveTokoCash()) {
            Map<String, String> headerMap = AuthUtil.generateHeadersWithXUserIdXMsisdn(path, method,
                    WalletUrl.KeyHmac.HMAC_PENDING_CASHBACK, contentTypeHeader,
                    walletUserSession.getPhoneNumber(), userSession.getUserId(), userSession);
            return headerMap;
        } else {
            Map<String, String> header = new HashMap<>();
            header.put(AUTHORIZATION, BEARER + " " + walletUserSession.getTokenWallet());
            header.put(HEADER_DEVICE, DEVICE + GlobalConfig.VERSION_NAME);
            return header;
        }
    }

    private boolean isUserInactiveTokoCash() {
        return walletUserSession.getTokenWallet().equals("");
    }
}
