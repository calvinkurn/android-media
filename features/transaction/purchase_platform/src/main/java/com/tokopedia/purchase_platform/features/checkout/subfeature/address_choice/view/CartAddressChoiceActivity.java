package com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticdata.data.constant.LogisticConstant;
import com.tokopedia.logisticdata.data.entity.address.Token;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress;
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity;
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant;
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.mapper.AddressModelMapper;
import com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list.CornerListFragment;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.MultipleAddressAdapterData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.tokopedia.logisticdata.data.constant.LogisticConstant.INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS;
import static com.tokopedia.logisticdata.data.constant.LogisticConstant.INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT;
import static com.tokopedia.purchase_platform.common.constant.CartConstant.SCREEN_NAME_CART_NEW_USER;
import static com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_ADD_NEW_ADDRESS_KEY;

/**
 * @author Irfan Khoirul on 05/02/18
 * Aghny A. Putra on 07/02/18
 * Fajar U N
 */
public class CartAddressChoiceActivity extends BaseCheckoutActivity
        implements ShipmentAddressListFragment.ICartAddressChoiceActivityListener, CornerListFragment.ActionListener {

    // Attention !!
    // If these constants will be used on other module, please move into CheckoutConstant.kt class on package purchase_platform_common
    public static final int RESULT_CODE_ACTION_ADD_DEFAULT_ADDRESS = 102;
    public static final int RESULT_CODE_ACTION_EDIT_ADDRESS = 103;
    public static final String EXTRA_CURRENT_ADDRESS = "CURRENT_ADDRESS";
    public static final String EXTRA_DISTRICT_RECOMMENDATION_TOKEN = "DISTRICT_RECOMMENDATION_TOKEN";
    public static final String EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX = "EXTRA_MULTIPLE_ADDRESS_CHILD_INDEX";
    public static final String EXTRA_MULTIPLE_ADDRESS_DATA_LIST = "EXTRA_MULTIPLE_ADDRESS_DATA_LIST";
    public static final String EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX = "EXTRA_MULTIPLE_ADDRESS_PARENT_INDEX";
    public static final String KERO_TOKEN = "token";
    public static final int TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS = 1;
    public static final int TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT = 3;
    public static final int TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS = 2;
    public static final int TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST = 0;
    public static final int TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN = 4;

    private static final String TAG_CORNER_FRAGMENT = "TAG_CORNER_FRAGMENT";
    private int typeRequest;
    private Token token;
    private String PARAM_ADDRESS_MODEL = "EDIT_PARAM";
    private CheckoutAnalyticsChangeAddress mAnalytics = new CheckoutAnalyticsChangeAddress();

    public static Intent createInstance(Activity activity,
                                        ArrayList<MultipleAddressAdapterData> dataList,
                                        int parentPosition) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, TYPE_REQUEST_MULTIPLE_ADDRESS_ADD_SHIPMENT);
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
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS);
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
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, typeRequest);
        if (currentAddress != null) {
            intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        }
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        RecipientAddressModel currentAddress,
                                        Token token,
                                        int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, typeRequest);
        intent.putExtra(EXTRA_DISTRICT_RECOMMENDATION_TOKEN, token);
        if (currentAddress != null) {
            intent.putExtra(EXTRA_CURRENT_ADDRESS, currentAddress);
        }
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        int typeRequest) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, typeRequest);
        return intent;
    }

    public static Intent createInstance(Activity activity,
                                        int typeRequest,
                                        Token token) {
        Intent intent = new Intent(activity, CartAddressChoiceActivity.class);
        intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, typeRequest);
        intent.putExtra(EXTRA_DISTRICT_RECOMMENDATION_TOKEN, token);
        return intent;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CornerListFragment) {
            ((CornerListFragment) fragment).setCornerListener(this);
        }
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.typeRequest = extras.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST);
        this.token = extras.getParcelable(EXTRA_DISTRICT_RECOMMENDATION_TOKEN);
    }

    @Override
    protected void initView() {
        updateTitle(getString(R.string.checkout_module_title_shipping_dest_multiple_address));
        Intent intent;
        switch (typeRequest) {
            case TYPE_REQUEST_ADD_SHIPMENT_DEFAULT_ADDRESS:
                if (isAddNewAddressEnabled()) {
                    mAnalytics.sendScreenName(this, SCREEN_NAME_CART_NEW_USER);
                    intent = RouteManager.getIntent(this, ApplinkConstInternalLogistic.ADD_ADDRESS_V2);
                    intent.putExtra(KERO_TOKEN, token);
                    startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY);
                } else {
                    intent = RouteManager.getIntent(this,
                            ApplinkConstInternalLogistic.ADD_ADDRESS_V1,
                            INSTANCE_TYPE_ADD_ADDRESS_FROM_SINGLE_CHECKOUT_EMPTY_DEFAULT_ADDRESS);
                    intent.putExtra(KERO_TOKEN, token);
                    startActivityForResult(intent, LogisticConstant.REQUEST_CODE_PARAM_CREATE);
                }

                break;
            case TYPE_REQUEST_EDIT_ADDRESS_FOR_TRADE_IN:
                RecipientAddressModel currentAddress = getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS);
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
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN:
                resultIntent = new Intent();
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
                setResult(CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS, resultIntent);
                finish();
                break;
            case TYPE_REQUEST_MULTIPLE_ADDRESS_CHANGE_ADDRESS:
                resultIntent = new Intent();
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
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
                resultIntent.putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, selectedAddressResult);
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
    public void requestCornerList() {
        updateTitle(getString(R.string.button_choose_corner));
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.stay_still)
                .replace(R.id.parent_view, CornerListFragment.newInstance(), TAG_CORNER_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCornerChosen(@NotNull RecipientAddressModel corner) {
        updateTitle(getString(R.string.checkout_module_title_shipping_dest_multiple_address));
        getSupportFragmentManager().popBackStack();
        ((ShipmentAddressListFragment) getFragment()).onChooseCorner(corner);
    }

    @Override
    protected Fragment getNewFragment() {
        RecipientAddressModel currentAddress = getIntent().getParcelableExtra(EXTRA_CURRENT_ADDRESS);
        switch (typeRequest) {
            case TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST:
                return ShipmentAddressListFragment.newInstance(currentAddress);
            case CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN:
                return ShipmentAddressListFragment.newInstance(currentAddress, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN);
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
            mAnalytics.eventClickAtcCartChangeAddressClickArrowBackFromGantiAlamat();
        } else if (getCurrentFragment() instanceof CornerListFragment) {
            updateTitle(getString(R.string.checkout_module_title_shipping_dest_multiple_address));
        }
        super.onBackPressed();
    }

    public boolean isAddNewAddressEnabled() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        return remoteConfig.getBoolean(ENABLE_ADD_NEW_ADDRESS_KEY, false);
    }
}