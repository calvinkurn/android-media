package com.tokopedia.groupchat.channel.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.groupchat.GroupChatModuleRouter;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.channel.data.analytics.ChannelAnalytics;
import com.tokopedia.groupchat.channel.di.DaggerChannelComponent;
import com.tokopedia.groupchat.channel.view.activity.ChannelActivity;
import com.tokopedia.groupchat.channel.view.adapter.typefactory.ChannelTypeFactory;
import com.tokopedia.groupchat.channel.view.listener.ChannelContract;
import com.tokopedia.groupchat.channel.view.model.ChannelListViewModel;
import com.tokopedia.groupchat.channel.view.model.ChannelViewModel;
import com.tokopedia.groupchat.channel.view.presenter.ChannelPresenter;
import com.tokopedia.groupchat.chatroom.view.activity.GroupChatActivity;
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics;
import com.tokopedia.groupchat.common.di.component.DaggerGroupChatComponent;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;
import com.tokopedia.groupchat.room.view.activity.PlayActivity;

import javax.inject.Inject;

/**
 * @author by nisie on 2/1/18.
 */


public class ChannelFragment extends BaseListFragment<ChannelViewModel, ChannelTypeFactory> implements ChannelContract.View {

    private static final int DEFAULT_INITIAL_PAGE = 1;

    private static final int REQUEST_OPEN_GROUPCHAT = 111;
    private static final int REQUEST_LOGIN = 101;
    private static final int DEFAULT_NO_POSITION = -1;

    @Inject
    ChannelPresenter presenter;

    @Inject
    GroupChatAnalytics analytics;

    SwipeRefreshLayout swipeRefreshLayout;
    private String lastCursor;

    public static Fragment createInstance() {
        return new ChannelFragment();
    }

    @Override
    protected String getScreenName() {
        return ChannelAnalytics.Screen.CHANNEL_LIST;
    }

    @Override
    public void onStart() {
        super.onStart();
        analytics.sendScreen(getActivity(), getScreenName());
    }

    @Override
    protected void initInjector() {
        GroupChatComponent streamComponent = DaggerGroupChatComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent()).build();

        DaggerChannelComponent.builder()
                .groupChatComponent(streamComponent)
                .build().inject(this);

        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        return view;
    }


    @Override
    public RecyclerView getRecyclerView(View view) {
        RecyclerView recyclerView = super.getRecyclerView(view);
        recyclerView.addItemDecoration(new ItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_med)));
        return recyclerView;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected boolean callInitialLoadAutomatically() {
        return true;
    }

    @Override
    protected void loadInitialData() {
        presenter.getChannelListFirstTime();
    }

    @Override
    public void loadData(int page) {
        if (page == DEFAULT_INITIAL_PAGE) {
            presenter.getChannelListFirstTime();
        } else {
            presenter.getChannelList(lastCursor);
        }
    }

    @Override
    protected ChannelTypeFactory getAdapterTypeFactory() {
        return new ChannelTypeFactory();
    }

    @NonNull
    @Override
    protected BaseListAdapter<ChannelViewModel, ChannelTypeFactory> createAdapterInstance() {
        BaseListAdapter<ChannelViewModel, ChannelTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onFailedGetChannelFirstTime(String errorMessage) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getChannelListFirstTime();
                    }
                });
    }

    @Override
    public void onSuccessGetChannelFirstTime(ChannelListViewModel channelListViewModel) {
        this.lastCursor = channelListViewModel.getCursor();
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void onSuccessGetChannel(ChannelListViewModel channelListViewModel) {
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void onFailedGetChannel(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void showLoadingFull() {
        getAdapter().showLoading();
    }

    @Override
    public void dismissLoadingFull() {
        getAdapter().hideLoading();
    }

    @Override
    public void onErrorRefreshChannel(String errorMessage) {
        swipeRefreshLayout.setRefreshing(false);
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onSuccessRefreshChannel(ChannelListViewModel channelListViewModel) {
        swipeRefreshLayout.setRefreshing(false);
        getAdapter().clearAllElements();
        renderList(channelListViewModel.getChannelViewModelList(), channelListViewModel.isHasNextPage());
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }


    @Override
    public void onItemClicked(ChannelViewModel channelViewModel) {
        goToChannel(channelViewModel, getAdapter().getData().indexOf(channelViewModel));
    }

    private void goToChannel(ChannelViewModel channelViewModel, int position) {
        analytics.eventClickGroupChatList(channelViewModel.getChannelUrl());
        startActivityForResult(PlayActivity.getCallingIntent(getActivity(),
                    channelViewModel, position),
                REQUEST_OPEN_GROUPCHAT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_GROUPCHAT
                && resultCode == ChannelActivity.RESULT_ERROR_ENTER_CHANNEL
                && data != null
                && data.getExtras() != null) {
            String errorMessage = data.getExtras().getString(ChannelActivity.RESULT_MESSAGE, "");
            if (!TextUtils.isEmpty(errorMessage)) {
                NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMessage);
            } else {
                presenter.refreshData();
            }
            updateTotalView(data.getExtras().getInt(GroupChatActivity.EXTRA_POSITION, -1),
                    data.getExtras().getString(GroupChatActivity.TOTAL_VIEW, ""));
        } else if (requestCode == REQUEST_OPEN_GROUPCHAT
                && data != null
                && data.getExtras() != null) {
            updateTotalView(data.getExtras().getInt(GroupChatActivity.EXTRA_POSITION, -1),
                    data.getExtras().getString(GroupChatActivity.TOTAL_VIEW, ""));
        } else if (requestCode == REQUEST_LOGIN
                && resultCode == Activity.RESULT_CANCELED) {
            Intent intent = ((GroupChatModuleRouter) getActivity().getApplicationContext())
                    .getHomeIntent(getActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        } else if (requestCode == REQUEST_LOGIN
                && resultCode == Activity.RESULT_OK) {
            NetworkErrorHelper.removeEmptyState(getView());
            loadInitialData();
        }
    }

    private void updateTotalView(int position, String totalView) {
        if (position != DEFAULT_NO_POSITION
                && position < getAdapter().getData().size()
                && !TextUtils.isEmpty(totalView)) {
            getAdapter().getData().get(position).setTotalView(totalView);
            getAdapter().notifyItemChanged(position);
        }
    }

    @Override
    public void onSwipeRefresh() {
        presenter.refreshData();
    }

    public class ItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        ItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
                outRect.bottom = space / 2;
            } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = space / 2;
            } else {
                outRect.top = space / 2;
                outRect.bottom = space / 2;
            }
        }
    }
}
