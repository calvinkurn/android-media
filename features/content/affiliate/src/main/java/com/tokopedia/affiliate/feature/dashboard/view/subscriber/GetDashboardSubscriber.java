package com.tokopedia.affiliate.feature.dashboard.view.subscriber;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardHeaderPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardItemPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuotaStatus;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardSubtitlePojo;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;
import com.tokopedia.calendar.SubTitle;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author by yfsx on 19/09/18.
 */
public class GetDashboardSubscriber extends Subscriber<GraphqlResponse> {

    private DashboardContract.View mainView;
    private Integer type;

    public GetDashboardSubscriber(Integer type, DashboardContract.View mainView) {
        this.mainView = mainView;
        this.type = type;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        mainView.hideLoading();
        mainView.onErrorGetDashboardItem(ErrorHandler.getErrorMessage(mainView.getContext(), e));
    }

    @Override
    public void onNext(GraphqlResponse response) {
        mainView.hideLoading();
        DashboardQuery query = response.getData(DashboardQuery.class);
        mainView.onSuccessGetDashboardItem(
                mappingHeader(query.getAffiliateStats()),
                query.getProduct().getAffiliatedProducts() != null ? mappingListItem(mainView.getContext(), type, query.getProduct().getAffiliatedProducts(), query.getProduct().getSubtitles()) : new ArrayList<>(),
                query.getProduct().getPagination().getNextCursor(),
                mappingFloatingItem(query.getPostQuota())
        );
    }

    private DashboardHeaderViewModel mappingHeader(DashboardHeaderPojo pojo) {
        return new DashboardHeaderViewModel(
                "",
                pojo.getTotalCommission(),
                pojo.getProfileView(),
                pojo.getProductClick(),
                pojo.getProductSold(),
                pojo.getProductCount());
    }

    public static List<DashboardItemViewModel> mappingListItem(Context context, Integer type, List<DashboardItemPojo> pojoList, List<DashboardSubtitlePojo> subtitleList) {
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
                    pojo.isIsActive(),
                    pojo.isIsActive() ? activeSection : inactiveSection,
                    (type != null) && (index == 0 || pojoList.get(index-1).isIsActive() != pojo.isIsActive()),
                    type,
                    pojo.getCreatePostAppLink(),
                    pojo.getReviewCount(),
                    pojo.getProductRating());
            itemList.add(item);
        }
        return itemList;
    }

    public static DashboardFloatingButtonViewModel mappingFloatingItem(DashboardQuotaStatus pojo) {
        return new DashboardFloatingButtonViewModel(pojo.getFormatted(), pojo.getNumber());
    }
}
