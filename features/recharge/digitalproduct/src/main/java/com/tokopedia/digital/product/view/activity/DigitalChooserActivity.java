package com.tokopedia.digital.product.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.product.view.fragment.DigitalChooserOperatorFragment;
import com.tokopedia.digital.product.view.fragment.DigitalChooserProductFragment;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserActivity extends BaseSimpleActivity implements
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
            Activity activity, String categoryId, String operatorId, String titleChooser) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupBundlePass(getIntent().getExtras());
        super.onCreate(savedInstanceState);
    }

    private void setupBundlePass(Bundle extras) {
        this.categoryId = extras.getString(EXTRA_CATEGORY_ID);
        this.operatorId = extras.getString(EXTRA_OPERATOR_ID);
        this.productStyleView = extras.getString(EXTRA_PRODUCT_STYLE_VIEW);
        this.operatorStyleView = extras.getString(EXTRA_OPERATOR_STYLE_VIEW);
        this.operatorLabel = extras.getString(EXTRA_OPERATOR_LABEL);
        this.categoryName = extras.getString(EXTRA_STATE_CATEGORY);
        if (titleToolbar == null) titleToolbar = extras.getString(EXTRA_TITLE_CHOOSER);
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
        if (!TextUtils.isEmpty(titleToolbar)) updateTitle(titleToolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        if (categoryId != null & operatorId != null) {
            return
                    DigitalChooserProductFragment.newInstance(
                            categoryId, operatorId, productStyleView
                    );
        } else if (categoryId != null) {
            return
                    DigitalChooserOperatorFragment.newInstance(
                            categoryId, operatorStyleView, operatorLabel, categoryName
                    );
        }
        return null;
    }
}
