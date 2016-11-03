package com.tokopedia.tkpd.purchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.tkpd.database.model.Bank;

import java.util.List;

/**
 * TxConfBankListAdapter
 * Created by Angga.Prasetiyo on 23/06/2016.
 */
public class TxConfBankListAdapter extends ArrayAdapter<Bank> {
    private final int resourceId;
    private final LayoutInflater inflater;

    public TxConfBankListAdapter(Context context, int resource, List<Bank> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(resourceId, parent, false);
        if (convertView instanceof TextView)
            ((TextView) convertView).setText(getItem(position).getBankName());
        return convertView;
    }
}
