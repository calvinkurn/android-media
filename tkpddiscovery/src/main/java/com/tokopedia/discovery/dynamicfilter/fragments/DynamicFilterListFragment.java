package com.tokopedia.discovery.dynamicfilter.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.discovery.dynamicfilter.adapter.DynamicFilterListAdapter;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterList;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterListImpl;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterListView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noiz354 on 7/11/16.
 */
public class DynamicFilterListFragment extends BaseFragment<DynamicFilterList> implements DynamicFilterListView {
    public static Fragment newInstance(List<String> title) {
        Bundle argument = new Bundle();
        argument.putParcelable(DynamicFilterList.TITLE_LIST, Parcels.wrap(title));

        return bundleWithFragment(argument);
    }

    public static Fragment newInstance2(List<DynamicFilterModel.Filter> data) {
        Bundle argument = new Bundle();
        argument.putParcelable(DynamicFilterList.DATA_LIST, Parcels.wrap(data));

        return bundleWithFragment(argument);
    }

    private static DynamicFilterListFragment bundleWithFragment(Bundle bundle) {
        DynamicFilterListFragment dynamicFilterFirstTimeFragment = new DynamicFilterListFragment();
        dynamicFilterFirstTimeFragment.setArguments(bundle);

        return dynamicFilterFirstTimeFragment;
    }

    @BindView(R2.id.dynamic_filter_list_recyclerview)
    RecyclerView dynamicFilterList;

    @BindView(R2.id.dynamic_filter_list_reset)
    Button dynamicFilterListReset;

    DynamicFilterListAdapter dynamicFilterListAdapter;
    LinearLayoutManager linearLayoutManager;

    private BroadcastReceiver filterBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String key = bundle.getString(DynamicFilterActivity.EXTRA_FILTER_KEY);
            boolean active = bundle.getBoolean(DynamicFilterActivity.EXTRA_FILTER_VALUE);
            dynamicFilterListAdapter.setActiveIndicator(key, active);
        }
    };
    private BroadcastReceiver resetFilterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dynamicFilterListAdapter.reset();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(filterBroadcastReceiver, new IntentFilter(DynamicFilterActivity.ACTION_SELECT_FILTER));
        getActivity().registerReceiver(resetFilterReceiver, new IntentFilter(DynamicFilterActivity.ACTION_RESET_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(filterBroadcastReceiver);
        getActivity().unregisterReceiver(resetFilterReceiver);
    }

    @Override
    protected void initPresenter() {
        presenter = new DynamicFilterListImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_filter_list_layout;
    }

    @Override
    public int getFragmentId() {
        return FRAGMENT_ID;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {

    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void setupRecyclerView() {
        dynamicFilterList.setLayoutManager(linearLayoutManager);
        dynamicFilterList.setAdapter(dynamicFilterListAdapter);
    }

    @Override
    public void setupAdapter(List<DynamicFilterModel.Filter> dataList) {
        dynamicFilterListAdapter = new DynamicFilterListAdapter(getActivity(), new ArrayList<RecyclerViewItem>(DynamicFilterListAdapter.convertTo2(dataList)));
        dynamicFilterListAdapter.activatePosition(0);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R2.id.dynamic_filter_list_reset)
    public void onResetClick() {
        ((DynamicFilterActivity) getActivity()).resetSelectedFilter();
        getActivity().sendBroadcast(new Intent(DynamicFilterActivity.ACTION_RESET_FILTER));
    }


}
