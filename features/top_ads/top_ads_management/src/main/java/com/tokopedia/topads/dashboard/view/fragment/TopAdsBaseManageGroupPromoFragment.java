package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.topads.R;
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.di.TopAdsAddPromoPoductDI;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAutoCompleteAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsManageGroupPromoView;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsManageGroupPromoPresenter;
import com.tokopedia.topads.dashboard.view.widget.TopAdsCustomAutoCompleteTextView;
import com.tokopedia.topads.dashboard.view.widget.TopAdsCustomRadioGroup;
import com.tokopedia.topads.dashboard.view.widget.TopAdsRadioExpandView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 2/16/17.
 */

public abstract class TopAdsBaseManageGroupPromoFragment<T extends TopAdsManageGroupPromoPresenter> extends BasePresenterFragment<T>
        implements TopAdsManageGroupPromoView {

    private TopAdsCustomRadioGroup radioGroup;

    protected EditText inputNewGroup;
    private TextInputLayout textInputLayoutNewGroup;
    protected TopAdsRadioExpandView viewRadioNewGroup;

    protected TopAdsCustomAutoCompleteTextView inputChooseGroup;
    private TextInputLayout textInputLayoutChooseGroup;
    protected TopAdsRadioExpandView viewRadioChooseGroup;

    protected TopAdsRadioExpandView viewRadioNotInGroup;
    private Button buttonNext;
    protected TextView titleChoosePromo;

    private TopAdsAutoCompleteAdapter adapterChooseGroup;
    private ArrayList<String> groupNames = new ArrayList<>();
    private List<GroupAd> groupAds = new ArrayList<>();
    protected String choosenId;
    private ProgressDialog progressDialog;

    protected String itemIdToAdd;
    protected String source;

    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void initialPresenter() {
        presenter = (T) TopAdsAddPromoPoductDI.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        if (arguments!= null) {
            itemIdToAdd = arguments.getString(TopAdsExtraConstant.EXTRA_ITEM_ID);
            source = arguments.getString(TopAdsExtraConstant.EXTRA_SOURCE, "");
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_manage_promo_product;
    }

    @Override
    protected void initView(View view) {
        buttonNext = (Button) view.findViewById(R.id.button_next);
        radioGroup = (TopAdsCustomRadioGroup) view.findViewById(R.id.radio_group);
        inputNewGroup = (EditText) view.findViewById(R.id.input_new_group);
        textInputLayoutNewGroup = (TextInputLayout) view.findViewById(R.id.input_layout_new_group_name);
        inputChooseGroup = (TopAdsCustomAutoCompleteTextView) view.findViewById(R.id.choose_group_auto_text);
        textInputLayoutChooseGroup = (TextInputLayout) view.findViewById(R.id.input_layout_choose_group);
        viewRadioNewGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_new_group);
        viewRadioChooseGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_choose_group);
        viewRadioNotInGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_not_in_group);
        titleChoosePromo = (TextView) view.findViewById(R.id.title_choose_promo);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        buttonNext.setText(getTitleButtonNext());

        // prevent other edit texts on focusing on start
        view.requestFocus();
    }

    @Override
    protected void setViewListener() {
        buttonNext.setOnClickListener(onClickNext());
        inputNewGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.checkIsGroupExist(editable.toString());
            }
        });
        inputChooseGroup.setAdapter(adapterChooseGroup);
        inputChooseGroup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus){
                    presenter.searchGroupName("");
                }
            }
        });
        inputChooseGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputChooseGroup.isPerformingCompletion()){
                    return;
                }
                presenter.searchGroupName(editable.toString());
            }
        });
        inputChooseGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputChooseGroup.lockView();
                if (groupAds.get(i) != null) {
                    choosenId = groupAds.get(i).getId();
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new TopAdsCustomRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(TopAdsCustomRadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio_promo_new_group) {
                    inputNewGroup.requestFocus();
                } else if (checkedId == R.id.radio_promo_choose_group) {
                    inputChooseGroup.requestFocus();
                } else if (checkedId == R.id.radio_promo_not_in_group) {
                    CommonUtils.hideKeyboard(getActivity(), getView());
                }
            }
        });
    }

    @Override
    protected void initialVar() {
        adapterChooseGroup = new TopAdsAutoCompleteAdapter(getActivity(), R.layout.item_autocomplete_text);
        adapterChooseGroup.setListenerGetData(new TopAdsAutoCompleteAdapter.ListenerGetData() {
            @Override
            public ArrayList<String> getData() {
                return groupNames;
            }
        });
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCheckGroupExistError() {
        textInputLayoutNewGroup.setError(getString(R.string.error_connection_problem));
    }

    @Override
    public void onGroupExist() {
        textInputLayoutNewGroup.setError(getString(R.string.top_ads_title_grup_exist));
    }

    @Override
    public void onGroupNotExist() {
        textInputLayoutNewGroup.setError(null);
    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        this.groupAds.clear();
        this.groupAds.addAll(groupAds);
        groupNames.clear();
        textInputLayoutChooseGroup.setError(null);
        for (GroupAd groupAd : groupAds) {
            groupNames.add(groupAd.getName());
        }
        inputChooseGroup.showDropDownFilter();
    }

    @Override
    public void onGetGroupAdListError() {
        textInputLayoutChooseGroup.setError(getString(R.string.error_connection_problem));
    }

    @Override
    public void onGroupNotExistOnSubmitNewGroup() {
        onGroupNotExist();
        onSubmitFormNewGroup(inputNewGroup.getText().toString());
    }

    @Override
    public void showErrorGroupNameNotValid() {
        textInputLayoutNewGroup.setError(getString(R.string.top_ads_title_group_name_not_valid));
    }

    @Override
    public void hideErrorGroupNameNotValid() {
        textInputLayoutNewGroup.setError(null);
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onValidateForm(radioGroup.getCheckedRadioButtonId());
            }
        };
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void showErrorShouldFillGroupName() {
        textInputLayoutNewGroup.setError(getString(R.string.label_top_ads_error_empty_group_name));
    }

    @Override
    public void dismissLoading() {
        progressDialog.dismiss();
    }

    protected void onValidateForm(int checkedRadioButtonId) {
        if (checkedRadioButtonId == R.id.radio_promo_new_group) {
            onSubmitPromoNewGroup();
        } else if (checkedRadioButtonId == R.id.radio_promo_choose_group) {
            onSubmitPromoChooseGroup();
        } else if (checkedRadioButtonId == R.id.radio_promo_not_in_group) {
            onSubmitPromoNotInGroup();
        } else {
            onErrorSubmit();
        }
    }

    private void onSubmitPromoNotInGroup() {
        onSubmitFormNotInGroup();
    }

    private void onSubmitPromoChooseGroup() {
        if (!inputChooseGroup.isEnabled()) {
            onSubmitFormChooseGroup(choosenId);
        } else {
            textInputLayoutChooseGroup.setError(getString(R.string.label_top_ads_error_choose_one_group));
        }
    }

    private void onSubmitPromoNewGroup() {
        presenter.checkIsGroupExistOnSubmitNewGroup(inputNewGroup.getText().toString());
    }

    protected void onErrorSubmit() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_choose_one_option));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    protected abstract void onSubmitFormNewGroup(String groupName);

    protected abstract void onSubmitFormNotInGroup();

    protected abstract void onSubmitFormChooseGroup(String choosenId);

    protected abstract String getTitleButtonNext();
}
