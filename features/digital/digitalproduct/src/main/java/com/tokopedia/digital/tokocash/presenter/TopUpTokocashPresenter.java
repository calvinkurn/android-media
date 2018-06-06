package com.tokopedia.digital.tokocash.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ServerErrorException;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.ServerErrorHandler;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.digitalmodule.passdata.DigitalCheckoutPassData;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.digital.common.domain.interactor.GetCategoryByIdUseCase;
import com.tokopedia.digital.common.view.compoundview.BaseDigitalProductView;
import com.tokopedia.digital.product.view.model.ProductDigitalData;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;
import com.tokopedia.digital.tokocash.interactor.ITokoCashBalanceInteractor;
import com.tokopedia.digital.tokocash.listener.TopUpTokoCashListener;
import com.tokopedia.digital.tokocash.model.WalletToken;
import com.tokopedia.digital.tokocash.model.tokocashitem.TokoCashBalanceData;
import com.tokopedia.digital.utils.ServerErrorHandlerUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 8/21/17.
 */

public class TopUpTokocashPresenter implements ITopUpTokocashPresenter {

    private final static String TOPUP_CATEGORY_ID = "103";
    private SessionHandler sessionHandler;
    private Context context;

    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final ITokoCashBalanceInteractor balanceInteractor;
    private final TopUpTokoCashListener view;

    public TopUpTokocashPresenter(Context context,
                                  GetCategoryByIdUseCase getCategoryByIdUseCase,
                                  ITokoCashBalanceInteractor balanceInteractor,
                                  TopUpTokoCashListener view) {
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.balanceInteractor = balanceInteractor;
        this.view = view;
        this.context = context;
        sessionHandler = new SessionHandler(context);
    }

    @Override
    public void processGetCategoryTopUp() {
        getCategoryByIdUseCase.execute(
                getCategoryByIdUseCase.createRequestParam(TOPUP_CATEGORY_ID),
                getSubscriberProductDigitalData()
        );
    }

    private Subscriber<ProductDigitalData> getSubscriberProductDigitalData() {
        return new Subscriber<ProductDigitalData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                view.hideProgressLoading();
            }

            @Override
            public void onNext(ProductDigitalData productDigitalData) {
                view.hideProgressLoading();
                view.renderTopUpDataTokoCash(productDigitalData.getCategoryData());
            }
        };
    }

    @Override
    public void processAddToCartProduct(BaseDigitalProductView.PreCheckoutProduct preCheckoutProduct) {
        DigitalCheckoutPassData digitalCheckoutPassData = new DigitalCheckoutPassData.Builder()
                .action(DigitalCheckoutPassData.DEFAULT_ACTION)
                .categoryId(preCheckoutProduct.getCategoryId())
                .clientNumber("")
                .instantCheckout(preCheckoutProduct.isInstantCheckout() ? "1" : "0")
                .isPromo(preCheckoutProduct.isPromo() ? "1" : "0")
                .operatorId(preCheckoutProduct.getOperatorId())
                .productId(preCheckoutProduct.getProductId())
                .utmCampaign((preCheckoutProduct.getCategoryName()))
                .utmContent(view.getVersionInfoApplication())
                .idemPotencyKey(generateATokenRechargeCheckout())
                .utmSource(DigitalCheckoutPassData.UTM_SOURCE_ANDROID)
                .utmMedium(DigitalCheckoutPassData.UTM_MEDIUM_WIDGET)
                .voucherCodeCopied(preCheckoutProduct.getVoucherCodeCopied())
                .build();

        if (view.getMainApplication() instanceof IDigitalModuleRouter) {
            IDigitalModuleRouter digitalModuleRouter =
                    (IDigitalModuleRouter) view.getMainApplication();
            view.navigateToActivityRequest(
                    digitalModuleRouter.instanceIntentCartDigitalProduct(digitalCheckoutPassData),
                    IDigitalModuleRouter.REQUEST_CODE_CART_DIGITAL
            );
        }
    }

    private String generateATokenRechargeCheckout() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String token = AuthUtil.md5(timeMillis);
        return view.getUserLoginId() + "_" + (token.isEmpty() ? timeMillis : token);
    }

    @Override
    public void processGetBalanceTokoCash() {
        view.showProgressLoading();
        balanceInteractor.getBalanceTokoCash(getBalanceSubscriber());
    }

    private Subscriber<TokoCashBalanceData> getBalanceSubscriber() {
        return new Subscriber<TokoCashBalanceData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                errorNetworkHandler(e);
                view.hideProgressLoading();
                view.showEmptyPage();
            }

            @Override
            public void onNext(TokoCashBalanceData tokoCashBalanceData) {
                view.hideProgressLoading();
                view.renderBalanceTokoCash(tokoCashBalanceData);
            }
        };
    }

    private void errorNetworkHandler(Throwable e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException) {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseDataNullException) {
            view.showToastMessage(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            view.showToastMessage(e.getMessage());
        } else if (e instanceof ServerErrorException) {
            ServerErrorHandlerUtil.handleError(e);
        } else {
            view.showToastMessage(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }

    @Override
    public void getTokenWallet() {
        balanceInteractor.getTokenWallet(getSubscriberWalletToken());
    }

    private Subscriber<WalletToken> getSubscriberWalletToken() {
        return new Subscriber<WalletToken>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResponseTokoCashRuntimeException) {
                    ServerErrorHandler.showForceLogoutDialog();
                } else {
                    errorNetworkHandler(e);
                }
            }

            @Override
            public void onNext(WalletToken walletToken) {
                sessionHandler.setTokenTokoCash(walletToken.getToken());
                Log.d("TOKEN TOKOCASH", "onNext: " + SessionHandler.getAccessTokenTokoCash());
            }
        };
    }
}
