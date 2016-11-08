package com.tokopedia.core;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.DatePickerUtil.onDateSelectedListener;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ListViewHelper;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customadapter.ListViewShopOrderDetail;
import com.tokopedia.core.selling.model.shopconfirmationdetail.ShippingConfirmDetModel;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.var.NotificationVariable;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class ShippingConfirmationProdConf extends TActivity {

	private TextView BuyerName;
	private TextView Invoice;
	private TextView Courier;
	private TextView TotalItem;
	private TextView Value;
	private TextView ReceiverName;
	private TextView Destination;
	private TextView ErrorMessage;
	private TextView ConfirmButton;
	private TextView CancelButton;
	private TextView DetailButton;
	private TextView errorSpinner;
	private EditText ReferenceNumber;
	private EditText ShippingDate;
	private ImageView vScan;
	private CheckBox switchCourier;
	private Spinner spinnerAgency;
	private Spinner spinnerService;
	private ProgressBar loadingSpinner;
	private LinearLayout layout;
	private ScrollView mainScroll;
	private Dialog dialog;

	private String day;
	private String month;
	private String year;
	private String OrderDetailList;
	private String Permission;
	private String UserID;
	private String PdfUri;
	private String PdfList;
	private boolean isProcessed = false;
	static final int DATE_DIALOG_ID = 0;
	private TkpdProgressDialog mProgressDialog;
	private NotificationVariable notif;
	private DatePickerUtil picker;
	private TextView SenderName;
	private TextView SenderPhone;
	private View SenderForm;
	private ListViewShopOrderDetail ProductAdapter;
	private ListView ProductListView;

	private ArrayList<String> ImageUrlList = new ArrayList<String>();
	private ArrayList<String> NameList = new ArrayList<String>();
	private ArrayList<String> PriceList = new ArrayList<String>();
	private ArrayList<String> TtlOrderList = new ArrayList<String>();
	private ArrayList<String> TtlPriceList = new ArrayList<String>();
	private ArrayList<String> MessageList = new ArrayList<String>();
	private ArrayList<String> ProductId = new ArrayList<String>();
	private ArrayList<String> ActorList = new ArrayList<String>();
	private ArrayList<String> DateList = new ArrayList<String>();
	private ArrayList<String> StateList = new ArrayList<String>();
	private ArrayList<String> ProductUrlList = new ArrayList<String>();
	private ArrayList<String> ProductIdList = new ArrayList<String>();
	private ArrayList<String> AllAgencyName = new ArrayList<String>();
	private ArrayList<String> AllAgencyID = new ArrayList<String>();
	private ArrayList<String> agencyName = new ArrayList<String>();
	private ArrayList<String> agencyService;
	private ArrayList<String> agencyServiceID;
	private ArrayAdapter<String> adapterAgency;
	private ArrayAdapter<String> adapterService;

	private HashMap<String, ArrayList<String>> hashMapServiceName;
	private HashMap<String, ArrayList<String>> hashMapServiceID;
	private HashMap<String, String> AllKurir = new HashMap<String, String>();

	private String OrderId;
	private String current_shipping = null;
	private String current_service = null;
	private String newShippingAgencyName = "";
	private String newShippingAgencyID = "";
	private String newService = "";
	private String newServiceID = "";

	private Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OrderDetailList = getIntent().getExtras().getString("order");
		Permission = getIntent().getExtras().getString("permission");
		UserID = getIntent().getExtras().getString("user_id");
		PdfUri = getIntent().getExtras().getString("invoice_uri");
		PdfList = getIntent().getExtras().getString("invoice_pdf");
		inflateView(R.layout.activity_shipping_confirmation_prod_conf);
		picker = new DatePickerUtil(this);


		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		BuyerName = (TextView) findViewById(R.id.buyer_name);
		Invoice = (TextView) findViewById(R.id.invoice_text);
		Courier = (TextView) findViewById(R.id.courier);
		TotalItem = (TextView) findViewById(R.id.total_item);
		Value = (TextView) findViewById(R.id.value);
		ReceiverName = (TextView) findViewById(R.id.receiver_name);
		Destination = (TextView) findViewById(R.id.destination);
		ErrorMessage = (TextView) findViewById(R.id.error_message);
		ShippingDate = (EditText) findViewById(R.id.shipping_date);
		DetailButton  = (TextView) findViewById(R.id.detail_button);
		CancelButton = (TextView) findViewById(R.id.cancel_button);
		ConfirmButton = (TextView) findViewById(R.id.confirm_button);
		ReferenceNumber = (EditText) findViewById(R.id.ref_number);
		SenderName = (TextView) findViewById(R.id.sender_name);
		SenderPhone = (TextView) findViewById(R.id.sender_phone);
		SenderForm = findViewById(R.id.sender_form);
		vScan = (ImageView)findViewById(R.id.scan);
		switchCourier = (CheckBox) findViewById(R.id.checkBoxSwitchCourier);
		spinnerAgency = (Spinner) findViewById(R.id.spinner_kurir);
		spinnerService = (Spinner) findViewById(R.id.spinner_type);
		layout = (LinearLayout) findViewById(R.id.layout);
		loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);
		errorSpinner = (TextView) findViewById(R.id.error_spinner);
		mainScroll = (ScrollView) findViewById(R.id.scroll_view);
		hashMapServiceName = new HashMap<String, ArrayList<String>>();
		hashMapServiceID = new HashMap<String, ArrayList<String>>();
		context = ShippingConfirmationProdConf.this;
		notif = MainApplication.getNotifInstance();
		notif.setContext(this);

		getSpinnerValue();

		vScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, 0);
			}
		});
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH) + 1;
		int cday = c.get(Calendar.DAY_OF_MONTH);
		//		day = Integer.toString(cday);
		//		month = Integer.toString(cmonth);
		//		year = Integer.toString(cyear);
		picker.SetDate(cday, cmonth, cyear);
		ShippingDate.setText(cday + "/" + cmonth + "/" + cyear);
		try {
			JSONObject order = new JSONObject(getIntent().getExtras().getString("order"));
			JSONObject customer = new JSONObject(order.getString("customer"));
			JSONObject orderdata = new JSONObject(order.getString("order"));
			JSONObject shipping = new JSONObject(order.getString("shipping"));
			JSONObject dest = new JSONObject(order.getString("dest"));
			OrderId = orderdata.getString("order_id");
			TotalItem.setText(Html.fromHtml(getString(R.string.title_total_item) + ": <b>" + orderdata.getString("quantity") + " ( " + orderdata.getString("total_weight") + "kg )</b>"));
			Invoice.setText(orderdata.getString("invoice"));
			BuyerName.setText(customer.getString("cust_name"));
			Courier.setText(shipping.getString("shipping_name") + "( " + shipping.getString("shipping_product") + " )");
			Value.setText(Html.fromHtml(getString(R.string.title_transaction_value) + " : <b>" + orderdata.getString("open_amt_idr")+"</b>"));
			ReceiverName.setText(dest.getString("receiver_name"));
			Destination.setText(Html.fromHtml(dest.getString("receiver_name") + "<br>" +dest.getString("address_name").replace("<br/>", "\n").replace("<br>", "\n")
					+ "<br>" + dest.getString("district") + " " + dest.getString("city") + ", " + dest.getString("postal")
					+ "<br>" + dest.getString("province") + "<br>"+ getString(R.string.title_phone) + " : " +dest.getString("phone")));

			if (!orderdata.isNull("dropship_name")) {
				SenderName.setText(orderdata.getString("dropship_name"));
				SenderPhone.setText(orderdata.getString("dropship_telp"));
				SenderForm.setVisibility(View.VISIBLE);
			} else {
				SenderForm.setVisibility(View.GONE);
			}

			current_shipping = shipping.getString("shipping_name");
			current_service = shipping.getString("shipping_product");

			JSONArray productList = new JSONArray(order.getString("products"));
			JSONObject product;

			for(int i=0; i<productList.length();i++){
				product = new JSONObject(productList.getString(i));
				ProductId.add(product.getString("order_dtl_id"));
				ImageUrlList.add(product.getString("product_pic"));
				NameList.add(product.getString("product_name"));
				PriceList.add(product.getString("product_price"));
				ProductUrlList.add(product.getString("product_url"));
				ProductIdList.add(product.getString("product_id"));
				TtlOrderList.add(product.getString("deliver_qty"));
				TtlPriceList.add(product.getString("subtotal_price_idr"));
				MessageList.add(product.getString("notes"));
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		ArrayList<ShippingConfirmDetModel.Data> datas =
				convertFromOldDatas(ImageUrlList, NameList, PriceList, TtlOrderList,
						TtlPriceList, MessageList, ProductUrlList, ProductIdList);

		ProductAdapter = new ListViewShopOrderDetail(ShippingConfirmationProdConf.this, datas);
		ProductListView = (ListView) findViewById(R.id.product_list);
		ProductListView.setAdapter(ProductAdapter);
		ProductListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				//				Intent intent = new Intent(ShippingConfirmationDetail.this, ProductDetailPresenter.class);
				Intent intent = new Intent(ShippingConfirmationProdConf.this, ProductInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("product_uri", ProductUrlList.get(position));
				bundle.putString("product_id", ProductIdList.get(position));
				System.out.println(ProductUrlList.get(position));
				System.out.println(ProductIdList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		ListViewHelper.getListViewSize(ProductListView);

		BuyerName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(
						PeopleInfoNoDrawerActivity.createInstance(getBaseContext(), UserID)
				);
			}
		});

		ShippingDate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				//				showDialog(DATE_DIALOG_ID);
				picker.DatePickerCalendar(new onDateSelectedListener() {

					@Override
					public void onDateSelected(int year, int month, int dayOfMonth) {
						ShippingDate.setText(dayOfMonth + "/" + month + "/" + year);
					}
				});
				return false;
			}
		});

		DetailButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), ShippingConfirmationDetail.class);
				Bundle bundle = new Bundle();
				bundle.putString("order", OrderDetailList);
				bundle.putString("permission", Permission);
				bundle.putString("user_id", UserID);
				bundle.putString("invoice_uri", PdfUri);
				bundle.putString("invoice_pdf", PdfList);
				intent.putExtras(bundle);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivityForResult(intent, 0);

			}
		});

		CancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CancelOrder();
			}
		});

		ConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				errorSpinner.setVisibility(View.GONE);
				ReferenceNumber.setError(null);

				if (ReferenceNumber.length() > 7 && ReferenceNumber.length() < 18){
					if(switchCourier.isChecked()){
						if(!(newShippingAgencyName.length()==0 || newServiceID.length()==0 || newService.length()==0)){
							Confirm("confirm", "");
							CommonUtils.dumper("validasi 1");
						} else {
							if(newShippingAgencyName.length()==0) {
								CommonUtils.dumper("validasi 2");
								errorSpinner.setText(getString(R.string.error_shipping_must_choose));
								errorSpinner.setVisibility(View.VISIBLE);
								mainScroll.scrollTo(Math.round(errorSpinner.getX()), Math.round(errorSpinner.getY()));
							} else if(newService.length()==0) {
								CommonUtils.dumper("validasi 3");
								errorSpinner.setText(getString(R.string.error_service_must_choose));
								errorSpinner.setVisibility(View.VISIBLE);
								mainScroll.scrollTo(Math.round(errorSpinner.getX()), Math.round(errorSpinner.getY()));
							} else {
								CommonUtils.dumper("Fungsi null "+newServiceID+" & "+newShippingAgencyID);
							}
						}
					} else {
						CommonUtils.dumper("validasi 4");
						Confirm("confirm", "");
					}
				} else {
					if (ReferenceNumber.length() == 0)
						ReferenceNumber
								.setError(getString(R.string.error_field_required));
					else
						ReferenceNumber
								.setError(getString(R.string.error_receipt_number));
				}
			}
		});

		switchCourier.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					CommonUtils.dumper("Checkbox : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
					layout.setVisibility(View.VISIBLE);
				} else {
					CommonUtils.dumper("Checkbox : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
					layout.setVisibility(View.GONE);
					errorSpinner.setVisibility(View.GONE);
				}
			}
		});

		spinnerAgency.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				errorSpinner.setVisibility(View.GONE);
				if(position > 0){
					setServiceSpinner(position);
					newShippingAgencyName = spinnerAgency.getItemAtPosition(position).toString();

					for(int i=0; i<AllAgencyName.size(); i++){
						if(AllAgencyName.get(i).toString().equals(newShippingAgencyName)){
							newShippingAgencyID = AllAgencyID.get(i).toString();
						}
					}

					newService = "";
					newServiceID = "";
					CommonUtils.dumper("shipping : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
					spinnerService.setVisibility(View.VISIBLE);
				} else {
					newShippingAgencyID="";
					newShippingAgencyName="";
					newService="";
					newServiceID = "";
					CommonUtils.dumper("shipping : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
					spinnerService.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinnerService.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				errorSpinner.setVisibility(View.GONE);
				if(position > 0){
					newService = spinnerService.getItemAtPosition(position).toString();
					newServiceID = agencyServiceID.get(position).toString();
					CommonUtils.dumper("servis : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
				} else {
					newService = "";
					newServiceID = "";
					CommonUtils.dumper("servis : " + newShippingAgencyID + " / " + newShippingAgencyName +" / " + newServiceID + " / " + newService);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void Loading(){
		mProgressDialog = new TkpdProgressDialog(ShippingConfirmationProdConf.this, TkpdProgressDialog.NORMAL_PROGRESS);
		mProgressDialog.showDialog();
	}

	private void CancelOrder(){
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_cancel_order);
		dialog.setTitle(getString(R.string.title_cancel_confirm));
		final EditText Remark = (EditText)dialog.findViewById(R.id.remark);
		TextView ConfirmButton = (TextView)dialog.findViewById(R.id.confirm_button);

		ConfirmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Remark.setError(null);
				if(Remark.length()>0){
					Confirm("cancel", Remark.getText().toString());
					dialog.dismiss();
					setResult(RESULT_OK);
					finish();
				}
				else
					Remark.setError(getString(R.string.error_field_required));
			}
		});

		dialog.show();
	}

	public void Confirm(String Action, String remark){
		CommonUtils.dumper("Fungsi ConfirmaShipping : "+newShippingAgencyID + " / " + newShippingAgencyName + " / " + newServiceID +" / "+ newService);
		Loading();
		NetworkHandler network = new NetworkHandler(ShippingConfirmationProdConf.this, TkpdUrl.MY_SHOP_ORDER);
		network.AddParam("act", "confirmation_shipping_event");
		network.AddParam("order_id", OrderId);
		network.AddParam("action_type", Action);
		network.AddParam("day", day);
		network.AddParam("month", month);
		network.AddParam("year", year);
		if(remark.length()>0)
			network.AddParam("reason", remark);
		network.AddParam("shipping_ref", ReferenceNumber.getText().toString());

		if(switchCourier.isChecked()){
			network.AddParam("agency_id", newShippingAgencyID);
			network.AddParam("agency_name", newShippingAgencyName);
			network.AddParam("sp_id", newServiceID);
		}
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {
				notif.GetNotif();
				mProgressDialog.dismiss();
			}

			@Override
			public void getResponse(JSONObject Result) {

				try {
					if(Result.getString("success").equals("1")){
						isProcessed = true;
						OrderIsProcessed();
						setResult(RESULT_OK);
						finish();
					}
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {

				ErrorMessage.setText("");
				ErrorMessage.setVisibility(View.VISIBLE);
				for(int i=0; i<MessageError.size(); i++){
					ErrorMessage.setText(ErrorMessage.getText() + MessageError.get(i));
					if((i+1)<MessageError.size())
						ErrorMessage.setText(ErrorMessage.getText() + "\n");
				}
			}
		});
	}


	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		ShippingDate.setText(cday + "/" + cmonth + "/" + cyear);
		Log.i("hari", Integer.toString(cday));
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this,pickerListener , cyear, cmonth, cday);
		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = checkNumber(selectedYear);
			month = checkNumber(selectedMonth + 1);
			day = checkNumber(selectedDay);

			ShippingDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
		}
	};

	public String checkNumber(int number) {
		return number<=9?"0"+number:String.valueOf(number);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK && data != null) {
				String contents = data.getStringExtra("SCAN_RESULT");
				//		         String format = data.getStringExtra("SCAN_RESULT_FORMAT");
				ReferenceNumber.setText(contents);
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		} else {
			setResult(RESULT_OK);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void OrderIsProcessed(){
		CancelButton.setVisibility(View.GONE);
		ConfirmButton.setText(context.getString(R.string.title_order_processed));
		ConfirmButton.setOnClickListener(null);
	}

	private void getSpinnerValue() {
		final LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.SHIPPING);
		LocalCacheHandler temp;
		CommonUtils.dumper(cache.getArrayListString(TkpdCache.Key.SHIPPING_NAME).size());

		if(cache.getArrayListString(TkpdCache.Key.SHIPPING_NAME).size() > 0 && !cache.isExpired()){
			CommonUtils.dumper("Cache Shipping");
			agencyName = cache.getArrayListString(TkpdCache.Key.SHIPPING_NAME);
			AllAgencyName = cache.getArrayListString(TkpdCache.Key.ALL_SHIPPING_NAME);
			AllAgencyID = cache.getArrayListString(TkpdCache.Key.ALL_SHIPPING_ID);

			ArrayList<String> shippingID = cache.getArrayListString(TkpdCache.Key.SHIPPING_ID); // make global
			ArrayList<String> tempService = null;
			ArrayList<String> tempServiceID = null;

			for(int i = 0; i < agencyName.size()-1; i++){
				CommonUtils.dumper("SERVICE-"+i);
				temp = new LocalCacheHandler(this, TkpdCache.SERVICE+i);
				tempService = temp.getArrayListString(TkpdCache.Key.SERVICE_NAME);
				tempServiceID = temp.getArrayListString(TkpdCache.Key.SERVICE_ID);
				hashMapServiceName.put(agencyName.get(i+1).toString(), tempService);
				hashMapServiceID.put(agencyName.get(i+1).toString(), tempServiceID);
			}

			setShippingSpinner(agencyName);

			loadingSpinner.setVisibility(View.GONE);
			if(switchCourier.isChecked()){
				layout.setVisibility(View.VISIBLE);
			}

		} else {

			NetworkHandler network = new NetworkHandler(context, TkpdUrl.MY_SHOP_ORDER);
			network.AddParam("act", "get_edit_shipping_form");
			network.AddParam("order_id", OrderId);
			network.Commit(new NetworkHandlerListener() {

				@Override
				public void onSuccess(Boolean status) {
					loadingSpinner.setVisibility(View.GONE);
					if(switchCourier.isChecked()){
						layout.setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void getResponse(JSONObject Result) {

					JSONArray allKurirJSON;
					JSONObject shippingData;
					JSONObject serviceData;
					JSONObject jResult;
					JSONObject shippingAgency = null;
					JSONObject serviceAgency;

					LocalCacheHandler cacheService;

					try {

						CommonUtils.dumper(Result);

						allKurirJSON = new JSONArray(Result.getString("all_kurir"));
						for(int i=0;i<allKurirJSON.length();i++){
							JSONObject c = allKurirJSON.getJSONObject(i);
							AllAgencyName.add(c.getString("name"));
							AllAgencyID.add(c.getString("id"));
							AllKurir.put(c.getString("name"), c.getString("id"));
						}

						jResult = new JSONObject(Result.getString("avail_kurir"));

						agencyName.add(getString(R.string.item_choose_agent));
						hashMapServiceName.put(agencyName.get(0), agencyService);
						hashMapServiceID.put(agencyName.get(0), agencyServiceID);

						Iterator<String> iterator = jResult.keys();
						for(int i=0; i<jResult.length();i++) {
							if(iterator.hasNext()){
								String name = iterator.next();

								shippingAgency = new JSONObject(jResult.getString(name));

								agencyName.add(shippingAgency.getString("shipping_name"));
								serviceData = new JSONObject(shippingAgency.getString("product"));

								Iterator<String> iterator2 = serviceData.keys();
								agencyService = new ArrayList<String>();
								agencyServiceID = new ArrayList<String>();

								agencyService.add(getString(R.string.item_choose_option));
								agencyServiceID.add("");

								CommonUtils.dumper(serviceData.length() + " / " +agencyName.size());
								for(int j=0; j<serviceData.length();j++){
									if(iterator2.hasNext()){
										String name2 = iterator2.next();
										serviceAgency = new JSONObject(serviceData.getString(name2));
										agencyServiceID.add(name2);
										agencyService.add(serviceAgency.getString("sp_name"));
									}
								}

								cacheService = new LocalCacheHandler(context, TkpdCache.SERVICE+i);
								CommonUtils.dumper(TkpdCache.SERVICE+i);
								cacheService.putArrayListString(TkpdCache.Key.SERVICE_NAME, agencyService);
								cacheService.putArrayListString(TkpdCache.Key.SERVICE_ID, agencyServiceID);
								cacheService.setExpire(3600);
								cacheService.applyEditor();

								hashMapServiceName.put(shippingAgency.getString(TkpdCache.Key.SHIPPING_NAME), agencyService);
								hashMapServiceID.put(shippingAgency.getString(TkpdCache.Key.SHIPPING_ID), agencyServiceID);
							}
						}
						cache.putArrayListString(TkpdCache.Key.ALL_SHIPPING_NAME, AllAgencyName);
						cache.putArrayListString(TkpdCache.Key.ALL_SHIPPING_ID, AllAgencyID);
						cache.putArrayListString(TkpdCache.Key.SHIPPING_NAME, agencyName);
						cache.setExpire(3600);
						cache.applyEditor();

						setShippingSpinner(agencyName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void getMessageError(ArrayList<String> MessageError) {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	private void setShippingSpinner(ArrayList<String> agencyName) {

		ArrayList<String> arrayTemp = new ArrayList<String>();

		for(int i = 0; i<agencyName.size(); i++){
			if(agencyName.get(i).toString().equals(current_shipping)){
				if(hashMapServiceName.get(agencyName.get(i)).size() > 2) {
					arrayTemp.add(agencyName.get(i).toString());
				}
			} else
				arrayTemp.add(agencyName.get(i).toString());
		}

		adapterAgency = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayTemp);
		adapterAgency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAgency.setAdapter(adapterAgency);
		spinnerAgency.setSelection(0);
	}

	private void setServiceSpinner(int position){
		ArrayList<String> arrayService = new ArrayList<String>();
		ArrayList<String> arrayTempServiceName = hashMapServiceName.get(spinnerAgency.getItemAtPosition(position).toString());
		ArrayList<String> arrayTempServiceID = hashMapServiceID.get(spinnerAgency.getItemAtPosition(position).toString());
		agencyServiceID = new ArrayList<String>();

		/**
		 * TODO Validasi untuk tidak menampilkan current_service
		 */
		for(int i = 0; i < arrayTempServiceName.size(); i++){
			if(!(arrayTempServiceName.get(i).toString().equals(current_service) && spinnerAgency.getItemAtPosition(position).toString().equals(current_shipping))){
				arrayService.add(arrayTempServiceName.get(i).toString());
				agencyServiceID.add(arrayTempServiceID.get(i).toString());
			}
		}

		adapterService = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayService);
		adapterService.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerService.setAdapter(adapterService);
		spinnerService.setSelection(0);
	}

	public static final ArrayList<ShippingConfirmDetModel.Data> convertFromOldDatas(
			ArrayList<String> ImageUrlList, ArrayList<String> NameList, ArrayList<String> PriceList, ArrayList<String> TtlOrderList,
			ArrayList<String> TtlPriceList, ArrayList<String> MessageList, ArrayList<String> ProductUrlList, ArrayList<String> ProductIdList
	) {
		ArrayList<ShippingConfirmDetModel.Data> dataHistories = new ArrayList<>();
		for (int i = 0; i < ImageUrlList.size(); i++) {
			ShippingConfirmDetModel.Data data = new ShippingConfirmDetModel.Data();
			data.ImageUrlList = ImageUrlList.get(i);
			data.NameList = NameList.get(i);
			data.PriceList = PriceList.get(i);
			data.TtlOrderList = TtlOrderList.get(i);
			data.TtlPriceList = TtlPriceList.get(i);
			data.MessageList = MessageList.get(i);
			data.ProductUrlList = ProductUrlList.get(i);
			data.ProductIdList = ProductIdList.get(i);

			dataHistories.add(data);
		}
		return dataHistories;
	}

}