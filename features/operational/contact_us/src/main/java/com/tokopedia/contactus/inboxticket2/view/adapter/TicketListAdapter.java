package com.tokopedia.contactus.inboxticket2.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;

import java.util.List;


public class TicketListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TicketsItem> itemList;
    private TicketsItem footerItem = new TicketsItem();
    private Context mContext;
    private InboxListContract.InboxListPresenter mPresenter;
    private Utils utils;
    private boolean isFooterAdded;
    private static final int ITEM = 1;
    private static final int FOOTER = 2;
    private boolean isOfficialStore = false;

    public TicketListAdapter(Context context, List<TicketsItem> data, InboxListContract.InboxListPresenter presenter) {
        mContext = context;
        itemList = data;
        mPresenter = presenter;
        utils = new Utils(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View v;

        switch (viewType) {
            case ITEM:
                v = inflater.inflate(R.layout.layout_item_ticket, parent, false);
                holder = new TicketItemHolder(v);
                break;
            case FOOTER:
                v = inflater.inflate(R.layout.inbox_footer_layout, parent, false);
                holder = new FooterViewHolder(v);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                ((TicketItemHolder) holder).bindViewHolder(itemList.get(position));
                break;
            case FOOTER:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }

    public void addFooter() {
        if (!isFooterAdded) {
            isFooterAdded = true;
            add(footerItem);
        }
    }

    private TicketsItem getItem(int position) {
        return itemList.get(position);
    }

    public void add(TicketsItem item) {
        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    public void addAll(List<TicketsItem> items) {
        for (TicketsItem item : items) {
            add(item);
        }
    }

    private void remove(TicketsItem item) {
        int position = itemList.indexOf(item);
        if (position > -1) {
            itemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    private boolean isLastPosition(int position) {
        return (position == itemList.size() - 1);
    }

    public void removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false;

            int position = itemList.size() - 1;
            TicketsItem item = getItem(position);

            if (item != null && item == footerItem) {
                itemList.remove(position);
                notifyItemRemoved(position);
            }
            notifyDataSetChanged();
            mPresenter.scrollList();
        }
    }

    class TicketItemHolder extends RecyclerView.ViewHolder {

        private AppCompatCheckBox checkboxDelete;
        private TextView tvTicketStatus;
        private TextView tvTicketTitle;
        private TextView tvTicketDesc;
        private TextView tvTicketDate;
        private TextView tvPrioritylabel;
        ConstraintLayout layoutItemTicket;

        TicketItemHolder(View itemView) {
            super(itemView);
            findingViewsId(itemView);
        }

        private void findingViewsId(View view) {
            checkboxDelete = view.findViewById(R.id.checkbox_delete);
            tvTicketStatus = view.findViewById(R.id.tv_ticket_status);
            tvTicketTitle = view.findViewById(R.id.tv_ticket_title);
            tvTicketDesc = view.findViewById(R.id.tv_ticket_desc);
            tvTicketDate = view.findViewById(R.id.tv_ticket_date);
            tvPrioritylabel = view.findViewById(R.id.tv_priority_label);
            layoutItemTicket = view.findViewById(R.id.layout_item_ticket);

        }

        void bindViewHolder(TicketsItem item) {
            if (item.getReadStatusId() == 2) {
                tvTicketDesc.setTextColor(mContext.getResources().getColor(R.color.black_38));
                tvTicketTitle.setTypeface(null, Typeface.NORMAL);
            } else {
                tvTicketDesc.setTextColor(mContext.getResources().getColor(R.color.black_70));
                tvTicketTitle.setTypeface(null, Typeface.BOLD);
            }
            tvTicketTitle.setText(item.getSubject());
            tvTicketDesc.setText(item.getLastMessagePlaintext());
            tvTicketDate.setText(utils.getDateTimeYear(item.getLastUpdate()));
            if (item.getStatusId() == 1) {
                tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_yellow);
                tvTicketStatus.setText(R.string.on_going);
                tvTicketStatus.setTextColor(mContext.getResources().getColor(R.color.black_38));
            } else if (item.getStatusId() == 2 && item.getNeedRating() != 1) {
                tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_grey);
                tvTicketStatus.setTextColor(mContext.getResources().getColor(R.color.black_38));
                tvTicketStatus.setText(R.string.closed);
            } else if (item.getNeedRating() == 1) {
                tvTicketStatus.setBackgroundResource(R.drawable.rounded_rect_orange);
                tvTicketStatus.setTextColor(mContext.getResources().getColor(R.color.red_150));
                tvTicketStatus.setText(R.string.need_rating);
            }


            if (item.isSelectableMode())
                checkboxDelete.setVisibility(View.VISIBLE);
            else
                checkboxDelete.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getIsOfficialStore()) && item.getIsOfficialStore().equalsIgnoreCase("yes")) {
                tvPrioritylabel.setVisibility(View.VISIBLE);
                tvPrioritylabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //opn tooltip
                    }
                });
            }

            layoutItemTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItem();
                }
            });

            layoutItemTicket.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });


        }

        void clickItem() {
            mPresenter.onClickTicket(getAdapterPosition(), isOfficialStore);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
