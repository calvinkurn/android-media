package com.tokopedia.core.tracking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.tracking.model.tracking.TrackHistory;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alifa on 10/12/2016.
 */

public class TrackingHistoryAdapter extends BaseLinearRecyclerViewAdapter {
    private static final int VIEW_HISTORY = 100;

    private ArrayList<TrackHistory> list;
    private final Context context;

    public class ViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R2.id.status)
        TextView status;

        @BindView(R2.id.city)
        TextView city;

        @BindView(R2.id.time)
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public TrackingHistoryAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_HISTORY:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_track_result, null);
                return new ViewHolder(itemLayoutView);
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_HISTORY:
                bindHistory((ViewHolder) holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindHistory(final ViewHolder holder, final int position) {
        if (list.get(position).getStatus()!=null)
            holder.status.setText(MethodChecker.fromHtml(list.get(position).getStatus()));
        if (list.get(position).getCity()!=null)
            holder.city.setText(MethodChecker.fromHtml(list.get(position).getCity()));
        if (list.get(position).getDate()!=null)
            holder.time.setText(MethodChecker.fromHtml(list.get(position).getDate()));
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return VIEW_HISTORY;
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount() ;
    }

    public static TrackingHistoryAdapter createInstance(Context context) {
        return new TrackingHistoryAdapter(context);
    }

    public ArrayList<TrackHistory> getList() {
        return list;
    }

    public void addList(List<TrackHistory> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}