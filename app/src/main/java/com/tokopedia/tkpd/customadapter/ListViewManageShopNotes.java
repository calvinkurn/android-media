package com.tokopedia.tkpd.customadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.tkpd.ManageShopNotes;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.ViewShopNotes;

import java.util.ArrayList;

public class ListViewManageShopNotes extends BaseAdapter{

	public Activity context;
	public LayoutInflater inflater;
	private ArrayList<String> NotesListString = new ArrayList<String>(); 
	private ArrayList<String> NotesIdList = new ArrayList<String>();
//	private ArrayList<String> NotesUpdateString = new ArrayList<String>();
//	private ArrayList<String> NotesContentString = new ArrayList<String>();
	private String ShopID;
	private String ShopName = "";
	private int IsOwner; 
	int curpos;
	AlertDialog dialog;
	private String IsAllowShop = "0";
	/**
	 * @param args
	 */
	public ListViewManageShopNotes(Activity context, ArrayList<String> NotesIdList,ArrayList<String> NotesList, int IsOwner, String ShopID, String ShopName, String IsAllowShop){
		super();
		this.context = context;
		this.NotesIdList = NotesIdList;
		this.ShopName = ShopName;
		NotesListString = NotesList;
		this.IsOwner = IsOwner;
		this.IsAllowShop = IsAllowShop;
		this.ShopID = ShopID;
//		NotesContentString = NotesContent;
//		NotesUpdateString = NotesUpdate;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setAllow (String isAllow) {
		IsAllowShop = isAllow;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return NotesListString.size();
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
	
	public static class Holder{
		TextView NotesName;	
		ImageView EditNotes;
		ImageView DeleteNotes;
	}

	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView==null){
			holder = new Holder();
			convertView = inflater.inflate(R.layout.listview_manage_shop_notes,null);
			holder.NotesName = (TextView) convertView.findViewById(R.id.notes_name);
			holder.EditNotes = (ImageView) convertView.findViewById(R.id.edit_notes);
			holder.DeleteNotes = (ImageView) convertView.findViewById(R.id.delete_notes);
			
			if(IsOwner==2){
				holder.EditNotes.setVisibility(View.GONE);
				holder.DeleteNotes.setVisibility(View.GONE);
			}
			
			if(IsAllowShop.equals("0")) {
				holder.EditNotes.setVisibility(View.GONE);
				holder.DeleteNotes.setVisibility(View.GONE);
			}
			convertView.setTag(holder);
		}else
			holder = (Holder)convertView.getTag();
		holder.NotesName.setText(Html.fromHtml(NotesListString.get(position)));
		holder.NotesName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ViewShopNotes.class);
				Bundle bundle = new Bundle();
				bundle.putString("shop_id", ShopID);
				bundle.putString("shop_name", ShopName);
				bundle.putString("note_id", NotesIdList.get(position));
				bundle.putString("note_name", NotesListString.get(position));
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		
		holder.DeleteNotes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((ManageShopNotes)context).DeleteShopNotes(position);
			}
		});
		
		holder.EditNotes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((ManageShopNotes)context).EditShopNotes(position);
			}
		});
		return convertView;
	}
	

}
