package com.tokopedia.core.inboxreputation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.presenter.InboxReputationFragmentPresenter;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.ToolTipUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 21/01/16.
 */
public class InboxReputationAdapter extends BaseLinearRecyclerViewAdapter {

    private static final int BUYER = 1;
    private static final int SELLER = 2;
    private static final int VIEW_REPUTATION = 100;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.username)
        TextView title;

        @BindView(R2.id.date)
        TextView date;

        @BindView(R2.id.avatar)
        ImageView avatar;

        @BindView(R2.id.recap_text)
        TextView recapText;

        @BindView(R2.id.view_summary_review)
        View viewFooter;

        @BindView(R2.id.main_view)
        View viewMain;

        @BindView(R2.id.reputation_holder)
        LinearLayout reputation;

        LabelUtils label;

        @BindView(R2.id.reputation_holder_user)
        View viewPercentage;

        @BindView(R2.id.rep_rating)
        TextView textPercentage;

        @BindView(R2.id.rep_icon)
        ImageView iconPercentage;

        @BindView(R2.id.deadline_icon)
        ImageView iconDeadline;

        @BindView(R2.id.deadline)
        TextView deadline;

        @BindView(R2.id.notification)
        View notification;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            label = LabelUtils.getInstance(itemView.getContext(), title);
        }
    }

    private List<InboxReputationItem> list;
    private Context context;
    private InboxReputationFragmentPresenter presenter;

    public InboxReputationAdapter(Context context) {
        super();
        this.context = context;
        this.list = new ArrayList<>();
    }

    public static InboxReputationAdapter createAdapter(Context context) {
        return new InboxReputationAdapter(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_REPUTATION:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_inbox_reputation_2, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_REPUTATION:
                bindReputation((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private void bindReputation(ViewHolder holder, int position) {
        setModelToView(holder, position);
        setVisibility(holder, position);
        setListener(holder, position);
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_REPUTATION;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    private void setModelToView(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getRevieweeName());
        setDate(holder, position);
        holder.label.giveSquareLabel(list.get(position).getLabel());
        setDeadline(holder, position);
        setAvatar(holder, position);
        setModelAsRole(holder, getRole(position), position);
    }

    private void setDate(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getCreateTime());
    }

    private void setDeadline(ViewHolder holder, int position) {
        if (list.get(position).getCanShowReputationDay()) {
            holder.deadline.setVisibility(View.VISIBLE);
            holder.iconDeadline.setVisibility(View.VISIBLE);
            holder.deadline.setText(list.get(position).getReputationDaysLeft());
        } else {
            holder.deadline.setVisibility(View.GONE);
            holder.iconDeadline.setVisibility(View.GONE);
        }
    }

    private void setAvatar(ViewHolder holder, int position) {
        ImageHandler.loadImageCircle2(context, holder.avatar, list.get(position).getRevieweeImageUrl());
    }

    private void setModelAsRole(ViewHolder holder, int role, int position) {
        switch (role) {
            case SELLER:
                holder.textPercentage.setText(list.get(position).getUserReputation().getPositivePercentage());
                setIconPercentage(holder, position);
                break;
            case BUYER:
                ReputationLevelUtils.setReputationMedals(context, holder.reputation,
                        list.get(position).getShopBadgeLevel().getSet(),
                        list.get(position).getShopBadgeLevel().getLevel(),
                        list.get(position).getReputationScore());
                break;
        }


        holder.recapText.setText(list.get(position).getReviewStatusDescription());
    }

    private void setIconPercentage(ViewHolder holder, int position) {
        if (allowActiveSmiley(position)) {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
            holder.textPercentage.setVisibility(View.VISIBLE);
        } else {
            holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
            holder.textPercentage.setVisibility(View.GONE);
        }
    }

    private boolean allowActiveSmiley(int position) {
        return list.get(position).getUserReputation().getNoReputation().equals("0");
    }

    private void setVisibility(ViewHolder holder, int position) {

        if (isShowNotification(position)) {
            holder.notification.setVisibility(View.VISIBLE);
        } else {
            holder.notification.setVisibility(View.GONE);

        }

        switch (getRole(position)) {
            case BUYER:
                holder.viewPercentage.setVisibility(View.GONE);
                holder.reputation.setVisibility(View.VISIBLE);
                break;
            case SELLER:
                holder.viewPercentage.setVisibility(View.VISIBLE);
                holder.reputation.setVisibility(View.GONE);
                break;
        }

    }

    private void setListener(ViewHolder holder, int position) {
        holder.viewPercentage.setOnClickListener(onLabelReputationUserClickListener(position));
        holder.viewMain.setOnClickListener(onMainClicked(position));
    }

    private View.OnClickListener onMainClicked(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventReviewDetail();
                presenter.onGoToDetailReview(position);

            }
        };
    }

    private View.OnClickListener onLabelReputationUserClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(setViewToolTip(position), v);
            }
        };
    }

    private View setViewToolTip(final int pos) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user,
                new ToolTipUtils.ToolTipListener() {
                    @Override
                    public void setView(View view) {
                        TextView smile = (TextView) view.findViewById(R.id.text_smile);
                        TextView netral = (TextView) view.findViewById(R.id.text_netral);
                        TextView bad = (TextView) view.findViewById(R.id.text_bad);
                        smile.setText(String.valueOf(list.get(pos).getUserReputation().getPositive()));
                        netral.setText(String.valueOf(list.get(pos).getUserReputation().getNeutral()));
                        bad.setText(String.valueOf(list.get(pos).getUserReputation().getNegative()));
                    }

                    @Override
                    public void setListener() {

                    }
                });
    }

    private boolean isShowNotification(int position) {
        return list.get(position).isShowBookmark();
    }

    private int getRole(int position) {
        return list.get(position).getRole();
    }


    public void clearList() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<InboxReputationItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public int getListSize() {
        return list.size();
    }

    public List<InboxReputationItem> getList() {
        return this.list;
    }

    public void setPresenter(InboxReputationFragmentPresenter presenter) {
        this.presenter = presenter;
    }

}
