package com.tokopedia.topads.keyword.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.product.manage.item.common.util.CurrencyIdrTextWatcher;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.topads.R;
import com.tokopedia.topads.common.util.TopAdsComponentUtils;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordEditDetailComponent;
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordEditDetailModule;
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordEditDetailView;
import com.tokopedia.topads.keyword.view.model.KeywordAd;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenter;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 5/23/17.
 */

public abstract class TopAdsKeywordEditDetailFragment extends BaseDaggerFragment implements TopAdsKeywordEditDetailView {
    public static final String TAG = "TopAdsKeywordEditDetailFragment";
    public static final int DEFAULT_KELIPATAN = 1_000;
    protected SpinnerTextView topAdsKeywordType;
    protected EditText topAdsKeyword;
    protected PrefixEditText topAdsCostPerClick;
    protected TextView topAdsMaxPriceInstruction;
    protected ProgressDialog progressDialog;
    protected TextInputLayout textInputLayoutCostPerClick;
    protected TextInputLayout textInputLayoutKeyword;
    protected Button submitButton;

    @Inject
    TopAdsKeywordEditDetailPresenter presenter;
    private KeywordAd keywordAd;

    public static Bundle createArguments(KeywordAd model) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(TopAdsExtraConstant.EXTRA_AD, model);
        return bundle;
    }

    @Override
    protected void initInjector() {
        DaggerTopAdsKeywordEditDetailComponent
                .builder()
                .topAdsComponent(TopAdsComponentUtils.getTopAdsComponent(this))
                .topAdsKeywordEditDetailModule(new TopAdsKeywordEditDetailModule())
                .build()
                .inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        keywordAd = bundle.getParcelable(TopAdsExtraConstant.EXTRA_AD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_top_ads_keyword_edit_detail, container, false);
        submitButton = view.findViewById(R.id.button_submit);
        settingTopAdsKeywordType(view);
        settingTopAdsKeyword(view);
        settingTopAdsCostPerClick(view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));

        if (keywordAd != null) {
            fillDataToView(keywordAd);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                presenter.editTopAdsKeywordDetail(collectDataFromView());
            }
        });

        presenter.attachView(this);

        return view;
    }

    private void showLoading() {
        progressDialog.show();
    }

    protected void hideLoading(){
        progressDialog.dismiss();
    }

    private void fillDataToView(KeywordAd keywordAd) {
        topAdsKeywordType.setSpinnerValueByEntries(keywordAd.getkeywordTypeDesc());
        topAdsKeyword.setText(keywordAd.getName());
        topAdsCostPerClick.setText(String.valueOf(CurrencyFormatHelper.convertRupiahToInt(keywordAd.getPriceBidFmt())));
    }

    protected void settingTopAdsCostPerClick(View view) {
        topAdsCostPerClick = view.findViewById(R.id.edit_text_top_ads_cost_per_click);
        topAdsMaxPriceInstruction = view.findViewById(R.id.text_view_top_ads_max_price_description);
        textInputLayoutCostPerClick = view.findViewById(R.id.text_input_layout_top_ads_cost_per_click);
        CurrencyIdrTextWatcher textWatcher = new CurrencyIdrTextWatcher(topAdsCostPerClick){
            @Override
            public void onNumberChanged(double number) {
                super.onNumberChanged(number);
                String errorMessage =
                        com.tokopedia.topads.dashboard.utils.ViewUtils.getKeywordClickBudgetError(getActivity(), number);
                if (!TextUtils.isEmpty(errorMessage)) {
                    textInputLayoutCostPerClick.setError(errorMessage);
                    submitButton.setEnabled(false);
                } else {
                    textInputLayoutCostPerClick.setError(null);
                    submitButton.setEnabled(true);
                }
            }
        };
        topAdsCostPerClick.addTextChangedListener(textWatcher);
    }

    private void settingTopAdsKeyword(View view) {
        topAdsKeyword = ((EditText) view.findViewById(R.id.edit_text_top_ads_keyword));
        textInputLayoutKeyword = (TextInputLayout) view.findViewById(R.id.text_input_layout_top_ads_keyword);
        topAdsKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (TextUtils.isEmpty(text)){
                    textInputLayoutKeyword.setError(getString(R.string.top_ads_keyword_must_be_filled));
                } else {
                    textInputLayoutKeyword.setError(null);
                }
                checkButtonNeedEnable();
            }
        });
    }

    private void checkButtonNeedEnable() {
        if (submitButton.isEnabled() && TextUtils.isEmpty(topAdsKeyword.getText().toString())){
            submitButton.setEnabled(false);
        } else if (!submitButton.isEnabled() && !TextUtils.isEmpty(topAdsKeyword.getText().toString())){
            submitButton.setEnabled(true);
        }
    }

    protected void settingTopAdsKeywordType(View view) {
        topAdsKeywordType = ((SpinnerTextView) view.findViewById(R.id.spinner_text_view_top_ads_keyword_type));
    }

    private KeywordAd collectDataFromView() {
        if (!TextUtils.isEmpty(topAdsKeywordType.getSpinnerValue())) {
            keywordAd.setKeywordTypeId(topAdsKeywordType.getSpinnerValue());
        }
        keywordAd.setKeywordTag(getTopAdsKeywordText());
        keywordAd.setPriceBidFmt(getTopAdsCostPerClick());
        return keywordAd;
    }

    public String getTopAdsKeywordText() {
        return topAdsKeyword.getText().toString();
    }

    public String getTopAdsCostPerClick() {
        return topAdsCostPerClick.getTextWithoutPrefix();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessEditTopAdsKeywordDetail(KeywordAd viewModel) {
        hideLoading();
        finishAndSetResult(viewModel.getId());
    }

    void finishAndSetResult(String id) {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED , true);
        intent.putExtra(TopAdsExtraConstant.EXTRA_AD_ID, id);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void showError(Throwable detail) {
        hideLoading();
        NetworkErrorHelper.showCloseSnackbar(getActivity(), ViewUtils.getErrorMessage(getActivity(), detail));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}