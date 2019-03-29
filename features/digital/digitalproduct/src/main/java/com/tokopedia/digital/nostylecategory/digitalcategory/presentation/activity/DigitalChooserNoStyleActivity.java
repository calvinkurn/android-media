package com.tokopedia.digital.nostylecategory.digitalcategory.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.digital.R;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalOperatorChooserNoStyleFragment;
import com.tokopedia.digital.nostylecategory.digitalcategory.presentation.fragment.DigitalProductChooserNoStyleFragment;

/**
 * Created by Rizky on 06/09/18.
 */
public class DigitalChooserNoStyleActivity extends BaseSimpleActivity
        implements DigitalOperatorChooserNoStyleFragment.ActionListener, DigitalProductChooserNoStyleFragment.ActionListener {

    private static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    private static final String EXTRA_OPERATOR_ID = "EXTRA_OPERATOR_ID";
    private static final String EXTRA_OPERATOR_LABEL = "EXTRA_OPERATOR_LABEL";
    private static final String EXTRA_TITLE_CHOOSER = "EXTRA_TITLE_CHOOSER";
    private static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    private static final String EXTRA_POSITION= "EXTRA_POSITION";

    public static final String EXTRA_CALLBACK_OPERATOR_DATA = "EXTRA_CALLBACK_OPERATOR_DATA";
    public static final String EXTRA_CALLBACK_PRODUCT_DATA = "EXTRA_CALLBACK_PRODUCT_DATA";
    public static final String EXTRA_CALLBACK_POSITION = "EXTRA_CALLBACK_POSITION";

    private String categoryId;
    private String operatorId;
    private String operatorLabel;
    private String categoryName;
    private String titleToolbar;
    private int position;

    public static Intent newInstanceProductChooser(
            Activity activity, String categoryId, String operatorId, String titleChooser
    ) {
        Intent intent = new Intent(activity, DigitalChooserNoStyleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(EXTRA_TITLE_CHOOSER, titleChooser);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceProductChooser2(FragmentActivity activity, String categoryId,
                                                    String operatorId, String titleChooser, int position) {
        Intent intent = new Intent(activity, DigitalChooserNoStyleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(EXTRA_OPERATOR_ID, operatorId);
        bundle.putString(EXTRA_TITLE_CHOOSER, titleChooser);
        bundle.putInt(EXTRA_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newInstanceOperatorChooser(Activity activity, String categoryId, String titleChooser,
                                                    String operatorLabel, String categoryName) {
        Intent intent = new Intent(activity, DigitalChooserNoStyleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(EXTRA_TITLE_CHOOSER, titleChooser);
        bundle.putString(EXTRA_OPERATOR_LABEL, operatorLabel);
        bundle.putString(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                this.categoryId = extras.getString(EXTRA_CATEGORY_ID);
                this.operatorId = extras.getString(EXTRA_OPERATOR_ID);
                this.operatorLabel = extras.getString(EXTRA_OPERATOR_LABEL);
                this.categoryName = extras.getString(EXTRA_CATEGORY_NAME);
                this.position = extras.getInt(EXTRA_POSITION);
                if (titleToolbar == null) titleToolbar = extras.getString(EXTRA_TITLE_CHOOSER);
            }
        }

        super.onCreate(savedInstanceState);

        invalidateHomeUpToolbarIndicator();
        invalidateTitleToolBar();
    }

    private void invalidateTitleToolBar() {
        if (!TextUtils.isEmpty(titleToolbar)) toolbar.setTitle(titleToolbar);
    }

    @Override
    protected Fragment getNewFragment() {
        Fragment fragment = null;
        if (categoryId != null & operatorId != null) {
            fragment = DigitalProductChooserNoStyleFragment.newInstance(
                    categoryId, operatorId);
        } else if (categoryId != null) {
            fragment = DigitalOperatorChooserNoStyleFragment.newInstance(
                    categoryId, operatorLabel, categoryName);
        }
        return fragment;
    }

    @Override
    public void onOperatorItemSelected(Operator operator) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_CALLBACK_OPERATOR_DATA, operator));
        finish();
        overridePendingTransition(R.anim.digital_anim_stay, R.anim.digital_slide_out_up );
    }

    @Override
    public void onProductItemSelected(Product product) {
        setResult(RESULT_OK, new Intent()
                .putExtra(EXTRA_CALLBACK_PRODUCT_DATA, product)
                .putExtra(EXTRA_CALLBACK_POSITION, position));
        finish();
        overridePendingTransition(R.anim.digital_anim_stay, R.anim.digital_slide_out_up );
    }

    private void invalidateHomeUpToolbarIndicator(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this,
                    com.tokopedia.abstraction.R.drawable.ic_close_default));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.digital_anim_stay, R.anim.digital_slide_out_up );
    }

}
