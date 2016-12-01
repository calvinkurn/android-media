package com.tokopedia.core.inboxreputation.adapter.viewbinder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;
import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputationItem;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetailItem;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 2/26/16.
 */
public class SkippedReputationDataBinder extends DataBinder<SkippedReputationDataBinder.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.product_avatar)
        ImageView productAvatar;

        @BindView(R2.id.product_title)
        TextView productName;

        @BindView(R2.id.product_review_date)
        TextView productReviewDate;

        @BindView(R2.id.btn_overflow)
        ImageView overflow;

        @BindView(R2.id.skipped_info)
        TextView skippedInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    private static final int BUYER = 1;
    private static final int SELLER = 2;

    private List<InboxReputationDetailItem> inboxReputationDetailItem;
    private InboxReputationItem inboxReputation;
    private Context context;


    public SkippedReputationDataBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
        inboxReputationDetailItem = new ArrayList<>();
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listview_inbox_detail_reputation_skipped, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        setHeaderProduct(holder, position);
        switch (inboxReputation.getRole()){
            case BUYER : holder.skippedInfo.setText("Anda telah melewati ulasan ini");break;
            case SELLER : holder.skippedInfo.setText("Pembeli telah melewati ulasan ini");break;
        }
        setListener(holder,position);
    }

    private void setListener(ViewHolder holder, int position) {
        holder.productName.setOnClickListener(onGoToProduct(position));
        holder.productAvatar.setOnClickListener(onGoToProduct(position));
    }

    private View.OnClickListener onGoToProduct(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductInfoActivity.createInstance(context, inboxReputationDetailItem.get(position).getProductId());
                context.startActivity(intent);
            }
        };
    }

    private void setHeaderProduct(ViewHolder holder, int position) {
        holder.productName.setText(inboxReputationDetailItem.get(position).getProductName());
        holder.productReviewDate.setText(inboxReputationDetailItem.get(position).getReviewPostTime());
        ImageHandler.LoadImage(holder.productAvatar, inboxReputationDetailItem.get(position).getProductImageUrl());
        holder.overflow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return inboxReputationDetailItem.size();
    }

    public void addList(List<InboxReputationDetailItem> inboxReputationDetailItem) {
        this.inboxReputationDetailItem.addAll(inboxReputationDetailItem);
        notifyDataSetChanged();
    }

    public void setInboxReputation(InboxReputationItem inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

    public InboxReputationItem getInboxReputation() {
        return inboxReputation;
    }

    public List<InboxReputationDetailItem> getInboxReputationDetailItem() {
        return inboxReputationDetailItem;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
