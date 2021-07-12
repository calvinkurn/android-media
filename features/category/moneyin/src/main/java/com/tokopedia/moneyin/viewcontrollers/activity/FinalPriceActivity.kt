package com.tokopedia.moneyin.viewcontrollers.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.common_tradein.model.DeviceAttr;
import com.tokopedia.common_tradein.model.DeviceDataResponse;
import com.tokopedia.common_tradein.model.TradeInParams;
import com.tokopedia.moneyin.R;
import com.tokopedia.moneyin.MoneyInGTMConstants;
import com.tokopedia.moneyin.viewmodel.FinalPriceViewModel;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.utils.currency.CurrencyFormatUtil;

import java.util.List;

public class FinalPriceActivity extends BaseMoneyInActivity<FinalPriceViewModel> implements Observer<DeviceDataResponse> {

    private final static int PINPOINT_ACTIVITY_REQUEST_CODE = 1302;
    public static final String PARAM_TRADEIN_PHONE_TYPE = "PARAM_TRADEIN_PHONE_TYPE";
    private final static String EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW";
    private FinalPriceViewModel viewModel;
    private String orderValue = "";
    private String deviceId = "";
    private int checkoutString = R.string.buy_now;
    private int hargeTncString = R.string.harga_tnc;
    /**
     * price_valid_until
     */
    private Typography mTvValidTill;
    private Typography mTvModelName;
    private Typography mTvSellingPrice;
    private Typography mTvDeviceReview;
    private Typography mTvFinalAmount;
    private Typography mTvTnc;

    /**
     * buy_now
     */
    private Typography mTvButtonPayOrKtp;
    private Typography tvTitle;
    private String category = MoneyInGTMConstants.CATEGORY_TRADEIN_HARGA_FINAL;
    private static final String KERO_TOKEN = "token";

    public static Intent getHargaFinalIntent(Context context) {
        return new Intent(context, FinalPriceActivity.class);
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void initView() {
        getComponent().inject(this);
        setTradeInParams();
        mTvValidTill = findViewById(R.id.tv_valid_till);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvSellingPrice = findViewById(R.id.tv_selling_price);
        mTvDeviceReview = findViewById(R.id.tv_device_review);
        mTvFinalAmount = findViewById(R.id.tv_final_amount);
        mTvButtonPayOrKtp = findViewById(R.id.tv_button_pay_or_ktp);
        mTvTnc = findViewById(R.id.tv_tnc);
        tvTitle = findViewById(R.id.tv_title);

    }

    private void setTradeInParams() {
        if (getIntent().hasExtra(TradeInParams.class.getSimpleName())) {
            viewModel.setTradeInParams(getIntent().getParcelableExtra(TradeInParams.class.getSimpleName()));
        }
        if (getIntent().hasExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE)) {
            viewModel.setTradeInType(getIntent().getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, 1));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutString = R.string.moneyin_sell_now;
        hargeTncString = R.string.moneyin_harga_tnc;
        category = MoneyInGTMConstants.CATEGORY_MONEYIN_HARGA_FINAL;
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
                    Intent intent = RouteManager.getIntent(
                            this, ApplinkConstInternalLogistic.ADD_ADDRESS_V2);
                    intent.putExtra(KERO_TOKEN, result.getToken());
                    startActivityForResult(intent, PINPOINT_ACTIVITY_REQUEST_CODE);
                }
            }

        });
    }

    @Override
    public Class<FinalPriceViewModel> getViewModelType() {
        return FinalPriceViewModel.class;
    }

    @Override
    public void setViewModel(BaseViewModel viewModel) {
        this.viewModel = (FinalPriceViewModel) viewModel;
        getLifecycle().addObserver(this.viewModel);
    }

    @Override
    protected int getMenuRes() {
        return -1;
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
        tvTitle.setText(String.format(getString(R.string.moneyin_price_elligible), getString(R.string.money_in)));
        DeviceAttr attr = deviceDataResponse.getDeviceAttr();
        if (attr != null) {
            mTvModelName.setText(attr.getModel());
            deviceId = attr.getImei().get(0);
        }
        orderValue = CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true);
        mTvSellingPrice.setText(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true));
        mTvValidTill.setText(String.format(getString(R.string.price_valid_until), deviceDataResponse.getExpiryTimeFmt()));
        List<String> deviceReview = deviceDataResponse.getDeviceReview();
        StringBuilder stringBuilder = new StringBuilder();
        for (String review : deviceReview) {
            stringBuilder.append("•").append(review).append("\n");
        }
        mTvDeviceReview.setText(stringBuilder.toString());
        mTvFinalAmount.setText(CurrencyFormatUtil.INSTANCE.convertPriceValueToIdrFormat(deviceDataResponse.getRemainingPrice(), true));

        if (tradeInData != null) {
            setbuttonCheckout();
        }
        hideProgressBar();
        sendGeneralEvent(MoneyInGTMConstants.ACTION_VIEW_MONEYIN,
                category,
                MoneyInGTMConstants.ACTION_VIEW_HARGA_FINAL,
                String.format("diagnostic id - %s", deviceId) );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT:
                    setResult(RESULT_OK, null);
                    finish();
                    break;
                case PINPOINT_ACTIVITY_REQUEST_CODE:
                    Parcelable parcelableExtra = data.getParcelableExtra(EXTRA_ADDRESS_NEW);
                    if (data.hasExtra(EXTRA_ADDRESS_NEW) && parcelableExtra != null) {
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
        viewModel.getAddress();
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
        int greenColor = getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
        spannableString.setSpan(clickableSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTnc.setText(spannableString);
        mTvTnc.setClickable(true);
        mTvTnc.setMovementMethod(LinkMovementMethod.getInstance());
        mTvButtonPayOrKtp.setBackgroundResource(R.drawable.bg_tradein_button_orange);
        mTvButtonPayOrKtp.setText(getString(checkoutString));
        mTvButtonPayOrKtp.setOnClickListener(v -> {
            goToCheckout();
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    category,
                    "click "+getString(checkoutString).toLowerCase()+" button",
                    "");
        });
    }

    private void showtnc() {
        showTnC(MONEYIN_TNC_URL);
        sendGeneralEvent("clickTradeIn",
                "harga final trade in",
                "click syarat dan ketentuan",
                "");    }
}
