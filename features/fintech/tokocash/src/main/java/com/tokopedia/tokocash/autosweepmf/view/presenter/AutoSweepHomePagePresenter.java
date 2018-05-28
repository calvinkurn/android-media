package com.tokopedia.tokocash.autosweepmf.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.domain.interactor.GetAutoSweepDetailUseCase;
import com.tokopedia.tokocash.autosweepmf.domain.interactor.PostAutoSweepLimitUseCase;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepDetailDomain;
import com.tokopedia.tokocash.autosweepmf.domain.model.AutoSweepLimitDomain;
import com.tokopedia.tokocash.autosweepmf.view.contract.AutoSweepHomeContract;
import com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepDetailMapper;
import com.tokopedia.tokocash.autosweepmf.view.mapper.AutoSweepLimitMapper;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;
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

import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.FALSE_INT;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.SUCCESS_CODE;
import static com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant.TRUE_INT;

public class AutoSweepHomePagePresenter extends BaseDaggerPresenter<AutoSweepHomeContract.View>
        implements AutoSweepHomeContract.Presenter {
    private GetAutoSweepDetailUseCase mGetAutoSweepDetailUseCase;
    private AutoSweepDetailMapper mMapper;
    private AutoSweepLimitMapper mAutoSweepLimitMapper;
    private PostAutoSweepLimitUseCase mAutoSweepLimitUseCase;

    @Inject
    public AutoSweepHomePagePresenter(@NonNull GetAutoSweepDetailUseCase getAutoSweepDetailUseCase,
                                      @NonNull PostAutoSweepLimitUseCase autoSweepLimitUseCase,
                                      @NonNull AutoSweepDetailMapper mapper,
                                      @NonNull AutoSweepLimitMapper autoSweepLimitMapper) {
        this.mGetAutoSweepDetailUseCase = getAutoSweepDetailUseCase;
        this.mAutoSweepLimitUseCase = autoSweepLimitUseCase;
        this.mMapper = mapper;
        this.mAutoSweepLimitMapper = autoSweepLimitMapper;
    }

    @Override
    public void destroyView() {
        if (mGetAutoSweepDetailUseCase != null) {
            mGetAutoSweepDetailUseCase.unsubscribe();
        }
    }

    @Override
    public void getAutoSweepDetail() {
        getView().showLoading();
        mGetAutoSweepDetailUseCase.getExecuteObservable(RequestParams.EMPTY).map(
                new Func1<AutoSweepDetailDomain, AutoSweepDetail>() {
                    @Override
                    public AutoSweepDetail call(AutoSweepDetailDomain domainData) {
                        return mMapper.transform(domainData);

                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AutoSweepDetail>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideLoading();
                        getView().onErrorAutoSweepDetail(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                    }

                    @Override
                    public void onNext(AutoSweepDetail data) {
                        getView().hideLoading();
                        if (data.getCode() == SUCCESS_CODE) {
                            getView().onSuccessAutoSweepDetail(data);
                        } else {
                            getView().onErrorAutoSweepDetail(data.getMessage());
                        }
                    }
                });

    }

    @Override
    public void updateAutoSweepStatus(boolean isEnable, int amount) {
        getView().showLoading();
        mAutoSweepLimitUseCase.getExecuteObservable(getPayload(isEnable, amount)).map(
                new Func1<AutoSweepLimitDomain, AutoSweepLimit>() {
                    @Override
                    public AutoSweepLimit call(AutoSweepLimitDomain data) {
                        return mAutoSweepLimitMapper.transform(data);

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
                        getView().onErrorAutoSweepStatus(ErrorHandler.getErrorMessage(getView().getActivityContext(), e));
                    }

                    @Override
                    public void onNext(AutoSweepLimit data) {
                        getView().hideLoading();
                        if (data.getCode() == SUCCESS_CODE) {
                            getView().onSuccessAutoSweepStatus(data);
                        } else {
                            getView().onErrorAutoSweepStatus(data.getMessage());
                        }

                    }
                });
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
}
