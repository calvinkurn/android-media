package com.tokopedia.digital.product.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.view.fragment.DigitalChooserOperatorFragment;
import com.tokopedia.digital.product.view.fragment.DigitalChooserProductFragment;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserActivity extends BasePresenterActivity implements
        DigitalChooserProductFragment.ActionListener, DigitalChooserOperatorFragment.ActionListener {

    private static final String EXTRA_PRODUCT_STYLE_VIEW = "EXTRA_PRODUCT_STYLE_VIEW";

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_OPERATOR_ID = "EXTRA_OPERATOR_ID";
    private static final String EXTRA_OPERATOR_LABEL = "EXTRA_OPERATOR_LABEL";
    private static final String EXTRA_OPERATOR_STYLE_VIEW = "EXTRA_OPERATOR_STYLE_VIEW";

    private static final String EXTRA_TITLE_CHOOSER = "EXTRA_TITLE_CHOOSER";
    private static final String EXTRA_STATE_CATEGORY = "EXTRA_STATE_CATEGORY";

    public static final String EXTRA_CALLBACK_PRODUCT_DATA = "EXTRA_CALLBACK_PRODUCT_DATA";
    public static final String EXTRA_CALLBACK_OPERATOR_DATA = "EXTRA_CALLBACK_OPERATOR_DATA";

    private static final String EXTRA_STATE_TITLE_TOOLBAR = "EXTRA_STATE_TITLE_TOOLBAR";

    private String categoryId;
    private String operatorId;
    private String operatorStyleView;
    private String operatorLabel;
    private String categoryName;
    private String productStyleView;
    private String titleToolbar;

    public static Intent newInstanceProductChooser(
            Activity activity, String categoryId, String operatorId, String titleChooser
    ) {
        Intent intent = new Intent(activity, DigitalChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(EXTRA_TITLE_CHOOSER, titleChooser);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceOperatorChooser(Activity activity, String categoryId, String titleChooser,
                                                    String operatorLabel, String categoryName) {
        Intent intent = new Intent(activity, DigitalChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(EXTRA_TITLE_CHOOSER, titleChooser);
        bundle.putString(EXTRA_OPERATOR_LABEL, operatorLabel);
        bundle.putString(EXTRA_STATE_CATEGORY, categoryName);
        intent.putExtras(bundle);
        return intent;
    }

    public static int sizeAsParcel(@NonNull Bundle bundle) {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeBundle(bundle);
            return parcel.dataSize();
        } finally {
            parcel.recycle();
        }
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        Log.d("DigitalChooserActivity", String.valueOf(sizeAsParcel(extras)));

        this.categoryId = extras.getString(EXTRA_CATEGORY_ID);
        this.operatorId = extras.getString(EXTRA_OPERATOR_ID);
        this.productStyleView = extras.getString(EXTRA_PRODUCT_STYLE_VIEW);
        this.operatorStyleView = extras.getString(EXTRA_OPERATOR_STYLE_VIEW);
        this.operatorLabel = extras.getString(EXTRA_OPERATOR_LABEL);
        this.categoryName = extras.getString(EXTRA_STATE_CATEGORY);
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
            if (categoryId != null & operatorId != null) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        DigitalChooserProductFragment.newInstance(
                                categoryId, operatorId, productStyleView
                        )).commit();
            } else if (categoryId != null) {
                getFragmentManager().beginTransaction().replace(R.id.container,
                        DigitalChooserOperatorFragment.newInstance(
                                categoryId, operatorStyleView, operatorLabel, categoryName
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
