package com.tokopedia.core.customadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;

import java.util.List;

/**
 * Created by Tkpd_Eka on 9/1/2015.
 */
public class SpinnerMultiLine extends ArrayAdapter implements SpinnerAdapter{

    int resource;
    List<String> datas;

    public SpinnerMultiLine(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    public SpinnerMultiLine(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.resource = resource;
    }

    public SpinnerMultiLine(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public SpinnerMultiLine(Context context, int resource, int textViewResourceId, Object[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
    }

    public SpinnerMultiLine(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resource = resource;
        datas = objects;
    }

    public SpinnerMultiLine(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
        datas = objects;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
//        }
//        TextView text = (TextView)convertView.findViewById(android.R.shopId.text1);
        TextView text = new TextView(getContext());
        text.setText(datas.get(position));
        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_tv_res, parent, false);
        }

        TextView item = (TextView) convertView;
        item.setText(datas.get(position));
        final TextView finalItem = item;
        item.post(new Runnable() {
            @Override
            public void run() {
                finalItem.setSingleLine(false);
            }
        });
        return item;
    }

}
