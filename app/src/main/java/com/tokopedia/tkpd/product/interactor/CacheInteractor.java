package com.tokopedia.tkpd.product.interactor;

import com.tokopedia.tkpd.product.dialog.ReportProductDialogFragment;
import com.tokopedia.tkpd.product.listener.ReportProductDialogView;
import com.tokopedia.tkpd.product.model.productdetail.ProductDetailData;
import com.tokopedia.tkpd.product.model.productother.ProductOther;
import com.tokopedia.tkpd.product.model.productotherace.ProductOtherDataAce;

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

    interface GetProductDetailCacheListener {
        void onSuccess(ProductDetailData productDetailData);

        void onError(Throwable e);
    }

    interface ProductOtherCacheListener {
        void onSuccess(List<ProductOther> data);

        void onError(Throwable e);
    }
}
