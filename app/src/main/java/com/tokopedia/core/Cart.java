package com.tokopedia.core;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.briepay.presenter.BRIePayConnectorView;
import com.tokopedia.core.fragment.BCAklikPayDialog.KlikpayInterface;
import com.tokopedia.core.fragment.EcashDialog.EcashInterface;
import com.tokopedia.core.fragment.FragmentCart;
import com.tokopedia.core.fragment.FragmentCart.ActivityCartCommunicator;
import com.tokopedia.core.fragment.FragmentCartFinish;
import com.tokopedia.core.fragment.FragmentCartFinish.ActivityCartFinishCommunicator;
import com.tokopedia.core.fragment.FragmentCartSummary;
import com.tokopedia.core.fragment.FragmentCartSummary.ActivityCartSummaryCommunicator;
import com.tokopedia.core.interfaces.CartInterfaces;
import com.tokopedia.core.payment.fragment.DynamicPaymentFragment;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.core.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.core.payment.model.responsecartstep2.CartStep2Data;
import com.tokopedia.core.payment.model.responsecartstep2.Transaction;
import com.tokopedia.core.payment.model.responsedynamicpayment.DynamicPaymentData;
import com.tokopedia.core.payment.model.responsethankspayment.ThanksPaymentData;
import com.tokopedia.core.router.TransactionRouter;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart extends TActivity implements ActivityCartCommunicator,
        ActivityCartSummaryCommunicator, ActivityCartFinishCommunicator, EcashInterface,
        KlikpayInterface, BRIePayConnectorView, DynamicPaymentFragment.ActionListener {

    private LocalCacheHandler cacheHandler;
    private FragmentManager fragmentManager;
    public CartInterfaces.FragmentCartCommunicator fragcom;
    public CartInterfaces.FragmentCartSummaryCommunicator fragcomsummary;
    private TkpdProgressDialog MainProgress;
    private DialogFragment fragment;
    private int State;
    private Boolean Status = false;
    private Dialog briDialog;
    private static final String TAG = Cart.class.getSimpleName();

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_CART;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainProgress = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        inflateView(R.layout.activity_cart);

        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentCart fragment = new FragmentCart();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack("cart");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();

        cacheHandler = new LocalCacheHandler(this, TkpdCache.NOTIFICATION_DATA);
    }

    @Override
    public void TriggerToAddFragment(String response) {
        Bundle bundle = new Bundle();
        bundle.putString("response", response);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentCartSummary fragment = new FragmentCartSummary();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack("cartsum");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void TriggerReloadFragment() {
        fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentCart fragment = new FragmentCart();
        fragmentTransaction.add(R.id.container, fragment, "cart");
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentCart fragment = new FragmentCart();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void TriggerToFinishTx(int Gateway, String response) {
        Bundle bundle = new Bundle();
        bundle.putInt("gateway", Gateway);
        bundle.putString("response", response);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentCartFinish fragment = new FragmentCartFinish();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment, "summary");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.findFragmentById(R.id.container) instanceof DynamicPaymentFragment) {
            if (((DynamicPaymentFragment) fragmentManager.findFragmentById(R.id.container))
                    .getPaymentId() != null) {
                onDynamicPaymentSuccess(((DynamicPaymentFragment)
                        fragmentManager.findFragmentById(R.id.container)).getPaymentId(), "");
                UnifyTracking.eventCartAbandon();
            } else {
                if (getFragmentManager().getBackStackEntryCount() == 2) {
                    finish();
                    startActivity(getIntent());
                } else {
                    finish();
                }
            }
            return;
        }

        if (fragmentManager.findFragmentById(R.id.container) instanceof FragmentCartFinish){
            UnifyTracking.eventCartAbandon();
        }
        if (Status) {
            ShowDialog();
        } else if (getFragmentManager().getBackStackEntryCount() == 2) {
            finish();
            startActivity(getIntent());
        } else {
            finish();
        }
    }

    @Override
    public void epayBRICancel() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void toFinishCart(CartStep2Data data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("CART_STEP_2_DATA", data);
        bundle.putBoolean("is_wsv4", true);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentCartFinish fragment = new FragmentCartFinish();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment, "summary");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void Loading() {
        MainProgress.showDialog();
    }

    @Override
    public void FinishLoading() {
        MainProgress.dismiss();

    }

    @Override
    public void toDynamicPayment(DynamicPaymentData data) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, DynamicPaymentFragment.newInstance(data),
                DynamicPaymentFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void toSummaryCart(CarStep1Data cartData) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, FragmentCartSummary.newInstanceWSV4(cartData));
        fragmentTransaction.addToBackStack("cartsum");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
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
    public void passID(String id) {
        Status = false;
        fragcomsummary.passIDToTx(id);

    }

    @Override
    public void passIDKlikPay(String id) {
        Status = false;
        fragcomsummary.completeKlikpay(id);

    }

    private void ShowDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.msg_cancel_payment));
        myAlertDialog.setCancelable(true);
        myAlertDialog.setPositiveButton(getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //startActivity(new Intent(AddToCart.this, IndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //Supaya bisa langsung keluar
                Status = false;
                fragment.dismiss();
            }

        });

        myAlertDialog.setNegativeButton(getString(R.string.title_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void showDialogBackToPage() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.msg_cancel_payment_briepay));
        myAlertDialog.setCancelable(true);
        myAlertDialog.setPositiveButton(getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //startActivity(new Intent(AddToCart.this, IndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //Supaya bisa langsung keluar
                Status = false;
                fragment.dismiss();
                epayBRICancel();
            }

        });

        myAlertDialog.setNegativeButton(getString(R.string.title_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        briDialog = myAlertDialog.create();
        briDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        briDialog.show();
    }

    private void ShowDialogFailEcash() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.msg_error_ecash));
        myAlertDialog.setCancelable(true);
        myAlertDialog.setPositiveButton(getString(R.string.title_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //startActivity(new Intent(AddToCart.this, IndexHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); //Supaya bisa langsung keluar
                Status = false;
                fragment.dismiss();
            }

        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void setDialogStatus(Boolean status, int State,
                                DialogFragment fargment) {
        this.Status = status;
        this.State = State;
        this.fragment = fargment;
    }

    @Override
    public void CancelEcash(DialogFragment Fragment) {
        this.fragment = Fragment;
        ShowDialog();

    }

    @Override
    public void CancelKlikPay(DialogFragment fragment) {
        this.fragment = fragment;
        ShowDialog();

    }

    @Override
    public void errorOTP(DialogFragment Fragment) {
        this.fragment = Fragment;
        ShowDialogFailEcash();

    }

    @Override
    public void onVerifyBRI(String tid) {
        Status = false;
        if (briDialog != null) {
            briDialog.dismiss();
            briDialog = null;
        }
        fragcomsummary.verifyBRIePayment(tid);
    }

    @Override
    public void onCancelBRI(DialogFragment dialogFragment) {
        showDialogBackToPage();
    }

    private void probeAnalytics(String paymentId, String gatewayName){

        String screenName = getString(R.string.cart_pg_3);
        Gson afGSON = new Gson();
        Map[] mapResult = afGSON.fromJson(cacheHandler.getString(Jordan.CACHE_AF_KEY_ALL_PRODUCTS),new TypeToken<Map[]>(){}.getType());
        ArrayList<Product> locaProducts =  afGSON.fromJson(cacheHandler.getString(Jordan.CACHE_LC_KEY_ALL_PRODUCTS),new TypeToken<ArrayList<Product>>(){}.getType());
        ArrayList<Purchase> purchases = afGSON.fromJson(cacheHandler.getString(Jordan.CACHE_KEY_DATA_AR_ALLPURCHASE),new TypeToken<ArrayList<Purchase>>(){}.getType());

        JSONArray arrJas = new JSONArray(cacheHandler.getArrayListString(Jordan.CACHE_AF_KEY_JSONIDS));
        String revenue = cacheHandler.getString(Jordan.CACHE_AF_KEY_REVENUE);
        int qty     =  cacheHandler.getInt(Jordan.CACHE_AF_KEY_QTY);
        String totalShipping = cacheHandler.getLong(Jordan.CACHE_LC_KEY_SHIPPINGRATE)+"";


        /**
         * GTM Block
         *
         */
        if(purchases!=null){
            if(!purchases.isEmpty()){
                for(Purchase purchase : purchases){
                    purchase.setTransactionID(paymentId);
                    PaymentTracking.eventTransactionGTM(purchase);
                }
            }
        }


        /**
         * Localytics Block
         *
         */
        Map<String, String> values = new HashMap<>();
        values.put(this.getString(R.string.event_payment_method), gatewayName);
        values.put(this.getString(R.string.event_value_total_transaction), revenue);
        values.put(this.getString(R.string.value_total_quantity), qty + "");
        values.put(this.getString(R.string.value_shipping_fee), totalShipping + "");

        PaymentTracking.eventTransactionLoca(values, locaProducts);


        /**
         * AppsFlyer Block
         *
         */
        Transaction tx = new Transaction(revenue, paymentId);
        PaymentTracking.eventTransactionAF(tx,arrJas,qty,mapResult);
    }

    @Override
    public void onDynamicPaymentSuccess(final String paymentId, String message) {
        Status = false;
        Map<String, String> params = new HashMap<>();
        params.put("id", paymentId);
        Loading();
        new PaymentNetInteractorImpl().getThanksDynamicPayment(this, params,
                new PaymentNetInteractor.OnGetThanksDynamicPayment() {
                    @Override
                    public void onSuccess(ThanksPaymentData data) {
                        FinishLoading();
                        if(data!=null){
                            CommonUtils.dumper("GAv4 scrooge "+paymentId+" method "+data.getParameter().getGatewayName());
                            String gatewayName = TextUtils.isEmpty(data.getParameter().getGatewayName()) ? "undefined" : data.getParameter().getGatewayName();
                            probeAnalytics(paymentId, gatewayName);
                        }

                        if (data.getIsSuccess() != null && data.getIsSuccess() == 1) {
                            startActivity(TransactionRouter.createIntentTxSummary(Cart.this));
                            finish();
                        } else {
                            CommonUtils.UniversalToast(Cart.this,
                                    getResources().getString(R.string.default_request_error_unknown));
                            finish();
                            startActivity(getIntent());
                        }
                    }

                    @Override
                    public void onError(String message) {
                        CommonUtils.UniversalToast(Cart.this, message);
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onNoConnection() {

                    }
                });
    }

    @Override
    public void onDynamicPaymentFailed(String message) {
        CommonUtils.UniversalToast(Cart.this, message);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onDynamicPaymentCanceled(String message) {
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendNotifLocalyticsCallback();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendNotifLocalyticsCallback();
    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)) {
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
    }
}
