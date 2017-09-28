package com.tokopedia.posapp.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.posapp.domain.model.CheckPasswordDomain;
import com.tokopedia.posapp.domain.usecase.ValidatePasswordUseCase;
import com.tokopedia.posapp.view.DialogPassword;
import com.tokopedia.posapp.view.subscriber.CheckPasswordSubscriber;
import com.tokopedia.session.session.presenter.Login;

import java.util.Date;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by okasurya on 9/27/17.
 */

public class DialogPasswordPresenter implements DialogPassword.Presenter {
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private Context context;
    private ValidatePasswordUseCase validatePasswordUseCase;
    private DialogPassword.View viewListener;

    @Inject
    public DialogPasswordPresenter(@ApplicationContext Context context,
                                   ValidatePasswordUseCase validatePasswordUseCase) {
        this.context = context;
        this.validatePasswordUseCase = validatePasswordUseCase;
    }

    @Override
    public void checkPassword(final String password) {
        RequestParams params = RequestParams.create();
        params.putString("password", password);

        validatePasswordUseCase.execute(params, new CheckPasswordSubscriber(viewListener));
    }

    private RequestParams getUserParams(String email, String password) {
        RequestParams params = RequestParams.create();
        String userId = SessionHandler.getLoginID(context);
        String deviceId = GCMHandler.getRegistrationId(context);
        String hash = AuthUtil.md5(userId + "~" + deviceId);

        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_DEVICE_ID, deviceId);
        params.putString(PARAM_HASH, hash);
        params.putString(PARAM_OS_TYPE, "1");
        params.putString(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        params.putString(Login.USER_NAME, email);
        params.putString(Login.PASSWORD, password);
        params.putString(Login.GRANT_TYPE, Login.GRANT_PASSWORD);
        params.putString(Login.SCOPE, Login.GRANT_020);

        return params;
    }

    public void attachView(DialogPassword.View viewListener) {
        this.viewListener = viewListener;
    }
}
