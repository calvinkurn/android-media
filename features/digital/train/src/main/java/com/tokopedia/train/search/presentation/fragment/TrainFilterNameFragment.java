package com.tokopedia.train.search.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.adapter.TrainFilterAdapter;
import com.tokopedia.train.search.presentation.contract.FilterSearchActionView;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/23/18.
 */

public class TrainFilterNameFragment extends BaseDaggerFragment {

    public static final String TAG = TrainFilterNameFragment.class.getSimpleName();

    private VerticalRecyclerView recyclerView;
    private TrainFilterAdapter adapter;
    private FilterSearchActionView listener;
    private FilterSearchData filterSearchData;

    public static TrainFilterNameFragment newInstance() {
        TrainFilterNameFragment fragment = new TrainFilterNameFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_filter_item_search, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterSearchData = listener.getFilterSearchData();

        listener.setTitleToolbar("Kereta");
        listener.setCloseButton(false);
        adapter = new TrainFilterAdapter();
        adapter.addList(filterSearchData.getTrains(), filterSearchData.getSelectedTrains());
        adapter.setListener(new TrainFilterAdapter.ActionListener() {
            @Override
            public void onCheckChanged(List<String> listObjectFilter) {
                filterSearchData.setSelectedTrains(listObjectFilter);
                listener.onChangeFilterSearchData(filterSearchData);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (FilterSearchActionView) context;
    }
}
