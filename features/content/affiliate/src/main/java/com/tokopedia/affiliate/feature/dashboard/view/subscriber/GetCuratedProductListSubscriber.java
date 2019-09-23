package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardItemPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardSubtitlePojo;
import com.tokopedia.affiliate.feature.dashboard.view.listener.AffiliateCuratedProductContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetCuratedProductListSubscriber extends Subscriber<GraphqlResponse> {

    private AffiliateCuratedProductContract.View mainView;
    private Integer type;
    private String cursor;

    public GetCuratedProductListSubscriber(Integer type, AffiliateCuratedProductContract.View mainView, String cursor) {
        this.mainView = mainView;
        this.type = type;
        this.cursor = cursor;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.hideLoading();
        mainView.onErrorGetDashboardItem(ErrorHandler.getErrorMessage(mainView.getCtx(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        DashboardQuery query = response.getData(DashboardQuery.class);
        Context context = mainView.getCtx();
        mainView.onSuccessLoadMoreDashboardItem(
                query.getProduct().getAffiliatedProducts() != null && context != null ?
                        mappingListItem(context, type, query.getProduct().getAffiliatedProducts(), query.getProduct().getSubtitles())
                        : new ArrayList<>(),
                query.getProduct().getPagination().getNextCursor());
    }

    private List<DashboardItemViewModel> mappingListItem(Context context, Integer type, List<DashboardItemPojo> pojoList, List<DashboardSubtitlePojo> subtitleList) {
        String activeSection = context.getString(R.string.affiliate_product_berlangsung);
        String inactiveSection = context.getString(R.string.affiliate_product_berakhir);

        for (DashboardSubtitlePojo subtitle: subtitleList) {
            if (subtitle.getKey().equalsIgnoreCase("active")) activeSection = subtitle.getText();
            else if (subtitle.getKey().equalsIgnoreCase("inactive")) inactiveSection = subtitle.getText();
        }

        List<DashboardItemViewModel> itemList = new ArrayList<>();
        for (int index = 0; index < pojoList.size(); index++) {
            DashboardItemPojo pojo = pojoList.get(index);
            DashboardItemViewModel item = new DashboardItemViewModel(
                    pojo.getId(),
                    pojo.getImage(),
                    pojo.getName(),
                    pojo.getCommission(),
                    pojo.getTotalClick(),
                    pojo.getTotalSold(),
                    pojo.getProductCommission() != null ? pojo.getProductCommission() : "",
                    pojo.getCommission() != null ? pojo.getCommission() : "",
                    pojo.isIsActive(),
                    pojo.isIsActive() ? activeSection : inactiveSection,
                    (type != null) &&
                            (
                                    (cursor.equals("") && index == 0) ||
                                    (index != 0 && pojoList.get(index-1).isIsActive() != pojo.isIsActive())
                            ),
                    type,
                    pojo.getCreatePostAppLink(),
                    pojo.getReviewCount(),
                    pojo.getProductRating(),
                    type != null);
            itemList.add(item);
        }
        return itemList;
    }
}
