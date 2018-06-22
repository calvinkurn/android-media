package com.tokopedia.checkout.view.view.addressoptions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.view.base.BaseCheckoutActivity;
import com.tokopedia.core.manage.people.address.activity.AddAddressActivity;
import com.tokopedia.core.manage.people.address.model.Token;

import static com.tokopedia.core.manage.people.address.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 07/02/18
 */

public class CartAddressChoiceActivity extends BaseCheckoutActivity
        implements ICartAddressChoiceActivityListener {

    public static final int REQUEST_CODE = 981;

    public static final int RESULT_CODE_ACTION_SELECT_ADDRESS = 100;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 101;
    public static final int RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS = 102;

    private static final String EXTRA_TYPE_REQUEST = "EXTRA_TYPE_REQUEST";
    public static final String EXTRA_DEFAULT_SELECTED_ADDRESS = "EXTRA_DEFAULT_SELECTED_ADDRESS";
    public static final String EXTRA_NAVIGATION_FROM_ADDRESS_LIST = "EXTRA_NAVIGATION_FROM_ADDRESS_LIST";
    public static final String EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA";
    public static final String EXTRA_CURRENT_ADDRESS = "CURRENT_ADDRESS";
    public static final String EXTRA_DISCOM_TOKEN = "discom_token";

    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST = 0;
    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST = 1;
    public static final int TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS = 2;

    private int typeRequest;
    private Token token;

    public static Intent createInstance(Activity activity, RecipientAddressModel currentAddress, int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        if (currentAddress != null) {
            intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        }

        return intent;
    }

    public static Intent createInstance(Activity activity, RecipientAddressModel currentAddress, int typeRequest, Token token) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        intent.putExtra(EXTRA_DISCOM_TOKEN, token);
        if (currentAddress != null) {
            intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        }

        return intent;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.typeRequest = extras.getInt(EXTRA_TYPE_REQUEST);
        this.token = extras.getParcelable(EXTRA_DISCOM_TOKEN);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        updateTitle(title.toString());
    }

    @Override
    protected void initView() {
        switch (typeRequest) {
            case TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS:
                startActivityForResult(AddAddressActivity.createInstance(this, token),
                        REQUEST_CODE_PARAM_CREATE);
                break;

            default:
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PARAM_CREATE) {
            if (resultCode == Activity.RESULT_OK) setResult(RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS);
            finish();
        }
    }

    @Override
    public void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult) {
        switch (typeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_SHORT_LIST:
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_DEFAULT_SELECTED_ADDRESS, selectedAddressResult);
                bundle.putBoolean(EXTRA_NAVIGATION_FROM_ADDRESS_LIST, true);

                Fragment fragment = CartAddressChoiceFragment.newInstance(selectedAddressResult);
                fragment.setArguments(bundle);

                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment, fragment.getClass().getSimpleName())
                        .commit();
                break;

            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
                finish();
                break;

            default:
        }
    }

    @Override
    public void finishSendResultActionToMultipleAddressForm() {
        setResult(RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM);
        finish();
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        switch (typeRequest) {
            default:
                return CartAddressChoiceFragment.newInstance(
                        getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS));
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                return ShipmentAddressListFragment.newInstance(
                        (RecipientAddressModel) getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS));

        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof CartAddressChoiceFragment) {
            ((CartAddressChoiceFragment) getCurrentFragment())
                    .checkoutAnalyticsChangeAddress.eventClickChangeAddressClickArrowBackFromGantiAlamat();
        }
        setToolbarTitle(getString(R.string.checkout_module_title_shipping_dest));
        super.onBackPressed();

    }
}