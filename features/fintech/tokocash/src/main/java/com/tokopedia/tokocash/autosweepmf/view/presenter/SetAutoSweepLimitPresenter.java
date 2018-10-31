package com.tokopedia.tokocash.autosweepmf.view.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.domain.interactor.PostAutoSweepLimitUseCase;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.tokocash.autosweepmf.view.contract.SetAutoSweepLimitContract;
import com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepLimitMapper;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.AUTO_SWEEP_MF_MAX_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.AUTO_SWEEP_MF_MIN_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.EXTRA_AUTO_SWEEP_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.EXTRA_AVAILABLE_TOKOCASH;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.FALSE_INT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.FIREBASE_APP_AUTOSWEEP_MAX_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.FIREBASE_APP_AUTOSWEEP_MIN_LIMIT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.TRUE_INT;

public class SetAutoSweepLimitPresenter extends BaseDaggerPresenter<SetAutoSweepLimitContract.View>
        implements SetAutoSweepLimitContract.Presenter {
    private PostAutoSweepLimitUseCase mAutoSweepLimitUseCase;
    private AutoSweepLimitMapper mMapper;


    @Inject
    public SetAutoSweepLimitPresenter(@NonNull PostAutoSweepLimitUseCase autoSweepLimitUseCase,
                                      @NonNull AutoSweepLimitMapper mapper) {
        this.mAutoSweepLimitUseCase = autoSweepLimitUseCase;
        this.mMapper = mapper;
    }

    @Override
    public void destroyView() {
        if (mAutoSweepLimitUseCase != null) {
            mAutoSweepLimitUseCase.unsubscribe();
        }
    }

    @Override
    public void postAutoSweepLimit(boolean isEnable, int amount) {
        getView().showLoading();
        mAutoSweepLimitUseCase.getExecuteObservable(getPayload(isEnable, amount)).map(
                new Func1<AutoSweepLimitDomain, AutoSweepLimit>() {
                    @Override
                    public AutoSweepLimit call(AutoSweepLimitDomain data) {
                        return mMapper.transform(data);

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AutoSweepLimit>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        getView().onErrorAccountStatus(ErrorHandler.getErrorMessage(getView()
                                .getActivityContext(), e));
                    }

                    @Override
                    public void onNext(AutoSweepLimit data) {
                        getView().hideLoading();
                        getView().onSuccessAccountStatus(data);
                    }
                });
    }

    public String getTokocashBalance(@NonNull Bundle bundle) {
        return bundle.getString(EXTRA_AVAILABLE_TOKOCASH);
    }

    public long getAutoSweepMaxLimit() {
        if (getView().getLongRemoteConfig(FIREBASE_APP_AUTOSWEEP_MAX_LIMIT) == null) {
            return AUTO_SWEEP_MF_MAX_LIMIT;
        }

        return getView().getLongRemoteConfig(FIREBASE_APP_AUTOSWEEP_MAX_LIMIT);
    }

    public long getAutoSweepMinLimit() {
        if (getView().getLongRemoteConfig(FIREBASE_APP_AUTOSWEEP_MIN_LIMIT) == null) {
            return AUTO_SWEEP_MF_MIN_LIMIT;
        }

        return getView().getLongRemoteConfig(FIREBASE_APP_AUTOSWEEP_MIN_LIMIT);
    }

    /**
     * Payload creator utility method for auto sweep detail api
     *
     * @param isEnable - Auto sweep status
     * @param amount   - Auto sweep limit (Min CommonConstant.AUTO_SWEEP_MF_MIN_LIMIT)
     * @return Payload object
     */
    private RequestParams getPayload(boolean isEnable, int amount) {
        Map<String, Object> variables = new HashMap<>();

        if (isEnable) {
            variables.put(CommonConstant.GqlApiKeys.ENABLE, TRUE_INT);
        } else {
            variables.put(CommonConstant.GqlApiKeys.ENABLE, FALSE_INT);
        }

        variables.put(CommonConstant.GqlApiKeys.LIMIT, amount);

        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CommonConstant.GqlApiKeys.QUERY, GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.gql_autosweepmf_set_limit));
        requestParams.putObject(CommonConstant.GqlApiKeys.VARIABLES, variables);
        return requestParams;
    }

    /**
     * Invoke broadcast message regarding mutual fund and auto sweep status changed event
     */
    public void sendMessage() {
        Intent intent = new Intent(CommonConstant.EVENT_AUTOSWEEPMF_STATUS_CHANGED);
        intent.putExtra(CommonConstant.EVENT_KEY_NEEDED_RELOADING, true);
        LocalBroadcastManager.getInstance(getView().getAppContext()).sendBroadcast(intent);
    }

    public long getAutoSweepLimit(@NonNull Bundle bundle) {
        if (bundle.getLong(EXTRA_AUTO_SWEEP_LIMIT) <= 0) {
            return getAutoSweepMinLimit();
        }

        return bundle.getLong(EXTRA_AUTO_SWEEP_LIMIT);
    }
}
