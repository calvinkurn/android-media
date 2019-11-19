package com.tokopedia.discovery.catalog.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokopedia.discovery.catalog.model.CatalogDetailListLocation;

import java.util.ArrayList;

/**
 * @author by alvarisi on 10/19/16.
 */
public class CatalogLocationAdapter extends ArrayAdapter<CatalogDetailListLocation> {

    @SuppressWarnings("unused")
    public CatalogLocationAdapter(Context context, int resource) {
        super(context, resource);
    }

    private static class ViewHolder {
        TextView location;
    }

    public CatalogLocationAdapter(Context context, ArrayList<CatalogDetailListLocation> locations) {
        super(context, android.R.layout.simple_list_item_1, locations);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        CatalogDetailListLocation location = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.select_dialog_item, parent, false);
            viewHolder.location = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (location != null && location.getTotalData() != 0) {
            viewHolder.location.setText(String.format("%s (%s)",
                    location.getName(), String.valueOf(location.getTotalData())));
        } else {
            viewHolder.location.setText(String.format("%s",
                    location != null ? location.getName() : ""));
        }

        return convertView;
    }
}
