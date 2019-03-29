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
import com.tokopedia.train.search.presentation.adapter.TrainFilterAdapter;
import com.tokopedia.train.search.presentation.contract.BaseTrainFilterListener;
import com.tokopedia.train.search.presentation.contract.FilterSearchActionView;
import com.tokopedia.train.search.presentation.model.FilterSearchData;

/**
 * Created by nabillasabbaha on 04/06/18.
 */
public abstract class BaseFilterTrainFragment extends BaseDaggerFragment implements BaseTrainFilterListener {

    public static final String TAG = FilterTrainDepartureFragment.class.getSimpleName();
    private static final String SAVE_FILTER_EXISTING = "filter_existing";
    private static final String FILTER_SEARCH_DATA = "filter_search_data";

    private VerticalRecyclerView recyclerView;
    protected TrainFilterAdapter adapter;
    protected FilterSearchActionView listener;
    protected FilterSearchData filterSearchData;
    private FilterSearchData existingFilterSearchData;

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
        if (savedInstanceState == null) {
            existingFilterSearchData = listener.getFilterSearchData().copy();
            filterSearchData = listener.getFilterSearchData();
        } else {
            existingFilterSearchData = savedInstanceState.getParcelable(SAVE_FILTER_EXISTING);
            filterSearchData = savedInstanceState.getParcelable(FILTER_SEARCH_DATA);
        }

        listener.setCloseButton(false);
        adapter = new TrainFilterAdapter();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_FILTER_EXISTING, existingFilterSearchData);
        outState.putParcelable(FILTER_SEARCH_DATA, filterSearchData);
    }

    @Override
    public void changeFilterToOriginal() {
        listener.onChangeFilterSearchData(existingFilterSearchData);
    }
}