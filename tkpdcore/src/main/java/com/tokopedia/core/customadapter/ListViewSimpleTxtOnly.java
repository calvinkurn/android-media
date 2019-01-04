package com.tokopedia.core.customadapter;

import java.util.ArrayList;

import com.tokopedia.core2.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewSimpleTxtOnly extends BaseAdapter{
	
	private ArrayList<String> prodNameList = new ArrayList<String>();
	private Activity context;
	private LayoutInflater inflater;
	
	public ListViewSimpleTxtOnly(Activity context, ArrayList<String> pName){
		prodNameList = pName;
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE );
	}
	
	@Override
	public int getCount() {
		return prodNameList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public static class ViewHolder{
		TextView title;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_simple, null);
			convertView.setTag(holder);
			holder.title = (TextView) convertView.findViewById(R.id.text);
		}
		else
			holder=(ViewHolder)convertView.getTag();
		
		holder.title.setText(prodNameList.get(position));
		return convertView;
	}

}
