package com.tokopedia.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.facade.FacadeCreditCard;
import com.tokopedia.core.fragment.FragmentCreditCard;
import com.tokopedia.core.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.core.util.CreditCardUtils;

import org.json.JSONObject;

import java.util.ArrayList;


public class CreditCardActivity extends TActivity {

    public static class Param {
        public String clientKey;
        public String amount;
        public String ccData;
        public String tokenCart;

        public ArrayList<String> BankInstallmentOptions;
    }

    public static Intent createIntent(Context context, Param param) {
        Intent intent = new Intent(context, CreditCardActivity.class);
        intent.putExtra("client_key", param.clientKey);
        intent.putExtra("amount", param.amount);
        intent.putExtra("cc_data", param.ccData);
        intent.putExtra("token_cart", param.tokenCart);
        return intent;
    }

    public static Intent createInstallmentIntent(Context context, Param param) {
        Intent intent = new Intent(context, CreditCardActivity.class);
        intent.putExtra("client_key", param.clientKey);
        intent.putExtra("amount", param.amount);
        intent.putExtra("cc_data", param.ccData);
        intent.putExtra("token_cart", param.tokenCart);
        intent.putStringArrayListExtra("installment_option", param.BankInstallmentOptions);
        intent.putExtra("installment", true);
        return intent;
    }


    public static Intent createIntentWSV4(Context context, CarStep1Data carStep1Data) {
        Intent intent = new Intent(context, CreditCardActivity.class);
        intent.putExtra("CART_STEP_1_DATA", carStep1Data);
        intent.putExtra("is_wsv4", true);
        return intent;
    }

    public static Intent createInstallmentIntentWSV4(Context context, CarStep1Data carStep1Data) {
        Intent intent = new Intent(context, CreditCardActivity.class);
        intent.putExtra("CART_STEP_1_DATA", carStep1Data);
        intent.putExtra("installment", true);
        intent.putExtra("is_wsv4", true);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_credit_card);
        setTitle("Informasi Tagihan");
        getFragmentManager().beginTransaction().add(R.id.main_view, createFragment()).commit();
    }

    private FragmentCreditCard createFragment() {
        if (getIntent().getBooleanExtra("is_wsv4", false)) {
            CarStep1Data carStep1Data = getIntent().getParcelableExtra("CART_STEP_1_DATA");
            FragmentCreditCard.Model model = new FragmentCreditCard.Model();
            if (carStep1Data.getVeritrans() != null)
                model.clientKey = carStep1Data.getVeritrans().getClientKey();
            model.paymentAmount = carStep1Data.getTransaction().getPaymentLeft();
            model.tokenCart = carStep1Data.getTransaction().getToken();
            if (carStep1Data.getCreditCardData() != null
                    && !carStep1Data.getCreditCardData().getAddress().equals("0")
                    && !carStep1Data.getCreditCardData().getPhone().equals("0")) {
                model.ccModel = new CreditCardUtils.Model();
                model.ccModel.address = carStep1Data.getCreditCardData().getAddress();
                model.ccModel.province = carStep1Data.getCreditCardData().getState();
                model.ccModel.city = carStep1Data.getCreditCardData().getCity();
                model.ccModel.forename = carStep1Data.getCreditCardData().getFirstName();
                model.ccModel.surname = carStep1Data.getCreditCardData().getLastName();
                model.ccModel.postCode = carStep1Data.getCreditCardData().getPostalCode();
                model.ccModel.phone = carStep1Data.getCreditCardData().getPhone();
            } else {
                model.ccModel = new CreditCardUtils.Model();
            }
            if (carStep1Data.getTransaction().getInstallmentBankOptionList() != null
                    && !carStep1Data.getTransaction().getInstallmentBankOptionList().isEmpty()) {
                model.installmentBankOptionList = carStep1Data.getTransaction().getInstallmentBankOptionList();
            }
            return FragmentCreditCard.createInstanceWSV4(model, (getIntent().getBooleanExtra("installment", false)));
        } else {
            FragmentCreditCard.Model model = new FragmentCreditCard.Model();
            model.clientKey = getIntent().getStringExtra("client_key");
            model.paymentAmount = getIntent().getStringExtra("amount");
            model.tokenCart = getIntent().getStringExtra("token_cart");
            if (getIntent().hasExtra("installment")) {
                model.BankInstallmentOptions = getIntent().getStringArrayListExtra("installment_option");
            }

            try {
                if (getIntent().getStringExtra("cc_data") != null) {
                    JSONObject ccData = new JSONObject(getIntent().getStringExtra("cc_data"));
                    model.ccModel = FacadeCreditCard.getCreditCardModel(ccData);
                } else {
                    model.ccModel = new CreditCardUtils.Model();
                }
            } catch (Exception e) {
                model.ccModel = new CreditCardUtils.Model();
                e.printStackTrace();
            }

            return FragmentCreditCard.createInstance(model);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.warning_credit_card_web_view));
            builder.setPositiveButton("Ya", positiveButtonListener());
            builder.setNegativeButton("Tidak", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private DialogInterface.OnClickListener positiveButtonListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
    }

}
