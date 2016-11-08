package com.tokopedia.core;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.Bank_Table;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.util.AppUtils;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PeopleBankForm extends TActivity {
	Pattern p = Pattern.compile("[^a-zA-Z\\s]");
	private EditText AccountName;
	private EditText BankBranch;
	private EditText AccountNumber;
	private EditText Password;
	private TextView SaveBut;
	private TextView ErrorMessage;
	private TextView ChooseBank;
//	private String AccId;
	private TkpdProgressDialog mProgressDialog;
//	private DBHandler db;
	private String Query = "";
	private String currBankName = "";
	private String currBankID = "";
	private ArrayList<String> ClearingCode = new ArrayList<String>();
	private ArrayList<String> BankList = new ArrayList<String>();
	private ArrayList<String> BankName = new ArrayList<String>();
	private ArrayList<String> BankID = new ArrayList<String>();
	private TextView SendOTP;
	private EditText CodeOTP;
	private LocalCacheHandler handler;
	private static int EXPIRE_TIME = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_people_bank_form);

		SaveBut = (TextView) findViewById(R.id.save_button);
		AccountName = (EditText) findViewById(R.id.account_name);
		ChooseBank = (TextView) findViewById(R.id.bank_name);
		AccountNumber = (EditText) findViewById(R.id.account_number);
		BankBranch = (EditText) findViewById(R.id.bank_branch);
		Password = (EditText) findViewById(R.id.password);
		ErrorMessage = (TextView) findViewById(R.id.error_message);
		SendOTP = (TextView) findViewById(R.id.send_otp);
		CodeOTP = (EditText) findViewById(R.id.otp);
		ErrorMessage.setVisibility(View.INVISIBLE);
		if(getIntent().getExtras().getString("bank_id") != null)
			GetBankAccount();
		if (getIntent().getIntExtra("is_verified_phone", -1) == 0) {
			SendOTP.setText(getString(R.string.title_otp_email));
		} else {
			SendOTP.setText(getString(R.string.title_otp_phone));
		}
		handler = new LocalCacheHandler(getBaseContext(),"SEND_OTP_BANK");
		SendOTP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(handler.isExpired()){
					CodeOTP.setEnabled(true);
					CodeOTP.requestFocus();
					AppUtils.sendOTP(PeopleBankForm.this);
					handler.setExpire(EXPIRE_TIME);
				}
				else{
					Long diff = System.currentTimeMillis()/1000 - handler.getLong("timestamp");
					int interval = handler.getInt("expired_time");
					int remainingTime = interval - diff.intValue();
					CommonUtils.UniversalToast(getBaseContext(), "Silahkan coba "+remainingTime +" detik lagi");
				}
			}
		});
		SaveBut.setVisibility(View.GONE);
		SaveBut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		ChooseBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				ShowDialog();
			}
		});
	}
	String temp = "";

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//CommonUtils.dumper("isRunning : backpressed called");
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void ShowDialog() {
		BankName.clear();
		ClearingCode.clear();
		BankID.clear();
		BankList.clear();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		LayoutInflater li = LayoutInflater.from(this);
		final View promptsView = li.inflate(R.layout.choose_bank_dialog, null);
		alertDialogBuilder.setView(promptsView);

		final ListView lvBank = (ListView) promptsView.findViewById(R.id.lv_bank);
		//final EditText Search = (EditText) promptsView.findViewById(R.shopId.search);
		final SearchView Search = (SearchView) promptsView.findViewById(R.id.search);
		Search.setIconified(false);
		Search.setIconifiedByDefault(false);
		Search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				Query = query;
				BankName.clear();
				ClearingCode.clear();
				BankID.clear();
				BankList.clear();
				List<Bank> banks = new Select().from(Bank.class)
						.where(Bank_Table.bank_name.like("%"+Query+"%"))
						.queryList();
				BankName = new ArrayList<String>();
				ClearingCode = new ArrayList<String>();
				BankID = new ArrayList<String>();
				for(Bank bank : banks){
					BankName.add(bank.getBankName());
					ClearingCode.add(bank.getBankClearingCode());
					BankID.add(bank.getBankId());
				}

				for (int i = 0; i < BankName.size(); i++) {
					temp = ClearingCode.get(i)+"";
					if(temp.equals("null"))
						temp = "";
					//BankList.add(BankName.get(i)+" "+temp);
					BankList.add(BankName.get(i));
				}

				if(BankList.size() == 0){
					BankList.add(getString(R.string.error_bank_not_found));
				}

				ArrayAdapter<String> adapter  = new ArrayAdapter<String>(PeopleBankForm.this, android.R.layout.simple_list_item_1, BankList);
				lvBank.setAdapter(adapter);

				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if(newText.length()==0){
					BankName.clear();
					ClearingCode.clear();
					BankID.clear();
					BankList.clear();
					Query = "";

					List<Bank> banks = DbManagerImpl.getInstance().getBankBasedOnText(Query);
					BankName = new ArrayList<String>();
					ClearingCode = new ArrayList<String>();
					BankID = new ArrayList<String>();
					for(Bank bank : banks){
						BankName.add(bank.getBankName());
						ClearingCode.add(bank.getBankClearingCode());
						BankID.add(bank.getBankId());
					}

					for (int i = 0; i < BankName.size(); i++) {
						temp = ClearingCode.get(i)+"";
						if(temp.equals("null"))
							temp = "";
						//BankList.add(BankName.get(i)+" "+temp);
						BankList.add(BankName.get(i));
					}
					if(BankList.size() == 0){
						BankList.add(getString(R.string.error_bank_not_found));
					}


					ArrayAdapter<String> adapter  = new ArrayAdapter<String>(PeopleBankForm.this, android.R.layout.simple_list_item_1, BankList);
					lvBank.setAdapter(adapter);
				}
				return false;
			}
		});

		List<Bank> banks = DbManagerImpl.getInstance().getBankBasedOnText(Query);
		BankName = new ArrayList<String>();
		ClearingCode = new ArrayList<String>();
		BankID = new ArrayList<String>();
		for(Bank bank : banks){
			BankName.add(bank.getBankName());
			ClearingCode.add(bank.getBankClearingCode());
			BankID.add(bank.getBankId());
		}
		System.out.println(ClearingCode.toString());
		for (int i = 0; i < BankName.size(); i++) {
			temp = ClearingCode.get(i) + "";
			if(temp.equals("null"))
				temp = "";
			//BankList.add(BankName.get(i)+" "+temp);
			BankList.add(BankName.get(i));
		}

		ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, BankList);
		lvBank.setAdapter(adapter);


		final AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.show();

		lvBank.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				currBankID = BankID.get(position);
				currBankName = BankName.get(position);
				ChooseBank.setText(currBankName);
				alertDialog.dismiss();
			}
		});
	}




	private boolean Validate(){
		boolean valid = true;
		AccountName.setError(null);
		AccountNumber.setError(null);
		BankBranch.setError(null);
		Password.setError(null);

		if(AccountName.getText().toString().length()==0){
			AccountName.setError(getString(R.string.error_field_required));
			AccountName.requestFocus();
			valid = false;
		}else if(p.matcher(AccountName.getText().toString()).find()){
			AccountName.setError(getString(R.string.error_invalid_name));
			AccountName.requestFocus();
			valid = false;
		}

		if(AccountNumber.getText().toString().length()==0){
			AccountNumber.setError(getString(R.string.error_field_required));
			AccountNumber.requestFocus();
			valid = false;
		}

		if(!currBankID.equals("71"))
			if(BankBranch.getText().toString().length()==0){
				BankBranch.setError(getString(R.string.error_field_required));
				BankBranch.requestFocus();
				valid = false;
			}
		if(currBankID == ""){
			Toast.makeText(this, getText(R.string.error_bank_not_selected), Toast.LENGTH_SHORT).show();
			valid = false;
		}

		if (CodeOTP.length() == 0) {
			CodeOTP.setError(getString(R.string.error_field_required));
			CodeOTP.requestFocus();
			valid = false;
		} else if (CodeOTP.length() < 6 || CodeOTP.length() > 6) {
			CodeOTP.setError(getString(R.string.title_error_otp));
			CodeOTP.requestFocus();
			valid = false;
		}

		if(Password.length() == 0){
			Password.setError(getString(R.string.error_field_required));
			Password.requestFocus();
			valid = false;
		}

		return valid;
	}

	private void GetBankAccount(){
		AccountName.setText(getIntent().getExtras().getString("acc_name"));
		AccountNumber.setText(getIntent().getExtras().getString("acc_no"));
		BankBranch.setText(getIntent().getExtras().getString("branch"));
//		AccId = getIntent().getExtras().getString("acc_id");
		currBankName = getIntent().getExtras().getString("bank_name");
		ChooseBank.setText(currBankName);
		List<Bank> banks = DbManagerImpl.getInstance().getBankBasedOnText(Query);
		BankName = new ArrayList<String>();
		ClearingCode = new ArrayList<String>();
		BankID = new ArrayList<String>();
		for(Bank bank : banks){
			BankName.add(bank.getBankName());
			ClearingCode.add(bank.getBankClearingCode());
			BankID.add(bank.getBankId());
		}
		currBankID = BankID.get(BankID.indexOf(getIntent().getExtras().getString("bank_id")));
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				onBackPressed();
				return true;
			case R2.id.action_save:
				ErrorMessage.setVisibility(View.INVISIBLE);
				if(Validate()){
					mProgressDialog = new TkpdProgressDialog(PeopleBankForm.this, TkpdProgressDialog.NORMAL_PROGRESS);
					mProgressDialog.showDialog();
					NetworkHandler network = new NetworkHandler(PeopleBankForm.this, TkpdUrl.GET_PEOPLE);
					if(getIntent().getExtras().getString("bank_id") !=null){
						network.AddParam("act", "edit_bank_account");
						network.AddParam("acc_id", getIntent().getExtras().getString("acc_id"));
					}
					else {
						network.AddParam("act", "add_bank_account");
					}
					network.AddParam("acc_name", AccountName.getText().toString());
					network.AddParam("acc_no", AccountNumber.getText().toString());
					network.AddParam("bank_name_sel", currBankName);
					network.AddParam("bank_id", currBankID);
					network.AddParam("branch", BankBranch.getText().toString());
					network.AddParam("user_pwd", Password.getText().toString());
					network.AddParam("otp_code", CodeOTP.getText().toString());
					final Intent intent = new Intent();
					final Bundle bundle = new Bundle();
					bundle.putString("acc_name", AccountName.getText().toString());
					bundle.putString("acc_no", AccountNumber.getText().toString());
					bundle.putString("branch", BankBranch.getText().toString());
					bundle.putString("bank_name", currBankName);
					bundle.putString("bank_id", currBankID);
					// butuh alamat icon juga ya-----------------------------------------------------------


					network.Commit(new NetworkHandlerListener() {

						@Override
						public void onSuccess(Boolean status) {
							
							mProgressDialog.dismiss();
						}

						@Override
						public void getResponse(JSONObject Result) {
							
							try {
								if(!Result.getString("success").equals("0")){
									if(getIntent().getExtras()==null)
										bundle.putString("acc_id", Result.getString("acc_id"));
									bundle.putString("icon_uri", Result.getString("icon_uri"));
									intent.putExtras(bundle);
									setResult(RESULT_OK, intent);
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
								CommonUtils.dumper("NISTAG : MessageError Address" + MessageError.toString());
								if(MessageError.get(i).equals("Kata Sandi tidak benar.")){
									Password.setError(getString(R.string.error_incorrect_password));
									Password.requestFocus();
								}
								if(MessageError.get(i).equals(getString(R.string.title_error_otp))
										|| MessageError.get(i).equals(getString(R.string.title_mismatch_otp))){
									CodeOTP.setError(getString(R.string.title_error_otp));
									CodeOTP.requestFocus();
								}
							}

						}
					});

				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_save_button, menu);
		return true;
	}


}