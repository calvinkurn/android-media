package com.tokopedia.navigation.presentation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.base.BaseTestableParentFragment;
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavComponent;
import com.tokopedia.navigation.presentation.di.GlobalNavModule;
import com.tokopedia.navigation.presentation.presenter.InboxPresenter;
import com.tokopedia.navigation.presentation.view.InboxView;
import com.tokopedia.navigation_common.model.NotificationsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends BaseTestableParentFragment<GlobalNavComponent, InboxPresenter> implements
        InboxView {

    public static final int CHAT_MENU = 0;
    public static final int DISCUSSION_MENU = 1;
    public static final int REVIEW_MENU = 2;
    public static final int HELP_MENU = 3;

    @Inject
    InboxPresenter presenter;

    @Inject
    GlobalNavAnalytics globalNavAnalytics;

    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter adapter;
    private View emptyLayout;

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    @Override
    public int resLayout() {
        return R.layout.fragment_inbox;
    }

    @Override
    public void initView(View view) {
        this.intiInjector();
        presenter.setView(this);

        List<Inbox> dataInbox = getData();

        adapter = new InboxAdapter(getActivity());

        emptyLayout = view.findViewById(R.id.empty_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getInboxData());

        adapter.addAll(dataInbox);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view1, position) -> {
            Inbox inbox = adapter.getItem(position);
            if (inbox == null)
                return;

            globalNavAnalytics.eventInboxPage(getString(inbox.getTitle()).toLowerCase());
            getCallingIntent(position);
        });
    }

    private void intiInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    private List<Inbox> getData() {
        List<Inbox> inboxList = new ArrayList<>();
        inboxList.add(new Inbox(R.drawable.ic_topchat, R.string.chat, R.string.chat_desc));
        inboxList.add(new Inbox(R.drawable.ic_tanyajawab, R.string.diskusi, R.string.diskusi_desc));
        inboxList.add(new Inbox(R.drawable.ic_ulasan, R.string.ulasan, R.string.ulasan_desc));
        inboxList.add(new Inbox(R.drawable.ic_pesan_bantuan, R.string.pesan_bantuan, R.string.pesan_bantuan_desc));
        return inboxList;
    }

    private void getCallingIntent(int position) {
        switch (position) {
            case CHAT_MENU:
                RouteManager.route(getActivity(), ApplinkConst.TOPCHAT_IDLESS);
                break;
            case DISCUSSION_MENU:
                if (getActivity() != null
                        && getActivity().getApplicationContext() != null) {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData("clickInboxChat",
                                        "inbox - talk",
                                        "click on diskusi product",
                                        ""));

                    if (getActivity().getApplication() instanceof GlobalNavRouter) {
                        startActivity(((GlobalNavRouter) getActivity().getApplication())
                                .getInboxTalkCallingIntent(getActivity()));
                    }
                }
                break;
            case REVIEW_MENU:
                RouteManager.route(getActivity(), ApplinkConst.REPUTATION);
                break;
            case HELP_MENU:
                if (getActivity().getApplication() instanceof GlobalNavRouter) {
                    startActivity(((GlobalNavRouter) getActivity().getApplication())
                            .getInboxTicketCallingIntent(getActivity()));
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumed()) presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void loadData() {
        setTitle(getString(R.string.inbox));
    }

    @Override
    public void onStartLoading() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onError(String message) {
        emptyLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), emptyLayout, message, () ->
                presenter.getInboxData());

    }

    @Override
    public void onHideLoading() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRenderNotifInbox(NotificationsModel entity) {
        emptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        adapter.updateValue(entity);
    }

    @Override
    protected String getScreenName() {
        return getString(R.string.inbox);
    }

    @Override
    public void reInitInjector(GlobalNavComponent component) {
        component.inject(this);
        presenter.setView(this);
    }

    @Override
    public InboxPresenter getPresenter() {
        return null;
    }

    @Override
    public void setPresenter(GlobalNavComponent presenter) {
    }
}