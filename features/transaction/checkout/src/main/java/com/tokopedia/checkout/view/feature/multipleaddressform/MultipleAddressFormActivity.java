package com.tokopedia.checkout.view.feature.multipleaddressform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.checkout.R;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartPromoSuggestion;
import com.tokopedia.checkout.domain.datamodel.cartsingleshipment.ShipmentCostModel;
import com.tokopedia.checkout.view.common.base.BaseCheckoutActivity;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentDonationModel;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.promocheckout.common.view.model.PromoData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.promocheckout.common.view.model.PromoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 2/22/18. Tokopedia
 */

public class MultipleAddressFormActivity extends BaseCheckoutActivity {
    public static final int REQUEST_CODE = 982;

    public static final String EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA";
    public static final String EXTRA_PROMO_SUGGESTION_DATA = "EXTRA_PROMO_SUGGESTION_DATA";
    public static final String EXTRA_RECIPIENT_ADDRESS_DATA = "EXTRA_RECIPIENT_ADDRESS_DATA";
    public static final String EXTRA_SHIPMENT_CART_TEM_LIST_DATA = "EXTRA_SHIPMENT_CART_TEM_LIST_DATA";
    public static final String EXTRA_SHIPMENT_COST_SATA = "EXTRA_SHIPMENT_COST_SATA";
    public static final String EXTRA_SHIPMENT_DONATION_DATA = "EXTRA_SHIPMENT_DONATION_DATA";
    public static final String EXTRA_SHIPMENT_CHECKOUT_BUTTON_DATA = "EXTRA_SHIPMENT_CHECKOUT_BUTTON_DATA";
    public static final String EXTRA_SHIPMENT_CART_IDS = "EXTRA_SHIPMENT_CART_IDS";

    public static final int RESULT_CODE_SUCCESS_SET_SHIPPING = 22;
    public static final int RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM = 23;
    public static final int RESULT_CODE_RELOAD_CART_PAGE = 24;

    private RecipientAddressModel addressData;
    private String cartIds;
    private MultipleAddressFragment fragment;

    public static Intent createInstance(Context context,
                                        PromoData promoData,
                                        CartPromoSuggestion cartPromoSuggestion,
                                        RecipientAddressModel recipientAddressData,
                                        List<ShipmentCartItemModel> shipmentCartItemModels,
                                        ShipmentCostModel shipmentCostModel,
                                        ShipmentDonationModel shipmentDonationModel,
                                        String cartIds
    ) {
        Intent intent = new Intent(context, MultipleAddressFormActivity.class);
        intent.putExtra(EXTRA_PROMO_DATA, promoData);
        intent.putExtra(EXTRA_PROMO_SUGGESTION_DATA, cartPromoSuggestion);
        intent.putExtra(EXTRA_RECIPIENT_ADDRESS_DATA, recipientAddressData);
        intent.putExtra(EXTRA_SHIPMENT_CART_TEM_LIST_DATA, new ArrayList<>(shipmentCartItemModels));
        intent.putExtra(EXTRA_SHIPMENT_COST_SATA, shipmentCostModel);
        intent.putExtra(EXTRA_SHIPMENT_DONATION_DATA, shipmentDonationModel);
        intent.putExtra(EXTRA_SHIPMENT_CART_IDS, cartIds);
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
        this.addressData = extras.getParcelable(EXTRA_RECIPIENT_ADDRESS_DATA);
        this.cartIds = extras.getString(EXTRA_SHIPMENT_CART_IDS);
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
        dialog.setTitle(getString(R.string.dialog_title_back_to_cart));
        dialog.setDesc(getString(R.string.dialog_message_back_to_cart));
        dialog.setBtnCancel(getString(R.string.label_dialog_back_to_cart_button_positive));
        dialog.setBtnOk(getString(R.string.label_dialog_back_to_cart_button_negative));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment != null) {
                    fragment.deleteChanges();
                }
                setActivityResult();
                dialog.dismiss();
            }
        });
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragment != null) {
                    fragment.stayInPage();
                }
                dialog.dismiss();
            }
        });
        dialog.getAlertDialog().setCancelable(true);
        dialog.getAlertDialog().setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void setActivityResult() {
        Intent resultIntent = new Intent();
        if (getIntent().hasExtra(EXTRA_PROMO_DATA)) {
            resultIntent.putExtra(EXTRA_PROMO_DATA, (PromoData) getIntent().getParcelableExtra(EXTRA_PROMO_DATA));
        }
        if (getIntent().hasExtra(EXTRA_PROMO_SUGGESTION_DATA)) {
            resultIntent.putExtra(EXTRA_PROMO_SUGGESTION_DATA, (CartPromoSuggestion) getIntent().getParcelableExtra(EXTRA_PROMO_SUGGESTION_DATA));
        }
        if (getIntent().hasExtra(EXTRA_RECIPIENT_ADDRESS_DATA)) {
            resultIntent.putExtra(EXTRA_RECIPIENT_ADDRESS_DATA, (RecipientAddressModel) getIntent().getParcelableExtra(EXTRA_RECIPIENT_ADDRESS_DATA));
        }
        if (getIntent().hasExtra(EXTRA_SHIPMENT_CART_TEM_LIST_DATA)) {
            resultIntent.putParcelableArrayListExtra(EXTRA_SHIPMENT_CART_TEM_LIST_DATA, getIntent().getParcelableArrayListExtra(EXTRA_SHIPMENT_CART_TEM_LIST_DATA));
        }
        if (getIntent().hasExtra(EXTRA_SHIPMENT_COST_SATA)) {
            resultIntent.putExtra(EXTRA_SHIPMENT_COST_SATA, (ShipmentCostModel) getIntent().getParcelableExtra(EXTRA_SHIPMENT_COST_SATA));
        }
        if (getIntent().hasExtra(EXTRA_SHIPMENT_DONATION_DATA)) {
            resultIntent.putExtra(EXTRA_SHIPMENT_DONATION_DATA, (ShipmentDonationModel) getIntent().getParcelableExtra(EXTRA_SHIPMENT_DONATION_DATA));
        }
        setResult(RESULT_CODE_FORCE_RESET_CART_ADDRESS_FORM, resultIntent);
        finish();
    }

    @Override
    protected android.support.v4.app.Fragment getNewFragment() {
        fragment = MultipleAddressFragment.newInstance(addressData, cartIds);
        return fragment;
    }
}
