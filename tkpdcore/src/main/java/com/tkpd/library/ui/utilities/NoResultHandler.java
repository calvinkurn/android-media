package com.tkpd.library.ui.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tokopedia.core2.R;

public class NoResultHandler {

	private int Message1;
	private int Message2;
	private TextView Message1View;
	private TextView Message2View;
	private View view;
	private ListView lv;
	private int State = 0;
	private Intent Redirect = null;
	private Activity context = null;
	private boolean isClickableMessage = true;

	public NoResultHandler (Activity context,View view, int Message1, int Message2, Intent redirect) {
		this.context = context;
		this.view = view;
		this.Message1 = Message1;
		this.Message2 = Message2;
		Redirect = redirect;
		State = 1;
	}

	public NoResultHandler (Context context, ListView lv) {
		this.lv = lv;
		this.view = View.inflate(context, R.layout.error_no_result, null);
		this.lv.addHeaderView(this.view);
		Message1 = R.string.error_no_result_default;
		Message2 = 0;
		State = 2;
		this.view.setOnClickListener(null);
	}

	public void setResultImage(Context context, Drawable res) {
		ImageView iv = (ImageView) view.findViewById(R.id.no_res_img);
		iv.setImageDrawable(res);
	}

	public NoResultHandler (View view) {
		this.view = view;
		Message1 = R.string.error_no_result_default;
		Message2 = 0;
		State = 1;
	}

	public void tempRemoveHeader() {
		this.lv.removeHeaderView(view);
	}

	public void showMessage(boolean isMessaageClickable){

		isClickableMessage = isMessaageClickable;
		showMessage();

	}

	public void showMessage() {
		if (State == 1) {
			view.findViewById(R.id.include_no_result).setVisibility(View.VISIBLE);
		} else if (State == 2) {
			lv.addHeaderView(view);
			view.setOnClickListener(null);
		} else {
			lv.setVisibility(View.GONE);
			view.findViewById(R.id.include_no_result).setVisibility(View.VISIBLE);
		}

		Message1View = (TextView) view.findViewById(R.id.message_error_1);
		Message2View = (TextView) view.findViewById(R.id.message_error_2);

		if(isClickableMessage) {
			Message2View.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (Redirect != null) {
						context.startActivity(Redirect);
						context.finish();
					}
				}
			});
		}

		if (Message1 != 0)  {
			Message1View.setText(Message1);
		} else {
			Message1View.setVisibility(View.GONE);
		}

		if (Message2 != 0)  {
			Message2View.setText(Message2);
		} else {
			Message2View.setVisibility(View.GONE);
		}

	}

	public void removeMessage() {
		if (view != null && State == 1)  {
			view.findViewById(R.id.include_no_result).setVisibility(View.GONE);
		} else if (State == 2){
			lv.removeHeaderView(view);
		} else {
			lv.setVisibility(View.VISIBLE);
			view.findViewById(R.id.include_no_result).setVisibility(View.GONE);
		}
	}

}
