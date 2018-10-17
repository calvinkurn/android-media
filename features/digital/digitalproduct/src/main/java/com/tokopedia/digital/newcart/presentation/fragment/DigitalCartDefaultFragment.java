package com.tokopedia.digital.newcart.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartDefaultContract;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartDefaultPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DigitalCartDefaultFragment extends DigitalBaseCartFragment implements DigitalCartDefaultContract.View {

    private ProgressBar progressBar;
    private RelativeLayout containerLayout;
    private AppCompatTextView categoryTextView;

    @Inject
    DigitalCartDefaultPresenter presenter;

    public static DigitalCartDefaultFragment newInstance(DigitalCheckoutPassData passData) {
        DigitalCartDefaultFragment fragment = new DigitalCartDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PASS_DATA, passData);
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
        getComponent(DigitalCartComponent.class).inject(this);
        super.presenter = presenter;
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
        checkoutHolderView = view.findViewById(R.id.view_checkout_holder);
        inputPriceContainer = view.findViewById(R.id.input_price_container);
        inputPriceHolderView = view.findViewById(R.id.input_price_holder_view);
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
