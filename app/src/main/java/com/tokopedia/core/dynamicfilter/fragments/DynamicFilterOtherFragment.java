package com.tokopedia.core.dynamicfilter.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.dynamicfilter.DynamicFilterActivity;
import com.tokopedia.core.dynamicfilter.adapter.DynamicFilterOtherAdapter;
import com.tokopedia.core.dynamicfilter.model.DynamicFilterModel;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterOtherPresenter;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterOtherPresenterImpl;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterOtherView;
import com.tokopedia.core.dynamicfilter.presenter.DynamicFilterView;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.widgets.DividerItemDecoration;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by noiz354 on 7/12/16.
 */
public class DynamicFilterOtherFragment extends BaseFragment<DynamicFilterOtherPresenter> implements DynamicFilterOtherView {

    public static Fragment newInstance(DynamicFilterModel.Filter filter){
        DynamicFilterOtherFragment dynamicFilterOtherFragment = new DynamicFilterOtherFragment();
        Bundle argument = new Bundle();
        argument.putParcelable(DynamicFilterOtherPresenter.FILTER_DATA, Parcels.wrap(filter));
        dynamicFilterOtherFragment.setArguments(argument);
        return  dynamicFilterOtherFragment;
    }

    @Bind(R2.id.dynamic_filter_other_search_container)
    LinearLayout dynamicFilterOtherSearchContainer;

    @Bind(R2.id.dynamic_filter_other_search)
    EditText dynamicFilterOtherSearch;

    @Bind(R2.id.dynamic_filter_other_recyclerview)
    RecyclerView dynamicFilterOtherRecyclerView;

    @Bind(R2.id.dynamic_filter_other_finish)
    Button dynamicFilterOtherFinish;

    DynamicFilterOtherAdapter dynamicFilterOtherAdapter;
    LinearLayoutManager linearLayoutManager;

    private BroadcastReceiver resetFilterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            dynamicFilterOtherAdapter.reset();
        }
    };

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(resetFilterReceiver, new IntentFilter(DynamicFilterActivity.ACTION_RESET_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(resetFilterReceiver);
    }

    @Override
    protected void initPresenter() {
        presenter = new DynamicFilterOtherPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dynamic_filter_other_layout;
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
    public void toggleSearch(boolean status, String hint) {
        if(status){
            dynamicFilterOtherSearch.setHint(hint);
            dynamicFilterOtherSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        CommonUtils.hideKeyboard(getActivity(), dynamicFilterOtherSearch);
                    }
                }
            });
            dynamicFilterOtherSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // search something here
//                    dynamicFilterOtherAdapter.getFilter().filter(s);
                    presenter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            // set listener here
        }else{
            dynamicFilterOtherSearchContainer.setVisibility(View.GONE);// hide the search view
        }
    }

    @Override
    public void setIsLoading(boolean loading) {
        dynamicFilterOtherAdapter.setIsLoading(loading);
    }

    @Override
    public boolean isLoading() {
        return dynamicFilterOtherAdapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition())== TkpdState.RecyclerView.VIEW_LOADING;
    }

    @Override
    public DynamicFilterOtherAdapter getAdapter() {
        return dynamicFilterOtherAdapter;
    }

    @Override
    public void setupAdapter(List<RecyclerViewItem> data) {
        Log.d(TAG, "Data Size "+data.size());
        dynamicFilterOtherAdapter = new DynamicFilterOtherAdapter(getActivity(), data);
    }

    @Override
    public void setupRecylerView() {
        // Set the RecyclerView's adapter to the ExpandableAdapter we just created
        linearLayoutManager = new LinearLayoutManager(getActivity());
        dynamicFilterOtherRecyclerView.setAdapter(dynamicFilterOtherAdapter);
        // Set the layout manager to a LinearLayout manager for vertical list
        dynamicFilterOtherRecyclerView.setHasFixedSize(true);
        dynamicFilterOtherRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        dynamicFilterOtherRecyclerView.setLayoutManager(linearLayoutManager);
        dynamicFilterOtherRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading() && linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1) {
                    presenter.loadMore(getActivity());
                }
            }
        });
    }

    @Override
    public void addListItem(List<RecyclerViewItem> items) {
        dynamicFilterOtherAdapter.addAll(false, items);
        dynamicFilterOtherAdapter.notifyItemInserted(dynamicFilterOtherAdapter.getItemCount());
    }

    @Override
    public void setListItem(List<RecyclerViewItem> items) {
        dynamicFilterOtherAdapter.addAll(true, true, items);
    }

    @OnClick(R2.id.dynamic_filter_other_finish)
    public void finishThis(){
        if(getActivity() != null && getActivity() instanceof DynamicFilterView){
            ((DynamicFilterView) getActivity()).finishThis();
        }
    }
}
