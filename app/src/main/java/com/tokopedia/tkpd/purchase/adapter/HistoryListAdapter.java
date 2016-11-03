package com.tokopedia.tkpd.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.purchase.model.response.txlist.OrderHistory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * HistoryListAdapter
 * Created by Angga.Prasetiyo on 02/05/2016.
 */
public class HistoryListAdapter extends ArrayAdapter<OrderHistory> {
    private final LayoutInflater inflater;
    private final Context context;

    public HistoryListAdapter(Context context) {
        super(context, R.layout.listview_order_status, new ArrayList<OrderHistory>());
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @SuppressLint({"InflateParams", "NewApi"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_order_status, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderHistory item = getItem(position);
        if (item.getHistoryActionBy().equalsIgnoreCase("Tokopedia")
                || item.getHistoryActionBy().equalsIgnoreCase("System-Tracker"))
            holder.tvActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_gray));
        else if (item.getHistoryActionBy().equalsIgnoreCase("Buyer"))
            holder.tvActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_orange));
        else if (item.getHistoryActionBy().equalsIgnoreCase("Seller"))
            holder.tvActor.setBackgroundColor(context.getResources().getColor(R.color.tkpd_dark_green));

        holder.tvComment.setText(item.getHistoryComments().replaceAll("<br/>\\p{Space}+", "\n"));
        holder.tvComment.setVisibility(item.getHistoryComments().equals("0")
                ? View.GONE : View.VISIBLE);

        holder.tvActor.setText(Html.fromHtml(item.getHistoryActionBy()));
        holder.tvDate.setText(Html.fromHtml(item.getHistoryStatusDateFull()));
        holder.tvStatus.setText(Html.fromHtml(item.getHistoryBuyerStatus()));

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.actor)
        TextView tvActor;
        @Bind(R.id.date)
        TextView tvDate;
        @Bind(R.id.state)
        TextView tvStatus;
        @Bind(R.id.comment)
        TextView tvComment;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
