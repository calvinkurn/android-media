package com.tokopedia.core.product.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.listener.ReportProductDialogView;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.core.product.model.productdink.ProductDinkData;
import com.tokopedia.core.product.model.productother.ProductOther;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

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
}
