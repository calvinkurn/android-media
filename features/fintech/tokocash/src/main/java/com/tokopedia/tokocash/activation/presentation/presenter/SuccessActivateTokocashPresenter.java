package com.tokopedia.tokocash.activation.presentation.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.activation.domain.GetRefreshWalletTokenUseCase;
import com.tokopedia.tokocash.activation.presentation.contract.SuccessActivateTokocashContract;
import com.tokopedia.cachemanager.PersistentCacheManager;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 16/08/18.
 */
public class SuccessActivateTokocashPresenter extends BaseDaggerPresenter<SuccessActivateTokocashContract.View>
        implements SuccessActivateTokocashContract.Presenter {

    private GetRefreshWalletTokenUseCase getRefreshWalletTokenUseCase;
    private WalletUserSession walletUserSession;

    @Inject
    public SuccessActivateTokocashPresenter(GetRefreshWalletTokenUseCase getRefreshWalletTokenUseCase,
                                            WalletUserSession walletUserSession) {
        this.getRefreshWalletTokenUseCase = getRefreshWalletTokenUseCase;
        this.walletUserSession = walletUserSession;
    }

    @Override
    public void deleteCacheBalanceTokoCash() {
        PersistentCacheManager.instance.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
    }

    @Override
    public void getUserPhoneNumber() {
        if (walletUserSession.getPhoneNumber() != null && !walletUserSession.getPhoneNumber().equals(""))
            getView().showUserPhoneNumber(walletUserSession.getPhoneNumber());
    }

    @Override
    public void refreshingWalletToken() {
        getRefreshWalletTokenUseCase.execute(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached())
                    getView().failedRefreshToken(e);
            }

            @Override
            public void onNext(String tokenWallet) {
                if (!TextUtils.isEmpty(tokenWallet)) {
                    walletUserSession.setTokenWallet(tokenWallet);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        detachView();
        if (getRefreshWalletTokenUseCase != null)
            getRefreshWalletTokenUseCase.unsubscribe();
    }
}
