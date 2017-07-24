package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseAdapterModel;

import java.util.List;

/**
 * @author by errysuprayogi on 7/24/17.
 */

public class EtalaseAdapter extends ArrayAdapter<EtalaseAdapterModel> {

    private List<EtalaseAdapterModel> list;
    private Context context;
    private LayoutInflater inflater;

    public EtalaseAdapter(Context context, List<EtalaseAdapterModel> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<EtalaseAdapterModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        EtalaseAdapterModel model = getItem(i);
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(android.R.layout.simple_spinner_item, viewGroup, false);
            holder.textView = (TextView) view.findViewById(android.R.id.text1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(model.getEtalaseName());
        return view;
    }

    public static class ViewHolder {
        public TextView textView;
    }

}
