package com.tokopedia.tradein.view.viewcontrollers.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.tokopedia.unifyprinciples.Typography;

import com.laku6.tradeinsdk.api.Laku6TradeIn;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalCategory;
import com.tokopedia.authentication.AuthKey;
import com.tokopedia.common_tradein.utils.TradeInUtils;
import com.tokopedia.design.dialog.AccessRequestDialogFragment;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.keys.Keys;
import com.tokopedia.tradein.R;
import com.tokopedia.tradein.TradeInGTMConstants;
import com.tokopedia.common_tradein.model.TradeInParams;
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInImeiHelpBottomSheet;
import com.tokopedia.tradein.viewmodel.HomeResult;
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModel;
import com.tokopedia.tradein.TradeinConstants;
import com.tokopedia.design.dialog.IAccessRequestListener;
import com.tokopedia.basemvvm.viewmodel.BaseViewModel;
import com.tokopedia.url.Env;
import com.tokopedia.url.TokopediaUrl;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import timber.log.Timber;

import static com.tokopedia.tradein.view.viewcontrollers.activity.FinalPriceActivity.PARAM_TRADEIN_PHONE_TYPE;

public class MoneyInHomeActivity extends BaseTradeInActivity<TradeInHomeViewModel> implements IAccessRequestListener, Laku6TradeIn.TradeInListener {


    private Typography mTvPriceElligible;
    private ImageView mButtonRemove;
    private Typography mTvModelName;
    private Typography mTvHeaderPrice;
    private Typography mTvInitialPrice;
    private Typography mTvGoToProductDetails;
    private Typography mTvNotUpto;
    private Typography tvIndicateive;
    private TradeInHomeViewModel tradeInHomeViewModel;
    private int closeButtonText;
    private int notElligibleText;
    private boolean isShowingPermissionPopup;
    private String category = TradeInGTMConstants.CATEGORY_TRADEIN_START_PAGE;
    private String errorDialogGTMLabel = "";
    private String productName = "";
    private LinearLayout imeiView;
    private EditText editTextImei;
    private Typography typographyImeiDescription;
    private Typography typographyImeiHelp;
    private boolean inputImei;

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
            if (intent != null && TradeInGTMConstants.ACTION_LAKU6_GTM.equals(intent.getAction())) {
                String page = intent.getStringExtra(TradeInGTMConstants.PAGE);
                String action = intent.getStringExtra(TradeInGTMConstants.ACTION);
                String value = intent.getStringExtra(TradeInGTMConstants.VALUE);
                String cekFisik = TradeInGTMConstants.CEK_FISIK_TRADE_IN;
                String cekFungsi = TradeInGTMConstants.CEK_FUNGSI_TRADE_IN;
                String cekFisikResult = TradeInGTMConstants.CEK_FISIK_RESULT_TRADE_IN;
                if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
                    cekFisik = TradeInGTMConstants.CEK_FISIK_MONEY_IN;
                    cekFungsi = TradeInGTMConstants.CEK_FUNGSI_MONEY_IN;
                    cekFisikResult = TradeInGTMConstants.CEK_FISIK_RESULT_MONEY_IN;
                }
                if (TradeInGTMConstants.CEK_FISIK.equals(page)) {
                    if (TradeInGTMConstants.CLICK_SALIN.equals(action)
                            || TradeInGTMConstants.CLICK_SOCIAL_SHARE.equals(action))
                        sendGeneralEvent(clickEvent,
                                cekFisik, action, value);
                } else if (TradeInGTMConstants.CEK_FUNGSI_TRADE_IN.equals(page)) {
                    sendGeneralEvent(clickEvent,
                            cekFungsi, action, value);
                } else if (TradeInGTMConstants.CEK_FISIK_RESULT_TRADE_IN.equals(page)) {
                    sendGeneralEvent(viewEvent,
                            cekFisikResult, action, value);
                }
            }
        }
    };
    private Laku6TradeIn laku6TradeIn;

    public static Intent getIntent(Context context) {
        return new Intent(context, MoneyInHomeActivity.class);
    }

    @Override
    public void initInject() {
        getComponent().inject(this);
    }

    @Override
    public void initView() {
        setTradeInParams();
        mTvPriceElligible = findViewById(R.id.tv_price_elligible);
        mButtonRemove = findViewById(R.id.button_remove);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvHeaderPrice = findViewById(R.id.tv_header_price);
        mTvInitialPrice = findViewById(R.id.tv_initial_price);
        mTvNotUpto = findViewById(R.id.tv_not_upto);
        mTvGoToProductDetails = findViewById(R.id.tv_go_to_product_details);
        tvIndicateive = findViewById(R.id.tv_indicative);
        imeiView = findViewById(R.id.imei_view);
        editTextImei = findViewById(R.id.edit_text_imei);
        typographyImeiDescription = findViewById(R.id.typography_imei_description);
        typographyImeiHelp = findViewById(R.id.typography_imei_help);
        mTvModelName.setText(new StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString());
        if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
            closeButtonText = R.string.tradein_return;
            notElligibleText = R.string.not_elligible_money_in;
            tncUrl = MONEYIN_TNC_URL;
            category = TradeInGTMConstants.CATEGORY_MONEYIN_PRICERANGE_PAGE;
        } else {
            closeButtonText = R.string.go_to_product_details;
            notElligibleText = R.string.not_elligible;
            tncUrl = TRADEIN_TNC_URL;
        }
        mTvGoToProductDetails.setText(closeButtonText);
        mTvGoToProductDetails.setOnClickListener(v -> {
            finish();
        });
        typographyImeiHelp.setOnClickListener(v -> {
            TradeInImeiHelpBottomSheet tradeInImeiHelpBottomSheet = TradeInImeiHelpBottomSheet.Companion.newInstance();
            tradeInImeiHelpBottomSheet.show(getSupportFragmentManager(), "");
        });
    }

    private void setTradeInParams() {
        if (getIntent().hasExtra(TradeInParams.class.getSimpleName())) {
            tradeInHomeViewModel.setTradeInParams(getIntent().getParcelableExtra(TradeInParams.class.getSimpleName()));
        }
    }

    @Override
    public Class<TradeInHomeViewModel> getViewModelType() {
        return TradeInHomeViewModel.class;
    }

    @Override
    public void setViewModel(BaseViewModel viewModel) {
        tradeInHomeViewModel = (TradeInHomeViewModel) viewModel;
        getLifecycle().addObserver(tradeInHomeViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradeInHomeViewModel.getHomeResultData().observe(this, (homeResult -> {
            if (!homeResult.isSuccess()) {
                mTvInitialPrice.setText(homeResult.getDisplayMessage());
                mTvPriceElligible.setText(getString(notElligibleText));
                mTvPriceElligible.setVisibility(View.VISIBLE);
                tvIndicateive.setVisibility(View.GONE);
                mTvGoToProductDetails.setText(closeButtonText);
                mTvGoToProductDetails.setOnClickListener(v -> {
                    sendGoToProductDetailGTM();
                    finish();
                });
            } else {
                HomeResult.PriceState state = homeResult.getPriceStatus();
                productName = tradeInHomeViewModel.getTradeInParams().getProductName() != null
                        ? tradeInHomeViewModel.getTradeInParams().getProductName().toLowerCase()
                        : "";
                switch (state) {
                    case DIAGNOSED_INVALID:
                        tvIndicateive.setVisibility(View.GONE);
                        mTvGoToProductDetails.setText(closeButtonText);
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            sendGoToProductDetailGTM();
                            if (TRADEIN_TYPE != TRADEIN_MONEYIN) {
                                sendGeneralEvent(clickEvent,
                                        category,
                                        TradeInGTMConstants.ACTION_KEMBALI_KE_DETAIL_PRODUK,
                                        getString(R.string.trade_in_event_label_phone_type_min_price_max_price,
                                                productName, homeResult.minPrice.toString(), homeResult.maxPrice.toString()));
                            }
                            finish();
                        });

                        showDeviceNotElligiblePopup(R.string.tradein_not_elligible_price_high);
                        mTvNotUpto.setVisibility(View.GONE);
                        mTvModelName.setText(homeResult.getDeviceDisplayName());
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        viewMoneyInPriceGTM(homeResult.getDisplayMessage());
                        break;
                    case DIAGNOSED_VALID:
                        tvIndicateive.setVisibility(View.GONE);
                        mTvModelName.setText(homeResult.getDeviceDisplayName());
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        mTvGoToProductDetails.setText(R.string.moneyin_sell_now);
                        if (TRADEIN_TYPE != TRADEIN_MONEYIN) {
                            sendIrisEvent(homeResult.maxPrice != null ? homeResult.maxPrice : 0 , homeResult.minPrice!= null ? homeResult.minPrice : 0 );
                        }
                        mTvGoToProductDetails.setOnClickListener(v -> goToHargaFinal(homeResult.getDeviceDisplayName()));
                        goToHargaFinal(homeResult.getDeviceDisplayName());
                        break;
                    case NOT_DIAGNOSED:
                        mTvInitialPrice.setText(homeResult.getDisplayMessage());
                        mTvGoToProductDetails.setText(getString(R.string.text_check_functionality));
                        mTvGoToProductDetails.setOnClickListener(v -> {
                            if (imeiView.getVisibility() == View.VISIBLE) {
                                if (editTextImei.getText().length() == 15) {
                                    showProgressBar();
                                    laku6TradeIn.checkImeiValidation(this, editTextImei.getText().toString());
                                } else if (editTextImei.getText().length() == 0){
                                    typographyImeiDescription.setText(getString(R.string.enter_the_imei_number_text));
                                    typographyImeiDescription.setTextColor(MethodChecker.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_R600));
                                } else {
                                    typographyImeiDescription.setText(getString(R.string.wrong_imei_string));
                                    typographyImeiDescription.setTextColor(MethodChecker.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_R600));
                                }
                            } else {
                                laku6TradeIn.startGUITest();
                            }
                            sendGeneralEvent(clickEvent,
                                    category,
                                    TradeInGTMConstants.ACTION_CLICK_MULAI_FUNGSI,
                                    TRADEIN_TYPE != TRADEIN_MONEYIN ? getString(R.string.trade_in_event_label_phone_type_min_price_max_price,
                                            productName, homeResult.minPrice.toString(), homeResult.maxPrice.toString()) : "");

                        });
                        if (TRADEIN_TYPE != TRADEIN_MONEYIN) {
                            sendIrisEvent(homeResult.maxPrice != null ? homeResult.maxPrice : 0 , homeResult.minPrice!= null ? homeResult.minPrice : 0 );
                        }
                        if(inputImei){
                            laku6TradeIn.startGUITest();
                            break;
                        }
                        viewMoneyInPriceGTM(homeResult.getDeviceDisplayName() + " - " + homeResult.getDisplayMessage());
                        break;
                    case MONEYIN_ERROR:
                        showDialogFragment(getString(R.string.money_in), homeResult.getDisplayMessage(),
                                getString(R.string.tradein_return), null);
                        errorDialogGTMLabel = homeResult.getDisplayMessage();
                    default:
                        break;
                }
            }
        }));
        tradeInHomeViewModel.getAskUserLogin().observe(this, (userLoginStatus -> {
            if (userLoginStatus != null && userLoginStatus == TradeinConstants.LOGIN_REQUIRED) {
                navigateToActivityRequest(RouteManager.getIntent(this, ApplinkConst.LOGIN), LOGIN_REQUEST);
            } else {
                showPermissionDialog();
            }
        }));
        tradeInHomeViewModel.getImeiStateLiveData().observe(this, showImei -> {
            if(showImei){
                imeiView.setVisibility(View.VISIBLE);
            } else {
                imeiView.setVisibility(View.GONE);
            }
        });
    }

    protected void showDialogFragment(String titleText, String bodyText, String positiveButton, String negativeButton) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AccessRequestDialogFragment accessDialog = AccessRequestDialogFragment.Companion.newInstance();
        accessDialog.setBodyText(bodyText);
        accessDialog.setTitle(titleText);
        accessDialog.setPositiveButton(positiveButton);
        accessDialog.setNegativeButton(negativeButton);
        accessDialog.show(fragmentManager, AccessRequestDialogFragment.TAG);
    }

    private void sendGoToProductDetailGTM() {
        sendGeneralEvent(clickEvent,
                category,
                TradeInGTMConstants.ACTION_KEMBALI_KE_DETAIL_PRODUK,
                "");
    }

    private void viewMoneyInPriceGTM(String label) {
        sendGeneralEvent(viewEvent,
                category,
                TradeInGTMConstants.VIEW_PRICE_RANGE_PAGE,
                label);
    }

    private void goToHargaFinal(String deviceDisplayName) {
        Intent finalPriceIntent = new Intent(this, FinalPriceActivity.class);
        TradeInParams params = tradeInHomeViewModel.getTradeInParams();
        finalPriceIntent.putExtra(TradeInParams.class.getSimpleName(), params);
        if(deviceDisplayName!=null)
            finalPriceIntent.putExtra(PARAM_TRADEIN_PHONE_TYPE, deviceDisplayName.toLowerCase());
        finalPriceIntent.putExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, TRADEIN_TYPE);
        navigateToActivityRequest(finalPriceIntent, ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE);
    }

    private void getPriceFromSDK(Context context) {
        String campaignId = TradeinConstants.CAMPAIGN_ID_PROD;
        if (TokopediaUrl.getInstance().getTYPE() == Env.STAGING)
            campaignId = TradeinConstants.CAMPAIGN_ID_STAGING;
        laku6TradeIn = Laku6TradeIn.getInstance(context, campaignId,
                TradeinConstants.APPID, Keys.AUTH_TRADE_IN_API_KEY_MA, TokopediaUrl.getInstance().getTYPE() == Env.STAGING, TRADEIN_TEST_TYPE, AuthKey.SAFETYNET_KEY_TRADE_IN);
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
    protected int getLayoutRes() {
        return R.layout.money_in_home_activity;
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
                            sendGoToProductDetailGTM();
                            finish();
                        });
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            showMessageWithAction(getString(R.string.tradein_permission_setting),
                                    getString(com.tokopedia.abstraction.R.string.title_ok), (v) -> {
                                        final Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setData(Uri.parse("package:" + this.getPackageName()));
                                        this.startActivityForResult(intent, APP_SETTINGS);
                                    });
                        } else {
                            showMessageWithAction(getString(R.string.tradein_requires_permission_for_diagnostic),
                                    getString(com.tokopedia.abstraction.R.string.title_ok), (v) -> requestPermission());
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

    private void showPermissionDialog() {
        isShowingPermissionPopup = true;
        if (getIntent().getBooleanExtra(TradeInParams.PARAM_PERMISSION_GIVEN, false)) {
            clickAccept();
        } else {
            showDialogFragment(getString(R.string.tradein_text_request_access),
                    getString(R.string.tradein_text_permission_description), "", "");
        }
    }

    private void showDeviceNotElligiblePopup(int messageStringId) {
        int greenColor = getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                showTnC(tncUrl);
            }
        };
        SpannableString spannableString = new SpannableString(getString(messageStringId));
        if(messageStringId == R.string.money_in_need_permission) {
            spannableString.setSpan(foregroundColorSpan, 62, 83, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(clickableSpan, 62, 83, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        mTvPriceElligible.setText(spannableString);
        mTvPriceElligible.setVisibility(View.VISIBLE);
        mButtonRemove.setVisibility(View.VISIBLE);
        mButtonRemove.setOnClickListener(view -> {
            mTvPriceElligible.setVisibility(View.GONE);
            mButtonRemove.setVisibility(View.GONE);
        });
        mTvPriceElligible.setClickable(true);
        mTvPriceElligible.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        } else if (requestCode == APP_SETTINGS) {
            requestPermission();
        } else if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK)
                tradeInHomeViewModel.checkLogin();
            else
                finish();
        }
    }

    @Override
    public void clickAccept() {
        if (isShowingPermissionPopup) {
            isShowingPermissionPopup = false;
            getPriceFromSDK(this);
            sendGeneralEvent(clickEvent,
                    category,
                    TradeInGTMConstants.ACTION_CLICK_SETUJU_BUTTON,
                    TradeInGTMConstants.BERI_IZIN_PENG_HP);
        } else {
            sendGeneralEvent(clickEvent,
                    category,
                    TradeInGTMConstants.ACTION_CLICK_KEMBALI_BUTTON,
                    errorDialogGTMLabel);
            finish();
        }
    }

    @Override
    public void clickDeny() {
        if (isShowingPermissionPopup) {
            isShowingPermissionPopup = false;
            showDeviceNotElligiblePopup(R.string.money_in_need_permission);
            mTvInitialPrice.setText("");
            mTvGoToProductDetails.setText(R.string.money_in_request_permission);
            mTvGoToProductDetails.setOnClickListener(v -> {
                showPermissionDialog();
            });
            if (TRADEIN_TYPE == TRADEIN_MONEYIN) {
                sendGeneralEvent(clickEvent,
                        category,
                        TradeInGTMConstants.ACTION_CLICK_BATAL_BUTTON,
                        TradeInGTMConstants.BERI_IZIN_PENG_HP);
            }
        } else {
        }
    }

    private void sendIrisEvent(Integer maxPrice, Integer minPrice){
        HashMap<String, Object> values = new HashMap<>();
        values.put(TradeInGTMConstants.EVENT, TradeInGTMConstants.ACTION_VIEW_TRADEIN_IRIS);
        values.put(TradeInGTMConstants.EVENT_CATEGORY, TradeInGTMConstants.CATEGORY_TRADEIN_START_PAGE);
        values.put(TradeInGTMConstants.EVENT_ACTION, TradeInGTMConstants.VIEW_PRICE_RANGE_PAGE);
        values.put(TradeInGTMConstants.EVENT_LABEL,  getString(R.string.trade_in_event_label_phone_type_min_price_max_price,
                productName, minPrice.toString(), maxPrice.toString()));
        values.put(TradeInGTMConstants.PRODUCT_ID,  tradeInHomeViewModel.getTradeInParams().getProductId());

        IrisAnalytics.getInstance(this).sendEvent(values);
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        hideProgressBar();
        tradeInHomeViewModel.setDeviceId(editTextImei.getText().toString());
        inputImei = true;
        TradeInUtils.setImeiNumber(this, editTextImei.getText().toString());
        getPriceFromSDK(this);
        typographyImeiDescription.setText(getString(R.string.enter_the_imei_number_text));
        typographyImeiDescription.setTextColor(MethodChecker.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_N700_68));
    }

    @Override
    public void onError(JSONObject error) {
        hideProgressBar();
        String errorMessage = getString(R.string.wrong_imei_string);
        try {
            errorMessage = error.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        typographyImeiDescription.setText(errorMessage);
        typographyImeiDescription.setTextColor(MethodChecker.getColor(this,com.tokopedia.unifyprinciples.R.color.Unify_R600));
    }
}
