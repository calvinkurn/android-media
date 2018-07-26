package com.tokopedia.navigation.presentation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.base.ParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.InboxPresenter;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.searchbar.NotificationToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends ParentFragment implements InboxView {

    public static final int CHAT_MENU = 0;
    public static final int DISCUSSION_MENU = 1;
    public static final int REVIEW_MENU = 2;
    public static final int HELP_MENU = 3;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    private NotificationToolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject InboxPresenter presenter;
    private InboxAdapter adapter;

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
        this.intiInjector();
        presenter.setView(this);

        adapter = new InboxAdapter();

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getInboxData());

        adapter.addAll(getData());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view1, position) -> {
            getCallingIntent(position);
        });
    }

    private List<Inbox> getData() {
        List<Inbox> inboxList = new ArrayList<>();
        inboxList.add(new Inbox(R.drawable.ic_topchat, R.string.chat, R.string.chat_desc));
        inboxList.add(new Inbox(R.drawable.ic_tanyajawab, R.string.diskusi, R.string.diskusi_desc));
        inboxList.add(new Inbox(R.drawable.ic_ulasan, R.string.ulasan, R.string.ulasan_desc));
        inboxList.add(new Inbox(R.drawable.ic_pesan_bantuan, R.string.pesan_bantuan, R.string.pesan_bantuan_desc));
        return inboxList;
    }

    private void intiInjector() {
        DaggerGlobalNavComponent.builder()
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    private void getCallingIntent(int position) {
        switch (position) {
            case CHAT_MENU:
                RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_IDLESS);
                break;
            case DISCUSSION_MENU:
                RouteManager.route(getActivity(), ApplinkConst.TALK);
                break;
            case REVIEW_MENU:
                RouteManager.route(getActivity(), ApplinkConst.REPUTATION);
                break;
            case HELP_MENU:
                RouteManager.route(getActivity(), ApplinkConst.INBOX_TICKET);
                break;
        }
    }

    @Override
    public void setupToolbar(View view) {
        try {
            this.toolbar = view.findViewById(R.id.toolbar);
            ((MainParentActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        } catch (Exception ignored) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void loadData() {
        if (toolbar != null)
            toolbar.setTitle(getString(R.string.inbox));
    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void onStartLoading() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onError(String message) { }

    @Override
    public void onHideLoading() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRenderNotifINbox(NotificationEntity.Notification entity) {
        adapter.updateValue(entity);
    }
}
