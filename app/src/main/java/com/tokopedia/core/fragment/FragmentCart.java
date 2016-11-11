package com.tokopedia.core.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tkpd.library.ui.utilities.NoResultHandler;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.Cart;
import com.tokopedia.core.EditAddressCart;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PaymentTracking;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.model.Basket;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.cart.interactor.CartRetrofitInteractor;
import com.tokopedia.core.cart.interactor.CartRetrofitInteractorImpl;
import com.tokopedia.core.cart.model.CartDestination;
import com.tokopedia.core.cart.model.CartModel;
import com.tokopedia.core.cart.model.CartProduct;
import com.tokopedia.core.cart.model.CartShipments;
import com.tokopedia.core.cart.model.CartShop;
import com.tokopedia.core.cart.model.GatewayList;
import com.tokopedia.core.customadapter.ListProductCart;
import com.tokopedia.core.discovery.activity.BrowseProductActivity;
import com.tokopedia.core.interfaces.CartInterfaces;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.NetworkHandler;
import com.tokopedia.core.network.NetworkHandler.NetworkHandlerListener;
import com.tokopedia.core.payment.adapter.PaymentGatewayAdapter;
import com.tokopedia.core.payment.interactor.PaymentNetInteractor;
import com.tokopedia.core.payment.interactor.PaymentNetInteractorImpl;
import com.tokopedia.core.payment.model.ParamParcel;
import com.tokopedia.core.payment.model.responsecartstep1.CarStep1Data;
import com.tokopedia.core.payment.model.responsedynamicpayment.DynamicPaymentData;
import com.tokopedia.core.payment.model.responsevoucher.VoucherCodeData;
import com.tokopedia.core.payment.receiver.PaymentResultReceiver;
import com.tokopedia.core.payment.services.PaymentIntentService;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCart extends Fragment implements CartInterfaces.FragmentCartCommunicator, PaymentResultReceiver.Receiver {

    private static final int PAYMENT_SALDO = 0;
    private static final int PAYMENT_TRANSFER = 1;
    private static final int PAYMENT_MANDIRI_CLICK = 4;
    private static final int PAYMENT_ECASH = 6;
    private static final int PAYMENT_BCA_KLIK = 7;
    private static final int PAYMENT_CREDIT_CARD = 8;
    private static final int PAYMENT_KLIKBCA = 9;
    private static final int PAYMENT_INDOMARET = 10;
    private static final int PAYMENT_EPAYBRI = 11;
    private static final int PAYMENT_INSTALLMENT = 12;

    private static final String NINJA_SHIPPING_AGENCY = "12";
    private static final String GOJEK_SHIPPING_AGENCY = "10";

    private static final String TAG = FragmentCart.class.getSimpleName();

    private String cacheSenderName[];
    private String cacheSenderPhone[];
    private boolean isItemHighLight = false;
    private boolean cacheIsDropshipper[];
    private View view;
    private Activity context;
    private List<GatewayList> gatewayList = new ArrayList<>();
    private ActivityCartCommunicator activitycom;
    private ArrayList<ArrayList<String>> pName = new ArrayList<>();
    private ArrayList<ArrayList<String>> pPrice = new ArrayList<>();
    private ArrayList<ArrayList<String>> pWeight = new ArrayList<>();
    private ArrayList<ArrayList<String>> Notes = new ArrayList<>();
    private ArrayList<ArrayList<String>> PriceTotal = new ArrayList<>();
    private ArrayList<ArrayList<String>> pImageUri = new ArrayList<>();
    private ArrayList<ArrayList<String>> pErrorMsg = new ArrayList<>();
    private ArrayList<ArrayList<String>> CartID = new ArrayList<>();
    private ArrayList<ArrayList<String>> ProdID = new ArrayList<>();
    private ArrayList<ArrayList<String>> ProdUrl = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> MinOrder = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> Qty = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> preorderStatus = new ArrayList<>();
    private ArrayList<ArrayList<String>> preorderPeriod = new ArrayList<>();
    private ArrayList<String> ShopName = new ArrayList<>();
    private ArrayList<Integer> luckyMerchantBadge = new ArrayList<>();
    private ArrayList<String> TotalPrice = new ArrayList<>();
    private ArrayList<String> TotalProductPrice = new ArrayList<>();
    private ArrayList<String> ShippingAddress = new ArrayList<>();
    private ArrayList<String> ShippingAgency = new ArrayList<>();
    private ArrayList<String> AddressReceiverPhones = new ArrayList<>();
    private ArrayList<String> Provincies = new ArrayList<>();
    private ArrayList<String> PostalCodes = new ArrayList<>();
    private ArrayList<String> Districts = new ArrayList<>();
    private ArrayList<String> AddressStreet = new ArrayList<>();
    private ArrayList<String> Cities = new ArrayList<>();
    private ArrayList<String> AddressNames = new ArrayList<>();
    private ArrayList<String> TotalWeight = new ArrayList<>();
    private ArrayList<String> SubTotal = new ArrayList<>();
    private ArrayList<String> ShippingCost = new ArrayList<>();
    private ArrayList<String> InsurancePrice = new ArrayList<>();
    private ArrayList<String> TitleInsurancePrice = new ArrayList<>();
    private ArrayList<String> ShippingID = new ArrayList<>();
    private ArrayList<String> AddressTitle = new ArrayList<>();
    private ArrayList<String> AddressName = new ArrayList<>();
    private ArrayList<String> latitude;
    private ArrayList<String> longitude;
    private ArrayList<String> SPid = new ArrayList<>();
    private ArrayList<String> AddrID = new ArrayList<>();
    private ArrayList<String> ShopID = new ArrayList<>();
    private ArrayList<String> Weight = new ArrayList<>();
    private ArrayList<String> QtyTotal = new ArrayList<>();
    private ArrayList<String> cartString = new ArrayList<>();
    private ArrayList<String> Error1 = new ArrayList<>();
    private ArrayList<String> Error2 = new ArrayList<>();
    private ArrayList<String> DSNameKey = new ArrayList<>();
    private ArrayList<String> DSPhoneKey = new ArrayList<>();
    private ArrayList<String> DSNameVal = new ArrayList<>();
    private ArrayList<String> DSPhoneVal = new ArrayList<>();
    private ArrayList<Boolean> isDropship = new ArrayList<>();
    private ArrayList<Boolean> isDelete = new ArrayList<>();
    private ArrayList<Integer> AllowCheckout = new ArrayList<>();
    private ArrayList<Integer> ButtonDetailState = new ArrayList<>();
    private ArrayList<Integer> InsuranceState = new ArrayList<>();
    private ArrayList<Integer> InsurancePos = new ArrayList<>();
    private ArrayList<Integer> isChosen = new ArrayList<>();
    private ArrayList<Integer> ForceInsurance = new ArrayList<>();
    private ArrayList<View> Item = new ArrayList<View>();
    private LinearLayout lvContainer;
    private String GrandTotal;
    private String grandTotalWithoutLP;
    private String grandTotalWithoutLPIDR;
    private String Deposit;
    private String TokenCart;
    private String Choosen;
    private String PaymentIDVal;
    private String DepositAmt;
    private String UseDeposit = "0";
    private String DropShipStr;
    private String VoucherCode = "";
    private String mEcashFlag = "0";
    private TextView TokopediaBalance;
    private TextView tvTickerGTM;
    private TextView TotalPayment;
    private TextView SaveBut;
    private TextView CancelBut;
    private TextView CheckoutBut;
    private TextView ErrorArea;
    private TextView paymentLabel;
    private TextView cashbackAmountTextView;
    private TextView loyaltyAmountTextView;
    private View PaymentWrapper;
    private EditText SaldoTokopedia;
    private TextView RpCurrency;
    private CheckBox VoucherUse;
    private EditText VoucherEdit;
    private ImageView paymentLogo;
    private TextInputLayout tilVoucher;
    private View VoucherCheck;
    private View VoucherLayout;
    private TextView VoucherResult;
    private TextView tvCariSekarang;
    private RelativeLayout paymentSummary;
    private View BalanceView;
    private View ButtonEditor;
    private TextView PaymentChooseBut;
    private LinearLayout MainView;
    private int EditPos;
    private Boolean EditMode = false;
    private Boolean BalanceState = false;
    private Boolean PaymentMethodSelected = false;
    private TkpdProgressDialog progressdialog;
    private int TempDepositLength;
    private NoResultHandler noResult;
    private int DepositNum;
    private LocalCacheHandler cache;
    private TextView PromoText;
    private ViewGroup mainContainer;
    private Checkout checkoutAnalytics = new Checkout();
    private Basket basket = new Basket();
    private ArrayList<ItemContainer> ItemContent = new ArrayList<>();
    private CartRetrofitInteractor cartRetrofitInteractor;
    private LinearLayout holderCashback;
    private Map<String, String> poolDynamicType = new HashMap<>();
    private PaymentResultReceiver atcReceiver;
    private PaymentNetInteractor interactor;
    private PopupMenu popupMenu;

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case PaymentIntentService.RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_SUCCESS:
                DynamicPaymentData data = resultData
                        .getParcelable(PaymentIntentService.EXTRA_RESULT_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT);
                activitycom.FinishLoading();
                activitycom.toDynamicPayment(data);
                break;
            case PaymentIntentService.RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_ERROR:
                activitycom.FinishLoading();
                NetworkErrorHelper.showSnackbar(getActivity(),
                        resultData.getString(PaymentIntentService.EXTRA_RESULT_MESSAGE,
                                getString(R.string.default_request_error_unknown)));
                break;
            case PaymentIntentService.RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_RUNNING:
                activitycom.Loading();
                break;
            case PaymentIntentService.RESULT_GET_PARAMETER_DYNAMIC_PAYMENT_NO_CONNECTION:
                activitycom.Loading();
                NetworkErrorHelper.showDialog(getActivity(),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                postDynamicPaymentWSV4Intent();
                            }
                        });
                break;
            case PaymentIntentService.RESULT_STEP_1_PAYMENT_SUCCESS:
                CarStep1Data cartData = resultData.getParcelable(PaymentIntentService.EXTRA_RESULT_STEP_1_PAYMENT);
                MainView.setVisibility(View.GONE);
                activitycom.toSummaryCart(cartData);
                break;
            case PaymentIntentService.RESULT_STEP_1_PAYMENT_ERROR:
                activitycom.FinishLoading();
                NetworkErrorHelper.showSnackbar(getActivity(),
                        resultData.getString(PaymentIntentService.EXTRA_RESULT_MESSAGE,
                                getString(R.string.default_request_error_unknown)));
                break;
            case PaymentIntentService.RESULT_STEP_1_PAYMENT_RUNNING:
                activitycom.Loading();
                break;
            case PaymentIntentService.RESULT_STEP_1_NO_CONNECTION:
                activitycom.FinishLoading();
                NetworkErrorHelper.showDialog(getActivity(),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                postStep1WSV4Intent();
                            }
                        });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (interactor != null)
            interactor.unSubscribeObservable();
    }

    public interface ActivityCartCommunicator {
        void TriggerToAddFragment(String response);

        void TriggerReloadFragment();

        void Loading();

        void FinishLoading();

        void toDynamicPayment(DynamicPaymentData data);

        void toSummaryCart(CarStep1Data cartData);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        context = getActivity();
        ((Cart) context).fragcom = this;
        cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
        activitycom = (ActivityCartCommunicator) context;
        super.onAttach(activity);

    }

    private void saveCacheSender() {
        getActivity().getIntent().putExtra("cacheIsDropshipper", cacheIsDropshipper);
        getActivity().getIntent().putExtra("cacheSenderName", cacheSenderName);
        getActivity().getIntent().putExtra("cacheSenderPhone", cacheSenderPhone);
    }

    private void loadCacheSender() {
        cacheIsDropshipper = getActivity().getIntent().getBooleanArrayExtra("cacheIsDropshipper");
        cacheSenderName = getActivity().getIntent().getStringArrayExtra("cacheSenderName");
        cacheSenderPhone = getActivity().getIntent().getStringArrayExtra("cacheSenderPhone");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cartRetrofitInteractor = new CartRetrofitInteractorImpl();
        interactor = new PaymentNetInteractorImpl();
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        atcReceiver = new PaymentResultReceiver(new Handler(), this);
        mainContainer = container;
        PromoText = (TextView) view.findViewById(R.id.promo_text);
        tvTickerGTM = (TextView) view.findViewById(R.id.tv_ticker_gtm);
        progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.MAIN_PROGRESS, view);
        progressdialog.setLoadingViewId(R.id.include_loading);
        progressdialog.showDialog();
        TotalPayment = (TextView) view.findViewById(R.id.total_payment);
        TokopediaBalance = (TextView) view.findViewById(R.id.tokopedia_balance);
        SaveBut = (TextView) view.findViewById(R.id.save_but);
        CancelBut = (TextView) view.findViewById(R.id.cancel_but);
        CheckoutBut = (TextView) view.findViewById(R.id.checkout_but);
        paymentLabel = (TextView) view.findViewById(R.id.payment_label);
        SaldoTokopedia = (EditText) view.findViewById(R.id.et_tkpd_balance);
        RpCurrency = (TextView) view.findViewById(R.id.rp_currency);
        ErrorArea = (TextView) view.findViewById(R.id.error_payment);
        BalanceView = (View) view.findViewById(R.id.balance_view);
        ButtonEditor = (View) view.findViewById(R.id.button_editor);
        MainView = (LinearLayout) view.findViewById(R.id.main_view);
        PaymentWrapper = view.findViewById(R.id.payment_wrapper);
        PaymentChooseBut = (TextView) view.findViewById(R.id.spinner_payment);
        VoucherUse = (CheckBox) view.findViewById(R.id.use_voucher_but);
        VoucherLayout = view.findViewById(R.id.use_voucher_layout);
        VoucherLayout.setVisibility(View.GONE);
        VoucherEdit = (EditText) view.findViewById(R.id.edit_voucher);
        VoucherCheck = view.findViewById(R.id.check_voucher);
        VoucherResult = (TextView) view.findViewById(R.id.voucher_text);
        paymentSummary = (RelativeLayout) view.findViewById(R.id.payment_area);
        tvCariSekarang = (TextView) view.findViewById(R.id.find_now);
        tilVoucher = (TextInputLayout) view.findViewById(R.id.til_voucher);
        paymentLogo = (ImageView) view.findViewById(R.id.logo_payment);
        lvContainer = (LinearLayout) view.findViewById(R.id.lv_cart);
        holderCashback = (LinearLayout) view.findViewById(R.id.holder_cashback);
        BalanceView.setVisibility(View.GONE);
        MainView.setVisibility(View.GONE);
        // Untuk redirect ke index home
        noResult = new NoResultHandler(context, view, R.string.error_no_cart_1, R.string.error_no_cart_2, BrowseProductActivity.getDefaultMoveToIntent(getActivity()));

        try {
            noResult.setResultImage(context, context.getResources().getDrawable(R.drawable.status_no_result));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        latitude = new ArrayList<>();
        longitude = new ArrayList<>();

        SaldoTokopedia.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TempDepositLength != SaldoTokopedia.length() && SaldoTokopedia.length() > 0) {
                    TempDepositLength = SaldoTokopedia.length();
                    CurrencyFormatHelper.SetToRupiah(SaldoTokopedia);
                    SaldoTokopedia.setSelection(TempDepositLength);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        VoucherUse.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    visibleTopPointView(View.GONE);
                    setCashbackAmountVisibility(View.GONE);
                    TotalPayment.setText(grandTotalWithoutLPIDR);
                    VoucherLayout.setVisibility(View.VISIBLE);
                } else {
                    visibleTopPointView(View.VISIBLE);
                    TotalPayment.setText(GrandTotal);
                    tilVoucher.setError(null);
                    setCashbackAmountVisibility(View.VISIBLE);
                    VoucherLayout.setVisibility(View.GONE);
                }
            }
        });

        VoucherEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (VoucherEdit.length() > 0) {
                        tilVoucher.setError(null);
                        CheckVoucher(VoucherEdit.getText().toString(), false);
                        return true;
                    } else {
                        tilVoucher.setError(getString(R.string.msg_voucher_empty));
                        return false;
                    }
                }
                return false;
            }
        });

        VoucherEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tilVoucher.isErrorEnabled()) {
                    tilVoucher.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        VoucherCheck.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (VoucherEdit.length() > 0) {
                    tilVoucher.setError(null);
                    CheckVoucher(VoucherEdit.getText().toString(), false);
                } else {
                    tilVoucher.setError(getString(R.string.msg_voucher_empty));
                }
            }
        });
        CheckoutBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int count = 0;
                ErrorArea.setVisibility(View.GONE);
                for (int i = 0; i < ItemContent.size(); i++) {
                    ItemContent.get(i).SenderNameWrapper.setErrorEnabled(false);
                    ItemContent.get(i).SenderPhoneWrapper.setErrorEnabled(false);
                }

                if (PaymentMethodSelected) {
                    Boolean ValidDS = true;
                    for (int i = 0; i < isDropship.size(); i++) {
                        if (!isDelete.get(i) && isDropship.get(i)) {
                            if (DSNameVal.get(i).equals("")) {
                                ValidDS = false;
                                ItemContent.get(i).SenderNameWrapper.setError(context.getString(R.string.error_field_required));
                            } else if (DSNameVal.get(i).length() < 3) {
                                ValidDS = false;
                                ItemContent.get(i).SenderNameWrapper.setError(context.getString(R.string.error_sender_name));
                            }
                            if (DSPhoneVal.get(i).equals("")) {
                                ValidDS = false;
                                ItemContent.get(i).SenderPhoneWrapper.setError(context.getString(R.string.error_field_required));
                            } else if (DSPhoneVal.get(i).length() < 6) {
                                ValidDS = false;
                                ItemContent.get(i).SenderPhoneWrapper.setError(context.getString(R.string.error_sender_phone));
                            }


                        }
                    }
                    if (ValidDS) {
                        for (int i = 0; i < ShopID.size(); i++) {
                            if (AllowCheckout.get(i) == 1) {
                                if (isChosen.get(i) == 1) {
                                    if (count != 0) {
                                        Choosen = Choosen + "*~*" + cartString.get(i);
                                    } else {
                                        Choosen = cartString.get(i);
                                    }

                                    count++;
                                }
                            }
                        }
                        int k = 0;
                        for (int i = 0; i < DSNameVal.size(); i++) {
                            if (AllowCheckout.get(i) == 1 && !isDelete.get(i)) {
                                if (isDropship.get(i)) {
                                    DSNameKey.set(i, ("dropship_name~" + cartString.get(i)).replace("~", "-"));
                                    DSPhoneKey.set(i, ("dropship_telp~" + cartString.get(i)).replace("~", "-"));
                                    if (k != 0) {
                                        DropShipStr = DropShipStr + "*~*" + cartString.get(i);
                                    } else {
                                        DropShipStr = (cartString.get(i));
                                    }
                                    k++;
                                } else {
                                    DSNameKey.set(i, "");
                                    DSPhoneKey.set(i, "");
                                }
                            } else {
                                DSNameKey.set(i, "");
                                DSPhoneKey.set(i, "");
                            }
                        }
                        saveCacheSender();
                        DepositAmt = SaldoTokopedia.getText().toString().replace(",", "").replace(".", "");
                        KeyboardHandler.DropKeyboard(context, SaldoTokopedia);
                        if (VoucherEdit.getText().toString().equals(VoucherCheck)
                                || VoucherEdit.length() == 0 || !VoucherUse.isChecked()) {
                            doCheckoutCart();
                        } else {
                            CheckVoucher(VoucherEdit.getText().toString(), true);
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_cart), Toast.LENGTH_LONG).show();
                    }
                } else {
                    PaymentWrapper.performClick();
                }
                UnifyTracking.eventCartCheckout();
            }
        });

        PaymentWrapper.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
                UnifyTracking.eventCartPayment();
            }
        });

        CancelBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CancelEdit();
            }

        });
        SaveBut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String params = ItemContent.get(EditPos).ListProduct.GetEditData();
                if (params != null) {
                    progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
                    progressdialog.showDialog();
                    BulkEditWS4(params, ShopID.get(EditPos), AddrID.get(EditPos), ShippingID.get(EditPos), SPid.get(EditPos), "1", cartString.get(EditPos));
                    ButtonEditor.setVisibility(View.GONE);
                    CheckoutBut.setEnabled(true);
                    EditMode = false;
                    isItemHighLight = false;
                    ItemContent.get(EditPos).MainView.setBackgroundResource(R.drawable.cards_ui_2);
                }
            }

        });
        tvCariSekarang.setVisibility(View.VISIBLE);
        tvCariSekarang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BrowseProductActivity.getDefaultMoveToIntent(getActivity()));
            }
        });
        GetCartInfoRetrofit();
        setLocalyticFlow();
        return view;
    }

    private void doCheckoutCart() {
        if (!poolDynamicType.get(PaymentIDVal).isEmpty()) {
            postDynamicPaymentWSV4Intent();
        } else {
            postStep1WSV4Intent();
        }
    }


    private void visibleTopPointView(int flag) {
        if (paymentSummary.getChildAt(3) != null && paymentSummary.getChildAt(3).getTag() != null
                && paymentSummary.getChildAt(3).getTag().equals("TOP_POINT_VIEW")) {
            paymentSummary.getChildAt(3).setVisibility(flag);
        }
    }

    private void setCashbackAmountVisibility(int flag) {
        holderCashback.setVisibility(flag);
    }

    public void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater li = LayoutInflater.from(context);
        @SuppressLint("InflateParams") final View promptsView = li.inflate(R.layout.choose_payment_dialog, null);
        alertDialogBuilder.setView(promptsView);
        final ListView lvPayment = (ListView) promptsView.findViewById(R.id.lv_payment_method);
        final PaymentGatewayAdapter lvadapter = new PaymentGatewayAdapter(getActivity(), gatewayList);
        lvPayment.setAdapter(lvadapter);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        lvPayment.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                alertDialog.dismiss();
                PaymentMethodSelected = true;
                PaymentChooseBut.setText(lvadapter.getItem(position).getGatewayName());
                paymentLogo.setVisibility(View.VISIBLE);
                ImageHandler.LoadImage(paymentLogo, lvadapter.getItem(position).getGatewayImage());
                PromoText.setVisibility(View.GONE);
                switch (lvadapter.getItem(position).getGateway()) {
                    case 0:
                        UnifyTracking.eventCartDeposit();
                        RpCurrency.setVisibility(View.GONE);
                        SaldoTokopedia.setVisibility(View.GONE);
                        PaymentIDVal = "0";
                        break;
                    case 1:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);

                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = "1";
                        break;
                    case 4:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = "4";
                        break;
                    case 6:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        if (mEcashFlag.equals("1")) {
                            PromoText.setVisibility(View.VISIBLE);
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        } else
                            PromoText.setVisibility(View.GONE);
                        PaymentIDVal = "6";
                        break;
                    case 7:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = "7";
                        break;

                    case 8:
                        RpCurrency.setVisibility(View.GONE);
                        SaldoTokopedia.setVisibility(View.VISIBLE);
                        PaymentIDVal = "8";
                        break;
                    case 9:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }

                        PaymentIDVal = "9";
                        break;
                    case 10:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = "10";
                        break;
                    case 11:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = "11";
                        break;
                    case 12:
                        RpCurrency.setVisibility(View.GONE);
                        SaldoTokopedia.setVisibility(View.VISIBLE);
                        PaymentIDVal = "12";
                        break;
                    default:
                        if (DepositNum > 0) {
                            RpCurrency.setVisibility(View.VISIBLE);
                            SaldoTokopedia.setVisibility(View.VISIBLE);
                        } else {
                            RpCurrency.setVisibility(View.GONE);
                            SaldoTokopedia.setVisibility(View.GONE);
                        }
                        PaymentIDVal = lvadapter.getItem(position).getGateway() + "";
                        break;
                }
                paymentLabel.setVisibility(View.VISIBLE);
                paymentLabel.setText(lvadapter.getItem(position).getFeeInformation(getActivity()));
                ErrorArea.setVisibility(View.GONE);

            }
        });
    }

    public class ItemContainer {
        LinearLayout ProdContainer;
        ListProductCart ListProduct;
        TextView vShopName;
        TextView vTotalPrice;
        TextView vShippingAddress;
        TextView vShippingAgency;
        TextView vTotalWeight;
        TextView vSubTotal;
        TextView vShippingCost;
        TextView vInsurancePrice;
        TextView vTitleInsurancePrice;
        TextView ErrorView1;
        TextView ErrorView2;
        View DetailInfo;
        View DetailInfoBut;
        View ErrorArea;
        View MainView;
        View ButOverflow;
        Spinner Insurance;
        Spinner Chosen;
        ImageView Chevron;
        CheckBox CheckedDropship;
        EditText SenderName;
        EditText SenderPhone;
        View SenderForm;
        TextInputLayout SenderNameWrapper;
        TextInputLayout SenderPhoneWrapper;
    }

    public void resetCartItemVar() {
        lvContainer.removeAllViews();
        Item.clear();
        ItemContent.clear();
        ProdUrl.clear();
        pErrorMsg.clear();
        pImageUri.clear();
        preorderPeriod.clear();
        preorderStatus.clear();
        Qty.clear();
        PriceTotal.clear();
        Notes.clear();
        pWeight.clear();
        pPrice.clear();
        pName.clear();
        CartID.clear();
        ProdID.clear();
        MinOrder.clear();
        ButtonDetailState.clear();
        isDropship.clear();
        DSPhoneKey.clear();
        DSNameKey.clear();
        DSPhoneVal.clear();
        DSNameVal.clear();
        isChosen.clear();
        isDelete.clear();
        Error1.clear();
        Error2.clear();
        ForceInsurance.clear();
        AllowCheckout.clear();
        TitleInsurancePrice.clear();
        InsurancePrice.clear();
        ShippingCost.clear();
        SubTotal.clear();
        TotalWeight.clear();
        TotalPrice.clear();
        Weight.clear();
        QtyTotal.clear();
        InsurancePos.clear();
        InsuranceState.clear();
        cartString.clear();
        ShippingID.clear();
        SPid.clear();
        ShippingAgency.clear();
        AddressReceiverPhones.clear();
        Provincies.clear();
        PostalCodes.clear();
        Districts.clear();
        Cities.clear();
        AddressStreet.clear();
        AddressNames.clear();
        longitude.clear();
        latitude.clear();
        AddressName.clear();
        AddressTitle.clear();
        AddrID.clear();
        ShippingAddress.clear();
        luckyMerchantBadge.clear();
        ShopID.clear();
        ShopName.clear();
    }

    public void addItemToLayout() {
        lvContainer = (LinearLayout) view.findViewById(R.id.lv_cart);
        LayoutInflater vi = LayoutInflater.from(context);
        for (int i = 0; i < pName.size(); i++) {
            final int currPos = i;
            final ItemContainer itemTemp = new ItemContainer();
            Item.add(vi.inflate(R.layout.listview_shop_cart, lvContainer, false));

            itemTemp.ProdContainer = (LinearLayout) Item.get(i).findViewById(R.id.listview_prod);
            itemTemp.vShopName = (TextView) Item.get(i).findViewById(R.id.shop_name);
            itemTemp.vTotalPrice = (TextView) Item.get(i).findViewById(R.id.total_price);
            itemTemp.vShippingAddress = (TextView) Item.get(i).findViewById(R.id.shipping_address);
            itemTemp.vShippingAgency = (TextView) Item.get(i).findViewById(R.id.shipping_agency);
            itemTemp.vTotalWeight = (TextView) Item.get(i).findViewById(R.id.total_weight);
            itemTemp.vSubTotal = (TextView) Item.get(i).findViewById(R.id.sub_total);
            itemTemp.vShippingCost = (TextView) Item.get(i).findViewById(R.id.shipping_cost);
            itemTemp.vInsurancePrice = (TextView) Item.get(i).findViewById(R.id.insurance_price);
            itemTemp.vTitleInsurancePrice = (TextView) Item.get(i).findViewById(R.id.title_insurance_price);
            itemTemp.ErrorView1 = (TextView) Item.get(i).findViewById(R.id.error1);
            itemTemp.ErrorView2 = (TextView) Item.get(i).findViewById(R.id.error2);
            itemTemp.DetailInfo = Item.get(i).findViewById(R.id.detail_info);
            itemTemp.DetailInfoBut = Item.get(i).findViewById(R.id.detail_info_but);
            itemTemp.ErrorArea = Item.get(i).findViewById(R.id.error_area);
            itemTemp.MainView = Item.get(i).findViewById(R.id.main_view);
            itemTemp.ButOverflow = Item.get(i).findViewById(R.id.but_overflow);
            itemTemp.Insurance = (Spinner) Item.get(i).findViewById(R.id.spinner_insurance);
            itemTemp.Chosen = (Spinner) Item.get(i).findViewById(R.id.spinner_remaining_stock);
            itemTemp.Chevron = (ImageView) Item.get(i).findViewById(R.id.chevron_sign);
            itemTemp.CheckedDropship = (CheckBox) Item.get(i).findViewById(R.id.checked_send_dropship);
            itemTemp.SenderName = (EditText) Item.get(i).findViewById(R.id.sender_name);
            itemTemp.SenderPhone = (EditText) Item.get(i).findViewById(R.id.sender_phone);
            itemTemp.SenderForm = Item.get(i).findViewById(R.id.sender_form);
            itemTemp.SenderNameWrapper = (TextInputLayout) Item.get(i).findViewById(R.id.sender_name_wrapper);
            itemTemp.SenderPhoneWrapper = (TextInputLayout) Item.get(i).findViewById(R.id.sender_phone_wrapper);
            itemTemp.ListProduct = new ListProductCart(context, itemTemp.ProdContainer,
                    FragmentCart.this, 1, CartID.get(i), pName.get(i), pImageUri.get(i),
                    pPrice.get(i), pWeight.get(i), Qty.get(i), Notes.get(i), PriceTotal.get(i),
                    pErrorMsg.get(i), MinOrder.get(i), ProdID.get(i), ProdUrl.get(i),
                    preorderStatus.get(i), preorderPeriod.get(i));
            ArrayAdapter<CharSequence> adapterInsurance = ArrayAdapter.createFromResource(context,
                    R.array.insurance_option, android.R.layout.simple_spinner_item);
            adapterInsurance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemTemp.Insurance.setAdapter(adapterInsurance);
            if (InsuranceState.get(currPos) == 0 || InsuranceState.get(currPos) == 1) {
                itemTemp.Insurance.setEnabled(false);
                itemTemp.Insurance.setClickable(false);
                itemTemp.Insurance.setBackgroundColor(context.getResources().getColor(R.color.white));
                itemTemp.Insurance.setSelection(InsuranceState.get(currPos));
            } else {
                itemTemp.Insurance.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long arg3) {
                        int Insurance;
                        Insurance = pos == 0 ? 1 : 0;
                        if (InsurancePos.get(currPos) != pos) {
                            progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
                            progressdialog.showDialog();
                            EditInsuranceWS4(AddrID.get(currPos), Integer.toString(Insurance),
                                    ShippingID.get(currPos), SPid.get(currPos), ShopID.get(currPos));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {


                    }

                });
                itemTemp.Insurance.setSelection(InsurancePos.get(currPos));
            }

            ArrayAdapter<CharSequence> adapterChosen = ArrayAdapter.createFromResource(context,
                    R.array.chosen_option, android.R.layout.simple_spinner_item);
            adapterChosen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemTemp.Chosen.setAdapter(adapterChosen);
            itemTemp.Chosen.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int pos, long arg3) {
                    isChosen.set(currPos, pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {


                }

            });
            loadCacheSender();
            if (ShippingID.get(currPos).equals(NINJA_SHIPPING_AGENCY) || ShippingID.get(currPos).equals(GOJEK_SHIPPING_AGENCY)) {
                itemTemp.CheckedDropship.setVisibility(View.GONE);
            } else {
                itemTemp.CheckedDropship.setVisibility(View.VISIBLE);
            }
            if (cacheIsDropshipper == null || cacheIsDropshipper.length != pName.size())
                cacheIsDropshipper = new boolean[pName.size()];
            if (cacheSenderName == null || cacheSenderName.length != pName.size())
                cacheSenderName = new String[pName.size()];
            if (cacheSenderPhone == null || cacheSenderPhone.length != pName.size())
                cacheSenderPhone = new String[pName.size()];

            itemTemp.CheckedDropship.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cacheIsDropshipper[currPos] = isChecked;
                    if (!isChecked) {
                        isDropship.set(currPos, false);
                        itemTemp.SenderForm.setVisibility(View.GONE);
                    } else {
                        isDropship.set(currPos, true);
                        UnifyTracking.eventCartDropshipper();

                        itemTemp.SenderForm.setVisibility(View.VISIBLE);
                    }

                }
            });

            itemTemp.SenderName.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    cacheSenderName[currPos] = s.toString();
                    DSNameVal.set(currPos, s.toString());
                    if (s.length() < 3) {
                        if (s.length() == 0) {
                            itemTemp.SenderNameWrapper.setError(context.getString(R.string.error_field_required));
                        } else {
                            itemTemp.SenderNameWrapper.setError(context.getString(R.string.error_sender_name));
                        }
                    } else {
                        itemTemp.SenderNameWrapper.setError(null);
                    }
                }
            });

            itemTemp.SenderPhone.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    cacheSenderPhone[currPos] = s.toString();
                    DSPhoneVal.set(currPos, s.toString());
                    if (s.length() < 6) {
                        if (s.length() == 0) {
                            itemTemp.SenderPhoneWrapper.setError(context.getString(R.string.error_field_required));
                        } else {
                            itemTemp.SenderPhoneWrapper.setError(context.getString(R.string.error_sender_phone));
                        }
                    } else {
                        itemTemp.SenderPhoneWrapper.setError(null);
                    }

                }
            });
            itemTemp.CheckedDropship.setChecked(cacheIsDropshipper[currPos]);

            if (cacheSenderName[currPos] != null && cacheSenderName[currPos].length() > 0) {
                itemTemp.SenderName.setText(cacheSenderName[currPos]);
            }
            if (cacheSenderPhone[currPos] != null && cacheSenderPhone[currPos].length() > 0) {
                itemTemp.SenderPhone.setText(cacheSenderPhone[currPos]);
            }

            if (Qty.get(i).size() <= 1 && Qty.get(i).size() > 0 && Qty.get(i).get(0) <= 1) {
                itemTemp.Chosen.setEnabled(false);
                itemTemp.Chosen.setClickable(false);
                itemTemp.Chosen.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                itemTemp.Chosen.setEnabled(true);
                itemTemp.Chosen.setClickable(true);
            }

            itemTemp.DetailInfo.setVisibility(View.GONE);
            itemTemp.DetailInfoBut.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (ButtonDetailState.get(currPos) == 0) {
                        itemTemp.DetailInfo.setVisibility(View.VISIBLE);
                        itemTemp.Chevron.setImageResource(R.drawable.ic_chevron_up);
                        ButtonDetailState.set(currPos, 1);
                    } else {
                        itemTemp.DetailInfo.setVisibility(View.GONE);
                        itemTemp.Chevron.setImageResource(R.drawable.ic_chevron_down);
                        ButtonDetailState.set(currPos, 0);
                    }
                }

            });

            itemTemp.vShopName.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!EditMode) {
                        Intent intent = new Intent(context, ShopInfoActivity.class);
                        Bundle bundle = ShopInfoActivity.createBundle(ShopID.get(currPos), "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });

            itemTemp.ButOverflow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO dont show dialog when, other item in highlight
                    if (isItemHighLight) {
                        return;
                    }
                     popupMenu = new PopupMenu(context, v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.cart_item_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R2.id.action_cart_edit:
                                    if (!EditMode) {
                                        isItemHighLight = true;
                                        itemTemp.ListProduct.TriggerEdit();
                                        EditMode = true;
                                        ButtonEditor.setVisibility(View.VISIBLE);
                                        EditPos = currPos;
                                        CheckoutBut.setEnabled(false);
                                        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
                                                LayoutParams.MATCH_PARENT);
                                        param.setMargins(0, 0, 0,
                                                (int) context.getResources().getDimension(R.dimen.btn_height));
                                        lvContainer.setLayoutParams(param);
                                        itemTemp.MainView.setBackgroundResource(R.drawable.cards_highlight);
                                    }
                                    return true;
                                case R2.id.action_cart_delete:
                                    isItemHighLight = true;
                                    CancelEdit();
                                    AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
                                    myAlertDialog.setTitle(context.getString(R.string.title_cancel_confirm));
                                    myAlertDialog.setMessage(context.getString(R.string.msg_cancel_1)
                                            + " " + ShopName.get(currPos) + " "
                                            + context.getString(R.string.msg_cancel_3) + " " + TotalPrice.get(currPos));

                                    myAlertDialog.setPositiveButton(context.getString(R.string.title_yes),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    progressdialog = new TkpdProgressDialog(context,
                                                            TkpdProgressDialog.NORMAL_PROGRESS);
                                                    progressdialog.showDialog();
                                                    CancelCartWS4(AddrID.get(currPos), ShippingID.get(currPos),
                                                            SPid.get(currPos), ShopID.get(currPos));
                                                }

                                            });

                                    myAlertDialog.setNegativeButton(context.getString(R.string.title_no),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });
                                    myAlertDialog.show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();

                }
            });

            if (Error1.get(i) != null) {
                itemTemp.ErrorArea.setVisibility(View.VISIBLE);
                itemTemp.ErrorView1.setText(Html.fromHtml(Error1.get(i)));
                itemTemp.ErrorView2.setText(Html.fromHtml(Error2.get(i)));
            }

            itemTemp.vShippingAddress.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("weight", Weight.get(currPos));
                    bundle.putString("shop_id", ShopID.get(currPos));
                    bundle.putString("addr_id", AddrID.get(currPos));
                    bundle.putString("qty", QtyTotal.get(currPos));
                    bundle.putString("shipping_id", ShippingID.get(currPos));
                    bundle.putString("sp_id", SPid.get(currPos));
                    bundle.putString("address_title", AddressTitle.get(currPos));
                    bundle.putString("address", AddressName.get(currPos));
                    bundle.putString("latitude", latitude.get(currPos));
                    bundle.putString("longitude", longitude.get(currPos));
                    bundle.putString("receiver_phone", AddressReceiverPhones.get(currPos));
                    bundle.putString("receiver_name", ShippingAddress.get(currPos));
                    Intent intent = new Intent(context, EditAddressCart.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }

            });

            itemTemp.vShippingAgency.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("weight", Weight.get(currPos));
                    bundle.putString("shop_id", ShopID.get(currPos));
                    bundle.putString("addr_id", AddrID.get(currPos));
                    bundle.putString("qty", QtyTotal.get(currPos));
                    bundle.putString("shipping_id", ShippingID.get(currPos));
                    bundle.putString("sp_id", SPid.get(currPos));
                    bundle.putString("address_title", AddressTitle.get(currPos));
                    bundle.putString("address", AddressName.get(currPos));
                    bundle.putString("latitude", latitude.get(currPos));
                    bundle.putString("longitude", longitude.get(currPos));
                    Intent intent = new Intent(context, EditAddressCart.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            });

            itemTemp.vShopName.setText(Html.fromHtml(ShopName.get(i)));
            setLuckyEmblem(luckyMerchantBadge.get(i), itemTemp.vShopName);
            itemTemp.vTotalPrice.setText(TotalPrice.get(i));
            itemTemp.vShippingAddress.setText(MessageFormat.format("{0} (Ubah) ", ShippingAddress.get(i)));
            itemTemp.vShippingAgency.setText(MessageFormat.format("{0} (Ubah) ", ShippingAgency.get(i)));
            itemTemp.vTotalWeight.setText(TotalWeight.get(i));
            itemTemp.vSubTotal.setText(SubTotal.get(i));
            itemTemp.vShippingCost.setText(ShippingCost.get(i));
            itemTemp.vInsurancePrice.setText(InsurancePrice.get(i));
            itemTemp.vTitleInsurancePrice.setText(TitleInsurancePrice.get(i));
            ItemContent.add(itemTemp);

            lvContainer.addView(Item.get(i));
        }
        progressdialog.dismiss();
        MainView.setVisibility(View.VISIBLE);
    }

    public void RefreshDeletedProdView(int pos) {
        TotalPayment.setText(GrandTotal);
        ItemContent.get(pos).vShopName.setText(ShopName.get(pos));
        setLuckyEmblem(luckyMerchantBadge.get(pos), ItemContent.get(pos).vShopName);
        ItemContent.get(pos).vTotalPrice.setText(TotalPrice.get(pos));
        ItemContent.get(pos).vShippingAddress.setText(ShippingAddress.get(pos));
        ItemContent.get(pos).vShippingAgency.setText(ShippingAgency.get(pos));
        ItemContent.get(pos).vTotalWeight.setText(TotalWeight.get(pos));
        ItemContent.get(pos).vSubTotal.setText(SubTotal.get(pos));
        ItemContent.get(pos).vShippingCost.setText(ShippingCost.get(pos));
        ItemContent.get(pos).vInsurancePrice.setText(InsurancePrice.get(pos));
        ItemContent.get(pos).vTitleInsurancePrice.setText(TitleInsurancePrice.get(pos));
        ItemContent.get(pos).ListProduct.RefreshView(CartID.get(pos), pName.get(pos),
                pImageUri.get(pos), pPrice.get(pos), pWeight.get(pos), Qty.get(pos), Notes.get(pos),
                PriceTotal.get(pos), pErrorMsg.get(pos), MinOrder.get(pos), ProdUrl.get(pos),
                preorderStatus.get(pos), preorderPeriod.get(pos), ProdID.get(pos));
    }


    public void RefreshView(int pos) {
        TotalPayment.setText(GrandTotal);
        ItemContent.get(pos).vShopName.setText(ShopName.get(pos));
        setLuckyEmblem(luckyMerchantBadge.get(pos), ItemContent.get(pos).vShopName);
        ItemContent.get(pos).vTotalPrice.setText(TotalPrice.get(pos));
        ItemContent.get(pos).vShippingAddress.setText(ShippingAddress.get(pos));
        ItemContent.get(pos).vShippingAgency.setText(ShippingAgency.get(pos));
        ItemContent.get(pos).vTotalWeight.setText(TotalWeight.get(pos));
        ItemContent.get(pos).vSubTotal.setText(SubTotal.get(pos));
        ItemContent.get(pos).vShippingCost.setText(ShippingCost.get(pos));
        ItemContent.get(pos).vInsurancePrice.setText(InsurancePrice.get(pos));
        ItemContent.get(pos).vTitleInsurancePrice.setText(TitleInsurancePrice.get(pos));
        ItemContent.get(pos).ListProduct.RefreshView(CartID.get(pos), pName.get(pos),
                pImageUri.get(pos), pPrice.get(pos), pWeight.get(pos), Qty.get(pos), Notes.get(pos),
                PriceTotal.get(pos), pErrorMsg.get(pos), MinOrder.get(pos), ProdUrl.get(pos),
                preorderStatus.get(pos), preorderPeriod.get(pos), ProdID.get(pos));
        if (Error1.get(pos) != null) {
            ItemContent.get(pos).ErrorArea.setVisibility(View.VISIBLE);
            ItemContent.get(pos).ErrorView1.setText(Html.fromHtml(Error1.get(pos)));
            ItemContent.get(pos).ErrorView2.setText(Html.fromHtml(Error2.get(pos)));
        } else {
            ItemContent.get(pos).ErrorArea.setVisibility(View.GONE);
        }
        if (Qty.get(pos).size() <= 1 && Qty.get(pos).get(0) <= 1) {
            ItemContent.get(pos).Chosen.setEnabled(false);
            ItemContent.get(pos).Chosen.setClickable(false);
        } else {
            ItemContent.get(pos).Chosen.setEnabled(true);
            ItemContent.get(pos).Chosen.setClickable(true);
        }
        lvContainer.invalidate();
    }

    public void GetCartInfoRetrofit() {
        resetCartItemVar();
        cartRetrofitInteractor.getCartInfo(getActivity(),
                new HashMap<String, String>(),
                new CartRetrofitInteractor.OnGetCartInfo() {
                    @Override
                    public void onSuccess(CartModel model) {

                        CommonUtils.dumper("GAv4 scrooge "+model.getGrandTotalIdr()+" entering cart ");

                        GrandTotal = model.getGrandTotalIdr();
                        grandTotalWithoutLP = model.getGrandTotalWithoutLP();
                        grandTotalWithoutLPIDR = model.getGrandTotalWithoutLPIDR();
                        Deposit = model.getDepositIdr();
                        DepositNum = model.getDeposit();
                        TokenCart = model.getToken();
                        setLoyaltyPoints(model.getLpAmountIdr(), model.getLpAmount());
                        setCashbackAmount(model.getCashbackIdr(), model.getCashback());
                        TotalPayment.setText(GrandTotal);
                        TokopediaBalance.setText(Deposit);
                        mEcashFlag = model.getEcashFlag();

                        gatewayList = model.getGatewayList();
                        for (int i = 0; i < model.getGatewayList().size(); i++) {
                            GatewayList gatewayDetail = model.getGatewayList().get(i);
                            if (checkIDAvailable(gatewayDetail.getGateway())) {
                                poolDynamicType.put(gatewayDetail.getGateway() + "",
                                        gatewayDetail.getToppayFlag() == null ||
                                                gatewayDetail.getToppayFlag().isEmpty() ? "" : gatewayDetail.getToppayFlag());
                            }
                        }
                        if (model.getTransactionLists().size() > 0) {
                            getGTMTicker();
                            List list = model.getTransactionLists();
                            ArrayList<String> afProductIds = new ArrayList<>();
                            ArrayList<Purchase> allPurchase = new ArrayList<>();

                            ArrayList<HashMap<String, Object>> afProducts = new ArrayList();
                            ArrayList<com.tokopedia.core.analytics.model.Product> locaProducts = new ArrayList<>();

                            int afQty = 0;
                            long shippingRate = 0;

                            for (int i = 0; i < list.size(); i++) {
                                ArrayList<String> pNameData = new ArrayList<>();
                                ArrayList<String> pPriceData = new ArrayList<>();
                                ArrayList<String> pWeightData = new ArrayList<>();
                                ArrayList<String> NotesData = new ArrayList<>();
                                ArrayList<String> ProdUrlData = new ArrayList<>();
                                ArrayList<String> PriceTotalData = new ArrayList<>();
                                ArrayList<String> pImageUriData = new ArrayList<>();
                                ArrayList<String> pErrorMsgData = new ArrayList<>();
                                ArrayList<String> CartIDData = new ArrayList<>();
                                ArrayList<String> ProdIDData = new ArrayList<>();
                                ArrayList<Integer> MinOrderData = new ArrayList<>();
                                ArrayList<Integer> QtyData = new ArrayList<>();
                                ArrayList<Integer> FInsurance = new ArrayList<>();
                                ArrayList<Integer> preorderStatusData = new ArrayList<>();
                                ArrayList<String> preorderPeriodData = new ArrayList<>();

                                Purchase purchase = new Purchase();
                                purchase.setAffiliation(model.getTransactionLists().get(i).getCartShop().getShopName());
                                purchase.setRevenue(model.getTransactionLists().get(i).getCartTotalProductPrice());
                                purchase.setShipping(model.getTransactionLists().get(i).getCartShippingRate());

                                try{
                                    shippingRate = shippingRate + Long.parseLong(model.getTransactionLists().get(i).getCartShippingRate());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                List<CartProduct> detailProdList = model.getTransactionLists().get(i).getCartProducts();
                                for (int k = 0; k < detailProdList.size(); k++) {
                                    afQty = afQty + Integer.valueOf(detailProdList.get(k).getProductQuantity());
                                    CartProduct detailProd = detailProdList.get(k);
                                    FInsurance.add(Integer.parseInt(detailProd.getProductMustInsurance()));
                                    pNameData.add(Html.fromHtml(detailProd.getProductName()).toString());
                                    pPriceData.add(detailProd.getProductPriceIdr());
                                    pWeightData.add(detailProd.getProductWeight());
                                    NotesData.add(detailProd.getProductNotes());
                                    PriceTotalData.add(detailProd.getProductTotalPriceIdr());
                                    QtyData.add(detailProd.getProductQuantity());
                                    MinOrderData.add(Integer.parseInt(detailProd.getProductMinOrder()));
                                    ProdIDData.add(detailProd.getProductId());
                                    ProdUrlData.add(detailProd.getProductUrl());
                                    CartIDData.add(detailProd.getProductCartId());
                                    pImageUriData.add(detailProd.getProductPic());
                                    pErrorMsgData.add(detailProd.getProductErrorMsg());
                                    if (detailProd.getProductPreorder().getStatus() == 1) {
                                        preorderStatusData.add(detailProd.getProductPreorder().getStatus());
                                        preorderPeriodData.add(detailProd.getProductPreorder()
                                                .getProcessTime());
                                    } else {
                                        preorderStatusData.add(0);
                                        preorderPeriodData.add("Tidak Tersedia");
                                    }
                                    Product product = new Product();
                                    product.setProductID(detailProd.getProductId());
                                    product.setPrice(detailProd.getProductPriceIdr());
                                    product.setQty(detailProd.getProductQuantity());
                                    product.setProductName(Html.fromHtml(detailProd.getProductName()).toString());

                                    com.tokopedia.core.analytics.model.Product locaProduct = new com.tokopedia.core.analytics.model.Product();
                                    locaProduct.setId(detailProd.getProductId());
                                    locaProduct.setName(detailProd.getProductName());
                                    locaProduct.setPrice(detailProd.getProductPrice());

                                    locaProducts.add(locaProduct);


                                    purchase.addProduct(product.getProduct());
                                    allPurchase.add(purchase);

                                    HashMap<String, Object> afItem = new HashMap<>();

                                    afItem.put(AFInAppEventParameterName.CONTENT_ID, detailProd.getProductId() + "");
                                    afItem.put(AFInAppEventParameterName.PRICE, detailProd.getProductPrice());
                                    afItem.put(AFInAppEventParameterName.QUANTITY, detailProd.getProductQuantity());

                                    afProducts.add(afItem);
                                    afProductIds.add(detailProd.getProductId()+"");

                                    checkoutAnalytics.addProduct(product.getProduct());
                                }

                                CartShop cartShop = model.getTransactionLists().get(i).getCartShop();
                                ShopName.add(Html.fromHtml(cartShop.getShopName()).toString());
                                ShopID.add(cartShop.getShopId());
                                luckyMerchantBadge.add(cartShop.getLuckyMerchant());

                                CartDestination cartDestination = model.getTransactionLists().get(i).getCartDestination();
                                ShippingAddress.add(Html.fromHtml(cartDestination.getReceiverName()).toString());
                                AddressReceiverPhones.add(cartDestination.getReceiverPhone());
                                PostalCodes.add(cartDestination.getAddressPostal());
                                Districts.add(cartDestination.getAddressDistrict());
                                Cities.add(cartDestination.getAddressCity());
                                AddressStreet.add(cartDestination.getAddressStreet());
                                AddressNames.add(cartDestination.getAddressName());
                                AddrID.add(cartDestination.getAddressId());
                                AddressTitle.add(Html.fromHtml(cartDestination.getAddressName()).toString());
                                AddressName.add(cartDestination.getReceiverName()
                                        + "<br>" + cartDestination.getAddressDistrict() + ", "
                                        + cartDestination.getAddressCity() + ", "
                                        + cartDestination.getAddressPostal()
                                        + "<br>" + cartDestination.getAddressProvince()
                                        + "<br>" + cartDestination.getReceiverPhone());
                                latitude.add(cartDestination.getLatitude());
                                longitude.add(cartDestination.getLongitude());

                                CartShipments shipping = model.getTransactionLists().get(i).getCartShipments();
                                ShippingAgency.add(shipping.getShipmentName() + " - " + shipping.getShipmentPackageName());
                                SPid.add(shipping.getShipmentPackageId());
                                ShippingID.add(shipping.getShipmentId());
                                cartString.add(model.getTransactionLists().get(i).getCartString());
                                if (model.getTransactionLists().get(i).getCartCannotInsurance() != 0) {
                                    InsuranceState.add(1);
                                } else if (model.getTransactionLists().get(i).getCartForceInsurance() != 0) {
                                    InsuranceState.add(0);
                                } else if (model.getTransactionLists().get(i).getCartInsuranceProd() == 1) {
                                    InsuranceState.add(2);
                                } else {
                                    InsuranceState.add(3);
                                }
                                InsurancePos.add(1);
                                for (int j = 0; j < FInsurance.size(); j++) {
                                    if (FInsurance.get(j) == 1 || InsuranceState.get(i) == 2) {
                                        InsurancePos.set(i, 0);
                                    } else if (InsuranceState.get(i) == 3) {
                                        InsurancePos.set(i, 1);
                                    }
                                }

                                QtyTotal.add(model.getTransactionLists().get(i).getCartTotalProduct());
                                Weight.add(model.getTransactionLists().get(i).getCartTotalWeight());
                                TotalPrice.add(model.getTransactionLists().get(i).getCartTotalAmountIdr());
                                TotalProductPrice.add(model.getTransactionLists().get(i).getCartTotalProductPrice());
                                TotalWeight.add(model.getTransactionLists().get(i).getCartTotalWeight() + " Kg");
                                SubTotal.add(model.getTransactionLists().get(i).getCartTotalProductPriceIdr());
                                ShippingCost.add(model.getTransactionLists().get(i).getCartShippingRateIdr());
                                if (Integer.parseInt(model.getTransactionLists().get(i).getCartLogisticFee()) <= 0) {
                                    InsurancePrice.add(model.getTransactionLists().get(i).getCartInsurancePriceIdr());
                                    TitleInsurancePrice
                                            .add(context.getResources().getString(R.string.title_insurance_cost));
                                } else {
                                    InsurancePrice
                                            .add(model.getTransactionLists().get(i).getCartTotalLogisticFeeIdr());
                                    TitleInsurancePrice.add(context.getResources().getString(R.string.title_extra_cost));
                                }

                                AllowCheckout.add(model.getTransactionLists().get(i).getCartIsAllowCheckout());
                                ForceInsurance.add(model.getTransactionLists().get(i).getCartForceInsurance());
                                if (model.getTransactionLists().get(i).getCartErrorMessage1() != null
                                        && model.getTransactionLists().get(i).getCartErrorMessage2() != null
                                        && model.getTransactionLists().get(i).getCartErrorMessage1().length() > 1
                                        && model.getTransactionLists().get(i).getCartErrorMessage2().length() > 1) {
                                    Error1.add(model.getTransactionLists().get(i).getCartErrorMessage1());
                                    Error2.add(model.getTransactionLists().get(i).getCartErrorMessage2());
                                } else {
                                    Error1.add(null);
                                    Error2.add(null);
                                }
                                isDelete.add(false);
                                isChosen.add(0);
                                DSNameVal.add("");
                                DSPhoneVal.add("");
                                DSNameKey.add("");
                                DSPhoneKey.add("");
                                isDropship.add(false);
                                ButtonDetailState.add(0);
                                MinOrder.add(MinOrderData);
                                ProdID.add(ProdIDData);
                                CartID.add(CartIDData);
                                pName.add(pNameData);
                                pPrice.add(pPriceData);
                                pWeight.add(pWeightData);
                                Notes.add(NotesData);
                                PriceTotal.add(PriceTotalData);
                                Qty.add(QtyData);
                                preorderStatus.add(preorderStatusData);
                                preorderPeriod.add(preorderPeriodData);
                                pImageUri.add(pImageUriData);
                                pErrorMsg.add(pErrorMsgData);
                                ProdUrl.add(ProdUrlData);
                            }


                            Map[] afAllItemsPurchased = new Map[afProducts.size()];
                            int ctr = 0;
                            for (HashMap<String, Object> afItem : afProducts) {
                                afAllItemsPurchased[ctr] = afItem;
                                ctr++;
                            }

                            CommonUtils.dumper("GAv4 scrooge "+afQty+" price "+model.getGrandTotal()+" lp "+model.getLpAmount()+" size "+afAllItemsPurchased.length);
                            addItemToLayout();

                            Gson afGSON = new Gson();
                            String afpurchased = afGSON.toJson(afAllItemsPurchased, new TypeToken<Map[]>(){}.getType());
                            String allPurchases = afGSON.toJson(allPurchase, new TypeToken<ArrayList<Purchase>>(){}.getType());
                            String allLocaProducts = afGSON.toJson(locaProducts, new TypeToken<ArrayList<com.tokopedia.core.analytics.model.Product>>(){}.getType());

                            cache.putLong(Jordan.CACHE_LC_KEY_SHIPPINGRATE, shippingRate);

                            cache.putString(Jordan.CACHE_LC_KEY_ALL_PRODUCTS,allLocaProducts);
                            cache.putArrayListString(Jordan.CACHE_AF_KEY_JSONIDS, afProductIds);
                            cache.putInt(Jordan.CACHE_AF_KEY_QTY, afQty);
                            cache.putString(Jordan.CACHE_AF_KEY_ALL_PRODUCTS, afpurchased);
                            cache.putString(Jordan.CACHE_AF_KEY_REVENUE, model.getGrandTotal()+"");
                            cache.putString(Jordan.CACHE_KEY_DATA_AR_ALLPURCHASE, allPurchases);
                            cache.applyEditor();

                            Map<String, Object> afValue = new HashMap<>();
                            afValue.put(AFInAppEventParameterName.PRICE, model.getGrandTotal());
                            afValue.put(AFInAppEventParameterName.CONTENT_ID, afProductIds);
                            afValue.put(AFInAppEventParameterName.QUANTITY,afQty);
                            afValue.put(AFInAppEventParameterName.CURRENCY, "IDR");
                            afValue.put("product", afAllItemsPurchased);

                            sendAppsFlyerATC(afValue);

                        } else {
                            tvTickerGTM.setVisibility(View.GONE);
                            MainView.setVisibility(View.GONE);
                            noResult.showMessage(false);
                            cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
                            cache.applyEditor();
                            progressdialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailed(String error) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetCartInfoRetrofit();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onTimeout(String timeoutMessage) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), timeoutMessage,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetCartInfoRetrofit();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(String errorMessage) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetCartInfoRetrofit();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onNoConnection() {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        GetCartInfoRetrofit();
                                    }
                                }
                        );
                    }
                });
    }

    private void getGTMTicker() {
        if (TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART).equalsIgnoreCase("true")) {
            String message = TrackingUtils.getGtmString(AppEventTracking.GTM.TICKER_CART_TEXT);
            tvTickerGTM.setText(Html.fromHtml(message));
            tvTickerGTM.setVisibility(View.VISIBLE);
            tvTickerGTM.setAutoLinkMask(0);
            Linkify.addLinks(tvTickerGTM, Linkify.WEB_URLS);
        } else {
            tvTickerGTM.setVisibility(View.VISIBLE);
        }
    }

    public void sendAppsFlyerATC(@NonNull Map<String, Object> afValue) {
        PaymentTracking.atcAF(afValue);
    }

    private boolean checkIDAvailable(int ID) {
        switch (ID) {
            case PAYMENT_BCA_KLIK:
                return true;
            case PAYMENT_ECASH:
                return true;
            case PAYMENT_MANDIRI_CLICK:
                return true;
            case PAYMENT_SALDO:
                return true;
            case PAYMENT_TRANSFER:
                return true;
            case PAYMENT_CREDIT_CARD:
                return true;
            case PAYMENT_KLIKBCA:
                return true;
            case PAYMENT_INDOMARET:
                return true;
            case PAYMENT_INSTALLMENT:
                return true;
            case PAYMENT_EPAYBRI:
                return true;
            default:
                return true;
        }
    }

    @SuppressWarnings("unused")
    public void PostStep1() {
        activitycom.Loading();
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.TRANSACTION);
        network.AddParam("lp_flag", 1);
        if (DepositAmt.equals("") || PaymentIDVal.equals("0")) {
            UseDeposit = "0";
        } else {
            UseDeposit = "1";
        }

        CommonUtils.dumper(TAG + " eBRI GATEWAY " + PaymentIDVal);

        if (checkSaldoUsed()) PaymentIDVal = "0";

        if (VoucherCode.length() > 0 && VoucherUse.isChecked())
            network.AddParam("voucher_code", VoucherCode);
        network.AddParam("gateway", PaymentIDVal);
        network.AddParam("step", "1");
        network.AddParam("token_cart", TokenCart);
        network.AddParam("chosen", Choosen);
        network.AddParam("method", "POST");
        network.AddParam("use_deposit", UseDeposit);
        network.AddParam("deposit_amt", DepositAmt);
        boolean addDropshipSrt = false;
        for (boolean dropship : isDropship) {
            if (dropship) addDropshipSrt = true;
        }
        if (addDropshipSrt) network.AddParam("dropship_str", DropShipStr);
        if (DSNameKey.size() > 0) {
            CommonUtils.dumper(DSNameVal);
            CommonUtils.dumper(DSPhoneVal);
            for (int i = 0; i < DSNameKey.size(); i++) {
                if (!DSNameKey.get(i).equals("")) {
                    network.AddParam(DSNameKey.get(i), DSNameVal.get(i));
                    network.AddParam(DSPhoneKey.get(i), DSPhoneVal.get(i));
                }
            }
        }
        network.setRetryPolicy(20000, 0, 0);
        network.Commit(new NetworkHandlerListener() {

            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void getResponse(JSONObject Result) {
                if (Result.has("gateway_name")) {
                    MainView.setVisibility(View.GONE);
                    activitycom.TriggerToAddFragment(Result.toString());
                } else
                    MainView.setVisibility(View.VISIBLE);
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                CommonUtils.ShowError(context, MessageError);
                if (MessageError.size() > 0) activitycom.FinishLoading();
            }
        });
    }

    private void postStep1WSV4Intent() {
        ParamParcel params = new ParamParcel();
        params.put("lp_flag", "1");
        UseDeposit = DepositAmt.equals("") || PaymentIDVal.equals("0") ? "0" : "1";

        if (VoucherCode.length() > 0 && VoucherUse.isChecked()) {
            params.put("voucher_code", VoucherCode);
        }
        params.put("gateway", PaymentIDVal);
        if (checkSaldoUsed()) PaymentIDVal = "0";
        params.put("step", "1");
        params.put("token", TokenCart);
        params.put("token_cart", TokenCart);
        params.put("chosen", generatePartial());
        params.put("method", "POST");
        params.put("use_deposit", UseDeposit);
        params.put("deposit_amt", DepositAmt);
        params.put("partial_str", generatePartial());
        boolean addDropshipSrt = false;
        for (boolean dropship : isDropship) {
            if (dropship) addDropshipSrt = true;
        }
        if (addDropshipSrt) {
            params.put("dropship_str", DropShipStr.replace("-", "~"));
        }
        if (DSNameKey.size() > 0) {
            for (int i = 0; i < DSNameKey.size(); i++) {
                if (!DSNameKey.get(i).equals("")) {
                    if (isDropship.get(i)) {
                        params.put(DSNameKey.get(i), DSNameVal.get(i));
                        params.put(DSPhoneKey.get(i), DSPhoneVal.get(i));
                    }
                }
            }
        }

        Intent intent = new Intent(Intent.ACTION_SYNC, null, context,
                PaymentIntentService.class);
        intent.putExtra(PaymentIntentService.EXTRA_ACTION,
                PaymentIntentService.ACTION_STEP_1_PAYMENT);
        intent.putExtra(PaymentIntentService.EXTRA_RECEIVER, atcReceiver);
        intent.putExtra(PaymentIntentService.EXTRA_PARAM_STEP_1_PAYMENT, params);
        context.startService(intent);
    }

    public void CancelCartWS4(String addressId, String shipmentId,
                              String shipmentPackageId, String shopId) {
        Map<String, String> maps = new HashMap<>();
        maps.put("address_id", addressId);
        maps.put("shipment_id", shipmentId);
        maps.put("shipment_package_id", shipmentPackageId);
        maps.put("shop_id", shopId);
        interactor.cancelCart(context, maps, new PaymentNetInteractor.OnCancelCart() {
                    @Override
                    public void onSuccess(String message) {
                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
                        myAlertDialog.setCancelable(false);
                        myAlertDialog.setMessage(context.getString(R.string.msg_success_delete_prod));

                        myAlertDialog.setPositiveButton(context.getString(R.string.title_ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int arg1) {
                                        if (lvContainer.getChildCount() == 0) {
                                            cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
                                            cache.applyEditor();
                                        }
                                        dialogInterface.dismiss();
                                        GetCartInfoRetrofit();
                                    }
                                });
                        myAlertDialog.show();
                        removeBasketAnalytics();
                    }

                    @Override
                    public void onError(String message) {
                        NetworkErrorHelper.showSnackbar(getActivity(),
                                message);
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onNoConnection() {
                        progressdialog.dismiss();
                    }
                }

        );
    }

    @SuppressWarnings("unused")
    public void CancelProduct(final String cartString, String... params) {
        NetworkHandler network = new NetworkHandler(context, TkpdUrl.CART);
        network.AddParam("act", "cancel_cart_event");
        network.AddParam("cart_id", params[0]);
        network.AddParam("addr_id", params[1]);
        network.AddParam("shop_id", params[2]);
        network.AddParam("ship_id", params[3]);
        network.AddParam("sp_id", params[4]);
        network.AddParam("lp_flag", 1);
        network.AddParam("cart_string", cartString);
        network.AddParam("method", "POST");
        network.Commit(new NetworkHandlerListener() {

            @Override
            public void onSuccess(Boolean status) {
                progressdialog.dismiss();


            }

            @Override
            public void getResponse(JSONObject Result) {
                removeBasketAnalytics();

                try {
                    GrandTotal = Result.getString("grandtotal");
                    int AffectedPos = 0;

                    ArrayList<String> pNameData = new ArrayList<>();
                    ArrayList<String> pPriceData = new ArrayList<>();
                    ArrayList<String> pWeightData = new ArrayList<>();
                    ArrayList<String> NotesData = new ArrayList<>();
                    ArrayList<String> PriceTotalData = new ArrayList<>();
                    ArrayList<String> pImageUriData = new ArrayList<>();
                    ArrayList<String> pErrorMsgData = new ArrayList<>();
                    ArrayList<String> CartIDData = new ArrayList<>();
                    ArrayList<String> ProdIDData = new ArrayList<>();
                    ArrayList<Integer> MinOrderData = new ArrayList<>();
                    ArrayList<Integer> QtyData = new ArrayList<>();
                    ArrayList<Integer> FInsurance = new ArrayList<>();
                    ArrayList<Bitmap> pImageData = new ArrayList<>();
                    ArrayList<String> productURLData = new ArrayList<>();
                    ArrayList<Integer> preorderStatusData = new ArrayList<>();
                    ArrayList<String> preorderPeriodData = new ArrayList<>();

                    JSONObject ListDetail = new JSONObject(Result.getString("affected_cart"));
                    JSONObject Shop = new JSONObject(ListDetail.getString("shop"));
                    String affectedShopID = Shop.getString("id");
                    JSONObject Dest = new JSONObject(ListDetail.getString("dest"));
                    String affectedAddrID = Dest.getString("id");
                    JSONObject Shipping = new JSONObject(ListDetail.getString("shipping"));
                    String affectedSPid = Shipping.getString("sp_id");
                    editCashbackAmount(Result.optString("cashback_idr", "0"), Result.optInt("cashback", 0));
                    editLoyaltyAmount(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
                    for (int i = 0; i < ShopID.size(); i++) {
                        if (FragmentCart.this.cartString.get(i).equals(ListDetail.getString("cart_string"))) {
                            AffectedPos = i;
                        }
                    }

                    JSONArray DetailProdList = new JSONArray(ListDetail.getString("details"));
                    for (int k = 0; k < DetailProdList.length(); k++) {
                        JSONObject DetailProd = new JSONObject(DetailProdList.getString(k));
                        FInsurance.add(DetailProd.getInt("finsurance"));
                        pNameData.add(Html.fromHtml(DetailProd.getString("prod_name")).toString());
                        pPriceData.add(DetailProd.getString("price"));
                        pWeightData.add(DetailProd.getString("weight"));
                        NotesData.add(DetailProd.getString("notes_p"));
                        PriceTotalData.add(DetailProd.getString("total_price"));
                        QtyData.add(DetailProd.getInt("qty"));
                        preorderStatusData.add(DetailProd.getJSONObject("preorder").getInt("status"));
                        preorderPeriodData.add(DetailProd.getJSONObject("preorder").optString("process_time", ""));
                        productURLData.add(DetailProd.getString("prod_url"));
                        if (!DetailProd.isNull("min_order")) {
                            MinOrderData.add(DetailProd.getInt("min_order"));
                        } else if (DetailProd.isNull("min_order")) {
                            MinOrderData.add(1);
                        }
                        ProdIDData.add(DetailProd.getString("prod_id"));
                        CartIDData.add(DetailProd.getString("cart_id"));
                        pImageUriData.add(DetailProd.getString("product_pic"));
                        if (!DetailProd.isNull("error_msg")) {
                            pErrorMsgData.add(DetailProd.getString("error_msg"));
                        } else {
                            pErrorMsgData.add(null);
                        }
                        pImageData.add(null);
                    }
                    InsurancePos.set(AffectedPos, ItemContent.get(AffectedPos).Insurance.getSelectedItemPosition());
                    QtyTotal.set(AffectedPos, ListDetail.getString("ttl_product"));
                    Weight.set(AffectedPos, ListDetail.getString("ttl_weight"));
                    TotalPrice.set(AffectedPos, ListDetail.getString("ttl_amount_idr"));
                    TotalWeight.set(AffectedPos, ListDetail.getString("ttl_weight") + " Kg");
                    SubTotal.set(AffectedPos, ListDetail.getString("ttl_product_price_idr"));
                    ShippingCost.set(AffectedPos, ListDetail.getString("shipping_rate_idr"));
                    if (ListDetail.getInt("logistic_fee") <= 0) {
                        InsurancePrice.set(AffectedPos, ListDetail.getString("insurance_price_idr"));
                        TitleInsurancePrice.set(AffectedPos, context.getResources().getString(R.string.title_insurance_cost));
                    } else {
                        InsurancePrice.set(AffectedPos, ListDetail.getString("total_logistic_fee_idr"));
                        TitleInsurancePrice.set(AffectedPos, context.getResources().getString(R.string.title_extra_cost));
                    }
                    AllowCheckout.set(AffectedPos, ListDetail.getInt("is_allow_checkout"));
                    ForceInsurance.set(AffectedPos, ListDetail.getInt("force_insurance"));
                    if (!ListDetail.isNull("cart_error_msg_1") && !ListDetail.isNull("cart_error_msg_2")) {
                        Error1.set(AffectedPos, ListDetail.getString("cart_error_msg_1"));
                        Error2.set(AffectedPos, ListDetail.getString("cart_error_msg_2"));
                    } else {
                        Error1.set(AffectedPos, null);
                        Error2.set(AffectedPos, null);
                    }
                    MinOrder.set(AffectedPos, MinOrderData);
                    ProdID.set(AffectedPos, ProdIDData);
                    CartID.set(AffectedPos, CartIDData);
                    pName.set(AffectedPos, pNameData);
                    pPrice.set(AffectedPos, pPriceData);
                    pWeight.set(AffectedPos, pWeightData);
                    Notes.set(AffectedPos, NotesData);
                    PriceTotal.set(AffectedPos, PriceTotalData);
                    Qty.set(AffectedPos, QtyData);
                    pImageUri.set(AffectedPos, pImageUriData);
                    pErrorMsg.set(AffectedPos, pErrorMsgData);
                    ProdUrl.set(AffectedPos, productURLData);
                    preorderStatus.set(AffectedPos, preorderStatusData);
                    preorderPeriod.set(AffectedPos, preorderPeriodData);
                    TotalPayment.setText(GrandTotal);

                    if (pName.get(AffectedPos).size() == 0) {
                        lvContainer.removeView(Item.get(AffectedPos));
                        isDelete.set(AffectedPos, true);
                        if (lvContainer.getChildCount() == 0) {
                            MainView.setVisibility(View.GONE);
                            Log.i("Magic", "Cancel 2 clear");
                            cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
                            cache.applyEditor();
                            noResult.showMessage(false);
                            ButtonEditor.setVisibility(View.GONE);
                        }
                    } else {
                        RefreshDeletedProdView(AffectedPos);
                    }

                    if (!Result.isNull("success")) {
                        progressdialog.dismiss();
                        reloadPaymentList();

                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
                        myAlertDialog.setMessage(context.getString(R.string.msg_success_delete_prod));

                        myAlertDialog.setPositiveButton(context.getString(R.string.title_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (popupMenu != null) {
                                     popupMenu.dismiss();
                                }
                            }

                        });
                        myAlertDialog.show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void getMessageError(ArrayList<String> MessageError) {
                InvalidCart(MessageError.get(0));
            }
        });
    }

    public void CancelProductWS4(String productCartId, String addressId, String shipmentId,
                                 String shipmentPackageId, String shopId) {
        Map<String, String> maps = new HashMap<>();
        maps.put("product_cart_id", productCartId);
        maps.put("address_id", addressId);
        maps.put("shipment_id", shipmentId);
        maps.put("shipment_package_id", shipmentPackageId);
        maps.put("shop_id", shopId);
        interactor.cancelProduct(context, maps, new PaymentNetInteractor.OnCancelProduct() {
                    @Override
                    public void onSuccess(String message) {
                        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
                        myAlertDialog.setCancelable(false);
                        myAlertDialog.setMessage(context.getString(R.string.msg_success_delete_prod));

                        myAlertDialog.setPositiveButton(context.getString(R.string.title_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int arg1) {
                                dialogInterface.dismiss();
                                if (popupMenu != null) {
                                    popupMenu.dismiss();
                                }
                                GetCartInfoRetrofit();
                            }

                        });
                        myAlertDialog.show();
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        NetworkErrorHelper.showSnackbar(getActivity(),
                                message);
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onNoConnection() {
                        NetworkErrorHelper.showSnackbar(getActivity());
                        progressdialog.dismiss();
                    }
                }

        );
    }

    public void BulkEditWS4(String carts, String cartShopId, String cartAddressID, String cartShippingId,
                            String cartSpId, String lpFlag, String cartString) {
        Map<String, String> maps = new HashMap<>();
        maps.put("carts", carts);
        maps.put("cart_shop_id", cartShopId);
        maps.put("cart_addr_id", cartAddressID);
        maps.put("cart_shipping_id", cartShippingId);
        maps.put("cart_sp_id", cartSpId);
        maps.put("lp_flag", lpFlag);
        maps.put("cart_string", cartString);
        interactor.updateCart(context, maps, new PaymentNetInteractor.OnUpdateCart() {
                    @Override
                    public void onSuccess(String message) {
                        GetCartInfoRetrofit();
                    }

                    @Override
                    public void onError(String message) {
                        NetworkErrorHelper.showSnackbar(getActivity(),
                                message);
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onNoConnection() {
                        NetworkErrorHelper.showSnackbar(getActivity());
                        progressdialog.dismiss();
                    }
                }

        );
    }

    public void EditInsuranceWS4(String addressId, String productInsurance, String shipmentId,
                                 String shipmentPackageId, String shopId) {
        Map<String, String> maps = new HashMap<>();
        maps.put("address_id", addressId);
        maps.put("product_insurance", productInsurance);
        maps.put("shipment_id", shipmentId);
        maps.put("shipment_package_id", shipmentPackageId);
        maps.put("shop_id", shopId);
        interactor.updateInsurance(context, maps, new PaymentNetInteractor.OnUpdateInsurance() {
                    @Override
                    public void onSuccess(String message) {
                        GetCartInfoRetrofit();
                    }

                    @Override
                    public void onError(String message) {
                        NetworkErrorHelper.showSnackbar(getActivity(),
                                message);
                    }

                    @Override
                    public void onNoConnection() {

                    }
                }
        );
    }

    private void postDynamicPaymentWSV4Intent() {
        ParamParcel params = new ParamParcel();
        UseDeposit = DepositAmt.equals("") || PaymentIDVal.equals("0") ? "0" : "1";
        params.put("lp_flag", "1");
        params.put("deposit_amt", DepositAmt);
        params.put("dropship_str", generateDropship());
        params.put("gateway", PaymentIDVal);
        params.put("step", "1");
        params.put("token", TokenCart);
        params.put("chosen", generatePartial());
        params.put("method", "POST");
        params.put("use_deposit", UseDeposit);
        params.put("partial_str", generatePartial());
        if (DSNameKey.size() > 0) {
            for (int i = 0; i < DSNameKey.size(); i++) {
                if (!DSNameKey.get(i).equals("")) {
                    if (isDropship.get(i)) {
                        params.put(DSNameKey.get(i), DSNameVal.get(i));
                        params.put(DSPhoneKey.get(i), DSPhoneVal.get(i));
                    }
                }
            }
        }
        if (VoucherCode.length() > 0 && VoucherUse.isChecked()) {
            params.put("voucher_code", VoucherCode);
        }
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context,
                PaymentIntentService.class);
        intent.putExtra(PaymentIntentService.EXTRA_ACTION,
                PaymentIntentService.ACTION_GET_PARAMETER_DYNAMIC_PAYMENT);
        intent.putExtra(PaymentIntentService.EXTRA_RECEIVER, atcReceiver);
        intent.putExtra(PaymentIntentService.EXTRA_PARAM_GET_PARAMETER_DYNAMIC_PAYMENT, params);
        context.startService(intent);
    }


    private String generateDropship() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < isDropship.size(); i++) {
            if (isDropship.get(i)) {
                value.append(cartString.get(i)).append("*~*");
            }
        }
        String finalValue = value.toString();
        if (finalValue.endsWith("*~*")) {
            finalValue = finalValue.substring(0, value.lastIndexOf("*~*"));
        }
        return finalValue;
    }

    private String generatePartial() {
        StringBuilder chosen = new StringBuilder();
        for (int i = 0; i < cartString.size(); i++) {
            if (isChosen.get(i) == 1) {
                chosen.append(cartString.get(i)).append("*~*");
            }
        }
        String finalChosen = chosen.toString();
        if (finalChosen.endsWith("*~*")) {
            finalChosen = finalChosen.substring(0, chosen.lastIndexOf("*~*"));
        }
        return finalChosen;
    }

    public void DeleteProduct(final String cID) {
        basket = new Basket();
        int mainPos = 0;
        int subPos = 0;
        for (int i = 0; i < CartID.size(); i++) {
            for (int k = 0; k < CartID.get(i).size(); k++) {
                if (CartID.get(i).get(k).equals(cID)) {
                    subPos = k;
                    mainPos = i;
                }
            }
        }
        final int pos = mainPos;
        Product product = new Product();
        product.setProductID(ProdID.get(mainPos).get(subPos));
        product.setProductName(pName.get(mainPos).get(subPos));
        product.setQty(Qty.get(mainPos).get(subPos));
        product.setPrice(CurrencyFormatHelper.convertRupiahToInt(pPrice.get(mainPos).get(subPos)));
        basket.addProduct(product.getProduct());
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setTitle(context.getString(R.string.title_cancel_confirm));
        myAlertDialog.setMessage(context.getString(R.string.msg_cancel_1) + " " + ShopName.get(mainPos) + " "
                + context.getString(R.string.msg_cancel_2) + " " + pName.get(mainPos).get(subPos) + " " + context.getString(R.string.msg_cancel_3) + " "
                + PriceTotal.get(mainPos).get(subPos));
        myAlertDialog.setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
                progressdialog.showDialog();
                CloseEditMenu();
                CancelProductWS4(cID, AddrID.get(pos), ShippingID.get(pos), SPid.get(pos), ShopID.get(pos));
            }
        });

        myAlertDialog.setNegativeButton(context.getString(R.string.title_no), null);
        myAlertDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void PassResponse(String response) {
        try {
            JSONObject Result = new JSONObject(response);
            GrandTotal = Result.getString("grandtotal");
            int AffectedPos = 0;

            ArrayList<String> pNameData = new ArrayList<>();
            ArrayList<String> pPriceData = new ArrayList<>();
            ArrayList<String> pWeightData = new ArrayList<>();
            ArrayList<String> NotesData = new ArrayList<>();
            ArrayList<String> PriceTotalData = new ArrayList<>();
            ArrayList<String> pImageUriData = new ArrayList<>();
            ArrayList<String> pErrorMsgData = new ArrayList<>();
            ArrayList<String> CartIDData = new ArrayList<>();
            ArrayList<String> ProdIDData = new ArrayList<>();
            ArrayList<Integer> MinOrderData = new ArrayList<>();
            ArrayList<Integer> QtyData = new ArrayList<>();
            ArrayList<Integer> FInsurance = new ArrayList<>();
            ArrayList<Bitmap> pImageData = new ArrayList<>();

            JSONObject ListDetail = new JSONObject(Result.getString("affected_cart"));
            JSONObject Shop = new JSONObject(ListDetail.getString("shop"));
            JSONObject Dest = new JSONObject(ListDetail.getString("dest"));
            JSONObject Shipping = new JSONObject(ListDetail.getString("shipping"));
            for (int i = 0; i < ShopID.size(); i++) {
                if (ShopID.get(i).equals(Shop.getString("id")) && AddrID.get(i).equals(Dest.getString("id"))
                        && SPid.get(i).equals(Shipping.getString("sp_id"))) {
                    AffectedPos = i;
                }
            }

            ShippingAddress.set(AffectedPos, Html.fromHtml(Dest.getString("receiver_name")).toString());
            AddrID.set(AffectedPos, Dest.getString("id"));
            AddressTitle.set(AffectedPos, Html.fromHtml(Dest.getString("addr_name")).toString());
            AddressName.set(AffectedPos, Dest.getString("receiver_name")
                    + "\n" + Html.fromHtml(Dest.getString("addr_name")).toString()
                    + "\n" + Dest.getString("district_name") + ", " + Dest.getString("city_name") + ", " + Dest.getString("postal")
                    + "\n" + Dest.getString("province_name")
                    + "\n" + Dest.getString("phone"));

            ShippingAgency.set(AffectedPos, Shipping.getString("name") + " - " + Shipping.getString("product_name"));
            SPid.set(AffectedPos, Shipping.getString("sp_id"));
            ShippingID.set(AffectedPos, Shipping.getString("shipping_id"));

            JSONArray DetailProdList = new JSONArray(ListDetail.getString("details"));
            for (int k = 0; k < DetailProdList.length(); k++) {
                JSONObject DetailProd = new JSONObject(DetailProdList.getString(k));
                FInsurance.add(DetailProd.getInt("finsurance"));
                pNameData.add(DetailProd.getString("prod_name"));
                pPriceData.add(DetailProd.getString("price"));
                pWeightData.add(DetailProd.getString("weight"));
                NotesData.add(DetailProd.getString("notes_p"));
                PriceTotalData.add(DetailProd.getString("total_price"));
                QtyData.add(DetailProd.getInt("qty"));
                if (!DetailProd.isNull("min_order")) {
                    MinOrderData.add(DetailProd.getInt("min_order"));
                } else if (DetailProd.isNull("min_order")) {
                    MinOrderData.add(1);
                }
                ProdIDData.add(DetailProd.getString("prod_id"));
                CartIDData.add(DetailProd.getString("cart_id"));
                pImageUriData.add(DetailProd.getString("product_pic"));
                if (!DetailProd.isNull("error_msg")) {
                    pErrorMsgData.add(DetailProd.getString("error_msg"));
                } else {
                    pErrorMsgData.add(null);
                }
                pImageData.add(null);
            }
            InsurancePos.set(AffectedPos, ItemContent.get(AffectedPos).Insurance.getSelectedItemPosition());
            QtyTotal.set(AffectedPos, ListDetail.getString("ttl_product"));
            Weight.set(AffectedPos, ListDetail.getString("ttl_weight"));
            TotalPrice.set(AffectedPos, ListDetail.getString("ttl_amount_idr"));
            TotalWeight.set(AffectedPos, ListDetail.getString("ttl_weight"));
            SubTotal.set(AffectedPos, ListDetail.getString("ttl_product_price_idr"));
            ShippingCost.set(AffectedPos, ListDetail.getString("shipping_rate_idr"));
            if (ListDetail.getInt("logistic_fee") <= 0) {
                InsurancePrice.set(AffectedPos, ListDetail.getString("insurance_price_idr"));
                TitleInsurancePrice.set(AffectedPos, context.getResources().getString(R.string.title_insurance_cost));
            } else {
                InsurancePrice.set(AffectedPos, ListDetail.getString("total_logistic_fee_idr"));
                TitleInsurancePrice.set(AffectedPos, context.getResources().getString(R.string.title_extra_cost));
            }
            AllowCheckout.set(AffectedPos, ListDetail.getInt("is_allow_checkout"));
            ForceInsurance.set(AffectedPos, ListDetail.getInt("force_insurance"));
            if (!ListDetail.isNull("cart_error_msg_1") && !ListDetail.isNull("cart_error_msg_2")) {
                Error1.set(AffectedPos, ListDetail.getString("cart_error_msg_1"));
                Error2.set(AffectedPos, ListDetail.getString("cart_error_msg_2"));
            } else {
                Error1.set(AffectedPos, null);
                Error2.set(AffectedPos, null);
            }
            if (pNameData.size() == pName.get(AffectedPos).size()) {
                RefreshView(AffectedPos);
            } else {
                activitycom.TriggerReloadFragment();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CancelEdit() {
        isItemHighLight = false;
        ItemContent.get(EditPos).ListProduct.CancelEdit();
        RefreshView(EditPos);
        ButtonEditor.setVisibility(View.GONE);
        CheckoutBut.setEnabled(true);
        EditMode = false;
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        param.setMargins(0, 0, 0, 0);
        lvContainer.setLayoutParams(param);
        ItemContent.get(EditPos).MainView.setBackgroundResource(R.drawable.cards_ui_2);
    }

    private void InvalidCart(String error_message) {
        if (error_message.equals(context.getString(R.string.error_cart_not_valid))) {
            Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean CloseEditMenu() {
        if (ButtonEditor.getVisibility() == View.VISIBLE) {
            CancelEdit();
            return true;
        } else
            return false;
    }

    private void CheckVoucher(String voucherCode, final boolean NextStep) {
        progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
        progressdialog.showDialog();
        Map<String, String> params = new HashMap<>();
        params.put("voucher_code", voucherCode);
        interactor.checkVoucher(context, params, new PaymentNetInteractor.OnCheckVoucher() {
            @Override
            public void onSuccess(VoucherCodeData data) {
                progressdialog.dismiss();
                visibleTopPointView(View.GONE);
                VoucherResult.setText(data.getVoucher().getVoucherAmount().equals("0")
                        ? data.getVoucher().getVoucherPromoDesc() : String.format("Anda mendapatkan voucher\nRp.%s,-",
                        CurrencyFormatHelper.ConvertToRupiah(data.getVoucher().getVoucherAmount())));
                VoucherCode = VoucherEdit.getText().toString();
                if (NextStep) doCheckoutCart();
            }

            @Override
            public void onError(String message) {
                progressdialog.dismiss();
                VoucherCode = "";
                VoucherResult.setText("");
                NetworkErrorHelper.showSnackbar(getActivity(), message);
            }

            @Override
            public void onVoucherError(String message) {
                progressdialog.dismiss();
                tilVoucher.setErrorEnabled(true);
                tilVoucher.setError(message);
                VoucherCode = "";
                VoucherResult.setText("");
                visibleTopPointView(View.VISIBLE);
            }
        });
    }

    private void setLocalyticFlow() {
        try {

            int qtyTotal = 0;
            for (String qty : QtyTotal) {
                qtyTotal = qtyTotal + Integer.parseInt(qty);
            }

            Map<String, String> attributes = new HashMap<>();
            attributes.put(getString(R.string.value_items_in_cart), qtyTotal + "");
            attributes.put(getString(R.string.value_price_total), grandTotalWithoutLP);

            CommonUtils.dumper("LocalTag : Cart 1");
            ScreenTracking.sendLocaCartEvent(attributes);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void ErrorMessage(ArrayList<String> MessageError) {
        for (int i = 0; i < MessageError.size(); i++) {
            Toast.makeText(context, MessageError.get(i), Toast.LENGTH_LONG).show();
            if ((i + 1) < MessageError.size())
                Toast.makeText(context, "No Error", Toast.LENGTH_LONG).show();
        }
    }

    private void setLoyaltyPoints(String loyaltyPoints, Integer loyaltyInteger) {
        if (loyaltyInteger != null && loyaltyInteger > 0) {
            View loyaltyLayout = LayoutInflater.from(context).inflate(R.layout.loyalty_view, mainContainer, false);
            loyaltyLayout.setTag("TOP_POINT_VIEW");
            RelativeLayout.LayoutParams relativeParams =
                    new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeParams.addRule(RelativeLayout.BELOW, R.id.saldo_left_wrapper);
            TextView loyaltyBalance = (TextView) loyaltyLayout.findViewById(R.id.loyalty_balance);
            loyaltyAmountTextView = loyaltyBalance;
            if (paymentSummary.getChildAt(3).getTag() != null && paymentSummary.getChildAt(3).getTag().equals("TOP_POINT_VIEW")) {
                paymentSummary.removeViewAt(3);
            }
            paymentSummary.addView(loyaltyLayout, 3, relativeParams);
            loyaltyBalance.setText(MessageFormat.format("({0})", loyaltyPoints));
        }
    }

    private void setCashbackAmount(String cashbackAmount, Integer cashbackInteger) {
        if (cashbackInteger != null && cashbackInteger > 0) {
            View cashbackLayout = LayoutInflater.from(context).inflate(R.layout.cashback_view, mainContainer, false);
            TextView cashbackValue = (TextView) cashbackLayout.findViewById(R.id.cashback_value);
            cashbackAmountTextView = cashbackValue;
            holderCashback.removeAllViews();
            holderCashback.addView(cashbackLayout);
            cashbackValue.setText(cashbackAmount);
        }
    }

    private void editCashbackAmount(String cashbackAmount, int cashbackInteger) {
        if (cashbackInteger > 0) {
            cashbackAmountTextView.setText(cashbackAmount);
        }
    }

    private void editLoyaltyAmount(String loyaltyPoints, int loyaltyInteger) {
        if (loyaltyInteger > 0) {
            loyaltyAmountTextView.setText(MessageFormat.format("({0})", loyaltyPoints));
        }
    }

    private void setLuckyEmblem(int luckyMerchant, TextView shopNameTextView) {
        if (luckyMerchant == 1) {
            shopNameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.loyal_merchant, 0, 0, 0);
            shopNameTextView.setCompoundDrawablePadding(5);
        }
    }

    @SuppressWarnings("unused")
    private void ErrorMessageVoucher(ArrayList<String> MessageError) {
        String message = "";
        for (String string : MessageError) {
            message = message + "\n" + string;
        }
        if (!MessageError.isEmpty()) {
            tilVoucher.setErrorEnabled(true);
            tilVoucher.setError(message);
        }
        VoucherEdit.requestFocus();
    }

    private void removeBasketAnalytics() {
        UnifyTracking.eventATCRemove();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cartRetrofitInteractor.destroyObservable();
    }

    private void reloadPaymentList() {
        progressdialog.showDialog();
        cartRetrofitInteractor.getCartInfo(getActivity(),
                new HashMap<String, String>(),
                new CartRetrofitInteractor.OnGetCartInfo() {
                    @Override
                    public void onSuccess(CartModel model) {
                        gatewayList.clear();
                        gatewayList = model.getGatewayList();
                        for (int i = 0; i < model.getGatewayList().size(); i++) {
                            GatewayList gatewayDetail = model.getGatewayList().get(i);
                            if (checkIDAvailable(gatewayDetail.getGateway())) {
                                poolDynamicType.put(gatewayDetail.getGateway() + "",
                                        gatewayDetail.getToppayFlag() == null || gatewayDetail.getToppayFlag().isEmpty()
                                                ? "" : gatewayDetail.getToppayFlag());
                            }
                        }
                        progressdialog.dismiss();
                    }

                    @Override
                    public void onFailed(String error) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        reloadPaymentList();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onTimeout(String timeout) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), timeout,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        reloadPaymentList();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(String errorMessage) {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(), errorMessage,
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        reloadPaymentList();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onNoConnection() {
                        progressdialog.dismiss();
                        NetworkErrorHelper.showEmptyState(getActivity(), getView(),
                                new NetworkErrorHelper.RetryClickedListener() {
                                    @Override
                                    public void onRetryClicked() {
                                        reloadPaymentList();
                                    }
                                }
                        );
                    }
                });
    }

    private boolean checkSaldoUsed() {
        return SaldoTokopedia.isShown() && DepositAmt.equals(GrandTotal
                .replaceAll("[^0-9]", ""));
    }

    @SuppressWarnings("unused")
    private void reRenderCartInfo(CartModel model) {
        GrandTotal = model.getGrandTotalIdr();
        grandTotalWithoutLP = model.getGrandTotalWithoutLP();
        Deposit = model.getDepositIdr();
        DepositNum = model.getDeposit();
        TokenCart = model.getToken();
        setLoyaltyPoints(model.getLpAmountIdr(), model.getLpAmount());
        setCashbackAmount(model.getCashbackIdr(), model.getCashback());
        TotalPayment.setText(GrandTotal);
        TokopediaBalance.setText(Deposit);
        mEcashFlag = model.getEcashFlag();

        gatewayList = model.getGatewayList();
        for (int i = 0; i < model.getGatewayList().size(); i++) {
            GatewayList gatewayDetail = model.getGatewayList().get(i);
            if (checkIDAvailable(gatewayDetail.getGateway())) {
                poolDynamicType.put(gatewayDetail.getGateway() + "",
                        gatewayDetail.getToppayFlag() == null ||
                                gatewayDetail.getToppayFlag().isEmpty() ? "" : gatewayDetail.getToppayFlag());
            }
        }
        if (model.getTransactionLists().size() > 0) {
            List list = model.getTransactionLists();
            for (int i = 0; i < list.size(); i++) {
                ArrayList<String> pNameData = new ArrayList<>();
                ArrayList<String> pPriceData = new ArrayList<>();
                ArrayList<String> pWeightData = new ArrayList<>();
                ArrayList<String> NotesData = new ArrayList<>();
                ArrayList<String> ProdUrlData = new ArrayList<>();
                ArrayList<String> PriceTotalData = new ArrayList<>();
                ArrayList<String> pImageUriData = new ArrayList<>();
                ArrayList<String> pErrorMsgData = new ArrayList<>();
                ArrayList<String> CartIDData = new ArrayList<>();
                ArrayList<String> ProdIDData = new ArrayList<>();
                ArrayList<Integer> MinOrderData = new ArrayList<>();
                ArrayList<Integer> QtyData = new ArrayList<>();
                ArrayList<Integer> FInsurance = new ArrayList<>();
                ArrayList<Bitmap> pImageData = new ArrayList<>();
                ArrayList<Integer> preorderStatusData = new ArrayList<>();
                ArrayList<String> preorderPeriodData = new ArrayList<>();
                List<CartProduct> detailProdList = model.getTransactionLists().get(i).getCartProducts();

                for (int k = 0; k < detailProdList.size(); k++) {
                    CartProduct detailProd = detailProdList.get(k);
                    FInsurance.add(Integer.parseInt(detailProd.getProductMustInsurance()));
                    pNameData.add(Html.fromHtml(detailProd.getProductName()).toString());
                    pPriceData.add(detailProd.getProductPriceIdr());
                    pWeightData.add(detailProd.getProductWeight());
                    NotesData.add(detailProd.getProductNotes());
                    PriceTotalData.add(detailProd.getProductTotalPriceIdr());
                    QtyData.add(detailProd.getProductQuantity());
                    MinOrderData.add(Integer.parseInt(detailProd.getProductMinOrder()));
                    ProdIDData.add(detailProd.getProductId());
                    ProdUrlData.add(detailProd.getProductUrl());
                    CartIDData.add(detailProd.getProductCartId());
                    pImageUriData.add(detailProd.getProductPic());
                    pErrorMsgData.add(detailProd.getProductErrorMsg());
                    if (detailProd.getProductPreorder().getStatus() == 1) {
                        preorderStatusData.add(detailProd.getProductPreorder().getStatus());
                        preorderPeriodData.add(detailProd.getProductPreorder()
                                .getProcessTime());
                    } else {
                        preorderStatusData.add(0);
                        preorderPeriodData.add("Tidak Tersedia");
                    }
                    Product product = new Product();
                    product.setProductID(detailProd.getProductId());
                    product.setPrice(detailProd.getProductPriceIdr());
                    product.setQty(detailProd.getProductQuantity());
                    product.setProductName(Html.fromHtml(detailProd.getProductName()).toString());

                    checkoutAnalytics.addProduct(product.getProduct());

                }

                CartShop cartShop = model.getTransactionLists().get(i).getCartShop();
                ShopName.add(Html.fromHtml(cartShop.getShopName()).toString());
                ShopID.add(cartShop.getShopId());
                luckyMerchantBadge.add(cartShop.getLuckyMerchant());

                CartDestination cartDestination = model.getTransactionLists().get(i).getCartDestination();
                ShippingAddress.add(Html.fromHtml(cartDestination.getReceiverName()).toString());
                AddressReceiverPhones.add(cartDestination.getReceiverPhone());
                PostalCodes.add(cartDestination.getAddressPostal());
                Districts.add(cartDestination.getAddressDistrict());
                Cities.add(cartDestination.getAddressCity());
                AddressStreet.add(cartDestination.getAddressStreet());
                AddressNames.add(cartDestination.getAddressName());
                AddrID.add(cartDestination.getAddressId());
                AddressTitle.add(Html.fromHtml(cartDestination.getAddressName()).toString());
                AddressName.add(cartDestination.getReceiverName()
                        + "<br>" + cartDestination.getAddressDistrict() + ", "
                        + cartDestination.getAddressCity() + ", "
                        + cartDestination.getAddressPostal()
                        + "<br>" + cartDestination.getAddressProvince()
                        + "<br>" + cartDestination.getReceiverPhone());
                latitude.add(cartDestination.getLatitude());
                longitude.add(cartDestination.getLongitude());

                CartShipments shipping = model.getTransactionLists().get(i).getCartShipments();
                ShippingAgency.add(shipping.getShipmentName() + " - " + shipping.getShipmentPackageName());
                SPid.add(shipping.getShipmentPackageId());
                ShippingID.add(shipping.getShipmentId());
                cartString.add(model.getTransactionLists().get(i).getCartString());
                if (model.getTransactionLists().get(i).getCartCannotInsurance() != 0) {
                    InsuranceState.add(1);
                } else if (model.getTransactionLists().get(i).getCartForceInsurance() != 0) {
                    InsuranceState.add(0);
                } else if (model.getTransactionLists().get(i).getCartInsuranceProd() == 1) {
                    InsuranceState.add(2);
                } else {
                    InsuranceState.add(3);
                }
                InsurancePos.add(1);
                for (int j = 0; j < FInsurance.size(); j++) {
                    if (FInsurance.get(j) == 1 || InsuranceState.get(i) == 2) {
                        InsurancePos.set(i, 0);
                    } else if (InsuranceState.get(i) == 3) {
                        InsurancePos.set(i, 1);
                    }
                }

                QtyTotal.add(model.getTransactionLists().get(i).getCartTotalProduct());
                Weight.add(model.getTransactionLists().get(i).getCartTotalWeight());
                TotalPrice.add(model.getTransactionLists().get(i).getCartTotalAmountIdr());
                TotalWeight.add(model.getTransactionLists().get(i).getCartTotalWeight() + " Kg");
                SubTotal.add(model.getTransactionLists().get(i).getCartTotalProductPrice());
                ShippingCost.add(model.getTransactionLists().get(i).getCartShippingRateIdr());
                if (Integer.parseInt(model.getTransactionLists().get(i).getCartLogisticFee()) <= 0) {
                    InsurancePrice.add(model.getTransactionLists().get(i).getCartInsurancePriceIdr());
                    TitleInsurancePrice
                            .add(context.getResources().getString(R.string.title_insurance_cost));
                } else {
                    InsurancePrice
                            .add(model.getTransactionLists().get(i).getCartTotalLogisticFeeIdr());
                    TitleInsurancePrice.add(context.getResources().getString(R.string.title_extra_cost));
                }

                AllowCheckout.add(model.getTransactionLists().get(i).getCartIsAllowCheckout());
                ForceInsurance.add(model.getTransactionLists().get(i).getCartForceInsurance());
                if (model.getTransactionLists().get(i).getCartErrorMessage1() != null
                        && model.getTransactionLists().get(i).getCartErrorMessage2() != null
                        && model.getTransactionLists().get(i).getCartErrorMessage1().length() > 1
                        && model.getTransactionLists().get(i).getCartErrorMessage2().length() > 1) {
                    Error1.add(model.getTransactionLists().get(i).getCartErrorMessage1());
                    Error2.add(model.getTransactionLists().get(i).getCartErrorMessage2());
                } else {
                    Error1.add(null);
                    Error2.add(null);
                }
                isDelete.add(false);
                isChosen.add(0);
                DSNameVal.add("");
                DSPhoneVal.add("");
                DSNameKey.add("");
                DSPhoneKey.add("");
                isDropship.add(false);
                ButtonDetailState.add(0);
                MinOrder.add(MinOrderData);
                ProdID.add(ProdIDData);
                CartID.add(CartIDData);
                pName.add(pNameData);
                pPrice.add(pPriceData);
                pWeight.add(pWeightData);
                Notes.add(NotesData);
                PriceTotal.add(PriceTotalData);
                Qty.add(QtyData);
                preorderStatus.add(preorderStatusData);
                preorderPeriod.add(preorderPeriodData);
                pImageUri.add(pImageUriData);
                pErrorMsg.add(pErrorMsgData);
                ProdUrl.add(ProdUrlData);
            }
            addItemToLayout();
        } else {
            noResult.showMessage(false);
            cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
            cache.applyEditor();
            progressdialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            GetCartInfoRetrofit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenTracking.screen(this);
    }
}
