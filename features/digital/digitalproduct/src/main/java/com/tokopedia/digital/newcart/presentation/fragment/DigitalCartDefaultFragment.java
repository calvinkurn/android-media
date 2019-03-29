package com.tokopedia.digital.newcart.presentation.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.newcart.di.DigitalCartComponent;
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

    private InteractionListener interactionListener;

    public interface InteractionListener {
        void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData passData);

        void inflateMyBillsSubscriptionPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData cartPassData);
    }

    @Inject
    DigitalCartDefaultPresenter presenter;

    public static DigitalCartDefaultFragment newInstance(DigitalCheckoutPassData passData) {
        DigitalCartDefaultFragment fragment = new DigitalCartDefaultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_digital_cart_default, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
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
    public void renderCategoryInfo(String categoryName) {
        categoryTextView.setText(categoryName);
    }

    @Override
    public void hideCartView() {
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void showFullPageLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCartView() {
        containerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullPageLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void inflateDealsPage(CartDigitalInfoData cartDigitalInfoData, DigitalCheckoutPassData cartPassData) {
        if (interactionListener != null)
            interactionListener.inflateDealsPage(cartDigitalInfoData, cartPassData);
    }


    @Override
    public void inflateMyBillsSubscriptionPage(CartDigitalInfoData cartDigitalInfoData,
                                               DigitalCheckoutPassData cartPassData) {
        if (interactionListener != null)
            interactionListener.inflateMyBillsSubscriptionPage(cartDigitalInfoData, cartPassData);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        interactionListener = (InteractionListener) context;
    }
}
