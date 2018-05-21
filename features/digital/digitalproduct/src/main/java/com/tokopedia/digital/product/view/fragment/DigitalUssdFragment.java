package com.tokopedia.digital.product.view.fragment;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.common.view.compoundview.ProductPriceInfoView;
import com.tokopedia.digital.product.view.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.view.activity.DigitalWebActivity;
import com.tokopedia.digital.product.view.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.view.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.view.compoundview.ProductAdditionalInfoView;
import com.tokopedia.digital.product.view.listener.IUssdDigitalView;
import com.tokopedia.digital.product.view.model.Operator;
import com.tokopedia.digital.product.view.model.Product;
import com.tokopedia.digital.product.view.model.PulsaBalance;
import com.tokopedia.digital.product.view.model.Validation;
import com.tokopedia.digital.product.view.presenter.IUssdProductDigitalPresenter;
import com.tokopedia.digital.product.view.presenter.UssdProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DigitalUssdFragment.ActionListener} interface
 * to handle interaction events.
 * Use the {@link DigitalUssdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DigitalUssdFragment extends BasePresenterFragment<IUssdProductDigitalPresenter>
        implements IUssdDigitalView, ProductAdditionalInfoView.ActionListener {


    @BindView(R2.id.tv_balance)
    TextView tvBalance;
    @BindView(R2.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R2.id.tv_operator_name)
    TextView tv_operator_name;
    @BindView(R2.id.holder_chooser_product)
    LinearLayout holderChooserProduct;
    @BindView(R2.id.holder_additional_info_product)
    LinearLayout holderAdditionalInfoProduct;
    @BindView(R2.id.holder_price_info_product)
    LinearLayout holderPriceInfoProduct;
    @BindView(R2.id.btn_buy_digital)
    TextView btnBuyDigital;
    @BindView(R2.id.cb_instant_checkout)
    CheckBox cbInstantCheckout;
    @BindView(R2.id.tv_unknown_number)
    TextView tvUnknownNumber;
    @BindView(R2.id.tooltip_instant_checkout)
    ImageView tooltipInstantCheckout;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DigitalUssdFragment.
     */
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
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {
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

    @Override
    public void onRestoreState(Bundle savedState) {
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
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

        presenter = new UssdProductDigitalPresenter(this);
    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.pulsaBalance = arguments.getParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA);
        this.selectedOperator = arguments.getParcelable(EXTRA_STATE_OPERATOR_DATA);
        this.mCategoryId = arguments.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName = arguments.getString(EXTRA_STATE_CATEGORY_NAME);
        this.validationList = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);
        this.selectedSimIndex = arguments.getInt(ARG_PARAM_EXTRA_SIM_INDEX_DATA, 0);
        this.selectedOperatorList = arguments.getParcelableArrayList(EXTRA_STATE_SELECTED_OPERATOR_LIST_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_ussd;
    }

    @Override
    protected void initView(View view) {
        digitalProductChooserView = new DigitalProductChooserView(context);
        productPriceInfoView = new ProductPriceInfoView(context);
        productAdditionalInfoView = new ProductAdditionalInfoView(context);
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
        tv_operator_name.setText(selectedOperator.getName());

        tvBalance.setText(pulsaBalance.getPulsaBalance());
        tvPhoneNumber.setText(pulsaBalance.getMobileNumber());
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());
        tvUnknownNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyUssdOperatorDialogFragment(true);
                UnifyTracking.eventUssd(AppEventTracking.Action.CLICK_USSD_EDIT_NUMBER, selectedOperator.getName() + " - " + pulsaBalance.getPulsaBalance() + " - " + productSelected.getPrice() + " - " + pulsaBalance.getMobileNumber());

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
                UnifyTracking.eventUssd(AppEventTracking.Action.CLICK_USSD_BUY_PULSA, selectedOperator.getName() + " - " + pulsaBalance.getPulsaBalance() + " - " + productSelected.getPrice() + " - " + pulsaBalance.getMobileNumber());
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
                        IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
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
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Product product = data.getParcelableExtra(DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA);
                    handleCallBackProductChooser(product);
                }
            case IDigitalModuleRouter.REQUEST_CODE_LOGIN:
                if (isUserLoggedIn() && digitalCheckoutPassDataState != null) {
                    // presenter.processAddToCartProduct(digitalCheckoutPassDataState);
                }
            case IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL:
                if (data != null && data.hasExtra(IDigitalModuleRouter.EXTRA_MESSAGE)) {
                    String message = data.getStringExtra(IDigitalModuleRouter.EXTRA_MESSAGE);
                    if (!TextUtils.isEmpty(message)) {
                        showToastMessage(message);
                    }
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
    }

    @Override
    public String getVersionInfoApplication() {
        return VersionInfo.getVersionInfo(getActivity());
    }

    @Override
    public String getUserLoginId() {
        return SessionHandler.getLoginID(getActivity());
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
        Intent intent = ((IDigitalModuleRouter) MainApplication.getAppContext()).getLoginIntent
                (getActivity());
        navigateToActivityRequest(intent, IDigitalModuleRouter.REQUEST_CODE_LOGIN);
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
    public void showDialog(Dialog dialog) {
        if (!dialog.isShowing()) dialog.show();
    }

    @Override
    public void dismissDialog(Dialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz) {

    }

    @Override
    public String getStringFromResource(@StringRes int resId) {
        return getString(resId);
    }

    @Override
    public TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
            TKPDMapParam<String, String> originParams) {
        return AuthUtil.generateParamsNetwork(getActivity(), originParams);
    }

    @Override
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public boolean isUserLoggedIn() {
        return SessionHandler.isV4Login(getActivity());
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
            if (product.getStatus() != Product.STATUS_INACTIVE) {
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
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onProductLinkClicked(String url) {
        startActivity(DigitalWebActivity.newInstance(getActivity(), url));
    }

    public interface ActionListener {
        void updateTitleToolbar(String title);
    }

    private void setBottomSheetDialog() {
        BottomSheetView bottomSheetView;
        bottomSheetView = new BottomSheetView(context);
        bottomSheetView.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(context.getString(R.string.title_tooltip_instan_payment))
                .setBody(context.getString(R.string.body_tooltip_instan_payment))
                .setImg(R.drawable.ic_digital_instant_payment)
                .build());
        bottomSheetView.show();
    }
}
