package com.tokopedia.digital.product.view.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.product.presentation.model.Operator;
import com.tokopedia.common_digital.product.presentation.model.Product;
import com.tokopedia.common_digital.product.presentation.model.Validation;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.common.analytic.DigitalAnalytics;
import com.tokopedia.digital.common.analytic.DigitalEventTracking;
import com.tokopedia.digital.common.router.DigitalModuleRouter;
import com.tokopedia.digital.common.view.compoundview.ProductPriceInfoView;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.listener.IUssdDigitalView;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.product.view.presenter.UssdProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DigitalUssdFragment extends BaseDaggerFragment
        implements IUssdDigitalView, ProductAdditionalInfoView.ActionListener {

    private static final int REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER = 1001;
    private static final int REQUEST_CODE_LOGIN = 1002;
    private TextView tvBalance;
    private TextView tvPhoneNumber;
    private TextView tvOperatorName;
    private LinearLayout holderChooserProduct;
    private LinearLayout holderAdditionalInfoProduct;
    private LinearLayout holderPriceInfoProduct;
    private TextView btnBuyDigital;
    private CheckBox cbInstantCheckout;
    private TextView tvUnknownNumber;
    private ImageView tooltipInstantCheckout;

    private DigitalProductChooserView digitalProductChooserView;

    private static final String ARG_PARAM_EXTRA_PULSA_BALANCE_DATA = "ARG_PARAM_EXTRA_PULSA_BALANCE_DATA";
    private static final String EXTRA_STATE_OPERATOR_DATA = "EXTRA_STATE_OPERATOR_DATA";
    private static final String EXTRA_STATE_CATEGORY_ID = "EXTRA_STATE_CATEGORY_ID";
    private static final String EXTRA_STATE_CATEGORY_NAME = "EXTRA_STATE_CATEGORY_NAME";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";
    private static final String ARG_PARAM_EXTRA_VALIDATION_LIST_DATA = "ARG_PARAM_EXTRA_VALIDATION_LIST_DATA";
    private static final String ARG_PARAM_EXTRA_SIM_INDEX_DATA = "ARG_PARAM_EXTRA_SIM_INDEX_DATA";
    private static final String EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA = "EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA";

    private PulsaBalance pulsaBalance;
    private Operator selectedOperator;
    private List<Operator> selectedOperatorList;
    private ActionListener actionListener;
    private ProductPriceInfoView productPriceInfoView;
    private ProductAdditionalInfoView productAdditionalInfoView;
    private String mCategoryId;
    private String mCategoryName;
    private Product productSelected;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;
    private List<Validation> validationList;
    private int selectedSimIndex = 0;
    private UssdProductDigitalPresenter presenter;
    private UserSession userSession;
    private DigitalAnalytics digitalAnalytics;
    private DigitalModuleRouter digitalModuleRouter;

    public static DigitalUssdFragment newInstance(PulsaBalance pulsaBalance, Operator selectedOperator,
                                                  List<Validation> validationListData, String categoryId,
                                                  String categoryName, int simPos, List<Operator> selectedOperatorList) {
        DigitalUssdFragment fragment = new DigitalUssdFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA, pulsaBalance);
        args.putParcelable(EXTRA_STATE_OPERATOR_DATA,
                selectedOperator);
        args.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationListData);
        args.putString(EXTRA_STATE_CATEGORY_ID, categoryId);
        args.putString(EXTRA_STATE_CATEGORY_NAME, categoryName);
        args.putInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, simPos);
        args.putParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) selectedOperatorList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);

        state.putParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA, pulsaBalance);
        state.putParcelable(EXTRA_STATE_OPERATOR_DATA, selectedOperator);
        state.putString(EXTRA_STATE_CATEGORY_ID, mCategoryId);
        state.putString(EXTRA_STATE_CATEGORY_NAME, mCategoryName);
        state.putParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA, digitalCheckoutPassDataState);
        state.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationList);
        state.putInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, selectedSimIndex);
        state.putParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA,
                (ArrayList<? extends Parcelable>) selectedOperatorList);
    }

    private void onRestoreState(Bundle savedState) {
        this.pulsaBalance = savedState.getParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA);
        this.selectedOperator = savedState.getParcelable(EXTRA_STATE_OPERATOR_DATA);
        this.mCategoryId = savedState.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName = savedState.getString(EXTRA_STATE_CATEGORY_NAME);
        this.digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);
        this.validationList = savedState.getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);
        this.selectedSimIndex = getArguments().getInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, 0);
        this.selectedOperatorList = savedState.getParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getActivity());
        if (getActivity().getApplicationContext() instanceof AbstractionRouter) {
            digitalAnalytics = new DigitalAnalytics();
        }
        if (getActivity().getApplicationContext() instanceof DigitalModuleRouter) {
            digitalModuleRouter = (DigitalModuleRouter) getActivity().getApplicationContext();
        }

        if (getArguments() != null){
            setupArguments(getArguments());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_digital_ussd, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null){
            onRestoreState(savedInstanceState);
        }
        presenter = new UssdProductDigitalPresenter(this);
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        actionListener = (ActionListener) context;
    }

    private void setupArguments(Bundle arguments) {
        this.pulsaBalance = arguments.getParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA);
        this.selectedOperator = arguments.getParcelable(EXTRA_STATE_OPERATOR_DATA);
        this.mCategoryId = arguments.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName = arguments.getString(EXTRA_STATE_CATEGORY_NAME);
        this.validationList = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);
        this.selectedSimIndex = arguments.getInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, 0);
        this.selectedOperatorList = arguments.getParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA);
    }

    private void initView(View view) {
        tvBalance = view.findViewById(R.id.tv_balance);
        tvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        tvOperatorName = view.findViewById(R.id.tv_operator_name);
        holderChooserProduct = view.findViewById(R.id.holder_chooser_product);
        holderAdditionalInfoProduct = view.findViewById(R.id.holder_additional_info_product);
        holderPriceInfoProduct = view.findViewById(R.id.holder_price_info_product);
        btnBuyDigital = view.findViewById(R.id.btn_buy_digital);
        cbInstantCheckout = view.findViewById(R.id.cb_instant_checkout);
        tvUnknownNumber = view.findViewById(R.id.tv_unknown_number);
        tooltipInstantCheckout = view.findViewById(R.id.tooltip_instant_checkout);

        digitalProductChooserView = new DigitalProductChooserView(getActivity());
        productPriceInfoView = new ProductPriceInfoView(getActivity());
        productAdditionalInfoView = new ProductAdditionalInfoView(getActivity());
        renderOperatorData();

        cbInstantCheckout.setOnCheckedChangeListener(getInstantCheckoutChangeListener());
        enableUnknownNumber();
    }

    private void renderOperatorData() {
        clearHolder(holderChooserProduct);
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(selectedOperator.getProductList(),
                selectedOperator.getDefaultProductId());
        digitalProductChooserView.setLabelText(selectedOperator.getRule().getProductText());
        holderChooserProduct.addView(digitalProductChooserView);
        handleCallBackProductChooser(getDefaultProduct());
        renderPriceInfoProduct();
        renderAdditionalInfoProduct();
        productAdditionalInfoView.setActionListener(this);
        tvOperatorName.setText(selectedOperator.getName());

        tvBalance.setText(pulsaBalance.getPulsaBalance());
        tvPhoneNumber.setText(pulsaBalance.getMobileNumber());
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());
        tvUnknownNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyUssdOperatorDialogFragment(true);
                digitalAnalytics.eventUssd2(DigitalEventTracking.Action.CLICK_USSD_EDIT_NUMBER, selectedOperator.getName() + " - " + pulsaBalance.getPulsaBalance() + " - " + productSelected.getPrice() + " - " + pulsaBalance.getMobileNumber());

            }
        });

        tooltipInstantCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomSheetDialog();
            }
        });
    }

    private void renderAdditionalInfoProduct() {
        clearHolder(holderAdditionalInfoProduct);
        productAdditionalInfoView.renderData(productSelected);
        holderAdditionalInfoProduct.addView(productAdditionalInfoView);
    }

    private void renderPriceInfoProduct() {
        clearHolder(holderPriceInfoProduct);
        productPriceInfoView.renderData(productSelected);
        holderPriceInfoProduct.addView(productPriceInfoView);
    }

    public void showVerifyUssdOperatorDialogFragment(boolean isEdit) {
        OperatorVerificationDialog dialog = OperatorVerificationDialog.newInstance(selectedOperator,
                validationList, selectedSimIndex, isEdit, selectedOperatorList);
        dialog.setTargetFragment(this, OperatorVerificationDialog.REQUEST_CODE_DIGITAL_USSD_OPERATOR_MATCH);
        dialog.show(getFragmentManager(), "Operator_validation");
    }

    @NonNull
    private View.OnClickListener getButtonBuyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = tvPhoneNumber.getText().toString();
                number = DeviceUtil.formatPrefixClientNumber(number);
                if (DeviceUtil.validateNumberAndMatchOperator(validationList, selectedOperator, number)) {
                    pulsaBalance.setMobileNumber(number);
                    presenter.processAddToCartProduct(presenter.generateCheckoutPassData(selectedOperator, pulsaBalance, mCategoryId, mCategoryName, productSelected.getProductId(), cbInstantCheckout.isChecked()));
                } else {
                    showToastMessage(getActivity().getString(R.string.error_message_ussd_operator_not_matched));
                    tvUnknownNumber.setVisibility(View.VISIBLE);
                }
                digitalAnalytics.eventUssd2(DigitalEventTracking.Action.CLICK_USSD_BUY_PULSA, selectedOperator.getName() + " - " + pulsaBalance.getPulsaBalance() + " - " + productSelected.getPrice() + " - " + pulsaBalance.getMobileNumber());
            }
        };
    }

    @NonNull
    private BaseDigitalChooserView.ActionListener<Product> getActionListenerProductChooser() {
        return new BaseDigitalChooserView.ActionListener<Product>() {
            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data) {
                productSelected = data;
            }

            @Override
            public void onUpdateDataDigitalChooserSelectedRendered(Product data, boolean resetClientNumber) {

            }

            @Override
            public void onDigitalChooserClicked(List<Product> data) {
                startActivityForResult(
                        DigitalChooserActivity.newInstanceProductChooser(
                                getActivity(), mCategoryId, selectedOperator.getOperatorId(), "Nominal"
                        ),
                        REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
                );
            }

            @Override
            public void tracking() {

            }
        };
    }

    @NonNull
    protected CompoundButton.OnCheckedChangeListener getInstantCheckoutChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    btnBuyDigital.setText(getActivity().getString(R.string.label_btn_pay_digital));
                else btnBuyDigital.setText(getActivity().getString(R.string.label_btn_buy_digital));

            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Product product = data.getParcelableExtra(DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA);
                    handleCallBackProductChooser(product);
                }
            case REQUEST_CODE_LOGIN:
                if (isUserLoggedIn() && digitalCheckoutPassDataState != null) {
                     presenter.processAddToCartProduct(digitalCheckoutPassDataState);
                }
                break;
            case OperatorVerificationDialog.REQUEST_CODE_DIGITAL_USSD_OPERATOR_MATCH:
                String ussdMobileNumber = data.getStringExtra(OperatorVerificationDialog.ARG_PARAM_EXTRA_RESULT_MOBILE_NUMBER_KEY);
                selectedOperator = data.getParcelableExtra(OperatorVerificationDialog.EXTRA_CALLBACK_OPERATOR_DATA);
                if (ussdMobileNumber != null) {
                    ussdMobileNumber=DeviceUtil.formatPrefixClientNumber(ussdMobileNumber);
                    renderOperatorData();
                    tvPhoneNumber.setText(ussdMobileNumber);
                    tvPhoneNumber.setTextColor(getResources().getColor(R.color.black));
                    presenter.storeUssdPhoneNumber(selectedSimIndex, ussdMobileNumber);

                }
                break;

        }

        if (DigitalRouter.Companion.getREQUEST_CODE_CART_DIGITAL() == requestCode)
        if (data != null && data.hasExtra(DigitalRouter.Companion.getEXTRA_MESSAGE())) {
            String message = data.getStringExtra(DigitalRouter.Companion.getEXTRA_MESSAGE());
            if (!TextUtils.isEmpty(message)) {
                showToastMessage(message);
            }
        }
    }

    @Override
    public String getVersionInfoApplication() {
        return GlobalConfig.VERSION_NAME;
    }

    @Override
    public String getUserLoginId() {
        return userSession.getUserId();
    }

    @Override
    public Application getMainApplication() {
        return getActivity().getApplication();
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
    public void interruptUserNeedLoginOnCheckout(DigitalCheckoutPassData digitalCheckoutPassData) {
        this.digitalCheckoutPassDataState = digitalCheckoutPassData;
        Intent intent = digitalModuleRouter.getLoginIntent
                (getActivity());
        navigateToActivityRequest(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(getActivity());
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

    }

    @Override
    public void hideProgressLoading() {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }


    public Map<String, String> getGeneratedAuthParamNetwork(
            Map<String, String> originParams) {
        TKPDMapParam<String, String> maps = new TKPDMapParam<>();
        maps.putAll(originParams);
        return AuthUtil.generateParamsNetwork(getActivity(), maps, userSession.getUserId(), userSession.getDeviceId());
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public boolean isUserLoggedIn() {
        return userSession.isLoggedIn();
    }

    @Override
    public void showToastMessage(String message) {
        View view = getView();
        if (view != null) NetworkErrorHelper.showSnackbar(getActivity(), message);
        else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void handleCallBackProductChooser(Product product) {
        digitalProductChooserView.renderUpdateDataSelected(product);
        renderAdditionalInfoProduct();
        renderPriceInfoProduct();
    }

    private void clearHolder(LinearLayout holderLayout) {
        if (holderLayout.getChildCount() > 0) {
            holderLayout.removeAllViews();
        }
    }

    private Product getDefaultProduct() {
        for (Product product : selectedOperator.getProductList()) {
            if (product.getStatus() != Product.Companion.getSTATUS_INACTIVE()) {
                productSelected = product;
                break;
            }
        }
        return productSelected;
    }

    public void enableUnknownNumber() {

        if (pulsaBalance.getMobileNumber() == null || "".equalsIgnoreCase(pulsaBalance.getMobileNumber().trim())) {
            tvUnknownNumber.setVisibility(View.VISIBLE);
            tvPhoneNumber.setTextColor(getResources().getColor(R.color.green_800));
            tvPhoneNumber.setText(getResources().getString(R.string.label_ussd_unknown));
            showVerifyUssdOperatorDialogFragment(false);
        } else if (!DeviceUtil.validateNumberAndMatchOperator(validationList, selectedOperator, pulsaBalance.getMobileNumber())) {
            tvUnknownNumber.setVisibility(View.VISIBLE);
        } else if (presenter.getUssdPhoneNumberFromCache(selectedSimIndex) != null) {
            tvUnknownNumber.setVisibility(View.VISIBLE);
        } else {
            tvUnknownNumber.setVisibility(View.GONE);
        }
    }


    @Override
    public void onProductLinkClicked(String url) {
        digitalModuleRouter.getWebviewActivityWithIntent(getActivity(), url);
    }

    private void setBottomSheetDialog() {
        BottomSheetView bottomSheetView;
        bottomSheetView = new BottomSheetView(getActivity());
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(getString(R.string.title_tooltip_instan_payment))
                .setBody(getString(R.string.body_tooltip_instan_payment))
                .setImg(R.drawable.ic_digital_instant_payment)
                .build());
        bottomSheetView.show();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public interface ActionListener {
        void updateTitleToolbar(String title);
    }

}
