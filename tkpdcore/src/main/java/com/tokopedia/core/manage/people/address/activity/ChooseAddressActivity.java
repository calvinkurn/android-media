package com.tokopedia.core.manage.people.address.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.manage.people.address.fragment.ChooseAddressFragment;
import com.tokopedia.core.manage.people.address.model.Destination;

import java.util.ArrayList;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressActivity extends BasePresenterActivity {

    public static final String REQUEST_CODE_PARAM_ADDRESS = "ADDRESSPASSDATA";
    public static final int RESULT_NOT_SELECTED_DESTINATION = 2;

    private static final String TAG = "CHOOSE_ADDRESS_FRAGMENT";

    public static Intent createInstance(Context context) {
        return new Intent(context, ChooseAddressActivity.class);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CHOOSE_ADDR;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
    }

    @Override
    protected int getLayoutId() {
       return R.layout.activity_choose_address;
    }

    @Override
    protected void initView() {
        ChooseAddressFragment fragment = ChooseAddressFragment.createInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        if (intent.getExtras() != null &&
                getFragmentManager().findFragmentById(R.id.container) instanceof OnChooseAddressViewListener) {

            ArrayList<Destination> mDestinations =
                    ((OnChooseAddressViewListener) getFragmentManager().findFragmentById(R.id.container))
                            .onActivityBackPressed();

            String addressId = intent.getExtras().getString(ChooseAddressActivity.REQUEST_CODE_PARAM_ADDRESS);
            for (Destination destination : mDestinations) {
                if (destination.getAddressId().equalsIgnoreCase(addressId)) {
                    intent.putExtra(ManageAddressConstant.EXTRA_ADDRESS, destination);
                    setResult(RESULT_NOT_SELECTED_DESTINATION, intent);
                    break;
                }
            }
        }
        finish();
    }

    public interface OnChooseAddressViewListener {
        ArrayList<Destination> onActivityBackPressed();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
