package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.data.ProductAd;
import com.tokopedia.topads.dashboard.data.model.data.ProductAdBulkAction;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.topads.dashboard.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchAdRequest;
import com.tokopedia.topads.dashboard.data.model.request.SearchProductRequest;
import com.tokopedia.topads.dashboard.data.model.request.ShopRequest;
import com.tokopedia.topads.dashboard.data.model.request.StatisticRequest;
import com.tokopedia.product.manage.item.common.data.source.cloud.DataResponse;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/4/16.
 */
@Deprecated
public interface DashboardTopadsInteractor {

    void getDeposit(ShopRequest shopRequest, final ListenerInteractor<DataDeposit> listener);

    DataDeposit getDeposit(String shopId);

    void getShopInfo(ShopRequest shopRequest, final ListenerInteractor<ShopModel> listener);

    void searchProduct(SearchProductRequest searchProductRequest, final ListenerInteractor<List<Product>> listener);

    void unSubscribe();

    void getListProductAds(HashMap<String, String> params, ListenerInteractor<PageDataResponse<List<ProductAd>>> listener);

    void getListGroupAds(SearchAdRequest searchAdRequest, ListenerInteractor<List<GroupAd>> listener);

    void getDashboardResponse(HashMap<String, String> params, ListenerInteractor<DataResponse<DataDeposit>> listener);

    void actionSingleAds(DataRequest<ProductAdBulkAction> dataRequest, ListenerInteractor<ProductAdBulkAction> listenerInteractor);

    void actionGroupAds(DataRequest<GroupAdBulkAction> dataRequest, ListenerInteractor<GroupAdBulkAction> listenerInteractor);
}