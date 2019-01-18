package com.tokopedia.affiliate.feature.explore.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.explore.view.activity.SortActivity;
import com.tokopedia.affiliate.feature.explore.view.adapter.SortAdapter;
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 16/01/19.
 */
public class SortFragment extends BaseDaggerFragment {

    private RecyclerView rvSort;
    private SortAdapter adapter;
    private List<SortViewModel> sortList = new ArrayList<>();
    private SortViewModel selectedSort;

    public static SortFragment getInstance(Bundle bundle) {
        SortFragment fragment = new SortFragment();
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sort, container, false);
        rvSort = view.findViewById(R.id.rv_sort);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null
                && getArguments().get(SortActivity.PARAM_SORT_LIST) != null
                && getArguments().get(SortActivity.PARAM_SORT_SELECTED) != null) {
            sortList.addAll(getArguments().getParcelableArrayList(SortActivity.PARAM_SORT_LIST));
            selectedSort = getArguments().getParcelable(SortActivity.PARAM_SORT_SELECTED);
            initView();
            initViewListener();
        } else
            getActivity().finish();
    }

    private void initView() {
        adapter = new SortAdapter(getSortItemClickListener(), sortList, selectedSort);
        rvSort.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSort.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initViewListener() {

    }

    private SortAdapter.OnSortItemClicked getSortItemClickListener() {
        return sort -> {
            Intent result = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(SortActivity.PARAM_SORT_SELECTED, sort);
            result.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
        };
    }
}
