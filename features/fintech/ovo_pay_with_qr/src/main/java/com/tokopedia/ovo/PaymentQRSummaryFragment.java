package com.tokopedia.ovo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.ovo.model.BarcodeResponseData;

public class PaymentQRSummaryFragment extends BaseDaggerFragment {
    String id;
    BarcodeResponseData responseData;
    TextView shopName;
    TextView shopDetail;
    LinearLayout bayarLayout;
    TextView bayarBtn;
    ProgressBar progressBar;
    LinearLayout ovoDetailLayout;
    Switch switchButton;

    public static Fragment createInstance(String qrData) {
        Bundle bundle = new Bundle();
        bundle.putString("QR_DATA", qrData);
        Fragment fragment = new PaymentQRSummaryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("QR_DATA");
        SaveInstanceCacheManager cacheManager = new SaveInstanceCacheManager(getActivity(), savedInstanceState);
        if(savedInstanceState == null)
            cacheManager = new SaveInstanceCacheManager(getActivity(), id);

        JsonObject response = cacheManager.get("QR_RESPONSE", new TypeToken<JsonObject>() {}.getType());
        Log.e("sandeep", response != null ? response.toString(): "null");
        if(response != null) {
            responseData = new GsonBuilder().create().fromJson(response, BarcodeResponseData.class);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_qr_summary_fragment, container, false);
        shopName = view.findViewById(R.id.shop_name);
        shopDetail = view.findViewById(R.id.merchant_name);
        bayarLayout = view.findViewById(R.id.btn_text_layout);
        ovoDetailLayout = view.findViewById(R.id.ovo_detail_layout);
        switchButton = view.findViewById(R.id.switch_ovo);
        bayarBtn = view.findViewById(R.id.bayar_btn);
        progressBar = view.findViewById(R.id.pay_progress);
        if(responseData.getGoalQRInquiry() != null){
            shopName.setText(responseData.getGoalQRInquiry().getMerchant().getName());
            shopDetail.setText(responseData.getGoalQRInquiry().getMerchant().getDescription());
            bayarBtn.setOnClickListener(view1 -> {
                bayarLayout.setAlpha(0.19f);
                progressBar.setVisibility(View.VISIBLE);
                bayarBtn.setClickable(false);
            });
            switchButton.setOnCheckedChangeListener((compoundButton, b) -> {
                ovoDetailLayout.setVisibility(b ? View.VISIBLE: View.GONE);
            });
        }
        return view;
    }
}
