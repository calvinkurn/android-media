package view.viewcontrollers;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tokopedia.tradein.R;

import java.util.ArrayList;
import java.util.List;

import model.DeviceDataResponse;
import model.KYCDetails;
import model.TradeInParams;
import tradein_common.Constants;
import tradein_common.router.TradeInRouter;
import viewmodel.FinalPriceViewModel;

public class FinalPriceActivity extends BaseTradeInActivity<FinalPriceViewModel> implements Observer<DeviceDataResponse> {
    public static final int FINAL_PRICE_REQUEST_CODE = 22456;
    private final static int FLAG_ACTIVITY_KYC_FORM = 1301;
    private FinalPriceViewModel viewModel;
    /**
     * price_valid_until
     */
    private TextView mTvValidTill;
    private TextView mTvModelName;
    private TextView mTvPriceTotal;
    private TextView mTvDeviceReview;
    private TextView mTvModelNew;
    private TextView mTvPriceNew;
    private TextView mTvPriceExchange;
    private TextView mTvFinalAmount;
    private TextView mTvTnc;

    private ArrayList<View> viewArrayList;
    /**
     * buy_now
     */
    private TextView mTvButtonPayOrKtp;

    public static Intent getHargaFinalIntent(Context context) {
        return new Intent(context, FinalPriceActivity.class);
    }

    @Override
    void initView() {
        viewArrayList = new ArrayList<>();
        mTvValidTill = findViewById(R.id.tv_valid_till);
        viewArrayList.add(mTvValidTill);
        mTvModelName = findViewById(R.id.tv_model_name);
        viewArrayList.add(mTvModelName);
        mTvPriceTotal = findViewById(R.id.tv_price_total);
        viewArrayList.add(mTvPriceTotal);
        mTvDeviceReview = findViewById(R.id.tv_device_review);
        viewArrayList.add(mTvDeviceReview);
        mTvModelNew = findViewById(R.id.tv_model_new);
        viewArrayList.add(mTvModelNew);
        mTvPriceNew = findViewById(R.id.tv_price_new);
        viewArrayList.add(mTvPriceNew);
        mTvPriceExchange = findViewById(R.id.tv_price_exchange);
        viewArrayList.add(mTvPriceExchange);
        mTvFinalAmount = findViewById(R.id.tv_final_amount);
        viewArrayList.add(mTvFinalAmount);
        mTvButtonPayOrKtp = findViewById(R.id.tv_button_pay_or_ktp);
        viewArrayList.add(mTvButtonPayOrKtp);
        mTvTnc = findViewById(R.id.tv_tnc);
        viewArrayList.add(mTvTnc);
        viewArrayList.add(findViewById(R.id.carc_background_white));
        viewArrayList.add(findViewById(R.id.tv_model));
        viewArrayList.add(findViewById(R.id.tv_exchange));
        viewArrayList.add(findViewById(R.id.tv_final_price));
        viewArrayList.add(findViewById(R.id.tv_title));
        viewArrayList.add(findViewById(R.id.divider1));
        viewArrayList.add(findViewById(R.id.divider2));
    }

    @Override
    Class<FinalPriceViewModel> getViewModelType() {
        return FinalPriceViewModel.class;
    }

    @Override
    void setViewModel(ViewModel viewModel) {
        this.viewModel = (FinalPriceViewModel) viewModel;
        getLifecycle().addObserver(this.viewModel);
    }

    @Override
    int getMenuRes() {
        return -1;
    }

    @Override
    int getBottomSheetLayoutRes() {
        return 0;
    }

    @Override
    boolean doNeedReattach() {
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

    @Override
    protected void onStart() {
        viewModel.getDeviceDiagData().observe(this, this);
        super.onStart();
    }

    private void renderDetails(DeviceDataResponse deviceDataResponse) {
        TradeInParams tradeInData = viewModel.getTradeInParams();
        if (tradeInData != null) {
            mTvModelNew.setText(tradeInData.getProductName());
            mTvPriceNew.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(tradeInData.getNewPrice(), true));
        }
        mTvModelName.setText(deviceDataResponse.getDeviceAttr().getModel());
        mTvPriceExchange.setText(String.valueOf(deviceDataResponse.getOldPrice()));
        mTvPriceTotal.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getRemainingPrice(), true));
        mTvValidTill.setText(String.format(getString(R.string.price_valid_until), deviceDataResponse.getExpiryTimeFmt()));
        List<String> deviceReview = deviceDataResponse.getDeviceReview();
        StringBuilder stringBuilder = new StringBuilder();
        for (String review : deviceReview) {
            stringBuilder.append("•").append(review).append("\n");
        }
        mTvDeviceReview.setText(stringBuilder.toString());
        mTvPriceExchange.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getOldPrice(), true));
        mTvFinalAmount.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(deviceDataResponse.getRemainingPrice(), true));

        if (tradeInData != null) {
            KYCDetails kycDetails = deviceDataResponse.getKycDetails();
            if (kycDetails == null) {
                setbuttonCheckout();
            } else {
                if (kycDetails.getDetail().getIsSuccess() != 1) {
                    setButtonKyc();
                } else {
                    setbuttonCheckout();
                }
            }
        }
        setVisibilityGroup(View.VISIBLE);
        hideProgressBar();
    }

    private void setVisibilityGroup(int visibility) {
        for (View v : viewArrayList) {
            v.setVisibility(visibility);
        }
    }

    private void goToKycActivity() {
        TradeInRouter router = (TradeInRouter) getApplication();
        Intent intent = router.getKYCIntent(this, 4);
        startActivityForResult(intent, FLAG_ACTIVITY_KYC_FORM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_ACTIVITY_KYC_FORM) {
            if (resultCode == Activity.RESULT_OK) {
                setbuttonCheckout();
                goToCheckout();
            }
        }
    }

    private void goToCheckout() {
        String deviceid = getDeviceId();
        setResult(Activity.RESULT_OK, new Intent(Constants.ACTION_GO_TO_SHIPMENT).putExtra(TradeInParams.PARAM_DEVICE_ID, deviceid));
        finish();
    }

    private void setbuttonCheckout() {
        String notElligible = getString(R.string.harga_tnc);
        SpannableString spannableString = new SpannableString(notElligible);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showTnC(R.string.tradein_tnc);
                setVisibilityGroup(View.INVISIBLE);
            }
        };
        int greenColor = getResources().getColor(R.color.green_nob);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
        spannableString.setSpan(clickableSpan, 40, 58, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(foregroundColorSpan, 40, 58, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvTnc.setText(spannableString);
        mTvTnc.setClickable(true);
        mTvTnc.setMovementMethod(LinkMovementMethod.getInstance());
        mTvButtonPayOrKtp.setBackgroundResource(R.drawable.bg_tradein_button_orange);
        mTvButtonPayOrKtp.setText(R.string.buy_now);
        mTvButtonPayOrKtp.setOnClickListener(v -> goToCheckout());
    }

    private void setButtonKyc() {
        String notElligible = getString(R.string.harga_tnc_kyc);
        SpannableString spannableString = new SpannableString(notElligible);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showTnC(R.string.tradein_tnc);
                setVisibilityGroup(View.INVISIBLE);
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
        mTvButtonPayOrKtp.setOnClickListener(v -> goToKycActivity());
    }

    @Override
    public void onBackPressed() {
        if (isTncShowing) {
            setVisibilityGroup(View.VISIBLE);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isTncShowing) {
                setVisibilityGroup(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
