package com.tokopedia.tkpd;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.ui.utilities.NoResultHandler;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tkpd.library.utils.data.DataManager;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.customadapter.ListViewManagePeopleBank;
import com.tokopedia.tkpd.customadapter.ListViewManagePeopleBank.ManagePeopleBankInterface;
import com.tokopedia.tkpd.database.model.Bank;
import com.tokopedia.tkpd.database.model.CategoryDB;
import com.tokopedia.tkpd.database.model.City;
import com.tokopedia.tkpd.database.model.District;
import com.tokopedia.tkpd.database.model.Province;
import com.tokopedia.tkpd.home.ParentIndexHome;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class ManagePeopleBank extends TActivity implements ManagePeopleBankInterface{

	private ArrayList<String> AccountNameList = new ArrayList<String>();
	private ArrayList<String> AccountNumberList = new ArrayList<String>();
	private ArrayList<String> BankNameList = new ArrayList<String>();
	private ArrayList<String> BankBranchList = new ArrayList<String>();
	private ArrayList<String> BankIconUriList = new ArrayList<String>();
	private ArrayList<String> BankIdList = new ArrayList<String>();
	private ArrayList<String> BankCodeIdList = new ArrayList<String>();
	
//	private TextView AddBank;
	private ListView ListViewBank;
	private NoResultHandler noResult;
	private ListViewManagePeopleBank BankAdapter;
	private int isVerified = 0;
	private Boolean isLoaded = false;
	
	private TkpdProgressDialog mProgressDialog;
	private TkpdProgressDialog MainProgress;
	LocalCacheHandler bankCache;
	DataManager dataManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflateView(R.layout.activity_manage_people_bank);

//		AddBank = (TextView) findViewById(R.shopId.add_bank);
		noResult = new NoResultHandler(getWindow().getDecorView().getRootView());
		ListViewBank = (ListView) findViewById(R.id.bank_list);
		BankAdapter = new ListViewManagePeopleBank(ManagePeopleBank.this, AccountNameList, AccountNumberList, BankNameList, BankBranchList, BankIconUriList);
		ListViewBank.setAdapter(BankAdapter);
		bankCache = new LocalCacheHandler(this, ParentIndexHome.FETCH_BANK);
		dataManager = DataManagerImpl.getDataManager();

		GetBank();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	onBackPressed();
	        return true;
	    case R2.id.action_add_bank :
	    	if(BankNameList.size()<10){
		    	Intent intent = new Intent(ManagePeopleBank.this, PeopleBankForm.class);
		    	intent.putExtra("is_verified_phone", isVerified);
				startActivityForResult(intent, 999);
	    	}
	    	else
	    		Toast.makeText(ManagePeopleBank.this, getString(R.string.error_max_account), Toast.LENGTH_SHORT).show();
			return true;
	    }
		
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		noResult.removeMessage();
		if(resultCode == RESULT_OK){
			AccountNameList.clear();
			AccountNumberList.clear();
			BankNameList.clear();
			BankBranchList.clear();
			BankIconUriList.clear();
			BankIdList.clear();
			BankCodeIdList.clear();
			BankAdapter.notifyDataSetChanged();
			GetBank();
			
		}
	}
	
	public void Loading(int i){
		mProgressDialog = new TkpdProgressDialog(this, i);
		mProgressDialog.showDialog();
	}
	@Override
	public void EditBank(int position){
		Intent intent = new Intent(ManagePeopleBank.this, PeopleBankForm.class);
		Bundle bundle = new Bundle();
		bundle.putString("acc_name", AccountNameList.get(position));
		bundle.putString("acc_no", AccountNumberList.get(position));
		bundle.putString("branch", BankBranchList.get(position));
		bundle.putString("acc_id", BankIdList.get(position));
		bundle.putString("bank_name", BankNameList.get(position));
		bundle.putString("bank_id", BankCodeIdList.get(position));
		bundle.putString("icon_uri", BankIconUriList.get(position));
		intent.putExtra("is_verified_phone", isVerified);
		intent.putExtras(bundle);
		startActivityForResult(intent, position);
	}
	@Override
	public void DeleteBank(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(ManagePeopleBank.this);
		builder
		.setMessage(getString(R.string.title_delete_bank) + " " + AccountNameList.get(position) + " ?")
		.setCancelable(true)
		.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity

				Loading(TkpdProgressDialog.NORMAL_PROGRESS);
				NetworkHandler network = new NetworkHandler(ManagePeopleBank.this, TkpdUrl.GET_PEOPLE);
				network.AddParam("act", "delete_bank_account");
				network.AddParam("acc_id", BankIdList.get(position));
				network.Commit(new NetworkHandlerListener() {
					
					@Override
					public void onSuccess(Boolean status) {

						mProgressDialog.dismiss();
					}
					
					@Override
					public void getResponse(JSONObject Result) {

						AccountNameList.remove(position);
						AccountNumberList.remove(position);
						BankNameList.remove(position);
						BankBranchList.remove(position);
						BankCodeIdList.remove(position);
						BankIconUriList.remove(position);
						BankIdList.remove(position);
						BankAdapter.notifyDataSetChanged();
						if(AccountNameList.size()==0)
							//MsgNoBank.setVisibility(View.VISIBLE);
							noResult.showMessage();
					}
					
					@Override
					public void getMessageError(ArrayList<String> MessageError) {

						System.out.println(MessageError);
					}
				});
			}
		  })
		.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing

				dialog.cancel();
			}
		});
		
		Dialog alertDialog = builder.create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.show();
	}
	@Override
	public void DefaultBank(final int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(ManagePeopleBank.this);
		builder
		.setMessage(getString(R.string.dialog_default_bank_1)+ " " + AccountNameList.get(position) + " " + getString(R.string.dialog_default_bank_2))
		.setCancelable(true)
		.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				Loading(TkpdProgressDialog.NORMAL_PROGRESS);
				NetworkHandler network = new NetworkHandler(ManagePeopleBank.this, TkpdUrl.GET_PEOPLE);
				network.AddParam("act", "set_default_account");
				network.AddParam("acc_id", BankIdList.get(position));
				network.AddParam("owner_id", SessionHandler.getLoginID(ManagePeopleBank.this));
				network.Commit(new NetworkHandlerListener() {
					
					@Override
					public void onSuccess(Boolean status) {

						mProgressDialog.dismiss();
						Toast.makeText(ManagePeopleBank.this, getText(R.string.title_success_default_bank), Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void getResponse(JSONObject Result) {

						Collections.swap(AccountNameList, 0, position);
						Collections.swap(AccountNumberList, 0, position);
						Collections.swap(BankNameList, 0, position);
						Collections.swap(BankCodeIdList, 0, position);
						Collections.swap(BankIconUriList, 0, position);
						Collections.swap(BankIdList, 0, position);
						Collections.swap(BankBranchList, 0, position);
						BankAdapter.notifyDataSetChanged();
					}
					
					@Override
					public void getMessageError(ArrayList<String> MessageError) {

						
					}
				});
			}
		  })
		.setNegativeButton(R.string.No,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				
				dialog.cancel();
			}
		});
		
		Dialog alertDialog = builder.create();
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		alertDialog.show();
		
		
	}
	
	public void GetBank(){
		MainProgress = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
		MainProgress.setLoadingViewId(R.id.include_loading);
		MainProgress.showDialog();

		if (bankCache.isExpired() || isListBankEmpty()) {
			dataManager.getListBank(this, new DataReceiver() {
				@Override
				public CompositeSubscription getSubscription() {
					return new CompositeSubscription();
				}

				@Override
				public void setDistricts(List<District> districts) {

				}

				@Override
				public void setCities(List<City> cities) {

				}

				@Override
				public void setProvinces(List<Province> provinces) {

				}

				@Override
				public void setBank(List<Bank> banks) {
					LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), ParentIndexHome.FETCH_BANK);
					cache.setExpire(86400);
					cache.applyEditor();
					getBankAccount();
				}

				@Override
				public void setDepartments(List<CategoryDB> departments) {

				}

				@Override
				public void setShippingCity(List<District> districts) {

				}

				@Override
				public void onNetworkError(String message) {
					MainProgress.dismiss();

					SnackbarManager.make(ManagePeopleBank.this,
							message,
							Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_retry),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									GetBank();
								}
							})
							.show();
				}

				@Override
				public void onMessageError(String message) {
					MainProgress.dismiss();
					SnackbarManager.make(ManagePeopleBank.this,
							message,
							Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_retry),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									GetBank();
								}
							})
							.show();
				}

				@Override
				public void onUnknownError(String message) {
					MainProgress.dismiss();
					SnackbarManager.make(ManagePeopleBank.this,
							message,
							Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_retry),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									GetBank();
								}
							})
							.show();
				}

				@Override
				public void onTimeout() {
					MainProgress.dismiss();
					SnackbarManager.make(ManagePeopleBank.this,
							getString(R.string.msg_connection_timeout),
							Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.title_retry),
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									GetBank();
								}
							})
							.show();
				}

				@Override
				public void onFailAuth() {
				}
			});
		} else {
			getBankAccount();
		}


	}

	private void getBankAccount() {
		NetworkHandler network = new NetworkHandler(ManagePeopleBank.this, TkpdUrl.GET_PEOPLE);
		network.AddParam("act", "get_bank_account");
		network.Commit(new NetworkHandlerListener() {

			@Override
			public void onSuccess(Boolean status) {

				MainProgress.dismiss();
				if (AccountNameList.size() == 0) {
					//MsgNoBank.setVisibility(View.VISIBLE);
					noResult.showMessage();
				}
				isLoaded = true;
				invalidateOptionsMenu();
			}

			@Override
			public void getResponse(JSONObject Result) {

				try {
					isVerified = Result.getInt("is_verified_phone");
					if (!Result.isNull("data")) {
						JSONArray DataList = new JSONArray(Result.getString("data"));
						JSONObject Data;
						for (int i = 0; i < DataList.length(); i++) {
							Data = new JSONObject(DataList.getString(i));

							AccountNameList.add(Data.getString("acc_name"));
							AccountNumberList.add(Data.getString("acc_no"));
							BankNameList.add(Data.getString("bank_name"));
							if (!Data.getString("branch").equals("null"))
								BankBranchList.add(Data.getString("branch"));
							else
								BankBranchList.add("");
							BankCodeIdList.add(Data.getString("bank_id"));

							if (!Data.isNull("bank_img"))
								BankIconUriList.add(Data.getString("bank_img"));
							else
								BankIconUriList.add("null");
							BankIdList.add(Data.getString("acc_id"));
							BankAdapter.notifyDataSetChanged();
						}

					}
				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			@Override
			public void getMessageError(ArrayList<String> MessageError) {


			}
		});
	}

	private boolean isListBankEmpty() {
		return new Select().from(Bank.class).count() == 0;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_people_bank, menu);
		MenuItem icon = menu.findItem(R.id.action_add_bank);
		icon.setVisible(false);
		icon.setEnabled(false);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem icon = menu.findItem(R.id.action_add_bank);
		if (isLoaded) {
			icon.setVisible(true);
			icon.setEnabled(true);
		} else {
			icon.setVisible(false);
			icon.setEnabled(false);
		}
		return true;
	}
}
