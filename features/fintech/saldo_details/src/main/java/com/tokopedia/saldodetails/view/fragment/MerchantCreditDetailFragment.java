package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS;

public class MerchantCreditDetailFragment extends BaseDaggerFragment {

    private Context context;
    private GqlMerchantCreditResponse merchantCreditDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_credit_details, container, false);
        Bundle bundle = getArguments();
        merchantCreditDetails = bundle != null ? bundle.getParcelable(BUNDLE_PARAM_MERCHANT_CREDIT_DETAILS) : null;
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateData();
        initListeners();
    }

    private void initListeners() {

    }

    private void populateData() {

    }

    public static Fragment newInstance(Bundle bundle) {
        Fragment fragment = new MerchantCreditDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
