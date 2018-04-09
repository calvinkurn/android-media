package com.tokopedia.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.shopinfo.activity.ShopFavoritedActivity;
import com.tokopedia.core.util.MethodChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShopStatistic extends TActivity {

	public static final int ICON_FAST = R.drawable.ic_icon_repsis_speed_cepat;
	public static final int ICON_MEDIUM = R.drawable.ic_icon_repsis_speed_sedang;
	public static final int ICON_SLOW = R.drawable.ic_icon_repsis_speed_lambat;

	public static final String BADGE_FAST = "badge-speed-good";
	public static final String BADGE_MEDIUM = "badge-speed-neutral";
	public static final String BADGE_SLOW = "badge-speed-bad";

	private TextView ShopName;
	private TextView DetailShopName;
	private TextView ShopTag;
	private TextView ShopDesc;
	private TextView LastLogin;
	private TextView SoldItems;
	private TextView Favorited;
//	private TextView SeeFavorited;
	private TextView ShopMainLocation;
	private TextView OpenSince;
	private ImageView ShopAvatar;
	private ImageView Speed;
	private ArrayList<Integer> StarIconLocation = new ArrayList<Integer>();
	private String ShopAddrParam;
	private String ShopId;
	private String OwnerId;
	private TextView OwnerName;
	private TextView OwnerMail;
	private TextView ShopAddress;
	private TextView ShopPhone;
	private TextView ShopFax;
	private TextView ShopEmail;
	private TextView SuccessfulTx;
	private TextView ProdSold;
	private TextView TtlEtalase;
	private TextView TtlProd;
	private TextView reputationPoint;
	private TextView speedText;
	private ImageView OwnerPicture;
	private LinearLayout AddressLayout;
	private LinearLayout EmailField;
	private TextView SeeAllAddr;
	private String OwnerPictureUri;
	private boolean IsOwner;
	private String ShopAddressList;
	private LinearLayout ShippingAgencyListView;
	private LinearLayout medal;
	private View openDetailStatistic;
	//private ListViewShipmentSupport ShippingAgencyAdapter;
	// TODO HEre
	//private LinearLayout PaymentSupportListView;
	//private ListViewShipmentSupport PaymentAdapter;
	ArrayList<String> AgencyUri = null;
	ArrayList<String> AgencyPackage = null;
	ArrayList<String> PaymentUriList = new ArrayList<String>();
	ArrayList<String> EmptyList = new ArrayList<String>();
	LayoutInflater iv;

	@Override
	public String getScreenName() {
		return AppScreen.SCREEN_SHOP_STATS;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		inflateView(R.layout.activity_shop_statistic);


		// TODO Here
		iv = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ShopAvatar = (ImageView) findViewById(R.id.shop_avatar);
		AddressLayout = (LinearLayout) findViewById(R.id.address_layout);
		ShopName = (TextView) findViewById(R.id.shop_name);
		ShopTag = (TextView) findViewById(R.id.shop_tag_line);
		ShopDesc = (TextView) findViewById(R.id.short_desc);
		LastLogin = (TextView) findViewById(R.id.last_login);
		SoldItems = (TextView) findViewById(R.id.sold_items);
		EmailField = (LinearLayout) findViewById(R.id.email_field);
		Favorited = (TextView) findViewById(R.id.favorited);
//		SeeFavorited = (TextView) findViewById(R.shopId.favorited_all);
		ShopMainLocation = (TextView) findViewById(R.id.shop_location);
		OpenSince = (TextView) findViewById(R.id.open_since);
		Speed = (ImageView) findViewById(R.id.speed);
		StarIconLocation.add(R.drawable.ic_star_none);
		StarIconLocation.add(R.drawable.ic_star_one);
		StarIconLocation.add(R.drawable.ic_star_two);
		StarIconLocation.add(R.drawable.ic_star_three);
		StarIconLocation.add(R.drawable.ic_star_four);
		StarIconLocation.add(R.drawable.ic_star_five);
		OwnerName = (TextView) findViewById(R.id.owner_name);
		OwnerMail = (TextView) findViewById(R.id.owner_email);
		DetailShopName = (TextView) findViewById(R.id.detail_shop_name);
		ShopAddress = (TextView) findViewById(R.id.detail_shop_address);
		ShopPhone = (TextView) findViewById(R.id.detail_shop_phone);
		ShopFax = (TextView) findViewById(R.id.detail_shop_fax);
		ShopEmail = (TextView) findViewById(R.id.detail_shop_email);
		SuccessfulTx = (TextView) findViewById(R.id.successfull_transaction);
		ProdSold = (TextView) findViewById(R.id.product_sold);
		TtlEtalase = (TextView) findViewById(R.id.total_etalase);
		TtlProd = (TextView) findViewById(R.id.total_product);
		speedText = (TextView)findViewById(R.id.transaction_speed);
		//PaymentSupportListView = (LinearLayout) findViewById(R.shopId.listview_payment_support);
		OwnerPicture = (ImageView) findViewById(R.id.owner_picture);
		SeeAllAddr = (TextView) findViewById(R.id.shop_detail_all_addr);
		ShippingAgencyListView = (LinearLayout) findViewById(R.id.shipping_agency_listtview);
		ShopId = getIntent().getExtras().getString("shop_id");
		medal = (LinearLayout)findViewById(R.id.reputation_medal);
		openDetailStatistic =  findViewById(R.id.lihat_detail);
		reputationPoint = (TextView)findViewById(R.id.reputation_point);


		try {
			JSONObject Result = new JSONObject(getIntent().getExtras().getString("shop_info"));
			JSONObject responseSpeed = Result.getJSONObject("speed");
			JSONObject ShopInfo = new JSONObject(Result.getString("shop_info"));
			final JSONObject ShopStats = new JSONObject(Result.getString("shop_stats"));
			JSONObject OwnerInfo = new JSONObject(Result.getString("owner_info"));

			switch(responseSpeed.getString("badge")){
				case BADGE_FAST:
					Speed.setImageResource(ICON_FAST);
					break;
				case BADGE_MEDIUM:
					Speed.setImageResource(ICON_MEDIUM);
					break;
				case BADGE_SLOW:
					Speed.setImageResource(ICON_SLOW);
					break;
			}

			speedText.setText(responseSpeed.getString("speed_level"));
			reputationPoint.setText(getScoreMedal(ShopStats)+" "+getString(R.string.title_poin));
			ReputationLevelUtils.setReputationMedals(this, medal, getMedalType(ShopStats), getMedalLevel(ShopStats), getScoreMedal(ShopStats));

			if(OwnerInfo.getString("flag_email").equals("2"))
				OwnerMail.setText(OwnerInfo.getString("user_email"));
				else
					EmailField.setVisibility(View.GONE);
			if(!Result.isNull("shop_address")){
			JSONArray ShopAddressList = new JSONArray(Result.getString("shop_address"));
			JSONObject MainShopAddr = new JSONObject(ShopAddressList.getString(0));
			ShopAddrParam = ShopAddressList.toString();
			if(ShopAddressList.length() == 1)
				SeeAllAddr.setVisibility(View.INVISIBLE);
			DetailShopName.setText(MethodChecker.fromHtml(MainShopAddr.getString("addr_name")).toString());
				ShopAddress.setText(MethodChecker.fromHtml(MainShopAddr.getString("address"))  + "\n"
						+ MethodChecker.fromHtml(MainShopAddr.getString("area")));
				if(MainShopAddr.getString("phone")!="null")
				ShopPhone.setText(MainShopAddr.getString("phone"));
				else
					ShopPhone.setVisibility(View.GONE);
				if(MainShopAddr.getString("fax")!="null")
				ShopFax.setText(MainShopAddr.getString("fax"));
				else
					ShopFax.setVisibility(View.GONE);
				if(MainShopAddr.getString("email")!="null")
				ShopEmail.setText(MainShopAddr.getString("email"));
				else
					ShopEmail.setVisibility(View.GONE);
			}
			else
				AddressLayout.setVisibility(View.GONE);
			if(Result.getInt("is_owner") != 0)
				IsOwner = true;

			ShopName.setText(MethodChecker.fromHtml(ShopInfo.getString("shop_name")));
			ShopTag.setText(getIntent().getExtras().getString("shop_tag"));
			ShopDesc.setText(getIntent().getExtras().getString("shop_desc"));
			LastLogin.setText(Result.getString("last_login"));
			SoldItems.setText(ShopStats.getString("item_sold"));
			Favorited.setText(Result.getString("total_fave"));
			ShopMainLocation.setText(Result.getString("shop_location"));
			OpenSince.setText(ShopInfo.getString("open_since"));
			OnClickListener ToReview = new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = ((TkpdCoreRouter) ShopStatistic.this.getApplication()).getShopPageIntent(ShopStatistic.this, sessionHandler.getShopID());
					startActivity(intent);
				}
			};
			//ShopAvatar.setImageBitmap((Bitmap)getIntent().getExtras().getParcelable("shop_avatar"));
			ImageHandler.loadImageCircle2(this, ShopAvatar, getIntent().getExtras().getString("shop_ava_uri"));
//			ImageHandler.LoadImageCircle(ShopAvatar, getIntent().getExtras().getString("shop_ava_uri"));
			OwnerName.setText(MethodChecker.fromHtml(OwnerInfo.getString("full_name")));
			ImageHandler.loadImageCircle2(this, OwnerPicture, OwnerInfo.getString("owner_img"));
//			ImageHandler.LoadImageCircle(OwnerPicture, OwnerInfo.getString("owner_img"));
			OwnerId = OwnerInfo.getString("user_id");
			SuccessfulTx.setText(ShopStats.getString("total_tx_success"));
			ProdSold.setText(ShopStats.getString("item_sold_fmt"));
			TtlEtalase.setText(ShopStats.getString("count_etalase"));
			TtlProd.setText(ShopStats.getString("count_product"));
			JSONObject Shipment = new JSONObject(Result.getString("shipment"));
			JSONArray ShopShipmentList = new JSONArray(Shipment.getString("shop_shipment"));
			JSONObject ShopShipmentPackageList = new JSONObject(Shipment.getString("shop_shipment_package"));
			JSONObject ShopShipment;
			JSONArray ShopShipmentPackage;
			JSONObject SSIndividualPackage;
			AgencyUri = new ArrayList<String>();
			AgencyPackage = new ArrayList<String>();
			String SSCombined = "";
			for(int i=0; i<ShopShipmentList.length(); i++){
				ShopShipment = new JSONObject(ShopShipmentList.getString(i));
				AgencyUri.add(ShopShipment.getString("logo"));
				ShopShipmentPackage = new JSONArray(ShopShipmentPackageList.getString(ShopShipment.getString("shipping_id")));
				SSCombined = "";
				for(int j=0; j<ShopShipmentPackage.length(); j++){
					SSIndividualPackage = new JSONObject(ShopShipmentPackage.getString(j));
					if(j==0)
					SSCombined = SSIndividualPackage.getString("product_name");
					else
					SSCombined = SSCombined + "\n" + SSIndividualPackage.getString("product_name");
				}
				AgencyPackage.add(SSCombined);
				}

			if (!Result.getString("shop_payment").equals("null")) {
				JSONArray ShopPaymentList = new JSONArray(
						Result.getString("shop_payment"));
				for (int i = 0; i < ShopPaymentList.length(); i++) {
					JSONObject ShopPayment = new JSONObject(
							ShopPaymentList.getString(i));
					PaymentUriList.add(ShopPayment.getString("img"));
					EmptyList.add("");
				}
			}
			//ShippingAgencyAdapter = new ListViewShipmentSupport(ShopStatistic.this, AgencyUri, AgencyPackage);
			//ShippingAgencyListView.setAdapter(ShippingAgencyAdapter);
			//ShippingAgencyAdapter.notifyDataSetChanged();
			//PaymentAdapter = new ListViewShipmentSupport(ShopStatistic.this, PaymentUriList, EmptyList);
			// TODO Sini
			//PaymentSupportListView.setAdapter(PaymentAdapter);
			//PaymentAdapter.notifyDataSetChanged();
			//ListViewHelper.getListViewSize(ShippingAgencyListView);
			//ListViewHelper.getListViewSize(PaymentSupportListView);
			for(int i=0 ;i<AgencyUri.size(); i++){
				View v = new View(ShopStatistic.this);
				v = iv.inflate(R.layout.listview_shipment_support, null);
				ImageView imv = (ImageView)v.findViewById(R.id.agency_logo);
				imv.getLayoutParams().height = (int) ShopStatistic.this.getResources().getDimension(R.dimen.img_thumb);
				imv.getLayoutParams().width = (int) ShopStatistic.this.getResources().getDimension(R.dimen.img_thumb);
				TextView package_list = (TextView)v.findViewById(R.id.package_list);
				package_list.setText(AgencyPackage.get(i));
				ImageHandler.LoadImage(imv, AgencyUri.get(i));
				ShippingAgencyListView.addView(v);
			}
			//Payment support tidak dipakai lagi
//			for(int i=0 ;i<PaymentUriList.size(); i++){
//				View v = new View(ShopStatistic.this);
//				v = iv.inflate(R.layout.listview_shipment_support, null);
//				ImageView imv = (ImageView)v.findViewById(R.shopId.agency_logo);
//				ImageHandler.LoadImage(imv, PaymentUriList.get(i));
//				PaymentSupportListView.addView(v);
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

        OwnerName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getBaseContext().getApplicationContext() instanceof TkpdCoreRouter) {
                    startActivity(((TkpdCoreRouter) getBaseContext().getApplicationContext())
                            .getTopProfileIntent(getBaseContext(), OwnerId));
                }
            }
        });

        Favorited.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopStatistic.this, ShopFavoritedActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("shop_id", ShopId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		SeeAllAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putBoolean("is_owner", IsOwner);
				bundle.putString("address_list", ShopAddrParam);
				Intent intent = new Intent(ShopStatistic.this, ShopLocation.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		openDetailStatistic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ShopStatistic.this, ShopStatisticDetail.class);
				intent.putExtra(ShopStatisticDetail.EXTRA_SHOP_INFO, getIntent().getExtras().getString("shop_info"));
				startActivity(intent);
			}
		});

	}

	private int getMedalType(JSONObject jsonObject) throws JSONException {
		JSONObject badges = new JSONObject(jsonObject.getString("reputation_badge"));
		return badges.getInt("set");
	}

	private int getMedalLevel(JSONObject jsonObject) throws  JSONException {
		JSONObject badges = new JSONObject(jsonObject.getString("reputation_badge"));
		return badges.getInt("level");
	}

	private String getScoreMedal(JSONObject json) throws JSONException {
		return json.getString("reputation_score");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	onBackPressed();
	        return true;

	    }

	    return super.onOptionsItemSelected(item);


	}

}
