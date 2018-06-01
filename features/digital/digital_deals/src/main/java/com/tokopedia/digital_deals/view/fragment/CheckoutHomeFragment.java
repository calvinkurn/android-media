package com.tokopedia.digital_deals.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DaggerDealsComponent;
import com.tokopedia.digital_deals.di.DealsModule;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.contractor.CheckoutDealContractor;
import com.tokopedia.digital_deals.view.presenter.CheckoutDealPresenter;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.DealsDetailsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PackageViewModel;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

public class CheckoutHomeFragment extends BaseDaggerFragment implements CheckoutDealContractor.View, View.OnClickListener {

    private DealsDetailsViewModel dealDetails;
    private Toolbar toolbar;
    private ImageView imageViewBrand;

    private TextView textViewdealDetails;
    private TextView textViewbrandName;
    private TextView textViewMrp;
    private TextView textViewSalesPrice;
    private TextView textViewTotalQuantityPrice;
    private TextView textViewServiceFeeAmount;
    private TextView textViewExpiryDate;
    private TextView textViewNumberLocation;
    private TextView textViewVoucherCode;
    private TextView textViewDiscountAmount;
    private TextView textViewNumberVouchers;
    private TextView textViewTotalAmount;
    private TextView textViewUpdateEmail;
    private PackageViewModel packageViewModel;
    private ConstraintLayout clApplyPromo;
    private ConstraintLayout clPromoApplied;
    private ConstraintLayout baseMainContent;
    private LinearLayout mainContent;

    private LinearLayout llPaymentMethod;


    @Inject
    CheckoutDealPresenter mPresenter;

    public static final int PAYMENT_REQUEST_CODE = 65000;
    private EditText textViewEmailID;


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
        View view = inflater.inflate(R.layout.activity_checkout_deal, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);


//        mPresenter.attachView(this);
        mPresenter.getProfile();
        mPresenter.getCheckoutDetails();
//        mPresenter.getBrandsList();
        return view;
    }

    private void setViewIds(View view) {

        ((CheckoutActivity) getActivity()).getSupportActionBar().setTitle("Checkout");
//        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_deals));
//        toolbar.setTitle(getActivity().getResources().getString(R.string.title_activity_checkout));
        imageViewBrand = view.findViewById(R.id.image_view_brand);
        textViewdealDetails = view.findViewById(R.id.tv_deal_details);
        textViewbrandName = view.findViewById(R.id.tv_brandName);
        textViewMrp = view.findViewById(R.id.tv_mrp_per_quantity);
        textViewSalesPrice = view.findViewById(R.id.tv_sales_price_per_quantity);
        textViewTotalQuantityPrice = view.findViewById(R.id.tv_sales_price_all_quantity);
        textViewServiceFeeAmount = view.findViewById(R.id.tv_service_fee_amount);
        textViewTotalAmount = view.findViewById(R.id.tv_total_amount);
        textViewExpiryDate = view.findViewById(R.id.tv_expiryDate);
        textViewNumberLocation = view.findViewById(R.id.tv_no_locations);
        textViewVoucherCode = view.findViewById(R.id.tv_voucher_code);
        textViewDiscountAmount = view.findViewById(R.id.amount_of_cashback);
        textViewNumberVouchers = view.findViewById(R.id.tv_number_vouchers);
        textViewEmailID = view.findViewById(R.id.tv_email);
        llPaymentMethod = view.findViewById(R.id.ll_select_payment_method);
        textViewUpdateEmail = view.findViewById(R.id.update_email);
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
    public void renderFromPackageVM(DealsDetailsViewModel dealDetails, PackageViewModel packageViewModel) {

        if (dealDetails.getBrand() != null) {
            ImageHandler.loadImage(getContext(), imageViewBrand,
                    dealDetails.getBrand().getFeaturedThumbnailImage(),
                    R.color.grey_1100, R.color.grey_1100);
            textViewbrandName.setText(dealDetails.getBrand().getTitle());
        }

        textViewdealDetails.setText(dealDetails.getDisplayName());
        textViewExpiryDate.setText(String.format(getString(R.string.valid_through),
                Utils.convertEpochToString(dealDetails.getSaleEndDate())));


        textViewMrp.setText(Utils.convertToCurrencyString(packageViewModel.getMrp()));
        textViewMrp.setPaintFlags(textViewMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        textViewSalesPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice()));
        textViewTotalQuantityPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity()));
        textViewServiceFeeAmount.setText(Utils.convertToCurrencyString(packageViewModel.getCommission()));

        textViewTotalAmount.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity() +
                packageViewModel.getCommission()));
        textViewNumberVouchers.setText(String.format(getActivity().getResources().getString(R.string.number_of_vouchers),
                packageViewModel.getSelectedQuantity()));
        if (dealDetails.getOutlets() != null && dealDetails.getOutlets().size() != 0) {


        }
        llPaymentMethod.setOnClickListener(this);
        textViewUpdateEmail.setOnClickListener(this);
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
        textViewEmailID.setText(emailID);
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
            if (!textViewEmailID.isEnabled()) {
                textViewEmailID.setEnabled(true);
                textViewEmailID.setTextIsSelectable(true);
                textViewEmailID.setFocusable(true);
                textViewEmailID.setFocusableInTouchMode(true);
                textViewEmailID.setSelection(textViewEmailID.getText().length());
                textViewEmailID.setInputType(InputType.TYPE_CLASS_TEXT);
                textViewEmailID.requestFocus();
                imm.showSoftInput(textViewEmailID, InputMethodManager.SHOW_IMPLICIT);
            } else {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(textViewEmailID.getWindowToken(), 0);
                }
                textViewEmailID.setEnabled(false);
                textViewEmailID.setTextIsSelectable(false);
                textViewEmailID.setFocusable(false);
                textViewEmailID.setInputType(InputType.TYPE_NULL);
                textViewEmailID.clearFocus();
                mainContent.requestFocus();
            }
            mPresenter.updateEmail(textViewEmailID.getText().toString());
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
}
