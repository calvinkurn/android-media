package com.tokopedia.shop.open.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.district_recommendation.domain.model.Token;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.shop.open.data.model.response.CreateShop;
import com.tokopedia.shop.open.data.model.response.ValidateShopDomainSuggestionHeader;
import com.tokopedia.shop.open.domain.interactor.ShopOpenCheckDomainNameUseCase;
import com.tokopedia.shop.open.domain.interactor.ShopOpenSubmitUseCase;
import com.tokopedia.shop.open.view.listener.ShopOpenDomainView;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ShopOpenDomainPresenterImpl extends BaseDaggerPresenter<ShopOpenDomainView>
        implements ShopOpenDomainPresenter {

    private static final int DELAY_DEBOUNCE = 700; // ms
    public static final String SUCCESS = "1";

    private Subscription domainDebounceSubscription;
    private Subscription shopDebounceSubscription;
    private boolean isHitToken;
    private GetOpenShopTokenUseCase getOpenShopTokenUseCase;
    private ShopOpenSubmitUseCase shopOpenSubmitUseCase;
    private ShopOpenCheckDomainNameUseCase shopOpenCheckDomainNameUseCase;

    private ShopOpenDomainPresenterImpl.QueryListener domainListener;
    private ShopOpenDomainPresenterImpl.QueryListener shopListener;
    private GlobalCacheManager globalCacheManager;
    private UserSessionInterface userSession;

    @Inject
    public ShopOpenDomainPresenterImpl(ShopOpenSubmitUseCase shopOpenSubmitUseCase,
                                       GlobalCacheManager globalCacheManager,
                                       UserSessionInterface userSession,
                                       ShopOpenCheckDomainNameUseCase shopOpenCheckDomainNameUseCase,
                                       GetOpenShopTokenUseCase getOpenShopTokenUseCase) {
        this.shopOpenSubmitUseCase = shopOpenSubmitUseCase;
        this.getOpenShopTokenUseCase = getOpenShopTokenUseCase;
        this.shopOpenCheckDomainNameUseCase = shopOpenCheckDomainNameUseCase;
        this.globalCacheManager = globalCacheManager;
        this.userSession = userSession;

        domainDebounceSubscription = Observable.unsafeCreate(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        domainListener = new ShopOpenDomainPresenterImpl.QueryListener() {
                            @Override
                            public void query(String string) {
                                subscriber.onNext(string);
                            }
                        };
                    }
                })
                .debounce(DELAY_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        checkDomainWS(s);
                    }
                });
        shopDebounceSubscription = Observable.unsafeCreate(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        shopListener = new ShopOpenDomainPresenterImpl.QueryListener() {
                            @Override
                            public void query(String string) {
                                subscriber.onNext(string);
                            }
                        };
                    }
                })
                .debounce(DELAY_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        checkShopWS(s);
                    }
                });
    }

    private Subscriber<CreateShop> getCreateShopSubscriber() {
        return new Subscriber<CreateShop>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorReserveShop(e);
                }
            }

            @Override
            public void onNext(CreateShop createShop) {
                if (!createShop.getCreateShop().getSuccess()) {
                    getView().onErrorCreateShop(createShop.getCreateShop().getMessage());
                    return;
                }
                userSession.setShopId(createShop.getCreateShop().getCreatedId());
                globalCacheManager.delete(ProfileSourceFactory.KEY_PROFILE_DATA);
                getView().onSuccessCreateShop(createShop.getCreateShop().getMessage(),createShop.getCreateShop().getCreatedId());
            }
        };
    }

    public void openDistrictRecommendation(RequestParams requestParams) {
        if (isHitToken) {
            return;
        }
        isHitToken = true;
        if (isViewAttached()) {
            getView().showSubmitLoading();
        }
        getOpenShopTokenUseCase.execute(requestParams, new Subscriber<Token>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                isHitToken = false;
                if (isViewAttached()) {
                    getView().hideSubmitLoading();
                    getView().onErrorReserveShop(e);
                }
            }

            @Override
            public void onNext(Token token) {
                isHitToken = false;
                getView().hideSubmitLoading();
                getView().onSuccessGetToken(token);
            }
        });
    }

    private interface QueryListener {
        void query(String string);
    }

    public void onSubmitCreateShop(String shopName, String domain, Integer districtId, Integer postalCodeId) {
        shopOpenSubmitUseCase.execute(ShopOpenSubmitUseCase.Companion.createRequestParams(shopName, domain, districtId, postalCodeId),
                getCreateShopSubscriber());
    }

    @Override
    public void checkShop(String shopName) {
        if (shopListener != null) {
            shopListener.query(shopName);
        }
    }

    private void checkShopWS(String shopName) {
        if (!getView().isShopNameInValidRange()) {
            return;
        }

        shopOpenCheckDomainNameUseCase.execute(ShopOpenCheckDomainNameUseCase.Companion.createRequestParam(shopName), new Subscriber<ValidateShopDomainSuggestionHeader>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorReserveShop(e);
                }
            }

            @Override
            public void onNext(ValidateShopDomainSuggestionHeader validateShopDomainSuggestionHeader) {
                boolean isNameValid = validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().isValid();
                boolean isNameErrorMessageEmpty = validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().getError().getMessage().isEmpty();
                boolean isDomainSuggestionErrorEmpty = validateShopDomainSuggestionHeader.getData().getShopDomainSuggestion().getError().getMessage().isEmpty();

                if ((!isNameValid && !isNameErrorMessageEmpty) || !isDomainSuggestionErrorEmpty) {
                    getView().onErrorCheckShopName(validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().getError().getMessage());
                    return;
                }

                if (getView().isShopNameInValidRange()) {
                    getView().onSuccessCheckShopName(validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().isValid(),
                            validateShopDomainSuggestionHeader.getData().getShopDomainSuggestion().getResult().getShopDomain());
                }
            }
    });
}

    private void checkDomainWS(String domainName) {
        if (!getView().isShopDomainInValidRange()) {
            return;
        }
        shopOpenCheckDomainNameUseCase.execute(ShopOpenCheckDomainNameUseCase.Companion.createRequestParams(domainName, ""), new Subscriber<ValidateShopDomainSuggestionHeader>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorReserveShop(e);
                }
            }

            @Override
            public void onNext(ValidateShopDomainSuggestionHeader validateShopDomainSuggestionHeader) {
                boolean isDomainValid = validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().isValid();
                boolean isDomainErrorMessageEmpty = validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().getError().getMessage().isEmpty();
                if (!isDomainValid && !isDomainErrorMessageEmpty){
                    getView().onErrorCheckShopDomain(validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().getError().getMessage());
                    return;
                }
                if (getView().isShopDomainInValidRange()) {
                    getView().onSuccessCheckShopDomain(validateShopDomainSuggestionHeader.getValidateDomainShopName().getValidateDomainShopName().isValid());
                }
            }
        });
    }

    @Override
    public void checkDomain(String domainName) {
        if (domainListener != null) {
            domainListener.query(domainName);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        shopOpenCheckDomainNameUseCase.unsubscribe();
        getOpenShopTokenUseCase.unsubscribe();
        shopOpenSubmitUseCase.unsubscribe();
        domainDebounceSubscription.unsubscribe();
        shopDebounceSubscription.unsubscribe();
    }
}
