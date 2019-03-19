package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.activity.FilterActivity;
import com.tokopedia.affiliate.feature.explore.view.adapter.FilterAdapter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel;
import com.tokopedia.design.component.ButtonCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 28/12/18.
 */
public class FilterFragment extends BaseDaggerFragment {

    private List<FilterViewModel> filterList = new ArrayList<>();

    private RecyclerView rvFilter;
    private ButtonCompat btnApply;
    private FilterAdapter adapter;

    public static FilterFragment getInstance(Bundle bundle) {
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        rvFilter = view.findViewById(R.id.rv_filter);
        btnApply = view.findViewById(R.id.btn_apply);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null && getArguments().get(FilterActivity.PARAM_FILTER_LIST) != null) {
            filterList.addAll(getArguments().getParcelableArrayList(FilterActivity.PARAM_FILTER_LIST));
            initView();
            initViewListener();
        } else {
            if (getActivity() != null) {
                getActivity().finish();
            }
        }

    }

    private void initView() {
        adapter = new FilterAdapter(getFilterClickListener(), R.layout.item_explore_filter_detail);
        rvFilter.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvFilter.setAdapter(adapter);
        adapter.setList(filterList);
    }

    private void initViewListener() {
        btnApply.setOnClickListener(view -> {
            if (getActivity() != null) {
                Intent result = new Intent();
                Bundle bundle = new Bundle();
                ArrayList<FilterViewModel> data =
                        new ArrayList<>(adapter.getFilterListCurrentSelectedSorted());
                bundle.putParcelableArrayList(FilterActivity.PARAM_FILTER_LIST, data);
                result.putExtras(bundle);
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
            }
        });
    }

    private FilterAdapter.OnFilterClickedListener getFilterClickListener() {
        return filters -> {

        };
    }

}
