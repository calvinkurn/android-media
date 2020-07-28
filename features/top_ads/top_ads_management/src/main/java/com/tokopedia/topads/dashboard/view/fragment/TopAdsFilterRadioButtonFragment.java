package com.tokopedia.topads.dashboard.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.topads.R;
import com.tokopedia.topads.common.view.fragment.BaseFilterContentFragment;
import com.tokopedia.topads.common.view.fragment.TopAdsFilterListFragment;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsBasicRadioButtonAdapter;
import com.tokopedia.topads.dashboard.view.model.RadioButtonItem;
import com.tokopedia.seller.common.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by Nathaniel on 1/31/2017.
 */
public abstract class TopAdsFilterRadioButtonFragment<P> extends
        BaseFilterContentFragment<P> implements TopAdsBasicRadioButtonAdapter.Callback {

    protected TopAdsBasicRadioButtonAdapter adapter;
    protected int selectedAdapterPosition;
    private RecyclerView recyclerView;

    protected abstract List<RadioButtonItem> getRadioButtonList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_group_name;
    }

    public void initialPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TopAdsBasicRadioButtonAdapter();
        initialPresenter();
    }

    protected void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        recyclerView.setAdapter(adapter);
        setAdapterData(getRadioButtonList());
    }

    public void setAdapterData(List<RadioButtonItem> radioButtonItems) {
        adapter.setData(radioButtonItems);
        adapter.notifyDataSetChanged();
        if (selectedAdapterPosition > -1) { // data might come from api
            adapter.setSelectedPosition(selectedAdapterPosition);
        }
        adapter.setCallback(this);
    }

    public String getSelectedRadioValue() {
        if (adapter.isEmpty()) {
            return String.valueOf(0);
        }
        return adapter.getSelectedItem().getValue();
    }

    @Override
    public void onItemSelected(RadioButtonItem radioButtonItem, int position) {
        selectedAdapterPosition = position;
        if (callback != null ) {
            callback.onStatusChanged(selectedAdapterPosition > 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION, selectedAdapterPosition);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            selectedAdapterPosition = savedInstanceState.getInt(TopAdsFilterListFragment.EXTRA_ITEM_SELECTED_POSITION);
        }
    }
}