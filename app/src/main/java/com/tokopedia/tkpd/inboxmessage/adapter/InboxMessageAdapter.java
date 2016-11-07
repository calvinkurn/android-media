package com.tokopedia.tkpd.inboxmessage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.tkpd.inboxmessage.InboxMessageConstant;
import com.tokopedia.tkpd.inboxmessage.model.inboxmessage.InboxMessageItem;
import com.tokopedia.tkpd.inboxmessage.presenter.InboxMessageFragmentPresenter;
import com.tokopedia.tkpd.util.ToolTipUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nisie on 5/9/16.
 */
public class InboxMessageAdapter extends BaseLinearRecyclerViewAdapter
        implements InboxMessageConstant {

    private static final int VIEW_MESSAGE = 100;
    private static final int STATE_READ = 0;
    private static final int STATE_NOT_READ = 2;
    private static final int STATE_SELECTED = 3;
    private static final int STATE_NOT_SELECTED = 3;


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.user_name)
        TextView userName;

        @Bind(R2.id.date)
        TextView date;

        @Bind(R2.id.user_ava)
        ImageView avatar;

        @Bind(R2.id.message)
        TextView message;

        @Bind(R2.id.main)
        View mainView;

        @Bind(R2.id.hour)
        TextView hour;

        @Bind(R2.id.label)
        TextView label;

        @Bind(R2.id.reputation_view)
        View viewReputation;

        @Bind(R2.id.rep_rating)
        TextView textPercentage;

        @Bind(R2.id.rep_icon)
        ImageView iconPercentage;

        @Bind(R2.id.title)
        TextView title;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ArrayList<InboxMessageItem> list;
    private ArrayList<InboxMessageItem> listMove;
    private Context context;
    private InboxMessageFragmentPresenter presenter;
    private int selected = 0;
    SimpleDateFormat sdf;
    Locale id;
    boolean isActionEnabled = false;

    public InboxMessageAdapter(Context context, InboxMessageFragmentPresenter presenter) {
        super();
        this.context = context;
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.listMove = new ArrayList<>();
        this.id = new Locale("in", "ID");
        this.sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm z", id);
        this.isActionEnabled = false;

    }

    public static InboxMessageAdapter createAdapter(Context context, InboxMessageFragmentPresenter presenter) {
        return new InboxMessageAdapter(context, presenter);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_MESSAGE:
                return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_message_2, viewGroup, false));
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_MESSAGE:
                bindMessage((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

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
            return VIEW_MESSAGE;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }


    private void bindMessage(ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getMessageTitle());
        holder.message.setText(list.get(position).getMessageReply());
        holder.userName.setText(list.get(position).getUserFullName());
        setDate(holder, position);
        setHour(holder, position);

        holder.textPercentage.setText(list.get(position).getUserReputation().getPositivePercentage());
        setIconPercentage(holder, position);
        setLabel(holder, position);

        setSelectedStatus(holder, position);
        setReadStatus(holder, position);

        holder.viewReputation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected == 0)
                    ToolTipUtils.showToolTip(setViewToolTip(position), view);
            }
        });

        holder.avatar.setOnClickListener(onGoToProfile(list.get(position)));
        holder.userName.setOnClickListener(onGoToProfile(list.get(position)));
        holder.mainView.setOnClickListener(onMessageClicked(holder, position));

        if (isActionEnabled) {
            holder.mainView.setOnLongClickListener(onLongClickListener(holder, position));
        } else {
            holder.mainView.setOnLongClickListener(null);
        }
    }

    private void setSelectedStatus(ViewHolder holder, int position) {
        if (list.get(position).isChecked())
            setSelectedState(holder);
        else if (!list.get(position).isChecked())
            setUnselectedState(holder, position);
    }


    private void setReadStatus(ViewHolder holder, int position) {
        if (list.get(position).getMessageReadStatus() == 0)
            setNotReadState(holder);
        else if (list.get(position).getMessageReadStatus() == 1)
            setReadState(holder);
    }


    private void setLabel(ViewHolder holder, int position) {
        holder.label.setText(list.get(position).getUserLabel());
        if (list.get(position).getUserLabelId() == 1) {
            try {
                holder.label.setBackgroundResource(R.drawable.label_admin);
            } catch (NoSuchMethodError e) {
                holder.label.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.label_admin));
            }
        } else {
            try {
                holder.label.setBackgroundResource(R.drawable.label_user);
            } catch (NoSuchMethodError e) {
                holder.label.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.label_user));
            }
        }
    }


    private void setDate(ViewHolder holder, int position) {
        holder.date.setVisibility(View.VISIBLE);
        try {

            holder.date.setText(list.get(position).getMessageCreateDateFmt());

            if (position != 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(list.get(position).getMessageCreateTimeFmt()));
                Calendar calBefore = Calendar.getInstance();
                calBefore.setTime(sdf.parse(list.get(position - 1).getMessageCreateTimeFmt()));
                if (cal.get(Calendar.DAY_OF_YEAR) == calBefore.get(Calendar.DAY_OF_YEAR)
                        && cal.get(Calendar.YEAR) == calBefore.get(Calendar.YEAR)) {
                    holder.date.setVisibility(View.GONE);
                }
            }
        } catch (ParseException e) {
            holder.date.setVisibility(View.GONE);
        }
    }

    private void setHour(ViewHolder holder, int position) {
        try {
            SimpleDateFormat newSdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            holder.hour.setText(newSdf.format(sdf.parse(list.get(position).getMessageCreateTimeFmt())) + " WIB");
        } catch (ParseException e) {
            holder.hour.setVisibility(View.GONE);
        }
    }

    private void setIconPercentage(ViewHolder holder, int position) {
        if (list.get(position).getUserReputation().getNoReputation().equals("0")) {
            setHasReputation(holder);
        } else {
            setNoReputation(holder);

        }
    }

    private void setHasReputation(ViewHolder holder) {
        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile_active);
        holder.textPercentage.setVisibility(View.VISIBLE);
    }

    private void setNoReputation(ViewHolder holder) {
        holder.iconPercentage.setImageResource(R.drawable.ic_icon_repsis_smile);
        holder.textPercentage.setVisibility(View.GONE);
    }

    private View.OnLongClickListener onLongClickListener(final ViewHolder holder, final int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (list.get(position).isChecked()) {
                    setReadState(holder);
                    presenter.onDeselect(position);
                } else {
                    setSelectedState(holder);
                    presenter.onSelected(position);
                }

                return true;
            }
        };
    }

    private View.OnClickListener onMessageClicked(final ViewHolder holder, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == 0) {
                    presenter.goToDetailMessage(position, list.get(position));
                } else if (list.get(position).isChecked()) {
                    setReadState(holder);
                    presenter.onDeselect(position);
                } else {
                    setSelectedState(holder);
                    presenter.onSelected(position);
                }
            }
        };
    }

    private View.OnClickListener onGoToProfile(final InboxMessageItem messageItem) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageItem.getUserLabelId() != IS_ADMIN)
                    presenter.goToProfile(messageItem.getUserId());
            }
        };
    }

    private View setViewToolTip(final int pos) {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText("" + list.get(pos).getUserReputation().getPositive());
                netral.setText("" + list.get(pos).getUserReputation().getNeutral());
                bad.setText("" + list.get(pos).getUserReputation().getNegative());
            }

            @Override
            public void setListener() {

            }
        });
    }

    private void setSelectedState(ViewHolder holder) {
        ImageHandler.loadImageWithIdWithoutPlaceholder(holder.avatar, R.drawable.ic_check_circle_48dp);
    }

    private void setUnselectedState(ViewHolder holder, int position) {
        ImageHandler.loadImageCircle2(context, holder.avatar, list.get(position).getUserImage());
    }

    private void setReadState(ViewHolder holder) {
        try {
            holder.mainView.setBackgroundResource(R.drawable.inbox_read_message);
        } catch (NoSuchMethodError e) {
            holder.mainView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.inbox_read_message));
        }
    }

    private void setNotReadState(ViewHolder holder) {
        try {
            holder.mainView.setBackgroundResource(R.drawable.inbox_unread_message);
        } catch (NoSuchMethodError e) {
            holder.mainView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.inbox_unread_message));
        }
    }

    public void setList(List<InboxMessageItem> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<InboxMessageItem> getList() {
        return list;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void clearSelection() {
        for (InboxMessageItem message : list) {
            message.setIsChecked(false);
        }
        listMove.clear();
        notifyDataSetChanged();
    }

    public void addChecked(int position) {
        InboxMessageItem item = list.get(position);
        item.setPosition(position);
        listMove.add(item);
        list.get(position).setIsChecked(true);
        this.selected++;
        notifyDataSetChanged();
    }

    public void removeChecked(int position) {
        listMove.remove(list.get(position));
        list.get(position).setIsChecked(false);
        this.selected--;
        notifyDataSetChanged();
    }

    public void removeAllChecked() {
        for (InboxMessageItem moveItem : listMove) {
            for (InboxMessageItem inboxMessageItem : list) {
                if (moveItem.getMessageId() == inboxMessageItem.getMessageId()) {
                    list.remove(inboxMessageItem);
                    break;
                }
            }
        }

        listMove.clear();
        notifyDataSetChanged();
    }

    public ArrayList<InboxMessageItem> getListMove() {
        return listMove;
    }

    public void setListMove(ArrayList<InboxMessageItem> listMove) {
        this.listMove.addAll(listMove);
        notifyDataSetChanged();
    }

    public void setEnabled(boolean isActionEnabled) {
        this.isActionEnabled = isActionEnabled;
        notifyDataSetChanged();
    }

}
