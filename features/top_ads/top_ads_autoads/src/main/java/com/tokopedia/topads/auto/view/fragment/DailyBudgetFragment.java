package com.tokopedia.topads.auto.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.design.text.watcher.CurrencyTextWatcher;
import com.tokopedia.design.text.watcher.NumberTextWatcher;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.topads.auto.router.TopAdsAutoRouter;
import com.tokopedia.topads.auto.view.widget.InfoAutoAdsSheet;
import com.tokopedia.topads.auto.view.widget.ManualAdsConfirmationSheet;
import com.tokopedia.topads.auto.R;
import com.tokopedia.topads.auto.view.activity.AutoAdsActivatedActivity;
import com.tokopedia.topads.auto.view.widget.Range;
import com.tokopedia.topads.auto.view.widget.RangeSeekBar;
import com.tokopedia.topads.auto.viewmodel.DailyBudgetViewModel;
import com.tokopedia.topads.common.constant.TopAdsAddingOption;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class DailyBudgetFragment extends BaseDaggerFragment implements View.OnClickListener,
        ManualAdsConfirmationSheet.ActionListener,
        SeekBar.OnSeekBarChangeListener {

    private static final int REQUEST_CODE_AD_OPTION = 3;
    public static final String SELECTED_OPTION = "selected_option";
    public static final int MIN_BUDGET = 10000;
    public static final int MAX_BUDGET = 20000;
    private Button startAutoAdsBtn;
    private Button startManualAdsBtn;
    private RangeSeekBar sekBar;
    private TextView priceRange;
    private PrefixEditText priceEditText;
    private ManualAdsConfirmationSheet adsConfirmationSheet;
    private DailyBudgetViewModel budgetViewModel;
    private TextInputLayout budgetInputLayout;

    public static DailyBudgetFragment newInstance() {

        Bundle args = new Bundle();

        DailyBudgetFragment fragment = new DailyBudgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        adsConfirmationSheet = ManualAdsConfirmationSheet.Companion.newInstance(getActivity());
        adsConfirmationSheet.setActionListener(this);
        budgetViewModel = ViewModelProviders.of(this).get(DailyBudgetViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_info) {
            InfoAutoAdsSheet.Companion.newInstance(getActivity()).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start_manual_ads_btn) {
            adsConfirmationSheet.show();
        } else if (id == R.id.start_autoads_btn) {
            startActivity(new Intent(getActivity(), AutoAdsActivatedActivity.class));
        }
    }

    @Override
    public void onManualAdsClicked() {
        Intent intent = ((TopAdsAutoRouter) getActivity().getApplication())
                .getTopAdsAddingPromoOptionIntent(getActivity());
        startActivityForResult(intent, REQUEST_CODE_AD_OPTION);
    }

    @Override
    public void onAutoAdsClicked() {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        double minBid = 200;
        double maxBid = 300;

        priceRange.setText(budgetViewModel.getPotentialImpression(minBid, maxBid, progress));
        priceEditText.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_start_daily_budget, container, false);
        startAutoAdsBtn = view.findViewById(R.id.start_autoads_btn);
        startManualAdsBtn = view.findViewById(R.id.start_manual_ads_btn);
        priceRange = view.findViewById(R.id.price_range);
        budgetInputLayout = view.findViewById(R.id.input_layout_budget_price);
        priceEditText = view.findViewById(R.id.edit_text_max_price);
        sekBar = view.findViewById(R.id.seekbar);
        setListener();
        return view;
    }

    private void setListener(){
        sekBar.setRange(new Range(MIN_BUDGET, MAX_BUDGET, 1000));
        sekBar.setValue(15000);
        sekBar.setOnSeekBarChangeListener(this);
        startManualAdsBtn.setOnClickListener(this);
        startAutoAdsBtn.setOnClickListener(this);
        priceEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        priceEditText.addTextChangedListener(new NumberTextWatcher(priceEditText, "0"){
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String error = budgetViewModel.checkBudget(getActivity(), number, MIN_BUDGET, MAX_BUDGET);
                if(!TextUtils.isEmpty(error)){
                    budgetInputLayout.setError(error);
                } else {
                    budgetInputLayout.setError(null);
                }
            }
        });
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AD_OPTION) {
            if (data != null) {
                switch (data.getIntExtra(SELECTED_OPTION, -1)) {
                    case TopAdsAddingOption.GROUP_OPT:
                        onSummaryGroupClicked();
                        break;
                    case TopAdsAddingOption.PRODUCT_OPT:
                        gotoCreateProductAd();
                        break;
                    case TopAdsAddingOption.KEYWORDS_OPT:
                        gotoCreateKeyword();
                        break;
                }
                getActivity().finishAffinity();
            }
        }
    }

    private void gotoCreateProductAd() {
        TopAdsAutoRouter router = ((TopAdsAutoRouter) getActivity().getApplication());
        if(GlobalConfig.isSellerApp()){
            startActivity(router.getTopAdsGroupNewPromoIntent(getActivity()));
        } else {
            router.openTopAdsDashboardApplink(getActivity());
        }
    }

    private void onSummaryGroupClicked() {
        TopAdsAutoRouter router = ((TopAdsAutoRouter) getActivity().getApplication());
        if(GlobalConfig.isSellerApp()){
            startActivity(router.getTopAdsGroupAdListIntent(getActivity()));
        } else {
            router.openTopAdsDashboardApplink(getActivity());
        }
    }

    private void gotoCreateKeyword() {
        TopAdsAutoRouter router = ((TopAdsAutoRouter) getActivity().getApplication());
        if(GlobalConfig.isSellerApp()){
            startActivity(router.getTopAdsKeywordNewChooseGroupIntent(getActivity(), true, null));
        } else {
            router.openTopAdsDashboardApplink(getActivity());
        }
    }
}
