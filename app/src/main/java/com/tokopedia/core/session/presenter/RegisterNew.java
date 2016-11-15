package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by m.normansyah on 1/25/16.
 */
public abstract class RegisterNew extends BaseImpl<RegisterNewView>{
    public static final String
        EMAIL = "EMAIL",
        PASSWORD = "PASSWORD",
        IS_SELECT_FROM_AUTO_TEXT_VIEW = "IS_SELECT_FROM_AUTO_TEXT_VIEW",
        IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME = "IS_EMAIL_EDITTED_FOR_THE_FIRSTTIME",
        IS_SAVING = "IsSaving", IS_CHECKED = "IS_CHECKED", DATA = "DATA"
                ;
    String LOGIN_UUID_KEY = "LOGIN_UUID";
    String UUID_KEY = "uuid";
    String DEFAULT_UUID_VALUE = "";
    String PROVIDER_LIST = "provider";
    String PROVIDER_CACHE_KEY = "provider_cache";


    @Override
    public String getMessageTAG() {
        return getMessageTAG(RegisterNew.class);
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return className.getSimpleName()+" : ";
    }

    public RegisterNew(RegisterNewView view) {
        super(view);
    }

    public abstract void validateEmail(Context context, String email, String password);
    public abstract void saveData(HashMap<String, Object> data);
    public abstract void startLoginWithGoogle(Context context,String type, LoginGoogleModel loginGoogleModel);
    public abstract void loginFacebook(Context context);


    public abstract void setData(Context baseContext, int type, Bundle data);

    public abstract void downloadProviderLogin(Context context);

    public abstract void loginWebView(Context context, Bundle code);

    public abstract void saveProvider(List<LoginProviderModel.ProvidersBean> listProvider);

    public abstract void getProvider(Context context);


    public abstract void unSubscribeFacade();

    public abstract void sendGTMRegisterError(Context context, String label);
}
