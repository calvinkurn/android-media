package com.tokopedia.tkpd;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.ui.utilities.DatePickerUtil.onDateSelectedListener;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.NetworkHandler;
import com.tokopedia.tkpd.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopInfoActService;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopInfoService;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.prototype.ShopCache;
import com.tokopedia.tkpd.prototype.ShopSettingCache;
import com.tokopedia.tkpd.shop.model.shopinfo.ClosedDetail;
import com.tokopedia.tkpd.shop.model.shopinfo.Data;
import com.tokopedia.tkpd.shop.model.shopinfo.Image;
import com.tokopedia.tkpd.shop.model.shopinfo.Info;
import com.tokopedia.tkpd.shopinfo.models.shopinfo.UpdateShopInfo;
import com.tokopedia.tkpd.util.RequestPermissionUtil;
import com.tokopedia.tkpd.util.SessionHandler;
import com.tokopedia.tkpd.util.UploadImageReVamp;
import com.tokopedia.tkpd.var.TkpdState;
import com.tokopedia.tkpd.var.TkpdUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Response;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@RuntimePermissions
public class ShopEditor extends TActivity {
    public static final String STUART = "STUART";
    public static final String SHOP_EDITOR = "Shop Editor";

    private EditText mShopNameText;
    private EditText mShopSloganText;
    private EditText mShopDescText;
    private EditText mShopReasonText;
    private EditText mShopClosedNoteText;
    private EditText mShopClosedUntilText;
    private Spinner mShopStatusSpinner;
    private View mShopClosedDetail;
    private ScrollView mShopEditor;
    private ArrayAdapter<String> spinnerCountShopStatus;
    private ArrayAdapter<String> spinnerMod;
    private ArrayList<String> mShopStatus;
    private TkpdProgressDialog mProgressDialog;
    private ImageView mShopAva;
    private String mShopAvaUri;
    private ImageHandler ih = new ImageHandler();
    private boolean UploadingAvatar = false;
    private TextView mBtnSend;
    private ProgressBar progressBar;
    private CoordinatorLayout rootView;
    private DatePickerUtil datePicker;

    private String mShopName;
    private String mShopSlogan;
    private String mShopDesc;
    private String mShopClosedNote;
    private String mShopClosedUntil;
    private String IsAllowShop = "0";

    private UploadImageReVamp uploadimage2;
    private SimpleDateFormat simpleDateFormat;
    private String formatDate = "dd/MM/yyyy";
    private Calendar calendar;
    private int cyear;
    private int cmonth;
    private int cday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_editor);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mShopEditor = (ScrollView) findViewById(R.id.shop_editor_scrollview);
        mShopEditor.setVisibility(View.GONE);
        rootView = (CoordinatorLayout) findViewById(R.id.root);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mShopNameText = (EditText) findViewById(R.id.shop_name);
        mShopSloganText = (EditText) findViewById(R.id.shop_slogan);
        mShopDescText = (EditText) findViewById(R.id.shop_desc);
//		mShopClosedNoteText = (EditText)findViewById(R.id.shop_closed_note);
//		mShopClosedUntilText = (EditText)findViewById(R.id.shop_closed_until);
//		mShopStatusSpinner = (Spinner)findViewById(R.id.shop_status);
//		mShopClosedDetail = (View)findViewById(R.id.shop_closed_detail);
        mShopAva = (ImageView) findViewById(R.id.shop_ava);
        //mBtnUploadImg = (Button)findViewById(R.shopId.btn_shop_ava);
        mBtnSend = (TextView) findViewById(R.id.button_send);

        uploadimage2 = UploadImageReVamp.createInstance(this, "logo", UploadImageReVamp.WS_SHOP_EDITOR);
        uploadimage2.addParam("shop_id", SessionHandler.getShopID(this));
        uploadimage2.addParam("resolution", "215");
        uploadimage2.setOnUploadListener(new UploadImageReVamp.UploadImageListener() {
            @Override
            public void onSuccess(JSONObject result) {
                UploadingAvatar = false;
                actionUpdateShopImage(result.optString("pic_src"), result.optString("pic_code"));
                ShopCache.DeleteCache(SessionHandler.getShopID(ShopEditor.this), ShopEditor.this);
                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, ShopEditor.this);
                LocalCacheHandler Cache = new LocalCacheHandler(ShopEditor.this, TkpdState.CacheName.CACHE_USER);
                Cache.putString("shop_pic_uri", result.optString("pic_src"));
                Cache.applyEditor();
            }

            @Override
            public void onStart() {
                UploadingAvatar = true;
                mShopAva.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                UploadingAvatar = false;
                mShopAva.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        datePicker = new DatePickerUtil(this);
        simpleDateFormat = new SimpleDateFormat(formatDate);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, 1);
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.SetDate(cday, cmonth, cyear);
        datePicker.setMinDate(calendar.getTimeInMillis());
        mShopClosedUntilText.setText(simpleDateFormat.format(calendar.getTime()));

        spinnerCountShopStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.shop_status));
        spinnerMod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.shop_moderated));
        mShopStatusSpinner.setAdapter(spinnerCountShopStatus);

        //declare shop status
        mShopStatus = new ArrayList<String>();
        mShopStatus.add("1");
        mShopStatus.add("2");
        mShopStatus.add("3");
        mShopClosedUntilText.setInputType(InputType.TYPE_NULL);
        mShopClosedUntilText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.add(Calendar.DATE, 1);
                if (!mShopClosedUntilText.getText().toString().isEmpty()) {
                    calculateDate(mShopClosedUntilText.getText().toString());
                } else {
                    cday = calendar.get(Calendar.DAY_OF_MONTH);
                    cmonth = calendar.get(Calendar.MONTH);
                    cyear = calendar.get(Calendar.YEAR);
                }
                datePicker.SetDate(cday, cmonth, cyear);
                datePicker.setMinDate(calendar.getTimeInMillis());
                datePicker.DatePickerCalendarShopClose(new onDateSelectedListener() {

                    @Override
                    public void onDateSelected(int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        mShopClosedUntilText.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                });
//				IsStartDate = true;
//				GetStartDate();
                return false;
            }

        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UploadingAvatar) {
                    Toast.makeText(ShopEditor.this, getString(R.string.error_upload_gambar), Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mShopNameText.getWindowToken(), 0);
                doEditShop();

            }
        });

        mShopAva.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ShopEditor.this);
                        myAlertDialog.setMessage(getString(R.string.dialog_upload_option));
                        myAlertDialog.setPositiveButton(getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShopEditorPermissionsDispatcher.actionImagePickerWithCheck(ShopEditor.this);
                            }
                        });
                        myAlertDialog.setNegativeButton(getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ShopEditorPermissionsDispatcher.actionCameraWithCheck(ShopEditor.this);
                            }
                        });
                        Dialog dialog = myAlertDialog.create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                    }
                }
        );

        mShopStatusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 1) {
                    mShopClosedDetail.setVisibility(View.VISIBLE);
                } else {
                    mShopClosedDetail.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
        mProgressDialog.showDialog();
        CheckCache();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void actionCamera() {
        uploadimage2.actionCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        uploadimage2.actionImagePicker();

    }

    private void actionUpdateShopImage(final String url, String picCode) {
        NetworkHandler network = new NetworkHandler(this, TkpdUrl.SHOP_EDITOR);
        network.AddParam("act", "update_shop_picture");
        network.AddParam("new_add", "1");
        network.AddParam("pic_code", picCode);
        network.AddParam("pic_src", url);
        network.Commit(new NetworkHandlerListener() {
            @Override
            public void onSuccess(Boolean status) {
                ImageHandler.LoadImage(mShopAva, url);
                mShopAva.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void getResponse(JSONObject Result) {

            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        uploadimage2.onResult(requestCode, resultCode, data);
    }

    public String checkNumber(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    /**
     * all validation goes here
     */
    public void doEditShop() {
        boolean cancel = false;
        View focusView = null;

        mShopName = mShopNameText.getText().toString();
        mShopSlogan = mShopSloganText.getText().toString();
        mShopDesc = mShopDescText.getText().toString();
        mShopClosedNote = mShopClosedNoteText.getText().toString();
        mShopClosedUntil = mShopClosedUntilText.getText().toString();

        if (TextUtils.isEmpty(mShopName)) {
            mShopNameText.setError(getString(R.string.error_field_required));
            focusView = mShopNameText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mShopSlogan)) {
            mShopSloganText.setError(getString(R.string.error_field_required));
            focusView = mShopSloganText;
            cancel = true;
        }

        if (TextUtils.isEmpty(mShopDesc)) {
            mShopDescText.setError(getString(R.string.error_field_required));
            focusView = mShopDescText;
            cancel = true;
        }
        if (mShopStatus.get(mShopStatusSpinner.getSelectedItemPosition()).equals("2")) {
            if (TextUtils.isEmpty(mShopClosedNote)) {
                mShopClosedNoteText.setError(getString(R.string.error_field_required));
                focusView = mShopClosedNoteText;
                cancel = true;
            }
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            mProgressDialog.showDialog();
            postShopData();
        }
    }

    private HashMap<String, String> generateParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("closed_note", mShopClosedNote);
        param.put("closed_until", mShopClosedUntil);
        param.put("short_desc", mShopDesc);
        param.put("status", mShopStatus.get(mShopStatusSpinner.getSelectedItemPosition()));
        param.put("tag_line", mShopSlogan);
        param.put("user_id", SessionHandler.getLoginID(this));
        return param;
    }

    private void postShopData() {
        new MyShopInfoActService().getApi().updateInfo(AuthUtil.generateParams(this, generateParam()))
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
                                mProgressDialog.dismiss();
                                NetworkErrorHelper.showSnackbar(ShopEditor.this);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                mProgressDialog.dismiss();

                                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_SHOP_INFO, ShopEditor.this);
                                TkpdResponse response = responseData.body();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStrResponse());

                                    Gson gson = new GsonBuilder().create();
                                    UpdateShopInfo data =
                                            gson.fromJson(jsonObject.toString(), UpdateShopInfo.class);
                                    processShopDataAfterSuccess(data);

                                } catch (JSONException je) {
                                    Log.e(STUART, ShopEditor.class.getSimpleName() + je.getLocalizedMessage());
                                }
                            }
                        }
                );
    }

    private void processShopDataAfterSuccess(UpdateShopInfo updateShopInfo) {
        if (updateShopInfo.getMessageError() != null && updateShopInfo.getMessageError().size() > 0) {
            CommonUtils.ShowError(ShopEditor.this, new ArrayList<String>(updateShopInfo.getMessageError()));
            return;
        }

        if (updateShopInfo.getData().getIsSuccess() == 1) {
            ShopCache.DeleteCache(SessionHandler.getShopID(ShopEditor.this), ShopEditor.this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.title_success_update_shop), Toast.LENGTH_SHORT).show();

        }

        finish();
    }


    private void CheckCache() {
        if (ShopSettingCache.getSetting(ShopSettingCache.CODE_SHOP_INFO, ShopEditor.this) != null) {
            Gson gson = new GsonBuilder().create();
            Data data = gson.fromJson(ShopSettingCache.getSetting(ShopSettingCache.CODE_SHOP_INFO, ShopEditor.this).toString(), Data.class);
            SetToUI(data);
            mProgressDialog.dismiss();
        } else
            GetShopDataV4();
    }

    private void SetToUI(Data data) {
        IsAllowShop = data.getIsAllow().toString();
        Info info = data.getInfo();
        ClosedDetail closedDetail = data.getClosedDetail();
        mShopNameText.setText(Html.fromHtml(info.getShopName()));
        Image mShopAvaObj = data.getImage();
        if (mShopAvaObj.getLogo() != null)
            mShopAvaUri = mShopAvaObj.getLogo();
        if (IsAllowShop.equals("0")) {
            mBtnSend.setVisibility(View.GONE);
        } else {
            mBtnSend.setVisibility(View.VISIBLE);
        }
        mShopSloganText.setText(Html.fromHtml(info.getShopTagline()));
        mShopDescText.setText(Html.fromHtml(info.getShopDescription()));
        if (CommonUtils.checkNullForZeroJson(closedDetail.getNote())) {
            mShopClosedNoteText.setText(Html.fromHtml(closedDetail.getNote()));
        }
        if (CommonUtils.checkNullForZeroJson(closedDetail.getUntil())) {
            mShopClosedUntilText.setText(Html.fromHtml(closedDetail.getUntil()));
        }
        //Log.i("ini kok", mShopAvaUri);
        String mSpinnerVal = null;
        switch (info.getShopStatus()) {
            case 1:
                mSpinnerVal = getResources().getString(R.string.shop_open);
                break;
            case 2:
                mSpinnerVal = getResources().getString(R.string.shop_closed);
                break;
            case 3:
                mSpinnerVal = getResources().getString(R.string.shop_moderated);
                break;
        }
        mProgressDialog.dismiss();

        int spinnerPos = spinnerCountShopStatus.getPosition(mSpinnerVal);
        Log.i("My Spin Pos", Integer.toString(spinnerPos));
        Log.i("My Info Obj val", info.getShopStatus().toString());
        mShopStatusSpinner.setSelection(spinnerPos);
        mShopEditor.setVisibility(View.VISIBLE);
        CommonUtils.dumper("spinner val" + mSpinnerVal);
        ImageHandler.loadImage2(mShopAva, mShopAvaUri, R.drawable.ic_default_shop_ava);
        if (info.getShopStatus() == 1) {
            mShopClosedDetail.setVisibility(View.GONE);

        } else if (info.getShopStatus() == 2) {
            mShopClosedDetail.setVisibility(View.VISIBLE);
        } else {
            mShopStatusSpinner.setAdapter(spinnerMod);
            mShopClosedDetail.setVisibility(View.GONE);
            mShopStatusSpinner.setEnabled(false);
        }

        spinnerCountShopStatus.notifyDataSetChanged();
    }

    private void GetShopDataV4() {
        new MyShopInfoService().getApi().getInfo(AuthUtil.generateParams(this, new HashMap<String, String>()))
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
                                Log.e(STUART, SHOP_EDITOR + "on error");
                                mProgressDialog.dismiss();
                                NetworkErrorHelper.showEmptyState(ShopEditor.this, rootView, new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetShopDataV4();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                Log.e(STUART, SHOP_EDITOR + "on next");
                                TkpdResponse response = responseData.body();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

                                    Gson gson = new GsonBuilder().create();
                                    Data data = gson.fromJson(jsonObject.toString(), Data.class);
                                    SetToUI(data);
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

    private void calculateDate(String date) {
        String[] dates = date.split("/");
        cday = Integer.parseInt(dates[0]);
        cmonth = Integer.parseInt(dates[1]);
        cyear = Integer.parseInt(dates[2]);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShopEditorPermissionsDispatcher.onRequestPermissionsResult(ShopEditor.this,
                requestCode, grantResults);
    }
    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(this, request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this,listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this,listPermission);
    }
}
