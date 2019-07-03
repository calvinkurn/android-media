package com.tokopedia.contactus.inboxticket2.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.view.adapter.TicketListAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

import java.util.List;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;


public class InboxListActivity extends InboxBaseActivity
        implements InboxListContract.InboxListView, View.OnClickListener {

    private ImageView ivNoTicket;
    private TextView tvNoTicket;
    private TextView tvRaiseTicket;
    private VerticalRecyclerView rvEmailList;
    private View btnFilter;
    private View searchView;
    private CustomEditText editText;
    private View clearSearch;
    private TicketListAdapter mAdapter;

    private TextView btnFilterTv;

    @DeepLink(ApplinkConst.INBOX_TICKET)
    public static TaskStackBuilder getCallingTaskStackList(Context context, Bundle extras) {
        Intent homeIntent = ((ContactUsModuleRouter) context.getApplicationContext()).getHomeIntent
                (context);
        Intent parentIntent = InboxListActivity.getCallingIntent(context);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(homeIntent);
        taskStackBuilder.addNextIntent(parentIntent);
        return taskStackBuilder;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, InboxListActivity.class);
    }


    @Override
    public void renderTicketList(List<TicketsItem> ticketList) {
        if (mAdapter == null) {
            mAdapter = new TicketListAdapter(this, ticketList, (InboxListContract.InboxListPresenter) mPresenter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        rvEmailList.setAdapter(mAdapter);
        rvEmailList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFilter() {
        btnFilter.setVisibility(View.GONE);
    }

    @Override
    public void showFilter() {
        btnFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void toggleSearch(int visibility) {
        searchView.setVisibility(visibility);
    }

    @Override
    public void toggleEmptyLayout(int visibility) {
        ivNoTicket.setVisibility(visibility);
        tvNoTicket.setVisibility(visibility);
        tvRaiseTicket.setVisibility(visibility);
        rvEmailList.setVisibility(View.GONE);
    }

    @Override
    public void removeFooter() {
        mAdapter.removeFooter();
    }

    @Override
    public void addFooter() {
        mAdapter.addFooter();
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return (LinearLayoutManager) rvEmailList.getLayoutManager();
    }

    @Override
    public void scrollRv() {
        rvEmailList.scrollBy(0, 0);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_ticket_list_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public InboxBaseContract.InboxBasePresenter getPresenter() {
        return component.getTicketListPresenter();
    }

    @Override
    void initView() {
        findingViewsId();
        settingOnClickListener();
        btnFilterTv.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (this, R.drawable.ic_filter_list), null, null , null);
        rvEmailList.addOnScrollListener(rvOnScrollListener);
        editText.setListener(((InboxListContract.InboxListPresenter) mPresenter).getSearchListener());
    }

    void findingViewsId(){
        ivNoTicket = findViewById(R.id.iv_no_ticket);
        tvNoTicket = findViewById(R.id.tv_no_ticket);
        tvRaiseTicket = findViewById(R.id.tv_raise_ticket);
        rvEmailList = findViewById(R.id.rv_email_list);
        btnFilter = findViewById(R.id.btn_filter);
        searchView = findViewById(R.id.inbox_search_view);
        editText = findViewById(R.id.custom_search);
        clearSearch = findViewById(R.id.close_search);
        btnFilterTv = findViewById(R.id.btn_filter_tv);
    }

    private void settingOnClickListener() {
        tvRaiseTicket.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        clearSearch.setOnClickListener(this);
    }

    @Override
    int getMenuRes() {
        return -1;
    }

    @Override
    int getBottomSheetLayoutRes() {
        return R.layout.layout_bottom_sheet_fragment;
    }

    @Override
    boolean doNeedReattach() {
        return true;
    }

    void onClickFilter(View v) {
        if (v.getId() == R.id.btn_filter) {
            ((InboxListContract.InboxListPresenter) mPresenter).onClickFilter();
        } else if (v.getId() == R.id.close_search) {
            mPresenter.clickCloseSearch();
        }
    }

    void raiseTicket() {
        Intent contactUsHome = new Intent(this, ContactUsHomeActivity.class);
        contactUsHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(contactUsHome);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickHubungi,
                InboxTicketTracking.Label.InboxEmpty);
        finish();
    }

    @Override
    public boolean isSearchMode() {
        return editText.getText().length() <= 0;
    }


    @Override
    public void updateDataSet() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearSearch() {
        editText.setText("");
    }

    private RecyclerView.OnScrollListener rvOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            ((InboxListContract.InboxListPresenter) mPresenter).onRecyclerViewScrolled(getLayoutManager());
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.btn_filter||id==R.id.close_search){
            onClickFilter(view);
        }else if(id==R.id.tv_raise_ticket){
            raiseTicket();
        }
    }
}
