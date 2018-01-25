package com.tokopedia.core.shopinfo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
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
        setDropDownViewResource(R.layout.list_shop_etalase);
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

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DropdownViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_shop_etalase, parent, false);
            vh = new DropdownViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (DropdownViewHolder) convertView.getTag();
        }
        vh.textEtalaseName.setText(getItem(position).getEtalaseName());

        if(getItem(position).getEtalaseBadge() != null
                && !getItem(position).getEtalaseBadge().isEmpty()) {
            vh.imageView.setVisibility(View.VISIBLE);
            ImageHandler.loadImage(getContext(), vh.imageView, getItem(position).getEtalaseBadge(), R.drawable.loading_page);
        } else {
            vh.imageView.setImageDrawable(null);
            vh.imageView.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {

        TextView textView;

        private ViewHolder(View rootView) {
            textView = (TextView) rootView.findViewById(android.R.id.text1);
        }
    }

    static class DropdownViewHolder {

        TextView textEtalaseName;
        ImageView imageView;

        private DropdownViewHolder(View rootView) {
            textEtalaseName = (TextView) rootView.findViewById(R.id.text_etalase_name);
            imageView = (ImageView) rootView.findViewById(R.id.image_badge);
        }
    }

}
