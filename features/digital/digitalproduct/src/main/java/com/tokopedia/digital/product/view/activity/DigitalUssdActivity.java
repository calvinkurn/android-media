package com.tokopedia.digital.product.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.fragment.DigitalUssdFragment;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.product.view.model.Validation;

import java.util.ArrayList;
import java.util.List;


public class DigitalUssdActivity extends BasePresenterActivity implements DigitalUssdFragment.ActionListener {
    private String titleToolbar;
    public static final String EXTRA_BALANCE_PASS_DATA = "EXTRA_BALANCE_PASS_DATA";
    private static final String EXTRA_OPERATOR_PASS_DATA = "EXTRA_OPERATOR_PASS_DATA";
    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";
    private static final String EXTRA_STATE_CATEGORY_ID = "EXTRA_STATE_CATEGORY_ID";
    private static final String EXTRA_STATE_CATEGORY_NAME = "EXTRA_STATE_CATEGORY_NAME";
    private static final String EXTRA_VALIDATION_LIST_PASS_DATA = "EXTRA_VALIDATION_LIST_PASS_DATA";
    private static final String ARG_PARAM_EXTRA_SIM_INDEX_DATA = "ARG_PARAM_EXTRA_SIM_INDEX_DATA";
    private static final String EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA = "EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA";

    private int selectedSimIndex = 0;
    private PulsaBalance pulsaBalance;
    private Operator selectedOperator;
    private String mCategoryId;
    private String mCategoryName;
    private List<Validation> validationList;
    private List<Operator> selectedOperatorList;

    public static Intent newInstance(Context context, PulsaBalance passData, Operator passOperatorData ,
                                     List<Validation> validationListData, String categoryId,
                                     String categoryName, int selectedSim, List<Operator> selectedOperatorList) {

        Intent intent = new Intent(context, DigitalUssdActivity.class);
        intent.putExtra(EXTRA_OPERATOR_PASS_DATA,  passOperatorData);
        intent.putExtra(EXTRA_BALANCE_PASS_DATA, passData);
        intent.putExtra(EXTRA_STATE_CATEGORY_ID, categoryId);
        intent.putExtra(EXTRA_STATE_CATEGORY_NAME, categoryName);
        intent.putParcelableArrayListExtra(EXTRA_VALIDATION_LIST_PASS_DATA,
                (ArrayList<? extends Parcelable>) validationListData);
        intent.putExtra(ARG_PARAM_EXTRA_SIM_INDEX_DATA, selectedSim);
        intent.putParcelableArrayListExtra(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) selectedOperatorList);
        return intent;
    }


    @Override
    protected void setupURIPass(Uri data) {

    }


    @Override
    protected void setupBundlePass(Bundle extras) {
        this.selectedOperator = extras.getParcelable(EXTRA_OPERATOR_PASS_DATA);
        this.validationList = extras.getParcelableArrayList(EXTRA_VALIDATION_LIST_PASS_DATA);
        this.pulsaBalance = extras.getParcelable(EXTRA_BALANCE_PASS_DATA);
        this.mCategoryId=extras.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName=extras.getString(EXTRA_STATE_CATEGORY_NAME);
        this.selectedSimIndex = extras.getInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, 0);
        this.selectedOperatorList = extras.getParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA);

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
                    DigitalUssdFragment.newInstance(pulsaBalance, selectedOperator,validationList,mCategoryId,mCategoryName, selectedSimIndex,selectedOperatorList)).commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
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
