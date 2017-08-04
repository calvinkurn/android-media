package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseAdapterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 7/24/17.
 */

public class EtalaseAdapter extends ArrayAdapter<EtalaseAdapterModel> {

    private List<EtalaseAdapterModel> list;

    public EtalaseAdapter(Context context, ArrayList<EtalaseAdapterModel> list) {
        super(context, 0, list);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setList(List<EtalaseAdapterModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_spinner_item, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textView.setText(getItem(position).getEtalaseName());
        return convertView;
    }

    static class ViewHolder {

        TextView textView;

        private ViewHolder(View rootView) {
            textView = (TextView) rootView.findViewById(android.R.id.text1);
        }
    }

}
