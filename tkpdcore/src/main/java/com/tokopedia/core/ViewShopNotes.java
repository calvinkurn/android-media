package com.tokopedia.core;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.shop.NotesService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.note.model.modelnote.Data;
import com.tokopedia.core.util.MethodChecker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.tokopedia.core2.R;

public class ViewShopNotes extends TActivity {
    public static final String STUART = "STUART";
    public static final String GET_SHOP_NOTES = "GetShopNotes";
    private String ShopId;
    private String NoteId;
    private TextView NoteName;
    //	private TextView ShopName;
    private TextView NoteDate;
    private TextView NoteContent;
    private View NoteLayout;
    private CoordinatorLayout rootView;
    private TkpdProgressDialog mProgress;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_NOTE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_view_shop_notes);

        rootView = (CoordinatorLayout) findViewById(R.id.root);
        NoteName = (TextView) findViewById(R.id.note_title);
        NoteLayout = (View) findViewById(R.id.note_layout);
        NoteDate = (TextView) findViewById(R.id.note_date);
//		ShopName = (TextView) findViewById(R.shopId.shop_name);
        NoteContent = (TextView) findViewById(R.id.note_content);
        Bundle bundle = getIntent().getExtras();
        ShopId = bundle.getString("shop_id");
        NoteId = bundle.getString("note_id");

        if (!bundle.getString("note_name").isEmpty()) {
            NoteName.setText(MethodChecker.fromHtml(bundle.getString("note_name")));
        } else {
            NoteName.setText(bundle.getString("note_name"));
        }
        getShopNotesV4(ShopId, "get_shop_note_detail", "GET", NoteId);
        mProgress = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        mProgress.setLoadingViewId(R.id.include_loading);
        mProgress.showDialog();
    }

    private HashMap<String, String> getNotesParams(String... params) {
        HashMap<String, String> notesList = new HashMap<>();
        notesList.put("shop_id", params[0]);
        notesList.put("act", params[1]);
        notesList.put("method", params[2]);
        notesList.put("note_id", params[3]);
        return notesList;
    }

    public void getShopNotesV4(String... params) {
        new NotesService().getApi().getNotesDetail(AuthUtil.generateParams(this, getNotesParams(params)))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                Log.e(STUART, GET_SHOP_NOTES + "completed");
                                mProgress.dismiss();
                                NoteLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(STUART, GET_SHOP_NOTES + "on error");
                                mProgress.dismiss();
                                NetworkErrorHelper.showEmptyState(ViewShopNotes.this, rootView, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        getShopNotesV4(ShopId, "get_shop_note_detail", "GET", NoteId);
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                Log.e(STUART, GET_SHOP_NOTES + "on next");
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    Data data = gson.fromJson(jsonObject.toString(), Data.class);
                                    NoteContent.setText(MethodChecker.fromHtml(data.getDetail().getNotesContent()));
                                    if (data.getDetail().getNotesUpdateTime() != null && data.getDetail().getNotesUpdateTime().length() < 5) {
                                        NoteDate.setText(getString(R.string.note_last_update) + " " + data.getDetail().getNotesCreateTime() + " WIB");
                                    } else {
                                        NoteDate.setText(getString(R.string.note_last_update) + " " + data.getDetail().getNotesUpdateTime() + " WIB");
                                    }
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        }
                );
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
