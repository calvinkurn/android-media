package com.tokopedia.tkpd.discovery.fragment.history;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.discovery.activity.BrowseProductActivity;
import com.tokopedia.tkpd.discovery.presenter.history.SearchHistory;
import com.tokopedia.tkpd.discovery.presenter.history.SearchHistoryImpl;
import com.tokopedia.tkpd.discovery.view.history.SearchHistoryView;
import com.tokopedia.tkpd.session.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Erry on 6/30/2016.
 */
public class SearchHistoryFragment extends BaseFragment<SearchHistory> implements SearchHistoryView {
    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";

    public static SearchHistoryFragment newInstance(){
        SearchHistoryFragment instance = new SearchHistoryFragment();
        Bundle args = new Bundle();
        instance.setArguments(args);
        return instance;
    }

    public static SearchHistoryFragment newInstance(String query){
        SearchHistoryFragment instance = new SearchHistoryFragment();
        Bundle args = new Bundle();
        args.putString(INIT_QUERY, query);
        instance.setArguments(args);
        return instance;
    }

    @Bind(R.id.list)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;


    @Override
    protected void initPresenter() {
        presenter = new SearchHistoryImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_history;
    }

    @Override
    public int getFragmentId() {
        return 0;
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
    public void onPause() {
        super.onPause();
        presenter.unregisterBroadcast(getActivity());
    }

    @Override
    public void initRecylerView() {
        // set linear layout manager
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        // set adapter (stored in presenter)
        recyclerView.setAdapter(presenter.getAdapter());
    }

    @Override
    public View onCreateView(View parentView, Bundle savedInstanceState) {
        initRecylerView();
        return super.onCreateView(parentView, savedInstanceState);
    }

    @Override
    public void sendSearchResult(String query) {
        ((BrowseProductActivity)getActivity()).sendQuery(query);
    }

    @Override
    public void clearSearchQuery() {
        ((BrowseProductActivity)getActivity()).clearQuery();
    }

    @Override
    public void sendHotlistResult(String selected) {
        ((BrowseProductActivity)getActivity()).sendHotlist(selected);
    }

}
