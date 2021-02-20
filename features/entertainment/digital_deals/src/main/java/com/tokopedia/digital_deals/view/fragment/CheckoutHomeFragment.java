package com.tokopedia.digital_deals.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
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
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView;
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView;
import com.tokopedia.unifycomponents.ticker.Ticker;

import javax.inject.Inject;


public class CheckoutHomeFragment extends BaseDaggerFragment implements CheckoutDealContractor.View, View.OnClickListener {

    public static int LOYALTY_ACTIVITY_REQUEST_CODE = 12345;
    private static final String SCREEN_NAME = "/digital/deals/checkout";

    public static String EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE";
    public static String COUPON_EXTRA_IS_USE = "EXTRA_IS_USE";
    public static String EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE";
    public static String IS_CANCEL = "IS_CANCEL";

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
    private TextView tvNumberVouchers;
    private TextView tvAmount;
    private EditText etEmailID;
    private FrameLayout progressParLayout;
    private DealFragmentCallbacks fragmentCallbacks;
    private int quantity;
    private TickerPromoStackingCheckoutView tickerApplyPromo;
    private String voucherCode;
    private String couponCode;

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
        View view = inflater.inflate(com.tokopedia.digital_deals.R.layout.fragment_checkout_deal, container, false);
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
        imageViewBrand = view.findViewById(com.tokopedia.digital_deals.R.id.image_view_brand);
        tvDealDetails = view.findViewById(com.tokopedia.digital_deals.R.id.tv_deal_details);
        tvBrandName = view.findViewById(com.tokopedia.digital_deals.R.id.tv_brand_name);
        tvMrp = view.findViewById(com.tokopedia.digital_deals.R.id.tv_mrp_per_quantity);
        tvSalesPrice = view.findViewById(com.tokopedia.digital_deals.R.id.tv_sales_price_per_quantity);
        tvTotalQuantityPrice = view.findViewById(com.tokopedia.digital_deals.R.id.tv_sales_price_all_quantity);
        tvServiceFee = view.findViewById(com.tokopedia.digital_deals.R.id.tv_service_fee_amount);
        tvAmount = view.findViewById(com.tokopedia.digital_deals.R.id.tv_total_amount);
        tvExpiryDate = view.findViewById(com.tokopedia.digital_deals.R.id.tv_expiry_date);
        tvNumberLocations = view.findViewById(com.tokopedia.digital_deals.R.id.tv_no_locations);
        tvNumberVouchers = view.findViewById(com.tokopedia.digital_deals.R.id.tv_number_vouchers);
        etEmailID = view.findViewById(com.tokopedia.digital_deals.R.id.tv_email);
        paymentMethod = view.findViewById(com.tokopedia.digital_deals.R.id.cl_btn_payment);
        setCardViewElevation();
        tvPaymentMethod = view.findViewById(com.tokopedia.digital_deals.R.id.ll_select_payment_method);
        tickerApplyPromo = view.findViewById(com.tokopedia.digital_deals.R.id.ticker_promocode);
        baseMainContent = view.findViewById(com.tokopedia.digital_deals.R.id.base_main_content);
        mainContent = view.findViewById(com.tokopedia.digital_deals.R.id.main_content);
        progressParLayout = view.findViewById(com.tokopedia.digital_deals.R.id.progress_bar_layout);
        clPromoAmount = view.findViewById(com.tokopedia.digital_deals.R.id.cl_promo);
        Drawable img = MethodChecker.getDrawable(getActivity(), com.tokopedia.digital_deals.R.drawable.ic_promo_code);
        ((Ticker) view.findViewById(R.id.ticker_info)).setTextDescription(getString(R.string.ticker_desc));
    }

    private void setCardViewElevation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            paymentMethod.setCardElevation(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8));
        } else {
            paymentMethod.setCardElevation(getResources().getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_0));
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
        dealsAnalytics.sendScreenNameEvent(getScreenName());
        if (dealDetails == null)
            return;

        this.dealDetails = dealDetails;
        quantity = packageViewModel.getSelectedQuantity();
        ImageHandler.loadImage(getContext(), imageViewBrand,
                dealDetails.getImageWeb(),
                com.tokopedia.unifyprinciples.R.color.Unify_N50, com.tokopedia.unifyprinciples.R.color.Unify_N50);

        if (dealDetails.getBrand() != null)
            tvBrandName.setText(dealDetails.getBrand().getTitle());


        tvDealDetails.setText(dealDetails.getDisplayName());
        tvExpiryDate.setText(String.format(getString(com.tokopedia.digital_deals.R.string.valid_through),
                Utils.convertEpochToString(dealDetails.getSaleEndDate())));

        TextView availableLocations = getView().findViewById(com.tokopedia.digital_deals.R.id.tv_available_locations);
        if (dealDetails.getOutlets() == null || dealDetails.getOutlets().isEmpty())
            availableLocations.setText(com.tokopedia.digital_deals.R.string.deals_all_indonesia);
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
            getRootView().findViewById(com.tokopedia.digital_deals.R.id.tv_service_fee).setVisibility(View.GONE);
            getRootView().findViewById(com.tokopedia.digital_deals.R.id.tv_service_fee_amount).setVisibility(View.GONE);
        } else {
            tvServiceFee.setText(Utils.convertToCurrencyString(packageViewModel.getCommission()));

        }

        tvAmount.setText(Utils.convertToCurrencyString(packageViewModel.getSalesPrice() *
                packageViewModel.getSelectedQuantity() +
                packageViewModel.getCommission()));
        tvNumberVouchers.setText(String.format(getActivity().getResources().getString(com.tokopedia.digital_deals.R.string.number_of_vouchers),
                packageViewModel.getSelectedQuantity()));
        if (dealDetails.getOutlets() != null && dealDetails.getOutlets().size() > 0) {
            tvNumberLocations.setText(String.format(getResources().getString(com.tokopedia.digital_deals.R.string.number_of_locations)
                    , dealDetails.getOutlets().size()));
        }
        tickerApplyPromo.enableView();
        tvPaymentMethod.setOnClickListener(this);
        tickerApplyPromo.setOnClickListener(this);
        tvNumberLocations.setOnClickListener(this);
        baseMainContent.setVisibility(View.VISIBLE);
        paymentMethod.setVisibility(View.VISIBLE);

        tickerApplyPromo.setActionListener(new TickerPromoStackingCheckoutView.ActionListener() {
            @Override
            public void onClickUsePromo() {
                dealsAnalytics.sendPromoCodeClickEvent(dealDetails);
                mPresenter.clickGoToPromo(getContext());
            }

            @Override
            public void onResetPromoDiscount() {
                setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "");
                promoApplied = false;
                mPresenter.updatePromoCode("");
            }

            @Override
            public void onClickDetailPromo() {
                if (couponCode != null) {
                    mPresenter.clickGoToDetailPromo(getContext(), couponCode);
                } else if (voucherCode != null) {
                    mPresenter.clickGotToListPromoApplied(getContext(), voucherCode);
                }
            }

            @Override
            public void onDisablePromoDiscount() {
                setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "");
                promoApplied = false;
                mPresenter.updatePromoCode("");
            }
        });
    }

    public void setupPromoTicker(TickerCheckoutView.State state, String title, String description) {
        if (state == TickerCheckoutView.State.EMPTY) {
            tickerApplyPromo.setTitle(title);
            tickerApplyPromo.setState(TickerPromoStackingCheckoutView.State.EMPTY);
        } else  if (state == TickerCheckoutView.State.ACTIVE) {
            tickerApplyPromo.setTitle(title);
            tickerApplyPromo.setState(TickerPromoStackingCheckoutView.State.ACTIVE);
            tickerApplyPromo.setDesc(description);
        }
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
        EditText etPhone = getRootView().findViewById(com.tokopedia.digital_deals.R.id.tv_phone);
        etPhone.setText(phoneNumber);
    }

    @Override
    public View getRootView() {
        return mainContent;
    }

    @Override
    public void showPromoSuccessMessage(String text, String message, long discountAmount, Boolean isCancel) {
        if (isCancel) {
            mPresenter.updatePromoCode("");
            tickerApplyPromo.setState(TickerPromoStackingCheckoutView.State.EMPTY);
            promoApplied = false;
            tickerApplyPromo.setTitle("");
            tickerApplyPromo.setDesc("");
        } else  {
            tickerApplyPromo.setState(TickerPromoStackingCheckoutView.State.ACTIVE);
            tickerApplyPromo.setTitle(text);
            tickerApplyPromo.setDesc(message);
            promoApplied = true;
        }
        if (discountAmount != 0) {
            clPromoAmount.setVisibility(View.VISIBLE);
            TextView view = getRootView().findViewById(com.tokopedia.digital_deals.R.id.tv_promo_discount);
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
    public void showFailureMessage(String error) {
        Utils.getSingletonInstance().showSnackBarDeals(error
                , getContext(), mainContent, false);
    }

    @Override
    protected String getScreenName() {
        return SCREEN_NAME;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallbacks = (CheckoutActivity) activity;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.tokopedia.digital_deals.R.id.ll_select_payment_method) {
            mPresenter.getPaymentLink();
            if (dealDetails.getBrand() != null) {
                dealsAnalytics.sendEcommercePayment(dealDetails.getCategoryId(), dealDetails.getId(), quantity, dealDetails.getSalesPrice(),
                        dealDetails.getDisplayName(), dealDetails.getBrand().getTitle(), promoApplied);
            }
        } else if (v.getId() == com.tokopedia.digital_deals.R.id.tv_no_locations) {
            fragmentCallbacks.replaceFragment(mPresenter.getOutlets(), 0);
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
        if (requestCode == LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar();
            switch (resultCode) {
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.VOUCHER_RESULT_CODE:
                    voucherCode = data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE);
                    mPresenter.updatePromoCode(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE));
                    showPromoSuccessMessage(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_CODE)
                            , data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_MESSAGE)
                            , data.getExtras().getInt(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.VOUCHER_DISCOUNT_AMOUNT)
                            , data.getExtras().getBoolean(IS_CANCEL));
                    break;
                case IRouterConstant.LoyaltyModule.ResultLoyaltyActivity.COUPON_RESULT_CODE:
                    couponCode = data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE);
                    mPresenter.updatePromoCode(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE));
                    showPromoSuccessMessage(data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_CODE)
                            , data.getExtras().getString(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_MESSAGE)
                            , data.getExtras().getInt(IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.COUPON_DISCOUNT_AMOUNT)
                            , data.getExtras().getBoolean(IS_CANCEL));
                    break;
                default:
                    break;
            }
        }
    }
}

