package com.tokopedia.opportunity.snapshot.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.opportunity.snapshot.SnapShotProduct;
import com.tokopedia.opportunity.snapshot.fragment.SnapShotFragment;
import com.tokopedia.opportunity.snapshot.listener.SnapShotProductListener;
import com.tokopedia.core.var.ProductItem;

import java.util.List;

/**
 * Created by hangnadi on 3/1/17.
 */
public class SnapShotProductImpl implements SnapShotProductPresenter {
    private final SnapShotProductListener viewListener;

    public SnapShotProductImpl(SnapShotProductListener listener) {
        this.viewListener = listener;
    }

    @Override
    public void initialFragment(Context context, Uri uriData, Bundle bundleData) {
        viewListener.inflateFragment(SnapShotFragment
                        .newInstance(generateProductPass(bundleData, uriData),
                                getOpportunityId(bundleData)),
                SnapShotFragment.class.getSimpleName());
    }

    private String getOpportunityId(Bundle bundleData) {
        return bundleData.getString(SnapShotProduct.PARAM_OPPORTUNITY_ID, "");
    }

    private ProductPass generateProductPass(Bundle bundleData, Uri uriData) {
        ProductPass productPass;
        if (bundleData != null) {
            productPass = bundleData.getParcelable(ProductDetailRouter.EXTRA_PRODUCT_PASS);
            ProductItem productItem = bundleData
                    .getParcelable(ProductDetailRouter.EXTRA_PRODUCT_ITEM);
            if (productPass == null && productItem == null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductId(bundleData.getString("product_id", ""))
                        .setProductName(bundleData.getString("product_key", ""))
                        .setProductPrice(bundleData.getString("product_price", ""))
                        .setShopDomain(bundleData.getString("shop_domain", ""))
                        .build();
            } else if (productItem != null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductPrice(productItem.getPrice())
                        .setProductId(productItem.getId())
                        .setProductName(productItem.getName())
                        .setProductImage(productItem.getImgUri())
                        .build();
            }
        } else {
            List<String> uriSegments = uriData.getPathSegments();
            String prodName = "";
            String shopDomain = "";
            if (uriSegments.size() >= 2) {
                prodName = uriSegments.get(1);
                shopDomain = uriSegments.get(0);
            }
            productPass = ProductPass.Builder.aProductPass()
                    .setProductName(prodName)
                    .setShopDomain(shopDomain)
                    .build();
        }
        return productPass;
    }
}
