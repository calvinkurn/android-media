package com.tokopedia.tradein.view.viewcontrollers;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laku6.tradeinsdk.api.Laku6TradeIn;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.model.TradeInParams;
import com.tokopedia.tradein.viewmodel.HomeResult;
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel;
import com.tokopedia.tradein_common.Constants;
import com.tokopedia.tradein_common.IAccessRequestListener;
import com.tokopedia.tradein_common.viewmodel.BaseViewModel;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class TradeInHomeActivity extends BaseTradeInActivity implements IAccessRequestListener {


    private TextView mTvPriceElligible;
    private ImageView mButtonRemove;
    private TextView mTvModelName;
    private TextView mTvHeaderPrice;
    private TextView mTvInitialPrice;
    private TextView mTvGoToProductDetails;
    private TextView mTvNotUpto;
    private TextView tvIndicateive;
    private TradeInHomeViewModel tradeInHomeViewModel;
    private int closeButtonText;
    private int tncStringId;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tradeInHomeViewModel.processMessage(intent);
        }
    };

    private BroadcastReceiver mBackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent. DO BACK TO PARENT
            Timber.d("Do back action to parent");
        }
    };

    private BroadcastReceiver laku6GTMReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "laku6-gtm".equals(intent.getAction())) {
                String page = intent.getStringExtra("page");
                String action = intent.getStringExtra("action");
                String value = intent.getStringExtra("value");
                if ("cek fisik".equals(page)) {
                    if ("click salin".equals(action) || "click social share".equals(action))
                        sendGeneralEvent("clickTradeIn", "cek fisik trade in", action, value);
                } else if ("cek fungsi trade in".equals(page)) {
                    sendGeneralEvent("clickTradeIn", "cek fungsi trade in", action, value);
                } else if ("cek fisik result trade in".equals(page)) {
                    sendGeneralEvent("viewTradeIn", "cek fisik result trade in", action, value);
                }
            }
        }
    };
    private Laku6TradeIn laku6TradeIn;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TRADEIN_TYPE == 2) {
            closeButtonText = R.string.tradein_return;
            tncStringId = R.string.money_in_tnc;
        } else {
            closeButtonText = R.string.go_to_product_details;
            tncStringId = R.string.tradein_tnc;
        }
        tradeInHomeViewModel.getHomeResultData().observe(this, (homeResult -> {
            if (!homeResult.isSuccess()) {
                mTvInitialPrice.setText(homeResult.getDisplayMessage());
                mTvPriceElligible.setText(getString(R.string.not_elligible));
                mTvPriceElligible.setVisibility(View.VISIBLE);
                tvIndicateive.setVisibility(View.GONE);
                mTvGoToProductDetails.setText(closeButtonText);
                mTvGoToProductDetails.setOnClickListener(v -> {
                    sendGeneralEvent("clickTradeIn",
                            "trade in start page",
                            "click kembali ke detail produk",
                            "");

                    finish();
                });
            } else {
                HomeResult.PriceState state = homeResult.getPriceStatus();
                switch (state) {
                    case DIAGNOSED_INVALID:
                        SpannableString spannableString = new SpannableString(getString(R.string.not_elligible_price_high));
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NotNull View widget) {
                                showTnC(tncStringId);
                            }
                        };
                        tvIndicateive.setVisibility(View.GONE);
                        mTvGoToProductDetails.setText(closeButtonText);
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
                        mTvNotUpto.setVisibility(View.GONE);
                        mTvModelName.setText(homeResult.getDeviceDisplayName());
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        break;
                    case DIAGNOSED_VALID:
                        tvIndicateive.setVisibility(View.GONE);
                        mTvModelName.setText(homeResult.getDeviceDisplayName());
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        mTvGoToProductDetails.setText(R.string.sell_now);
                        mTvGoToProductDetails.setOnClickListener(v -> goToHargaFinal());
                        break;
                    case NOT_DIAGNOSED:
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        mTvGoToProductDetails.setText(getString(R.string.text_check_functionality));
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            laku6TradeIn.startGUITest();
                            sendGeneralEvent("clickTradeIn",
                                    "trade in start page",
                                    "click mulai cek fungsi",
                                    "");

                        });
                        break;
                    case MONEYIN_ERROR:
                        showDialogFragment(0, "Money In Error", homeResult.getDisplayMessage(),
                                getString(R.string.tradein_return), null);
                    default:
                        break;
                }
            }
        }));
        getPriceFromSDK(this);
    }

    private void goToHargaFinal() {
        Intent finalPriceIntent = new Intent(this, FinalPriceActivity.class);
        TradeInParams params = tradeInHomeViewModel.getTradeInParams();
        finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(), params);
        finalPriceIntent.putExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, TRADEIN_TYPE);
        navigateToActivityRequest(finalPriceIntent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE);
    }

    private void getPriceFromSDK(Context context) {
        String campaignId = Constants.CAMPAIGN_ID_PROD;
        if (Constants.LAKU6_BASEURL.equals(Constants.LAKU6_BASEURL_STAGING))
            campaignId = Constants.CAMPAIGN_ID_STAGING;
        laku6TradeIn = Laku6TradeIn.getInstance(context, campaignId,
                Constants.APPID, Constants.APIKEY, Constants.LAKU6_BASEURL);
        requestPermission();
    }

    private void requestPermission() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            tradeInHomeViewModel.getMaxPrice(laku6TradeIn, TRADEIN_TYPE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tradeInHomeViewModel.getMinPriceData().observe(this, jsonObject -> {
//            if (!isAlreadySet) {
//                try {
//                    hideProgressBar();
//                    mTvGoToProductDetails.setText(getString(R.string.text_check_functionality));
//                    mTvGoToProductDetails.setOnClickListener(v -> {
//                        laku6TradeIn.startGUITest();
//                        sendGeneralEvent("clickTradeIn",
//                                "trade in start page",
//                                "click mulai cek fungsi",
//                                "");
//
//                    });
//                    int maxPrice = jsonObject.getInt("max_price");
//                    int minPrice = jsonObject.getInt("min_price");
//                    TradeInParams tradeInParams = tradeInHomeViewModel.getTradeInParams();
//                    int diagnosedPrice = tradeInParams.getUsedPrice();
//                    if (diagnosedPrice > 0)
//                        minPrice = diagnosedPrice;
//                    if (!errorPriceNotElligible(minPrice)) {
//                        mTvNotUpto.setVisibility(View.VISIBLE);
//                        if (diagnosedPrice <= 0)
//                            mTvInitialPrice.setText(String.format("%1$s - %2$s",
//                                    CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true),
//                                    CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true)));
//                        else
//                            mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(diagnosedPrice, true));
//                    } else {
//                        mTvNotUpto.setVisibility(View.GONE);
//                        mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true));
//                    }
//                    isAlreadySet = true;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        tradeInHomeViewModel.getPriceFailData().observe(this, jsonObject -> {
//            hideProgressBar();
//            try {
//                mTvInitialPrice.setText(jsonObject.getString("message"));
//                mTvPriceElligible.setText(getString(R.string.not_elligible));
//                mTvPriceElligible.setVisibility(View.VISIBLE);
//                tvIndicateive.setVisibility(View.GONE);
//                mTvGoToProductDetails.setText(R.string.go_to_product_details);
//                mTvGoToProductDetails.setOnClickListener(v -> {
//                    sendGeneralEvent("clickTradeIn",
//                            "trade in start page",
//                            "click kembali ke detail produk",
//                            "");
//
//                    finish();
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        });
//        tradeInHomeViewModel.getInsertResult().observe(this, price -> {
//            try {
//                if (price != null && price > 0) {
//                    errorPriceNotElligible(price);
//                    mTvNotUpto.setVisibility(View.GONE);
//                    mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(price, true));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        tradeInHomeViewModel.getFinalPriceData().observe(this, tradeInParams -> {
//            Intent finalPriceIntent = new Intent(this, FinalPriceActivity.class);
//            finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(), tradeInParams);
//            navigateToActivityRequest(finalPriceIntent, FinalPriceActivity.FINAL_PRICE_REQUEST_CODE);
//            tradeInHomeViewModel.getFinalPriceData().removeObservers(this);
//        });
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mMessageReceiver, new IntentFilter("laku6-test-end"));
        localBroadcastManager.registerReceiver(mBackReceiver, new IntentFilter("laku6-back-action"));
        localBroadcastManager.registerReceiver(laku6GTMReceiver, new IntentFilter("laku6-gtm"));


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.unregisterReceiver(mMessageReceiver);
            localBroadcastManager.unregisterReceiver(mBackReceiver);
            localBroadcastManager.unregisterReceiver(laku6GTMReceiver);
        }
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
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    int result = grantResults[i];
                    if (result == PackageManager.PERMISSION_DENIED) {
                        mTvGoToProductDetails.setText(closeButtonText);
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            sendGeneralEvent("clickTradeIn",
                                    "trade in start page",
                                    "click kembali ke detail produk",
                                    "");

                            finish();
                        });
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            showMessageWithAction(getString(R.string.tradein_permission_setting),
                                    getString(R.string.title_ok), (v) -> {
                                        final Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setData(Uri.parse("package:" + this.getPackageName()));
                                        this.startActivityForResult(intent, APP_SETTINGS);
                                    });
                        } else {
                            showMessageWithAction("Perkenankan izin yang hilang untuk memulai memeriksa fungsi",
                                    getString(R.string.title_ok), (v) -> requestPermission());
                        }
                        return;
                    }
                }
                showProgressBar();
                tradeInHomeViewModel.getMaxPrice(laku6TradeIn, TRADEIN_TYPE);
            } else {
                requestPermission();
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
            requestPermission();
        }
    }

    private boolean errorPriceNotElligible(int oldMinPrice) {
        if (oldMinPrice > tradeInHomeViewModel.getTradeInParams().getNewPrice()) {
            String notElligible = getString(R.string.not_elligible_price_high);
            SpannableString spannableString = new SpannableString(notElligible);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    showTnC(tncStringId);
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

    @Override
    public void clickAccept() {
        finish();
    }

    @Override
    public void clickDeny() {

    }
}
