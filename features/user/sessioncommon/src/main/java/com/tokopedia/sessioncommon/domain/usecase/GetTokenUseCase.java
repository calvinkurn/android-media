package com.tokopedia.sessioncommon.domain.usecase;

import com.tokopedia.network.refreshtoken.EncoderDecoder;
import com.tokopedia.sessioncommon.data.TokenApi;
import com.tokopedia.sessioncommon.data.model.TokenViewModel;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.sessioncommon.domain.mapper.TokenMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by stevenfredian on 3/8/17.
 * Please don't forget to handle
 * {@link com.tokopedia.sessioncommon.network.TokenErrorException
 * from @link {@link com.tokopedia.sessioncommon.network.TokenErrorResponse}}
 * in onError
 */
public class GetTokenUseCase extends UseCase<TokenViewModel> {

    public static final String GRANT_TYPE = "grant_type";
    public static final String SOCIAL_TYPE = "social_type";
    public static final String ACCESS_TOKEN = "access_token";

    public static final int SOCIAL_TYPE_FACEBOOK = 1;
    public static final int SOCIAL_TYPE_GPLUS = 7;
    public static final int SOCIAL_TYPE_PHONE_NUMBER = 5;

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String CODE = "code";
    public static final String REDIRECT_URI = "redirect_uri";

    public static final String GRANT_PASSWORD = "password";
    public static final String GRANT_SDK = "extension";
    public static final String GRANT_WEBVIEW = "authorization_code";

    private final TokenApi api;
    private final TokenMapper tokenMapper;
    private final UserSessionInterface userSession;

    @Inject
    public GetTokenUseCase(TokenApi api,
                           TokenMapper tokenMapper,
                           @Named(SessionModule.SESSION_MODULE)
                                       UserSessionInterface userSession) {
        this.api = api;
        this.tokenMapper = tokenMapper;
        this.userSession = userSession;

    }

    public Observable<TokenViewModel> createObservable(RequestParams requestParams) {
        return api.getToken(requestParams.getParameters())
                .map(tokenMapper)
                .doOnNext(saveAccessToken());
    }

    public static RequestParams getParamThirdParty(int socialType, String accessToken) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_SDK);
        params.putInt(SOCIAL_TYPE, socialType);
        params.putString(ACCESS_TOKEN, accessToken);
        return params;
    }

    public static RequestParams getParamRegisterWebview(String code, String redirectUri) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_WEBVIEW);
        params.putString(CODE, code);
        params.putString(REDIRECT_URI, redirectUri);
        return params;
    }

    public static RequestParams getParamLogin(String email, String password) {
        RequestParams params = RequestParams.create();
        params.putString(GRANT_TYPE, GRANT_PASSWORD);
        params.putString(PASSWORD, password);
        params.putString(USER_NAME, email);
        return params;
    }


    private Action1<TokenViewModel> saveAccessToken() {
        return tokenModel -> userSession.setToken(
                tokenModel.getAccessToken(),
                tokenModel.getTokenType(),
                EncoderDecoder.Encrypt(tokenModel.getRefreshToken(),
                        userSession.getRefreshTokenIV())
        );
    }
}
