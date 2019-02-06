package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.widgets.ShareItem;

import java.util.ArrayList;

/**
 * Created by stevenfredian on 2/21/17.
 */

public class ShareAdapter extends BaseAdapter {

    final LayoutInflater inflater;
    ArrayList<ShareItem> shareItems;

    public ShareAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        shareItems = new ArrayList<>();
    }

    public void addItem(ShareItem item) {
        shareItems.add(item);
    }

    @Override
    public int getCount() {
        return shareItems.size();
    }

    @Override
    public ShareItem getItem(int position) {
        return shareItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shareItems.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sheet_grid_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShareItem info = shareItems.get(position);

        if (info.getIcon() != null) {
            holder.icon.setBackgroundDrawable(info.getIcon());
        } else {
//                loadIcon();
        }
        holder.label.setText(info.getName());

        convertView.setOnClickListener(shareItems.get(position).getOnClickListener());
        return convertView;
    }

    class ViewHolder {
        final ImageView icon;
        final TextView label;

        ViewHolder(View root) {
            icon = (ImageView) root.findViewById(R.id.icon);
            label = (TextView) root.findViewById(R.id.label);
        }
    }

}