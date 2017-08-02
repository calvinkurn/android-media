package com.tokopedia.digital.product.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalUssdFragment;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.PulsaBalance;


public class DigitalUssdActivity extends BasePresenterActivity implements DigitalUssdFragment.ActionListener {
    private String titleToolbar;
    public static final String EXTRA_BALANCE_PASS_DATA = "EXTRA_BALANCE_PASS_DATA";
    private static final String EXTRA_OPERATOR_PASS_DATA = "EXTRA_OPERATOR_PASS_DATA";
    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";
    private static final String EXTRA_STATE_CATEGORY_ID = "EXTRA_STATE_CATEGORY_ID";
    private static final String EXTRA_STATE_CATEGORY_NAME = "EXTRA_STATE_CATEGORY_NAME";
    private PulsaBalance pulsaBalance;
    private Operator selectedOperator;
    private String mCategoryId;
    private String mCategoryName;

    public static Intent newInstance(Context context, PulsaBalance passData, Operator passOperatorData,String categoryId,String categoryName) {

        Intent intent = new Intent(context, DigitalUssdActivity.class);
        intent.putExtra(EXTRA_OPERATOR_PASS_DATA,  passOperatorData);
        intent.putExtra(EXTRA_BALANCE_PASS_DATA, passData);
        intent.putExtra(EXTRA_STATE_CATEGORY_ID, categoryId);
        intent.putExtra(EXTRA_STATE_CATEGORY_NAME, categoryName);
        return intent;
    }


    @Override
    protected void setupURIPass(Uri data) {

    }


    @Override
    protected void setupBundlePass(Bundle extras) {
        this.selectedOperator = extras.getParcelable(EXTRA_OPERATOR_PASS_DATA);
        this.pulsaBalance = extras.getParcelable(EXTRA_BALANCE_PASS_DATA);
        this.mCategoryId=extras.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName=extras.getString(EXTRA_STATE_CATEGORY_NAME);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_digital_ussd;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !(fragment instanceof DigitalUssdFragment))
            getFragmentManager().beginTransaction().replace(R.id.container,
                    DigitalUssdFragment.newInstance(pulsaBalance, selectedOperator,mCategoryId,mCategoryName)).commit();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_STATE_TITLE_TOOLBAR, titleToolbar);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        invalidateTitleToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
    }

    @Override
    public void updateTitleToolbar(String title) {
        this.titleToolbar = title;
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }
}
