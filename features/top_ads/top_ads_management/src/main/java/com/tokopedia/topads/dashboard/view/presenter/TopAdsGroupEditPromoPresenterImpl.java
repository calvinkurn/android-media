package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditProductGroupToNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMoveProductGroupToExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsGroupEditPromoView;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public class TopAdsGroupEditPromoPresenterImpl extends TopAdsManageGroupPromoPresenterImpl<TopAdsGroupEditPromoView> implements TopAdsGroupEditPromoPresenter {

    public static final String MOVE_OUT_GROUP_CODE = "0";
    private final TopAdsEditProductGroupToNewGroupUseCase topAdsEditProductGroupToNewGroupUseCase;
    private final TopAdsMoveProductGroupToExistGroupUseCase topAdsMoveProductGroupToExistGroupUseCase;

    public TopAdsGroupEditPromoPresenterImpl(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase,
                                             TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase,
                                             TopAdsEditProductGroupToNewGroupUseCase topAdsEditProductGroupToNewGroupUseCase,
                                             TopAdsMoveProductGroupToExistGroupUseCase topAdsMoveProductGroupToExistGroupUseCase) {
        super(topAdsSearchGroupAdsNameUseCase, topAdsCheckExistGroupUseCase);
        this.topAdsEditProductGroupToNewGroupUseCase = topAdsEditProductGroupToNewGroupUseCase;
        this.topAdsMoveProductGroupToExistGroupUseCase = topAdsMoveProductGroupToExistGroupUseCase;
    }

    @Override
    public void moveOutProductGroup(String shopId, String adId) {
        getView().showLoading();
        topAdsMoveProductGroupToExistGroupUseCase.execute(
                TopAdsMoveProductGroupToExistGroupUseCase.createRequestParams(adId, MOVE_OUT_GROUP_CODE, shopId),
                getSubscriberMoveOutProductGroup());
    }

    @Override
    public void moveToNewProductGroup(String adid, String groupName, String shopID) {
        getView().showLoading();
        topAdsEditProductGroupToNewGroupUseCase.execute(TopAdsEditProductGroupToNewGroupUseCase.createRequestParams(adid, groupName, shopID),
                getSubscriberMoveToNewProductGroup());
    }

    @Override
    public void moveToExistProductGroup(String adid, String groupId, String shopID) {
        getView().showLoading();
        topAdsMoveProductGroupToExistGroupUseCase.execute(TopAdsMoveProductGroupToExistGroupUseCase.createRequestParams(adid, groupId, shopID),
                getSubscriberMoveToExistProductGroup());
    }

    @Override
    public void detachView() {
        super.detachView();
        topAdsEditProductGroupToNewGroupUseCase.unsubscribe();
        topAdsMoveProductGroupToExistGroupUseCase.unsubscribe();
    }

    private Subscriber<Boolean> getSubscriberMoveToExistProductGroup() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoading();
                if(ViewUtils.getErrorMessage(e) != null){
                    getView().showErrorSnackBar(ViewUtils.getErrorMessage(e));
                }else{
                    getView().showErrorSnackBar(e.getMessage());
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().dismissLoading();
                if(isSuccess){
                    getView().onSuccessMoveToExistProductGroup();
                }else{
                    getView().onErrorMoveToExistProductGroup();
                }
            }
        };
    }

    private Subscriber<Boolean> getSubscriberMoveOutProductGroup() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoading();
                getView().showErrorSnackBar(e.getMessage());
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().dismissLoading();
                if (isSuccess) {
                    getView().onSuccessMoveOutProductGroup();
                } else {
                    getView().onErrorMoveOutProductGroup();
                }
            }
        };
    }

    private Subscriber<Boolean> getSubscriberMoveToNewProductGroup() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().dismissLoading();
                getView().showErrorSnackBar(e.getMessage());
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().dismissLoading();
                if (isSuccess) {
                    getView().onSuccessMoveToNewProductGroup();
                } else {
                    getView().onErrorMoveToNewProductGroup();
                }
            }
        };
    }
}
