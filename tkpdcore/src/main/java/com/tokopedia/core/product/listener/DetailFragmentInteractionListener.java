package com.tokopedia.core.product.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.linker.model.LinkerData;

/**
 * @author madi on 5/12/17.
 *         this class is extracted from inner ProductDetailFragment.class
 */

public interface DetailFragmentInteractionListener {

    void shareProductInfo(@NonNull LinkerData shareData);

    void onProductDetailLoaded(@NonNull ProductDetailData productData);

    void onNullResponseData(ProductPass productPass);

    void jumpOtherProductDetail(ProductPass productPass);
}
