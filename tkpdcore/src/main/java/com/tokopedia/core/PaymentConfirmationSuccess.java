//package com.tokopedia.core;
//
//import com.tokopedia.core.analytics.AppScreen;
//import com.tokopedia.core.app.TActivity;
//
//import android.os.Bundle;
//import android.view.Menu;
//import android.widget.TextView;
//
//public class PaymentConfirmationSuccess extends TActivity {
//
//	private TextView TotalPay;
//	private TextView TitleTotalPay;
//	private TextView MsgSuccess;
//	private TextView TokopediaDeposit;
//	private TextView RemainingDeposit;
//	private String ResultResponse;
//
//	@Override
//	public String getScreenName() {
//		return AppScreen.SCREEN_CONFIRMATION_SUCCESS;
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		inflateView(R.layout.activity_payment_confirmation_success);
//		TotalPay = (TextView) findViewById(R.id.total_payment);
//		TitleTotalPay = (TextView) findViewById(R.id.title_conf_payment);
//		MsgSuccess = (TextView) findViewById(R.id.msg_success);
//		TokopediaDeposit = (TextView) findViewById(R.id.tokopedia_deposit);
//		RemainingDeposit = (TextView) findViewById(R.id.remaining_tokopedia_deposit);
//		ResultResponse = getIntent().getExtras().getString("response");
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.payment_confirmation_success, menu);
//		return true;
//	}
//
//}
