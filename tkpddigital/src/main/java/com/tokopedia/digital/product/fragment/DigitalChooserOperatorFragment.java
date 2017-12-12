package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.adapter.OperatorChooserAdapter;
import com.tokopedia.digital.product.model.Operator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserOperatorFragment extends BasePresenterFragment {

    private static final String ARG_PARAM_EXTRA_OPERATOR_LIST_DATA =
            "ARG_PARAM_EXTRA_OPERATOR_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW =
            "ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW";

    private static final String EXTRA_STATE_OPERATOR_LIST_DATA =
            "EXTRA_STATE_OPERATOR_LIST_DATA";
    private static final String EXTRA_STATE_OPERATOR_STYLE_VIEW =
            "EXTRA_STATE_OPERATOR_STYLE_VIEW";
    private static final String EXTRA_OPERATOR_LABEL = "EXTRA_OPERATOR_LABEL";
    private static final String EXTRA_STATE_CATEGORY = "EXTRA_STATE_CATEGORY";

    @BindView(R2.id.rv_list_chooser)
    RecyclerView rvOperatorList;
    @BindView(R2.id.field_search)
    EditText fieldSearch;

    private List<Operator> operatorListData;
    private String operatorStyleView;
    private String operatorLabel;
    private String categoryState;
    private ActionListener actionListener;
    private OperatorChooserAdapter operatorChooserAdapter;

    public static Fragment newInstance(List<Operator> operatorListData, String operatorStyleView,
                                       String operatorLabel, String categoryState) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorListData);
        bundle.putString(ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW, operatorStyleView);
        bundle.putString(EXTRA_OPERATOR_LABEL, operatorLabel);
        bundle.putString(EXTRA_STATE_CATEGORY, categoryState);
        Fragment fragment = new DigitalChooserOperatorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
        state.putParcelableArrayList(EXTRA_STATE_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) operatorListData);
        state.putString(EXTRA_STATE_OPERATOR_STYLE_VIEW, operatorStyleView);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        operatorListData = savedState.getParcelableArrayList(EXTRA_STATE_OPERATOR_LIST_DATA);
        operatorStyleView = savedState.getString(EXTRA_STATE_OPERATOR_STYLE_VIEW);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.operatorListData = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_OPERATOR_LIST_DATA);
        this.operatorStyleView = arguments.getString(ARG_PARAM_EXTRA_OPERATOR_STYLE_VIEW);
        this.operatorLabel = arguments.getString(EXTRA_OPERATOR_LABEL);
        categoryState = arguments.getString(EXTRA_STATE_CATEGORY);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chooser_product_digital_module;
    }

    @Override
    protected void initView(View view) {
        rvOperatorList.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (operatorListData.size() > 10) {
            fieldSearch.setHint(getResources().getString(R.string.action_search_with_suffix, operatorLabel));
            fieldSearch.setOnFocusChangeListener(onAnalyticsFocusChangedListener());
            fieldSearch.addTextChangedListener(onSearchTextChange());
            fieldSearch.clearFocus();
            rvOperatorList.requestFocus();
        }
        else fieldSearch.setVisibility(View.GONE);
    }

    @Override
    protected void setViewListener() {
        rvOperatorList.setAdapter(operatorChooserAdapter);
    }

    @Override
    protected void initialVar() {
        operatorChooserAdapter = new OperatorChooserAdapter(this, operatorListData,
                actionListener);
    }

    @Override
    protected void setActionVar() {

    }

    public interface ActionListener {
        void onOperatorItemSelected(Operator operator);

        void onOperatortItemChooserCanceled();
    }

    private void fiterData(String query) {
        List<Operator> searchOperatorList = new ArrayList<>();
        for (int i = 0; i < operatorListData.size(); i++) {
            if (operatorListData.get(i).getName().toLowerCase()
                    .contains(query.toLowerCase())) {
                searchOperatorList.add(operatorListData.get(i));
            }
        }
        operatorChooserAdapter.setSearchResultData(searchOperatorList);
        operatorChooserAdapter.notifyDataSetChanged();
    }

    private void checkEmptyQuery(String query) {
        if (query.isEmpty())
            operatorChooserAdapter.setSearchResultData(operatorListData);
    }

    private View.OnFocusChangeListener onAnalyticsFocusChangedListener(){
        return new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    UnifyTracking.eventClickSearchBar(categoryState,categoryState);
                }
            }
        };
    }

    private TextWatcher onSearchTextChange() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fiterData(s.toString());
                checkEmptyQuery(s.toString());
            }
        };
    }

}
