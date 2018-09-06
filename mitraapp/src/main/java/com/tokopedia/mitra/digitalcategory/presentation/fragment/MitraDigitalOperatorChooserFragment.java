package com.tokopedia.mitra.digitalcategory.presentation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.RenderProductModel;
import com.tokopedia.mitra.R;
import com.tokopedia.mitra.digitalcategory.di.AgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.di.DaggerAgentDigitalCategoryComponent;
import com.tokopedia.mitra.digitalcategory.presentation.adapter.MitraDigitalOperatorChooserAdapter;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalOperatorChooserContract;
import com.tokopedia.mitra.digitalcategory.presentation.presenter.MitraDigitalOperatorChooserPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rizky on 06/09/18.
 */
public class MitraDigitalOperatorChooserFragment extends BaseDaggerFragment
        implements MitraDigitalOperatorChooserContract.View, MitraDigitalOperatorChooserAdapter.ActionListener {

    private static final String ARG_PARAM_CATEGORY_ID = "ARG_PARAM_CATEGORY_ID";
    private static final String ARG_PARAM_OPERATOR_LABEL = "ARG_PARAM_OPERATOR_LABEL";
    private static final String ARG_PARAM_CATEGORY = "ARG_PARAM_CATEGORY";

    private String categoryId;
    private String operatorLabel;
    private String categoryName;

    private RecyclerView rvOperatorList;
    private EditText fieldSearch;
    private ProgressBar pbMainLoading;

    private MitraDigitalOperatorChooserAdapter operatorChooserAdapter;

    private List<Operator> operatorList = new ArrayList<>();

    private ActionListener actionListener;

    @Inject
    MitraDigitalOperatorChooserPresenter presenter;

    public interface ActionListener {

        void onOperatorItemSelected(Operator operator);

    }

    public static Fragment newInstance(String categoryId, String operatorLabel, String categoryName) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_CATEGORY_ID, categoryId);
        bundle.putString(ARG_PARAM_OPERATOR_LABEL, operatorLabel);
        bundle.putString(ARG_PARAM_CATEGORY, categoryName);
        Fragment fragment = new MitraDigitalOperatorChooserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString(ARG_PARAM_CATEGORY_ID);
            operatorLabel = getArguments().getString(ARG_PARAM_OPERATOR_LABEL);
            categoryName = getArguments().getString(ARG_PARAM_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_mitra_digital_operator_chooser, container,
                false);

        rvOperatorList = rootview.findViewById(R.id.rv_list_chooser);
        fieldSearch = rootview.findViewById(R.id.field_search);
        pbMainLoading = rootview.findViewById(R.id.pb_main_loading);

        fieldSearch.setOnFocusChangeListener(onAnalyticsFocusChangedListener());
        fieldSearch.addTextChangedListener(onSearchTextChange());

        rvOperatorList.setLayoutManager(new LinearLayoutManager(getActivity()));

        operatorChooserAdapter = new MitraDigitalOperatorChooserAdapter(this, operatorList);

        rvOperatorList.setAdapter(operatorChooserAdapter);

        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.attachView(this);

        presenter.getOperators(Integer.valueOf(categoryId));
    }

    @Override
    protected void initInjector() {
        DigitalComponent digitalComponent =
                DaggerDigitalComponent.builder().baseAppComponent((
                        (BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                        .build();
        AgentDigitalCategoryComponent agentDigitalCategoryComponent =
                DaggerAgentDigitalCategoryComponent.builder().digitalComponent(digitalComponent)
                        .build();
        agentDigitalCategoryComponent.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void renderOperators(List<RenderProductModel> renderProductModels) {
        //TODO: render operators here
        for (RenderProductModel renderProductModel : renderProductModels) {
            operatorList.add(renderProductModel.getOperator());
        }
        operatorChooserAdapter.notifyDataSetChanged();

        if (renderProductModels.size() > 10) {
            fieldSearch.setHint(getResources().getString(R.string.action_search_with_suffix, operatorLabel));
            fieldSearch.clearFocus();
            fieldSearch.setVisibility(View.VISIBLE);
            rvOperatorList.requestFocus();
        } else {
            fieldSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOperatorItemSelected(Operator operator) {
        actionListener.onOperatorItemSelected(operator);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.actionListener = (ActionListener) activity;
    }

    private View.OnFocusChangeListener onAnalyticsFocusChangedListener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    UnifyTracking.eventClickSearchBar(categoryName, categoryName);
//                }
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

    private void fiterData(String query) {
        List<Operator> searchOperatorList = new ArrayList<>();
        for (int i = 0; i < operatorList.size(); i++) {
            if (operatorList.get(i).getName().toLowerCase()
                    .contains(query.toLowerCase())) {
                searchOperatorList.add(operatorList.get(i));
            }
        }
        operatorChooserAdapter.setSearchResultData(searchOperatorList);
        operatorChooserAdapter.notifyDataSetChanged();
    }

    private void checkEmptyQuery(String query) {
        if (query.isEmpty())
            operatorChooserAdapter.setSearchResultData(operatorList);
    }

}