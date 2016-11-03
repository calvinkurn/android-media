package com.tokopedia.tkpd;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.tkpd.app.TActivity;
import com.tokopedia.tkpd.customadapter.ListViewPaymentEditor;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.apiservices.shop.MyShopPaymentService;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.selling.model.MyShopPayment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PaymentEditor extends TActivity {
    private ListView PaymentMethods;
    private ListViewPaymentEditor PaymentAdapter;
    private TkpdProgressDialog mProgressDialog;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflateView(R.layout.activity_payment_editor);


        PaymentMethods = (ListView) findViewById(R.id.listview_payment);
        mProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.MAIN_PROGRESS, getWindow().getDecorView().getRootView());
        mProgressDialog.setLoadingViewId(R.id.include_loading);
        mProgressDialog.showDialog();
        getPaymentMethods();
    }


    private void setToUI(MyShopPayment.Data myShopPayment) {
        ArrayList<String> PaymentIconUri = new ArrayList<String>();
        ArrayList<String> PaymentInfo = new ArrayList<String>();
        for (int i = 0; i < myShopPayment.getPaymentOptions().size(); i++) {
            MyShopPayment.PaymentOption paymentOption = myShopPayment.getPaymentOptions().get(i);
            PaymentIconUri.add(paymentOption.getPaymentImage());
            PaymentInfo.add(Html.fromHtml(getPaymentLocByPaymentId(paymentOption.getPaymentId(), myShopPayment.getLoc())).toString());
        }
        PaymentAdapter = new ListViewPaymentEditor(PaymentEditor.this, PaymentIconUri, PaymentInfo);
        PaymentMethods.setAdapter(PaymentAdapter);
        PaymentAdapter.notifyDataSetChanged();
    }

    private String getPaymentLocByPaymentId(String paymentId, MyShopPayment.Loc loc) {
        String res = "";
        switch (paymentId) {
            case "1":
                res = loc.get1();
                break;
            case "2":
//				loc.get2();
                break;
            case "3":
//				loc.get3();
                break;
            case "4":
                res = loc.get4();
                break;
            case "5":
//				loc.get5();
                break;
            case "6":
                res = loc.get6();
                break;
            case "7":
                res = loc.get7();
                break;
            case "8":
                res = loc.get8();
                break;
            case "9":
                res = loc.get9();
                break;
            case "10":
                res = loc.get10();
                break;
            case "11":
                res = loc.get11();
                break;
            case "12":
                res = loc.get12();
                break;
            case "13":
                res = loc.get13();
                break;
            case "14":
                res = loc.get14();
                break;
            case "15":
//				loc.get15();
                break;
            case "16":
//				loc.get16();
                break;
            case "17":
//				loc.get17();
                break;
            case "18":
//				loc.get18();
                break;
            case "19":
//				loc.get18();
                break;
            case "20":
//				loc.get20();
                break;
            default:
                CommonUtils.dumper("from 1 to 20");
        }
        if (res.equals(""))
            return null;

        return res;
    }

    private void setToUI(JSONObject Result) {
        System.out.println("Initiate payment editor");
        mProgressDialog.dismiss();
        ArrayList<String> PaymentIconUri = new ArrayList<String>();
        ArrayList<String> PaymentInfo = new ArrayList<String>();
        try {
            JSONArray PaymentOptions = new JSONArray(Result.getString("payment_options"));
            JSONObject PaymentLoc = new JSONObject(Result.getString("loc"));
            JSONObject PaymentOption;
            for (int i = 0; i < PaymentOptions.length(); i++) {
                PaymentOption = new JSONObject(PaymentOptions.getString(i));
                PaymentIconUri.add(PaymentOption.getString("payment_image"));
                PaymentInfo.add(Html.fromHtml(PaymentLoc.getString(PaymentOption.getString("payment_id"))).toString());
            }
            PaymentAdapter = new ListViewPaymentEditor(PaymentEditor.this, PaymentIconUri, PaymentInfo);
            PaymentMethods.setAdapter(PaymentAdapter);
            PaymentAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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


    public void getPaymentMethods() {
        new MyShopPaymentService().getApi().getPaymentInfo(AuthUtil.generateParams(this, new HashMap<String, String>()))
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
                                NetworkErrorHelper.showEmptyState(PaymentEditor.this, getWindow().getDecorView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        mProgressDialog.showDialog();
                                        getPaymentMethods();
                                    }
                                });
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                Log.d("STUART", responseData.toString());
                                TkpdResponse response = responseData.body();

                                mProgressDialog.dismiss();

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());

//									Gson gson = new GsonBuilder().create();
//									MyShopPayment.Data data =
//											gson.fromJson(jsonObject.toString(), MyShopPayment.Data.class);

                                    setToUI(jsonObject);

                                } catch (JSONException je) {
                                    Log.e("STUART", PaymentEditor.class.getSimpleName() + je.getLocalizedMessage());
                                }
                            }
                        }
                );
    }

}
