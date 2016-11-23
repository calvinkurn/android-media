package com.tokopedia.core.inboxreputation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.inboxreputation.InboxReputationConstant;
import com.tokopedia.core.inboxreputation.adapter.InboxReputationAdapter;
import com.tokopedia.core.inboxreputation.listener.InboxReputationFragmentView;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFragmentPresenter;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.util.RefreshHandler;

import butterknife.Bind;

/**
 * Created by Nisie on 21/01/16.
 */
public class InboxReputationFragment extends BasePresenterFragment<InboxReputationFragmentPresenter>
        implements InboxReputationFragmentView, InboxReputationConstant {

    private static final String TAG = InboxReputationFragment.class.getSimpleName();

    @Bind(R2.id.review_list)
    RecyclerView mainList;

    @Bind(R2.id.swipe_refresh_layout)
    SwipeToRefresh swipeToRefresh;


    @Bind(R2.id.fab)
    FloatingActionButton fab;

    private RefreshHandler refreshHandler;
    private LinearLayoutManager linearLayoutManager;
    private InboxReputationAdapter adapter;
    private BottomSheetDialog bottomSheetDialog;
    private SnackbarRetry snackbarRetry;

    private EditText search;
    private Button confirmButton;
    private Button cancelButton;
    private RadioButton allReview;


    public static InboxReputationFragment createInstance(String navigation) {
        InboxReputationFragment fragment = new InboxReputationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NAV, navigation);
        fragment.setArguments(bundle);
        return fragment;
    }

    public InboxReputationFragment() {
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_inbox_reputation;
    }

    @Override
    protected void initView(View view) {
        View filterLayout = getActivity().getLayoutInflater().inflate(R.layout.layout_filter_reputation, null);
        search = (EditText) filterLayout.findViewById(R.id.search);
        confirmButton = (Button) filterLayout.findViewById(R.id.button_confirm);
        cancelButton = (Button) filterLayout.findViewById(R.id.button_cancel);
        allReview = (RadioButton) filterLayout.findViewById(R.id.radio_all);
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(filterLayout);

        refreshHandler = new RefreshHandler(getActivity(), swipeToRefresh, onRefresh());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(linearLayoutManager);
        adapter = InboxReputationAdapter.createAdapter(getActivity());
        adapter.setPresenter(presenter);
        mainList.setAdapter(adapter);
    }

    @Override
    protected void setViewListener() {
        mainList.addOnScrollListener(onScrollListener());
        fab.setOnClickListener(onFabClicked());
        confirmButton.setOnClickListener(onSearchFilter());
        cancelButton.setOnClickListener(onCancelFilter());
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private View.OnClickListener onCancelFilter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
                allReview.setChecked(true);
                fab.show();
            }
        };
    }

    private View.OnClickListener onSearchFilter() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                adapter.getList().clear();
                presenter.generateSearchParam();
                presenter.getInboxReputation();

            }
        };
    }

    private View.OnClickListener onFabClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        };
    }

    @Override
    protected void initialVar() {
    }

    @Override
    protected void setActionVar() {
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initData();
    }

    @Override
    public void onSaveState(Bundle outState) {
    }

    @Override
    public void onRestoreState(Bundle savedInstanceState) {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_OPEN_DETAIL_REPUTATION
                && resultCode == Activity.RESULT_OK) {
            int isSuccess = data.getIntExtra(IS_SUCCESS, 0);
            if (isSuccess == 1) {
                switch (data.getStringExtra(PARAM_ACTION)) {
                    case ACTION_UPDATE_REPUTATION:
                        presenter.getSingleReview(data.getIntExtra(PARAM_POSITION, 0),
                                data.getStringExtra(PARAM_INVOICE_NUM));
                        break;
                }
            }
        }
    }

    @Override
    public RefreshHandler.OnRefreshHandlerListener onRefresh() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                presenter.refreshList();
            }
        };
    }

    @Override
    public RecyclerView.OnScrollListener onScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int visibleItem = linearLayoutManager.getItemCount() - 1;
                presenter.getNextPage(lastItemPosition, visibleItem);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InboxReputationFragmentPresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
    }

    @Override
    public boolean isAllReviewChecked() {
        return allReview.isChecked();
    }

    @Override
    public String getKeyword() {
        return search.getText().toString();
    }

    @Override
    public void setActionEnabled(boolean isEnabled) {
        refreshHandler.setPullEnabled(isEnabled);
        if (isEnabled) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    public boolean isRefreshing() {
        return refreshHandler.isRefreshing();
    }

    @Override
    public void showNoResult() {
        adapter.showEmpty(true);
    }

    @Override
    public InboxReputationAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            presenter.setUserVisibleHint(isVisibleToUser);

            if (snackbarRetry != null) {
                snackbarRetry.resumeRetrySnackbar();
                if (!isVisibleToUser)
                    snackbarRetry.pauseRetrySnackbar();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void clearData() {
        adapter.clearList();
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        refreshHandler.setRefreshing(isRefreshing);
    }

    @Override
    public String getFilter() {
        if (allReview.isChecked())
            return ALL;
        else return UNREAD;
    }

    @Override
    public void showSnackbar(String error, NetworkErrorHelper.RetryClickedListener listener) {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), error, listener);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void showSnackbar(NetworkErrorHelper.RetryClickedListener listener) {
        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener);
        snackbarRetry.showRetrySnackbar();
    }

    @Override
    public void showRefreshing() {
        refreshHandler.setRefreshing(true);
        refreshHandler.setIsRefreshing(true);
    }

    @Override
    public void removeError() {
        adapter.showRetry(false);
        adapter.showEmpty(false);
    }

    @Override
    public void finishLoading() {
        adapter.showLoadingFull(false);
        refreshHandler.finishRefresh();
    }

    @Override
    public void showEmptyState(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void showEmptyState(String error, NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, listener);
    }

}
