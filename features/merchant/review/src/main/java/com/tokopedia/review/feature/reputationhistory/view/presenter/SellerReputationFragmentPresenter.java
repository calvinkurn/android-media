package com.tokopedia.review.feature.reputationhistory.view.presenter;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.review.R;
import com.tokopedia.review.feature.reputationhistory.data.model.request.SellerReputationRequest;
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationMergeUseCase;
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationUseCase;
import com.tokopedia.review.feature.reputationhistory.domain.model.SellerReputationDomain;
import com.tokopedia.review.feature.reputationhistory.util.DefaultErrorSubscriber;
import com.tokopedia.review.feature.reputationhistory.util.GoldMerchantDateUtils;
import com.tokopedia.review.feature.reputationhistory.util.NetworkStatus;
import com.tokopedia.review.feature.reputationhistory.util.ReputationDateUtils;
import com.tokopedia.review.feature.reputationhistory.util.ShopNetworkController;
import com.tokopedia.review.feature.reputationhistory.view.SellerReputationView;
import com.tokopedia.review.feature.reputationhistory.view.model.EmptyListModel;
import com.tokopedia.review.feature.reputationhistory.view.model.ReputationReviewModel;
import com.tokopedia.review.feature.reputationhistory.view.model.SetDateHeaderModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author normansyahputa on 3/15/17.
 */
public class SellerReputationFragmentPresenter extends BaseDaggerPresenter<SellerReputationView> {
    public static final String REPUTATION_DATE = "dd-MM-yyyy";
    private SellerReputationRequest sellerReputationRequest;
    private UserSessionInterface userSession;
    private NetworkStatus networkStatus;
    private GCMHandler gcmHandler;
    private ReviewReputationUseCase reviewReputationUseCase;
    private DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener;
    private ReviewReputationMergeUseCase reviewReputationMergeUseCase;
    private ShopNetworkController.ShopInfoParam shopInfoParam;
    private String messageExceptionDesc;

    public SellerReputationFragmentPresenter() {
        sellerReputationRequest = new SellerReputationRequest();
        sellerReputationRequest.setStartDate(ReputationDateUtils.getDateFormat(
                Calendar.getInstance().getTimeInMillis(), 7
        ));
        sellerReputationRequest.setsDate(
                GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 7));
        sellerReputationRequest.setEndDate(ReputationDateUtils.getDateFormat(
                Calendar.getInstance().getTimeInMillis(), 0
        ));
        sellerReputationRequest.seteDate(
                GoldMerchantDateUtils.getPreviousDate(Calendar.getInstance().getTimeInMillis(), 0));

        // set this flag to hit network
        setNetworkStatus(NetworkStatus.PULLTOREFRESH);

        shopInfoParam = new ShopNetworkController.ShopInfoParam();
    }

    public void incrementPage() {
        sellerReputationRequest.incrementPage();
    }

    public void resetPage() {
        sellerReputationRequest.resetPage();
    }

    public void resetHitNetwork() {
        setNetworkStatus(NetworkStatus.NONETWORKCALL);
    }

    public void setStartDate(long startDate) {
        sellerReputationRequest.setStartDate(
                GoldMerchantDateUtils
                        .getDateFormatForInput(startDate, ReputationDateUtils.DATE_FORMAT)
        );
    }

    public void setEndDate(long endDate) {
        sellerReputationRequest.setEndDate(
                GoldMerchantDateUtils
                        .getDateFormatForInput(endDate, ReputationDateUtils.DATE_FORMAT)
        );
    }

    public void setUserSession(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    public void setGcmHandler(GCMHandler gcmHandler) {
        this.gcmHandler = gcmHandler;
    }

    public void setReviewReputationUseCase(ReviewReputationUseCase reviewReputationUseCase) {
        this.reviewReputationUseCase = reviewReputationUseCase;
    }

    public void setErrorNetworkListener(DefaultErrorSubscriber.ErrorNetworkListener errorNetworkListener) {
        this.errorNetworkListener = errorNetworkListener;
    }

    public void setReviewReputationMergeUseCase(ReviewReputationMergeUseCase reviewReputationMergeUseCase) {
        this.reviewReputationMergeUseCase = reviewReputationMergeUseCase;
    }

    private TKPDMapParam<String, String> fillParam() {
        return AuthUtil.generateParamsNetwork(
                userSession.getUserId(), gcmHandler.getRegistrationId(),
                sellerReputationRequest.getParamSummaryReputation());
    }

    private ShopNetworkController.ShopInfoParam fillParamShopInfo() {
        shopInfoParam.shopId = userSession.getShopId();
        return shopInfoParam;
    }

    private RequestParams fillParamShopInfo2() {
        return ShopNetworkController.RequestParamFactory.generateRequestParam(userSession.getUserId(),
                gcmHandler.getRegistrationId(), fillParamShopInfo());
    }

    private RequestParams fillParamReview() {
        TKPDMapParam<String, String> mapParam = fillParam();
        String shopID = userSession.getShopId();

        return ReviewReputationUseCase.RequestParamFactory.generateRequestParam(shopID, mapParam);
    }

    private RequestParams mergeFillParam() {
        return ReviewReputationMergeUseCase.RequestParamFactory.generateRequestParam(
                fillParamReview(),
                fillParamShopInfo2()
        );
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;

        switch (getNetworkStatus()) {
            case ONACTIVITYFORRESULT:
            case PULLTOREFRESH:
            case SEARCHVIEW:
                resetPage();
                break;
            default:
                break;
        }
    }

    public boolean isHitNetwork() {
        switch (networkStatus) {
            case LOADMORE:
            case PULLTOREFRESH:
            case SEARCHVIEW:
            case RETRYNETWORKCALL:
            case ONACTIVITYFORRESULT:
                return true;
            case NONETWORKCALL:
            default:
                return false;
        }
    }

    public void fillMessages(Context context) {
        messageExceptionDesc = context.getString(R.string.message_exception_description);
    }

    public void firstTimeNetworkCall2() {
        if (isHitNetwork()) {
            reviewReputationMergeUseCase.execute(
                    mergeFillParam()
                    , new DefaultErrorSubscriber<List<Object>>(errorNetworkListener, messageExceptionDesc) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(List<Object> objects) {
                            super.onNext(objects);
                            if (isViewAttached()) {

                                Object object = objects.get(0);
                                boolean isEmptyShop = false;
                                if (object != null && object instanceof ShopModel) {
                                    ShopModel shopModel = (ShopModel) object;
                                    getView().loadShopInfo(shopModel);
                                    if(shopModel.stats.shopReputationScore.equals("0") ||
                                            shopModel.stats.shopReputationScore.isEmpty()){
                                        isEmptyShop = true;
                                    }
                                }

                                object = objects.get(1);
                                if (object != null && object instanceof SellerReputationDomain) {
                                    SellerReputationDomain sellerReputationDomain
                                            = (SellerReputationDomain) object;

                                    getView().dismissSnackbar();
                                    getView().setLoadMoreFlag(
                                            sellerReputationDomain.getLinks().getNext() == null);
                                    List<ItemType> itemTypes = convertTo(sellerReputationDomain.getList());

                                    EmptyListModel emptyListModel = null;
                                    if (itemTypes.isEmpty()) {
                                        itemTypes.add(emptyListModel = new EmptyListModel());
                                        emptyListModel.setEmptyShop(isEmptyShop);
                                    }

                                    SetDateHeaderModel headerModel = getView().getHeaderModel();
                                    if (headerModel == null) {
                                        SetDateHeaderModel setDateHeaderModel = new SetDateHeaderModel();
                                        setDateHeaderModel.setStartDate(
                                                formatDate(sellerReputationRequest.getsDate()));
                                        setDateHeaderModel.setsDate(sellerReputationRequest.getsDate());
                                        setDateHeaderModel.setEndDate(
                                                formatDate(sellerReputationRequest.geteDate()));
                                        setDateHeaderModel.seteDate(sellerReputationRequest.geteDate());

                                        itemTypes.add(0, setDateHeaderModel);
                                        if (emptyListModel != null)
                                            emptyListModel.setSetDateHeaderModel(setDateHeaderModel);
                                    } else {
                                        itemTypes.add(0, headerModel);
                                        if (emptyListModel != null)
                                            emptyListModel.setSetDateHeaderModel(headerModel);
                                    }
                                    getView().loadMore(itemTypes);
                                }
                            }
                        }
                    }
            );
        }
    }

    public String formatDate(long date) {
        return GoldMerchantDateUtils.getDateFormatForInput(
                date, REPUTATION_DATE
        );
    }

    public void loadMoreNetworkCall() {
        if (isHitNetwork()) {
            reviewReputationUseCase.execute(fillParamReview(),
                    new DefaultErrorSubscriber<SellerReputationDomain>(errorNetworkListener, messageExceptionDesc) {
                        @Override
                        public void onCompleted() {
                            resetHitNetwork();
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }

                        @Override
                        public void onNext(SellerReputationDomain sellerReputationDomain) {
                            super.onNext(sellerReputationDomain);
                            if (isViewAttached()) {
                                getView().dismissSnackbar();
                                getView().setLoadMoreFlag(
                                        sellerReputationDomain.getLinks().getNext() == null);
                                getView().loadData(convertTo(sellerReputationDomain.getList()));
                            }
                        }
                    });
        }
    }

    private List<ItemType> convertTo(List<SellerReputationDomain.Data> datas) {
        List<ItemType> reputationReviewModels =
                new ArrayList<>();

        for (SellerReputationDomain.Data data : datas) {
            ReputationReviewModel.Data resultData =
                    new ReputationReviewModel.Data();
            resultData.setPenaltyScore(data.getPenaltyScore());
            resultData.setDate(data.getDate());
            resultData.setInformation(data.getInformation());

            ReputationReviewModel reputationReviewModel =
                    new ReputationReviewModel();
            reputationReviewModel.setData(resultData);

            reputationReviewModels.add(reputationReviewModel);
        }

        return reputationReviewModels;

    }
}
