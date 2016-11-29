package com.tokopedia.core.product.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.CategoryDB_Table;
import com.tokopedia.core.myproduct.service.ProductService;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.fragment.ProductDetailFragment;
import com.tokopedia.core.product.listener.ProductInfoView;
import com.tokopedia.core.product.model.passdata.ProductPass;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.share.fragment.ProductShareFragment;

import java.util.List;

/**
 * Created by Angga.Prasetiyo on 09/11/2015.
 */
public class ProductInfoPresenterImpl implements ProductInfoPresenter {
    private static final String TAG = ProductInfoPresenterImpl.class.getSimpleName();

    private final ProductInfoView viewListener;

    public ProductInfoPresenterImpl(ProductInfoView viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initialFragment(@NonNull Context context, Uri uri, Bundle bundle) {
        // [variable for add product before share]
        int type = bundle.getInt(ProductService.TYPE, -1);
        long productId = bundle.getLong(ProductService.PRODUCT_DATABASE_ID, -1);
        String stockStatus = bundle.getString(ProductService.STOCK_STATUS,"");
        // [variable for add product before share]
        ShareData shareDa = bundle.getParcelable(ProductInfoActivity.SHARE_DATA);

        // [variable for add product before share]
        if(type != -1 && productId != -1 && !stockStatus.equals("")){
            viewListener.inflateFragment(ProductShareFragment.newInstance(type, productId, stockStatus), ProductShareFragment.TAG);
        // [variable for add product before share]
        }else if(shareDa !=null){
            viewListener.inflateFragment(ProductShareFragment.newInstance(shareDa), ProductShareFragment.TAG);
        }else if (isProductDetail(uri, bundle)) {
            viewListener.inflateFragment(ProductDetailFragment
                            .newInstance(generateProductPass(bundle, uri)),
                    ProductDetailFragment.class.getSimpleName());
        } else {
            List<String> uriSegments = uri.getPathSegments();
            String iden = uriSegments.get(1);
            for (int i = 2; i < uriSegments.size(); i++) {
                iden = iden + "_" + uriSegments.get(i);
            }
            CommonUtils.dumper(iden);
            CategoryDB dep =
                    new Select().from(CategoryDB.class)
                            .where(CategoryDB_Table.categoryIdentifier.eq(iden))
                            .querySingle();
            String dep_id = dep.getDepartmentId()+"";
            Intent moveIntent = BrowseProductRouter.getDefaultBrowseIntent(context);
            moveIntent.putExtra("d_id", dep_id);
            viewListener.navigateToActivity(moveIntent);
        }
    }

    public void processToShareProduct(Context context, @NonNull ShareData shareData) {
        UnifyTracking.eventShareProduct();
    }

    @Override
    public void setLocalyticFlow(@NonNull Context context) {
        String screenName = context.getString(R.string.product_info_page);
        ScreenTracking.screenLoca(screenName);

    }

    private ProductPass generateProductPass(Bundle bundleData, Uri uriData) {
        ProductPass productPass;
        if (bundleData != null) {
            productPass = bundleData.getParcelable(ProductInfoActivity.EXTRA_PRODUCT_PASS);
            if (productPass == null) {
                productPass = ProductPass.Builder.aProductPass()
                        .setProductId(bundleData.getString("product_id", ""))
                        .setProductName(bundleData.getString("product_key", ""))
                        .setProductPrice(bundleData.getString("product_price", ""))
                        .setShopDomain(bundleData.getString("shop_domain", ""))
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

    private boolean isProductDetail(Uri uriData, Bundle bundleData) {
        if (uriData != null & bundleData == null) {
            List<String> uriSegments = uriData.getPathSegments();
            return !uriSegments.get(0).equals("p");
        } else {
            return true;
        }
    }
}
