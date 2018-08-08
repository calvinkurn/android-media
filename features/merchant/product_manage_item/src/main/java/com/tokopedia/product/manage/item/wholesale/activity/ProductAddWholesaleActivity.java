package com.tokopedia.product.manage.item.wholesale.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef;
import com.tokopedia.product.manage.item.main.base.data.model.ProductWholesaleViewModel;
import com.tokopedia.product.manage.item.wholesale.fragment.ProductAddWholesaleFragment;

import java.util.ArrayList;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleActivity extends BaseSimpleActivity {

    public static final String EXTRA_PRODUCT_WHOLESALE_LIST = "EXTRA_PRODUCT_WHOLESALE_LIST";
    public static final String EXTRA_PRODUCT_MAIN_PRICE = "EXTRA_PRODUCT_MAIN_PRICE";
    public static final String EXTRA_PRODUCT_CURRENCY = "EXTRA_PRODUCT_CURRENCY";
    public static final String EXTRA_OFFICIAL_STORE = "EXTRA_OFFICIAL_STORE";
    public static final String EXTRA_HAS_VARIANT = "EXTRA_HAS_VARIANT";

    @Override
    protected Fragment getNewFragment() {
        return ProductAddWholesaleFragment.newInstance();
    }

    public static Intent getIntent(Context context, ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList, @CurrencyTypeDef int currencyType, double productPrice, boolean officialStore, boolean hasVariant) {
        Intent intent = new Intent(context, ProductAddWholesaleActivity.class);
        intent.putExtra(EXTRA_PRODUCT_WHOLESALE_LIST, productWholesaleViewModelList);
        intent.putExtra(EXTRA_PRODUCT_CURRENCY, currencyType);
        intent.putExtra(EXTRA_PRODUCT_MAIN_PRICE, productPrice);
        intent.putExtra(EXTRA_OFFICIAL_STORE, officialStore);
        intent.putExtra(EXTRA_HAS_VARIANT, hasVariant);
        return intent;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getFragment();
        if (fragment != null && fragment instanceof ProductAddWholesaleFragment) {
            if (!((ProductAddWholesaleFragment) fragment).isAnyWholesaleChange()) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                        .setTitle(getString(R.string.product_dialog_cancel_title))
                        .setMessage(getString(R.string.product_dialog_cancel_message))
                        .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProductAddWholesaleActivity.super.onBackPressed();
                            }
                        }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // no op, just dismiss
                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            } else {
                super.onBackPressed();
            }
        }
    }

}
