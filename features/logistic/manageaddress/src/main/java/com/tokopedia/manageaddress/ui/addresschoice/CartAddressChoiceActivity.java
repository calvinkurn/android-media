package com.tokopedia.manageaddress.ui.addresschoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel;
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils;
import com.tokopedia.logisticCommon.data.constant.LogisticConstant;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticCommon.data.entity.address.Token;
import com.tokopedia.manageaddress.R;
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper;
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListFragment;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;

import static com.tokopedia.logisticCommon.data.constant.LogisticConstant.INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT;
import static com.tokopedia.purchase_platform.common.constant.CartConstant.SCREEN_NAME_CART_NEW_USER;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF;
import static com.tokopedia.purchase_platform.common.constant.CheckoutConstant.KERO_TOKEN;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 07/02/18
 * Fajar U N
 */
public class CartAddressChoiceActivity extends BaseCheckoutActivity
        implements ShipmentAddressListFragment.ICartAddressChoiceActivityListener {

    // Attention !!
    // If these constants will be used on other module, please move into CheckoutConstant.kt class on package purchase_platform_common
    public static final int RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS = 102;
    public static final int RESULT_CODE_ACTION_EDIT_ADDRESS = 103;

    private int typeRequest;
    private Token token;
    private int prevState;
    private LocalCacheModel localChosenAddr;
    private String PARAM_ADDRESS_MODEL = "EDIT_PARAM";
    private CheckoutAnalyticsChangeAddress mAnalytics = new CheckoutAnalyticsChangeAddress();

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.typeRequest = extras.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST);
        this.token = extras.getParcelable(CheckoutConstant.EXTRA_DISTRICT_RECOMMENDATION_TOKEN);
        this.prevState = extras.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS);
    }

    @Override
    protected void initView() {
        updateTitle(getString(R.string.checkout_module_title_activity_shipping_address));
        Intent intent;
        switch (typeRequest) {
            case CheckoutConstant.TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS:
                intent = RouteManager.getIntent(this, ApplinkConstInternalLogistic.ADD_ADDRESS_V2);
                intent.putExtra(KERO_TOKEN, token);
                intent.putExtra(EXTRA_REF, SCREEN_NAME_CART_NEW_USER);
                startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY);
                break;
            case CheckoutConstant.TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN:
                RecipientAddressModel currentAddress = getIntent().getParcelableExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS);
                AddressModelMapper mapper = new AddressModelMapper();
                intent = RouteManager.getIntent(this, ApplinkConstInternalLogistic.ADD_ADDRESS_V1,
                        INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT);
                intent.putExtra(PARAM_ADDRESS_MODEL, mapper.transform(currentAddress));
                intent.putExtra(KERO_TOKEN, token);
                startActivityForResult(intent, LogisticConstant.REQUEST_CODE_PARAM_EDIT);
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LogisticConstant.REQUEST_CODE_PARAM_CREATE ||
                requestCode == LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY) {
            if (resultCode == Activity.RESULT_OK) setResult(RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS);
            finish();
        } else if (requestCode == LogisticConstant.REQUEST_CODE_PARAM_EDIT) {
            setResult(RESULT_CODE_ACTION_EDIT_ADDRESS);
            finish();
        }
    }

    @Override
    public void finishAndSendResult(RecipientAddressModel selectedAddressResult) {
        Intent resultIntent;
        switch (typeRequest) {
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN:
                resultIntent = new Intent();
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                setResult(CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
                finish();
                break;
            case CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS:
                resultIntent = new Intent();
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                if (getIntent().hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            getIntent().getParcelableArrayListExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST));
                }
                if (getIntent().hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX,
                            getIntent().getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX, -1));
                }
                if (getIntent().hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            getIntent().getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            case CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT:
                resultIntent = new Intent();
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                if (getIntent().hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST,
                            getIntent().getParcelableArrayListExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_DATA_LIST));
                }
                if (getIntent().hasExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX)) {
                    resultIntent.putExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX,
                            getIntent().getIntExtra(CheckoutConstant.EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX, -1));
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
            default:
        }
    }

    @Override
    protected Fragment getNewFragment() {
        RecipientAddressModel currentAddress = getIntent().getParcelableExtra(CheckoutConstant.EXTRA_CURRENT_ADDRESS);
        switch (typeRequest) {
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                return ShipmentAddressListFragment.newInstance(currentAddress, prevState);
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN:
                return ShipmentAddressListFragment.newInstance(currentAddress, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN);
            case CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT:
            case CheckoutConstant.TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS:
                return ShipmentAddressListFragment.newInstanceFromMultipleAddressForm(currentAddress, prevState);
            default:
                return ShipmentAddressListFragment.newInstance(currentAddress, prevState);
        }
    }

    @Override
    public void onBackPressed() {
        mAnalytics.eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat();
        super.onBackPressed();
    }
}