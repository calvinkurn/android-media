package com.tokopedia.loyalty.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.constant.IRouterConstant;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.DaggerPromoCodeComponent;
import com.tokopedia.loyalty.di.component.PromoCodeComponent;
import com.tokopedia.loyalty.di.module.PromoCodeViewModule;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.data.VoucherViewModel;
import com.tokopedia.loyalty.view.presenter.IPromoCodePresenter;
import com.tokopedia.loyalty.view.view.IPromoCodeView;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 24/11/17.
 */

public class PromoCodeFragment extends BaseDaggerFragment implements IPromoCodeView {

    @Inject
    IPromoCodePresenter dPresenter;
    @Inject
    LoyaltyModuleRouter loyaltyModuleRouter;

    private ManualInsertCodeListener listener;

    private ProgressDialog progressDialog;

    private TextInputLayout voucherCodeFieldHolder;

    private static final String PLATFORM_KEY = "PLATFORM_KEY";

    private static final String PLATFORM_PAGE_KEY = "PLATFORM_PAGE_KEY";

    private static final String CATEGORY_KEY = "CATEGORY_KEY";

    private static final String ADDITIONAL_DATA_KEY = "ADDITIONAL_DATA_KEY";

    private static final String CART_ID = "CART_ID";

    private static final String TRAIN_RESERVATION_ID = "TRAIN_RESERVATION_ID";
    private static final String TRAIN_RESERVATION_CODE = "TRAIN_RESERVATION_CODE";

    private static final String CHECKOUT = "checkoutdata";

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_code, container, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        voucherCodeFieldHolder = view.findViewById(R.id.til_et_voucher_code);
        final EditText voucherCodeField = view.findViewById(R.id.et_voucher_code);
        TextView submitVoucherButton = view.findViewById(R.id.btn_check_voucher);

        if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DIGITAL_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitDigitalVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.MARKETPLACE_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitMarketPlaceCartListVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.FLIGHT_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitFlightVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.TRAIN_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitTrainVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        } else if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.EVENT_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitEventVoucher(voucherCodeField,
                    voucherCodeFieldHolder));

        } else if (getArguments().getString(PLATFORM_KEY, "").equalsIgnoreCase(
                IRouterConstant.LoyaltyModule.ExtraLoyaltyActivity.DEALS_STRING)) {
            submitVoucherButton.setOnClickListener(onSubmitDealVoucher(voucherCodeField,
                    voucherCodeFieldHolder));
        } else {
            submitVoucherButton.setOnClickListener(onSubmitMarketplaceVoucher(
                    voucherCodeField,
                    voucherCodeFieldHolder)
            );
        }

    }

    private View.OnClickListener onSubmitFlightVoucher(final EditText voucherCodeField, final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else
                    dPresenter.processCheckFlightPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString(),
                            getArguments().getString(CART_ID)
                    );
            }
        };
    }

    private View.OnClickListener onSubmitTrainVoucher(final EditText voucherCodeField, final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof LoyaltyModuleRouter) {
                    ((LoyaltyModuleRouter) getActivity())
                            .trainSendTrackingOnClickUseVoucherCode(voucherCodeField.getText().toString());
                }
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else
                    dPresenter.processCheckTrainPromoCode(
                            getActivity(),
                            getArguments().getString(TRAIN_RESERVATION_ID),
                            getArguments().getString(TRAIN_RESERVATION_CODE),
                            voucherCodeField.getText().toString()
                    );
            }
        };
    }

    private View.OnClickListener onSubmitMarketplaceVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else
                    dPresenter.processCheckPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString());
            }
        };
    }

    private View.OnClickListener onSubmitDigitalVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    dPresenter.processCheckDigitalPromoCode(
                            getActivity(),
                            voucherCodeField.getText().toString(),
                            getArguments().getString(CATEGORY_KEY));
                }
            }
        };
    }


    private View.OnClickListener onSubmitMarketPlaceCartListVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return view -> {
            voucherCodeFieldHolder.setError(null);
            if (voucherCodeField.getText().toString().isEmpty()) {
                textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
            } else {
                dPresenter.processCheckMarketPlaceCartListPromoCode(
                        getActivity(),
                        voucherCodeField.getText().toString(), getArguments().getString(ADDITIONAL_DATA_KEY, ""));
            }
            listener.onUsePromoCodeClicked();
        };
    }

    private View.OnClickListener onSubmitEventVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    String jsonbody = getActivity().getIntent().getStringExtra(CHECKOUT);
                    JsonObject requestBody = null;
                    if (jsonbody != null || jsonbody.length() > 0) {
                        JsonElement jsonElement = new JsonParser().parse(jsonbody);
                        requestBody = jsonElement.getAsJsonObject();
                        dPresenter.processCheckEventPromoCode(
                                voucherCodeField.getText().toString(),
                                requestBody,
                                false);
                    }

                }
            }
        };
    }

    private View.OnClickListener onSubmitDealVoucher(
            final EditText voucherCodeField,
            final TextInputLayout textHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voucherCodeFieldHolder.setError(null);
                if (voucherCodeField.getText().toString().isEmpty()) {
                    textHolder.setError(getActivity().getString(R.string.error_empty_voucher_code));
                } else {
                    String jsonbody = getActivity().getIntent().getStringExtra(CHECKOUT);
                    JsonObject requestBody = null;
                    if (jsonbody != null || jsonbody.length() > 0) {
                        JsonElement jsonElement = new JsonParser().parse(jsonbody);
                        requestBody = jsonElement.getAsJsonObject();
                        dPresenter.processCheckDealPromoCode(
                                voucherCodeField.getText().toString(),
                                requestBody,
                                false);
                    }

                }
            }
        };
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void initInjector() {
        PromoCodeComponent promoCodeComponent = DaggerPromoCodeComponent.builder()
                .baseAppComponent((BaseAppComponent) getComponent(BaseAppComponent.class))
                .promoCodeViewModule(new PromoCodeViewModule(this))
                .build();
        promoCodeComponent.inject(this);
    }

    public static Fragment newInstance(String platform, String categoryKey) {
        PromoCodeFragment fragment = new PromoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(CATEGORY_KEY, categoryKey);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Fragment newInstance(String platform,String platformPage, String categoryKey,
                                       String cartId, String additionalDataString,
                                       String trainReservationId, String trainReservartionCode) {
        PromoCodeFragment fragment = new PromoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PLATFORM_KEY, platform);
        bundle.putString(PLATFORM_PAGE_KEY, platformPage);
        bundle.putString(CATEGORY_KEY, categoryKey);
        bundle.putString(CART_ID, cartId);
        bundle.putString(ADDITIONAL_DATA_KEY, additionalDataString);
        bundle.putString(TRAIN_RESERVATION_ID, trainReservationId);
        bundle.putString(TRAIN_RESERVATION_CODE, trainReservartionCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void checkVoucherSuccessfull(VoucherViewModel voucher) {
        listener.onCodeSuccess(voucher.getCode(), voucher.getMessage(), voucher.getAmount());
    }

    @Override
    public void checkDigitalVoucherSucessful(VoucherViewModel voucher) {
        listener.onDigitalCodeSuccess(
                voucher.getCode(),
                voucher.getMessage(),
                voucher.getRawDiscount(),
                voucher.getRawCashback());
    }

    @Override
    public void onGetGeneralError(String errorMessage) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onPromoCodeError(String errorMessage) {
        voucherCodeFieldHolder.setError(errorMessage);
        listener.sendAnalyticsOnErrorGetPromoCode(errorMessage);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void sendTrackingOnCheckTrainVoucherError(String errorMessage) {
        if (getActivity() instanceof LoyaltyModuleRouter) {
            ((LoyaltyModuleRouter) getActivity())
                    .trainSendTrackingOnCheckVoucherCodeError(errorMessage);
        }
    }

    @Override
    public void navigateToActivityRequest(Intent intent, int requestCode) {

    }

    @Override
    public void navigateToActivity(Intent intent) {

    }

    @Override
    public void showInitialProgressLoading() {

    }

    @Override
    public void hideInitialProgressLoading() {

    }

    @Override
    public void clearContentRendered() {

    }

    @Override
    public void showProgressLoading() {
        progressDialog.show();
    }

    @Override
    public void hideProgressLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showDialog(Dialog dialog) {

    }

    @Override
    public void dismissDialog(Dialog dialog) {

    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(TKPDMapParam<String, String> originParams) {
        return null;
    }

    @Override
    public void closeView() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ManualInsertCodeListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ManualInsertCodeListener) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            listener.sendAnalyticsScreenNamePromoCode();
        }
    }

    @Override
    public void sendEventDigitalEventTracking(Context context,String text, String failmsg) {
        loyaltyModuleRouter.sendEventDigitalEventTracking(context,text, failmsg);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            listener.sendAnalyticsScreenNamePromoCode();
        }
    }

    public interface ManualInsertCodeListener {

        void onCodeSuccess(String voucherCode, String voucherMessage, String voucherAmount);

        void onDigitalCodeSuccess(String voucherCode, String voucherMessage, long discountAmount, long cashBackAmount);

        void onUsePromoCodeClicked();

        void sendAnalyticsScreenNamePromoCode();

        void sendAnalyticsOnErrorGetPromoCode(String errorMessage);

    }
}
