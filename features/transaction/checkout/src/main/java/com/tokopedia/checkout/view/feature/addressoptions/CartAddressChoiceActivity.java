package com.tokopedia.checkout.view.feature.addressoptions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.checkout.domain.datamodel.MultipleAddressAdapterData;
import com.tokopedia.checkout.router.ICheckoutModuleRouter;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity;
import com.tokopedia.logisticcommon.LogisticCommonConstant;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

import java.util.ArrayList;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 07/02/18
 * Fajar U N
 */
public class CartAddressChoiceActivity extends BaseCheckoutActivity
        implements ShipmentAddressListFragment.ICartAddressChoiceActivityListener {

    public static final int REQUEST_CODE = 981;

    public static final int RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS = 102;
    public static final int RESULT_CODE_ACTION_SELECT_ADDRESS = 100;
    public static final int RESULT_CODE_ACTION_TO_MULTIPLE_ADDRESS_FORM = 101;
    public static final int RESULT_CODE_ACTION_EDIT_ADDRESS = 103;

    private static final String EXTRA_TYPE_REQUEST = "EXTRA_TYPE_REQUEST";
    public static final String EXTRA_CURRENT_ADDRESS = "CURRENT_ADDRESS";
    public static final String EXTRA_DISTRICT_RECOMMENDATION_TOKEN = "DISTRICT_RECOMMENDATION_TOKEN";
    public static final String EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX = "EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX";
    public static final String EXTRA_MULTIPLE_ADDRESS_DATA_LIST = "EXTRA_MULTIPLE_ADDRESS_DATA_LIST";
    public static final String EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX = "EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX";
    public static final String EXTRA_SELECTED_ADDRESS_DATA = "EXTRA_SELECTED_ADDRESS_DATA";

    public static final int TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS = 1;
    public static final int TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT = 3;
    public static final int TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS = 2;
    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST = 0;
    public static final int TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN = 4;

    private int typeRequest;
    private Token token;

    public static Intent createInstance(Activity activity,
                                        ArrayList<MultipleAddressAdapterData> dataList,
                                        int parentPosition) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT);
        intent.putExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST, dataList);
        intent.putExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, parentPosition);
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        RecipientAddressModel currentAddress,
                                        ArrayList<MultipleAddressAdapterData> dataList,
                                        int childPosition,
                                        int parentPosition) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS);
        intent.putExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST, dataList);
        intent.putExtra(EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, childPosition);
        intent.putExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, parentPosition);
        intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        RecipientAddressModel currentAddress,
                                        int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        if (currentAddress != null) {
            intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        }
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        int typeRequest,
                                        Token token) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(EXTRA_TYPE_REQUEST, typeRequest);
        intent.putExtra(EXTRA_DISTRICT_RECOMMENDATION_TOKEN, token);
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
        this.typeRequest = extras.getInt(EXTRA_TYPE_REQUEST);
        this.token = extras.getParcelable(EXTRA_DISTRICT_RECOMMENDATION_TOKEN);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        updateTitle(getString(R.string.checkout_module_title_shipping_dest_multiple_address));
    }

    @Override
    protected void initView() {
        Intent intent;
        switch (typeRequest) {
            case TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS:
                intent = AddAddressActivity
                        .createInstanceAddAddressFromCheckoutSingleAddressFormWhenDefaultAddressIsEmpty(
                                this, token);
                startActivityForResult(intent,
                        LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE);
                break;
            case TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN:
                RecipientAddressModel currentAddress = getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS);
                AddressModelMapper mapper = new AddressModelMapper();
                intent = AddAddressActivity.createInstanceEditAddressFromCheckoutSingleAddressForm(
                        this, mapper.transform(currentAddress), token
                );
                startActivityForResult(intent,
                        LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT);
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
        if (requestCode == LogisticCommonConstant.REQUEST_CODE_PARAM_CREATE) {
            if (resultCode == Activity.RESULT_OK) setResult(RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS);
            finish();
        } else if (requestCode == LogisticCommonConstant.REQUEST_CODE_PARAM_EDIT) {
            setResult(RESULT_CODE_ACTION_EDIT_ADDRESS);
            finish();
        }
    }

    @Override
    public void finishSendResultActionSelectedAddress(RecipientAddressModel selectedAddressResult) {
        Intent resultIntent;
        switch (typeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                setResult(RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
                finish();
                break;
            case TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS:
                resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                if (getIntent().hasExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            getIntent().getParcelableArrayListExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST));
                }
                if (getIntent().hasExtra(EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX)) {
                    resultIntent.putExtra(EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX,
                            getIntent().getIntExtra(EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, -1));
                }
                if (getIntent().hasExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            getIntent().getIntExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            case TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT:
                resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                if (getIntent().hasExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            getIntent().getParcelableArrayListExtra(EXTRA_MULTIPLE_ADDRESS_DATA_LIST));
                }
                if (getIntent().hasExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            getIntent().getIntExtra(EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            default:
        }
    }

    @Override
    protected Fragment getNewFragment() {
        RecipientAddressModel currentAddress = getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS);
        switch (typeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                return ShipmentAddressListFragment.newInstance(currentAddress);
            case TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT:
            case TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS:
                return ShipmentAddressListFragment.newInstanceFromMultipleAddressForm(currentAddress, true);
            default:
                return ShipmentAddressListFragment.newInstance(currentAddress);
        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof ShipmentAddressListFragment) {
            ((ShipmentAddressListFragment) getCurrentFragment())
                    .checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat();
            super.onBackPressed();
        }
    }

}