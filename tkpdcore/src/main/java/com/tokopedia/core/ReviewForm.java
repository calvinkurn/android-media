package com.tokopedia.core;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.SpinnerWithImage;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core2.R;

import java.util.ArrayList;

public class ReviewForm extends TActivity {

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_REVIEW_FORM;
	}

	public static final String productIDVariabel	 = "p_id";
	public static final String imageProdVariabel	 = "prod_img";
	public static final String prodNameVariabel		 = "p_name";
	public static final String imageUrlVariabel		 = "UserImage";
	public static final String userNameVariabel		 = "UserName";
	public static final String createTimeVariabel	 = "CreateTime";
	public static final String messageVariabel		 = "Message";
	public static final String rateProdVariabel		 = "RateProd";
	public static final String rateAccuracyVariabel	 = "RateAccuracy";
	public static final String rateServiceVariabel	 = "RateService";
	public static final String rateSpeedVariabel	 = "RateSpeed";
	public static final String isEditFormVariabel	 = "EditOrWrite";
    public static final String ratingsAverage        = "RatingsAverage";
	public static final String positionVariabel		 = "position";
	public static final int resultCodeReviewForm	 = 151;

	private boolean isEdit;
	private String mImageUrl;
	private String mUsername;
	private String mCreateTime;
	private String mMessage;
	private String mImageProd;
	private String mProdName;
	private String mProdID;
	
	private int mRateProd;
	private int mRateAccuracy;
	private int mRateService;
	private int mRateSpeed;
	private int position;

	private ArrayList<Integer> ResIDSpinner = new ArrayList<Integer>();
	//atributs View
	private SpinnerWithImage Stars;
	private Spinner mQualitySpin;
	private Spinner mAccuracySpin;
	private Spinner mSpeedSpin;
	private Spinner mServiceSpin;
	private ImageView mUser_ava;
	private ImageView mProdImgView;
	private EditText mWriteMessage;
	private TextView mUserView;
	private TextView mTimeView;
	private TextView mSubmitBut;
	private TextView mCancelbut;
	private TextView mProdNameTV;
	private LinearLayout mProductLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.dialog_review_form);
		initData();
		initView();
		initAdapter();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initData() {
		mProdID			 = getIntent().getStringExtra(productIDVariabel);
		mImageUrl		 = getIntent().getStringExtra(imageUrlVariabel);
		mUsername		 = getIntent().getStringExtra(userNameVariabel);
		mCreateTime		 = getIntent().getStringExtra(createTimeVariabel);
		mMessage		 = getIntent().getStringExtra(messageVariabel);
		mImageProd		 = getIntent().getStringExtra(imageProdVariabel);
		mProdName		 = getIntent().getStringExtra(prodNameVariabel);
		mRateProd		 = getIntent().getIntExtra(rateProdVariabel, 0);
		mRateAccuracy	 = getIntent().getIntExtra(rateAccuracyVariabel, 0);
		mRateService	 = getIntent().getIntExtra(rateServiceVariabel, 0);
		mRateSpeed		 = getIntent().getIntExtra(rateSpeedVariabel, 0);
		position		 = getIntent().getIntExtra(positionVariabel, 0);
		isEdit			 = getIntent().getBooleanExtra(isEditFormVariabel, false);
		

		if(ResIDSpinner.size() == 0){
			ResIDSpinner.add(R.drawable.ic_star_none);
			ResIDSpinner.add(R.drawable.ic_star_one);
			ResIDSpinner.add(R.drawable.ic_star_two);
			ResIDSpinner.add(R.drawable.ic_star_three);
			ResIDSpinner.add(R.drawable.ic_star_four);
			ResIDSpinner.add(R.drawable.ic_star_five);
		}

	}

	private void initView() {
		
		mProductLayout	 = (LinearLayout) findViewById(R.id.prod_view);
		mUser_ava	 	 = (ImageView) findViewById(R.id.user_ava);
		mProdImgView	 = (ImageView) findViewById(R.id.product_image_iv);
		mUserView	 	 = (TextView) findViewById(R.id.user_name);
		mTimeView	 	 = (TextView) findViewById(R.id.create_time);
		mWriteMessage	 = (EditText) findViewById(R.id.write_message);
		mQualitySpin	 = (Spinner) findViewById(R.id.prod_quality_rat_spin);
		mAccuracySpin	 = (Spinner) findViewById(R.id.prod_acc_rat_spin);
		mSpeedSpin		 = (Spinner) findViewById(R.id.ship_speed_rat_spin);
		mServiceSpin	 = (Spinner) findViewById(R.id.shop_service_rat_spin);
		mSubmitBut		 = (TextView) findViewById(R.id.submit_but);
		mCancelbut		 = (TextView) findViewById(R.id.cancel_but);
		mProdNameTV		 = (TextView) findViewById(R.id.product_name_tv);

		ImageHandler.loadImageCircle2(this, mUser_ava, mImageUrl);
//		ImageHandler.LoadImageCircle(mUser_ava, mImageUrl);
		ImageHandler.loadImageRounded2(this, mProdImgView, mImageProd);
//		ImageHandler.LoadImageRounded(mProdImgView, mImageProd);
		
		if (mProdName == null) {
			mProductLayout.setVisibility(View.GONE);
		} else {
			mProdNameTV.setText(MethodChecker.fromHtml(mProdName));
		}
		mUserView.setText(mUsername);
		if(isEdit){
			mWriteMessage.setText(MethodChecker.fromHtml(mMessage));
			mTimeView.setText(mCreateTime);
		}
		
		mProductLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RouteManager.route(ReviewForm.this, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, mProdID);
			}
		});

		mSubmitBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(mWriteMessage.length()>=30 &&
						mQualitySpin.getSelectedItemPosition() > 0 && 
						mServiceSpin.getSelectedItemPosition() > 0 && 
						mAccuracySpin.getSelectedItemPosition() > 0 &&
						mSpeedSpin.getSelectedItemPosition() > 0 ){
					storeData();
					finish();
				}
				else if(mWriteMessage.length() < 30)
					mWriteMessage.setError(getString(R.string.error_review_message));
				else
					Toast.makeText(
							getBaseContext(),
							getString(R.string.error_review_rate),
							Toast.LENGTH_SHORT).show();
			}
		});
		mCancelbut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initAdapter() {
		Stars = new SpinnerWithImage(this, android.R.layout.simple_spinner_dropdown_item, ResIDSpinner);
		Stars.setDropDownViewResource(R.layout.spinner_with_image);

		mQualitySpin.setAdapter(Stars);
		mAccuracySpin.setAdapter(Stars);
		mServiceSpin.setAdapter(Stars);
		mSpeedSpin.setAdapter(Stars);
		if(isEdit){
			mQualitySpin.setSelection(mRateProd);
			mAccuracySpin.setSelection(mRateAccuracy);
			mServiceSpin.setSelection(mRateService);
			mSpeedSpin.setSelection(mRateSpeed);
		}
	}

	private void storeData(){
		Log.e("storeData", "storeData");
		Intent intent = new Intent();
		intent.putExtra(positionVariabel, position);
		intent.putExtra(messageVariabel, mWriteMessage.getText().toString());
		intent.putExtra(rateProdVariabel, mQualitySpin.getSelectedItemPosition());
		intent.putExtra(rateServiceVariabel, mServiceSpin.getSelectedItemPosition());
		intent.putExtra(rateAccuracyVariabel, mAccuracySpin.getSelectedItemPosition());
		intent.putExtra(rateSpeedVariabel, mSpeedSpin.getSelectedItemPosition());
		intent.putExtra(isEditFormVariabel, isEdit);
        intent.putExtra(ratingsAverage, calculateRatingsAverage());
		intent.putExtra("requestcode", getIntent().getExtras().getInt("requestcode"));
		setResult(resultCodeReviewForm, intent);
	}

    private float calculateRatingsAverage(){
        int quality = mQualitySpin.getSelectedItemPosition();
        int service = mServiceSpin.getSelectedItemPosition();
        int accuracy = mAccuracySpin.getSelectedItemPosition();
        int speed = mSpeedSpin.getSelectedItemPosition();
        float average = ((quality + service + accuracy + speed)/4);
        CommonUtils.dumper("JUJFJYFYFUKFF<VVMHGHF HVMGGL<CV   " + Float.toString(average));
        return average;
    }

}
