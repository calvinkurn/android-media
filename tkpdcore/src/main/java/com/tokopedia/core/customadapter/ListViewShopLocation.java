package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.MethodChecker;

public class ListViewShopLocation extends BaseAdapter {
    public Activity context;
    public LayoutInflater inflater;
    private ShopModel shopModel;

    public ListViewShopLocation(Activity context, ShopModel shopModel) {
        super();
        this.context = context;
        this.shopModel = shopModel;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return shopModel.address.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder {
        TextView ShopName;
        TextView ShopDetail;
        TextView ShopPhone;
        TextView ShopFax;
        TextView ShopMail;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_shop_location, null);

            holder.ShopName = (TextView) convertView.findViewById(R.id.detail_shop_name_all);
            holder.ShopDetail = (TextView) convertView.findViewById(R.id.detail_shop_address_all);
            holder.ShopPhone = (TextView) convertView.findViewById(R.id.detail_shop_phone_all);
            holder.ShopFax = (TextView) convertView.findViewById(R.id.detail_shop_fax_all);
            holder.ShopMail = (TextView) convertView.findViewById(R.id.detail_shop_email_all);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.ShopName.setText(MethodChecker.fromHtml(shopModel.address.get(position).locationAddressName).toString());
        holder.ShopDetail.setText(MethodChecker.fromHtml(shopModel.address.get(position).locationAddress + "\n"
                + shopModel.address.get(position).locationArea).toString());
        if (!shopModel.address.get(position).locationPhone.equals("0")) {
            holder.ShopPhone.setText(shopModel.address.get(position).locationPhone);
            holder.ShopPhone.setVisibility(View.VISIBLE);
        } else {
            holder.ShopPhone.setVisibility(View.GONE);
        }

        if (!shopModel.address.get(position).locationFax.equals("0")) {
            holder.ShopFax.setText(shopModel.address.get(position).locationFax);
            holder.ShopFax.setVisibility(View.VISIBLE);
        } else {
            holder.ShopFax.setVisibility(View.GONE);
        }


        if (!shopModel.address.get(position).locationEmail.equals("0")) {
            holder.ShopMail.setText(shopModel.address.get(position).locationEmail);
            holder.ShopMail.setVisibility(View.VISIBLE);
        } else {
            holder.ShopMail.setVisibility(View.GONE);
        }

        return convertView;
    }


}
