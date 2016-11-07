package com.tokopedia.tkpd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ConnectionDetector;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.facade.FacadeSendFormDialog;
import com.tokopedia.tkpd.fragment.ReturnPolicyDialog;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopNoteActService;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.prototype.ManageProductCache;
import com.tokopedia.tkpd.prototype.ProductCache;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.shopinfo.facades.GetShopNote;
import com.tokopedia.tkpd.shopinfo.models.NoteModel;
import com.tokopedia.tkpd.shopinfo.models.shopnotes.AddShopNotes;
import com.tokopedia.tkpd.shopinfo.models.shopnotes.EditShopNotes;
import com.tokopedia.tkpd.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ShopNoteForm extends TActivity {

    public static final String ADD_SHOP_NOTE = "add_shop_note";
    public static final String EDIT_SHOP_NOTE = "edit_shop_note";
    public static final String STUART = "STUART";
    private EditText NoteName;
    private EditText NoteContent;
    private CoordinatorLayout rootView;
    private String NoteId;
    private String ShopId;
    private TextView noteContentCount;
    private TextView SaveButton;
    private boolean IsNewNotes;
    private TkpdProgressDialog mProgressDialog;
    private boolean IsSaving = false;
    private View MainView;
    private final int maxNoteLength = 30000;
    private final int visibleNoteLength = 50;
    private boolean addReturnable;
    private ConnectionDetector connectionDetector;
    GetShopNote facadeShopNote;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    private FacadeSendFormDialog facadeSendFormDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_note_form);
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        IsNewNotes = false;
        addReturnable = false;
        connectionDetector = new ConnectionDetector(this);
        NoteName = (EditText) findViewById(R.id.note_name);
        NoteContent = (EditText) findViewById(R.id.note_content);
        MainView = (View) findViewById(R.id.main_view);
        noteContentCount = (TextView) findViewById(R.id.note_content_count);
        SaveButton = (TextView) findViewById(R.id.save_but);
        if (getIntent().getExtras().containsKey("add_returnable")) {
            initiateAddReturnablePolicy();
        }
        if (getIntent().getExtras().getString("note_id") != null) {
            NoteId = getIntent().getExtras().getString("note_id");

        } else IsNewNotes = true;
        if (!IsNewNotes) {
            ShopId = getIntent().getExtras().getString("shop_id");
            initFacade();
            facadeShopNote.setCompositeSubscription(compositeSubscription);
            facadeShopNote.getNoteDetail2(SessionHandler.getShopID(this), SessionHandler.getShopDomain(this), NoteId);
//			GetShopNotes("edit_shop_note", "GET", NoteId);

            mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
            mProgressDialog.setLoadingViewId(R.id.include_loading);
            mProgressDialog.showDialog();
        }
        if (connectionDetector.isConnectingToInternet()) {
            MainView.setVisibility(View.VISIBLE);
        } else {
            showEmptyState();
        }

        NoteContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int remainingLength = maxNoteLength - s.length();
                if (remainingLength <= visibleNoteLength) {
                    noteContentCount.setVisibility(View.VISIBLE);
                    noteContentCount.setText(String.valueOf(remainingLength));
                } else {
                    noteContentCount.setVisibility(View.INVISIBLE);
                }

            }
        });
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxNoteLength);
        NoteContent.setFilters(FilterArray);

        SaveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Validate() && !IsSaving) {
                    IsSaving = true;
                    mProgressDialog = new TkpdProgressDialog(ShopNoteForm.this, TkpdProgressDialog.NORMAL_PROGRESS);
                    if (IsNewNotes) {
                        AddShopNotes(ADD_SHOP_NOTE);
                    } else {
                        AddShopNotes(EDIT_SHOP_NOTE);
                    }
                }
            }
        });
    }

    private void showEmptyState() {
        NetworkErrorHelper.showEmptyState(this, rootView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                invalidateOptionsMenu();
                if (connectionDetector.isConnectingToInternet()) {
                    MainView.setVisibility(View.VISIBLE);
                } else {
                    showEmptyState();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    public boolean Validate() {
        boolean valid = true;
        NoteName.setError(null);
        NoteContent.setError(null);
        if (NoteName.getText().toString().trim().length() < 1) {
            NoteName.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if (NoteContent.getText().toString().trim().length() < 1) {
            NoteContent.setError(getString(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }

    private void addReturnablePolicy() {
        facadeSendFormDialog = FacadeSendFormDialog.createFacadeInstance(ShopNoteForm.this, ShopId, ReturnPolicyDialog.ADD_SHOP_NOTE_ACTION);
        facadeSendFormDialog.sendForm(NoteContent.getText().toString().replaceAll("\\n", "<br />"), onPolicyChanged());


    }

    private FacadeSendFormDialog.AddNewPolicyListener onPolicyChanged() {
        return new FacadeSendFormDialog.AddNewPolicyListener() {
            @Override
            public void OnSuccess(String NotesID) {
                mProgressDialog.dismiss();
                doneAddReturnablePolicy(NotesID);
                ProductCache.ClearCache(ShopNoteForm.this);
            }

            @Override
            public void OnFailure() {

            }
        };
    }

    public void AddShopNotes(String... params) {
        if (NoteContent.getText().toString().trim().length() > 6000)
            return;
        if (NoteName.getText().toString().trim().length() > 128)
            return;
        addOrEditShopNotes(params);
    }

    private HashMap<String, String> generateEditAndAddParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("note_content", NoteContent.getText().toString().replaceAll("\\n", "<br />"));
        param.put("note_id", NoteId);
        param.put("note_title", NoteName.getText().toString());
        param.put("terms", "");
        return param;

    }

    public void addOrEditShopNotes(final String... params) {
        mProgressDialog.showDialog();
        switch (params[0]) {
            case ADD_SHOP_NOTE:
                new MyShopNoteActService().getApi()
                        .addNote(AuthUtil.generateParams(this, generateEditAndAddParam()))
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<Response<TkpdResponse>>() {
                                    @Override
                                    public void onCompleted() {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                        Log.e(ShopNoteForm.class.getSimpleName(), e.getLocalizedMessage());
                                        NetworkErrorHelper.createSnackbarWithAction(ShopNoteForm.this, new NetworkErrorHelper.RetryClickedListener() {
                                            @Override
                                            public void onRetryClicked() {
                                                addOrEditShopNotes(params[0]);
                                            }
                                        }).showRetrySnackbar();
                                    }

                                    @Override
                                    public void onNext(Response<TkpdResponse> responseData) {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                        TkpdResponse response = responseData.body();
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response.getStringData());

                                            if (!response.isError()) {
                                                Gson gson = new GsonBuilder().create();
                                                AddShopNotes.Data data =
                                                        gson.fromJson(jsonObject.toString(), AddShopNotes.Data.class);

                                                if (data.getIsSuccess() == 1) {
                                                    Intent intent = new Intent();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("note_name", NoteName.getText().toString());
                                                    bundle.putString("note_id", data.getNoteId());
                                                    intent.putExtras(bundle);
                                                    setResult(RESULT_OK, intent);
                                                    ShopNoteForm.this.finish();
                                                }
                                            } else {
                                                onMessageError(response.getErrorMessages());
                                            }
                                        } catch (JSONException je) {
                                            Log.e(STUART, ShopNoteForm.class.getSimpleName() + je.getLocalizedMessage());
                                        }
                                    }
                                }
                        );
                break;
            case EDIT_SHOP_NOTE:
                new MyShopNoteActService().getApi().editNote(
                        AuthUtil.generateParams(this, generateEditAndAddParam())
                ).subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                new Subscriber<Response<TkpdResponse>>() {
                                    @Override
                                    public void onCompleted() {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                        Log.e(ShopNoteForm.class.getSimpleName(), e.getLocalizedMessage());
                                        NetworkErrorHelper.createSnackbarWithAction(ShopNoteForm.this, new NetworkErrorHelper.RetryClickedListener() {
                                            @Override
                                            public void onRetryClicked() {
                                                addOrEditShopNotes(params[0]);
                                            }
                                        }).showRetrySnackbar();
                                    }

                                    @Override
                                    public void onNext(Response<TkpdResponse> responseData) {
                                        mProgressDialog.dismiss();
                                        IsSaving = false;
                                        TkpdResponse response = responseData.body();
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response.getStringData());

                                            if (!response.isError()) {
                                                Gson gson = new GsonBuilder().create();
                                                EditShopNotes.Data data
                                                        = gson.fromJson(jsonObject.toString(), EditShopNotes.Data.class);
                                                if (data.getIsSuccess() == 1) {
                                                    Intent intent = new Intent();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("note_name", NoteName.getText().toString());
                                                    bundle.putString("note_id", NoteId);
                                                    intent.putExtras(bundle);
                                                    setResult(RESULT_OK, intent);
                                                    ShopNoteForm.this.finish();
                                                }
                                            } else {
                                                onMessageError(response.getErrorMessages());
                                            }
                                        } catch (JSONException je) {
                                            Log.e(STUART, ShopNoteForm.class.getSimpleName() + je.getLocalizedMessage());
                                        }
                                    }
                                }
                        );
                break;
        }
    }

    public void onMessageError(List<String> MessageError) {
        if (MessageError == null || !(MessageError.size() > 0))
            return;

        NetworkErrorHelper.showSnackbar(this, MessageError.toString().replace("[", "").replace("]", ""));
    }

    private void initFacade() {
        facadeShopNote = new GetShopNote(this);
        facadeShopNote.setOnGetNoteDetailListener(onGetNoteDetail());
    }

    private GetShopNote.OnGetNoteDetailListener onGetNoteDetail() {
        return new GetShopNote.OnGetNoteDetailListener() {
            @Override
            public void onSuccess(NoteModel model) {
                MainView.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();

                NoteContent.setText(Html.fromHtml(model.content));
                NoteName.setText((Html.fromHtml(model.title)));
                editTitleAllowance(Integer.parseInt(model.status));
            }

            @Override
            public void onFailure() {
//				MainView.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();
                NetworkErrorHelper.showEmptyState(ShopNoteForm.this, rootView, new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        initFacade();
                    }
                });
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_btn, menu);
        MenuItem item = menu.findItem(R.id.action_send);
        item.setTitle(getString(R.string.title_action_save_note));
        if (!connectionDetector.isConnectingToInternet()) {
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R2.id.action_send:
                if (Validate() && !IsSaving) {
                    IsSaving = true;
                    mProgressDialog = new TkpdProgressDialog(ShopNoteForm.this, TkpdProgressDialog.NORMAL_PROGRESS);
                    mProgressDialog.showDialog();
                    if (addReturnable) {
                        addReturnablePolicy();
                    } else {
                        if (IsNewNotes) {
                            AddShopNotes(ADD_SHOP_NOTE);
                        } else {
                            AddShopNotes(EDIT_SHOP_NOTE);
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doneAddReturnablePolicy(String NotesID) {
        ManageProductCache manageProductCache = new ManageProductCache(ShopNoteForm.this);
        manageProductCache.ClearCache();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        IsSaving = false;
        bundle.putString("note_name", NoteName.getText().toString());
        bundle.putString("note_id", NotesID);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initiateAddReturnablePolicy() {
        addReturnable = true;
        ShopId = getIntent().getExtras().getString("shop_id");
        NoteName.setText(getString(R.string.title_returnable_policy));
        NoteName.setEnabled(false);
        NoteName.setTextColor(getResources().getColor(R.color.grey_500));
    }

    private void editTitleAllowance(int status) {
        if (status == 2) {
            NoteName.setEnabled(false);
            NoteName.setTextColor(getResources().getColor(R.color.grey_500));
        }
    }
}
