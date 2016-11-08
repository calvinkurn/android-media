package com.tokopedia.core.inboxreputation.adapter;

import android.content.Context;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.HeaderReputationDataBinder;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.NoReviewDataBinder;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.ReputationDataBinder;
import com.tokopedia.core.inboxreputation.adapter.viewbinder.SkippedReputationDataBinder;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationDetailFragmentPresenter;
import com.tokopedia.core.util.DataBinder;

import java.util.ArrayList;

/**
 * Created by Nisie on 1/26/16.
 */
public class InboxReputationDetailAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int VIEW_HEADER = 100;
    private static final int VIEW_REPUTATION_DETAIL = 101;
    private static final int VIEW_SKIPPED = 102;
    private static final int VIEW_EMPTY = 103;

    public static final int BUYER = 1;
    public static final int SELLER = 2;

    private static final int HEADER = 1;

    private HeaderReputationDataBinder headerView;
    private ReputationDataBinder reputationView;
    private SkippedReputationDataBinder skippedView;
    private NoReviewDataBinder noReviewView;

    private InboxReputationDetail inboxReputationDetail;
    private InboxReputationItem inboxReputation;

    public InboxReputationDetailAdapter(Context context,
                                        InboxReputationDetailFragmentPresenter presenter) {
        super();
        inboxReputationDetail = new InboxReputationDetail();
        inboxReputationDetail.setInboxReputationDetailItem(new ArrayList<InboxReputationDetailItem>());
        headerView = new HeaderReputationDataBinder(this);
        headerView.setContext(context);
        headerView.setPresenter(presenter);
        reputationView = new ReputationDataBinder(this);
        reputationView.setContext(context);
        reputationView.setPresenter(presenter);
        skippedView = new SkippedReputationDataBinder(this);
        skippedView.setContext(context);
        noReviewView = new NoReviewDataBinder(this);
        noReviewView.setPresenter(presenter);
        noReviewView.setContext(context);
    }

    public static InboxReputationDetailAdapter createAdapter(Context context,
                                                             InboxReputationDetailFragmentPresenter presenter) {
        return new InboxReputationDetailAdapter(context, presenter);
    }

    @Override
    public int getItemCount() {
        return headerView.getItemCount()
                + inboxReputationDetail.getInboxReputationDetailItemList().size()
                + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else if (isLastItemPosition(position)
                && (reputationView.getItemCount() == 0 || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else if (inboxReputationDetail.getInboxReputationDetailItemList().get(position - HEADER)
                .getIsSkipped()) {
            return VIEW_SKIPPED;
        } else if (inboxReputationDetail.getInboxReputationDetailItemList().get(position - HEADER)
                .getReviewMessage().toString().equals("0")) {
            return VIEW_EMPTY;
        } else {
            return VIEW_REPUTATION_DETAIL;
        }
    }

    @Override
    public DataBinder getDataBinder(int viewType) {
        switch (viewType) {
            case VIEW_HEADER:
                return headerView;
            case VIEW_REPUTATION_DETAIL:
                return reputationView;
            case VIEW_SKIPPED:
                return skippedView;
            case VIEW_EMPTY:
                return noReviewView;
            default:
                return super.getDataBinder(viewType);
        }
    }

    @Override
    public int getBinderPosition(int position) {
        switch (getItemViewType(position)) {
            case VIEW_HEADER:
                return 0;
            case VIEW_REPUTATION_DETAIL:
                return position - HEADER;
            case VIEW_SKIPPED:
                return position - HEADER;
            case VIEW_EMPTY:
                return position - HEADER;
            default:
                return super.getBinderPosition(position);
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == HEADER + inboxReputationDetail.getInboxReputationDetailItemList().size();
    }

    public void setInboxReputation(InboxReputationItem inboxReputation) {
        this.inboxReputation = inboxReputation;
        this.headerView.setData(inboxReputation);
        this.reputationView.setInboxReputation(inboxReputation);
        this.skippedView.setInboxReputation(inboxReputation);
        this.noReviewView.setInboxReputation(inboxReputation);
        notifyDataSetChanged();

    }

    public InboxReputationItem getInboxReputation() {
        return inboxReputation;
    }

    public java.util.List<InboxReputationDetailItem> getList() {
        return inboxReputationDetail.getInboxReputationDetailItemList();
    }


    public void addAll(java.util.List<InboxReputationDetailItem> inboxReputationDetailItem) {
        this.inboxReputationDetail.getInboxReputationDetailItemList().addAll(inboxReputationDetailItem);
        this.reputationView.addList(inboxReputationDetailItem);
        this.skippedView.addList(inboxReputationDetailItem);
        this.noReviewView.addList(inboxReputationDetailItem);
        notifyDataSetChanged();
    }

    public void clearList() {
        inboxReputationDetail.getInboxReputationDetailItemList().clear();
        reputationView.getList().clear();
        noReviewView.getList().clear();
        skippedView.getInboxReputationDetailItem().clear();
        notifyDataSetChanged();
    }


    public void setInboxReputationDetail(InboxReputationDetail inboxReputationDetail) {
        this.inboxReputationDetail.setToken(inboxReputationDetail.getToken());
        this.noReviewView.setToken(inboxReputationDetail.getToken());
        this.reputationView.setToken(inboxReputationDetail.getToken());
        addAll(inboxReputationDetail.getInboxReputationDetailItemList());
    }
}
