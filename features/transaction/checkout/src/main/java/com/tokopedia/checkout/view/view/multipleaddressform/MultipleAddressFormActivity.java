package com.tokopedia.checkout.view.view.multipleaddressform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.core.manage.people.address.model.Token;
import com.tokopedia.design.component.Dialog;

/**
 * Created by kris on 2/22/18. Tokopedia
 */

public class MultipleAddressFormActivity extends BaseCheckoutActivity {
    public static final int REQUEST_CODE = 982;

    private static final String EXTRA_CART_LIST_DATA = "EXTRA_CART_LIST_DATA";
    private static final String EXTRA_RECIPIENT_ADDRESS_DATA = "EXTRA_RECIPIENT_ADDRESS_DATA";
    private static final String EXTRA_DISTRICT_RECOMMENDATION_TOKEN = "EXTRA_DISTRICT_RECOMMENDATION_TOKEN";
    public static final int RESULT_CODE_SUCCESS_SET_SHIPPING = 22;
    public static final int RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM = 23;

    private CartListData cartListData;
    private RecipientAddressModel addressData;
    private MultipleAddressFragment fragment;

    public static Intent createInstance(Context context,
                                        CartListData cartListData,
                                        RecipientAddressModel recipientAddressData) {
        Intent intent = new Intent(context, MultipleAddressFormActivity.class);
        intent.putExtra(EXTRA_CART_LIST_DATA, cartListData);
        intent.putExtra(EXTRA_RECIPIENT_ADDRESS_DATA, recipientAddressData);
        return intent;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.cartListData = extras.getParcelable(EXTRA_CART_LIST_DATA);
        this.addressData = extras.getParcelable(EXTRA_RECIPIENT_ADDRESS_DATA);
    }

    @Override
    protected void initView() {

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
        final Dialog dialog = new Dialog(this, Dialog.Type.LONG_PROMINANCE);
        if (fragment != null) {
            fragment.backPressed();
        }
        dialog.setTitle(getString(R.string.dialog_title_back_to_choose_address));
        dialog.setDesc(getString(R.string.dialog_message_back_to_cart));
        dialog.setBtnCancel(getString(R.string.label_dialog_back_to_cart_button_positive));
        dialog.setBtnOk(getString(R.string.label_dialog_back_to_cart_button_negative));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM);
                if (fragment != null) {
                    fragment.deleteChanges();
                }
                finish();
                dialog.dismiss();
            }
        });
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.stayInPage();
                dialog.dismiss();
            }
        });
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        fragment = MultipleAddressFragment.newInstance(cartListData, addressData);
        return fragment;
    }
}
