package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductItem;
import com.tokopedia.core.product.model.goldmerchant.FeaturedProductServiceModel;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by HenryPri on 16/06/17.
 */

public class GetFeaturedProductRetrofit {
    public static final int CONNECTION_TYPE_ERROR = 1;
    public static final int WS_TYPE_ERROR = 2;

    public interface OnGetFeaturedProductListener {
        void onSuccess(List<FeaturedProductItem> featuredProductItemList);

        void onFailure(int connectionTypeError, String message);
    }

    private GoldMerchantService goldMerchantService;
    private Context context;
    private String shopId;

    private GetFeaturedProductRetrofit.OnGetFeaturedProductListener onGetFeaturedProductListener;

    private Subscription onGetFeaturedProductSubs;

    public GetFeaturedProductRetrofit(Context context, String shopId) {
        this.context = context;
        this.shopId = shopId;
        goldMerchantService = new GoldMerchantService();
    }

    public void setOnGetFeaturedProductListener(OnGetFeaturedProductListener onGetFeaturedProductListener) {
        this.onGetFeaturedProductListener = onGetFeaturedProductListener;
    }

    public void getFeaturedProduct() {
        onGetFeaturedProductSubs = goldMerchantService.getApi().getShopFeaturedProducts(shopId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onGetFeaturedProduct());
    }

    private Observer<Response<FeaturedProductServiceModel>> onGetFeaturedProduct() {
        return new Observer<Response<FeaturedProductServiceModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (onGetFeaturedProductListener != null) {
                    onGetFeaturedProductListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
                }
            }

            @Override
            public void onNext(Response<FeaturedProductServiceModel> serviceResponse) {
                if (onGetFeaturedProductListener == null) {
                    return;
                }

                if (serviceResponse.isSuccessful()) {
                    List<FeaturedProductItem> featuredProductItemList = serviceResponse.body().getItemList();
                    onGetFeaturedProductListener.onSuccess(featuredProductItemList);
                } else {
                    onGetFeaturedProductListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
                }
            }
        };
    }

    public void unsubscribeGetFeaturedProduct() {
        if (onGetFeaturedProductSubs != null) {
            onGetFeaturedProductSubs.unsubscribe();
        }
    }
}
