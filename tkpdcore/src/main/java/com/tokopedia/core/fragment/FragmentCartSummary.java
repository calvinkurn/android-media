//package com.tokopedia.core.fragment;
//
//import android.app.Activity;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.tkpd.library.utils.CommonUtils;
//import com.tkpd.library.utils.CurrencyFormatHelper;
//import com.tokopedia.core.Cart;
//import com.tokopedia.core.CreditCardActivity;
//import com.tokopedia.core.R;
//import com.tokopedia.core.analytics.AppScreen;
//import com.tokopedia.core.analytics.PaymentTracking;
//import com.tokopedia.core.analytics.ScreenTracking;
//import com.tokopedia.core.analytics.UnifyTracking;
//import com.tokopedia.core.analytics.nishikino.model.Checkout;
//import com.tokopedia.core.analytics.nishikino.model.Product;
//import com.tokopedia.core.app.TkpdFragment;
//import com.tokopedia.core.briepay.BRIePayDialog;
//import com.tokopedia.core.customadapter.ListProductCart;
//import com.tokopedia.core.interfaces.CartInterfaces;
//import com.tokopedia.core.network.NetworkErrorHelper;
//import com.tokopedia.core.network.NetworkHandler;
//import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
//import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
//import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
//import com.tokopedia.core.payment.model.responsecartstep1.BCAParam;
//import com.tokopedia.core.payment.model.responsecartstep1.CarStep1Data;
//import com.tokopedia.core.payment.model.responsecartstep1.CartProduct;
//import com.tokopedia.core.payment.model.responsecartstep2.CartStep2Data;
//import com.tokopedia.core.util.JSONHandler;
//import com.tokopedia.core.util.MethodChecker;
//import com.tokopedia.core.util.TokenHandler;
//import com.tokopedia.core.var.TkpdUrl;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class FragmentCartSummary extends TkpdFragment implements CartInterfaces.FragmentCartSummaryCommunicator {
//
//    private View view;
//    private Activity context;
//    //private ActivityCartCommunicator activitycom;
//    private JSONHandler JsonSender;
//    private TokenHandler Token = new TokenHandler();
//    private ArrayList<ArrayList<String>> pName = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> pPrice = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> pWeight = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> Notes = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> PriceTotal = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> pImageUri = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> pErrorMsg = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<String>> CartID = new ArrayList<ArrayList<String>>();
//    private ArrayList<ArrayList<Integer>> Qty = new ArrayList<ArrayList<Integer>>();
//    private ArrayList<ArrayList<Bitmap>> pImage = new ArrayList<ArrayList<Bitmap>>();
//    private ArrayList<String> ShopName = new ArrayList<String>();
//    private ArrayList<String> TotalPrice = new ArrayList<String>();
//    private ArrayList<String> ShippingAddress = new ArrayList<String>();
//    private ArrayList<String> ShippingAgency = new ArrayList<String>();
//    private ArrayList<String> TotalWeight = new ArrayList<String>();
//    private ArrayList<String> SubTotal = new ArrayList<String>();
//    private ArrayList<String> ShippingCost = new ArrayList<String>();
//    private ArrayList<String> InsurancePrice = new ArrayList<>();
//    private ArrayList<String> additionalPrice = new ArrayList<>();
//    private ArrayList<String> ShippingID = new ArrayList<String>();
//    private ArrayList<String> AddrID = new ArrayList<String>();
//    private ArrayList<String> ShopID = new ArrayList<String>();
//    private ArrayList<Integer> luckyMerchantBadge = new ArrayList<>();
//    private ArrayList<String> Error1 = new ArrayList<String>();
//    private ArrayList<String> Error2 = new ArrayList<String>();
//    private ArrayList<Integer> AllowCheckout = new ArrayList<Integer>();
//    private ArrayList<Integer> ForceInsurance = new ArrayList<Integer>();
//    private ArrayList<Integer> isChosen = new ArrayList<Integer>();
//    private ArrayList<Integer> InsuranceState = new ArrayList<Integer>();
//    private ArrayList<Integer> ButtonDetailState = new ArrayList<Integer>();
//    private ArrayList<String> DSNameVal = new ArrayList<String>();
//    private ArrayList<String> DSPhoneVal = new ArrayList<String>();
//    private ArrayList<Boolean> isDropship = new ArrayList<Boolean>();
//    private ArrayList<View> Item = new ArrayList<View>();
//    private LinearLayout lvContainer;
//    private TextView PayementMethod;
//    private TextView TotalInvoice;
//    private TextView TokopediaBalance;
//    private View BalanceView;
//    private TextView GrandTotal;
//    private TextView UniqueCode;
//    private EditText Password;
//    private EditText CardNumber;
//    private EditText TokenResponse;
//    private EditText Step1;
//    private EditText Step2;
//    private EditText Step3;
//    private EditText Step4;
//    private EditText klikBCAUSerID;
//    private TextView PayBut;
//    private String PasswordString = null;
//    private String Response;
//    private String TokenCart;
//    private int GatewayID;
//    private View SaldoMethod;
//    private View TransferMethod;
//    private View ClickpayMethod;
//    private View EcashMethod;
//    private View CreditCardMethod;
//    private LinearLayout MainView;
//    private View klikBCAMethod;
//    private View indomaretMethod;
//    private String postResponse;
//    private String checkoutOption;
//    private View EcashDiscountText;
//    private TextView EcashDiscount;
//    private TextView mVoucherText;
//    private TextView indomaretInstruction;
//    private TextView indomaretAdminFee;
//    private View mVoucher;
//    private DialogFragment newFragment;
//    private ViewGroup mainContainer;
//    private LinearLayout paymentSummaryLayout;
//    //	private View OrderStatusBtn;
//    private ActivityCartSummaryCommunicator activitycom;
//    private Checkout checkoutAnalytics = new Checkout();
//    private static final String TAG = FragmentCartSummary.class.getSimpleName();
//
//
//    private ArrayList<ItemContainer> ItemContent = new ArrayList<ItemContainer>();
//    private CarStep1Data cartStep1Data;
//    private boolean isDataFromWSV4;
//    private PaymentNetInteractorImpl netInteractor;
//
//    @Override
//    protected String getScreenName() {
//        return AppScreen.SCREEN_CART_SUMMARY;
//    }
//
//    public interface ActivityCartSummaryCommunicator {
//        void TriggerToFinishTx(int Gateway, String response);
//
//        void Loading();
//
//        void FinishLoading();
//
//        void setDialogStatus(Boolean status, int State, DialogFragment fargment);
//
//        void epayBRICancel();
//
//        void toFinishCart(CartStep2Data data);
//    }
//
//    public static FragmentCartSummary newInstanceWSV4(CarStep1Data cartData) {
//        FragmentCartSummary fragment = new FragmentCartSummary();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("CART_STEP_1_DATA", cartData);
//        bundle.putString("response", "");
//        bundle.putBoolean("is_wsv4", true);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bundle = this.getArguments();
//        Response = bundle.getString("response");
//        cartStep1Data = bundle.getParcelable("CART_STEP_1_DATA");
//        isDataFromWSV4 = bundle.getBoolean("is_wsv4", false);
//        netInteractor = new PaymentNetInteractorImpl();
//        CommonUtils.dumper(TAG + " ebri oncreate");
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        context = getActivity();
//        activitycom = (ActivityCartSummaryCommunicator) context;
//        ((Cart) context).fragcomsummary = this;
//        super.onAttach(activity);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_cart_summary, container, false);
//        mainContainer = container;
//        SaldoMethod = (View) view.findViewById(R.id.saldo_method);
//        MainView = (LinearLayout) view.findViewById(R.id.main_view);
//        MainView.setVisibility(View.GONE);
//        TransferMethod = (View) view.findViewById(R.id.transfer_method);
//        ClickpayMethod = (View) view.findViewById(R.id.clickpay_method);
//        CreditCardMethod = view.findViewById(R.id.credit_method);
//        EcashMethod = (View) view.findViewById(R.id.ecash_method);
//        klikBCAMethod = (View) view.findViewById(R.id.klikbca_method);
//        EcashDiscount = (TextView) view.findViewById(R.id.total_discount4);
//        EcashDiscountText = view.findViewById(R.id.discount4);
//        indomaretMethod = klikBCAMethod;
//        if (isDataFromWSV4) {
//            GatewayID = Integer.parseInt(cartStep1Data.getTransaction().getGateway());
//            renderDataFromWSV4();
//            return view;
//        }
//
////		OrderStatusBtn = (View) view.findViewById(R.shopId.order_stats_but);
////
////		OrderStatusBtn.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////
////				Log.i("order_status_class", "here");
////			}
////		});
//
//        try {
//            //JSONObject json = new JSONObject(Response);
//            //if (!json.isNull("result")) {
//            JSONObject Result = new JSONObject(Response);
//            GatewayID = Result.getInt("gateway");
//            TokenCart = Result.getString("token");
//            checkoutOption = Result.getString("gateway_name");
//            if (GatewayID == 0) {
//                SaldoMethod.setVisibility(View.VISIBLE);
//                mVoucher = view.findViewById(R.id.voucher1);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt1);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice);
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance);
//                setSummaryView((LinearLayout) SaldoMethod);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                Password = (EditText) view.findViewById(R.id.password);
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_idr"));
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                Password.setTypeface(Typeface.DEFAULT);
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        PasswordString = Password.getText().toString();
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
//                        PostStep2();
//                        sendToGTMPayment();
//                    }
//
//                });
//            } else if (GatewayID == 1) {
//                TransferMethod.setVisibility(View.VISIBLE);
//                mVoucher = view.findViewById(R.id.voucher2);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt2);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method2);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice2);
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance2);
//                UniqueCode = (TextView) view.findViewById(R.id.unique_code);
//                setSummaryView((LinearLayout) TransferMethod);
//                Password = (EditText) view.findViewById(R.id.password_trf);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_bf_fee_idr"));
//                UniqueCode.setText(Result.getString("conf_code_idr"));
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                Password.setTypeface(Typeface.DEFAULT);
//                if (Result.getInt("deposit_amt") == 0) {
//                    Password.setVisibility(View.GONE);
//                }
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        PasswordString = Password.getText().toString();
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
//                        PostStep2();
//                        sendToGTMPayment();
//
//                    }
//
//                });
//            } else if (GatewayID == 4) {
//                ClickpayMethod.setVisibility(View.VISIBLE);
//                mVoucher = view.findViewById(R.id.voucher3);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt3);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice3);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method3);
//                setSummaryView((LinearLayout) ClickpayMethod);
//                CardNumber = (EditText) view.findViewById(R.id.card_number);
//                TokenResponse = (EditText) view.findViewById(R.id.token_response);
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance3);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                BalanceView = view.findViewById(R.id.tokopedia_balance3);
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                if (Result.getInt("deposit_amt") == 0) {
//                    BalanceView.setVisibility(View.GONE);
//                    TokopediaBalance.setVisibility(View.GONE);
//                }
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_idr"));
//                Step1 = (EditText) view.findViewById(R.id.step_1);
//                Step2 = (EditText) view.findViewById(R.id.step_2);
//                Step3 = (EditText) view.findViewById(R.id.step_3);
//                Step4 = (EditText) view.findViewById(R.id.step_4);
//                Step1.setEnabled(false);
//                Step2.setEnabled(false);
//                Step3.setEnabled(false);
//                Step4.setEnabled(false);
//                Step1.setText("3");
//                Step3.setText(Result.getString("payment_left"));
//                Step4.setText(Result.getString("payment_id"));
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(TokenResponse.getWindowToken(), 0);
//                        PostStep2();
//                        sendToGTMPayment();
//                    }
//
//                });
//                CardNumber.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count,
//                                                  int after) {
//
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        String cardNo = CardNumber.getText().toString();
//                        char TenDigit[] = new char[10];
//                        if (cardNo.length() > 10) {
//                            int k = 0;
//                            for (int i = cardNo.length() - 10; i < cardNo.length(); i++) {
//                                TenDigit[k] = cardNo.charAt(i);
//                                k++;
//                            }
//                        } else {
//                            int k = 0;
//                            for (int i = 0; i < cardNo.length(); i++) {
//                                TenDigit[k] = cardNo.charAt(i);
//                                k++;
//                            }
//                        }
//                        Step2.setText(String.valueOf(TenDigit));
//                    }
//                });
//                    /*CardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//						@Override
//						public void onFocusChange(View v, boolean hasFocus) {
//							String cardNo = CardNumber.getText().toString();
//							char TenDigit[] = new char[10];
//							if (cardNo.length() > 10) {
//								int k = 0;
//								for (int i = cardNo.length() - 10; i < cardNo.length(); i++) {
//									TenDigit[k] = cardNo.charAt(i);
//									k++;
//								}
//								Step2.setText(String.valueOf(TenDigit));
//							}
//
//						}
//					});*/
//
//            } else if (GatewayID == 6) {
//                EcashMethod.setVisibility(View.VISIBLE);
//                mVoucher = view.findViewById(R.id.voucher4);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//                setSummaryView((LinearLayout) EcashMethod);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_idr"));
//                //final String UrlMandiri = Result.getString("link_mandiri");TODO HERE
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                BalanceView = view.findViewById(R.id.tokopedia_balance4);
//                if (Result.getInt("deposit_amt") == 0) {
//                    BalanceView.setVisibility(View.GONE);
//                    TokopediaBalance.setVisibility(View.GONE);
//                }
//                if (Result.has("discount_gateway")) {
//                    if (Result.getInt("discount_gateway") != 0) {
//                        EcashDiscount.setText(Result.getString("discount_gate_idr"));
//                        EcashDiscount.setVisibility(View.VISIBLE);
//                        EcashDiscountText.setVisibility(View.VISIBLE);
//                    }
//                }
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        PostStep2();
//                        sendToGTMPayment();
//                    }
//
//                });
//
//            } else if (GatewayID == 7) {
//                JSONObject BcaParam = new JSONObject(Result.getString("bca_param"));
//                EcashMethod.setVisibility(View.VISIBLE);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//                setSummaryView((LinearLayout) EcashMethod);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_idr"));
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                BalanceView = view.findViewById(R.id.tokopedia_balance4);
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                if (Result.getInt("deposit_amt") == 0) {
//                    BalanceView.setVisibility(View.GONE);
//                    TokopediaBalance.setVisibility(View.GONE);
//                }
//                final String url = TkpdUrl.BCA_KLIKPAY;
//                //final String url = BcaParam.getString("bca_url");
//                final String bca_code = BcaParam.getString("bca_code");
//                final String bca_amt = BcaParam.getString("bca_amt");
//                final String currency = BcaParam.getString("currency");
//                final String miscFee = BcaParam.getString("miscFee");
//                //final String descp = BcaParam.getString("descp");
//                final String bca_date = BcaParam.getString("bca_date");
//                final String signature = BcaParam.getString("signature");
//                final String callback = BcaParam.getString("callback");
//                final String payment_id = BcaParam.getString("payment_id");
//                final String payType = BcaParam.getString("payType");
//                //final String UrlMandiri = Result.getString("link_mandiri");
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        //PostStep2();
//                        //showDialogBCA("https://202.6.215.230:8081/purchasing/purchase.do?action=loginRequest");
//                        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.addToBackStack(null);
//
//                        // Create and show the dialog.
//                        newFragment = new BCAklikPayDialog();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("url", url);
//                        bundle.putString("bca_code", bca_code);
//                        bundle.putString("bca_amt", bca_amt);
//                        bundle.putString("currency", currency);
//                        bundle.putString("miscFee", miscFee);
//                        bundle.putString("bca_date", bca_date);
//                        bundle.putString("signature", signature);
//                        bundle.putString("callback", callback);
//                        bundle.putString("descp", "");
//                        bundle.putString("payment_id", payment_id);
//                        bundle.putString("payType", payType);
//                        activitycom.setDialogStatus(true, 2, newFragment);
//                        newFragment.setArguments(bundle);
//                        newFragment.show(ft, "dialog");
//
//                        sendToGTMPayment();
//                    }
//
//                });
//            } else if (GatewayID == 8 || GatewayID == 12) { // TODO Credit Card
//                CreditCardMethod.setVisibility(View.VISIBLE);
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance5);
//                BalanceView = view.findViewById(R.id.tokopedia_balance5);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method5);
//                setSummaryView((LinearLayout) CreditCardMethod);
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice5);
//                mVoucher = (TextView) view.findViewById(R.id.voucher5);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt5);
//                TotalInvoice.setText(Result.optString("grand_total_idr"));
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                PayementMethod.setText(Result.getString("gateway_name"));
//                JSONObject creditCard = Result.optJSONObject("credit_card");
//                TextView adminFee = (TextView) view.findViewById(R.id.admin_fee5);
//                adminFee.setText(creditCard.optString("charge_idr"));
//                if (creditCard.optInt("charge") < 1) {
//                    adminFee.setText(context.getString(R.string.title_gratis));
//                }
//                final CreditCardActivity.Param param = new CreditCardActivity.Param();
//                if (Result.optInt("deposit_amt", 0) == 0) {
//                    BalanceView.setVisibility(View.GONE);
//                    TokopediaBalance.setVisibility(View.GONE);
//                }
//                try {
//                    JSONObject veritrans = Result.optJSONObject("veritrans");
//                    param.clientKey = veritrans.optString("client_key");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    param.clientKey = ""; // TODO kenapa jadi gak ada dibalikin dari webservice?
//                }
//                param.amount = Result.optString("payment_left");
//                param.ccData = Result.optString("credit_card_data");
//                param.tokenCart = TokenCart;
//                PayBut.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivityForResult(CreditCardActivity.createIntent(getActivity(), param), 0);
//                        sendToGTMPayment();
//                    }
//                });
//                setInstallmentPayment(param, Result, GatewayID);
//            } else if (GatewayID == 9) {
//                setSummaryView((LinearLayout) klikBCAMethod);
//                alterViewElementKlikBCA();
//                setKlikBCAParameter(Result);
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                if (Result.getInt("deposit_amt") == 0) {
//                    Password.setVisibility(View.GONE);
//                }
//            } else if (GatewayID == 10) {
//                setSummaryView((LinearLayout) indomaretMethod);
//                alterViewElementIndomaret();
//                setIndomaretPayment(Result);
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                klikBCAUSerID.setVisibility(View.GONE);
//                if (Result.getInt("deposit_amt") == 0) {
//                    Password.setVisibility(View.GONE);
//                }
//            } else if (GatewayID == 11) {
//                // BRI epay
//                mVoucher = view.findViewById(R.id.voucher4);
//                ((TextView) mVoucher).setTypeface(((TextView) mVoucher).getTypeface(), Typeface.BOLD);
//                mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//                mVoucherText.setTypeface(mVoucherText.getTypeface(), Typeface.BOLD);
//
//                EcashMethod.setVisibility(View.VISIBLE);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//                PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//                TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//                setSummaryView((LinearLayout) EcashMethod);
//                GrandTotal.setText(Result.getString("payment_left_idr"));
//                PayementMethod.setText(Result.getString("gateway_name"));
//                TotalInvoice.setText(Result.getString("grand_total_idr"));
//                TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//                BalanceView = view.findViewById(R.id.tokopedia_balance4);
//                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
//                if (Result.getInt("deposit_amt") == 0) {
//                    BalanceView.setVisibility(View.GONE);
//                    TokopediaBalance.setVisibility(View.GONE);
//                }
//                if (Result.has("discount_gateway")) {
//                    if (!Result.isNull("discount_gateway")) {
//                        if (Result.getInt("discount_gateway") != 0) {
//                            EcashDiscount.setText(Result.getString("discount_gate_idr"));
//                            EcashDiscount.setVisibility(View.VISIBLE);
//                            EcashDiscountText.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//
//                //"https://ib.bri.co.id/pg/ePayment";
//                final String url = Result.getString("bri_website_link");
//                final String token = "";
//                final String gateway = "11";
//                final String refback = "";
//                final String step = "2";
//                final String keysTrxEcomm = Result.getString("transaction_code");
//                postResponse = Result.toString();
//                PayBut.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        if (isNetworkStatusAvailable(getActivity())) {
//                            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                            ft.addToBackStack(null);
//
//                            newFragment = new BRIePayDialog();
//                            Bundle bundle = new Bundle();
//                            bundle.putString("url", url);
//                            bundle.putString("token", token);
//                            bundle.putString("gateway", gateway);
//                            bundle.putString("refback", refback);
//                            bundle.putString("keysTrxEcomm", keysTrxEcomm);
//                            bundle.putString("step", step);
//                            activitycom.setDialogStatus(true, 2, newFragment);
//                            newFragment.setArguments(bundle);
//                            newFragment.show(ft, "dialogBRI");
//                            sendToGTMPayment();
//                        } else {
//                            showNoConnectionDialog(getActivity());
//                        }
//
//                    }
//
//                });
//            }
//
//            CommonUtils.dumper("neworder " + Result.getString("voucher_amt_idr") + " " + Result.getString("voucher_amt"));
//            if (!Result.getString("voucher_amt").equals("0") && mVoucher != null) {
//                mVoucher.setVisibility(View.VISIBLE);
//                mVoucherText.setText(Result.getString("voucher_amt_idr"));
//                mVoucherText.setVisibility(View.VISIBLE);
//            }
//
//            JSONArray List = new JSONArray(Result.getString("carts"));
//            for (int i = 0; i < List.length(); i++) {
//                ArrayList<String> pNameData = new ArrayList<String>();
//                ArrayList<String> pPriceData = new ArrayList<String>();
//                ArrayList<String> pWeightData = new ArrayList<String>();
//                ArrayList<String> NotesData = new ArrayList<String>();
//                ArrayList<String> PriceTotalData = new ArrayList<String>();
//                ArrayList<String> pImageUriData = new ArrayList<String>();
//                ArrayList<String> pErrorMsgData = new ArrayList<String>();
//                ArrayList<Integer> QtyData = new ArrayList<Integer>();
//                ArrayList<Integer> FInsurance = new ArrayList<Integer>();
//                ArrayList<Bitmap> pImageData = new ArrayList<Bitmap>();
//                ArrayList<String> CartIDData = new ArrayList<String>();
//
//                JSONObject ListDetail = new JSONObject(List.getString(i));
//                JSONArray DetailProdList = new JSONArray(ListDetail.getString("details"));
//                for (int k = 0; k < DetailProdList.length(); k++) {
//                    JSONObject DetailProd = new JSONObject(DetailProdList.getString(k));
//                    FInsurance.add(DetailProd.getInt("finsurance"));
//                    pNameData.add(DetailProd.getString("prod_name"));
//                    pPriceData.add(DetailProd.getString("price"));
//                    pWeightData.add(DetailProd.getString("weight"));
//                    NotesData.add(DetailProd.getString("notes_p"));
//                    PriceTotalData.add(DetailProd.getString("total_price"));
//                    QtyData.add(DetailProd.getInt("qty"));
//                    pImageUriData.add(DetailProd.getString("product_pic"));
//                    if (!DetailProd.isNull("error_msg")) {
//                        pErrorMsgData.add(DetailProd.getString("error_msg"));
//                    } else {
//                        pErrorMsgData.add(null);
//                    }
//                    pImageData.add(null);
//
//                    Product product = new Product();
//                    product.setProductID(DetailProd.getString("prod_id"));
//                    product.setPrice(CurrencyFormatHelper.convertRupiahToInt(DetailProd.getString("price")));
//                    product.setQty(DetailProd.getInt("qty"));
//                    product.setProductName(MethodChecker.fromHtml(DetailProd.getString("prod_name")).toString());
//
//                    checkoutAnalytics.addProduct(product.getProduct());
//                }
//
//                JSONObject Shop = new JSONObject(ListDetail.getString("shop"));
//                ShopName.add(Shop.getString("name"));
//                ShopID.add(Shop.getString("id"));
//                luckyMerchantBadge.add(Shop.optInt("lucky_merchant", 0));
//
//                JSONObject Dest = new JSONObject(ListDetail.getString("dest"));
//                ShippingAddress.add(MethodChecker.fromHtml(Dest.getString("receiver_name")).toString());
//                AddrID.add(Dest.getString("id"));
//
//                JSONObject Shipping = new JSONObject(ListDetail.getString("shipping"));
//                ShippingAgency.add(Shipping.getString("name") + " - " + Shipping.getString("product_name"));
//                ShippingID.add(Shipping.getString("sp_id"));
//
//					/*if (!ListDetail.isNull("must_insurance")) {
//                        InsuranceState.add(0);
//					} else if (!ListDetail.isNull("cannot_insurance")) {
//						InsuranceState.add(1);
//					} else {
//						InsuranceState.add(2);
//					}*/
//
//                if (!ListDetail.isNull("fpartial")) {
//                    isChosen.add(ListDetail.getInt("fpartial"));
//                } else {
//                    isChosen.add(0);
//                }
//                TotalPrice.add(ListDetail.getString("ttl_amount_idr"));
//                TotalWeight.add(ListDetail.getString("ttl_weight"));
//                SubTotal.add(ListDetail.getString("ttl_product_price_idr"));
//                ShippingCost.add(ListDetail.getString("shipping_rate_idr"));
//                InsurancePrice.add(ListDetail.getString("insurance_price_idr"));
//                additionalPrice.add(ListDetail.getString("logistic_fee"));
//                AllowCheckout.add(ListDetail.getInt("is_allow_checkout"));
//                ForceInsurance.add(ListDetail.getInt("force_insurance"));
//                if (!ListDetail.isNull("cart_error_msg_1") && !ListDetail.isNull("cart_error_msg_2")) {
//                    Error1.add(ListDetail.getString("cart_error_msg_1"));
//                    Error2.add(ListDetail.getString("cart_error_msg_2"));
//                } else {
//                    Error1.add(null);
//                    Error2.add(null);
//                }
//                InsuranceState.add(1);
//                for (int k = 0; k < FInsurance.size(); k++) {
//                    if (FInsurance.get(k) == 1) {
//                        InsuranceState.set(i, 0);
//                    }
//                }
//                ButtonDetailState.add(0);
//                pName.add(pNameData);
//                pPrice.add(pPriceData);
//                pWeight.add(pWeightData);
//                Notes.add(NotesData);
//                PriceTotal.add(PriceTotalData);
//                Qty.add(QtyData);
//                pImage.add(pImageData);
//                pImageUri.add(pImageUriData);
//                pErrorMsg.add(pErrorMsgData);
//                if (!ListDetail.isNull("dropship_name")) {
//                    DSNameVal.add(ListDetail.getString("dropship_name"));
//                    isDropship.add(true);
//                } else {
//                    DSNameVal.add("");
//                    isDropship.add(false);
//                }
//                if (!ListDetail.isNull("dropship_telp")) {
//                    DSPhoneVal.add(ListDetail.getString("dropship_telp"));
//                } else {
//                    DSPhoneVal.add("");
//                }
//
//            }
//            setCashbackAmount(Result.optString("cashback_idr", "0"), Result.optInt("cashback", 0));
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        addItemToLayout();
//
//        checkoutAnalytics.setCurrency("IDR");
//        if (!TextUtils.isEmpty(checkoutOption))
//            checkoutAnalytics.setCheckoutOption(checkoutOption);
//        checkoutAnalytics.setStep(2);
//        PaymentTracking.eventCartCheckout(checkoutAnalytics);
//
//        setLocalyticFlow();
//
//        return view;
//    }
//
//    private void renderDataFromWSV4() {
//        switch (cartStep1Data.getTransaction().getGateway()) {
//            case "0":
//                renderTokopediaBalanceMethod();
//                break;
//            case "1":
//                renderBankTransferMethod();
//                break;
//            case "4":
//                renderMandiriKlikPayMethod();
//                break;
//            case "6":
//                renderMandiriECashMethod();
//                break;
//            case "7":
//                renderBCAKlikPayMethod();
//                break;
//            case "8":
//                renderCreditCardMethod();
//                break;
//            case "12":
//                renderInstallmentMethod();
//                break;
//            case "9":
//                renderKlikBCAMethod();
//                break;
//            case "10":
//                renderIndomaretMethod();
//                break;
//            case "11":
//                renderBRIEpayMethod();
//                break;
//        }
//
//
//        if (!cartStep1Data.getTransaction().getVoucherAmount().equals("0") && mVoucher != null) {
//            mVoucher.setVisibility(View.VISIBLE);
//            mVoucherText.setText(cartStep1Data.getTransaction().getVoucherAmountIdr());
//            mVoucherText.setVisibility(View.VISIBLE);
//        }
//
//        for (int i = 0; i < cartStep1Data.getTransaction().getCarts().size(); i++) {
//            com.tokopedia.core.payment.model.responsecartstep1.Cart cart = cartStep1Data.getTransaction().getCarts().get(i);
//            ArrayList<String> pNameData = new ArrayList<String>();
//            ArrayList<String> pPriceData = new ArrayList<String>();
//            ArrayList<String> pWeightData = new ArrayList<String>();
//            ArrayList<String> NotesData = new ArrayList<String>();
//            ArrayList<String> PriceTotalData = new ArrayList<String>();
//            ArrayList<String> pImageUriData = new ArrayList<String>();
//            ArrayList<String> pErrorMsgData = new ArrayList<String>();
//            ArrayList<Integer> QtyData = new ArrayList<Integer>();
//            ArrayList<Integer> FInsurance = new ArrayList<Integer>();
//            ArrayList<Bitmap> pImageData = new ArrayList<Bitmap>();
//            ArrayList<String> CartIDData = new ArrayList<String>();
//            for (CartProduct product : cart.getCartProducts()) {
//                FInsurance.add(product.getProductUseInsurance());
//                pNameData.add(product.getProductName());
//                pPriceData.add(product.getProductPriceIdr());
//                pWeightData.add(product.getProductWeight());
//                NotesData.add(product.getProductNotes());
//                PriceTotalData.add(product.getProductTotalPriceIdr());
//                QtyData.add(Integer.valueOf(product.getProductQuantity()));
//                pImageUriData.add(product.getProductPic());
//                if (product.getErrors() != null && !product.getErrors().isEmpty()) {
//                    pErrorMsgData.add(product.getErrors().get(0).toString());
//                } else {
//                    pErrorMsgData.add(null);
//                }
//                pImageData.add(null);
//                Product product1 = new Product();
//                product1.setProductID(product.getProductId());
//                product1.setPrice(CurrencyFormatHelper.convertRupiahToInt(product.getProductPrice()));
//                product1.setQty(product.getProductQuantity());
//                product1.setProductName(MethodChecker.fromHtml(product.getProductName()).toString());
//                checkoutAnalytics.addProduct(product1.getProduct());
//            }
//
//            ShopName.add(cart.getCartShop().getShopName());
//            ShopID.add(cart.getCartShop().getShopId());
//            luckyMerchantBadge.add(cart.getCartShop().getLuckyMerchant());
//
//            ShippingAddress.add(MethodChecker.fromHtml(cart.getCartDestination().getReceiverName()).toString());
//            AddrID.add(cart.getCartDestination().getAddressId());
//
//            ShippingAgency.add(cart.getCartShipments().getShipmentName() + " - " + cart.getCartShipments().getShipmentPackageName());
//            ShippingID.add(cart.getCartShipments().getShipmentPackageId());
//            if (cart.getCartPartial() != null) {
//                isChosen.add(cart.getCartPartial());
//            } else {
//                isChosen.add(0);
//            }
//            TotalPrice.add(cart.getCartTotalAmountIdr());
//            TotalWeight.add(cart.getCartTotalWeight());
//            SubTotal.add(cart.getCartTotalProductPriceIdr());
//            ShippingCost.add(cart.getCartShippingRateIdr());
//            InsurancePrice.add(cart.getCartInsurancePriceIdr());
//            additionalPrice.add(cart.getCartLogisticFee());
//            AllowCheckout.add(cart.getCartIsAllowCheckout());
//            ForceInsurance.add(cart.getCartForceInsurance());
//            if (cart.getCartErrorMessage1() != null && !cart.getCartErrorMessage1().equals("0")
//                    && cart.getCartErrorMessage2() != null && !cart.getCartErrorMessage2().equals("0")) {
//                Error1.add(cart.getCartErrorMessage1());
//                Error2.add(cart.getCartErrorMessage2());
//            } else {
//                Error1.add(null);
//                Error2.add(null);
//            }
//            InsuranceState.add(1);
//            for (int k = 0; k < FInsurance.size(); k++) {
//                if (FInsurance.get(k) == 1) {
//                    InsuranceState.set(i, 0);
//                }
//            }
//
//            ButtonDetailState.add(0);
//            pName.add(pNameData);
//            pPrice.add(pPriceData);
//            pWeight.add(pWeightData);
//            Notes.add(NotesData);
//            PriceTotal.add(PriceTotalData);
//            Qty.add(QtyData);
//            pImage.add(pImageData);
//            pImageUri.add(pImageUriData);
//            pErrorMsg.add(pErrorMsgData);
//
//            if (cart.getCartDropshipName() != null && !cart.getCartDropshipName().equals("0")) {
//                DSNameVal.add(cart.getCartDropshipName());
//                isDropship.add(true);
//            } else {
//                DSNameVal.add("");
//                isDropship.add(false);
//            }
//            if (cart.getCartDropshipTelp() != null && !cart.getCartDropshipTelp().equals("0")) {
//                DSPhoneVal.add(cart.getCartDropshipTelp());
//            } else {
//                DSPhoneVal.add("");
//            }
//
//        }
//
//        setCashbackAmount(cartStep1Data.getTransaction().getCashbackIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getCashback()));
//        addItemToLayout();
//        checkoutAnalytics.setCurrency("IDR");
//        if (!TextUtils.isEmpty(checkoutOption))
//            checkoutAnalytics.setCheckoutOption(checkoutOption);
//        checkoutAnalytics.setStep(2);
//        PaymentTracking.eventCartCheckout(checkoutAnalytics);
//
//        setLocalyticFlow();
//    }
//
//    private void renderBCAKlikPayMethod() {
//        EcashMethod.setVisibility(View.VISIBLE);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//        BalanceView = view.findViewById(R.id.tokopedia_balance4);
//        setSummaryView((LinearLayout) EcashMethod);
//
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//
//        final String url = TkpdUrl.BCA_KLIKPAY;
//        BCAParam bcaParam = cartStep1Data.getTransaction().getBcaParam();
//
//        //final String url = BcaParam.getString("bca_url");
//        final String bca_code = bcaParam.getBcaCode();
//        final String bca_amt = bcaParam.getBcaAmt();
//        final String currency = bcaParam.getCurrency();
//        final String miscFee = bcaParam.getMiscFee();
//        //final String descp = BcaParam.getString("descp");
//        final String bca_date = bcaParam.getBcaDate();
//        final String signature = bcaParam.getSignature();
//        final String callback = bcaParam.getCallback();
//        final String payment_id = bcaParam.getPaymentId();
//        final String payType = bcaParam.getPayType();
//        //final String UrlMandiri = Result.getString("link_mandiri");
//
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                //PostStep2();
//                //showDialogBCA("https://202.6.215.230:8081/purchasing/purchase.do?action=loginRequest");
//                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.addToBackStack(null);
//
//                // Create and show the dialog.
//                newFragment = new BCAklikPayDialog();
//                Bundle bundle = new Bundle();
//                bundle.putString("url", url);
//                bundle.putString("bca_code", bca_code);
//                bundle.putString("bca_amt", bca_amt);
//                bundle.putString("currency", currency);
//                bundle.putString("miscFee", miscFee);
//                bundle.putString("bca_date", bca_date);
//                bundle.putString("signature", signature);
//                bundle.putString("callback", callback);
//                bundle.putString("descp", "");
//                bundle.putString("payment_id", payment_id);
//                bundle.putString("payType", payType);
//                activitycom.setDialogStatus(true, 2, newFragment);
//                newFragment.setArguments(bundle);
//                newFragment.show(ft, "dialog");
//                sendToGTMPayment();
//            }
//
//        });
//    }
//
//    private void renderBRIEpayMethod() {
//        mVoucher = view.findViewById(R.id.voucher4);
//        ((TextView) mVoucher).setTypeface(((TextView) mVoucher).getTypeface(), Typeface.BOLD);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//        mVoucherText.setTypeface(mVoucherText.getTypeface(), Typeface.BOLD);
//
//        EcashMethod.setVisibility(View.VISIBLE);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//        setSummaryView((LinearLayout) EcashMethod);
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//        TokopediaBalance.setText(MessageFormat.format("({0})", cartStep1Data.getTransaction().getDepositAmountIdr()));
//        BalanceView = view.findViewById(R.id.tokopedia_balance4);
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            BalanceView.setVisibility(View.GONE);
//            TokopediaBalance.setVisibility(View.GONE);
//        }
//        if (cartStep1Data.getTransaction().getDiscountGateway() != null
//                && !cartStep1Data.getTransaction().getDiscountGateway().equals("0")) {
//            EcashDiscount.setText(cartStep1Data.getTransaction().getDiscountGatewayIdr());
//            EcashDiscount.setVisibility(View.VISIBLE);
//            EcashDiscountText.setVisibility(View.VISIBLE);
//        }
//
//        final String url = cartStep1Data.getTransaction().getBriWebsiteLink();
//        final String token = "";
//        final String gateway = "11";
//        final String refback = "";
//        final String step = "2";
//        final String keysTrxEcomm = cartStep1Data.getTransaction().getTransactionCode();
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if (isNetworkStatusAvailable(getActivity())) {
//                    android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.addToBackStack(null);
//
//                    newFragment = new BRIePayDialog();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", url);
//                    bundle.putString("token", token);
//                    bundle.putString("gateway", gateway);
//                    bundle.putString("refback", refback);
//                    bundle.putString("keysTrxEcomm", keysTrxEcomm);
//                    bundle.putString("step", step);
//                    activitycom.setDialogStatus(true, 2, newFragment);
//                    newFragment.setArguments(bundle);
//                    newFragment.show(ft, "dialogBRI");
//                    sendToGTMPayment();
//                } else {
//                    showNoConnectionDialog(getActivity());
//                }
//
//            }
//
//        });
//    }
//
//    private void renderIndomaretMethod() {
//        setSummaryView((LinearLayout) indomaretMethod);
//        alterViewElementIndomaret();
//        initKlikBCAView();
//        GrandTotal.setText(cartStep1Data.getTransaction().getIndomaret().getTotalChargeRealIdr());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getIndomaret().getTotalChargeRealIdr());
//        indomaretAdminFee.setText(cartStep1Data.getTransaction().getIndomaret().getChargeRealIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        UniqueCode.setText(cartStep1Data.getTransaction().getConfCodeIdr());
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        klikBCAUSerID.setVisibility(View.GONE);
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            Password.setVisibility(View.GONE);
//        }
//    }
//
//    private void renderKlikBCAMethod() {
//        setSummaryView((LinearLayout) klikBCAMethod);
//        alterViewElementKlikBCA();
//        initKlikBCAView();
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        UniqueCode.setText(cartStep1Data.getTransaction().getConfCodeIdr());
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            Password.setVisibility(View.GONE);
//        }
//    }
//
//    private void renderCreditCardMethod() {
//        CreditCardMethod.setVisibility(View.VISIBLE);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance5);
//        BalanceView = view.findViewById(R.id.tokopedia_balance5);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method5);
//        setSummaryView((LinearLayout) CreditCardMethod);
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice5);
//        mVoucher = (TextView) view.findViewById(R.id.voucher5);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt5);
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//
//        TextView adminFee = (TextView) view.findViewById(R.id.admin_fee5);
//        adminFee.setText(cartStep1Data.getTransaction().getCreditCard().getChargeIdr());
//        if (Integer.parseInt(cartStep1Data.getTransaction().getCreditCard().getCharge()) < 1) {
//            adminFee.setText(context.getString(R.string.title_gratis));
//        }
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            BalanceView.setVisibility(View.GONE);
//            TokopediaBalance.setVisibility(View.GONE);
//        }
//
//        PayBut.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(CreditCardActivity.createIntentWSV4(getActivity(), cartStep1Data), 0);
//                sendToGTMPayment();
//            }
//        });
//    }
//
//    private void renderInstallmentMethod() {
//        CreditCardMethod.setVisibility(View.VISIBLE);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance5);
//        BalanceView = view.findViewById(R.id.tokopedia_balance5);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method5);
//        setSummaryView((LinearLayout) CreditCardMethod);
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice5);
//        mVoucher = (TextView) view.findViewById(R.id.voucher5);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt5);
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//
//        TextView adminFee = (TextView) view.findViewById(R.id.admin_fee5);
//        adminFee.setText(cartStep1Data.getTransaction().getCreditCard().getChargeIdr());
//        if (Integer.parseInt(cartStep1Data.getTransaction().getCreditCard().getCharge()) < 1) {
//            adminFee.setText(context.getString(R.string.title_gratis));
//        }
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            BalanceView.setVisibility(View.GONE);
//            TokopediaBalance.setVisibility(View.GONE);
//        }
//
//        PayBut.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(CreditCardActivity.createInstallmentIntentWSV4(getActivity(), cartStep1Data), 0);
//                sendToGTMPayment();
//            }
//        });
//        // setInstallmentPayment(param, Result, GatewayID);
//    }
//
//    private void renderMandiriECashMethod() {
//        EcashMethod.setVisibility(View.VISIBLE);
//        mVoucher = view.findViewById(R.id.voucher4);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt4);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice4);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method4);
//        setSummaryView((LinearLayout) EcashMethod);
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance4);
//        TokopediaBalance.setText(MessageFormat.format("({0})",
//                cartStep1Data.getTransaction().getDepositAmountIdr()));
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        BalanceView = view.findViewById(R.id.tokopedia_balance4);
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            BalanceView.setVisibility(View.GONE);
//            TokopediaBalance.setVisibility(View.GONE);
//        }
//        if (cartStep1Data.getTransaction().getDiscountGateway() != null) {
//            if (!cartStep1Data.getTransaction().getDiscountGateway().equals("0")) {
//                EcashDiscount.setText(cartStep1Data.getTransaction().getDiscountGatewayIdr());
//                EcashDiscount.setVisibility(View.VISIBLE);
//                EcashDiscountText.setVisibility(View.VISIBLE);
//            }
//        }
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                PostStep2();
//                sendToGTMPayment();
//            }
//
//        });
//    }
//
//    private void renderMandiriKlikPayMethod() {
//        ClickpayMethod.setVisibility(View.VISIBLE);
//        mVoucher = view.findViewById(R.id.voucher3);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt3);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice3);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method3);
//        setSummaryView((LinearLayout) ClickpayMethod);
//        CardNumber = (EditText) view.findViewById(R.id.card_number);
//        TokenResponse = (EditText) view.findViewById(R.id.token_response);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance3);
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(), Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        BalanceView = view.findViewById(R.id.tokopedia_balance3);
//        TokopediaBalance.setText("(" + cartStep1Data.getTransaction().getDepositAmountIdr() + ")");
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            BalanceView.setVisibility(View.GONE);
//            TokopediaBalance.setVisibility(View.GONE);
//        }
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        Step1 = (EditText) view.findViewById(R.id.step_1);
//        Step2 = (EditText) view.findViewById(R.id.step_2);
//        Step3 = (EditText) view.findViewById(R.id.step_3);
//        Step4 = (EditText) view.findViewById(R.id.step_4);
//        Step1.setEnabled(false);
//        Step2.setEnabled(false);
//        Step3.setEnabled(false);
//        Step4.setEnabled(false);
//        Step1.setText("3");
//        Step3.setText(cartStep1Data.getTransaction().getPaymentLeft());
//        Step4.setText(cartStep1Data.getTransaction().getPaymentId());
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(TokenResponse.getWindowToken(), 0);
//                PostStep2();
//                sendToGTMPayment();
//            }
//
//        });
//        CardNumber.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String cardNo = CardNumber.getText().toString();
//                char TenDigit[] = new char[10];
//                if (cardNo.length() > 10) {
//                    int k = 0;
//                    for (int i = cardNo.length() - 10; i < cardNo.length(); i++) {
//                        TenDigit[k] = cardNo.charAt(i);
//                        k++;
//                    }
//                } else {
//                    int k = 0;
//                    for (int i = 0; i < cardNo.length(); i++) {
//                        TenDigit[k] = cardNo.charAt(i);
//                        k++;
//                    }
//                }
//                Step2.setText(String.valueOf(TenDigit));
//            }
//        });
//    }
//
//    private void renderBankTransferMethod() {
//        TransferMethod.setVisibility(View.VISIBLE);
//        mVoucher = view.findViewById(R.id.voucher2);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt2);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method2);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice2);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance2);
//        UniqueCode = (TextView) view.findViewById(R.id.unique_code);
//        setSummaryView((LinearLayout) TransferMethod);
//        Password = (EditText) view.findViewById(R.id.password_trf);
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalBeforeFeeIdr());
//        UniqueCode.setText(cartStep1Data.getTransaction().getConfCodeIdr());
//        TokopediaBalance.setText("(" + cartStep1Data.getTransaction().getDepositAmountIdr() + ")");
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(), Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        Password.setTypeface(Typeface.DEFAULT);
//        if (cartStep1Data.getTransaction().getDepositAmount().equals("0")) {
//            Password.setVisibility(View.GONE);
//        }
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                PasswordString = Password.getText().toString();
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
//                PostStep2();
//                sendToGTMPayment();
//            }
//
//        });
//    }
//
//    private void renderTokopediaBalanceMethod() {
//        SaldoMethod.setVisibility(View.VISIBLE);
//        mVoucher = view.findViewById(R.id.voucher1);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt1);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance);
//        setSummaryView((LinearLayout) SaldoMethod);
//        GrandTotal.setText(cartStep1Data.getTransaction().getPaymentLeftIdr());
//        Password = (EditText) view.findViewById(R.id.password);
//        PayementMethod.setText(cartStep1Data.getTransaction().getGatewayName());
//        TotalInvoice.setText(cartStep1Data.getTransaction().getGrandTotalIdr());
//        TokopediaBalance.setText("(" + cartStep1Data.getTransaction().getDepositAmountIdr() + ")");
//        setLoyaltyPoints(cartStep1Data.getTransaction().getLpAmountIdr(),
//                Integer.parseInt(cartStep1Data.getTransaction().getLpAmount()));
//        Password.setTypeface(Typeface.DEFAULT);
//        PayBut.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                PasswordString = Password.getText().toString();
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
//                PostStep2();
//                sendToGTMPayment();
//            }
//
//        });
//    }
//
//    public class ItemContainer {
//        LinearLayout ProdContainer;
//        ListProductCart ListProduct;
//        TextView vShopName;
//        TextView vTotalPrice;
//        TextView vShippingAddress;
//        TextView vShippingAgency;
//        TextView vTotalWeight;
//        TextView vSubTotal;
//        TextView vShippingCost;
//        TextView vInsurancePrice;
//        TextView tvAdditionalCost;
//        TextView tvAdditionalCostTitle;
//        ImageView EditBut;
//        ImageView DeleteBut;
//        TextView ErrorView1;
//        TextView ErrorView2;
//        View DetailInfo;
//        View DetailInfoBut;
//        View ErrorArea;
//        View EdtiDeleteArea;
//        Spinner Insurance;
//        Spinner Chosen;
//        ImageView Chevron;
//        CheckBox CheckedDropship;
//        EditText SenderName;
//        EditText SenderPhone;
//        View SenderForm;
//
//    }
//
//    public void addItemToLayout() {
//        lvContainer = (LinearLayout) view.findViewById(R.id.lv_cart);
//        LayoutInflater vi = LayoutInflater.from(context);
//        System.out.println("STEP: " + pName);
//        for (int i = 0; i < pName.size(); i++) {
//            final int currPos = i;
//            System.out.println("Step: " + i);
//            final ItemContainer itemTemp = new ItemContainer();
//            Item.add(vi.inflate(R.layout.listview_shop_cart, lvContainer, false));
//
//            itemTemp.ProdContainer = (LinearLayout) Item.get(i).findViewById(R.id.listview_prod);
//            itemTemp.vShopName = (TextView) Item.get(i).findViewById(R.id.shop_name);
//            itemTemp.vTotalPrice = (TextView) Item.get(i).findViewById(R.id.total_price);
//            itemTemp.vShippingAddress = (TextView) Item.get(i).findViewById(R.id.shipping_address);
//            itemTemp.vShippingAgency = (TextView) Item.get(i).findViewById(R.id.shipping_agency);
//            itemTemp.vTotalWeight = (TextView) Item.get(i).findViewById(R.id.total_weight);
//            itemTemp.vSubTotal = (TextView) Item.get(i).findViewById(R.id.sub_total);
//            itemTemp.vShippingCost = (TextView) Item.get(i).findViewById(R.id.shipping_cost);
//            itemTemp.vInsurancePrice = (TextView) Item.get(i).findViewById(R.id.insurance_price);
//            itemTemp.tvAdditionalCost = (TextView) Item.get(i).findViewById(R.id.tv_additional_cost);
//            itemTemp.tvAdditionalCostTitle = (TextView) Item.get(i).findViewById(R.id.tv_additional_cost_title);
//            itemTemp.ErrorView1 = (TextView) Item.get(i).findViewById(R.id.error1);
//            itemTemp.ErrorView2 = (TextView) Item.get(i).findViewById(R.id.error2);
//            itemTemp.DetailInfo = (View) Item.get(i).findViewById(R.id.detail_info);
//            itemTemp.DetailInfoBut = (View) Item.get(i).findViewById(R.id.detail_info_but);
//            itemTemp.ErrorArea = (View) Item.get(i).findViewById(R.id.error_area);
//            itemTemp.EditBut = (ImageView) Item.get(i).findViewById(R.id.edit);
//            itemTemp.DeleteBut = (ImageView) Item.get(i).findViewById(R.id.delete);
//            itemTemp.EdtiDeleteArea = (View) Item.get(i).findViewById(R.id.edit_delete_area);
//            itemTemp.Insurance = (Spinner) Item.get(i).findViewById(R.id.spinner_insurance);
//            itemTemp.Chosen = (Spinner) Item.get(i).findViewById(R.id.spinner_remaining_stock);
//            itemTemp.Chevron = (ImageView) Item.get(i).findViewById(R.id.chevron_sign);
//            itemTemp.CheckedDropship = (CheckBox) Item.get(i).findViewById(R.id.checked_send_dropship);
//            itemTemp.SenderName = (EditText) Item.get(i).findViewById(R.id.sender_name);
//            itemTemp.SenderPhone = (EditText) Item.get(i).findViewById(R.id.sender_phone);
//            itemTemp.SenderForm = Item.get(i).findViewById(R.id.sender_form);
//
//            itemTemp.ListProduct = new ListProductCart(context, itemTemp.ProdContainer, 2, null, pName.get(i), pImageUri.get(i), pPrice.get(i), pWeight.get(i),
//                    Qty.get(i), Notes.get(i), PriceTotal.get(i), pErrorMsg.get(i));
//
//            itemTemp.CheckedDropship.setChecked(isDropship.get(currPos));
//            if (isDropship.get(currPos)) {
//                itemTemp.SenderForm.setVisibility(View.VISIBLE);
//            } else {
//                itemTemp.SenderForm.setVisibility(View.GONE);
//            }
//            itemTemp.CheckedDropship.setEnabled(false);
//
//            itemTemp.SenderName.setText(DSNameVal.get(currPos));
//            itemTemp.SenderPhone.setText(DSPhoneVal.get(currPos));
//            itemTemp.SenderName.setEnabled(false);
//            itemTemp.SenderPhone.setEnabled(false);
//
//            ArrayAdapter<CharSequence> adapterInsurance = ArrayAdapter.createFromResource(context, R.array.insurance_option, android.R.layout.simple_spinner_item);
//            adapterInsurance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            itemTemp.Insurance.setAdapter(adapterInsurance);
//            itemTemp.Insurance.setEnabled(false);
//            if (InsuranceState.get(currPos) == 0 || InsuranceState.get(currPos) == 1) {
//                itemTemp.Insurance.setSelection(InsuranceState.get(currPos));
//            }
//
//            ArrayAdapter<CharSequence> adapterChosen = ArrayAdapter.createFromResource(context, R.array.chosen_option, android.R.layout.simple_spinner_item);
//            adapterInsurance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            itemTemp.Chosen.setAdapter(adapterChosen);
//            itemTemp.Chosen.setEnabled(false);
//            itemTemp.Chosen.setSelection(isChosen.get(currPos));
//
//            itemTemp.EdtiDeleteArea.setVisibility(View.GONE);
//            itemTemp.DetailInfo.setVisibility(View.GONE);
//            itemTemp.DetailInfoBut.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    if (ButtonDetailState.get(currPos) == 0) {
//                        itemTemp.DetailInfo.setVisibility(View.VISIBLE);
//                        itemTemp.Chevron.setImageResource(R.drawable.chevron_thin_up);
//                        ButtonDetailState.set(currPos, 1);
//                    } else {
//                        itemTemp.DetailInfo.setVisibility(View.GONE);
//                        itemTemp.Chevron.setImageResource(R.drawable.chevron_thin_down);
//                        ButtonDetailState.set(currPos, 0);
//                    }
//                }
//
//            });
//
//            if (Error1.get(i) != null) {
//                itemTemp.ErrorArea.setVisibility(View.VISIBLE);
//                itemTemp.ErrorView1.setText(Error1.get(i));
//                itemTemp.ErrorView2.setText(Error2.get(i));
//            }
//
//            itemTemp.vShopName.setText(MethodChecker.fromHtml(ShopName.get(i)));
//            setLuckyEmblem(luckyMerchantBadge.get(i), itemTemp.vShopName);
//            itemTemp.vTotalPrice.setText(TotalPrice.get(i));
//            itemTemp.vShippingAddress.setText(ShippingAddress.get(i));
//            itemTemp.vShippingAgency.setText(ShippingAgency.get(i));
//            itemTemp.vTotalWeight.setText(TotalWeight.get(i) + " Kg");
//            itemTemp.vSubTotal.setText(SubTotal.get(i));
//            itemTemp.vShippingCost.setText(ShippingCost.get(i));
//            itemTemp.vInsurancePrice.setText(InsurancePrice.get(i));
//            itemTemp.tvAdditionalCost.setVisibility(View.VISIBLE);
//            itemTemp.tvAdditionalCostTitle.setVisibility(View.VISIBLE);
//            itemTemp.tvAdditionalCost.setText("Rp " + additionalPrice.get(i));
//
//            ItemContent.add(itemTemp);
//
//            lvContainer.addView(Item.get(i));
//        }
//        MainView.setVisibility(View.VISIBLE);
//        activitycom.FinishLoading();
//        getAllImage();
//    }
//
//
//    public void getAllImage() {
//        for (int i = 0; i < pImageUri.size(); i++) {
//            for (int k = 0; k < pImageUri.get(i).size(); k++) {
//                new GetImage(i, k).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pImageUri.get(i).get(k));
//            }
//        }
//    }
//
//    public class GetImage extends AsyncTask<String, Void, Bitmap> {
//        private int pos1;
//        private int pos2;
//
//        public GetImage(int pos1, int pos2) {
//            this.pos1 = pos1;
//            this.pos2 = pos2;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            Bitmap bitmap = null;
//            System.out.println(params[0]);
//            if (params[0] != null) {
//                for (int i = 1; i <= 1; i++) {
//                    try {
//                        bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return bitmap;
//
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            pImage.get(pos1).set(pos2, bitmap);
//        }
//
//    }
//
//    private HttpClient createHttpClient() {
//        HttpParams params = new BasicHttpParams();
//        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
//        HttpProtocolParams.setUseExpectContinue(params, true);
//
//        SchemeRegistry schReg = new SchemeRegistry();
//        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
//
//        return new DefaultHttpClient(conMgr, params);
//    }
//
//    public class Postklipay extends AsyncTask<Void, String, String> {
//
//        @Override
//        protected String doInBackground(Void... arg0) {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//            nameValuePairs.add(new BasicNameValuePair("foo", "12345"));
//            nameValuePairs.add(new BasicNameValuePair("bar", "23456"));
//
//            HttpClient httpclient = createHttpClient();
//            HttpPost httppost = new HttpPost("https://202.6.215.230:8081/purchasing/purchase.do?action=loginRequest");
//            try {
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            } catch (UnsupportedEncodingException e) {
//
//                e.printStackTrace();
//            }
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//
//                return response.toString();
//            } catch (ClientProtocolException e) {
//
//                e.printStackTrace();
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String response) {
//            CommonUtils.dumper(response);
//        }
//
//    }
//
//    public void PostStep2() {
//        activitycom.Loading();
//        if (isDataFromWSV4) {
//            postStep2WSV4();
//        } else {
//            postStep2WSNew();
//        }
//    }
//
//    private void postStep2WSNew() {
//        NetworkHandler network = new NetworkHandler(context, TkpdUrl.TRANSACTION);
//        network.AddParam("gateway", Integer.toString(GatewayID));
//        network.AddParam("password", PasswordString);
//        network.AddParam("step", "2");
//        network.AddParam("token_cart", TokenCart);
//        network.setRetryPolicy(20000, 0, 0);
//        if (GatewayID == 9) {
//            network.AddParam("bca_user_id", klikBCAUSerID.getText().toString());
//        } else if (GatewayID == 4) {
//            network.AddParam("card_no", CardNumber.getText().toString());
//            network.AddParam("token_response", TokenResponse.getText().toString());
//        }
//        network.AddParam("method", "POST");
//
//        network.Commit(new NetworkHandlerListener() {
//
//            @Override
//            public void onSuccess(Boolean status) {
//
//                activitycom.FinishLoading();
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//                if (!Result.toString().equals("{}")) {
//                    if (!Result.isNull("link_mandiri")) {
//                        try {
//                            showDialog(Result.getString("link_mandiri"));
//                            postResponse = Result.toString();
//                        } catch (JSONException e) {
//
//                            e.printStackTrace();
//                        }
//                    } else {
//                        MainView.setVisibility(View.GONE);
//                        activitycom.TriggerToFinishTx(GatewayID, Result.toString());
//                    }
//                }
//
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                activitycom.FinishLoading();
//                ErrorMessage(MessageError);
//                //Password.setError(MessageError.get(0));
//            }
//        });
//    }
//
//    private void postStep2WSV4() {
//        Map<String, String> param = new HashMap<>();
//        param.put("gateway", cartStep1Data.getTransaction().getGateway());
//        param.put("password", PasswordString);
//        param.put("step", "2");
//        param.put("token", cartStep1Data.getTransaction().getToken());
//        if (GatewayID == 9) {
//            param.put("bca_user_id", klikBCAUSerID.getText().toString());
//        } else if (GatewayID == 4) {
//            param.put("card_no", CardNumber.getText().toString());
//            param.put("token_response", TokenResponse.getText().toString());
//        }
//        param.put("method", "POST");
//        netInteractor.postStep2(getActivity(), param, new PaymentNetInteractor.OnPostStep2() {
//            @Override
//            public void onSuccess(CartStep2Data data) {
//                if (data.getTransaction().getLinkMandiri() != null
//                        && !data.getTransaction().getLinkMandiri().equals("0")) {
//                    showDialog(data.getTransaction().getLinkMandiri());
//                } else {
//                    MainView.setVisibility(View.GONE);
//                    activitycom.toFinishCart(data);
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                activitycom.FinishLoading();
//                NetworkErrorHelper.showSnackbar(getActivity(), message);
//            }
//
//            @Override
//            public void onTimeout(String message) {
//                activitycom.FinishLoading();
//                NetworkErrorHelper.createSnackbarWithAction(getActivity(), message,
//                        new NetworkErrorHelper.RetryClickedListener() {
//                            @Override
//                            public void onRetryClicked() {
//                                PostStep2();
//                            }
//                        }).showRetrySnackbar();
//            }
//
//            @Override
//            public void onNoConnection() {
//                activitycom.FinishLoading();
//                NetworkErrorHelper.showDialog(getActivity(),
//                        new NetworkErrorHelper.RetryClickedListener() {
//                            @Override
//                            public void onRetryClicked() {
//                                PostStep2();
//                            }
//                        });
//            }
//        });
//    }
//
//    public void getDataQty(int pos, String data) {
//        System.out.println(data);
//    }
//
//    public void getDataNotes(int pos, String data) {
//        System.out.println(data);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//
//    }
//
//    private void showDialog(String url) {
//        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.addToBackStack(null);
//
//        // Create and show the dialog.
//        newFragment = new EcashDialog();
//        activitycom.setDialogStatus(true, 1, newFragment);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        newFragment.setCancelable(true);
//        newFragment.setArguments(bundle);
//        newFragment.show(ft, "dialog");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//    }
//
//    @Override
//    public void passIDToTx(String id) {
//        activitycom.Loading();
//        NetworkHandler network = new NetworkHandler(context, TkpdUrl.ECASH_VERIFY);
//        network.AddParam("mandiri_id", id);
//        network.AddParam("act", "validate_code");
//        network.Commit(new NetworkHandlerListener() {
//
//            @Override
//            public void onSuccess(Boolean status) {
//                activitycom.FinishLoading();
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//                try {
//                    if (Result.getString("status").equals("1")) {
//                        MainView.setVisibility(View.GONE);
//                        activitycom.TriggerToFinishTx(GatewayID, postResponse);
//                        activitycom.FinishLoading();
//                    } else {
//                        activitycom.FinishLoading();
//                        CommonUtils.UniversalToast(context, getString(R.string.error_failed_ecash));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                activitycom.FinishLoading();
//                ErrorMessage(MessageError);
//            }
//        });
//
//    }
//
//    @Override
//    public void completeKlikpay(String response) {
//        activitycom.Loading();
//        NetworkHandler network = new NetworkHandler(context, response);
//        network.AddParam("step", 2);
//        network.AddParam("gateway", "7");
//        network.Commit(new NetworkHandlerListener() {
//
//            @Override
//            public void onSuccess(Boolean status) {
//
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//                try {
//                    if (Result.getString("success").equals("1")) {
//                        MainView.setVisibility(View.GONE);
//                        activitycom.TriggerToFinishTx(GatewayID, Result.toString());
//                    } else {
//                        activitycom.FinishLoading();
//                        CommonUtils.UniversalToast(context, getString(R.string.error_failed_ecash) + " " + getString(R.string.error_extra));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                ErrorMessage(MessageError);
//
//            }
//        });
//    }
//
//
//    @Override
//    public void verifyBRIePayment(String tId) {
//
//        CommonUtils.dumper(TAG + " eBri verify " + tId + " url verify " + TkpdUrl.EBRI_VERIFY);
//
//        activitycom.Loading();
//        NetworkHandler network = new NetworkHandler(context, TkpdUrl.EBRI_VERIFY);
//        network.AddParam("tid", tId);
//        network.AddParam("act", "validate_payment");
//        network.Commit(new NetworkHandlerListener() {
//
//            @Override
//            public void onSuccess(Boolean status) {
//
//                CommonUtils.dumper(TAG + " eBri success response " + status);
//                activitycom.FinishLoading();
//
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//                CommonUtils.dumper(TAG + " eBri finish response " + Result.toString() + " " + postResponse);
//                try {
//                    if (Result.has("is_success")) {
//                        if (Result.getString("is_success").equals("1")) {
//                            MainView.setVisibility(View.GONE);
//                            activitycom.TriggerToFinishTx(GatewayID, postResponse);
//                        } else {
//                            activitycom.FinishLoading();
//                            activitycom.epayBRICancel();
//                            CommonUtils.UniversalToast(context, getString(R.string.error_failed_briepay) + " " + getString(R.string.error_extra));
//                        }
//                    } else {
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                activitycom.FinishLoading();
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                ErrorMessage(MessageError);
//                CommonUtils.dumper(TAG + " eBri error response " + MessageError.get(0));
//                activitycom.FinishLoading();
//            }
//        });
//
//    }
//
//    //TODO
//
//    private void PostStep2CreditCard() {
//        NetworkHandler network = new NetworkHandler(context, TkpdUrl.TRANSACTION);
//        network.AddParam("gateway", Integer.toString(GatewayID));
//        network.AddParam("step", "2");
//        network.AddParam("lp_flag", 1);
//        network.Commit(new NetworkHandlerListener() {
//            @Override
//            public void onSuccess(Boolean status) {
//
//            }
//
//            @Override
//            public void getResponse(JSONObject Result) {
//
//            }
//
//            @Override
//            public void getMessageError(ArrayList<String> MessageError) {
//                ErrorMessage(MessageError);
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data.getStringExtra("result").equals("Failed")) {
//            context.onBackPressed();
//        } else if (requestCode == 0 && resultCode == Activity.RESULT_OK && !data.getStringExtra("result").equals(FragmentCreditCard.sprintAsiaMarker)) {
//            MainView.setVisibility(View.GONE);
//            activitycom.TriggerToFinishTx(GatewayID, data.getStringExtra("result"));
//        } else if (requestCode == 0 && resultCode == Activity.RESULT_OK && data.getStringExtra("result").equals(FragmentCreditCard.sprintAsiaMarker)) {
//            MainView.setVisibility(View.GONE);
//            PostStep2CreditCard();
//            activitycom.TriggerToFinishTx(GatewayID, Response);
//        }
//    }
//
//    private void setLocalyticFlow() {
//        try {
//            CommonUtils.dumper("LocalTag : Cart 2");
//            String screenName = getString(R.string.cart_pg_2);
//            ScreenTracking.screenLoca(screenName);
//        } catch (NullPointerException e) {
//
//        }
//    }
//
//    private OnClickListener paymentButtonClick() {
//        return new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Password.isShown()) {
//                    PasswordString = Password.getText().toString();
//                }
//                InputMethodManager imm = (InputMethodManager) context.getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(Password.getWindowToken(), 0);
//                postKlikBCA();
//                sendToGTMPayment();
//            }
//        };
//    }
//
//    private void postKlikBCA() {
//        if (validateField()) {
//            PostStep2();
//        }
//    }
//
//    private void setKlikBCAParameter(JSONObject Result) {
//        initKlikBCAView();
//        initKlikBCAVariable(Result);
//    }
//
//    private void initKlikBCAView() {
//        mVoucher = view.findViewById(R.id.voucher_klikbca_summary);
//        mVoucherText = (TextView) view.findViewById(R.id.voucher_amt_klikbca_summary);
//        PayementMethod = (TextView) view.findViewById(R.id.pay_method_klikbca);
//        TotalInvoice = (TextView) view.findViewById(R.id.total_invoice_klikbca_summary);
//        TokopediaBalance = (TextView) view.findViewById(R.id.total_tokopedia_balance_klikbca_summary);
//        UniqueCode = (TextView) view.findViewById(R.id.unique_code);
//        Password = (EditText) view.findViewById(R.id.password_klikbca);
//        klikBCAUSerID = (EditText) view.findViewById(R.id.user_id_klikbca_summary);
//        Password.setTypeface(klikBCAUSerID.getTypeface());
//    }
//
//    private void initKlikBCAVariable(JSONObject Result) {
//        try {
//            GrandTotal.setText(Result.getString("payment_left_idr"));
//            PayementMethod.setText(Result.getString("gateway_name"));
//            TotalInvoice.setText(Result.getString("grand_total_idr"));
//            UniqueCode.setText(Result.getString("conf_code_idr"));
//            TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private boolean validateField() {
//        if (klikBCAUSerID.isShown() && klikBCAUSerID.getText().toString().length() < 1) {
//            klikBCAUSerID.setError(context.getString(R.string.error_field_required));
//            klikBCAUSerID.requestFocus();
//            return false;
//        } else if (Password.isShown() && PasswordString.isEmpty()) {
//            Password.setError(context.getString(R.string.error_field_required));
//            Password.requestFocus();
//            return false;
//        }
//        return true;
//    }
//
//    private void alterViewElementKlikBCA() {
//        klikBCAMethod.setVisibility(View.VISIBLE);
//        PayBut.setOnClickListener(paymentButtonClick());
//    }
//
//    private void alterViewElementIndomaret() {
//        indomaretMethod.setVisibility(View.VISIBLE);
//        indomaretAdminFee = (TextView) view.findViewById(R.id.admin_fee_klikbca_summary);
//        PayBut.setOnClickListener(paymentButtonClick());
//    }
//
//    private void setIndomaretPayment(JSONObject Result) {
//        initKlikBCAView();
//        initIndomaretVariable(Result);
//    }
//
//    private void initIndomaretVariable(JSONObject Result) {
//        try {
//            JSONObject indomaretData = Result.getJSONObject("indomaret");
//            GrandTotal.setText(indomaretData.getString("total_charge_real_idr"));
//            TotalInvoice.setText(indomaretData.getString("total_charge_real_idr"));
//            indomaretAdminFee.setText(indomaretData.getString("charge_real_idr"));
//            PayementMethod.setText(Result.getString("gateway_name"));
//            UniqueCode.setText(Result.getString("conf_code_idr"));
//            TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void ErrorMessage(ArrayList<String> MessageError) {
//        for (int i = 0; i < MessageError.size(); i++) {
//            Toast.makeText(context, MessageError.get(i), Toast.LENGTH_LONG).show();
//            if ((i + 1) < MessageError.size())
//                Toast.makeText(context, "No Error", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void setSummaryView(LinearLayout methodLayout) {
//        LinearLayout paymentAmountLayout;
//        LayoutInflater loyaltyLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View summaryLayout = loyaltyLayoutInflater.inflate(R.layout.cart_summary_grand_total, mainContainer, false);
//        paymentSummaryLayout = (LinearLayout) summaryLayout;
//        PayBut = (TextView) summaryLayout.findViewById(R.id.pay_button);
//        GrandTotal = (TextView) summaryLayout.findViewById(R.id.grand_total_price);
//        paymentAmountLayout = (LinearLayout) methodLayout.getChildAt(1);
//        paymentAmountLayout.addView(summaryLayout);
//    }
//
//    private void setLoyaltyPoints(String loyaltyPoints, int loyaltyInt) {
//        if (loyaltyInt > 0) {
//            LayoutInflater loyaltyLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View loyaltyLayout = loyaltyLayoutInflater.inflate(R.layout.loyalty_view, mainContainer, false);
//            TextView loyaltyBalance = (TextView) loyaltyLayout.findViewById(R.id.loyalty_balance);
//            TextView loyaltyTitle = (TextView) loyaltyLayout.findViewById(R.id.string_loyalty_point);
//            loyaltyBalance.setTypeface(null, Typeface.BOLD);
//            loyaltyTitle.setTypeface(null, Typeface.BOLD);
//            loyaltyBalance.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
//            loyaltyTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font_small));
//            paymentSummaryLayout.addView(loyaltyLayout, 0);
//            loyaltyBalance.setText("(" + loyaltyPoints + ")");
//        }
//    }
//
//    private void setCashbackAmount(String cashbackAmount, int cashbackInt) {
//        if (cashbackInt > 0) {
//            LayoutInflater loyaltyLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View cashbackLayout = loyaltyLayoutInflater.inflate(R.layout.cashback_view, mainContainer, false);
//            TextView cashbackValue = (TextView) cashbackLayout.findViewById(R.id.cashback_value);
//            MainView.addView(cashbackLayout, 7);
//            cashbackValue.setText(cashbackAmount);
//        }
//    }
//
//    private void setLuckyEmblem(int luckyMerchant, TextView shopNameTextView) {
//        if (luckyMerchant == 1) {
//            shopNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loyal_merchant, 0, 0, 0);
//        }
//    }
//
//    private OnClickListener payButtonInstallment(final CreditCardActivity.Param param) {
//        return new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(CreditCardActivity.createInstallmentIntent(getActivity(), param), 0);
//                sendToGTMPayment();
//            }
//        };
//    }
//
//    private void setInstallmentPayment(CreditCardActivity.Param param, JSONObject Result, int GatewayID) {
//        if (GatewayID == 12) {
//            ArrayList<String> installmentOptions = new ArrayList<>();
//            JSONArray installmentOptionsJSONArray;
//            try {
//                installmentOptionsJSONArray = Result.getJSONArray("installment_bank_option");
//                for (int i = 0; i < installmentOptionsJSONArray.length(); i++) {
//                    installmentOptions.add(installmentOptionsJSONArray.getJSONObject(i).toString());
//                }
//                param.BankInstallmentOptions = installmentOptions;
//                PayBut.setOnClickListener(payButtonInstallment(param));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void completeDynamicPay(String id) {
//
//
//    }
//
//    private void sendToGTMPayment(){
//        UnifyTracking.eventCartPayNow();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ScreenTracking.screen(getScreenName());
//    }
//}
