package com.tokopedia.core.product.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.entity.variant.Child;
import com.tokopedia.core.network.entity.variant.ProductVariant;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.core.product.model.productdetail.discussion.LatestTalkViewModel;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdink.ProductDinkData;
import com.tokopedia.core.product.model.productother.ProductOther;

import java.util.List;
import java.util.Map;

/**
 * RetrofitInteractor
 * Created by Angga.Prasetiyo on 02/12/2015.
 */
public interface RetrofitInteractor {

    void getProductDetail(@NonNull Context context, @NonNull TKPDMapParam<String, String> paramNetwork,
                          @NonNull ProductDetailListener listener);

    void getOtherProducts(@NonNull Context context, @NonNull Map<String, String> params,
                          @NonNull OtherProductListener listener);

    void promoteProduct(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull PromoteProductListener listener);

    void moveToWarehouse(@NonNull Context context, @NonNull Map<String, String> params,
                         @NonNull ToWarehouseListener listener);

    void getShopEtalase(@NonNull Context context, @NonNull Map<String, String> params,
                        @NonNull GetEtalaseListener listener);

    void moveToEtalase(@NonNull Context context, @NonNull Map<String, String> params,
                       @NonNull ToEtalaseListener listener);

    void favoriteShop(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull FaveListener listener);

    void addToWishList(@NonNull Context context, @NonNull Integer params,
                       @NonNull AddWishListListener listener);

    void removeFromWishList(@NonNull Context context, @NonNull Integer params,
                            @NonNull RemoveWishListListener listener);

    void getProductManagePermissions(@NonNull Context context, @NonNull Map<String, String> params,
                                     @NonNull ProductManagePermissionListener listener);

    void requestProductVideo(@NonNull Context context, @NonNull String productId,
                             @NonNull VideoLoadedListener listener);

    void unSubscribeObservable();

    void downloadReportType(Context context, Integer productId, ReportProductDialogView viewListener);

    void getProductVariant(@NonNull Context context, @NonNull String productId,
                              @NonNull ProductVariantListener listener);

    void getProductStock(@NonNull Context context, @NonNull String productId,
                           @NonNull ProductStockListener listener);

    void updateRecentView(@NonNull Context context, @NonNull String productId);

    void getPromo(@NonNull Context context, @NonNull String targetType, @NonNull String userId,
                  @NonNull String shopType, @NonNull PromoListener listener);

    void getMostHelpfulReview(@NonNull Context context, @NonNull String productId,
                              @NonNull String shopId, @NonNull MostHelpfulListener listener);

    void getProductDiscussion(@NonNull Context context, @NonNull String productId, @NonNull String shopId,
                              @NonNull DiscussionListener listener);

    void getProductTalkComment(@NonNull Context context, @NonNull String talkId, @NonNull String shopId,
                              @NonNull DiscussionListener listener);

    void checkPromoAds(String shopId, int itemId, String userId, CheckPromoAdsListener listener);

    interface ProductDetailListener {

        void onSuccess(@NonNull ProductDetailData data);

        void onTimeout();

        void onError(String error);

        void onNullData();

        void onReportServerProblem();
    }

    interface OtherProductListener {

        void onSuccess(@NonNull List<ProductOther> datas);

        void onTimeout(String message);

        void onError(String error);
    }

    interface PromoteProductListener {
        void onSuccess(ProductDinkData data);

        void onError(String error);
    }

    interface ToEtalaseListener {

        void onSuccess(boolean success);

        void onError(String error);
    }

    interface ToWarehouseListener {

        void onSuccess(boolean success);

        void onError(String error);
    }

    interface GetEtalaseListener {

        void onSuccess(List<Etalase> etalases);

        void onError(String error);
    }

    interface FaveListener {

        void onSuccess(boolean status);

        void onError(String error);
    }

    interface AddWishListListener {

        void onSuccess();

        void onError(String error);
    }

    interface RemoveWishListListener {

        void onSuccess();

        void onError(String error);
    }

    interface ProductManagePermissionListener {

        void onSuccess(String productManager);

        void onError();
    }

    interface VideoLoadedListener {

        void onSuccess(@NonNull VideoData data);

        void onError();
    }

    interface ProductVariantListener {

        void onSucccess(ProductVariant productVariant);

        void onError(String error);
    }

    interface ProductStockListener {

        void onSucccess(Child productStock);

        void onError(String error);
    }

    interface PromoListener {

        void onSucccess(DataPromoWidget dataPromoWidget);

        void onError(String error);
    }

    interface MostHelpfulListener {

        void onSucccess(List<Review> reviews);

        void onError(String error);
    }

    interface DiscussionListener {

        void onSucccess(LatestTalkViewModel discussion);

        void onError(String error);
    }

    interface CheckPromoAdsListener {
        void onSuccess(String adsId);

        void onError(String error);
    }
}
