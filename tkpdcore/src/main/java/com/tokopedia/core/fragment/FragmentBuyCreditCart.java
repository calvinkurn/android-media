//package com.tokopedia.core.fragment;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.tokopedia.core.R;
//import com.tokopedia.core.app.V2BaseFragment;
//import com.tokopedia.core.customadapter.SimplePaymentListViewAdapter;
//
//import java.util.ArrayList;
//
//
///**
// * Created by Tkpd_Eka on 3/16/2015.
// */
//public class FragmentBuyCreditCart extends V2BaseFragment{
//
//    private ViewHolder holder;
//
//    private class ViewHolder{
//        ListView list;
//        TextView totalPayment;
//        TextView tokopediaBalance;
//        View paymentMode;
//        EditText useBalance;
//        View useBalanceView;
//        View checkout;
//        TextView selectedPayment;
//    }
//
//    @Override
//    protected Object getHolder() {
//        return holder;
//    }
//
//    @Override
//    protected void setHolder(Object holder) {
//        this.holder = (ViewHolder) holder;
//    }
//
//    @Override
//    protected void initView() {
//        holder.list = (ListView)findViewById(R.id.list);
//        holder.totalPayment = (TextView)findViewById(R.id.total_payment);
//        holder.tokopediaBalance = (TextView)findViewById(R.id.tokopedia_balance);
//        holder.paymentMode = findViewById(R.id.payment_wrapper);
//        holder.useBalanceView = findViewById(R.id.use_saldo_wrapper);
//        holder.useBalance = (EditText)findViewById(R.id.et_tkpd_balance);
//        holder.checkout = findViewById(R.id.checkout_but);
//        holder.selectedPayment = (TextView) findViewById(R.id.spinner_payment);
//    }
//
//    @Override
//    protected void setListener() {
//        holder.paymentMode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCreateChoosePaymentDialog();
//            }
//        });
//        holder.checkout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCheckOut();
//            }
//        });
//    }
//
//    private void onCreateChoosePaymentDialog(){
//        final ArrayList<String> PaymentName = getPaymentNameList();
//        final ArrayList<Integer> PaymentImg = getPaymentImage();
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//        View dialogView = View.inflate(getActivity(), R.layout.choose_payment_dialog, null);
//        ListView paymentList = (ListView)dialogView.findViewById(R.id.lv_payment_method);
//        SimplePaymentListViewAdapter lvadapter = new SimplePaymentListViewAdapter(getActivity(), PaymentName, PaymentImg, false);
//        paymentList.setAdapter(lvadapter);
//        dialog.setView(dialogView);
//        Dialog dia = dialog.create();
//        paymentList.setOnItemClickListener(onPaymentSelected(dia));
//        dia.show();
//    }
//
//    private AdapterView.OnItemClickListener onPaymentSelected(final Dialog dia){
//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                setSelectedPayment(position);
//                dia.dismiss();
//            }
//        };
//    }
//
//    @SuppressWarnings("deprecation")
//    private void setSelectedPayment(int position){
//        holder.selectedPayment.setText(getPaymentNameList().get(position));
//        holder.selectedPayment.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(getPaymentImage().get(position)), null, null, null);
//    }
//
//    private void onCheckOut(){
////        CreditCardUtils.SendTokenAsync async = new CreditCardUtils.SendTokenAsync();
////        CreditCardUtils.TestCC(getActivity(), async);
//    }
//
//    private ArrayList<String> getPaymentNameList(){
//        ArrayList<String> PaymentName = new ArrayList<>();
//        PaymentName.add(getString(R.string.title_saldo_tokopedia));
//        PaymentName.add(getString(R.string.title_mandiri_clickpay));
//        PaymentName.add(getString(R.string.title_mandiri_ecash));
//        PaymentName.add(getString(R.string.title_bca_klikpay));
//        PaymentName.add("Credit Card");
//        return PaymentName;
//    }
//
//    private ArrayList<Integer> getPaymentImage(){
//        final ArrayList<Integer> PaymentImg = new ArrayList<>();
//        PaymentImg.add(R.drawable.payment_saldo);
//        PaymentImg.add(R.drawable.payment_clickpay);
//        PaymentImg.add(R.drawable.payment_ecash);
//        PaymentImg.add(R.drawable.payment_bca_klikpay);
//        PaymentImg.add(R.drawable.payment_ecash);
//        return PaymentImg;
//    }
//
//    @Override
//    protected void onCreateView() {
//
//    }
//
//    @Override
//    protected int getRootViewId() {
//        return R.layout.fragment_buy_credit_cart;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initVar();
//    }
//
//    private void initVar(){
//        holder = new ViewHolder();
//    }
//
//}
