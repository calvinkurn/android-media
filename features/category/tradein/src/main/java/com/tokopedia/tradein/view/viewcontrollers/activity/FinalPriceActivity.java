package com.tokopedia.tradein.view.viewcontrollers.activity;

import android.app.Activity;
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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic;
import com.tokopedia.common_tradein.model.TradeInParams;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tradein.TradeinConstants;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.TradeInGTMConstants;
import com.tokopedia.tradein.model.DeviceAttr;
import com.tokopedia.tradein.model.DeviceDataResponse;
import com.tokopedia.tradein.model.KYCDetails;
import com.tokopedia.tradein.viewmodel.FinalPriceViewModel;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class FinalPriceActivity extends BaseTradeInActivity<FinalPriceViewModel> implements Observer<DeviceDataResponse> {

    public static final int FINAL_PRICE_REQUEST_CODE = 22456;
    private final static int FLAG_ACTIVITY_KYC_FORM = 1301;
    private final static int PINPOINT_ACTIVITY_REQUEST_CODE = 1302;
    public static final String PARAM_PROJECTID_TRADEIN = "TRADEIN_PROJECT";
    public static final String PARAM_TRADEIN_PHONE_TYPE = "PARAM_TRADEIN_PHONE_TYPE";
    private final static String EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW";
    private static final int PROJECT_ID = 4;
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

    private ArrayList<View> viewsTradeIn;
    /**
     * buy_now
     */
    private TextView mTvButtonPayOrKtp;
    private TextView tvTitle;
    private int tradeInStringId = R.string.tukar_tambah;
    private String category = TradeInGTMConstants.CATEGORY_TRADEIN_HARGA_FINAL;
    private static final String KERO_TOKEN = "token";
    private String phoneType = "", price = "";

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
        viewsTradeIn = new ArrayList<>();
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
        View card = findViewById(R.id.carc_background_white);
        View tvModel = findViewById(R.id.tv_model);
        View tvexchange = findViewById(R.id.tv_exchange);
        View tvfinalprice = findViewById(R.id.tv_final_price);
        View tvprice = findViewById(R.id.tv_price);
        tvTitle = findViewById(R.id.tv_title);
        View divider1 = findViewById(R.id.divider1);
        View divider2 = findViewById(R.id.divider2);
        View dividerModel = findViewById(R.id.divider_model);
        View space = findViewById(R.id.space);

        viewsTradeIn.add(mTvTnc);
        viewsTradeIn.add(mTvValidTill);
        viewsTradeIn.add(mTvSellingPrice);
        viewsTradeIn.add(mTvDeviceReview);
        viewsTradeIn.add(mTvPriceNew);
        viewsTradeIn.add(mTvFinalAmount);
        viewsTradeIn.add(mTvButtonPayOrKtp);
        viewsTradeIn.add(card);
        viewsTradeIn.add(tvModel);
        viewsTradeIn.add(dividerModel);
        viewsTradeIn.add(tvfinalprice);
        viewsTradeIn.add(tvprice);
        viewsTradeIn.add(tvTitle);
        viewsTradeIn.add(divider2);
        viewsTradeIn.add(space);

        if (TRADEIN_TYPE != TRADEIN_MONEYIN) {
            viewsTradeIn.add(mTvModelNew);
            viewsTradeIn.add(mTvPriceNew);
            viewsTradeIn.add(tvexchange);
            viewsTradeIn.add(mTvPriceExchange);
            viewsTradeIn.add(divider1);
        }
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
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            checkoutString = R.string.moneyin_sell_now;
            hargeTncString = R.string.moneyin_harga_tnc;
            tncUrl = MONEYIN_TNC_URL;
            tradeInStringId = R.string.money_in;
            category = TradeInGTMConstants.CATEGORY_MONEYIN_HARGA_FINAL;
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
        tvTitle.setText(String.format(getString(R.string.moneyin_price_elligible), getString(tradeInStringId)));
        if (tradeInData != null && TRADEIN_TYPE != TRADEIN_MONEYIN) {
            price = String.valueOf(deviceDataResponse.getOldPrice());
            phoneType = getIntent().getStringExtra(PARAM_TRADEIN_PHONE_TYPE);
            mTvModelNew.setText(tradeInData.getProductName());
            mTvPriceNew.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeInData.getNewPrice(), true));
        }
        DeviceAttr attr = deviceDataResponse.getDeviceAttr();
        if (attr != null) {
            mTvModelName.setText(attr.getModel());
            deviceId = attr.getImei().get(0);
        }
        orderValue = CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true);
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
        setVisibilityGroup(View.VISIBLE);
        hideProgressBar();
        sendGeneralEvent(viewEvent,
                category,
                TradeInGTMConstants.ACTION_VIEW_HARGA_FINAL,
                TRADEIN_TYPE == TRADEIN_MONEYIN ? String.format("diagnostic id - %s", deviceId) : String.format("phone type : %s - phone price : %s - diagnostic id : %s", phoneType, price, deviceId));
    }

    private void setVisibilityGroup(int visibility) {
        for (View v : viewsTradeIn) {
            v.setVisibility(visibility);
        }
    }

    private void goToKycActivity() {
        Intent intent = RouteManager.getIntent(this, ApplinkConst.KYC_FORM, String.valueOf(PROJECT_ID));
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
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            viewModel.getAddress();
        } else {
            Intent intent = new Intent(TradeinConstants.ACTION_GO_TO_SHIPMENT);
            intent.putExtra(TradeInParams.PARAM_DEVICE_ID, deviceId);
            intent.putExtra(TradeInParams.PARAM_PHONE_TYPE, phoneType);
            intent.putExtra(TradeInParams.PARAM_PHONE_PRICE, price);
            setResult(Activity.RESULT_OK, intent);
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
            sendGeneralEvent(clickEvent,
                    category,
                    "click "+getString(checkoutString).toLowerCase()+" button",
                    "");
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
        int greenColor = getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400);
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
            sendGeneralEvent(clickEvent,
                    category,
                    "click lanjut foto ktp",
                    TRADEIN_TYPE != TRADEIN_MONEYIN ? String.format("phone type : %s - phone price : %s - diagnostic id : %s", phoneType, price, deviceId) : "");
        });
    }

    private void showtnc() {
        showTnC(tncUrl);
        sendGeneralEvent("clickTradeIn",
                "harga final trade in",
                "click syarat dan ketentuan",
                TRADEIN_TYPE != TRADEIN_MONEYIN ? String.format("phone type : %s - phone price : %s - diagnostic id : %s", phoneType, price, deviceId) : "");    }
}
