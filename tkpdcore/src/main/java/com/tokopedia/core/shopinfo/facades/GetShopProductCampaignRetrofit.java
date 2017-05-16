package com.tokopedia.core.shopinfo.facades;

import android.content.Context;

import com.google.gson.JsonObject;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductCampaign;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductCampaignResponse;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nakama on 15/05/17.
 */

public class GetShopProductCampaignRetrofit {

    public static final int CONNECTION_TYPE_ERROR = 1;
    public static final int WS_TYPE_ERROR = 2;

    private MojitoService mojitoService;
    private Context context;
    private ProductModel productModel;

    private ProductsCampaignListener productsCampaignListener;

    private Subscription onGetProductsCampaignSubs;

    public interface ProductsCampaignListener {
        void onSuccess(ProductModel model);

        void onFailure(int connectionTypeError, String message);
    }

    public GetShopProductCampaignRetrofit(Context context) {
        this.context = context;
        mojitoService = new MojitoService();
    }

    public void setProductsCampaignListener(ProductsCampaignListener productsCampaignListener) {
        this.productsCampaignListener = productsCampaignListener;
    }


    public void getProductsCampaign(ProductModel model, List<String> ids) {
        this.productModel = model;
        Observable<Response<ShopProductCampaignResponse>> observable = mojitoService.getApi()
                .getProductsCampaign(StringUtils.convertListToStringDelimiter(ids, ","));
        onGetProductsCampaignSubs = observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onGetProductCampaign());
    }

    public void unsubscribeGetProductsCampaign() {
        if(onGetProductsCampaignSubs != null) {
            onGetProductsCampaignSubs.unsubscribe();
        }
    }


    private Observer<Response<ShopProductCampaignResponse>> onGetProductCampaign() {
        return new Observer<Response<ShopProductCampaignResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Overridecd
            public void onError(Throwable e) {
                productsCampaignListener.onSuccess(productModel);
            }

            @Override
            public void onNext(Response<ShopProductCampaignResponse> tkpdResponse) {
                if(tkpdResponse.isSuccessful()) {
                    ShopProductCampaignResponse response = tkpdResponse.body();
                    // TODO: 16/05/17  
                }

                productsCampaignListener.onSuccess(productModel);
            }
        };
    }
}
