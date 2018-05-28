package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.contractor.SelectQuantityContract;
import com.tokopedia.digital_deals.view.presenter.SelectQuantityPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

public class SelectDealQuantityFragment extends BaseDaggerFragment implements SelectQuantityContract.View, View.OnClickListener {

    private DealsDetailsViewModel dealDetails;
    private DealFragmentCallbacks fragmentCallbacks;
    private Toolbar toolbar;
    private ImageView imageViewBrand;
    private ImageView imageViewAdd;
    private ImageView imageViewSubtract;
    private TextView textViewdealDetails;
    private TextView textViewbrandName;
    private TextView textViewMrp;
    private TextView textViewSalesPrice;
    private TextView textViewTotalAmount;
    private TextView textViewQuantity;
    private PackageViewModel packageViewModel;
    @Inject
    public SelectQuantityPresenter mPresenter;
    private LinearLayout llContinue;
    private int MAX_QUANTITY = 8;
    private int CURRENT_QUANTITY = 1;

    private static final int EVENT_LOGIN_REQUEST = 1099;
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
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
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
        imageViewBrand = view.findViewById(R.id.imageViewBrand);
        imageViewAdd = view.findViewById(R.id.iv_add);
        imageViewSubtract = view.findViewById(R.id.iv_subtract);
        textViewdealDetails = view.findViewById(R.id.tv_deal_details);
        textViewbrandName = view.findViewById(R.id.tv_brandName);
        textViewQuantity = view.findViewById(R.id.tv_no_quantity);
        textViewMrp = view.findViewById(R.id.tvMrp);
        textViewSalesPrice = view.findViewById(R.id.tvSalesPrice);
        textViewTotalAmount = view.findViewById(R.id.tv_total_amount);
        llContinue = view.findViewById(R.id.ll_continue);

    }

    void setButtons() {

        if (CURRENT_QUANTITY > 0) {
            imageViewSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_nob), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewSubtract.setClickable(true);

        } else {
            imageViewSubtract.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewSubtract.setClickable(false);
        }
        if (CURRENT_QUANTITY < MAX_QUANTITY) {
            imageViewAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.green_nob), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewAdd.setClickable(true);

        } else {
            imageViewAdd.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_400), android.graphics.PorterDuff.Mode.SRC_IN);
            imageViewAdd.setClickable(false);
        }
    }


    void setUpTotalAmount() {
        if (CURRENT_QUANTITY > 0) {
            textViewTotalAmount.setVisibility(View.VISIBLE);
            llContinue.setVisibility(View.VISIBLE);
            textViewTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice() * CURRENT_QUANTITY));
        } else {
            textViewTotalAmount.setVisibility(View.GONE);
            llContinue.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_subtract) {
            if (CURRENT_QUANTITY > 0) {
                CURRENT_QUANTITY--;
                textViewQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.iv_add) {
            if (CURRENT_QUANTITY < MAX_QUANTITY) {
                CURRENT_QUANTITY++;
                textViewQuantity.setText(String.format(getContext().getResources().getString(R.string.quantity_of_deals), CURRENT_QUANTITY));
            }
            setUpTotalAmount();
            setButtons();

        } else if (v.getId() == R.id.ll_continue) {
            packageViewModel=new PackageViewModel();
            packageViewModel.setCategoryId(dealDetails.getCategoryId());
            packageViewModel.setProductId(dealDetails.getId());
            packageViewModel.setSalesPrice(dealDetails.getSalesPrice());
            packageViewModel.setSelectedQuantity(CURRENT_QUANTITY);
            packageViewModel.setDigitalCategoryID(dealDetails.getCatalog().getDigitalCategoryId());
            packageViewModel.setDigitalProductID(dealDetails.getCatalog().getDigitalProductId());

            mPresenter.verifyCart(packageViewModel);
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
    public void renderFromDetails(DealsDetailsViewModel dealDetail) {

        if (dealDetails.getBrand() != null) {
            ImageHandler.loadImage(getContext(), imageViewBrand, dealDetails.getBrand().getFeaturedThumbnailImage(), R.color.grey_1100, R.color.grey_1100);
            textViewbrandName.setText(dealDetails.getBrand().getTitle());
        }

        textViewdealDetails.setText(dealDetails.getDisplayName());

        textViewQuantity.setText(CURRENT_QUANTITY + "");
        textViewMrp.setText(Utils.convertToCurrencyString(dealDetails.getMrp()));
        textViewMrp.setPaintFlags(textViewMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        textViewSalesPrice.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));
        textViewTotalAmount.setText(Utils.convertToCurrencyString(dealDetails.getSalesPrice()));

        llContinue.setOnClickListener(this);
        imageViewAdd.setOnClickListener(this);
        imageViewSubtract.setOnClickListener(this);
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public int getRequestCode() {
        return EVENT_LOGIN_REQUEST;
    }
}
