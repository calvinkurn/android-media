package com.tokopedia.tkpd.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.localytics.android.Localytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.analytics.PaymentTracking;
import com.tokopedia.tkpd.analytics.ScreenTracking;
import com.tokopedia.tkpd.analytics.UnifyTracking;
import com.tokopedia.tkpd.analytics.nishikino.model.Product;
import com.tokopedia.tkpd.analytics.nishikino.model.Purchase;
import com.tokopedia.tkpd.payment.model.responsecartstep1.Cart;
import com.tokopedia.tkpd.payment.model.responsecartstep1.CartProduct;
import com.tokopedia.tkpd.payment.model.responsecartstep2.CartStep2Data;
import com.tokopedia.tkpd.payment.model.responsecartstep2.SystemBank;
import com.tokopedia.tkpd.purchase.activity.PurchaseActivity;
import com.tokopedia.tkpd.util.TokopediaBankAccount;
import com.tokopedia.tkpd.var.TkpdCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentCartFinish extends Fragment {

    private View view;
    private Activity context;
    private int GatewayID;
    private View Saldo;
    private View Transfer;
    private String Response;
    private TextView PayementMethod;
    private TextView TotalInvoice;
    private TextView additionalTotalInvoice;
    private TextView TokopediaBalance;
    private TextView TitleBalance;
    private TextView OrderStatsButton;
    private TextView FinalInvoice;
    private TextView TitleFinalInvoice;
    private TextView Voucher;
    private TextView TitleVoucher;
    private TextView BcaNumber;
    private TextView mandiriNumberTextView;
    private TextView briNumberTextView;
    private LinearLayout creditCardFailed;
    private View creditCardFailNotification;
    private ActivityCartFinishCommunicator activitycom;
    private View klikBCAGuide;
    private LinearLayout klikBCAPage;
    private LinearLayout klikBCAUserIDInformation;
    private LinearLayout indomaretInstructionPage;
    private String userIDKlikBCA;
    private String indoMaretPaymentCode;
    private TextView paymentResultInfo;
    private TextView loyaltyPointsTextView;
    private TextView loyaltyPointsTitle;
    private TextView klikBCAUserID;
    private TextView indomaretPaymentCodeTextView;
    private ViewGroup mainContainer;
    private TextView detailToggle;
    private RelativeLayout transactionDetail;
    private TextView TitleTotalPayment;
    private boolean isWsv4;
    private CartStep2Data cartStep2Data;


    public interface ActivityCartFinishCommunicator {
        public void Loading();

        public void FinishLoading();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.NOTIFICATION_DATA);
        cache.putInt(TkpdCache.Key.IS_HAS_CART, 0);
        cache.applyEditor();
    }

    @Override
    public void onAttach(Activity activity) {
        context = getActivity();
        activitycom = (ActivityCartFinishCommunicator) context;
        Bundle bundle = this.getArguments();
        if (bundle.getBoolean("is_wsv4", false)) {
            isWsv4 = true;
            cartStep2Data = bundle.getParcelable("CART_STEP_2_DATA");
        } else {
            GatewayID = bundle.getInt("gateway");
            Response = bundle.getString("response");
            isWsv4 = false;
        }

        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_finish, container, false);
        mainContainer = container;
        Saldo = (View) view.findViewById(R.id.saldo_method);
        Transfer = (View) view.findViewById(R.id.transfer_method);
        klikBCAPage = (LinearLayout) view.findViewById(R.id.klikbca_method_finish);
        klikBCAUserIDInformation = (LinearLayout) view.findViewById(R.id.klikbca_user_id_information);
        klikBCAGuide = (View) view.findViewById(R.id.guide_view);
        indomaretInstructionPage = (LinearLayout) view.findViewById(R.id.indomaret_method_finish);
        paymentResultInfo = (TextView) view.findViewById(R.id.payment_result_information);
        context = getActivity();
        declareCreditCardFailView();

        if (isWsv4) {
            renderDataWsV4();
            return view;
        }

        try {
            //JSONObject json = new JSONObject(Response);
            //if (!json.isNull("result")) {
            JSONObject Result = new JSONObject(Response);
            creditCardResultChecker(Result);
            //Sending data to Ga
            JSONArray GaData = null;

            if (!Result.isNull("bout_data") && !Result.getString("bout_data").equals("0")) {
                GaData = new JSONArray(Result.optString("bout_data", "[]"));
            } else if (!Result.getString("ga_data").equals("0")) {
                GaData = new JSONArray(Result.optString("ga_data", "[]"));
            }

            if (GaData != null && GaData.length() > 0) {

                for (int i = 0; i < GaData.length(); i++) {
                    JSONObject DataTx = new JSONObject(GaData.getString(i));

                    AdWordsConversionReporter.reportWithConversionId(getActivity(),
                            "966484783", "9i8wCKii6VcQr8btzAM", DataTx.getString("item_price"), true);

                    Purchase purchase = new Purchase();
                    purchase.setAffiliation(DataTx.getString("shop_name"));
                    purchase.setRevenue(DataTx.getDouble("item_price"));
                    purchase.setShipping(DataTx.getDouble("shipping"));
                    purchase.setTransactionID(DataTx.getString("order_id"));
                    if (Result.has("voucher_code")) {
                        CommonUtils.dumper("appdata voucher code " + Result.getString("voucher_code"));
                        purchase.setVoucherCode(Result.getString("voucher_code"));
                    }

					CommonUtils.dumper("datatx masuk sini neeeeh "+DataTx.toString());
					//AppsFlyerLib.sendTrackingWithEvent(context.getApplicationContext(), "purchase", DataTx.getString("item_price"));

					JSONArray ProductList = new JSONArray(DataTx.getString("detail"));
					String[] products = new String[ProductList.length()];
					int qtyTotal = 0;
					for(int j = 0; j < ProductList.length(); j++) {
						JSONObject ProductData = new JSONObject(ProductList.getString(j));
                        Product product = new Product();

						products[j] = ProductData.getString("product_id");
						qtyTotal = qtyTotal + ProductData.getInt("qty");

                        product.setCategory(ProductData.getString("category"));
                        product.setPrice(ProductData.getDouble("price"));
                        product.setProductID(ProductData.getString("product_id"));
                        product.setQty(ProductData.getInt("qty"));
                        product.setProductName(ProductData.getString("product_name"));

                        purchase.addProduct(product.getProduct());

                    }

                    PaymentTracking.eventTransactionGTM(purchase);

                }
            }

            GatewayID = Result.getInt("gateway");
            if (UsingSaldoFull()) {
                Voucher = (TextView) view.findViewById(R.id.total_voucher);
                TitleVoucher = (TextView) view.findViewById(R.id.head_title_total_voucher);// TODO
                Saldo.setVisibility(View.VISIBLE);
                Transfer.setVisibility(View.GONE);
                PayementMethod = (TextView) view.findViewById(R.id.pay_method);
                TotalInvoice = (TextView) view.findViewById(R.id.total_payment);
                setGrandTotal((LinearLayout) Saldo);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
                setCashbackAmount(Result.optString("cashback_idr", "0"), Result.optInt("cashback", 0), (LinearLayout) Saldo);
                PayementMethod.setText(Result.getString("gateway_name") + " " + context.getString(R.string.title_full));
                TotalInvoice.setText(Result.getString("payment_left_idr"));
                additionalTotalInvoice.setText(Result.getString("grand_total_idr"));
                TitleTotalPayment.setText(context.getString(R.string.title_total_payment_saldo));
/*				if(!Result.getString("voucher_amt").equals("0")){
                    Voucher.setText(Result.getString("voucher_amt_idr"));
					FinalInvoice.setText(Result.getString("payment_left_idr"));
					Voucher.setVisibility(View.VISIBLE);
					TitleVoucher.setVisibility(View.VISIBLE);
					FinalInvoice.setVisibility(View.VISIBLE);
					TitleFinalInvoice.setVisibility(View.VISIBLE);
					CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name") + " + VOCHER");

				}*/
                detailToggle.setOnClickListener(onDetailButtonSaldoSwitchListener(Result));
                CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name"));
                OrderStatsButton = (TextView) view.findViewById(R.id.order_stats_but);
                OrderStatsButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(PurchaseActivity.createIntentTxStatus(context));
                        context.finish();
                        sendGTMConfirm();
                    }
                });
/*				setCashbackAmount(Result.optString("lp_amount_idr", "0"), (LinearLayout) Saldo);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), (LinearLayout) Saldo);*/
            } else if (UsingTransferBank()) {
                Saldo.setVisibility(View.GONE);// TODO
                Transfer.setVisibility(View.VISIBLE);
                PayementMethod = (TextView) view.findViewById(R.id.pay_method2);
                setGrandTotal((LinearLayout) Transfer);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
                setCashbackAmount(Result.optString("cashback_idr", "0"), Result.optInt("cashback", 0), (LinearLayout) Transfer);
                BcaNumber = (TextView) view.findViewById(R.id.bca_number);
                mandiriNumberTextView = (TextView) view.findViewById(R.id.mandiri_number);
                briNumberTextView = (TextView) view.findViewById(R.id.bri_number);
                //TokopediaBalance = (TextView) view.findViewById(R.shopId.total_tokopedia_balance);
                TitleBalance = (TextView) view.findViewById(R.id.title_balance);
                OrderStatsButton = (TextView) view.findViewById(R.id.payment_conf_but);
                JSONArray systemBank = Result.getJSONArray("system_bank");
                JSONObject bankBCA = systemBank.getJSONObject(0);
                BcaNumber.setText(bankBCA.getString("acc_no"));
                TokopediaBankAccount.setBCANUmber(getActivity(), bankBCA.getString("acc_no"));
                getAccountNumber(systemBank);
                TotalInvoice.setText(Result.getString("payment_left_idr"));
                additionalTotalInvoice.setText(Result.getString("grand_total_idr"));
                TitleTotalPayment.setText(context.getString(R.string.title_total_payment_left));
                OrderStatsButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(PurchaseActivity.createIntentConfirmPayment(context));
						context.finish();
						CommonUtils.dumper("LOCALTAG : CHECKOUT FINISH");
                        sendGTMConfirm();
                    }
                });
/*				if (Result.getInt("deposit_amt") > 0) {
                    PayementMethod.setText(Result.getString("gateway_name")+" "+context.getString(R.string.title_and)+" "+context.getString(R.string.title_tokopedia_balance)
							+" "+context.getString(R.string.title_partial));
					TitleFinalInvoice.setVisibility(View.VISIBLE);
					FinalInvoice.setVisibility(View.VISIBLE);
					FinalInvoice.setText(Result.getString("payment_left_idr"));
					TotalInvoice.setText(Result.getString("grand_total_idr"));
					TokopediaBalance.setText(Result.getString("deposit_amt_idr"));
					TitleBalance.setVisibility(View.VISIBLE);
					TokopediaBalance.setVisibility(View.VISIBLE);
					CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name") + " + SALDO SEBAGIAN");

				} else {
					TitleFinalInvoice.setVisibility(View.GONE);
					FinalInvoice.setVisibility(View.GONE);
					PayementMethod.setText(Result.getString("gateway_name") + " " + context.getString(R.string.title_full));
					TotalInvoice.setText(Result.getString("grand_total_idr"));
					TitleBalance.setVisibility(View.GONE);
					TokopediaBalance.setVisibility(View.GONE);
					CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name"));
				}
				if(!Result.getString("voucher_amt").equals("0")){
					Voucher.setText(Result.getString("voucher_amt_idr"));
					FinalInvoice.setText(Result.getString("payment_left_idr"));
					Voucher.setVisibility(View.VISIBLE);
					TitleVoucher.setVisibility(View.VISIBLE);
					FinalInvoice.setVisibility(View.VISIBLE);
					TitleFinalInvoice.setVisibility(View.VISIBLE);
					CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name") + " + VOCHER");
				}*/
                detailToggle.setOnClickListener(onDetailButtonSwitchListener(Result));
/*				setCashbackAmount(Result.optString("lp_amount_idr", "0"), (LinearLayout) Transfer);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), (LinearLayout) Transfer);*/
            } else if (instantPayment()) {
                Voucher = (TextView) view.findViewById(R.id.total_voucher);// TODO
                Saldo.setVisibility(View.VISIBLE);
                Transfer.setVisibility(View.GONE);
                TotalInvoice = (TextView) view.findViewById(R.id.total_payment);
                setGrandTotal((LinearLayout) Saldo);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), Result.optInt("lp_amount", 0));
                setCashbackAmount(Result.optString("cashback_idr", "0"), Result.optInt("cashback", 0), (LinearLayout) Saldo);

                if (isKlikBca() || isIndomaret()) {
                    TitleTotalPayment.setText(context.getString(R.string.title_total_payment_left));
                } else {
                    TitleTotalPayment.setText(context.getString(R.string.title_total_payment));
                }

                detailToggle.setOnClickListener(onDetailButtonSwitchListener(Result));
                TotalInvoice.setText(Result.getString("payment_left_idr"));
                additionalTotalInvoice.setText(Result.getString("grand_total_idr"));
                OrderStatsButton = (TextView) view.findViewById(R.id.order_stats_but);
                OrderStatsButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(PurchaseActivity.createIntentTxStatus(context));
                        context.finish();
                        sendGTMConfirm();
                    }
                });
/*				setCashbackAmount(Result.optString("lp_amount_idr", "0"), (LinearLayout) Saldo);
                setLoyaltyPoints(Result.optString("lp_amount_idr", "0"), (LinearLayout) Saldo);*/
                checkKlikBCAMode(Result);
                checkIndomaretMode(Result);
            }
            //}

		} catch (Exception e) {
			e.printStackTrace();
		}

        activitycom.FinishLoading();
        return view;
    }

    private void renderDataWsV4() {
        postAnal();


        GatewayID = Integer.parseInt(cartStep2Data.getTransaction().getGateway());
        if (UsingSaldoFull()) {
            Voucher = (TextView) view.findViewById(R.id.total_voucher);
            TitleVoucher = (TextView) view.findViewById(R.id.head_title_total_voucher);// TODO
            Saldo.setVisibility(View.VISIBLE);
            Transfer.setVisibility(View.GONE);
            PayementMethod = (TextView) view.findViewById(R.id.pay_method);
            TotalInvoice = (TextView) view.findViewById(R.id.total_payment);
            setGrandTotal((LinearLayout) Saldo);
            setLoyaltyPoints(cartStep2Data.getTransaction().getLpAmountIdr(),
                    Integer.parseInt(cartStep2Data.getTransaction().getLpAmount()));
            setCashbackAmount(cartStep2Data.getTransaction().getCashbackIdr(),
                    Integer.parseInt(cartStep2Data.getTransaction().getCashback()), (LinearLayout) Saldo);
            PayementMethod.setText(MessageFormat.format("{0} {1}",
                    cartStep2Data.getTransaction().getGatewayName(), context.getString(R.string.title_full)));
            TotalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
            additionalTotalInvoice.setText(cartStep2Data.getTransaction().getGrandTotalIdr());
            TitleTotalPayment.setText(context.getString(R.string.title_total_payment_saldo));
            detailToggle.setOnClickListener(new OneOnClick() {
                @Override
                public void oneOnClick(View view) {
                    detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                    transactionDetail.setVisibility(View.VISIBLE);
                    if (cartStep2Data.getTransaction().getVoucherAmount() != null
                            && !cartStep2Data.getTransaction().getVoucherAmount().equals("0")) {
                        Voucher.setText(cartStep2Data.getTransaction().getVoucherAmountIdr());
                        FinalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
                        Voucher.setVisibility(View.VISIBLE);
                        TitleVoucher.setVisibility(View.VISIBLE);
                        FinalInvoice.setVisibility(View.VISIBLE);
                        TitleFinalInvoice.setVisibility(View.VISIBLE);
                    }
                    detailToggle.setOnClickListener(onDetailButtonClosedListener(this));
                }
            });
            OrderStatsButton = (TextView) view.findViewById(R.id.order_stats_but);
            OrderStatsButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(PurchaseActivity.createIntentTxStatus(context));
                    context.finish();
                    sendGTMConfirm();
                }
            });
        } else if (UsingTransferBank()) {
            Saldo.setVisibility(View.GONE);// TODO
            Transfer.setVisibility(View.VISIBLE);
            PayementMethod = (TextView) view.findViewById(R.id.pay_method2);
            setGrandTotal((LinearLayout) Transfer);
            setLoyaltyPoints(cartStep2Data.getTransaction().getLpAmountIdr(),
                    Integer.parseInt(cartStep2Data.getTransaction().getLpAmount()));
            setCashbackAmount(cartStep2Data.getTransaction().getCashbackIdr(),
                    Integer.parseInt(cartStep2Data.getTransaction().getCashback()), (LinearLayout) Transfer);
            BcaNumber = (TextView) view.findViewById(R.id.bca_number);
            mandiriNumberTextView = (TextView) view.findViewById(R.id.mandiri_number);
            briNumberTextView = (TextView) view.findViewById(R.id.bri_number);
            TitleBalance = (TextView) view.findViewById(R.id.title_balance);
            OrderStatsButton = (TextView) view.findViewById(R.id.payment_conf_but);

            BcaNumber.setText(cartStep2Data.getSystemBankList().get(0).getSbAccountNo());
            TokopediaBankAccount.setBCANUmber(getActivity(), cartStep2Data.getSystemBankList().get(0).getSbAccountNo());
            getAccountNumberWSV4(cartStep2Data.getSystemBankList());
            TotalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
            additionalTotalInvoice.setText(cartStep2Data.getTransaction().getGrandTotalIdr());
            TitleTotalPayment.setText(context.getString(R.string.title_total_payment_left));
            OrderStatsButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(PurchaseActivity.createIntentConfirmPayment(context));
                    context.finish();
                    CommonUtils.dumper("LOCALTAG : CHECKOUT FINISH");
                    sendGTMConfirm();
                }
            });
            detailToggle.setOnClickListener(new OneOnClick() {
                @Override
                public void oneOnClick(View view) {
                    transactionDetail.setVisibility(View.VISIBLE);
                    detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                    if (cartStep2Data.getTransaction().getDepositAmount() != null
                            && Integer.parseInt(cartStep2Data.getTransaction().getDepositAmount()) > 0) {
                        TokopediaBalance.setVisibility(View.VISIBLE);
                        TitleBalance.setVisibility(View.VISIBLE);
                        PayementMethod.setText(MessageFormat.format("{0} {1} {2} {3}",
                                cartStep2Data.getTransaction().getGatewayName(),
                                context.getString(R.string.title_and),
                                context.getString(R.string.title_tokopedia_balance),
                                context.getString(R.string.title_partial)));
                        TokopediaBalance.setText(MessageFormat.format("({0})",
                                cartStep2Data.getTransaction().getDepositAmountIdr()));
                    } else {
                        PayementMethod.setText(MessageFormat.format("{0} {1}",
                                cartStep2Data.getTransaction().getGatewayName(),
                                context.getString(R.string.title_full)));
                    }
                    if (cartStep2Data.getTransaction().getVoucherAmount() != null
                            && !cartStep2Data.getTransaction().getVoucherAmount().equals("0")) {
                        Voucher.setText(cartStep2Data.getTransaction().getVoucherAmountIdr());
                        FinalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
                        Voucher.setVisibility(View.VISIBLE);
                        TitleVoucher.setVisibility(View.VISIBLE);
                        FinalInvoice.setVisibility(View.VISIBLE);
                        TitleFinalInvoice.setVisibility(View.VISIBLE);
                    }
                    detailToggle.setOnClickListener(onDetailButtonClosedListener(this));
                }
            });
        } else if (instantPayment()) {
            Voucher = (TextView) view.findViewById(R.id.total_voucher);// TODO
            Saldo.setVisibility(View.VISIBLE);
            Transfer.setVisibility(View.GONE);
            TotalInvoice = (TextView) view.findViewById(R.id.total_payment);
            setGrandTotal((LinearLayout) Saldo);
            setLoyaltyPoints(cartStep2Data.getTransaction().getLpAmountIdr(), Integer.parseInt(cartStep2Data.getTransaction().getLpAmount()));
            setCashbackAmount(cartStep2Data.getTransaction().getCashbackIdr(), Integer.parseInt(cartStep2Data.getTransaction().getCashback()), (LinearLayout) Saldo);

            if (isKlikBca() || isIndomaret()) {
                TitleTotalPayment.setText(context.getString(R.string.title_total_payment_left));
            } else {
                TitleTotalPayment.setText(context.getString(R.string.title_total_payment));
            }

            detailToggle.setOnClickListener(new OneOnClick() {
                @Override
                public void oneOnClick(View view) {
                    transactionDetail.setVisibility(View.VISIBLE);
                    detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                    if (cartStep2Data.getTransaction().getDepositAmount() != null
                            && Integer.parseInt(cartStep2Data.getTransaction().getDepositAmount()) > 0) {
                        TokopediaBalance.setVisibility(View.VISIBLE);
                        TitleBalance.setVisibility(View.VISIBLE);
                        PayementMethod.setText(MessageFormat.format("{0} {1} {2} {3}",
                                cartStep2Data.getTransaction().getGatewayName(),
                                context.getString(R.string.title_and),
                                context.getString(R.string.title_tokopedia_balance),
                                context.getString(R.string.title_partial)));
                        TokopediaBalance.setText(MessageFormat.format("({0})",
                                cartStep2Data.getTransaction().getDepositAmountIdr()));
                    } else {
                        PayementMethod.setText(MessageFormat.format("{0} {1}",
                                cartStep2Data.getTransaction().getGatewayName(),
                                context.getString(R.string.title_full)));
                    }
                    if (cartStep2Data.getTransaction().getVoucherAmount() != null
                            && !cartStep2Data.getTransaction().getVoucherAmount().equals("0")) {
                        Voucher.setText(cartStep2Data.getTransaction().getVoucherAmountIdr());
                        FinalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
                        Voucher.setVisibility(View.VISIBLE);
                        TitleVoucher.setVisibility(View.VISIBLE);
                        FinalInvoice.setVisibility(View.VISIBLE);
                        TitleFinalInvoice.setVisibility(View.VISIBLE);
                    }
                    detailToggle.setOnClickListener(onDetailButtonClosedListener(this));
                }
            });
            TotalInvoice.setText(cartStep2Data.getTransaction().getPaymentLeftIdr());
            additionalTotalInvoice.setText(cartStep2Data.getTransaction().getGrandTotalIdr());
            OrderStatsButton = (TextView) view.findViewById(R.id.order_stats_but);
            OrderStatsButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(PurchaseActivity.createIntentTxStatus(context));
                    context.finish();
                    sendGTMConfirm();
                }
            });

            if (GatewayID == 9 && cartStep2Data.getTransaction().getKlikbcaUser() != null) {
                userIDKlikBCA = cartStep2Data.getTransaction().getKlikbcaUser();
                alterViewElementKlikBCA();
                setklikBCAVisibility();
                klikBCAGateway();
            }
        }
        activitycom.FinishLoading();
    }

    private void postAnal() {
        try {

            if (cartStep2Data.getTransaction().getCarts() != null &&
                    !cartStep2Data.getTransaction().getCarts().isEmpty()) {

                ArrayList<HashMap<String, Object>> afProducts = new ArrayList();
                ArrayList<String> afProductIds = new ArrayList<>();
                ArrayList<com.tokopedia.tkpd.analytics.model.Product> locaProducts = new ArrayList<>();

                int afQty = 0;
                double totalShipping = 0;
                for (int i = 0; i < cartStep2Data.getTransaction().getCarts().size(); i++) {
                    Cart cart2 = cartStep2Data.getTransaction().getCarts().get(i);

                    AdWordsConversionReporter.reportWithConversionId(getActivity(),
                            "966484783", "9i8wCKii6VcQr8btzAM", cart2.getCartTotalProductPrice(), true);

                    Purchase purchase = new Purchase();
                    purchase.setAffiliation(cart2.getCartShop().getShopName());
                    purchase.setRevenue(cart2.getCartTotalProductPrice());
                    purchase.setShipping(cart2.getCartShippingRate());
                    purchase.setTransactionID(cartStep2Data.getTransaction().getPaymentId());

                    try {
                        totalShipping = totalShipping + Double.parseDouble(cart2.getCartShippingRate());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    CommonUtils.dumper("masuk sini neeeeh");
                    afQty = afQty + Integer.valueOf(cart2.getCartTotalProductActive());
                    for (int j = 0; j < cart2.getCartProducts().size(); j++) {
                        CartProduct detail = cart2.getCartProducts().get(j);
                        Product product = new Product();
                        com.tokopedia.tkpd.analytics.model.Product locaProduct = new com.tokopedia.tkpd.analytics.model.Product();
                        locaProduct.setId(detail.getProductId());
                        locaProduct.setName(detail.getProductName());
                        locaProduct.setPrice(detail.getProductPrice());

                        product.setPrice(detail.getProductPrice());
                        product.setProductID(String.valueOf(detail.getProductId()));
                        product.setQty(detail.getProductQuantity());
                        product.setProductName(detail.getProductName());

                        purchase.addProduct(product.getProduct());
                        locaProducts.add(locaProduct);

                        HashMap<String, Object> afItem = new HashMap<>();

                        afItem.put(AFInAppEventParameterName.CONTENT_ID, detail.getProductId() + "");
                        afItem.put(AFInAppEventParameterName.PRICE, detail.getProductPrice());
                        afItem.put(AFInAppEventParameterName.QUANTITY, detail.getProductQuantity());

                        afProducts.add(afItem);
                        afProductIds.add(detail.getProductId() + "");
                    }

                    CommonUtils.dumper("Appsflyer orderID number " + cart2.getCartCustomerId() + " price total " + cart2.getCartTotalProductPrice() + " grand tottals " + cartStep2Data.getTransaction().getGrandTotal() + " before fee " + cartStep2Data.getTransaction().getGrandTotalBeforeFee());

                    PaymentTracking.eventTransactionGTM(purchase);

                }

                Map[] afAllItemsPurchased = new Map[afProducts.size()];
                int ctr = 0;
                for (HashMap<String, Object> afItem : afProducts) {
                    afAllItemsPurchased[ctr] = afItem;
                    ctr++;
                }

                /**
                 * AppsFlyer Block
                 *
                 */

                PaymentTracking.eventTransactionAF(cartStep2Data.getTransaction(),afProductIds,afQty,afAllItemsPurchased);

                /**
                 * Localytics Block
                 *
                 */

                Map<String, String> values = new HashMap<>();
                values.put(context.getString(R.string.event_payment_method), cartStep2Data.getTransaction().getGatewayName());
                values.put(context.getString(R.string.event_value_total_transaction), cartStep2Data.getTransaction().getGrandTotalBeforeFee() + "");
                values.put(context.getString(R.string.value_total_quantity), afQty + "");
                values.put(context.getString(R.string.value_shipping_fee), totalShipping + "");

                PaymentTracking.eventTransactionLoca(values, locaProducts);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean UsingTransferBank() {
        if (GatewayID == 1) return true;
        else return false;
    }

    private boolean UsingSaldoFull() {
        if (GatewayID == 0) {
            return true;
        } else return false;
    }

    private void declareCreditCardFailView() {
        creditCardFailed = (LinearLayout) view.findViewById(R.id.credit_card_failed_layout);
        creditCardFailNotification = (View) view.findViewById(R.id.credit_card_failed_form);
        TextView backToCartButton = (TextView) view.findViewById(R.id.back_to_cart_butt);
        backToCartButton.setOnClickListener(onBackToCartListener());
    }

    private void creditCardResultChecker(JSONObject Result) {
        if ((GatewayID == 8 || GatewayID == 12) && Result.length() < 1) {
            creditCardFailed.setVisibility(View.VISIBLE);
            Saldo.setVisibility(View.GONE);
            Transfer.setVisibility(View.GONE);
        }
    }

    private OnClickListener onBackToCartListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent = new Intent(context, getActivity().getClass());
                context.startActivity(intent);
                context.finish();
            }
        };
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void klikBCAGateway() {
        String[] imageName = {"klikbca1.jpg", "klikbca2.jpg", "klikbca3.jpg", "klikbca4.jpg"};
        int[] screenShotTextID = {R.id.klikbca_screenshot_1, R.id.klikbca_screenshot_2, R.id.klikbca_screenshot_3, R.id.klikbca_screenshot_4};
        TextView klikBCAStep1 = null;
        TextView klikBCAStep2 = null;
        TextView klikBCAStep3 = null;
        TextView klikBCAStep4 = null;
        TextView[] TextViewList = {klikBCAStep1, klikBCAStep2, klikBCAStep3, klikBCAStep4};
        for (int i = 0; i < 4; i++) {
            TextViewList[i] = (TextView) klikBCAGuide.findViewById(screenShotTextID[i]);
            TextViewList[i].setOnClickListener(imageZoomOnClickListener("klikBCA/" + imageName[i]));
        }
    }

    private void setklikBCAVisibility() {
        klikBCAPage.setVisibility(View.VISIBLE);
        klikBCAUserIDInformation.setVisibility(View.VISIBLE);
        klikBCAUserID = (TextView) view.findViewById(R.id.user_id_klik_bca);
        klikBCAUserID.setText(userIDKlikBCA);
    }

    private OnClickListener imageZoomOnClickListener(final String imageName) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                //showImagePreviewDialog(imageName);
                showImagePreview(imageName);
            }
        };
    }

    private void showImagePreview(String imageName) {
        FragmentManager fm = context.getFragmentManager();
        DialogFragment imageDetailDialog = ImageDetailDialog.openImageFromAsset(context, imageName);
        imageDetailDialog.show(fm, "image_dialog");
    }

    private OnClickListener indomaretOrderStatsListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                indomaretAlertDialog();
            }
        };
    }

    private OnClickListener klikBCAOrderStatsClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChangePayment();
            }
        };
    }

    private void checkKlikBCAMode(JSONObject Result) {
        if (GatewayID == 9) {
            try {
                userIDKlikBCA = Result.getString("klikbca_user");
                alterViewElementKlikBCA();
                setklikBCAVisibility();
                klikBCAGateway();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void alterViewElementKlikBCA() {
        OrderStatsButton.setText(context.getString(R.string.title_rescenter_canceled));
        OrderStatsButton.setOnClickListener(klikBCAOrderStatsClickListener());
        paymentResultInfo.setText(context.getString(R.string.description_klikbca_information));
    }

    private void alterViewElementIndomaret() {
        OrderStatsButton.setText(context.getString(R.string.title_rescenter_canceled));
        OrderStatsButton.setOnClickListener(indomaretOrderStatsListener());
        paymentResultInfo.setText(context.getString(R.string.description_klikbca_information));
    }

    private void checkIndomaretMode(JSONObject Result) {
        if (GatewayID == 10) {
            try {
                indoMaretPaymentCode = Result.getJSONObject("indomaret").getString("payment_code");
                TotalInvoice.setText(Result.getString("indomaret_amt_idr"));
                additionalTotalInvoice.setText(Result.getString("indomaret_amt_idr"));
                alterViewElementIndomaret();
                setIndomaretVisibility();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setIndomaretVisibility() {
        indomaretInstructionPage.setVisibility(View.VISIBLE);
        indomaretPaymentCodeTextView = (TextView) view.findViewById(R.id.indomaret_payment_code_text_view);
        indomaretPaymentCodeTextView.setText(indoMaretPaymentCode);
    }

    private boolean instantPayment() {
        if (GatewayID == 4 || GatewayID == 6 || GatewayID == 7 || GatewayID == 8 || GatewayID == 9 || GatewayID == 10 || GatewayID == 12 || GatewayID == 11) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isKlikBca() {
        return GatewayID == 9;
    }

    private boolean isIndomaret() {
        return GatewayID == 10;
    }


    private void indomaretAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        setIndomaretDialogView(builder);
        builder.setNegativeButton("Check Status", negativeButtonListener());
        builder.setPositiveButton("Tutup", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        sendGTMConfirm();
    }

    private void setIndomaretDialogView(AlertDialog.Builder builder) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alert_indomaret, null);
        builder.setView(view);
        TextView dialogIndomaretCode = (TextView) view.findViewById(R.id.indomaret_code_dialog_text_view);
        dialogIndomaretCode.setText(indoMaretPaymentCode);
    }

    private void goToChangePayment() {
        startActivity(PurchaseActivity.createIntentTxVerification(context));
        context.finish();
        sendGTMConfirm();
    }

    private DialogInterface.OnClickListener negativeButtonListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToChangePayment();
            }
        };
    }

    private void getAccountNumber(JSONArray systemBank) {
        try {
            JSONObject bankMandiri = systemBank.getJSONObject(1);
            JSONObject bankBRI = systemBank.getJSONObject(2);
            String mandiriAccNumber = bankMandiri.getString("acc_no");
            String briAccNumber = bankBRI.getString("acc_no");
            TokopediaBankAccount.setMandiriNumber(context, mandiriAccNumber);
            TokopediaBankAccount.setBRINumber(context, briAccNumber);
            setBankAccountNumber(mandiriAccNumber, mandiriNumberTextView);
            setBankAccountNumber(briAccNumber, briNumberTextView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAccountNumberWSV4(List<SystemBank> systemBank) {
        try {
            String mandiriAccNumber = systemBank.get(1).getSbAccountNo();
            String briAccNumber = systemBank.get(2).getSbAccountNo();
            TokopediaBankAccount.setMandiriNumber(context, mandiriAccNumber);
            TokopediaBankAccount.setBRINumber(context, briAccNumber);
            setBankAccountNumber(mandiriAccNumber, mandiriNumberTextView);
            setBankAccountNumber(briAccNumber, briNumberTextView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBankAccountNumber(String accountNumber, TextView accountNumberTextView) {
        accountNumberTextView.setText(accountNumber);
    }

    private void setGrandTotal(LinearLayout methodLayout) {
        LinearLayout headerLayout;
        LayoutInflater grandTotalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grandTotalView = grandTotalInflater.inflate(R.layout.grand_total_label, mainContainer, false);
        PayementMethod = (TextView) grandTotalView.findViewById(R.id.pay_method);
        transactionDetail = (RelativeLayout) grandTotalView.findViewById(R.id.transaction_detail);
        detailToggle = (TextView) grandTotalView.findViewById(R.id.transaction_detail_toggle);
        TotalInvoice = (TextView) grandTotalView.findViewById(R.id.total_payment_value);
        TokopediaBalance = (TextView) grandTotalView.findViewById(R.id.total_tokopedia_balance);
        TitleBalance = (TextView) grandTotalView.findViewById(R.id.title_balance);
        additionalTotalInvoice = (TextView) grandTotalView.findViewById(R.id.total_payment);
        Voucher = (TextView) grandTotalView.findViewById(R.id.total_voucher);
        TitleVoucher = (TextView) grandTotalView.findViewById(R.id.head_title_total_voucher);// TODO
        TitleFinalInvoice = (TextView) grandTotalView.findViewById(R.id.title_final_invoice);
        FinalInvoice = (TextView) grandTotalView.findViewById(R.id.final_invoice);
        PayementMethod = (TextView) grandTotalView.findViewById(R.id.pay_method);
        loyaltyPointsTextView = (TextView) grandTotalView.findViewById(R.id.loyalty_points);
        loyaltyPointsTitle = (TextView) grandTotalView.findViewById(R.id.title_loyalty_points);
        TitleTotalPayment = (TextView) grandTotalView.findViewById(R.id.head_title_total_payment);
        headerLayout = (LinearLayout) methodLayout.getChildAt(0);
        headerLayout.addView(grandTotalView, 3);
    }

    private void setCashbackAmount(String cashbackAmount, int cashbackInt, LinearLayout methodLayout) {
        if (cashbackInt > 0) {
            LinearLayout headerLayout;
            LayoutInflater loyaltyLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cashbackLayout = loyaltyLayoutInflater.inflate(R.layout.cashback_view, mainContainer, false);
            TextView cashbackValue = (TextView) cashbackLayout.findViewById(R.id.cashback_value);
            headerLayout = (LinearLayout) methodLayout.getChildAt(0);
            headerLayout.addView(cashbackLayout, 4);
            cashbackValue.setText(cashbackAmount);
        }
    }

    private void switchVoucherVisibility(JSONObject Result) {
        try {
            if (!Result.getString("voucher_amt").equals("0")) {
                Voucher.setText(Result.getString("voucher_amt_idr"));
                FinalInvoice.setText(Result.getString("payment_left_idr"));
                Voucher.setVisibility(View.VISIBLE);
                TitleVoucher.setVisibility(View.VISIBLE);
                FinalInvoice.setVisibility(View.VISIBLE);
                TitleFinalInvoice.setVisibility(View.VISIBLE);
                CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name") + " + VOCHER");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void switchDepositVisibility(JSONObject Result) {
        try {
            if (Result.getInt("deposit_amt") > 0) {
                TokopediaBalance.setVisibility(View.VISIBLE);
                TitleBalance.setVisibility(View.VISIBLE);
                PayementMethod.setText(Result.getString("gateway_name") + " " + context.getString(R.string.title_and) + " " + context.getString(R.string.title_tokopedia_balance)
                        + " " + context.getString(R.string.title_partial));
                //TotalInvoice.setText(Result.getString("grand_total_bf_fee_idr"));
                TokopediaBalance.setText("(" + Result.getString("deposit_amt_idr") + ")");

                CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name") + " + SALDO SEBAGIAN");

            } else {
                PayementMethod.setText(Result.getString("gateway_name") + " " + context.getString(R.string.title_full));
                //TotalInvoice.setText(Result.getString("grand_total_idr"));

                CommonUtils.dumper("LOCALTAG : PAYMENT_GATEWAY : " + Result.getString("gateway_name"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener onDetailButtonSwitchListener(final JSONObject Result) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                transactionDetail.setVisibility(View.VISIBLE);
                detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                switchDepositVisibility(Result);
                switchVoucherVisibility(Result);
                detailToggle.setOnClickListener(onDetailButtonClosedListener(this));
            }
        };
    }

    private View.OnClickListener onDetailButtonSaldoSwitchListener(final JSONObject Result) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                transactionDetail.setVisibility(View.VISIBLE);
                switchVoucherVisibility(Result);
                detailToggle.setOnClickListener(onDetailButtonClosedListener(this));
            }
        };
    }

    private View.OnClickListener onDetailButtonClosedListener(final OnClickListener previousListener) {
        return new OnClickListener() {
            @Override
            public void onClick(View view) {
                detailToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0);
                transactionDetail.setVisibility(View.GONE);
                detailToggle.setOnClickListener(previousListener);
            }
        };
    }

    private void setLoyaltyPoints(String loyaltyPoints, int loyaltyValue) {
        if (loyaltyValue > 0) {
            loyaltyPointsTextView.setVisibility(View.VISIBLE);
            loyaltyPointsTitle.setVisibility(View.VISIBLE);
            loyaltyPointsTextView.setText("(" + loyaltyPoints + ")");
        }
    }

    private void sendGTMConfirm(){
        UnifyTracking.eventCartThankYou();
    }

    @Override
    public void onResume() {
        super.onResume();
        ScreenTracking.screen(this);
    }
}
