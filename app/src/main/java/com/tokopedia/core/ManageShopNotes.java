package com.tokopedia.core;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customView.SimpleListView;
import com.tokopedia.core.customadapter.ListViewManageShopNotes;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.MyShopNoteActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.note.model.modelDeleteNote.Data;
import com.tokopedia.core.prototype.ManageProductCache;
import com.tokopedia.core.prototype.ProductCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shopinfo.facades.GetShopNote;
import com.tokopedia.core.shopinfo.models.NoteModel;
import com.tokopedia.core.shopinfo.models.shopnotes.GetShopNotes;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ManageShopNotes extends TActivity {

    public static final String MANAGE_SHOP_NOTES = "Manage Shop Notes";
    public static final String STUART = "STUART";
    private SimpleListView ShopNotesListView;
    private ListViewManageShopNotes ShopNotesAdapter;
    private CoordinatorLayout rootView;
    private String ShopId;
    private ArrayList<String> ShopNoteStringList = new ArrayList<String>();
    private ArrayList<String> ShopNoteListId = new ArrayList<String>();
    private TkpdProgressDialog mProgressDialog;
    //	private NoResultHandler noResult;
    private String IsAllowShop = "0";
    private RefreshHandler Refresh;
    private View footerLV;
    private boolean containsReturnablePolicy;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    GlobalCacheManager globalCacheManager = new GlobalCacheManager();

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_CONFIG_S_INFO;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inflateView(R.layout.activity_manage_shop_notes);
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);

//		noResult = new NoResultHandler(getWindow().getDecorView().getRootView());
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        ShopId = SessionHandler.getShopID(ManageShopNotes.this);
        containsReturnablePolicy = false;
        // ErrorMessage = (TextView) findViewById(R.shopId.error_message);
        ShopNotesListView = (SimpleListView) findViewById(R.id.shop_notes);
        ShopNotesAdapter = new ListViewManageShopNotes(ManageShopNotes.this, ShopNoteListId, ShopNoteStringList, 1, ShopId, "", IsAllowShop);
        ShopNotesListView.setAdapter(ShopNotesAdapter);
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
        mProgressDialog.showDialog();
        footerLV = View.inflate(this, R.layout.footer_list_view, null);
        Refresh = new RefreshHandler(ManageShopNotes.this, getWindow().getDecorView().getRootView(), onRefreshListener());
        Refresh.setPullEnabled(false);
        CheckCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 4 for add 2 for edit
        if (resultCode == RESULT_OK) {
            ShopSettingCache.DeleteCache(ShopSettingCache.CODE_NOTES, ManageShopNotes.this);
            globalCacheManager.delete(TkpdCache.Key.SHOP_NOTE_LIST);
            globalCacheManager.delete(TkpdCache.Key.KEBIJAKAN_PENGEMBALIAN_PRODUK);
            if (requestCode == 4) {
                ShopNoteListId.add(data.getExtras().getString("note_id"));
                ShopNoteStringList.add(data.getExtras().getString("note_name"));
                ShopNotesAdapter.notifyDataSetChanged();
            } else if (requestCode != 4) {
                // ShopNoteListId.set(requestCode,
                // data.getExtras().getString("note_id"));
                ShopNoteStringList.set(requestCode, data.getExtras().getString("note_name"));
                ShopNotesAdapter.notifyDataSetChanged();
            }
            if (ShopNoteListId.size() > 0)
                // ErrorMessage.setVisibility(View.GONE);
                ShopNotesListView.removeNoResult();
            Log.i("Magic", ShopNoteListId.toString());
            containsReturnablePolicy = ShopNoteStringList.contains(getString(R.string.title_returnable_policy));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.manage_shop_notes, menu);
        getMenuInflater().inflate(R.menu.manage_people_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R2.id.action_add_notes:
                if (ShopNoteListId.size() < 3 || ShopNoteListId.size() < 4 && containsReturnablePolicy) {
                    Intent intent = new Intent(ManageShopNotes.this, ShopNoteForm.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", ShopId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 4);
                } else {
                    CommonUtils.UniversalToast(this, getString(R.string.error_max_notes));
                    // ErrorMessage.setVisibility(View.VISIBLE);
                    // ErrorMessage.setText(getString(R.string.error_max_notes));
                }
                return true;
            case R2.id.action_add_returnable_policy:
                if (!containsReturnablePolicy) {
                    Intent intent = new Intent(ManageShopNotes.this, ShopNoteForm.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", ShopId);
                    bundle.putBoolean("add_returnable", true);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 4);
                } else {
                    CommonUtils.UniversalToast(this, getString(R.string.returnable_policy_existed));
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void EditShopNotes(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("note_id", ShopNoteListId.get(position));
        bundle.putString("shop_id", SessionHandler.getShopID(ManageShopNotes.this));
        Intent intent = new Intent(ManageShopNotes.this, ShopNoteForm.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, position);
    }

    public void DeleteShopNotes(final int position) {
        final String noteToDelete = ShopNoteStringList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageShopNotes.this);

        builder.setMessage(
                getString(R.string.title_delete_notes) + " "
                        + Html.fromHtml(noteToDelete) + " ?")
                .setCancelable(true)
                .setPositiveButton(getString(R.string.title_yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                UnifyTracking.eventDeleteShopNotes();

                                mProgressDialog = new TkpdProgressDialog(
                                        ManageShopNotes.this,
                                        TkpdProgressDialog.NORMAL_PROGRESS);
                                mProgressDialog.showDialog();
                                ConfirmDeleteNotes(position, noteToDelete);
                            }
                        })
                .setNegativeButton(getString(R.string.title_no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

    }

    private HashMap<String, String> getNoteDeleteParams(int position) {
        HashMap<String, String> notesList = new HashMap<>();
        notesList.put("note_id", ShopNoteListId.get(position));
        notesList.put("shop_id", ShopId);
        return notesList;
    }

    private void ConfirmDeleteNotes(final int position, final String noteToDelete) {
        compositeSubscription.add(new MyShopNoteActService().getApi().deleteNote(AuthUtil.generateParams(this, getNoteDeleteParams(position)))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(STUART, MANAGE_SHOP_NOTES + "on error");
                                mProgressDialog.dismiss();
                                NetworkErrorHelper.showDialog(ManageShopNotes.this, null);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                Log.e(STUART, MANAGE_SHOP_NOTES + "on next");
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    if (!response.isError()) {
                                        Gson gson = new GsonBuilder().create();
                                        Data deleteNote = gson.fromJson(jsonObject.toString(), Data.class);

                                        if (deleteNote.getIsSuccess() != null && deleteNote.getIsSuccess() == 1) {
                                            ShopSettingCache.DeleteCache(ShopSettingCache.CODE_NOTES, ManageShopNotes.this);
                                            ShopNoteListId.remove(position);
                                            ShopNoteStringList.remove(position);
                                            ShopNotesAdapter.notifyDataSetChanged();
                                            containsReturnablePolicy = ShopNoteStringList.contains(getString(R.string.title_returnable_policy));
                                            invalidateOptionsMenu();
                                            clearCacheForReturnablePolicyUpdate(noteToDelete);
                                        }
                                    } else {
                                        onMessageError(response.getErrorMessages());
                                    }
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        }
                ));
    }

    public void onMessageError(List<String> MessageError) {
        if (MessageError == null || !(MessageError.size() > 0))
            return;

        NetworkErrorHelper.showSnackbar(this, MessageError.toString().replace("[", "").replace("]", ""));
    }

    private void CheckCache() {
        if (ShopSettingCache.getSetting(ShopSettingCache.CODE_NOTES, ManageShopNotes.this) != null) {
//			SetToUI(ShopSettingCache.getSetting(ShopSettingCache.CODE_NOTES, ManageShopNotes.this));
            ShopNotesAdapter.setAllow(IsAllowShop);
            mProgressDialog.dismiss();
        } else {
            // OLD
//			GetShopNotes(ShopId);
            // NEW
            getShopNotesList();
        }
        invalidateOptionsMenu();
        //containsReturnablePolicy = ShopNoteStringList.contains(getString(R.string.title_returnable_policy));
    }

    private void afterFetchingForSuccessOrNot(boolean networkAvailable) {
        mProgressDialog.dismiss();
        invalidateOptionsMenu();
        ShopNotesAdapter.setAllow(IsAllowShop);
        Refresh.finishRefresh();
        Refresh.setPullEnabled(true);
        NetworkErrorHelper.removeEmptyState(rootView);
        if (ShopNoteListId.size() == 0) {
            if (!networkAvailable) {
                ShopNotesListView.removeNoResult();
                NetworkErrorHelper.showEmptyState(this, rootView, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getShopNotesList();
                    }
                });
            } else {
                ShopNotesListView.addNoResult();
            }
        } else {
            ShopNotesListView.removeNoResult();
            if (!networkAvailable) {
                Refresh.setPullEnabled(false);
                NetworkErrorHelper.createSnackbarWithAction(ManageShopNotes.this, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getShopNotesList();
                    }
                }).showRetrySnackbar();
            }
        }
    }

    private void getShopNotesList() {
        GetShopNote getShopNote = new GetShopNote(this);
        getShopNote.setCompositeSubscription(compositeSubscription);
        getShopNote.setOnGetNoteListListener(new GetShopNote.OnGetNoteListListener() {
            @Override
            public void onCompleteDataSuccess(GetShopNotes.Data data) {
                ShopNoteStringList.clear();
                ShopNoteListId.clear();
                List<GetShopNotes.List> list = data.getList();
                IsAllowShop = data.getIsAllow() + "";

                for (int i = 0; i < list.size(); i++) {
                    ShopNoteStringList.add(list.get(i).getNoteTitle());
                    ShopNoteListId.add(list.get(i).getNoteId());
                }
                containsReturnablePolicy = ShopNoteStringList.contains(getString(R.string.title_returnable_policy));

                ShopNotesAdapter.notifyDataSetChanged();
                ShopNotesAdapter.setAllow(IsAllowShop);
                afterFetchingForSuccessOrNot(true);
                // TODO ShopSettingCache Need Caching
//				ShopSettingCache.SaveCache(ShopSettingCache.CODE_NOTES, Result.toString(), ManageShopNotes.this);
            }

            @Override
            public void onSuccess(List<NoteModel> notesList) {
                Log.d("STUART", ManageShopNotes.class.getSimpleName() + " didn't implemented in here!!");
            }

            @Override
            public void onFailure() {
                afterFetchingForSuccessOrNot(false);
            }
        });
        getShopNote.getNoteListV4(SessionHandler.getShopID(ManageShopNotes.this), "");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    private RefreshHandler.OnRefreshHandlerListener onRefreshListener() {
        return new RefreshHandler.OnRefreshHandlerListener() {
            @Override
            public void onRefresh(View view) {
                ShopNotesListView.addFooterView(footerLV);
                ShopNotesAdapter = new ListViewManageShopNotes(ManageShopNotes.this, ShopNoteListId, ShopNoteStringList, 1, ShopId, "", IsAllowShop);
                ShopNotesListView.setAdapter(ShopNotesAdapter);
                ShopNotesListView.removeFooterView(footerLV);
                // OLD
//				GetShopNotes(ShopId);
                // NEW
                getShopNotesList();

            }
        };
    }

    private void clearCacheForReturnablePolicyUpdate(String noteToDelete) {
        if (noteToDelete.equals(getString(R.string.title_returnable_policy))) {
            ManageProductCache manageProductCache = new ManageProductCache(ManageShopNotes.this);
            manageProductCache.ClearCache();
            ProductCache.ClearCache(ManageShopNotes.this);
            globalCacheManager.delete(TkpdCache.Key.SHOP_NOTE_LIST);
            globalCacheManager.delete(TkpdCache.Key.KEBIJAKAN_PENGEMBALIAN_PRODUK);
        }
    }

}