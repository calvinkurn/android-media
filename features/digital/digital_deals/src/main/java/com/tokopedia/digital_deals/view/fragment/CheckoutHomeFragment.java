package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.contractor.CheckoutDealContractor;
import com.tokopedia.digital_deals.view.presenter.CheckoutDealPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

public class CheckoutHomeFragment extends BaseDaggerFragment implements CheckoutDealContractor.View, View.OnClickListener {



    private ConstraintLayout clApplyPromo;
    private ConstraintLayout clPromoApplied;
    private ConstraintLayout baseMainContent;
    private LinearLayout mainContent;
    private LinearLayout llPaymentMethod;

    private ImageView imageViewBrand;
    private TextView tvDealDetails;
    private TextView tvBrandName;
    private TextView tvMrp;
    private TextView tvSalesPrice;
    private TextView tvTotalQuantityPrice;
    private TextView tvServiceFee;
    private TextView tvExpiryDate;
    private TextView tvNumberLocations;
    private TextView tvVoucherCode;
    private TextView tvDiscount;
    private TextView tvNumberVouchers;
    private TextView tvAmount;
    private EditText etEmailID;

    @Inject
    CheckoutDealPresenter mPresenter;

    @Override
    protected void initInjector() {
        DaggerDealsComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .dealsModule(new DealsModule(getContext()))
                .build().inject(this);
        mPresenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CheckoutHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_deal, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);
        mPresenter.getProfile();
        mPresenter.getCheckoutDetails();
        return view;
    }

    private void setViewIds(View view) {
        ((CheckoutActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.activity_checkout_title));
        imageViewBrand = view.findViewById(R.id.image_view_brand);
        tvDealDetails = view.findViewById(R.id.tv_deal_details);
        tvBrandName = view.findViewById(R.id.tv_brand_name);
        tvMrp = view.findViewById(R.id.tv_mrp_per_quantity);
        tvSalesPrice = view.findViewById(R.id.tv_sales_price_per_quantity);
        tvTotalQuantityPrice = view.findViewById(R.id.tv_sales_price_all_quantity);
        tvServiceFee = view.findViewById(R.id.tv_service_fee_amount);
        tvAmount = view.findViewById(R.id.tv_total_amount);
        tvExpiryDate = view.findViewById(R.id.tv_expiry_date);
        tvNumberLocations = view.findViewById(R.id.tv_no_locations);
        tvVoucherCode = view.findViewById(R.id.tv_voucher_code);
        tvDiscount = view.findViewById(R.id.amount_of_cashback);
        tvNumberVouchers = view.findViewById(R.id.tv_number_vouchers);
        etEmailID = view.findViewById(R.id.tv_email);
        llPaymentMethod = view.findViewById(R.id.ll_select_payment_method);
        clApplyPromo = view.findViewById(R.id.cl_apply_promo);
        clPromoApplied = view.findViewById(R.id.cl_promo_applied);
        baseMainContent = view.findViewById(R.id.base_main_content);
        mainContent = view.findViewById(R.id.main_content);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);

    }

    @Override
    public void renderFromDetails(DealsDetailsViewModel dealDetails, PackageViewModel packageViewModel) {

        if (dealDetails.getBrand() != null) {
            ImageHandler.loadImage(getContext(), imageViewBrand,
                    dealDetails.getBrand().getFeaturedThumbnailImage(),
                    R.color.grey_1100, R.color.grey_1100);
            tvBrandName.setText(dealDetails.getBrand().getTitle());
        }

        tvDealDetails.setText(dealDetails.getDisplayName());
        tvExpiryDate.setText(String.format(getString(R.string.valid_through),
                Utils.convertEpochToString(dealDetails.getSaleEndDate())));


        tvMrp.setText(Utils.convertToCurrencyString(packageViewModel.getMrp()));
        tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvSalesPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice()));
        tvTotalQuantityPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity()));
        tvServiceFee.setText(Utils.convertToCurrencyString(packageViewModel.getCommission()));

        tvAmount.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity() +
                packageViewModel.getCommission()));
        tvNumberVouchers.setText(String.format(getActivity().getResources().getString(R.string.number_of_vouchers),
                packageViewModel.getSelectedQuantity()));
        if (dealDetails.getOutlets() != null && dealDetails.getOutlets().size() != 0) {
            tvNumberLocations.setText(String.format(getResources().getString(R.string.number_of_locations)
                    , dealDetails.getOutlets().size()));
        }
        llPaymentMethod.setOnClickListener(this);
        clApplyPromo.setOnClickListener(this);
        baseMainContent.setVisibility(View.VISIBLE);
        llPaymentMethod.setVisibility(View.VISIBLE);
    }


    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }


    @Override
    public void setEmailID(String emailID) {
        etEmailID.setText(emailID);
    }

    @Override
    public RequestParams getParams() {

        return null;
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showPromoSuccessMessage(String text, int color) {

    }

    @Override
    public void showCashbackMessage(String text) {

    }

    @Override
    public void hideSuccessMessage() {

    }

    @Override
    public boolean validateAllFields() {
        return false;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_email) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!etEmailID.isEnabled()) {
                etEmailID.setEnabled(true);
                etEmailID.setTextIsSelectable(true);
                etEmailID.setFocusable(true);
                etEmailID.setFocusableInTouchMode(true);
                etEmailID.setSelection(etEmailID.getText().length());
                etEmailID.setInputType(InputType.TYPE_CLASS_TEXT);
                etEmailID.requestFocus();
                imm.showSoftInput(etEmailID, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etEmailID.getWindowToken(), 0);
                }
                etEmailID.setEnabled(false);
                etEmailID.setTextIsSelectable(false);
                etEmailID.setFocusable(false);
                etEmailID.setInputType(InputType.TYPE_NULL);
                etEmailID.clearFocus();
                mainContent.requestFocus();
            }
            mPresenter.updateEmail(etEmailID.getText().toString());
        } else if (v.getId() == R.id.ll_select_payment_method) {
            mPresenter.getPaymentLink();
        } else if (v.getId() == R.id.cl_apply_promo) {
            mPresenter.clickGoToPromo();
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }
}
