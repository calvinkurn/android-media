package com.tokopedia.navigation.presentation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.navigation.GlobalNavAnalytics;
import com.tokopedia.navigation.GlobalNavRouter;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.Recomendation;
import com.tokopedia.navigation.presentation.adapter.InboxAdapter;
import com.tokopedia.navigation.presentation.adapter.InboxAdapterListener;
import com.tokopedia.navigation.presentation.adapter.InboxAdapterTypeFactory;
import com.tokopedia.navigation.presentation.adapter.RecomItemDecoration;
import com.tokopedia.navigation.presentation.adapter.RecomendationViewHolder;
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

/**
 * Created by meta on 19/06/18.
 */
public class InboxFragment extends BaseTestableParentFragment<GlobalNavComponent, InboxPresenter> implements
        InboxView, InboxAdapterListener {

    public static final int CHAT_MENU = 0;
    public static final int DISCUSSION_MENU = 1;
    public static final int REVIEW_MENU = 2;
    public static final int HELP_MENU = 3;
    public static final int DEFAULT_SPAN_COUNT = 2;
    public static final int SINGLE_SPAN_COUNT = 1;

    @Inject
    InboxPresenter presenter;

    @Inject
    GlobalNavAnalytics globalNavAnalytics;

    private SwipeRefreshLayout swipeRefreshLayout;
    private InboxAdapter adapter;
    private View emptyLayout;
    private GridLayoutManager layoutManager;

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

        List<Visitable> dataInbox = getData();
        InboxAdapterTypeFactory typeFactory = new InboxAdapterTypeFactory(this);
        adapter = new InboxAdapter(typeFactory, dataInbox);

        emptyLayout = view.findViewById(R.id.empty_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecomItemDecoration(getResources()
                .getDimensionPixelSize(R.dimen.dp_8)));
        layoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setColorSchemeResources(R.color.tkpd_main_green);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.getInboxData());

        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.getRecomData(page);
            }
        });
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RecomendationViewHolder.LAYOUT) {
                    return SINGLE_SPAN_COUNT;
                }
                return DEFAULT_SPAN_COUNT;
            }
        });
        presenter.getRecomData(0);
    }

    @Override
    public void onItemClickListener(Visitable item, int position) {
        if (item instanceof Inbox) {
            Inbox inbox = (Inbox) item;
            globalNavAnalytics.eventInboxPage(getString(inbox.getTitle()).toLowerCase());
            getCallingIntent(position);
        } else if (item instanceof Recomendation) {

        }
    }

    private void intiInjector() {
        DaggerGlobalNavComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .globalNavModule(new GlobalNavModule())
                .build()
                .inject(this);
    }

    private List<Visitable> getData() {
        List<Visitable> inboxList = new ArrayList<>();
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
                    if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
                        ((AbstractionRouter) getActivity().getApplicationContext()).getAnalyticTracker().
                                sendEventTracking("clickInboxChat",
                                        "inbox - talk",
                                        "click on diskusi product",
                                        "");
                    }

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
    public void hideLoadMoreLoading() {
        adapter.hideLoading();
    }

    @Override
    public void showLoadMoreLoading() {
        adapter.showLoading();
    }

    @Override
    public void onRenderRecomInbox(List<Recomendation> recomendationList) {
        adapter.addElement(recomendationList);
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