package com.tokopedia.tkpd.session.presenter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.presenter.BaseView;

/**
 * Created by noiz354 on 1/25/16.
 */
public interface RegisterNewNextView extends BaseView {
    String PASSWORD = "RegisterNewNextFragment PASSWORD";
    String EMAIL = "RegisterNewNextFragment EMAIL";
    String IS_AUTO_VERIFY = "RegisterNewNextFragment ISAUTOVERIFY";
    String TAG = "MNORMANSYAH";
    String messageTAG = "RegisterNewNextView : ";

    int FULLNAME = 0;
    int TELEPHONE = 1;
    int GENDER = 2;
    int TTL = 3;
    int T_AND_C = 4;

    void showProgress(boolean show);
    void initDatePickerDialog(Context context, int year, int monthOfYear, int dayOfMonth);
    void initDatePicker(long maxtime, long mintime);
    void setData(int type, Object... data);
}
