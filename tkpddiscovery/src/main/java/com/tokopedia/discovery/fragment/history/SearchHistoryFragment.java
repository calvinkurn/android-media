package com.tokopedia.discovery.fragment.history;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.presenter.history.SearchHistory;
import com.tokopedia.discovery.presenter.history.SearchHistoryImpl;
import com.tokopedia.discovery.view.history.SearchHistoryView;

import butterknife.BindView;

/**
 * Created by Erry on 6/30/2016.
 */
public class SearchHistoryFragment extends BaseFragment<SearchHistory> implements SearchHistoryView {
    public static final String FRAGMENT_TAG = "SearchHistoryFragment";
    public static final String INIT_QUERY = "INIT_QUERY";

    public static SearchHistoryFragment newInstance() {
        SearchHistoryFragment instance = new SearchHistoryFragment();
        Bundle args = new Bundle();
        instance.setArguments(args);
        return instance;
    }

    public static SearchHistoryFragment newInstance(String query) {
        SearchHistoryFragment instance = new SearchHistoryFragment();
        Bundle args = new Bundle();
        args.putString(INIT_QUERY, query);
        instance.setArguments(args);
        return instance;
    }

    @BindView(R2.id.list)
    RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;

    @Override
    public String getScreenName() {
        return null;
    }

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
        ((BrowseProductActivity) getActivity()).sendQuery(query);
    }

    @Override
    public void clearSearchQuery() {
        ((BrowseProductActivity) getActivity()).clearQuery();
    }

    @Override
    public void sendHotlistResult(String selected, String keyword) {
        ((BrowseProductActivity) getActivity()).sendHotlist(selected, keyword);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecylerView();
    }
}
