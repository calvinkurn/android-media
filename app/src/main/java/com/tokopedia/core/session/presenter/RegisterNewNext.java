package com.tokopedia.core.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.session.base.BaseImpl;
import com.tokopedia.core.session.model.RegisterViewModel;

/**
 * Created by m.normansyah on 1/25/16.
 */
public abstract class RegisterNewNext extends BaseImpl<RegisterNewNextView>{
    public static final String TAG = "MNORMANSYAH";
    public static final String message = "RegisterNewNext : ";
    public static final String DATA = "RegisterNewNext_DATA";

    public static final int DATE_YEAR = 0;
    public static final int DATE_MONTH = 1;
    public static final int DATE_DAY = 2;
    public static final int GENDER = 3;
    public static final int EMAIl = 4;
    public static final int PASSWORD = 5;
    public static final int FULLNAME = 6;
    public static final int PHONT = 7;

    public RegisterNewNext(RegisterNewNextView view) {
        super(view);
    }

    public abstract void updateData(int type, Object... data);
    public abstract void setData(int type, Bundle data);
    public abstract void register(Context context, RegisterViewModel registerViewModel);
    public abstract void calculateDateTime();
    public abstract void showTermsAndOptionsTextView(Context context);
    public abstract Object getData(int type);
    public abstract RegisterViewModel compileAll(String fullName, String phoneNumber);
    public abstract void saveBeforeDestroy(Context context, String fullName, String phone);
}
