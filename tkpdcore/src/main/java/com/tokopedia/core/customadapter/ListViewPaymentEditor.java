package com.tokopedia.core.customadapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core2.R;
import com.tokopedia.core.util.MethodChecker;

import java.util.ArrayList;


public class ListViewPaymentEditor extends BaseAdapter{
	private Activity context;
	private LayoutInflater inflater;
	private ArrayList<String> PaymentIconUri; 
	private ArrayList<String> PaymentInfo;
	private ImageHandler imagehandler = new ImageHandler();
	
	public ListViewPaymentEditor(Activity context, ArrayList<String> PaymentIconUri, ArrayList<String> PaymentInfo){
		this.context = context;
		this.PaymentIconUri = PaymentIconUri;
		this.PaymentInfo = PaymentInfo;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PaymentIconUri.size();
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
	
	public static class ViewHolder{
		ImageView PaymentIcon;
		TextView PaymentInfo;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listview_payment_editor, null);

			holder.PaymentIcon = (ImageView) convertView.findViewById(R.id.payment_icon);
			holder.PaymentInfo = (TextView) convertView.findViewById(R.id.payment_info); 
			
			convertView.setTag(holder);
		} else 
			holder=(ViewHolder)convertView.getTag();
		imagehandler.LoadImage(holder.PaymentIcon, PaymentIconUri.get(position));
		holder.PaymentInfo.setText(MethodChecker.fromHtml(PaymentInfo.get(position).replaceAll("\\*", "")));
		holder.PaymentInfo.setText(MethodChecker.fromHtml(PaymentInfo.get(position).replaceAll("Baca syarat dan ketentuannya disini", "")));
		holder.PaymentInfo.setMovementMethod(LinkMovementMethod.getInstance());

		return convertView;
	}

}
