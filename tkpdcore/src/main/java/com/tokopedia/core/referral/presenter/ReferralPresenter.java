package com.tokopedia.core.referral.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.JsonObject;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.data.UserAttributesRepository;
import com.tokopedia.core.analytics.data.UserAttributesRepositoryImpl;
import com.tokopedia.core.analytics.data.factory.UserAttributesFactory;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.referral.ReferralService;
import com.tokopedia.core.network.apiservices.referral.apis.ReferralApi;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.exception.HttpErrorException;
import com.tokopedia.core.network.exception.ResponseDataNullException;
import com.tokopedia.core.network.exception.ResponseErrorException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.data.TkpdReferralResponse;
import com.tokopedia.core.referral.listner.ReferralView;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TokoCashTypeDef;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public class ReferralPresenter implements IReferralPresenter {

    private Activity activity;
    private ReferralView view;
    private String contents = "";
    private RemoteConfig remoteConfig;
    public static final String ShareScreenName = "Share Channel";
    public static final String phoneVerificationScreenName = "Phone Number Verification";
    private static final String BEARER_TOKEN = "Bearer ";


    public ReferralPresenter(ReferralView view) {
        this.activity = view.getActivity();
        this.view = view;
        remoteConfig = new FirebaseRemoteConfigImpl(view.getActivity());
    }

    @Override
    public void initialize() {
        if (view.isUserLoggedIn()) {
            if (isAppShowReferralButtonActivated()) {
                if (view.isUserPhoneNumberVerified()) {
                    fetchTokoCashBalance();

                } else {
                    view.showVerificationPhoneNumberPage();
                    TrackingUtils.sendMoEngageReferralScreenOpen(phoneVerificationScreenName);

                }
            }
        } else {
            view.navigateToLoginPage();
        }
    }

    @Override
    public void shareApp() {
        if (isAppShowReferralButtonActivated()) {
            contents = contents.replace("(kode)", view.getReferralCodeFromTextView()) + " ";
        } else {
            contents = getAppShareDescription();
        }
        ShareData shareData = ShareData.Builder.aShareData()
                .setType(ShareData.APP_SHARE_TYPE)
                .setId(view.getReferralCodeFromTextView())
                .setName(activity.getString(R.string.app_share_title))
                .setTextContent(contents)
                .setUri(Constants.WEB_PLAYSTORE_BUYER_APP_URL)
                .build();
        activity.startActivity(ShareActivity.createIntent(activity, shareData));
        TrackingUtils.sendMoEngageReferralScreenOpen(ShareScreenName);


    }

    @Override
    public void getReferralVoucherCode() {
        if (getVoucherCodeFromCache() == null || "".equalsIgnoreCase(getVoucherCodeFromCache())) {
            view.showProcessDialog();
        }
        Observable<ReferralCodeEntity> observer = processGetReferralVoucherCode();
        observer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ReferralCodeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideProcessDialog();
                        e.printStackTrace();
                        if (getVoucherCodeFromCache() == null || "".equalsIgnoreCase(getVoucherCodeFromCache())) {
                            return;
                        }
                        String message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                        if (e instanceof UnknownHostException || e instanceof ConnectException) {
                            message = ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL;
                        } else if (e instanceof SocketTimeoutException) {
                            message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                        } else if (e instanceof ResponseErrorException
                                || e instanceof ResponseDataNullException
                                || e instanceof HttpErrorException) {
                            message = e.getMessage();
                        }
                        NetworkErrorHelper.createSnackbarWithAction(view.getActivity(), message, new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getReferralVoucherCode();
                            }
                        }).showRetrySnackbar();
                    }

                    @Override
                    public void onNext(ReferralCodeEntity referralCodeEntity) {
                        if (referralCodeEntity.getErorMessage() == null) {
                            LocalCacheHandler localCacheHandler = new LocalCacheHandler(activity, TkpdCache.REFERRAL);
                            localCacheHandler.putString(TkpdCache.Key.REFERRAL_CODE, referralCodeEntity.getPromoContent().getCode());
                            localCacheHandler.applyEditor();
                            contents = referralCodeEntity.getPromoContent().getContent();
                            view.renderVoucherCode(referralCodeEntity.getPromoContent().getCode());
                        } else {
                            NetworkErrorHelper.createSnackbarWithAction(view.getActivity(), referralCodeEntity.getErorMessage(), new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getReferralVoucherCode();
                                }
                            }).showRetrySnackbar();
                        }
                        view.hideProcessDialog();
                    }
                });

    }

    private Observable<ReferralCodeEntity> processGetReferralVoucherCode() {
        JsonObject requestBody = new JsonObject();
        JsonObject requestBody1 = new JsonObject();
        requestBody.addProperty("user_id", Integer.parseInt(SessionHandler.getLoginID(activity)));
        requestBody.addProperty("msisdn", SessionHandler.getPhoneNumber());
        requestBody1.add("data", requestBody);
        ReferralApi referralApi = new ReferralService().getApi();
        return referralApi.getReferralVoucherCode(requestBody1.toString()).map(new Func1<String, ReferralCodeEntity>() {
            @Override
            public ReferralCodeEntity call(String tkpdResponseResponse) {
                TkpdReferralResponse factory = TkpdReferralResponse.factory(tkpdResponseResponse);
                ReferralCodeEntity referralCodeEntity = new ReferralCodeEntity();
                if (factory.isError() && factory.getErrorMessages() != null && factory.getErrorMessages().size() > 0) {
                    referralCodeEntity.setErorMessage(factory.getErrorMessages().get(0));
                } else {
                    referralCodeEntity = factory.convertToObj(ReferralCodeEntity.class);// new Gson().fromJson(tkpdResponseResponse, ReferralCodeEntity.class);

                }
                return referralCodeEntity;
            }
        });
    }

    @Override
    public void copyVoucherCode(String voucherCode) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(view.getActivity().getString(R.string.copied_to_clipboard), voucherCode);
        clipboard.setPrimaryClip(clip);
        view.showToastMessage(view.getActivity().getString(R.string.copied_to_clipboard) + " " + voucherCode);
        UnifyTracking.eventReferralAndShare(AppEventTracking.Action.CLICK_COPY_REFERRAL_CODE, voucherCode);

    }

    @Override
    public String getReferralContents() {
        if (isAppShowReferralButtonActivated()) {
            return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFFERAL_CONTENT, view.getActivity().getString(R.string.app_share_referral_label_desc));
        } else {
            return view.getActivity().getString(R.string.app_share_label_desc);
        }
    }

    @Override
    public String getHowItWorks() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_HOWITWORKS, view.getActivity().getString(R.string.title_app_referral_howitworks));
    }

    @Override
    public String getVoucherCodeFromCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(view.getActivity(), TkpdCache.REFERRAL);
        return localCacheHandler.getString(TkpdCache.Key.REFERRAL_CODE, "");
    }

    @Override
    public Boolean isAppShowReferralButtonActivated() {
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    private String getAppShareDescription() {
        return remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_SHARE_DESCRIPTION, view.getActivity().getString(R.string.app_share_label_desc));
    }

    /**
     * This function fetches the tokocash balance and then check Voucher code
     */
    private void fetchTokoCashBalance() {
        SessionHandler sessionHandler = new SessionHandler(view.getActivity());
        JobExecutor jobExecutor = new JobExecutor();
        PostExecutionThread uiThread = new UIThread();
        GlobalCacheManager profileCache = new GlobalCacheManager();
        ProfileSourceFactory profileSourceFactory = new ProfileSourceFactory(
                view.getActivity(),
                new PeopleService(),
                new ProfileMapper(),
                profileCache,
                new AnalyticsCacheHandler(),
                sessionHandler
        );
        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(view.getActivity());
        authKey = BEARER_TOKEN + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);
        GlobalCacheManager walletCache = new GlobalCacheManager();

        UserAttributesRepository userAttributesRepository = new UserAttributesRepositoryImpl(
                new UserAttributesFactory(
                        ApolloClient.builder()
                                .okHttpClient(OkHttpFactory.create().buildClientDefaultAuth())
                                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                                .build())
        );
        TokoCashSourceFactory tokoCashSourceFactory = new TokoCashSourceFactory(
                view.getActivity(),
                accountsService,
                new TokoCashMapper(),
                walletCache);

        TokoCashRepository tokoCashRepository = new TokoCashRepositoryImpl(tokoCashSourceFactory);

        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                jobExecutor,
                uiThread,
                tokoCashRepository
        );
        tokoCashUseCase.execute(RequestParams.EMPTY, new Subscriber<TokoCashModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("ManagePaymentOptionsPresenter :: inside tokocash subscriber error");
            }

            @Override
            public void onNext(TokoCashModel tokoCashModel) {
                if (tokoCashModel != null
                        && tokoCashModel.isSuccess()
                        && tokoCashModel.getTokoCashData() != null) {
                    CommonUtils.dumper("ManagePaymentOptionsPresenter :: tokocash balance == " + tokoCashModel.getTokoCashData().getBalance());
                    if (tokoCashModel.getTokoCashData().getLink() == TokoCashTypeDef.TOKOCASH_ACTIVE) {
                        getReferralVoucherCode();

                    } else {

                        WalletRouterUtil.navigateWallet(
                                view.getActivity().getApplication(),
                                view.getActivity(),
                                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                                tokoCashModel.getTokoCashData().getmAppLinks(),
                                tokoCashModel.getTokoCashData().getRedirectUrl(),
                                new Bundle()
                        );
                    }
                } else {

                }
            }
        });
    }
}