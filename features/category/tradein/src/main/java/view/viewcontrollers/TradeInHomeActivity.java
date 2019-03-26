package view.viewcontrollers;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

import org.json.JSONException;
import org.json.JSONObject;

import viewmodel.TradeInHomeViewModel;

public class TradeInHomeActivity extends BaseTradeInActivity<TradeInHomeViewModel> {

    private TextView mTvPriceElligible;
    private ImageView mButtonRemove;
    private TextView mTvModelName;
    private TextView mTvHeaderPrice;
    private TextView mTvInitialPrice;
    private TextView mTvGoToProductDetails;
    private TextView mTvNotUpto;
    private TradeInHomeViewModel tradeInHomeViewModel;
    private boolean isAlreadySet = false;
    public static final int TRADEIN_HOME_REQUEST = 22345;

    public static Intent getIntent(Context context) {
        return new Intent(context, TradeInHomeActivity.class);
    }


    @Override
    void initView() {
        mTvPriceElligible = findViewById(R.id.tv_price_elligible);
        mButtonRemove = findViewById(R.id.button_remove);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvHeaderPrice = findViewById(R.id.tv_header_price);
        mTvInitialPrice = findViewById(R.id.tv_initial_price);
        mTvNotUpto = findViewById(R.id.tv_not_upto);
        mTvGoToProductDetails = findViewById(R.id.tv_go_to_product_details);
        mTvModelName.setText(new StringBuilder().append(Build.MANUFACTURER).append(" ").append(Build.MODEL).toString());
    }

    @Override
    public Class<TradeInHomeViewModel> getViewModelType() {
        return TradeInHomeViewModel.class;
    }

    @Override
    void setViewModel(ViewModel viewModel) {
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
                        mTvGoToProductDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tradeInHomeViewModel.startGUITest();
                            }
                        });
                        int maxPrice = jsonObject.getInt("max_price");
                        int minPrice = jsonObject.getInt("min_price");
                        if (minPrice > tradeInHomeViewModel.getTradeInParams().getNewPrice()) {
                            String notElligible = getString(R.string.not_elligible_price_high);
                            SpannableString spannableString = new SpannableString(notElligible);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    showTnC(R.string.tradein_tnc);
                                }
                            };
                            mTvGoToProductDetails.setText(R.string.go_to_product_details);
                            mTvGoToProductDetails.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
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
                        }
                        mTvNotUpto.setVisibility(View.VISIBLE);
                        mTvInitialPrice.setText(String.format("%1$s - %2$s",
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, true),
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, true)));
                        isAlreadySet = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tradeInHomeViewModel.getPriceFailData().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                hideProgressBar();
                try {
                    mTvInitialPrice.setText(jsonObject.getString("message"));
                    mTvPriceElligible.setText(getString(R.string.not_elligible));
                    mTvPriceElligible.setVisibility(View.VISIBLE);
                    mTvGoToProductDetails.setText(R.string.go_to_product_details);
                    mTvGoToProductDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    int getMenuRes() {
        return R.menu.trade_in_home;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_activity_tradeinhome;
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case TradeInHomeViewModel.MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showProgressBar();
                    tradeInHomeViewModel.getMaxPrice();
                } else {
                    tradeInHomeViewModel.requestPermission();
                }
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
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing())
            isAlreadySet = false;
    }
}
