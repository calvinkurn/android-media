package com.tokopedia.topupbills.telco.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.topupbills.telco.view.fragment.DigitalSearchNumberFragment;
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber;
import com.tokopedia.topupbills.telco.view.model.DigitalOrderClientNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class DigitalSearchNumberActivity extends BaseSimpleActivity implements
        DigitalSearchNumberFragment.OnClientNumberClickListener {

    private static final String EXTRA_NUMBER_LIST = "EXTRA_NUMBER_LIST";
    private static final String EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER";
    private static final String EXTRA_NUMBER = "EXTRA_NUMBER";
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    public static final String EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER";

    private String categoryId;
    private DigitalFavNumber clientNumber;
    private String number;
    private List<DigitalOrderClientNumber> numberList;

    @Override
    public String getScreenName() {
        return DigitalSearchNumberActivity.class.getSimpleName();
    }

    public static Intent newInstance(Activity activity, DigitalFavNumber clientNumber,
                                     String number) {
        Intent intent = new Intent(activity, DigitalSearchNumberActivity.class);
        intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumber);
        intent.putExtra(EXTRA_NUMBER, number);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        this.clientNumber = extras.getParcelable(EXTRA_CLIENT_NUMBER);
        this.number = extras.getString(EXTRA_NUMBER);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        return DigitalSearchNumberFragment
                .newInstance(clientNumber, number);
    }

    @Override
    public void onClientNumberClicked(DigitalOrderClientNumber orderClientNumber) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber));
        finish();
    }

}
