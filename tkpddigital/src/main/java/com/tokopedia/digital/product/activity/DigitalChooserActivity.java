package com.tokopedia.digital.product.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.fragment.DigitalChooserOperatorFragment;
import com.tokopedia.digital.product.fragment.DigitalChooserProductFragment;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserActivity extends BasePresenterActivity implements
        DigitalChooserProductFragment.ActionListener, DigitalChooserOperatorFragment.ActionListener {

    private static final String EXTRA_LIST_DATA_PRODUCT = "EXTRA_LIST_DATA_PRODUCT";
    private static final String EXTRA_PRODUCT_STYLE_VIEW = "EXTRA_PRODUCT_STYLE_VIEW";

    private static final String EXTRA_LIST_DATA_OPERATOR = "EXTRA_LIST_DATA_OPERATOR";
    private static final String EXTRA_OPERATOR_STYLE_VIEW = "EXTRA_OPERATOR_STYLE_VIEW";

    private static final String EXTRA_TITLE_CHOOSER = "EXTRA_TITLE_CHOOSER";
    private static final String EXTRA_STATE_CATEGORY = "EXTRA_STATE_CATEGORY";

    public static final String EXTRA_CALLBACK_PRODUCT_DATA = "EXTRA_CALLBACK_PRODUCT_DATA";
    public static final String EXTRA_CALLBACK_OPERATOR_DATA = "EXTRA_CALLBACK_OPERATOR_DATA";

    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";


    private List<Operator> operatorListData;
    private List<Product> productListData;

    private String operatorStyleView;
    private String categoryState;
    private String productStyleView;
    private String titleToolbar;


    public static Intent newInstanceProductChooser(
            Activity activity, List<Product> productListData, String titleChooser
    ) {
        List<Product> productListModify = new ArrayList<>();
        for (Product product : productListData) {
            if (product.getStatus() != Product.STATUS_INACTIVE) productListModify.add(product);
        }
        Intent intent = new Intent(activity, DigitalChooserActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_LIST_DATA_PRODUCT,
                (ArrayList<? extends Parcelable>) productListModify);
        intent.putExtra(EXTRA_TITLE_CHOOSER, titleChooser);
        return intent;
    }

    public static Intent newInstanceOperatorChooser(
            Activity activity, List<Operator> operatorListData, String titleChooser, String categoryState
    ) {
        Intent intent = new Intent(activity, DigitalChooserActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_LIST_DATA_OPERATOR,
                (ArrayList<? extends Parcelable>) operatorListData);
        intent.putExtra(EXTRA_TITLE_CHOOSER, titleChooser);
        intent.putExtra(EXTRA_STATE_CATEGORY, categoryState);
        return intent;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.operatorListData = extras.getParcelableArrayList(EXTRA_LIST_DATA_OPERATOR);
        this.productListData = extras.getParcelableArrayList(EXTRA_LIST_DATA_PRODUCT);
        this.productStyleView = extras.getString(EXTRA_PRODUCT_STYLE_VIEW);
        this.operatorStyleView = extras.getString(EXTRA_OPERATOR_STYLE_VIEW);
        this.categoryState = extras.getString(EXTRA_STATE_CATEGORY);
        if (titleToolbar == null) titleToolbar = extras.getString(EXTRA_TITLE_CHOOSER);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chooser_digital_module;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment == null || !((fragment instanceof DigitalChooserOperatorFragment)
                || (fragment instanceof DigitalChooserProductFragment))) {
            if (operatorListData == null && !productListData.isEmpty()) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        DigitalChooserProductFragment.newInstance(
                                productListData, productStyleView
                        )).commit();
            } else if (productListData == null && !operatorListData.isEmpty()) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        DigitalChooserOperatorFragment.newInstance(
                                operatorListData, operatorStyleView, categoryState
                        )).commit();
            }
        }
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
    public void onOperatorItemSelected(Operator operator) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_OPERATOR_DATA, operator));
        finish();
    }

    @Override
    public void onOperatortItemChooserCanceled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onProductItemSelected(Product product) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_PRODUCT_DATA, product));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateTitleToolBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_STATE_TITLE_TOOLBAR, titleToolbar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.titleToolbar = savedInstanceState.getString(EXTRA_STATE_TITLE_TOOLBAR);
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
