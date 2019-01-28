package com.tokopedia.digital.product.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.digital.R;
import com.tokopedia.digital.product.di.DigitalProductComponentInstance;
import com.tokopedia.digital.product.view.adapter.OperatorChooserAdapter;
import com.tokopedia.digital.product.view.presenter.OperatorChooserContract;
import com.tokopedia.digital.product.view.presenter.OperatorChooserPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;
import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public class DigitalChooserOperatorFragment extends BasePresenterFragment<OperatorChooserContract.Presenter> implements
        OperatorChooserContract.View {

    private final String TAG = DigitalChooserOperatorFragment.class.getSimpleName();

    private static final String ARG_PARAM_CATEGORY_ID = "ARG_PARAM_CATEGORY_ID";
    private static final String ARG_PARAM_OPERATOR_STYLE_VIEW = "ARG_PARAM_OPERATOR_STYLE_VIEW";
    private static final String ARG_PARAM_OPERATOR_LABEL = "ARG_PARAM_OPERATOR_LABEL";
    private static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";

    private static final String EXTRA_STATE_OPERATOR_STYLE_VIEW =
            "EXTRA_STATE_OPERATOR_STYLE_VIEW";

    private RecyclerView rvOperatorList;
    private EditText fieldSearch;
    private ProgressBar pbMainLoading;

    private CompositeSubscription compositeSubscription;

    private List<Operator> operators = new ArrayList<>();

    private OperatorChooserAdapter operatorChooserAdapter;

    private String categoryId;
    private String operatorStyleView;
    private String operatorLabel;
    private String categoryName;

    private ActionListener actionListener;

    @Inject
    OperatorChooserPresenter presenter;

    public interface ActionListener {
        void onOperatorItemSelected(Operator operator);
    }

    public static Fragment newInstance(String categoryId, String operatorStyleView,
                                       String operatorLabel, String categoryName) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_OPERATOR_STYLE_VIEW, operatorStyleView);
        bundle.putString(ARG_PARAM_OPERATOR_LABEL, operatorLabel);
        bundle.putString(ARG_PARAM_CATEGORY, categoryName);
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
        presenter.getOperatorsByCategoryId(categoryId);
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_STATE_OPERATOR_STYLE_VIEW, operatorStyleView);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        operatorStyleView = savedState.getString(EXTRA_STATE_OPERATOR_STYLE_VIEW);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initInjector() {
        super.initInjector();

        DigitalProductComponentInstance.getDigitalProductComponent(getActivity().getApplication())
                .inject(this);
    }

    @Override
    protected void initialPresenter() {
        if (compositeSubscription == null) compositeSubscription = new CompositeSubscription();

        presenter.attachView(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    public static int sizeAsParcel(@NonNull Bundle bundle) {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeBundle(bundle);
            return parcel.dataSize();
        } finally {
            parcel.recycle();
        }
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        Log.d(TAG, String.valueOf(sizeAsParcel(arguments)));
        categoryId = arguments.getString(ARG_PARAM_CATEGORY_ID);
        operatorStyleView = arguments.getString(ARG_PARAM_OPERATOR_STYLE_VIEW);
        operatorLabel = arguments.getString(ARG_PARAM_OPERATOR_LABEL);
        categoryName = arguments.getString(ARG_PARAM_CATEGORY);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_chooser_product_digital_module;
    }

    @Override
    protected void initView(View view) {
        rvOperatorList = view.findViewById(R.id.rv_list_chooser);
        fieldSearch = view.findViewById(R.id.field_search);
        pbMainLoading = view.findViewById(R.id.pb_main_loading);

        rvOperatorList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void setViewListener() {
        fieldSearch.setOnFocusChangeListener(onAnalyticsFocusChangedListener());
        fieldSearch.addTextChangedListener(onSearchTextChange());
    }

    @Override
    protected void initialVar() {
        operatorChooserAdapter = new OperatorChooserAdapter(this, operators,
                actionListener);

        rvOperatorList.setAdapter(operatorChooserAdapter);
    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showOperators(List<Operator> operators) {
        this.operators.clear();
        this.operators.addAll(operators);
        operatorChooserAdapter.notifyDataSetChanged();

        if (operators.size() > 10) {
            fieldSearch.setHint(getResources().getString(R.string.action_search_with_suffix, operatorLabel));
            fieldSearch.clearFocus();
            fieldSearch.setVisibility(View.VISIBLE);
            rvOperatorList.requestFocus();
        } else {
            fieldSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInitialProgressLoading() {
        pbMainLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInitialProgressLoading() {
        pbMainLoading.setVisibility(View.GONE);
    }

    private void fiterData(String query) {
        List<Operator> searchOperatorList = new ArrayList<>();
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).getName().toLowerCase()
                    .contains(query.toLowerCase())) {
                searchOperatorList.add(operators.get(i));
            }
        }
        operatorChooserAdapter.setSearchResultData(searchOperatorList);
        operatorChooserAdapter.notifyDataSetChanged();
    }

    private void checkEmptyQuery(String query) {
        if (query.isEmpty())
            operatorChooserAdapter.setSearchResultData(operators);
    }

    private View.OnFocusChangeListener onAnalyticsFocusChangedListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    UnifyTracking.eventClickSearchBar(getActivity(),categoryName, categoryName);
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

    @Override
    public void onDestroy() {
        if (compositeSubscription != null && compositeSubscription.hasSubscriptions())
            compositeSubscription.unsubscribe();

        super.onDestroy();
    }

}
