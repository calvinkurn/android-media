package com.tokopedia.tkpd.session.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.session.model.LoginGoogleModel;
import com.tokopedia.tkpd.session.model.RegisterViewModel;

/**
 * @author m.normansyah
 * @since 12-11-2015
 */
@Deprecated
public interface Register {
    String VIEW_MODEL = "VIEW_MODEL";
    String TAG = "MNORMANSYAH";
    String messageTAG = Register.class.getSimpleName();
    void initDataInstances(Context context);
    int PHONE = 0;
    int NAME = 1;
    int GENDER = 2;
    int BIRTHDAYTTL = 3;

    void calculateDateTime();

    void initData(Context context);

    String formatDateText(int mDateDay, int mDateMonth, int mDateYear );

    void updateDate(int mDateYear, int mDateMonth, int mDateDay);

    boolean checkRegexName(String param);

    boolean isExceedMaxCharacter(String text);

    /**
     * this value already provided
     * @see RegisterViewModel#GENDER_MALE
     * @see RegisterViewModel#GENDER_FEMALE
     * @param gender 1 for male and 2 for female
     */
    void updateGender(int gender);

    void setLocalyticFlow(Context context);

    void saveDataBeforeRotation(Bundle outState);

    void fetchDataAfterRotation(Bundle outState);

    void updateViewModel(RegisterViewModel model);
    void updateViewModel(int type, Object... data);

    boolean isAfterRotate();

    RegisterViewModel getData();

    void loginFacebook();

    void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel);

    void updateData(int type, Object... data);
}
