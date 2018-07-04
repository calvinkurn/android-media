package com.tokopedia.contactus.inboxticket2.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.view.adapter.TicketListAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 18/06/18.
 */

public class InboxListActivity extends InboxBaseActivity
        implements InboxListContract.InboxListView {
    @BindView(R2.id.iv_no_ticket)
    ImageView ivNoTicket;
    @BindView(R2.id.tv_no_ticket)
    TextView tvNoTicket;
    @BindView(R2.id.tv_raise_ticket)
    TextView tvRaiseTicket;
    @BindView(R2.id.rv_email_list)
    VerticalRecyclerView rvEmailList;
    @BindView(R2.id.btn_filter)
    View btnFilter;
    @BindView(R2.id.inbox_search_view)
    View searchView;
    @BindView(R2.id.custom_search)
    CustomEditText editText;
    @BindView(R2.id.close_search)
    View clearSearch;

    private TicketListAdapter mAdapter;


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
        editText.setVisibility(visibility);
        clearSearch.setVisibility(visibility);
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
        return R.layout.layout_email_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Type getType() {
        return InboxListActivity.class;
    }

    @Override
    void initView() {
        rvEmailList.addOnScrollListener(rvOnScrollListener);
        editText.setListener(((InboxListContract.InboxListPresenter) mPresenter).getSearchListener());
    }

    @Override
    int getMenuRes() {
        return -1;
    }

    @OnClick(R2.id.btn_filter)
    void onClickFilter() {
        ((InboxListContract.InboxListPresenter) mPresenter).onClickFilter();
    }

    @OnClick(R2.id.tv_raise_ticket)
    void raiseTicket() {
        Intent contactUsHome = new Intent(this, ContactUsHomeActivity.class);
        contactUsHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(contactUsHome);
        finish();
    }

    @OnClick(R2.id.close_search)
    void clearSearch() {
        editText.setText("");
    }

    @Override
    public void updateDataSet() {
        mAdapter.notifyDataSetChanged();
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
}
