package com.tokopedia.core.purchase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.purchase.model.response.txverinvoice.Detail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TxVerInvoiceAdapter
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class TxVerInvoiceAdapter extends ArrayAdapter<Detail> {
    private static final String TAG = TxVerInvoiceAdapter.class.getSimpleName();

    private final LayoutInflater inflater;
    private final Context context;

    public TxVerInvoiceAdapter(Context context) {
        super(context, R.layout.listview_simple, new ArrayList<Detail>());
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_simple, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(getItem(position).getInvoice());
        holder.tvTitle.setTextColor(context.getResources().getColor(R.color.href_link));
        return convertView;
    }

    class ViewHolder {
        @Bind(R2.id.text)
        TextView tvTitle;

        public ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
