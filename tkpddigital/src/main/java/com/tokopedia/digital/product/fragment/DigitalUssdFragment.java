package com.tokopedia.digital.product.fragment;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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
import com.tokopedia.digital.product.presenter.IUssdProductDigitalPresenter;
import com.tokopedia.digital.product.presenter.UssdProductDigitalPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.digital.utils.data.RequestBodyIdentifier;

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
public class DigitalUssdFragment extends BasePresenterFragment<IUssdProductDigitalPresenter> implements IUssdDigitalView {


    @BindView(R2.id.tv_balance)
    TextView tvBalance;
    @BindView(R2.id.tv_phone_number)
    TextView tvPhoneNumber;
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

    private DigitalProductChooserView digitalProductChooserView;
    private static final String ARG_PARAM_EXTRA_PULSA_BALANCE_DATA =
            "ARG_PARAM_EXTRA_PULSA_BALANCE_DATA";
    private static final String EXTRA_STATE_OPERATOR_DATA =
            "EXTRA_STATE_OPERATOR_DATA";

    private static final String EXTRA_STATE_CATEGORY_ID = "EXTRA_STATE_CATEGORY_ID";
    private static final String EXTRA_STATE_CATEGORY_NAME = "EXTRA_STATE_CATEGORY_NAME";
    private static final String EXTRA_STATE_CHECKOUT_PASS_DATA = "EXTRA_STATE_CHECKOUT_PASS_DATA";

    private PulsaBalance pulsaBalance;
    private Operator selectedOperator;
    private ActionListener actionListener;
    private ProductPriceInfoView productPriceInfoView;
    private String mCategoryId;
    private String mCategoryName;
    private Product productSelected;
    private DigitalCheckoutPassData digitalCheckoutPassDataState;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DigitalUssdFragment.
     */
    public static DigitalUssdFragment newInstance(PulsaBalance pulsaBalance, Operator selectedOperator, String categoryId, String categoryName) {
        DigitalUssdFragment fragment = new DigitalUssdFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA, pulsaBalance);
        args.putParcelable(EXTRA_STATE_OPERATOR_DATA,
                selectedOperator);
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

    }

    @Override
    public void onRestoreState(Bundle savedState) {
        this.pulsaBalance = savedState.getParcelable(ARG_PARAM_EXTRA_PULSA_BALANCE_DATA);
        this.selectedOperator = savedState.getParcelable(EXTRA_STATE_OPERATOR_DATA);
        this.mCategoryId = savedState.getString(EXTRA_STATE_CATEGORY_ID);
        this.mCategoryName = savedState.getString(EXTRA_STATE_CATEGORY_NAME);
        this.digitalCheckoutPassDataState = savedState.getParcelable(EXTRA_STATE_CHECKOUT_PASS_DATA);

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

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_digital_ussd;
    }

    @Override
    protected void initView(View view) {
        digitalProductChooserView = new DigitalProductChooserView(context);
        clearHolder(holderChooserProduct);
        holderChooserProduct.addView(digitalProductChooserView);
        digitalProductChooserView.renderInitDataList(selectedOperator.getProductList());
        digitalProductChooserView.setActionListener(getActionListenerProductChooser());
        productPriceInfoView = new ProductPriceInfoView(context);
        tvBalance.setText(pulsaBalance.getPulsaBalance());
        tvPhoneNumber.setText(pulsaBalance.getMobileNumber());
        tv_operator_name.setText(selectedOperator.getName());
        btnBuyDigital.setOnClickListener(getButtonBuyClickListener());
        productSelected = selectedOperator.getProductList().get(0);
        digitalProductChooserView.setLabelText(selectedOperator.getRule().getProductText());
        handleCallBackProductChooser(productSelected);
    }

    @NonNull
    private View.OnClickListener getButtonBuyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.processAddToCartProduct(presenter.generateCheckoutPassData(selectedOperator, pulsaBalance, mCategoryId, mCategoryName, productSelected.getProductId(), cbInstantCheckout.isChecked()));
                UnifyTracking.eventUssd(AppEventTracking.Action.CLICK_USSD_BUY_PULSA,selectedOperator.getName()+","+pulsaBalance.getMobileNumber());
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
