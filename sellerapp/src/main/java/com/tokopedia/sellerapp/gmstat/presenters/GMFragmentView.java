package com.tokopedia.sellerapp.gmstat.presenters;

import com.tokopedia.core.discovery.dynamicfilter.facade.models.HadesV1Model;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;
import com.tokopedia.sellerapp.gmstat.models.GetPopularProduct;
import com.tokopedia.sellerapp.gmstat.models.GetProductGraph;
import com.tokopedia.sellerapp.gmstat.models.GetShopCategory;
import com.tokopedia.sellerapp.gmstat.models.GetTransactionGraph;

import java.util.List;

/**
 * Created by normansyahputa on 1/2/17.
 */

public interface GMFragmentView {
    void onSuccessGetShopCategory(GetShopCategory getShopCategory);
    void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph, long sDate, long eDate, int lastSelectionPeriod, int selectionType);
    void onSuccessProductnGraph(GetProductGraph getProductGraph, boolean isFirstTime);
    void onSuccessPopularProduct(GetPopularProduct getPopularProduct);
    void onSuccessBuyerData(GetBuyerData getBuyerData);
    void onSuccessGetKeyword(List<GetKeyword> getKeywords);
    void onSuccessGetCategory(List<HadesV1Model> hadesV1Models);
    void onComplete();
    void onError(Throwable e);
    void onFailure();
    void fetchData();
    void fetchData(long sDate, long eDate, int lastSelectionPeriod, int selectionType);
    void resetToLoading();
    void bindHeader(long sDate, long eDate, int lastSelectionPeriod, int selectionType);
}
