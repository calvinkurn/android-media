package com.tokopedia.review.feature.inbox.buyerreview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tokopedia.kotlin.extensions.view.ImageViewExtKt;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.inbox.buyerreview.view.widgets.ShareItem;
import com.tokopedia.unifyprinciples.Typography;

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
            convertView = inflater.inflate(R.layout.reputation_sheet_grid_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShareItem info = shareItems.get(position);

        if (info.getIcon() != null) {
            ImageViewExtKt.loadImage(holder.icon, info.getIcon(), com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder);
        } else {
//                loadIcon();
        }
        holder.label.setText(info.getName());

        convertView.setOnClickListener(shareItems.get(position).getOnClickListener());
        return convertView;
    }

    class ViewHolder {
        final ImageView icon;
        final Typography label;

        ViewHolder(View root) {
            icon = (ImageView) root.findViewById(R.id.icon);
            label = (Typography) root.findViewById(R.id.label);
        }
    }

}