package com.tokopedia.core.customadapter;

import java.util.ArrayList;

import com.tokopedia.core2.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewPeopleTransactionSummary extends BaseAdapter{

	ArrayList<String> MenuName = new ArrayList<String>(); 
	ArrayList<Integer> MenuCounter = new ArrayList<Integer>();
	ArrayList<String> MenuDesc = new ArrayList<String>();
	
	public LayoutInflater inflater;
	
	public ListViewPeopleTransactionSummary(Activity context, ArrayList<String> MenuName, ArrayList<Integer> MenuCounter, ArrayList<String> MenuDesc){
		super();
		this.MenuCounter = MenuCounter;
		this.MenuName = MenuName;
		this.MenuDesc = MenuDesc;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private class ViewHolder{
		TextView vName;
		TextView vCounter;
		TextView vDesc;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MenuName.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_tx_center, parent, false);
			holder.vName = (TextView)convertView.findViewById(R.id.menu_title);
			holder.vCounter = (TextView)convertView.findViewById(R.id.menu_count);
			holder.vDesc = (TextView)convertView.findViewById(R.id.menu_desc);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		try {
			holder.vCounter.setText(MenuCounter.get(position).toString());
			holder.vName.setText(MenuName.get(position));
			holder.vDesc.setText(MenuDesc.get(position));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

}
