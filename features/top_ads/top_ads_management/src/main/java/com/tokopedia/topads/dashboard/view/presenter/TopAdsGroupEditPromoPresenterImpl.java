package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsCheckExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsEditProductGroupToNewGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMinimumBidUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsMoveProductGroupToExistGroupUseCase;
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSearchGroupAdsNameUseCase;
import com.tokopedia.topads.dashboard.utils.ViewUtils;
import com.tokopedia.topads.dashboard.view.listener.TopAdsGroupEditPromoView;
import com.tokopedia.topads.sourcetagging.data.TopAdsSourceTaggingModel;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/1/17.
 */

public class TopAdsGroupEditPromoPresenterImpl extends TopAdsManageGroupPromoPresenterImpl<TopAdsGroupEditPromoView> implements TopAdsGroupEditPromoPresenter {

    public static final String MOVE_OUT_GROUP_CODE = "0";
    private final TopAdsEditProductGroupToNewGroupUseCase topAdsEditProductGroupToNewGroupUseCase;
    private final TopAdsMoveProductGroupToExistGroupUseCase topAdsMoveProductGroupToExistGroupUseCase;
    private final TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase;

    public TopAdsGroupEditPromoPresenterImpl(TopAdsSearchGroupAdsNameUseCase topAdsSearchGroupAdsNameUseCase,
                                             TopAdsCheckExistGroupUseCase topAdsCheckExistGroupUseCase,
                                             TopAdsEditProductGroupToNewGroupUseCase topAdsEditProductGroupToNewGroupUseCase,
                                             TopAdsMoveProductGroupToExistGroupUseCase topAdsMoveProductGroupToExistGroupUseCase,
                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase,
                                             TopAdsMinimumBidUseCase topAdsMinimumBidUseCase,
                                             UserSessionInterface sessionInterface) {
        super(topAdsSearchGroupAdsNameUseCase, topAdsCheckExistGroupUseCase, topAdsMinimumBidUseCase, sessionInterface);
        this.topAdsEditProductGroupToNewGroupUseCase = topAdsEditProductGroupToNewGroupUseCase;
        this.topAdsMoveProductGroupToExistGroupUseCase = topAdsMoveProductGroupToExistGroupUseCase;
        this.topAdsGetSourceTaggingUseCase = topAdsGetSourceTaggingUseCase;
    }

    @Override
    public void moveOutProductGroup(String shopId, String adId) {
        getView().showLoading();
        topAdsMoveProductGroupToExistGroupUseCase.execute(
                TopAdsMoveProductGroupToExistGroupUseCase.createRequestParams(adId, MOVE_OUT_GROUP_CODE, shopId),
                getSubscriberMoveOutProductGroup());
    }

    @Override
    public void moveToNewProductGroup(final String adid, final String groupName, final String shopID) {
        getView().showLoading();
        topAdsGetSourceTaggingUseCase.execute(new Subscriber<TopAdsSourceTaggingModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TopAdsSourceTaggingModel topAdsSourceTaggingModel) {
                String source = TopAdsNetworkConstant.VALUE_SOURCE_ANDROID;

                if (topAdsSourceTaggingModel != null){
                    source = topAdsSourceTaggingModel.getSource();
                }

                topAdsEditProductGroupToNewGroupUseCase.execute(TopAdsEditProductGroupToNewGroupUseCase
                                .createRequestParams(adid, groupName, shopID, source),
                        getSubscriberMoveToNewProductGroup());
            }
        });
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
        topAdsGetSourceTaggingUseCase.unsubscribe();
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
