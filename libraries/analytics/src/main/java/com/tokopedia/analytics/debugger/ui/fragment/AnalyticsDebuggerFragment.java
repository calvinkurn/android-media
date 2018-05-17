package com.tokopedia.analytics.debugger.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.di.AnalyticsDebuggerComponent;
import com.tokopedia.analytics.debugger.di.DaggerAnalyticsDebuggerComponent;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;
import com.tokopedia.analytics.debugger.ui.activity.AnalyticsDebuggerDetailActivity;
import com.tokopedia.analytics.debugger.ui.adapter.AnalyticsDebuggerTypeFactory;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerFragment
        extends BaseSearchListFragment<Visitable, AnalyticsDebuggerTypeFactory>
        implements AnalyticsDebugger.View {

    public static final String TAG = AnalyticsDebuggerFragment.class.getSimpleName();

    @Inject
    AnalyticsDebugger.Presenter presenter;

    public static Fragment newInstance() {
        return new AnalyticsDebuggerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected boolean isLoadMoreEnabledByDefault() {
        return true;
    }

    @Override
    protected void loadInitialData() {
        showLoading();
        presenter.reloadData();
    }

    @Nullable
    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return view.findViewById(com.tokopedia.abstraction.R.id.swipe_refresh_layout);
    }

    @Override
    public void loadData(int page) {
        presenter.loadMore();
    }

    @Override
    protected AnalyticsDebuggerTypeFactory getAdapterTypeFactory() {
        return new AnalyticsDebuggerTypeFactory();
    }

    @Override
    public void onItemClicked(Visitable visitable) {
        if(visitable instanceof AnalyticsDebuggerViewModel) {
            openDetail((AnalyticsDebuggerViewModel) visitable);
        }
    }

    @Override
    protected void initInjector() {
        AnalyticsDebuggerComponent component = DaggerAnalyticsDebuggerComponent
                .builder()
                .baseAppComponent(
                    ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent()
                ).build();

        component.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return AnalyticsDebuggerFragment.class.getSimpleName();
    }

    @Override
    public void onSearchSubmitted(String text) {
        presenter.search(text);
    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onLoadMoreCompleted(List<Visitable> visitables) {
        renderList(visitables, true);
    }

    @Override
    public void onReloadCompleted(List<Visitable> visitables) {
        isLoadingInitialData = true;
        renderList(visitables, true);
    }

    @Override
    public void onSwipeRefresh() {
        searchInputView.getSearchTextView().setText("");
        super.onSwipeRefresh();
    }

    @Override
    public void onDeleteCompleted() {
        presenter.reloadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_analytics_debugger, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_menu_delete) {
            presenter.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDetail(AnalyticsDebuggerViewModel viewModel) {
        startActivity(AnalyticsDebuggerDetailActivity.newInstance(getContext(), viewModel));
    }
}
