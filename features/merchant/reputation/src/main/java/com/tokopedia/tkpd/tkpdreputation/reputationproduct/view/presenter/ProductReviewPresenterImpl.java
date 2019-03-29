package com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core2.R;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReviewPass;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.ActResult;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReputationRetrofitInteractor;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.ActReputationRetrofitInteractorImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.facade.FacadeProductReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.facade.FacadeProductReviewImpl;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review.HelpfulReviewList;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.product_review.AdvanceReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.Const;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.util.RatingStatsUtils;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.activity.ReputationProduct;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.view.listener.ProductReviewView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Steven on 12/01/2016.
 */
public class ProductReviewPresenterImpl implements ProductReviewPresenter {

    private final Context context;
    private final ProductReviewView view;
    private FacadeProductReviewImpl facade;
    private ActReputationRetrofitInteractor actNetworkInteractor;

    public ProductReviewPresenterImpl(Context context, String NAV, ProductReviewView view){
        this.context = context;
        this.view = view;
        this.facade = new FacadeProductReview(NAV,context,ProductReviewPresenterImpl.this);
        this.actNetworkInteractor = new ActReputationRetrofitInteractorImpl();
    }

    @Override
    public void getMostHelpfulReview() {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("product_id", view.getProductID());
        hashMap.put("shop_id", view.getShopId());
        if(view.getNAV().equals(ReputationProduct.SIX_MONTH_REPUTATION)){
            hashMap.put("month_range", String.valueOf(6));
        }
        facade.getMostHelpfulReview(hashMap, new FacadeProductReviewImpl.GetHelpfulReviewListener() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onTimeout() {

            }

            @Override
            public void onSuccess(HelpfulReviewList result) {
                view.returnHelpfulReview(result);
            }
        });
    }

    @Override
    public void getReputation(final HashMap<String, String> paramReview) {
        if (paramReview.get("NAV").equals(ReputationProduct.SIX_MONTH_REPUTATION)) {
            paramReview.put("month_range", String.valueOf(6));
        }
        paramReview.put("per_page", "10");
        switch (paramReview.get("filter")) {
            case "ByQuality":
                paramReview.put("shop_quality", paramReview.get("filter_value"));
                break;
            case "ByAccuracy":
                paramReview.put("shop_accuracy", paramReview.get("filter_value"));
                break;
            default:
                break;
        }
        facade.getReputation(context, paramReview, new FacadeProductReviewImpl.GetReviewListener() {


            @Override
            public void onError(String error) {
                view.onConnectionTimeOut(error);
            }

            @Override
            public void onThrowable(Throwable e) {
                if (e instanceof IndexOutOfBoundsException) {

                } else view.onConnectionTimeOut();
            }

            @Override
            public void onTimeout() {
                view.onConnectionTimeOut();
            }

            @Override
            public void onSuccess(String product_id, String NAV, JSONObject result) {
                PagingHandler.PagingHandlerModel pagingHandlerModel = facade.getPaging(result);
                view.onConnectionResponse(facade.getModelRatingStats(result),
                        facade.getListReputation(result),
                        pagingHandlerModel);
                if (pagingHandlerModel.getUriPrevious().equals("0")
                        && paramReview.get("filter_value").equals("0"))
                    facade.storeFirstPage(product_id, NAV, result);
            }
        });
    }


    public void saveStateList(Bundle outState,List<RecyclerViewItem> values,
                              int position, AdvanceReview advanceReview, int currentRating,
                              RatingStatsUtils.RatingType currentRatingType, int page,
                              boolean pageHasNext){
        if (values != null){
            outState.putSerializable(Const.STATE_LIST, (Serializable) values);
            outState.putInt(Const.STATE_POSITION, position);
            outState.putParcelable(Const.STATE_ADVANCE, advanceReview);
            outState.putInt(Const.STATE_RATING, currentRating);
            outState.putSerializable(Const.STATE_TYPE, currentRatingType);
            outState.putInt(Const.STATE_PAGE, page);
            outState.putBoolean(Const.STATE_HAS_NEXT,pageHasNext);
        }
    }

    @Override
    public void restoreStateList(Bundle savedInstanceState) {
        Serializable list = savedInstanceState.getSerializable(Const.STATE_LIST);
        int position = savedInstanceState.getInt(Const.STATE_POSITION);
        AdvanceReview advanceReview = savedInstanceState.getParcelable(Const.STATE_ADVANCE);
        int currentRating = savedInstanceState.getInt(Const.STATE_RATING);
        RatingStatsUtils.RatingType currentRatingType
                = (RatingStatsUtils.RatingType) savedInstanceState.getSerializable(Const.STATE_TYPE);
        int page = savedInstanceState.getInt(Const.STATE_PAGE);
        boolean pageHasNext = savedInstanceState.getBoolean(Const.STATE_HAS_NEXT);
        view.onStateResponse((List) list, position, advanceReview, currentRating,
                currentRatingType, page, pageHasNext);
    }

    @Override
    public void getReputationFromCache(String productID, String NAV) {
        facade.getReputationFromCache(productID, NAV, new FacadeProductReviewImpl.GetReputationCacheListener() {
            @Override
            public void onSuccess(JSONObject result) {
                view.onCacheResponse(facade.getModelRatingStats(result),
                        facade.getListReputation(result),
                        facade.getPaging(result));
            }

            @Override
            public void onError(Throwable e) {
                view.onCacheResponse(null, null, null);
            }
        });
    }

    @Override
    public void unsubscribeNetwork() {
        facade.unsubscribeNetwork();
    }

    @Override
    public void reportReview(ActReviewPass pass) {
        view.showLoadingDialog();
        actNetworkInteractor.postReport(view.getActivity(), pass.getReportParam(), new ActReputationRetrofitInteractor.ActReputationListener() {
            @Override
            public void onSuccess(ActResult result) {
                view.dismissLoadingDialog();
                view.showSnackbar(context.getString(R.string.toast_success_report));
            }

            @Override
            public void onTimeout() {
                view.dismissLoadingDialog();
                view.showNetworkErrorSnackbar();
            }

            @Override
            public void onFailAuth() {

            }

            @Override
            public void onThrowable(Throwable e) {

            }

            @Override
            public void onError(String error) {
                view.dismissLoadingDialog();
                view.showSnackbar(error);
            }

            @Override
            public void onNullData() {

            }

            @Override
            public void onNoConnection() {
                view.dismissLoadingDialog();
                view.showNetworkErrorSnackbar();
            }
        });
    }

    @Override
    public void onDestroyView() {
        actNetworkInteractor.unSubscribeObservable();
    }


}
