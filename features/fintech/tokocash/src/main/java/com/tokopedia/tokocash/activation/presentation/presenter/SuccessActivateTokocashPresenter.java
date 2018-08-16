package com.tokopedia.tokocash.activation.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.tokocash.CacheUtil;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.activation.domain.GetRefreshWalletTokenUseCase;
import com.tokopedia.tokocash.activation.presentation.contract.SuccessActivateTokocashContract;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 16/08/18.
 */
public class SuccessActivateTokocashPresenter extends BaseDaggerPresenter<SuccessActivateTokocashContract.View>
        implements SuccessActivateTokocashContract.Presenter {

    private GetRefreshWalletTokenUseCase getRefreshWalletTokenUseCase;
    private WalletUserSession walletUserSession;
    private CacheManager cacheManager;

    @Inject
    public SuccessActivateTokocashPresenter(GetRefreshWalletTokenUseCase getRefreshWalletTokenUseCase,
                                            WalletUserSession walletUserSession,
                                            CacheManager cacheManager) {
        this.getRefreshWalletTokenUseCase = getRefreshWalletTokenUseCase;
        this.walletUserSession = walletUserSession;
        this.cacheManager = cacheManager;
    }

    @Override
    public void deleteCacheBalanceTokoCash() {
        cacheManager.delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
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
                getView().failedRefreshToken();
            }

            @Override
            public void onNext(String tokenWallet) {
                if (!tokenWallet.equals("")) {
                    walletUserSession.setTokenWallet(tokenWallet);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (getRefreshWalletTokenUseCase != null)
            getRefreshWalletTokenUseCase.unsubscribe();
    }
}
