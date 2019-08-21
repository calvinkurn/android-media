package com.tokopedia.tradein.view.viewcontrollers;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.logisticaddaddress.AddressConstants;
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.model.DeviceDataResponse;
import com.tokopedia.tradein.model.KYCDetails;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein.viewmodel.FinalPriceViewModel;
import com.tokopedia.tradein_common.Constants;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;

import java.util.List;

import tradein_common.router.TradeInRouter;

public class FinalPriceActivity extends BaseTradeInActivity implements Observer<DeviceDataResponse> {
    public static final int FINAL_PRICE_REQUEST_CODE = 22456;
    private final static int FLAG_ACTIVITY_KYC_FORM = 1301;
    private final static int PINPOINT_ACTIVITY_REQUEST_CODE = 1302;
    private FinalPriceViewModel viewModel;
    private String orderValue = "";
    private String deviceId = "";
    private int checkoutString = R.string.buy_now;
    private int hargeTncString = R.string.harga_tnc;
    /**
     * price_valid_until
     */
    private TextView mTvValidTill;
    private TextView mTvModelName;
    private TextView mTvSellingPrice;
    private TextView mTvDeviceReview;
    private TextView mTvModelNew;
    private TextView mTvPriceNew;
    private TextView mTvPriceExchange;
    private TextView mTvFinalAmount;
    private TextView mTvTnc;
    private Group activeGroup;

    /**
     * buy_now
     */
    private TextView mTvButtonPayOrKtp;

    public static Intent getHargaFinalIntent(Context context) {
        return new Intent(context, FinalPriceActivity.class);
    }

    @Override
    protected void initView() {
        mTvValidTill = findViewById(R.id.tv_valid_till);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvSellingPrice = findViewById(R.id.tv_selling_price);
        mTvDeviceReview = findViewById(R.id.tv_device_review);
        mTvModelNew = findViewById(R.id.tv_model_new);
        mTvPriceNew = findViewById(R.id.tv_price_new);
        mTvPriceExchange = findViewById(R.id.tv_price_exchange);
        mTvFinalAmount = findViewById(R.id.tv_final_amount);
        mTvButtonPayOrKtp = findViewById(R.id.tv_button_pay_or_ktp);
        mTvTnc = findViewById(R.id.tv_tnc);
        if (TRADEIN_TYPE == 2)
            activeGroup = findViewById(R.id.group_moneyin);
        else
            activeGroup = findViewById(R.id.group_tradein);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TRADEIN_TYPE == 2) {
            checkoutString = R.string.sell_now;
            hargeTncString = R.string.harga_tnc_moneyin;
        }
        viewModel.getDeviceDiagData().observe(this, this);
        viewModel.getAddressLiveData().observe(this, result -> {
            if (result != null) {
                if (result.getDefaultAddress() != null) {
                    //start money in checkout with address object
                    Intent goToCheckout = new Intent(this, MoneyInCheckoutActivity.class);
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_DEFAULT_ADDRESS, result.getDefaultAddress());
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_ORDER_VALUE, orderValue);
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_HARDWARE_ID, deviceId);
                    navigateToActivityRequest(goToCheckout, MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT);
                } else {
                    startActivityForResult(PinpointMapActivity.Companion.newInstance(this,
                            AddressConstants.MONAS_LAT, AddressConstants.MONAS_LONG, true, result.getToken(),
                            false, false, false, null,
                            false), PINPOINT_ACTIVITY_REQUEST_CODE);
                }
            }

        });
    }

    @Override
    protected Class<FinalPriceViewModel> getViewModelType() {
        return FinalPriceViewModel.class;
    }

    @Override
    protected void setViewModel(BaseViewModel viewModel) {
        this.viewModel = (FinalPriceViewModel) viewModel;
        getLifecycle().addObserver(this.viewModel);
    }

    @Override
    protected int getMenuRes() {
        return -1;
    }

    @Override
    protected Fragment getTncFragmentInstance(int TncResId) {
        return TnCFragment.getInstance(TncResId);
    }

    @Override
    protected int getBottomSheetLayoutRes() {
        return 0;
    }

    @Override
    protected boolean doNeedReattach() {
        return false;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_activity_tradein_final;
    }

    @Override
    public void onChanged(@Nullable DeviceDataResponse deviceDataResponse) {
        renderDetails(deviceDataResponse);
    }

    private void renderDetails(DeviceDataResponse deviceDataResponse) {
        TradeInParams tradeInData = viewModel.getTradeInParams();
        if (tradeInData != null && TRADEIN_TYPE != 2) {
            mTvModelNew.setText(tradeInData.getProductName());
            mTvPriceNew.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeInData.getNewPrice(), true));
        }
        mTvModelName.setText(deviceDataResponse.getDeviceAttr().getModel());
        mTvPriceExchange.setText(String.valueOf(deviceDataResponse.getOldPrice()));
        orderValue = deviceDataResponse.getOldPrice().toString();
        deviceId = deviceDataResponse.getDeviceAttr().getImei().get(0);
        mTvSellingPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true));
        mTvValidTill.setText(String.format(getString(R.string.price_valid_until), deviceDataResponse.getExpiryTimeFmt()));
        List<String> deviceReview = deviceDataResponse.getDeviceReview();
        StringBuilder stringBuilder = new StringBuilder();
        for (String review : deviceReview) {
            stringBuilder.append("•").append(review).append("\n");
        }
        mTvDeviceReview.setText(stringBuilder.toString());
        mTvPriceExchange.setText(String.format("- %1$s", CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true)));
        mTvFinalAmount.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getRemainingPrice(), true));

        if (tradeInData != null) {
            KYCDetails kycDetails = deviceDataResponse.getKycDetails();
            if (kycDetails == null) {
                setbuttonCheckout();
            } else {
                if (kycDetails.getDetail().getIsSuccess() != 1) {
                    setButtonKyc();
                } else {
                    if (kycDetails.getDetail().getStatus() == 1 || kycDetails.getDetail().getStatus() == 0)
                        setbuttonCheckout();
                    else
                        setButtonKyc();

                }
            }
        }
        activeGroup.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    private void goToKycActivity() {
        TradeInRouter router = (TradeInRouter) getApplication();
        Intent intent = router.getKYCIntent(this, 4);
        startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FLAG_ACTIVITY_KYC_FORM:
                    setbuttonCheckout();
                    goToCheckout();
                    break;
                case MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT:
                    setResult(Activity.RESULT_OK, null);
                    finish();
                    break;
                case PINPOINT_ACTIVITY_REQUEST_CODE:
                    Parcelable parcelableExtra = data.getParcelableExtra("EXTRA_ADDRESS_NEW");
                    if (data.hasExtra("EXTRA_ADDRESS_NEW") && parcelableExtra != null) {
                        Intent goToCheckout = new Intent(this, MoneyInCheckoutActivity.class);
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_NEW_ADDRESS, parcelableExtra);
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_ORDER_VALUE, orderValue);
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_HARDWARE_ID, deviceId);
                        navigateToActivityRequest(goToCheckout, MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT);
                    }
                    break;

            }
        }
    }

    private void goToCheckout() {
        if (TRADEIN_TYPE == 2) {
            viewModel.getAddress();
        } else {
            setResult(Activity.RESULT_OK, new Intent(Constants.ACTION_GO_TO_SHIPMENT).putExtra(TradeInParams.PARAM_DEVICE_ID, deviceId));
            finish();
        }

    }

    private void setbuttonCheckout() {
        String notElligible = getString(hargeTncString);
        SpannableString spannableString = new SpannableString(notElligible);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showtnc();

            }
        };
        int greenColor = getResources().getColor(R.color.green_nob);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
        spannableString.setSpan(clickableSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTnc.setText(spannableString);
        mTvTnc.setClickable(true);
        mTvTnc.setMovementMethod(LinkMovementMethod.getInstance());
        mTvButtonPayOrKtp.setBackgroundResource(R.drawable.bg_tradein_button_orange);
        mTvButtonPayOrKtp.setText(checkoutString);
        mTvButtonPayOrKtp.setOnClickListener(v -> {
            goToCheckout();
        });
    }

    private void setButtonKyc() {
        String notElligible = getString(R.string.harga_tnc_kyc);
        SpannableString spannableString = new SpannableString(notElligible);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showtnc();

            }
        };
        int greenColor = getResources().getColor(R.color.green_nob);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
        spannableString.setSpan(clickableSpan, 40, 58, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, 40, 58, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTnc.setText(spannableString);
        mTvTnc.setClickable(true);
        mTvTnc.setMovementMethod(LinkMovementMethod.getInstance());
        mTvButtonPayOrKtp.setBackgroundResource(R.drawable.bg_tradein_button_green);
        mTvButtonPayOrKtp.setText(R.string.do_ktp);
        mTvButtonPayOrKtp.setOnClickListener(v -> {
            goToKycActivity();
            sendGeneralEvent("clickTradeIn",
                    "harga final trade in",
                    "click lanjut foto ktp",
                    "");

        });
    }

    @Override
    public void onBackPressed() {
        if (isTncShowing) {
            activeGroup.setVisibility(View.VISIBLE);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isTncShowing) {
                activeGroup.setVisibility(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showtnc() {
        showTnC(R.string.tradein_tnc);
        activeGroup.setVisibility(View.INVISIBLE);
        sendGeneralEvent("clickTradeIn",
                "harga final trade in",
                "click syarat dan ketentuan",
                "");
    }
}
