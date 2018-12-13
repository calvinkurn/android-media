package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.core2.R;

import java.util.ArrayList;

public class ListViewNotification3 extends BaseAdapter
{
	private ArrayList<String> TitlePurchase = new ArrayList<String>();
	private ArrayList<String> TitleSales = new ArrayList<String>();
	private ArrayList<String> NotifTitle = new ArrayList<String>();
	private ArrayList<Integer> TotalAvail = new ArrayList<Integer>();
	private ArrayList<Integer> CountSales = new ArrayList<Integer>();
	private ArrayList<Integer> CountPurchase = new ArrayList<Integer>();

	public Activity context;
	public LayoutInflater inflater;

	public ListViewNotification3(Activity context, ArrayList<String> TitlePurchase, ArrayList<Integer> CountPurchase,
			ArrayList<String> TitleSales, ArrayList<Integer> CountSales,
			ArrayList<String> NotifTitle, ArrayList<Integer> TotalAvail) {
		super();

		this.context = context;
		this.TitlePurchase = TitlePurchase;
		this.TitleSales = TitleSales;
		this.CountSales = CountSales;
		this.CountPurchase = CountPurchase;
		this.NotifTitle = NotifTitle;
		this.TotalAvail = TotalAvail;
		//this.title = title;
		//this.images = images;
		

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder
	{
		TextView txtViewTitle;
		TextView txtViewCounter;
		TextView txtViewTitleMain;
		View MainView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_notification, null);
			holder.txtViewCounter = (TextView) convertView.findViewById(R.id.counter_notif);
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.title_notif);
			holder.txtViewTitleMain = (TextView) convertView.findViewById(R.id.title);
			holder.MainView = (View) convertView.findViewById(R.id.main_view);


			convertView.setTag(holder);
		} else 
			holder=(ViewHolder)convertView.getTag();
		if(TotalAvail.size() > 0)
			if(position < 4){ // Untuk notifikasi penjual
				if(TotalAvail.get(1)>0){
					if(position == 0){ // Menulis title
						holder.txtViewTitleMain.setVisibility(View.VISIBLE);
						holder.txtViewTitleMain.setText(NotifTitle.get(1));
						holder.txtViewTitle.setVisibility(View.GONE);
						holder.txtViewCounter.setVisibility(View.GONE);
					}
					else{
						if(CountSales.get(position - 1) > 0){
							holder.txtViewTitleMain.setVisibility(View.GONE);
							holder.txtViewTitle.setVisibility(View.VISIBLE);
							holder.txtViewCounter.setVisibility(View.VISIBLE);
							holder.txtViewTitle.setText(TitleSales.get(position - 1));
							holder.txtViewCounter.setText(CountSales.get(position - 1).toString());
						}
						else{
							holder.txtViewTitleMain.setVisibility(View.GONE);
							holder.txtViewTitle.setVisibility(View.GONE);
							holder.txtViewCounter.setVisibility(View.GONE);
						}
					}
				}
				else{
					holder.txtViewTitleMain.setVisibility(View.GONE);
					holder.txtViewTitle.setVisibility(View.GONE);
					holder.txtViewCounter.setVisibility(View.GONE);
				}
			}
			if(position > 3){ // Untuk notifikasi pembeli
				if(TotalAvail.size() > 0 && TotalAvail.get(2)>0){
					if(position == 4){
						holder.txtViewTitleMain.setVisibility(View.VISIBLE);
						holder.txtViewTitleMain.setText(NotifTitle.get(2));
						holder.txtViewTitle.setVisibility(View.GONE);
						holder.txtViewCounter.setVisibility(View.GONE);
					}
					else{
						if(CountPurchase.get(position - 5) > 0){
							holder.txtViewTitleMain.setVisibility(View.GONE);
							holder.txtViewTitle.setVisibility(View.VISIBLE);
							holder.txtViewCounter.setVisibility(View.VISIBLE);
							holder.txtViewTitle.setText(TitlePurchase.get(position - 5));
							holder.txtViewCounter.setText(CountPurchase.get(position - 5).toString());
						}
						else{
							holder.txtViewTitleMain.setVisibility(View.GONE);
							holder.txtViewTitle.setVisibility(View.GONE);
							holder.txtViewCounter.setVisibility(View.GONE);
						}
					}
				}
				else{
					holder.txtViewTitleMain.setVisibility(View.GONE);
					holder.txtViewTitle.setVisibility(View.GONE);
					holder.txtViewCounter.setVisibility(View.GONE);	
				}
			}
		
		return convertView;
	}
}