package com.tokopedia.contactus.inboxticket.adapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.customadapter.ImageUploadAdapter;
import com.tokopedia.contactus.inboxticket.InboxTicketConstant;
import com.tokopedia.contactus.inboxticket.activity.InboxTicketDetailActivity;
import com.tokopedia.contactus.inboxticket.fragment.InboxTicketFragment;
import com.tokopedia.contactus.inboxticket.model.inboxticket.InboxTicketItem;
import com.tokopedia.core.util.LabelUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 4/21/16.
 */
public class InboxTicketAdapter extends BaseLinearRecyclerViewAdapter implements InboxTicketConstant {

    private static final int VIEW_TICKET = 100;
    private static final String ON_GOING = "1";
    private boolean actionEnabled;

    public void setActionEnabled(boolean actionEnabled) {
        this.actionEnabled = actionEnabled;
    }

    public boolean isActionEnabled() {
        return actionEnabled;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.title)
        TextView title;

        @BindView(R2.id.status)
        TextView status;

        @BindView(R2.id.create_time)
        TextView createTime;

        @BindView(R2.id.main)
        View main;

        LabelUtils label;
        ImageUploadAdapter imageAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            label = LabelUtils.getInstance(itemView.getContext(), status);
            imageAdapter = ImageUploadAdapter.createAdapter(itemView.getContext());
        }
    }

    private List<InboxTicketItem> list;
    private InboxTicketFragment context;

    public InboxTicketAdapter(InboxTicketFragment context) {
        super();
        this.context = context;
        this.list = new ArrayList<>();
    }

    public static InboxTicketAdapter createAdapter(InboxTicketFragment context) {
        return new InboxTicketAdapter(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TICKET:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_inbox_ticket_2, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TICKET:
                bindTicket((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private void bindTicket(ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTicketTitle());
        holder.createTime.setText(list.get(position).getTicketUpdateTimeFmt2());
        if (list.get(position).getTicketStatus().equals(ON_GOING)) {
            holder.status.setText(context.getString(R.string.title_status_1));
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_orange));
        } else {
            holder.status.setText(context.getString(R.string.title_status_2));
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_gray));
        }

        if (list.get(position).getTicketReadStatus() == 1) {
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.main.setBackgroundResource(R.drawable.cards_ui_unread);
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.main.setBackgroundResource(R.drawable.cards_ui_selected);
        }

        List<ImageUpload> listImage = new ArrayList<>();
        //TODO : Add List Image
        holder.imageAdapter.setListener(onImageClicked());
        holder.imageAdapter.addList(listImage);

        holder.main.setOnClickListener(onGoToDetail(position));

    }

    private ImageUploadAdapter.ProductImageListener onImageClicked() {
        return new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(int position) {
                return null;
            }

            @Override
            public View.OnClickListener onImageClicked(int position, ArrayList<ImageUpload> imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }
        };
    }

    private View.OnClickListener onGoToDetail(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActionEnabled()) {
                    Intent intent = new Intent(context.getActivity(), InboxTicketDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(TICKET_ID_BUNDLE, list.get(position).getTicketId());
                    bundle.putString(INBOX_ID_BUNDLE, list.get(position).getTicketInboxId());
                    bundle.putInt(POSITION_BUNDLE, position);
                    intent.putExtras(bundle);
                    context.startActivityForResult(intent, START_INBOX_TICKET_DETAIL);
                }
            }
        };
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
            return VIEW_TICKET;
        }
    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    public List<InboxTicketItem> getList() {
        return list;
    }

    public void setList(List<InboxTicketItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setIsRead(int position) {
        if (list != null && list.size() >= position) {
            list.get(position).setTicketReadStatus(2);
            notifyDataSetChanged();
        }

    }
}
