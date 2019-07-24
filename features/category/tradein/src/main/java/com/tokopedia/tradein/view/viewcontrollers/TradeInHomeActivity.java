package com.tokopedia.tradein.view.viewcontrollers;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class TradeInHomeActivity extends BaseTradeInActivity {

    private TextView mTvPriceElligible;
    private ImageView mButtonRemove;
    private TextView mTvModelName;
    private TextView mTvHeaderPrice;
    private TextView mTvInitialPrice;
    private TextView mTvGoToProductDetails;
    private TextView mTvNotUpto;
    private TextView tvIndicateive;
    private TradeInHomeViewModel tradeInHomeViewModel;
    private boolean isAlreadySet = false;
    public static final int TRADEIN_HOME_REQUEST = 22345;
    private static final int APP_SETTINGS = 9988;

    public static Intent getIntent(Context context) {
        return new Intent(context, TradeInHomeActivity.class);
    }


    @Override
    protected void initView() {
        mTvPriceElligible = findViewById(R.id.tv_price_elligible);
        mButtonRemove = findViewById(R.id.button_remove);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvHeaderPrice = findViewById(R.id.tv_header_price);
        mTvInitialPrice = findViewById(R.id.tv_initial_price);
        mTvNotUpto = findViewById(R.id.tv_not_upto);
        mTvGoToProductDetails = findViewById(R.id.tv_go_to_product_details);
        tvIndicateive = findViewById(R.id.tv_indicative);
        mTvModelName.setText(new StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString());
    }

    @Override
    public Class<TradeInHomeViewModel> getViewModelType() {
        return TradeInHomeViewModel.class;
    }

    @Override
    protected void setViewModel(BaseViewModel viewModel) {
        tradeInHomeViewModel = (TradeInHomeViewModel) viewModel;
        getLifecycle().addObserver(tradeInHomeViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tradeInHomeViewModel.getMinPriceData().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                if (!isAlreadySet) {
                    try {
                        hideProgressBar();
                        mTvGoToProductDetails.setText(getString(R.string.text_check_functionality));
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            tradeInHomeViewModel.startGUITest();
                            sendGeneralEvent("clickTradeIn",
                                    "trade in start page",
                                    "click mulai cek fungsi",
                                    "");

                        });
                        int maxPrice = jsonObject.getInt("max_price");
                        int minPrice = jsonObject.getInt("min_price");
                        TradeInParams tradeInParams = tradeInHomeViewModel.getTradeInParams();
                        int diagnosedPrice = tradeInParams.getUsedPrice();
                        if (tradeInParams != null && diagnosedPrice > 0)
                            minPrice = diagnosedPrice;
                        if (!errorPriceNotElligible(minPrice)) {
                            mTvNotUpto.setVisibility(View.VISIBLE);
                            if (diagnosedPrice <= 0)
                                mTvInitialPrice.setText(String.format("%1$s - %2$s",
                                        CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true)));
                            else
                                mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnosedPrice, true));
                        } else {
                            mTvNotUpto.setVisibility(View.GONE);
                            mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true));
                        }
                        isAlreadySet = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tradeInHomeViewModel.getPriceFailData().observe(this, jsonObject -> {
            hideProgressBar();
            try {
                mTvInitialPrice.setText(jsonObject.getString("message"));
                mTvPriceElligible.setText(getString(R.string.not_elligible));
                mTvPriceElligible.setVisibility(View.VISIBLE);
                tvIndicateive.setVisibility(View.GONE);
                mTvGoToProductDetails.setText(R.string.go_to_product_details);
                mTvGoToProductDetails.setOnClickListener(v -> {
                    sendGeneralEvent("clickTradeIn",
                            "trade in start page",
                            "click kembali ke detail produk",
                            "");

                    finish();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });

        tradeInHomeViewModel.getInsertResult().observe(this, price -> {
            try {
                if (price != null && price > 0) {
                    errorPriceNotElligible(price);
                    mTvNotUpto.setVisibility(View.GONE);
                    mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(price, true));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getMenuRes() {
        return R.menu.trade_in_home;
    }

    @Override
    protected Fragment getTncFragmentInstance(int TncResId) {
        return TnCFragment.getInstance(TncResId);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_activity_tradeinhome;
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == TradeInHomeViewModel.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    int result = grantResults[i];
                    if (result == PackageManager.PERMISSION_DENIED) {
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            sendGeneralEvent("clickTradeIn",
                                    "trade in start page",
                                    "click kembali ke detail produk",
                                    "");

                            finish();
                        });
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            showMessageWithAction("Izinkan akses di Pengaturan Aplikasi-> Izin -> " +
                                            "Camera, Telephone,Storage",
                                    "Go To Settings", (v) -> {
                                        final Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setData(Uri.parse("package:" + this.getPackageName()));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        this.startActivityForResult(intent, APP_SETTINGS);
                                    });
                            return;
                        } else {
                            showMessageWithAction("Perkenankan izin yang hilang untuk memulai memeriksa fungsi",
                                    getString(R.string.title_ok), (v) -> tradeInHomeViewModel.requestPermission());
                        }
                        return;
                    }
                }
                showProgressBar();
                tradeInHomeViewModel.getMaxPrice();
            } else {
                tradeInHomeViewModel.requestPermission();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FinalPriceActivity.FINAL_PRICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        } else if (requestCode == APP_SETTINGS) {
            tradeInHomeViewModel.requestPermission();
        }
    }

    private boolean errorPriceNotElligible(int oldMinPrice) {
        if (oldMinPrice > tradeInHomeViewModel.getTradeInParams().getNewPrice()) {
            String notElligible = getString(R.string.not_elligible_price_high);
            SpannableString spannableString = new SpannableString(notElligible);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showTnC(R.string.tradein_tnc);
                }
            };
            tvIndicateive.setVisibility(View.GONE);
            mTvGoToProductDetails.setText(R.string.go_to_product_details);
            mTvGoToProductDetails.setOnClickListener(v -> {
                sendGeneralEvent("clickTradeIn",
                        "trade in start page",
                        "click kembali ke detail produk",
                        "");
                finish();
            });
            int greenColor = getResources().getColor(R.color.green_nob);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
            spannableString.setSpan(foregroundColorSpan, 67, 84, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(clickableSpan, 67, 84, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mTvPriceElligible.setText(spannableString);
            mTvPriceElligible.setVisibility(View.VISIBLE);
            mButtonRemove.setVisibility(View.VISIBLE);
            mButtonRemove.setOnClickListener(view -> {
                mTvPriceElligible.setVisibility(View.GONE);
                mButtonRemove.setVisibility(View.GONE);
            });
            mTvPriceElligible.setClickable(true);
            mTvPriceElligible.setMovementMethod(LinkMovementMethod.getInstance());
            return true;
        }
        return false;
    }
}
