package com.tokopedia.core;

import com.tokopedia.core.listener.ShowPopupInboxMessageListener;
import com.tokopedia.core.util.MethodChecker;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.tokopedia.core2.R;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link FragmentPopupInboxMessage#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class FragmentPopupInboxMessage extends Fragment {
	
	public ShowPopupInboxMessageListener listener;
	private TextView vContent;
	private TextView vContent2;
	private CheckBox vCheckbox;
	private TextView vClosebut;
	private Context context;
	
	public static FragmentPopupInboxMessage newInstance(ShowPopupInboxMessageListener listener) {
		FragmentPopupInboxMessage fragment = new FragmentPopupInboxMessage();
		fragment.listener = listener;
		return fragment;
	}

	public FragmentPopupInboxMessage() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fragment_inbox_message,
				container, false);
		
		vContent = (TextView) view.findViewById(R.id.text_content);
		vContent2 = (TextView) view.findViewById(R.id.text_content2);
		vCheckbox = (CheckBox) view.findViewById(R.id.checked_never_show);
		vClosebut = (TextView) view.findViewById(R.id.close_but);
		
		vContent.setText(MethodChecker.fromHtml(context.getResources().getString(R.string.html_content)));
		vContent2.setText(MethodChecker.fromHtml(context.getResources().getString(R.string.html_content_2)));
		
		vContent2.setClickable(true);
		vContent2.setMovementMethod (LinkMovementMethod.getInstance());
		initListener();
		
		return view;
	}
	
	private void initListener() {
		vClosebut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onButtonClicked(vCheckbox.isChecked());
				
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
		
	}

}
