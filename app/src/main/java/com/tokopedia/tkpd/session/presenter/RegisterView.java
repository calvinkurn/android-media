package com.tokopedia.tkpd.session.presenter;

import android.app.DatePickerDialog;
import android.content.Context;

import com.tokopedia.tkpd.presenter.BaseView;
import com.tokopedia.tkpd.session.model.LoginGoogleModel;
import com.tokopedia.tkpd.session.model.RegisterViewModel;

/**
 * @author m.normansyah
 * @since  12/11/2015.
 * @version 2
 */
public interface RegisterView extends BaseView {
    String TAG = "MNORMANSYAH";
    String messageTAG = RegisterView.class.getSimpleName();

    int NAME = 0;
    int PHONE = 1;
    int GENDER = 2;
    int TTL = 3;
    int DATETEXT = 4;

    void initDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth);

    void initDatePicker(long maxtime, long mintime);

    void setDateText(String text);

    void showProgress(boolean show);

    /**
     * This is view logic without data so don't need to pass to presenter
     */
    void attemptRegisterStep1();

    void updateData(int type, Object data);

    void updateDialogPicker(int day, int month, int year);

    void moveToRegisterNext(RegisterViewModel model);

    void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel);
}
