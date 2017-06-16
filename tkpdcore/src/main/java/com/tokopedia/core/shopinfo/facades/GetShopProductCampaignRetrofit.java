package com.tokopedia.core.shopinfo.facades;

import android.content.Context;

import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductCampaign;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductCampaignResponse;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

import java.util.Iterator;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by brilliant.oka on 15/05/17.
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


    public void getProductsCampaign(ProductModel model) {
        this.productModel = model;

        Observable<Response<ShopProductCampaignResponse>> observable = mojitoService.getApi()
                .getProductCampaigns(getIds(model));
        onGetProductsCampaignSubs = observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onGetProductCampaign());
    }

    private String getIds(ProductModel model) {
        StringBuilder builder = new StringBuilder();
        String delimiter = ",";
        Iterator<com.tokopedia.core.shopinfo.models.productmodel.List> it = model.list.iterator();
        while (it.hasNext()) {
            builder.append(it.next().productId);
            if (it.hasNext()) {
                builder.append(delimiter);
            }
        }

        return builder.toString();
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

            @Override
            public void onError(Throwable e) {
                productsCampaignListener.onSuccess(productModel);
            }

            @Override
            public void onNext(Response<ShopProductCampaignResponse> tkpdResponse) {
                if(tkpdResponse.isSuccessful()) {
                    ShopProductCampaignResponse response = tkpdResponse.body();
                    if(response.getData() != null) {
                        for(ShopProductCampaign productCampaign : response.getData()) {
                            for(com.tokopedia.core.shopinfo.models.productmodel.List list : productModel.list) {
                                if(list.productId == productCampaign.getProductId()) {
                                    list.shopProductCampaign = productCampaign;
                                    break;
                                }
                            }
                        }
                    }
                }

                productsCampaignListener.onSuccess(productModel);
            }
        };
    }
}
