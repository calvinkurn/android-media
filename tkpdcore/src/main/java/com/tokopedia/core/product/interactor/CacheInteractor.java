package com.tokopedia.core.product.interactor;

import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productother.ProductOther;

import java.util.List;

/**
 * Created by ricoharisin on 12/3/15.
 */
public interface CacheInteractor {

    void getProductDetailCache(String productID, GetProductDetailCacheListener listener);

    void storeProductDetailCache(String productID, ProductDetailData productDetailData);

    void deleteProductDetail(Integer productId);

    void getOtherProductCache(String productId, ProductOtherCacheListener listener);

    void storeOtherProductCache(String productId, List<ProductOther> productOthers);

    void loadReportTypeFromCache(ReportProductDialogView viewListener);

    void saveReportTypeToCache(String data);

    void getPromoWidgetCache(String targetType, String userId, String shopType, GetPromoWidgetCacheListener listener);

    void storePromoWidget(String targetType, String userId, String shopType, DataPromoWidget dataPromoWidget);

    interface GetProductDetailCacheListener {
        void onSuccess(ProductDetailData productDetailData);

        void onError(Throwable e);
    }

    interface ProductOtherCacheListener {
        void onSuccess(List<ProductOther> data);

        void onError(Throwable e);
    }

    interface GetPromoWidgetCacheListener {
        void onSuccess(PromoAttributes result);

        void onError();
    }
}
