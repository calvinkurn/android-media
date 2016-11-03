package com.tokopedia.tkpd.talk.talkproduct.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.talk.talkproduct.fragment.TalkAddNewFragment;
import com.tokopedia.tkpd.talk.talkproduct.intentservice.TalkAddNewIntentService;
import com.tokopedia.tkpd.talk.talkproduct.intentservice.TalkAddNewResultReceiver;

import butterknife.ButterKnife;

public class TalkAddNew extends TActivity implements TalkAddNewResultReceiver.Receiver{

	TalkAddNewResultReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_talk_product2);
		ButterKnife.bind(this);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		mReceiver = new TalkAddNewResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		String prodID= getIntent().getStringExtra("prod_id");
		String prodName = getIntent().getStringExtra("prod_name");
		Bundle bundle = new Bundle();
		bundle.putString("prod_id", prodID);
		bundle.putString("prod_name", prodName);
		if(savedInstanceState == null){
			Fragment fragment = TalkAddNewFragment.createInstance(bundle);
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container, fragment, TalkAddNew.class.getSimpleName());
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}

	public void addNewTalk(Bundle bundle) {
		TalkAddNewIntentService.startAction(this,bundle,mReceiver);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Fragment fragment = getFragmentManager().findFragmentById(R.id.container);

		if (fragment != null) {
			switch (resultCode) {
				case TalkAddNewIntentService.STATUS_SUCCESS_ADD :
					((TalkAddNewFragment)fragment).onSuccessAdd();
					break;
				case TalkAddNewIntentService.STATUS_ERROR_ADD :
					((TalkAddNewFragment)fragment).onErrorAdd(resultData.getString(TalkAddNewIntentService.EXTRA_RESULT));
					break;
				default:
					break;
			}
		}
	}
}
