package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.contractor.SelectQuantityContract;
import com.tokopedia.digital_deals.view.model.PackageViewModel;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.SelectQuantityPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SelectDealQuantityFragment extends BaseDaggerFragment implements SelectQuantityContract.View, View.OnClickListener {


    private int MAX_QUANTITY = 1;
    private int CURRENT_QUANTITY = 1;

    private TextView tvContinue;
    private CoordinatorLayout mainContent;
    private FrameLayout progressBarLayout;

    private Toolbar toolbar;
    private ImageView ivBrand;
    private ImageView ivAdd;
    private ImageView ivSubtract;
    private TextView tvDealDetails;
    private TextView tvBrandName;
    private TextView tvMrp;
    private TextView tvSalesPrice;
    private TextView tvTotalAmount;
    private TextView tvQuantity;

    @Inject
    public SelectQuantityPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;

    private PackageViewModel packageViewModel;
    private DealsDetailsResponse dealDetails;
    private DealFragmentCallbacks fragmentCallbacks;
    private static final int LOGIN_REQUEST_CODE = 1099;

    public static Fragment createInstance() {
        Fragment fragment = new SelectDealQuantityFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dealDetails = fragmentCallbacks.getDealDetails();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deal_quantity, container, false);
        setHasOptionsMenu(true);
        setViewIds(view);
        mPresenter.initialize(dealDetails);
        return view;
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (DealDetailsActivity) activity;
    }

    private void setViewIds(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_deals));
        toolbar.setTitle(getActivity().getResources().getString(R.string.select_number_of_voucher));
        ivBrand = view.findViewById(R.id.iv_brand);
        ivAdd = view.findViewById(R.id.iv_add);
        ivSubtract = view.findViewById(R.id.iv_subtract);
        tvDealDetails = view.findViewById(R.id.tv_deal_details);
        tvBrandName = view.findViewById(R.id.tv_brand_name);
        tvQuantity = view.findViewById(R.id.tv_no_quantity);
        tvMrp = view.findViewById(R.id.tv_mrp);
        tvSalesPrice = view.findViewById(R.id.tv_sales_price);
        tvTotalAmount = view.findViewById(R.id.tv_total_amount);
        tvContinue = view.findViewById(R.id.tv_continue);
        progressBarLayout = view.findViewById(R.id.progress_bar_layout);
        mainContent = view.findViewById(R.id.main_content);
    }

    void setButtons() {

        if (CURRENT_QUANTITY > 1) {
            ivSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_250), android.graphics.PorterDuff.Mode.SRC_IN);
            ivSubtract.setClickable(true);

        } else {
            ivSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            ivSubtract.setClickable(false);
        }
        if (CURRENT_QUANTITY < MAX_QUANTITY) {
            ivAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_250), android.graphics.PorterDuff.Mode.SRC_IN);
            ivAdd.setClickable(true);

        } else {
            ivAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            ivAdd.setClickable(false);
        }
    }


    void setUpTotalAmount() {

        tvTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice() * CURRENT_QUANTITY));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_subtract) {
            if (CURRENT_QUANTITY > 1) {
                CURRENT_QUANTITY--;
                tvQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.iv_add) {
            if (CURRENT_QUANTITY < MAX_QUANTITY) {
                CURRENT_QUANTITY++;
                tvQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.tv_continue) {
            packageViewModel = new PackageViewModel();
            packageViewModel.setCategoryId(dealDetails.getCategoryId());
            packageViewModel.setProductId(dealDetails.getId());
            packageViewModel.setSalesPrice(dealDetails.getSalesPrice());
            packageViewModel.setMrp(dealDetails.getMrp());
            packageViewModel.setSelectedQuantity(CURRENT_QUANTITY);
            packageViewModel.setDigitalCategoryID(dealDetails.getCatalog().getDigitalCategoryId());
            packageViewModel.setDigitalProductID(dealDetails.getCatalog().getDigitalProductId());
            mPresenter.verifyCart(packageViewModel);
            if(dealDetails.getBrand()!=null){
                dealsAnalytics.sendEcommerceQuantity(dealDetails.getId(), CURRENT_QUANTITY, dealDetails.getSalesPrice(),
                        dealDetails.getDisplayName(), dealDetails.getBrand().getTitle());
            }

        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void renderFromDetails(DealsDetailsResponse dealDetail) {

        if (dealDetail == null)
            return;

        ImageHandler.loadImage(getContext(), ivBrand, dealDetails.getImageWeb(), R.color.grey_1100, R.color.grey_1100);
        if (dealDetails.getBrand() != null) {
            tvBrandName.setText(dealDetails.getBrand().getTitle());
        }

        tvDealDetails.setText(dealDetails.getDisplayName());

        tvQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
        if (dealDetails.getMrp() != 0 && dealDetail.getMrp() != dealDetail.getSalesPrice()) {
            tvMrp.setVisibility(View.VISIBLE);
            tvMrp.setText(Utils.convertToCurrencyString(dealDetails.getMrp()));
            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvMrp.setVisibility(View.GONE);
        }
        tvSalesPrice.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));
        tvTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));

        tvContinue.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        ivSubtract.setOnClickListener(this);
    }

    @Override
    public RequestParams getParams() {
        return null;
    }

    @Override
    public void showPayButton() {

    }

    @Override
    public void hidePayButton() {

    }

    @Override
    public void showProgressBar() {
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public int getRequestCode() {
        return LOGIN_REQUEST_CODE;
    }

    @Override
    public void showFailureMessage(String error) {
        Utils.getSingletonInstance().showSnackBarDeals(error
                , getContext(), mainContent, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode);
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }
}
