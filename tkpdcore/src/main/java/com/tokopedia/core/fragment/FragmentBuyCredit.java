//package com.tokopedia.core.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.tkpd.library.utils.SimpleSpinnerAdapter;
//import com.tkpd.library.ui.utilities.TkpdProgressDialog;
//import com.tokopedia.core.BuyCreditCart;
//import com.tokopedia.core.R;
//import com.tokopedia.core.app.V2BaseFragment;
//import com.tokopedia.core.facade.FacadeActionBuyCredit;
//import com.tokopedia.core.facade.FacadeBuyCredit;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Tkpd_Eka on 3/16/2015.
// */
//public class FragmentBuyCredit extends V2BaseFragment{
//
//    private ViewHolder holder;
//    private FacadeActionBuyCredit facadeAction;
//    private FacadeBuyCredit facade;
//    private List<Model> modelProvider;
//    private SimpleSpinnerAdapter providerAdapter;
//    private TkpdProgressDialog progressDialog;
//
//    public static class Model{
//        public String name;
//        public String id;
//        public List<String> valueList;
//        public List<String> priceList;
//        public List<String> valueId;
//
//        public Model(){
//            valueList = new ArrayList<>();
//            priceList = new ArrayList<>();
//            valueId = new ArrayList<>();
//        }
//    }
//
//    private class ViewHolder{
//        Spinner provider;
//        Spinner value;
//        EditText phone;
//        View buyButton;
//        TextView price;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initVar();
//        getData();
//    }
//
//    private void initVar(){
//        holder = new ViewHolder();
//        facade = FacadeBuyCredit.createInstance(getActivity());
//        facadeAction = FacadeActionBuyCredit.createInstance(getActivity());
//        modelProvider = new ArrayList<>();
//        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
//    }
//
//    private void getData(){
//        progressDialog.showDialog();
//        facade.getProvider(onGetData());
//    }
//
//    private FacadeBuyCredit.GetProviderListener onGetData(){
//        return new FacadeBuyCredit.GetProviderListener() {
//            @Override
//            public void onSuccess(List<Model> modelList) {
//                modelProvider = modelList;
//                onGetDataSuccess();
//            }
//
//            @Override
//            public void onFailed() {
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onError(String message) {
//
//            }
//        };
//    }
//
//    private void onGetDataSuccess(){
//        progressDialog.dismiss();
//        providerAdapter = SimpleSpinnerAdapter.createAdapter(getActivity(), getProviderNameList());
//        holder.provider.setAdapter(providerAdapter);
//    }
//
//    private List<String> getProviderNameList(){
//        List<String> providerList = new ArrayList<>();
//        for(Model prov : modelProvider){
//            providerList.add(prov.name);
//        }
//        return providerList;
//    }
//
//    @Override
//    protected Object getHolder() {
//        return holder;
//    }
//
//    @Override
//    protected void setHolder(Object holder) {
//        this.holder = (ViewHolder)holder;
//    }
//
//    @Override
//    protected void initView(){
//        holder.provider = (Spinner) findViewById(R.id.provider);
//        holder.value = (Spinner) findViewById(R.id.value);
//        holder.phone = (EditText) findViewById(R.id.phone_number);
//        holder.buyButton = findViewById(R.id.buy_button);
//        holder.price = (TextView) findViewById(R.id.price);
//    }
//
//    @Override
//    protected void setListener(){
//        holder.provider.setOnItemSelectedListener(onProviderSelected());
//        holder.buyButton.setOnClickListener(onBuyClick());
//        holder.value.setOnItemSelectedListener(onValueSelected());
//    }
//
//    @Override
//    protected void onCreateView() {
//    }
//
//    @Override
//    protected int getRootViewId() {
//        return R.layout.fragment_buy_credit;
//    }
//
//    private AdapterView.OnItemSelectedListener onProviderSelected(){
//        return new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                updateCreditValue(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//    }
//
//    private AdapterView.OnItemSelectedListener onValueSelected(){
//        return new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                updatePrice(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        };
//    }
//
//    private void updateCreditValue(int pos){
//        SimpleSpinnerAdapter adapter = SimpleSpinnerAdapter.createAdapter(getActivity(), modelProvider.get(pos).valueList);
//        holder.value.setAdapter(adapter);
//    }
//
//    private void updatePrice(int pos){
//        holder.price.setText("Rp. " + modelProvider.get(holder.provider.getSelectedItemPosition()).priceList.get(pos));
//    }
//
//    private View.OnClickListener onBuyClick(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionBuyCredit();
//            }
//        };
//    }
//
//    private void actionBuyCredit(){
//        startActivity(new Intent(getActivity(), BuyCreditCart.class));
////        progressDialog.showDialog();
////        facadeAction.actionBuyCredit(getProviderId(),getValueId(), getPhoneNo(), onActionBuyCredit());
//    }
//
//    private FacadeActionBuyCredit.BuyCreditInterface onActionBuyCredit(){
//        return new FacadeActionBuyCredit.BuyCreditInterface() {
//            @Override
//            public void onSuccess() {
//                progressDialog.dismiss();
//            }
//        };
//    }
//
//    private String getProviderId(){
//        return modelProvider.get(holder.provider.getSelectedItemPosition()).id;
//    }
//
//    private String getValueId(){
//        return modelProvider.get(holder.provider.getSelectedItemPosition()).valueId.get(holder.value.getSelectedItemPosition());
//    }
//
//    private String getPhoneNo(){
//        return holder.phone.getText().toString();
//    }
//
//}
