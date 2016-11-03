package com.tokopedia.tkpd.purchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.purchase.model.TxSummaryItem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TxSummaryAdapter
 * Created by Angga.Prasetiyo on 07/04/2016.
 */
public class TxSummaryAdapter extends ArrayAdapter<TxSummaryItem> {
    public final LayoutInflater inflater;
    private List<TxSummaryItem> dataList = new ArrayList<>();

    public TxSummaryAdapter(Context context) {
        super(context, R.layout.gridview_tx_center);
        this.inflater = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
    }

    public void setDataList(List<TxSummaryItem> dataList) {
        this.dataList.clear();
        this.dataList = dataList;
    }

    @Override
    public TxSummaryItem getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_tx_center, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(getItem(position).getName());
        holder.tvCount.setText(MessageFormat.format("{0}", getItem(position).getCount()));
        holder.tvDesc.setText(getItem(position).getDesc());
        return convertView;
    }


    class ViewHolder {
        @Bind(R.id.menu_title)
        TextView tvName;
        @Bind(R.id.menu_count)
        TextView tvCount;
        @Bind(R.id.menu_desc)
        TextView tvDesc;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
