package com.tokopedia.core.customadapter;

import java.util.ArrayList;

import com.tokopedia.core2.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleListViewAdapter extends BaseAdapter
{
	private ArrayList<String> name = new ArrayList<String>();
	private ArrayList<Integer> Res = new ArrayList<Integer>();
	private ArrayList<Bitmap> Image = new ArrayList<Bitmap>();
	public Boolean isMain = false;

	public Activity context;
	public LayoutInflater inflater;

	public SimpleListViewAdapter(Activity context, ArrayList<String> name,  ArrayList<Bitmap> Image, ArrayList<Integer> Res) {
		super();

		this.context = context;
		this.name = name;
		this.Image = Image;
		this.Res = Res;
		
		//this.Image = pImage;
		

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public SimpleListViewAdapter(Activity context, ArrayList<String> name,  ArrayList<Integer> Res) {
		super();

		this.context = context;
		this.name = name;
		this.Res = Res;
		
		//this.Image = pImage;
		

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.size();
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
	
	
	public void AddBitmap(Bitmap pImage) {
		Image.add(pImage);
	}

	public static class ViewHolder
	{
		ImageView pImageView;
		TextView pNameView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(convertView==null || !isMain) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.simple_listview, null);

			holder.pImageView = (ImageView) convertView.findViewById(R.id.img);
			holder.pNameView = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(holder);
		} else 
			holder=(ViewHolder)convertView.getTag();
		try {
			if (Image.size()!=0) {
				holder.pImageView.setImageBitmap(Image.get(position));
			} else if (Res.size()!=0) {
				holder.pImageView.setImageResource(Res.get(position));
			}
		}catch(Exception e) {
			
		}
		holder.pNameView.setText(name.get(position));

		return convertView;
	}
}