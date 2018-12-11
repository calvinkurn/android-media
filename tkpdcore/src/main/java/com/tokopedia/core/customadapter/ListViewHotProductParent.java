package com.tokopedia.core.customadapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

import java.util.ArrayList;

public class ListViewHotProductParent extends BaseAdapter
{
	protected ArrayList<String> HpName = new ArrayList<String>();
	protected ArrayList<String> HpPrice = new ArrayList<String>();
	protected ArrayList<String> HpImgUri = new ArrayList<String>();
	protected ArrayList<String> HpImgUri600 = new ArrayList<String>();
			
	public Activity context;
	public LayoutInflater inflater;



	public ListViewHotProductParent(Activity context, ArrayList<String> HpName,  ArrayList<String> HpPrice, ArrayList<String> HpImgUri, ArrayList<String> HpImgUri600) {
		this.context = context;
		this.HpName = HpName;
		this.HpPrice = HpPrice;
		this.HpImgUri = HpImgUri;
		this.HpImgUri600 = HpImgUri600;
		
	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return HpName.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	public static class ViewHolder
	{
		ImageView HpImage;
		TextView HpName;
		TextView HpPrice;
		TextView HpImgUri600;
	}
	
	
	
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_hotproduct, null);

//			holder.pImageView = (ImageView) convertView.findViewById(R.shopId.prod_img);
			
			holder.HpImage = (ImageView) convertView.findViewById(R.id.hotprod_img);
			holder.HpName = (TextView) convertView.findViewById(R.id.hotprod_name);
			holder.HpPrice = (TextView) convertView.findViewById(R.id.hotprod_price);

			convertView.setTag(holder);
		} else 
			holder=(ViewHolder)convertView.getTag();
		ImageHandler.LoadImage(holder.HpImage, HpImgUri600.get(position));
		holder.HpName.setText(HpName.get(position));
		holder.HpPrice.setText(HpPrice.get(position));
		
		
		
		return convertView;
	}
	
	
}