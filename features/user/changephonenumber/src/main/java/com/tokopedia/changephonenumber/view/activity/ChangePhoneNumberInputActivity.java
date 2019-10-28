package com.tokopedia.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.changephonenumber.view.fragment.ChangePhoneNumberInputFragment;

import java.util.ArrayList;

public class ChangePhoneNumberInputActivity extends BaseSimpleActivity {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";

    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;

    public static Intent newInstance(Context context, String phoneNumber, String email,
                                     ArrayList<String> warningList) {
        Intent intent = new Intent(context, ChangePhoneNumberInputActivity.class);
        intent.putExtra(PARAM_PHONE_NUMBER, phoneNumber);
        intent.putExtra(PARAM_WARNING_LIST, warningList);
        intent.putExtra(PARAM_EMAIL, email);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getExtras();
        super.onCreate(savedInstanceState);
    }

    private void getExtras(){
        if (getIntent().getExtras() != null) {
            phoneNumber = getIntent().getExtras().getString(PARAM_PHONE_NUMBER);
            warningList = getIntent().getExtras().getStringArrayList(PARAM_WARNING_LIST);
            email = getIntent().getExtras().getString(PARAM_EMAIL);
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return ChangePhoneNumberInputFragment.newInstance(phoneNumber, email, warningList);
    }
}
