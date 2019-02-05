package com.tokopedia.core.customadapter;



import java.util.ArrayList;

import com.tokopedia.core2.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class SpinnerWithImage extends ArrayAdapter<Integer> {
	
	public Activity context;
	public LayoutInflater inflater;
	public ArrayList<Integer> ResID;

	public SpinnerWithImage(Activity context, int resource, ArrayList<Integer> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.ResID = objects;
		
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	 @Override
	 public View getDropDownView(int position, View convertView,ViewGroup parent) {
	      return getView(position, convertView, parent);
	 }
	 
	 public static class ViewHolder
		{
			ImageView ImageView;
		}
	 
	 @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder holder;
			if(convertView==null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.spinner_with_image, null);

				holder.ImageView = (ImageView) convertView.findViewById(R.id.image);
		

				convertView.setTag(holder);
			} else 
				holder=(ViewHolder)convertView.getTag();
				holder.ImageView.setImageResource(ResID.get(position));

			return convertView;
		}


}
