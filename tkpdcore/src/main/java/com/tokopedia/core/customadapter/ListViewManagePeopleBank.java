package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;

import java.util.ArrayList;

public class ListViewManagePeopleBank extends BaseAdapter{

	private ArrayList<String> AccountNameList = new ArrayList<String>();
	private ArrayList<String> AccountNumberList = new ArrayList<String>();
	private ArrayList<String> BankNameList = new ArrayList<String>();
	private ArrayList<String> BankBranchList = new ArrayList<String>();
	private ArrayList<String> BankIconUriList = new ArrayList<String>();
	private Activity context;
	private LayoutInflater inflater;
	private ImageHandler image;
	private ManagePeopleBankInterface listener;
	
	public interface ManagePeopleBankInterface{
		void DeleteBank(int pos);
		void EditBank(int pos);
		void DefaultBank(int pos);
	}
	
	public ListViewManagePeopleBank(Activity context, ArrayList<String> AccountNameList, ArrayList<String> AccountNumberList, ArrayList<String> BankNameList, ArrayList<String> BankBranchList, ArrayList<String> BankIconUriList){
		this.context = context;
		listener = (ManagePeopleBankInterface) context;
		this.AccountNameList = AccountNameList;
		this.AccountNumberList = AccountNumberList;
		this.BankNameList = BankNameList;
		this.BankBranchList = BankBranchList;
		this.BankIconUriList = BankIconUriList;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return AccountNameList.size();
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
	
	public class ViewHolder{
		TextView AccountName;
		TextView AccountNumber;
		TextView BankName;
		TextView BankBranch;
		ImageView BankIcon;
		ImageView DeleteBank;
		ImageView DefaultBank;
		ImageView EditBank;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_manage_people_bank, null);

			holder.AccountName = (TextView) convertView.findViewById(R.id.account_name);
			holder.AccountNumber = (TextView) convertView.findViewById(R.id.account_number);
			holder.BankName = (TextView) convertView.findViewById(R.id.bank_name);
			holder.BankBranch = (TextView) convertView.findViewById(R.id.bank_branch);
			holder.BankIcon = (ImageView) convertView.findViewById(R.id.bank_icon);
			holder.DeleteBank = (ImageView) convertView.findViewById(R.id.delete_bank);
			holder.DefaultBank = (ImageView) convertView.findViewById(R.id.default_bank);
			holder.EditBank = (ImageView) convertView.findViewById(R.id.edit_bank);
			holder.DeleteBank.setTag(position);
			holder.EditBank.setTag(position);
			holder.DefaultBank.setTag(position);
			convertView.setTag(holder);
		} else 
			Log.i("Magic", AccountNameList.get(position) + " " + position);
			holder=(ViewHolder)convertView.getTag();
			if(position==0){
				holder.DefaultBank.setVisibility(View.GONE);
			}
			else
				holder.DefaultBank.setVisibility(View.VISIBLE);
			holder.AccountName.setText(AccountNameList.get(position));
			holder.AccountNumber.setText(AccountNumberList.get(position));
			holder.BankBranch.setText(BankBranchList.get(position));
			if(BankBranchList.get(position).equals("null") || BankBranchList.get(position).length() == 0)
				holder.BankBranch.setVisibility(View.GONE);
			else
				holder.BankBranch.setVisibility(View.VISIBLE);
			holder.BankName.setText(BankNameList.get(position));
			Log.i("Magic", BankIconUriList.get(position));
			if(!BankIconUriList.get(position).equals("null")){
				image.LoadImage(holder.BankIcon, BankIconUriList.get(position));
			} else {
				image.LoadImage(holder.BankIcon, null);
			}
			
			holder.DeleteBank.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.DeleteBank(position);
				}
			});
			
			holder.EditBank.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.EditBank(position);
				}
			});
			
			holder.DefaultBank.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.DefaultBank(position);
				}
			});
			
		return convertView;
	}

}
