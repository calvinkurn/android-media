package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.ManageShopAddress;
import com.tokopedia.core.R;

import java.util.ArrayList;

public class ListViewManageShopLocation extends BaseAdapter {

	ArrayList<String> LocationNameList;
	ArrayList<String> LocationAddressList;
	ArrayList<String> LocationPhoneList;
	ArrayList<String> LocationFaxList;
	ArrayList<String> LocationEmailList;
	private Activity context;
	private String IsAllowShop;
	private LayoutInflater inflater;

	public ListViewManageShopLocation(Activity context,
			ArrayList<String> LocationNameList,
			ArrayList<String> LocationAddressList,
			ArrayList<String> LocationPhoneList,
			ArrayList<String> LocationFaxList,
			ArrayList<String> LocationEmailList, String IsAllowShop) {
		this.LocationNameList = LocationNameList;
		this.LocationAddressList = LocationAddressList;
		this.LocationPhoneList = LocationPhoneList;
		this.LocationFaxList = LocationFaxList;
		this.LocationEmailList = LocationEmailList;
		this.IsAllowShop = IsAllowShop;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setAllow (String isAllow) {
		IsAllowShop = isAllow;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return LocationNameList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class Holder {
		TextView LocationName;
		TextView LocationAddress;
		TextView LocationPhone;
		TextView LocationFax;
		TextView LocationEmail;
		ImageView EditLocation;
		ImageView DeleteLocation;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(
					R.layout.listview_manage_shop_location, null);
			holder.LocationName = (TextView) convertView
					.findViewById(R.id.location_name);
			holder.LocationAddress = (TextView) convertView
					.findViewById(R.id.location_address);
			holder.LocationPhone = (TextView) convertView
					.findViewById(R.id.location_phone);
			holder.LocationFax = (TextView) convertView
					.findViewById(R.id.location_fax);
			holder.LocationEmail = (TextView) convertView
					.findViewById(R.id.location_email);
			holder.EditLocation = (ImageView) convertView
					.findViewById(R.id.edit_location);
			holder.DeleteLocation = (ImageView) convertView
					.findViewById(R.id.delete_location);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();

		holder.LocationName.setText(LocationNameList.get(position));
		holder.LocationAddress.setText(LocationAddressList.get(position));
		holder.LocationPhone.setText(LocationPhoneList.get(position));
		holder.LocationFax.setText(LocationFaxList.get(position));
		holder.LocationEmail.setText(LocationEmailList.get(position));

		if (!CommonUtils.checkNullForZeroJson(LocationPhoneList.get(position)))
			holder.LocationPhone.setVisibility(View.GONE);
		else
			holder.LocationPhone.setVisibility(View.VISIBLE);

		if (!CommonUtils.checkNullForZeroJson(LocationFaxList.get(position)))
			holder.LocationFax.setVisibility(View.GONE);
		else
			holder.LocationFax.setVisibility(View.VISIBLE);

		if (!CommonUtils.checkNullForZeroJson(LocationEmailList.get(position)))
			holder.LocationEmail.setVisibility(View.GONE);
		else
			holder.LocationEmail.setVisibility(View.VISIBLE);
		
		if(IsAllowShop.equals("0")) {
			holder.EditLocation.setVisibility(View.GONE);
			holder.DeleteLocation.setVisibility(View.GONE);
		}

		holder.EditLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((ManageShopAddress) context).EditLocation(position);
			}
		});

		holder.DeleteLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((ManageShopAddress) context).DeleteLocationV4(position);
			}
		});

		return convertView;
	}

}
