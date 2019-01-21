package com.tokopedia.topads.dashboard.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.base.list.seller.view.fragment.BasePresenterFragment;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.base.view.model.StepperModel;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsSuggestionBidInteractionTypeDef;
import com.tokopedia.topads.dashboard.data.model.response.GetSuggestionResponse;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.model.TopAdsDetailAdViewModel;

/**
 * Created by zulfikarrahman on 8/7/17.
 */

public abstract class TopAdsNewCostFragment<T extends StepperModel, V extends TopAdsDetailAdViewModel> extends BasePresenterFragment {

    private static final String TAG = "TopAdsNewCostFragment";

    protected T stepperModel;
    protected StepperListener stepperListener;
    protected Button submitButton;
    protected ProgressDialog progressDialog;
    protected TextView headerText;
    protected TextView titleCost;
    protected TextView titleSuggestionBidUse;
    protected TextView titleSuggestionBid;
    protected V detailAd;
    private TextInputLayout maxPriceInputLayout;
    protected PrefixEditText maxPriceEditText;
    private RadioGroup budgetRadioGroup;
    private RadioButton budgetLifeTimeRadioButton;
    private RadioButton budgetPerDayRadioButton;
    private TextInputLayout budgetPerDayInputLayout;
    private View containerBudgetPerDay;
    private PrefixEditText budgetPerDayEditText;
    protected long suggestionBidValue;
    protected String defaultSuggestionBidButtonStatus;

    protected void onClickedNext() {
        showLoading();
        populateDataFromFields();
    }

    protected abstract V initiateDetailAd();

    @Override
    protected void initView(View view) {
        super.initView(view);
        maxPriceInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_max_price);
        maxPriceEditText = (PrefixEditText) view.findViewById(R.id.edit_text_max_price);
        budgetRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_budget);
        budgetLifeTimeRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_life_time);
        budgetPerDayRadioButton = (RadioButton) view.findViewById(R.id.radio_button_budget_per_day);
        budgetPerDayInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_budget_per_day);
        containerBudgetPerDay = view.findViewById(R.id.container_budget_per_day);
        budgetPerDayEditText = (PrefixEditText) view.findViewById(R.id.edit_text_budget_per_day);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        headerText = (TextView) view.findViewById(R.id.header_text);
        titleCost = (TextView) view.findViewById(R.id.title_cost);
        // trigger the watcher first time
        budgetPerDayEditText.setText(budgetPerDayEditText.getText());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        titleSuggestionBid = (TextView) view.findViewById(R.id.text_suggestion_bid);
        titleSuggestionBidUse = (TextView) view.findViewById(R.id.text_suggestion_bid_use);
        titleSuggestionBidUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPriceEditText.setText(String.valueOf(suggestionBidValue));
                onSuggestionBidClicked();
            }
        });
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadSuggestionBid();
        defaultSuggestionBidButtonStatus = TopAdsSuggestionBidInteractionTypeDef.NO_SUGGESTION;
    }

    protected abstract void loadSuggestionBid();

    @Override
    protected void initialVar() {
        super.initialVar();
        detailAd = initiateDetailAd();
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        maxPriceEditText.addTextChangedListener(new CurrencyIdrTextWatcher(maxPriceEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                checkMaxPrice(number);
                onPriceChanged(number);
            }
        });
        budgetPerDayEditText.addTextChangedListener(new CurrencyIdrTextWatcher(budgetPerDayEditText, getString(R.string.top_ads_detail_edit_default_currency_value)) {

            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String clickBudgetString = CurrencyFormatHelper.RemoveNonNumeric(maxPriceEditText.getTextWithoutPrefix());
                float clickBudget = 0;
                if (!TextUtils.isEmpty(clickBudgetString)) {
                    clickBudget = Float.parseFloat(clickBudgetString);
                }
                String errorMessage = ViewUtils.getDailyBudgetError(getActivity(), clickBudget, number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    budgetPerDayInputLayout.setError(errorMessage);
                } else {
                    budgetPerDayInputLayout.setError(null);
                }
            }
        });
        budgetPerDayEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    CommonUtils.hideKeyboard(getActivity(), view);
                }
            }
        });
        budgetRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_budget_life_time) {
                    showBudgetPerDay(false);
                } else if (checkedId == R.id.radio_button_budget_per_day) {
                    showBudgetPerDay(true);
                }
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickedNext();
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    protected boolean isPriceError() {
        boolean maxPriceError = maxPriceInputLayout.isErrorEnabled() && maxPriceInputLayout.getError() != null;
        if (maxPriceError) {
            return true;
        }
        boolean budgetPerDayError = budgetPerDayInputLayout.isErrorEnabled() && budgetPerDayInputLayout.getError() != null;
        if (budgetPerDayRadioButton.isChecked() && budgetPerDayError) {
            return true;
        }
        return false;
    }

    protected void onPriceChanged(double number) {

    }

    protected void onSuggestionBidClicked() {

    }

    protected void setSuggestionBidText(@Nullable GetSuggestionResponse data) {
        if (data == null) {
            return;
        }
        setSuggestionBidText(data.getData().get(0).getMedian(), data.getData().get(0).getMedianFmt());
        if (TextUtils.isEmpty(maxPriceEditText.getTextWithoutPrefix())) {
            maxPriceEditText.setText(String.valueOf(data.getData().get(0).getMedian()));
            budgetPerDayEditText.setText(String.valueOf(data.getData().get(0).getMedian()*10));
        }
    }

    protected void setSuggestionBidText(long suggestionBidValue, @Nullable String suggestionBidText){
        this.suggestionBidValue = suggestionBidValue;
        titleSuggestionBid.setText(MethodChecker.fromHtml(getString(R.string.label_top_ads_max_price_description) + " <b>" + suggestionBidText + "</b> "));
        titleSuggestionBidUse.setVisibility(View.VISIBLE);
    }

    private void checkMaxPrice(double number) {
        String errorMessage = ViewUtils.getClickBudgetError(getActivity(), number);
        if (!TextUtils.isEmpty(errorMessage)) {
            maxPriceInputLayout.setError(errorMessage);
        } else {
            maxPriceInputLayout.setError(null);
        }
    }

    private void showBudgetPerDay(boolean show) {
        containerBudgetPerDay.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show){
            containerBudgetPerDay.getParent().requestChildFocus(containerBudgetPerDay, containerBudgetPerDay);
        }
        if (!show && !budgetLifeTimeRadioButton.isChecked()) {
            budgetLifeTimeRadioButton.setChecked(true);
        }
        if (show && !budgetPerDayRadioButton.isChecked()) {
            budgetPerDayRadioButton.setChecked(true);
        }
    }

    protected void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading() {
        progressDialog.dismiss();
    }

    protected void populateDataFromFields() {
        String priceBid = maxPriceEditText.getTextWithoutPrefix();

        if (TextUtils.isEmpty(priceBid)) {
            detailAd.setPriceBid(0);
        } else {
            detailAd.setPriceBid(Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(priceBid)));
        }
        if (budgetLifeTimeRadioButton.isChecked()) {
            detailAd.setBudget(false);
            detailAd.setPriceDaily(0);
        } else {
            String priceDaily = budgetPerDayEditText.getTextWithoutPrefix();
            if (TextUtils.isEmpty(priceDaily)) {
                detailAd.setPriceDaily(0);
            } else {
                detailAd.setPriceDaily(Float.parseFloat(CurrencyFormatHelper.RemoveNonNumeric(priceDaily)));
            }
            detailAd.setBudget(true);
        }
    }

    protected void loadAd(V detailAd) {
        if (detailAd != null) {
            this.detailAd = detailAd;
            maxPriceEditText.setText(String.valueOf(detailAd.getPriceBid()));
            if (detailAd.getPriceDaily() > 0) {
                showBudgetPerDay(true);
                budgetPerDayEditText.setText(String.valueOf(detailAd.getPriceDaily()));
            } else {
                showBudgetPerDay(false);
            }
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_cost;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        super.setupArguments(arguments);
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener) context;
        }
    }
}
