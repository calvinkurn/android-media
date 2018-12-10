package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.di.DealsComponent;
import com.tokopedia.digital_deals.view.activity.CheckoutActivity;
import com.tokopedia.digital_deals.view.contractor.CheckoutDealContractor;
import com.tokopedia.digital_deals.view.model.PackageViewModel;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.digital_deals.view.presenter.CheckoutDealPresenter;
import com.tokopedia.digital_deals.view.utils.DealFragmentCallbacks;
import com.tokopedia.digital_deals.view.utils.DealsAnalytics;
import com.tokopedia.digital_deals.view.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

;

public class CheckoutHomeFragment extends BaseDaggerFragment implements CheckoutDealContractor.View, View.OnClickListener {


    private ConstraintLayout clPromoApplied;
    private ConstraintLayout baseMainContent;
    private ConstraintLayout clPromoAmount;
    private ViewGroup mainContent;
    private TextView tvPaymentMethod;
    private CardView paymentMethod;
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
    private TextView tvApplyPromo;
    private TextView tvNumberVouchers;
    private TextView tvAmount;
    private EditText etEmailID;
    private FrameLayout progressParLayout;
    private DealFragmentCallbacks fragmentCallbacks;
    private ImageView ivRemovePromo;
    private int quantity;

    @Inject
    CheckoutDealPresenter mPresenter;
    @Inject
    DealsAnalytics dealsAnalytics;
    private DealsDetailsResponse dealDetails;
    private boolean promoApplied = false;

    @Override
    protected void initInjector() {
        getComponent(DealsComponent.class).inject(this);
        mPresenter.attachView(this);
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new CheckoutHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_deal, container, false);
        setViewIds(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getCheckoutDetails();
    }

    private void setViewIds(View view) {
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
        paymentMethod = view.findViewById(R.id.cl_btn_payment);
        setCardViewElevation();
        tvPaymentMethod = view.findViewById(R.id.ll_select_payment_method);
        tvApplyPromo = view.findViewById(R.id.tv_promocode);
        clPromoApplied = view.findViewById(R.id.cl_promo_applied);
        baseMainContent = view.findViewById(R.id.base_main_content);
        mainContent = view.findViewById(R.id.main_content);
        progressParLayout = view.findViewById(R.id.progress_bar_layout);
        ivRemovePromo = view.findViewById(R.id.iv_remove_promo);
        clPromoAmount = view.findViewById(R.id.cl_promo);
        Drawable img = getResources().getDrawable(R.drawable.ic_promo_code);
        tvApplyPromo.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

    }

    private void setCardViewElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paymentMethod.setCardElevation(getResources().getDimension(R.dimen.dp_8));
        } else {
            paymentMethod.setCardElevation(getResources().getDimension(R.dimen.dp_0));
        }
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
    public void renderFromDetails(DealsDetailsResponse dealDetails, PackageViewModel packageViewModel) {


        if (dealDetails == null)
            return;

        this.dealDetails = dealDetails;
        quantity = packageViewModel.getSelectedQuantity();
        ImageHandler.loadImage(getContext(), imageViewBrand,
                dealDetails.getImageWeb(),
                R.color.grey_1100, R.color.grey_1100);

        if (dealDetails.getBrand() != null)
            tvBrandName.setText(dealDetails.getBrand().getTitle());


        tvDealDetails.setText(dealDetails.getDisplayName());
        tvExpiryDate.setText(String.format(getString(R.string.valid_through),
                Utils.convertEpochToString(dealDetails.getSaleEndDate())));

        TextView availableLocations = getView().findViewById(R.id.tv_available_locations);
        if (dealDetails.getOutlets() == null || dealDetails.getOutlets().isEmpty())
            availableLocations.setText(R.string.deals_all_indonesia);
        if (dealDetails.getMrp() != 0 && dealDetails.getMrp() != dealDetails.getSalesPrice()) {
            tvMrp.setVisibility(View.VISIBLE);
            tvMrp.setText(Utils.convertToCurrencyString(dealDetails.getMrp()));
            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvMrp.setVisibility(View.GONE);
        }

        tvSalesPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice()));
        tvTotalQuantityPrice.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity()));
        if (packageViewModel.getCommission() == 0) {
            getRootView().findViewById(R.id.tv_service_fee).setVisibility(View.GONE);
            getRootView().findViewById(R.id.tv_service_fee_amount).setVisibility(View.GONE);
        } else {
            tvServiceFee.setText(Utils.convertToCurrencyString(packageViewModel.getCommission()));

        }

        tvAmount.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity() +
                packageViewModel.getCommission()));
        tvNumberVouchers.setText(String.format(getActivity().getResources().getString(R.string.number_of_vouchers),
                packageViewModel.getSelectedQuantity()));
        if (dealDetails.getOutlets() != null && dealDetails.getOutlets().size() > 0) {
            tvNumberLocations.setText(String.format(getResources().getString(R.string.number_of_locations)
                    , dealDetails.getOutlets().size()));
        }
        tvPaymentMethod.setOnClickListener(this);
        tvApplyPromo.setOnClickListener(this);
        tvNumberLocations.setOnClickListener(this);
        ivRemovePromo.setOnClickListener(this);
        baseMainContent.setVisibility(View.VISIBLE);
        paymentMethod.setVisibility(View.VISIBLE);
    }


    @Override
    public void showProgressBar() {
        progressParLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressParLayout.setVisibility(View.GONE);
    }


    @Override
    public void setEmailIDPhoneNumber(String emailID, String phoneNumber) {
        etEmailID.setText(emailID);
        EditText etPhone = getRootView().findViewById(R.id.tv_phone);
        etPhone.setText(phoneNumber);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showPromoSuccessMessage(String text, String message, long discountAmount) {
        tvApplyPromo.setVisibility(View.GONE);
        clPromoApplied.setVisibility(View.VISIBLE);
        tvDiscount.setText(message);
        tvVoucherCode.setText(text);
        promoApplied = true;
        if (discountAmount != 0) {
            clPromoAmount.setVisibility(View.VISIBLE);
            TextView view = getRootView().findViewById(R.id.tv_promo_discount);
            view.setText(Utils.convertToCurrencyString(discountAmount));
        } else {
            clPromoAmount.setVisibility(View.GONE);
        }
        mPresenter.updateAmount(discountAmount);

    }


    @Override
    public void updateAmount(String s) {
        tvAmount.setText(s);
    }

    @Override
    public void showFailureMessageProductExpired() {
        Utils.getSingletonInstance().showSnackBarDeals(getContext().getResources().getString(R.string.product_expired)
                , getContext(), mainContent, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (CheckoutActivity) activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_select_payment_method) {
            mPresenter.getPaymentLink();
            if (dealDetails.getBrand() != null) {
                dealsAnalytics.sendEcommercePayment(dealDetails.getId(), quantity, dealDetails.getSalesPrice(),
                        dealDetails.getDisplayName(), dealDetails.getBrand().getTitle(), promoApplied);
            }
        } else if (v.getId() == R.id.tv_promocode) {
            mPresenter.clickGoToPromo();
        } else if (v.getId() == R.id.tv_no_locations) {
            fragmentCallbacks.replaceFragment(mPresenter.getOutlets(), 0);
        } else if (v.getId() == R.id.iv_remove_promo) {
            promoApplied = false;
            mPresenter.updatePromoCode("");
            tvApplyPromo.setVisibility(View.VISIBLE);
            clPromoApplied.setVisibility(View.GONE);
            clPromoAmount.setVisibility(View.GONE);
            mPresenter.updateAmount(0);
        }
    }


    @Override
    public void onDestroyView() {
        mPresenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IRouterConstant.LoyaltyModule.LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar();
            switch (resultCode) {
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE:
                    mPresenter.updatePromoCode(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE));
                    showPromoSuccessMessage(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE)
                            , data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE)
                            , data.getExtras().getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT));
                    break;
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE:
                    mPresenter.updatePromoCode(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE));
                    showPromoSuccessMessage(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE)
                            , data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE)
                            , data.getExtras().getLong(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT));
                    break;
                default:
                    break;
            }
        }
    }
}

