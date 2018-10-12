package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDefaultFragment extends DigitalBaseCartFragment implements DigitalCartDefaultContract.View {

    private ProgressBar progressBar;
    private LinearLayout containerLayout;
    private AppCompatTextView categoryTextView;


    public static DigitalCartDefaultFragment newInstance(CartDigitalInfoData cartDigitalInfoData,
                                                         CheckoutDataParameter checkoutDataParameter,
                                                         DigitalCheckoutPassData passData) {
        DigitalCartDefaultFragment fragment = new DigitalCartDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PASS_DATA, passData);
        bundle.putParcelable(ARG_CART_INFO, cartDigitalInfoData);
        bundle.putParcelable(ARG_CHECKOUT_INFO, checkoutDataParameter);
        fragment.setArguments(bundle);
        return fragment;

    }

    public DigitalCartDefaultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_cart_default, container, false);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void setupView(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        containerLayout = view.findViewById(R.id.container);
        categoryTextView = view.findViewById(R.id.tv_category_name);
        detailHolderView = view.findViewById(R.id.view_cart_detail);
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
    }

    @Override
    public void renderCategory(String categoryName) {
        categoryTextView.setText(categoryName);
    }

    @Override
    public void hideContent() {
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        containerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
