package com.tokopedia.discovery.catalog.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.discovery.catalog.model.SingleItemFilter;

import java.util.List;

/**
 * Created by kris on 12/2/16. Tokopedia
 */

public class FilterItemAdapter extends BaseAdapter{

    private Context context;

    private int selectedPosition = 0;

    private List<SingleItemFilter> items;

    public FilterItemAdapter(Activity context, int selectedPosition,
                             List<SingleItemFilter> items) {
        this.context = context;
        this.selectedPosition = selectedPosition;
        this.items = items;
    }

    public static class ViewHolder {
        TextView text;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            convertView = inflater.inflate(android.R.layout.select_dialog_item, null);
            convertView.setTag(holder);
            holder.text = (TextView) convertView;
        } else holder = (ViewHolder) convertView.getTag();
        holder.text.setText(items.get(position).getName());

        if(position == selectedPosition)
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.green_500));
        else holder.text.setTextColor(ContextCompat.getColor(context, R.color.black));
        return convertView;
    }
}
