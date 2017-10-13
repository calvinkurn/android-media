package com.tokopedia.digital.product.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.adapter.NumberListAdapter;
import com.tokopedia.digital.product.fragment.DigitalSearchNumberFragment;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class DigitalSearchNumberActivity extends BasePresenterActivity implements
        DigitalSearchNumberFragment.OnClientNumberClickListener {

    private static final String EXTRA_NUMBER_LIST = "EXTRA_NUMBER_LIST";
    private static final String EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER";
    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    public static final String EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER";
    private static final String EXTRA_CALLBACK_CLIENT_NUMBER_BACK = "EXTRA_CALLBACK_CLIENT_NUMBER_BACK";

    private String categoryId;
    private String clientNumber;
    private List<OrderClientNumber> numberList;

    public static Intent newInstance(Activity activity, String categoryId, String clientNumber, List<OrderClientNumber> numberList) {
        Intent intent = new Intent(activity, DigitalSearchNumberActivity.class);
        intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumber);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, (ArrayList<? extends Parcelable>) numberList);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.categoryId = extras.getString(EXTRA_CATEGORY_ID);
        this.clientNumber = extras.getString(EXTRA_CLIENT_NUMBER);
        this.numberList = extras.getParcelableArrayList(EXTRA_NUMBER_LIST);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_number_digital;
    }

    @Override
    protected void initView() {
        toolbar.setTitle("Nomor Favorit");

        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalSearchNumberFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalSearchNumberFragment.newInstance(categoryId, clientNumber, numberList)).commit();
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
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    public void onClientNumberClicked(OrderClientNumber orderClientNumber) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber));
        finish();
    }

}
