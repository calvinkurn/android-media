package view.viewcontrollers;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tradein.R;
import com.tokopedia.design.utils.CurrencyFormatUtil;

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
    private TradeInHomeViewModel tradeInHomeViewModel;
    private int newPrice;
    private boolean isShowingTnC;


    @Override
    void initView() {
        newPrice = getIntent().getIntExtra("NEW PRICE", 0);
        mTvPriceElligible = findViewById(R.id.tv_price_elligible);
        mButtonRemove = findViewById(R.id.button_remove);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvHeaderPrice = findViewById(R.id.tv_header_price);
        mTvInitialPrice = findViewById(R.id.tv_initial_price);
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
                    if (minPrice > newPrice) {
                        String notElligible = getString(R.string.not_elligible_price_high);
                        SpannableString spannableString = new SpannableString(notElligible);
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {

                            }
                        };
                        int greenColor = getResources().getColor(R.color.green_nob);
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(greenColor);
                        spannableString.setSpan(clickableSpan, 66, 84, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(foregroundColorSpan, 66, 84, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        mTvPriceElligible.setVisibility(View.VISIBLE);
                        mTvInitialPrice.setText(String.format("%1$s - %2$s",
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(minPrice, false),
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, false)));
                    }
                    mTvInitialPrice.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat(maxPrice, false));
                } catch (JSONException e) {
                    e.printStackTrace();
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
        return 0;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_show_tnc) {
            showTnC();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showTnC() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
            getSupportActionBar().setTitle("Syarat dan Ketentuan");
        }
        isShowingTnC = true;
        TnCFragment fragment = TnCFragment.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack("TNC");
        transaction.replace(R.id.root_view, fragment);
        transaction.commit();
    }
}
