package com.tokopedia.tkpd.inboxreputation.adapter.viewbinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.tkpd.product.activity.ProductInfoActivity;
import com.tokopedia.tkpd.util.DataBindAdapter;
import com.tokopedia.tkpd.util.DataBinder;
import com.tokopedia.tkpd.inboxreputation.adapter.InboxReputationDetailAdapter;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;
import com.tokopedia.tkpd.inboxreputation.presenter.InboxReputationDetailFragmentPresenter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/26/16.
 */
public class NoReviewDataBinder extends DataBinder<NoReviewDataBinder.ViewHolder> {


    private InboxReputationDetail inboxReputationDetail;
    private InboxReputationItem inboxReputation;
    private InboxReputationDetailFragmentPresenter presenter;
    private static final int ACTION_SKIP = 1;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.product_avatar)
        ImageView productAvatar;

        @Bind(R2.id.product_title)
        TextView productName;

        @Bind(R2.id.product_review_date)
        TextView productReviewDate;

        @Bind(R2.id.btn_overflow)
        ImageView overflow;

        @Bind(R2.id.review_info)
        TextView reviewInfo;

        @Bind(R2.id.give_review)
        View viewGiveReview;

        @Bind(R2.id.btn_give_review)
        TextView btnGiveReview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


    public NoReviewDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        inboxReputationDetail = new InboxReputationDetail();
        inboxReputationDetail.setInboxReputationDetailItem(new ArrayList<InboxReputationDetailItem>());
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup viewGroup) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_inbox_detail_reputation_no_review, viewGroup, false));
    }

    private View.OnClickListener onGiveReviewListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGiveReview(inboxReputation, inboxReputationDetail, position);
            }
        };
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        setHeaderProduct(holder, position);
        switch (inboxReputation.getRole()) {
            case InboxReputationDetailAdapter.BUYER:
                bindNoReviewBuyer(holder, position);
                break;
            case InboxReputationDetailAdapter.SELLER:
                bindNoReviewSeller(holder);

        }
        setListener(holder, position);
    }

    private void setListener(ViewHolder holder, int position) {
        holder.productName.setOnClickListener(onGoToProduct(position));
        holder.productAvatar.setOnClickListener(onGoToProduct(position));
    }

    private View.OnClickListener onGoToProduct(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductInfoActivity.createInstance(context, inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductId());
                context.startActivity(intent);
            }
        };
    }

    private void bindNoReviewSeller(ViewHolder holder) {
        holder.reviewInfo.setVisibility(View.VISIBLE);
        holder.viewGiveReview.setVisibility(View.GONE);
        holder.reviewInfo.setText(R.string.reputation_title_has_no_reviewed);

    }

    private void bindNoReviewBuyer(ViewHolder holder, int position) {
        holder.reviewInfo.setVisibility(View.GONE);
        holder.viewGiveReview.setVisibility(View.VISIBLE);
        holder.btnGiveReview.setOnClickListener(onGiveReviewListener(position));
        holder.overflow.setOnClickListener(onOverflowClickListener(position));

    }

    private View.OnClickListener onOverflowClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, position, getMenuID(position));
            }
        };
    }

    private void showPopup(final View v, final int position, int menuID) {

        PopupMenu popup = new PopupMenu(context, v);
        System.out.println("pos clicked: " + position);
        MenuInflater inflater = popup.getMenuInflater();
        if (menuID != 0)
            inflater.inflate(menuID, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_skip:
                        showDialogSkip(position);
                        return true;
                    default:
                        return false;
                }
            }

        });
        popup.show();

    }

    private int getMenuID(int position) {
        int menuID = 0;

        switch (getStatusOverFlow(position)) {
            case ACTION_SKIP:
                menuID = R.menu.skip_review_menu;
                break;
        }
        return menuID;
    }

    private void showDialogSkip(final int position) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(R.string.dialog_skip_review_confirmation)).
                setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActReviewPass pass = presenter.getSkipParam(inboxReputation,
                                inboxReputationDetail.getInboxReputationDetailItemList().get(position));
                        presenter.skipReview(pass, position);
                    }
                }).
                setNegativeButton(context.getString(R.string.title_no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }


    private void setHeaderProduct(ViewHolder holder, int position) {
        holder.productName.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductName());
        holder.productReviewDate.setText(inboxReputationDetail.getInboxReputationDetailItemList().get(position).getReviewPostTime());
        ImageHandler.LoadImage(holder.productAvatar, inboxReputationDetail.getInboxReputationDetailItemList().get(position).getProductImageUrl());
        setOverFlow(holder, position);
    }

    private void setOverFlow(ViewHolder holder, int position) {
        switch (getStatusOverFlow(position)) {
            case ACTION_SKIP:
                holder.overflow.setVisibility(View.VISIBLE);
                break;
            default:
                holder.overflow.setVisibility(View.GONE);
                break;
        }
    }

    private int getStatusOverFlow(int position) {
        if (isSkippable(position)) {
            return ACTION_SKIP;
        } else {
            return 0;
        }
    }

    private boolean isSkippable(int position) {
        return (inboxReputationDetail.getInboxReputationDetailItemList().get(position).getIsSkippable());
    }

    @Override
    public int getItemCount() {
        return inboxReputationDetail.getInboxReputationDetailItemList().size();
    }

    public void addList(java.util.List<InboxReputationDetailItem> inboxReputationDetailItem) {
        this.inboxReputationDetail.getInboxReputationDetailItemList().addAll(inboxReputationDetailItem);
        notifyDataSetChanged();
    }


    public void setInboxReputation(InboxReputationItem inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public InboxReputationItem getInboxReputation() {
        return inboxReputation;
    }

    public void setPresenter(InboxReputationDetailFragmentPresenter presenter) {
        this.presenter = presenter;
    }

    public void setToken(String token) {
        this.inboxReputationDetail.setToken(token);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public java.util.List<InboxReputationDetailItem> getList() {
        return inboxReputationDetail.getInboxReputationDetailItemList();
    }
}
