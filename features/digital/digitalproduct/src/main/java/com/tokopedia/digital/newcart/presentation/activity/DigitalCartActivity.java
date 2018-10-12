package com.tokopedia.digital.newcart.presentation.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier;
import com.tokopedia.common_digital.cart.view.model.cart.CartDigitalInfoData;
import com.tokopedia.common_digital.cart.view.model.checkout.CheckoutDataParameter;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData;
import com.tokopedia.digital.R;
import com.tokopedia.digital.applink.DigitalApplinkConstant;
import com.tokopedia.digital.cart.di.DaggerDigitalCartComponent;
import com.tokopedia.digital.cart.di.DigitalCartComponent;
import com.tokopedia.digital.newcart.presentation.contract.DigitalCartContract;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalCartDefaultFragment;
import com.tokopedia.digital.newcart.presentation.presenter.DigitalCartPresenter;
import com.tokopedia.digital.utils.DeviceUtil;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import javax.inject.Inject;

public class DigitalCartActivity extends BaseSimpleActivity implements DigitalCartContract.View {
    private static final String EXTRA_PASS_DIGITAL_CART_DATA = "EXTRA_PASS_DIGITAL_CART_DATA";
    private static final int REQUEST_CODE_OTP = 1001;
    private DigitalCheckoutPassData passData;
    private ProgressBar progressBar;
    private FrameLayout container;
    private CartDigitalInfoData cartDigitalInfoDataState;
    private CheckoutDataParameter.Builder checkoutDataBuilder;

    public static Intent newInstance(Context context, DigitalCheckoutPassData passData) {
        return new Intent(context, DigitalCartActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    public static Intent newInstance(Context context, Bundle bundle) {
        DigitalCheckoutPassData passData = new DigitalCheckoutPassData();
        passData.setAction(bundle.getString(DigitalCheckoutPassData.PARAM_ACTION));
        passData.setCategoryId(bundle.getString(DigitalCheckoutPassData.PARAM_CATEGORY_ID));
        passData.setClientNumber(bundle.getString(DigitalCheckoutPassData.PARAM_CLIENT_NUMBER));
        passData.setOperatorId(bundle.getString(DigitalCheckoutPassData.PARAM_OPERATOR_ID));
        passData.setProductId(bundle.getString(DigitalCheckoutPassData.PARAM_PRODUCT_ID));
        passData.setIsPromo(bundle.getString(DigitalCheckoutPassData.PARAM_IS_PROMO));
        passData.setInstantCheckout(bundle.getString(DigitalCheckoutPassData.PARAM_INSTANT_CHECKOUT));
        passData.setUtmCampaign(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CAMPAIGN));
        passData.setUtmMedium(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_MEDIUM));
        passData.setUtmSource(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_SOURCE));
        passData.setUtmContent(bundle.getString(DigitalCheckoutPassData.PARAM_UTM_CONTENT));
        if (!TextUtils.isEmpty(bundle.getString(DigitalCheckoutPassData.PARAM_IDEM_POTENCY_KEY, ""))) {
            passData.setIdemPotencyKey(
                    bundle.getString(DigitalCheckoutPassData.PARAM_IDEM_POTENCY_KEY)
            );
        } else {
            passData.setIdemPotencyKey(
                    generateATokenRechargeCheckout(context)
            );
        }
        return new Intent(context, DigitalCartActivity.class)
                .putExtra(EXTRA_PASS_DIGITAL_CART_DATA, passData);
    }

    private static String generateATokenRechargeCheckout(Context context) {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        UserSession userSession = new UserSession(context);
        return userSession.getUserId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @DeepLink({DigitalApplinkConstant.DIGITAL_CART})
    public static Intent getCallingApplinksTaskStask(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return DigitalCartActivity.newInstance(context, extras)
                .setData(uri.build())
                .putExtras(extras);
    }


    @Inject
    UserSession userSession;
    @Inject
    DigitalCartPresenter presenter;

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_digital_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();
        initInjector();
        passData = getIntent().getParcelableExtra(EXTRA_PASS_DIGITAL_CART_DATA);
        presenter.attachView(this);
        presenter.onViewCreated();
    }

    private void setupView() {
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.parent_view);
    }

    private void initInjector() {
        DigitalComponent digitalComponent = DaggerDigitalComponent.builder().baseAppComponent(
                ((BaseMainApplication) getApplication()).getBaseAppComponent()).build();
        DigitalCartComponent component = DaggerDigitalCartComponent.builder()
                .digitalComponent(digitalComponent)
                .build();
        component.inject(this);
    }


    @Override
    public void hideContent() {
        container.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public String getIdemPotencyKey() {
        return passData.getIdemPotencyKey();
    }

    @Override
    public String getClientNumber() {
        return passData.getClientNumber();
    }

    @Override
    public boolean isInstantCheckout() {
        return passData.getInstantCheckout().equals("1");
    }

    @Override
    public int getProductId() {
        return Integer.parseInt(passData.getProductId());
    }

    @Override
    public RequestBodyIdentifier getDigitalIdentifierParam() {
        return DeviceUtil.getDigitalIdentifierParam(this);
    }

    @Override
    public void closeViewWithMessageAlert(String message) {
        Intent intent = new Intent();
        intent.putExtra(DigitalRouter.EXTRA_MESSAGE, message);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void setCartDigitalInfo(CartDigitalInfoData cartDigitalInfoData) {
        if (cartDigitalInfoData != null) {
            this.cartDigitalInfoDataState = cartDigitalInfoData;
            buildCheckoutData(cartDigitalInfoData);
        }
    }

    private void buildCheckoutData(CartDigitalInfoData cartDigitalInfoData) {
        checkoutDataBuilder = new CheckoutDataParameter.Builder();
        checkoutDataBuilder.cartId(cartDigitalInfoData.getId());
        checkoutDataBuilder.accessToken(userSession.getAccessToken());
        checkoutDataBuilder.walletRefreshToken("");
        checkoutDataBuilder.ipAddress(DeviceUtil.getLocalIpAddress());
        checkoutDataBuilder.relationId(cartDigitalInfoData.getId());
        checkoutDataBuilder.relationType(cartDigitalInfoData.getType());
        checkoutDataBuilder.transactionAmount(cartDigitalInfoData.getAttributes().getPricePlain());
        checkoutDataBuilder.userAgent(DeviceUtil.getUserAgentForApiCall());
        checkoutDataBuilder.needOtp(cartDigitalInfoData.isNeedOtp());
    }

    @Override
    public void interruptRequestTokenVerification() {
        Intent intent = VerificationActivity.getCallingIntent(this,
                userSession.getPhoneNumber(),
                RequestOtpUseCase.OTP_TYPE_CHECKOUT_DIGITAL,
                true,
                RequestOtpUseCase.MODE_SMS);
        startActivityForResult(intent, REQUEST_CODE_OTP);
    }

    @Override
    public CheckoutDataParameter getCheckoutData() {
        if (cartDigitalInfoDataState.getAttributes().isEnableVoucher() &&
                cartDigitalInfoDataState.getAttributes().getAutoApplyVoucher() != null &&
                cartDigitalInfoDataState.getAttributes().getAutoApplyVoucher().isSuccess()) {
            if (!(cartDigitalInfoDataState.getAttributes().isCouponActive() == 0 && cartDigitalInfoDataState.getAttributes().getAutoApplyVoucher().isCoupon() == 1)) {
                checkoutDataBuilder.voucherCode(cartDigitalInfoDataState.getAttributes().getAutoApplyVoucher().getCode());
            } else {
                checkoutDataBuilder.voucherCode("");
            }
        } else {
            checkoutDataBuilder.voucherCode("");
        }
        return checkoutDataBuilder.build();
    }

    @Override
    public Map<String, String> getGeneratedAuthParamNetwork(String userId, String
            deviceId, Map<String, String> paramGetCart) {
        return AuthUtil.generateParamsNetwork(userId, deviceId, paramGetCart);
    }

    @Override
    public void inflateDefaultCartPage(CartDigitalInfoData cartDigitalInfoData) {
        this.cartDigitalInfoDataState = cartDigitalInfoData;
        inflateFragment(DigitalCartDefaultFragment.newInstance(cartDigitalInfoData, checkoutDataBuilder.build(), passData));
    }

    private void inflateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.parent_view, fragment, getTagFragment())
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OTP) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.processPatchOtpCart(passData.getCategoryId());
            } else {
//                closeView();
            }
        }
    }
}
