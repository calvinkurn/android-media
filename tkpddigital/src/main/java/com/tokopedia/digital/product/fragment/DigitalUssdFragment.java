package com.tokopedia.digital.product.fragment;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.VersionInfo;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.product.activity.DigitalChooserActivity;
import com.tokopedia.digital.product.compoundview.BaseDigitalChooserView;
import com.tokopedia.digital.product.compoundview.DigitalProductChooserView;
import com.tokopedia.digital.product.compoundview.ProductPriceInfoView;
import com.tokopedia.digital.product.listener.IUssdDigitalView;
import com.tokopedia.digital.product.model.Operator;
import com.tokopedia.digital.product.model.Product;
import com.tokopedia.digital.product.model.PulsaBalance;
import com.tokopedia.digital.product.model.Validation;
import com.tokopedia.digital.product.presenter.IUssdProductDigitalPresenter;
import com.tokopedia.digital.product.presenter.UssdProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DigitalUssdFragment.ActionListener} interface
 * to handle interaction events.
 * Use the {@link DigitalUssdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DigitalUssdFragment extends BasePresenterFragment<IUssdProductDigitalPresenter> implements IUssdDigitalView {


    @BindView(R2.id.tv_balance)
    TextView tvBalance;
    //    @BindView(R2.id.tv_phone_number)
//    TextView tvPhoneNumber;
    @BindView(R2.id.tv_operator_name)
    TextView tv_operator_name;
    @BindView(R2.id.holder_chooser_product)
    LinearLayout holderChooserProduct;
    @BindView(R2.id.holder_price_info_product)
    LinearLayout holderPriceInfoProduct;
    @BindView(R2.id.btn_buy_digital)
    TextView btnBuyDigital;
    @BindView(R2.id.cb_instant_checkout)
    CheckBox cbInstantCheckout;

    @BindView(R2.id.ac_number)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R2.id.btn_clear_number)
    ImageView btnClear;
    @BindView(R2.id.iv_pic_operator)
    ImageView imgOperator;
    @BindView(R2.id.tv_error_number)
    TextView tvErrorNumber;


    private DigitalProductChooserView digitalProductChooserView;
    private static final String ARG_PARAM_EXTRA_PULSA_BALANCE_DATA =
            "ARG_PARAM_EXTRA_PULSA_BALANCE_DATA";
    private static final String EXTRA_STATE_OPERATOR_DATA =
            "EXTRA_STATE_OPERATOR_DATA";

    private static final String EXTRA_STATE_CATEGORY_ID = "EXTRA_STATE_CATEGORY_ID";
    private static final String EXTRA_STATE_CATEGORY_NAME = "EXTRA_STATE_CATEGORY_NAME";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";
    private static final String ARG_PARAM_EXTRA_VALIDATION_LIST_DATA =
            "ARG_PARAM_EXTRA_VALIDATION_LIST_DATA";

    private PulsaBalance pulsaBalance;
    private Operator selectedOperator;
    private ActionListener actionListener;
    private ProductPriceInfoView productPriceInfoView;
    private String mCategoryId;
    private String mCategoryName;
    private Product productSelected;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;
    private List<Validation> validationList;
    private boolean isOperaorVerified = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DigitalUssdFragment.
     */
    public static DigitalUssdFragment newInstance(PulsaBalance pulsaBalance, Operator selectedOperator, List<Validation> validationListData, String categoryId, String categoryName) {
        DigitalUssdFragment fragment = new DigitalUssdFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA, pulsaBalance);
        args.putParcelable(EXTRA_STATE_OPERATOR_DATA,
                selectedOperator);
        args.putParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA,
                (ArrayList<? extends Parcelable>) validationListData);
        args.putString(EXTRA_STATE_CATEGORY_ID, categoryId);
        args.putString(EXTRA_STATE_CATEGORY_NAME, categoryName);
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

    }

    @Override
    public void onRestoreState(Bundle savedState) {
        this.pulsaBalance = savedState.getParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA);
        this.selectedOperator = savedState.getParcelable(EXTRA_STATE_OPERATOR_DATA);
        this.mCategoryId = savedState.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName = savedState.getString(EXTRA_STATE_CATEGORY_NAME);
        this.digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);
        this.validationList = savedState.getParcelableArrayList(ARG_PARAM_EXTRA_VALIDATION_LIST_DATA);

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


    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_ussd;
    }

    @Override
    protected void initView(View view) {
        digitalProductChooserView = new DigitalProductChooserView(context);
        productPriceInfoView = new ProductPriceInfoView(context);

        clearHolder(holderChooserProduct);
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        digitalProductChooserView.renderInitDataList(selectedOperator.getProductList());
        digitalProductChooserView.setLabelText(selectedOperator.getRule().getProductText());
        holderChooserProduct.addView(digitalProductChooserView);

        for (Product product : selectedOperator.getProductList()) {
            if (product.getStatus() != Product.STATUS_INACTIVE) {
                productSelected = product;
                break;
            }
        }
        handleCallBackProductChooser(productSelected);

        tvBalance.setText(pulsaBalance.getPulsaBalance());
        //tvPhoneNumber.setText(pulsaBalance.getMobileNumber());
        tv_operator_name.setText(selectedOperator.getName());
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());

        final TextWatcher textWatcher = getTextWatcherInput();
        autoCompleteTextView.removeTextChangedListener(textWatcher);
        autoCompleteTextView.addTextChangedListener(textWatcher);
        this.btnClear.setOnClickListener(getButtonClearClickListener());
        autoCompleteTextView.setText(pulsaBalance.getMobileNumber());
    }

    @NonNull
    private TextWatcher getTextWatcherInput() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                isOperaorVerified = false;
                String tempInput = charSequence.toString();
                btnClear.setVisibility(tempInput.length() > 0 ? VISIBLE : GONE);
                pulsaBalance.setMobileNumber(tempInput);
                if (tempInput.length() < 4) {
                    imgOperator.setVisibility(GONE);

                }
                String tempInputTrim = tempInput;
                if (tempInput.startsWith("+62")) {
                    tempInputTrim = tempInput.replace("+62", "0");
                } else if (tempInput.startsWith("62")) {
                    tempInputTrim = tempInput.replace("62", "0");
                }
                if (tempInput.isEmpty()) {

                    tvErrorNumber.setText("");
                    tvErrorNumber.setVisibility(GONE);
                } else {
                    String errorString = null;
                    for (Validation validation : validationList) {
                        if (!Pattern.matches(validation.getRegex(), tempInput)) {
                            errorString = validation.getError();
                            break;
                        } else {
                            errorString = null;
                        }
                    }

                    if (errorString == null) {
                        tvErrorNumber.setText("");
                        tvErrorNumber.setVisibility(GONE);
                        if(matchOperator(tempInputTrim)){
                            isOperaorVerified = true;
                        }else{
                            tvErrorNumber.setText("Operator doesn't match");
                            tvErrorNumber.setVisibility(VISIBLE);
                        }

                    } else {
                        if (tempInput.isEmpty()) {
                            tvErrorNumber.setText("");
                            tvErrorNumber.setVisibility(GONE);

                        } else {
                            tvErrorNumber.setText(errorString);
                            tvErrorNumber.setVisibility(VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private boolean matchOperator(String tempInputTrim) {
        for (String prefix : selectedOperator.getPrefixList()) {
            if (tempInputTrim.startsWith(prefix)) {
                enableImageOperator(selectedOperator.getImage());
                return true;
            }
        }
        return false;
    }

    @NonNull
    private View.OnClickListener getButtonBuyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOperaorVerified) {
                    pulsaBalance.setMobileNumber(autoCompleteTextView.getText().toString());
                    presenter.processAddToCartProduct(presenter.generateCheckoutPassData(selectedOperator, pulsaBalance, mCategoryId, mCategoryName, productSelected.getProductId(), cbInstantCheckout.isChecked()));
                    UnifyTracking.eventUssd(AppEventTracking.Action.CLICK_USSD_BUY_PULSA, selectedOperator.getName() + "," + pulsaBalance.getMobileNumber());
                } else {
                    showToastMessage("Operator does not match.");
                }
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
            public void onDigitalChooserClicked(List<Product> data) {
                startActivityForResult(
                        DigitalChooserActivity.newInstanceProductChooser(
                                getActivity(), selectedOperator.getProductList(), "Nominal"
                        ),
                        IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER
                );
            }
        };
    }

    @NonNull
    private View.OnClickListener getButtonClearClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInputTyped();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IDigitalModuleRouter.REQUEST_CODE_DIGITAL_PRODUCT_CHOOSER:
                if (resultCode == Activity.RESULT_OK && data != null)
                    handleCallBackProductChooser(
                            (Product) data.getParcelableExtra(
                                    DigitalChooserActivity.EXTRA_CALLBACK_PRODUCT_DATA
                            )
                    );
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

        }
    }

    public void resetInputTyped() {
        autoCompleteTextView.setText("");
        imgOperator.setVisibility(View.GONE);
        btnClear.setVisibility(View.GONE);
    }

    public void enableImageOperator(String imageUrl) {
       // imgOperator.setVisibility(VISIBLE);
       // Glide.with(getActivity()).load(imageUrl).dontAnimate().into(this.imgOperator);

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
        Intent intent = SessionRouter.getLoginActivityIntent(getActivity());
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
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
        renderPriceInfoProduct();
    }

    private void renderPriceInfoProduct() {
        clearHolder(holderPriceInfoProduct);
        productPriceInfoView.renderData(productSelected);
        holderPriceInfoProduct.addView(productPriceInfoView);

    }

    private void clearHolder(LinearLayout holderLayout) {
        if (holderLayout.getChildCount() > 0) {
            holderLayout.removeAllViews();
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

    public interface ActionListener {
        void updateTitleToolbar(String title);

    }
}
