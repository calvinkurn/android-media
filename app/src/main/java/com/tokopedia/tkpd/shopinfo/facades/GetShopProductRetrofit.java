package com.tokopedia.tkpd.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.shopinfo.facades.authservices.ShopService;
import com.tokopedia.tkpd.shopinfo.models.GetShopProductParam;
import com.tokopedia.tkpd.shopinfo.models.productmodel.ProductModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tkpd_Eka on 12/8/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class GetShopProductRetrofit {

    public static final int CONNECTION_TYPE_ERROR = 1;
    public static final int WS_TYPE_ERROR = 2;

    public interface OnGetShopProductListener {
        void onSuccess(ProductModel model);

        void onFailure(int connectionTypeError, String message);
    }

    private ShopService shopService;
    private Context context;
    private String shopId;
    private String shopDomain;

    private OnGetShopProductListener onGetShopProductListener;

    private Subscription onGetShopProductSubs;

    public GetShopProductRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        shopService = new ShopService();
    }

    public void setOnGetShopProductListener(OnGetShopProductListener listener) {
        this.onGetShopProductListener = listener;
    }

    public void getShopProduct(GetShopProductParam param) {
        Observable<Response<TkpdResponse>> observable = shopService.getApi().getShopProduct(AuthUtil.generateParams(context, paramGetShopProduct(param)));
        onGetShopProductSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetShopProduct());
    }

    private Observer<Response<TkpdResponse>> onGetShopProduct() {
        return new Observer<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                onGetShopProductListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
            }

            @Override
            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                if(tkpdResponseResponse.isSuccessful()) {
                    TkpdResponse response = tkpdResponseResponse.body();
                    if (!response.isError()) {
                        ProductModel productModel = new Gson().fromJson(response.getStringData(), ProductModel.class);
                        onGetShopProductListener.onSuccess(productModel);
                    }else{
                        if(response.getErrorMessages() != null && response.getErrorMessages().size() >0)
                        {
                            onGetShopProductListener.onFailure(WS_TYPE_ERROR, response.getErrorMessages().toString().replace("[", "").replace("]", ""));
                        }
                    }
                }else{
                    onGetShopProductListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
                }
            }
        };
    }

    private Map<String, String> paramGetShopProduct(GetShopProductParam param) {
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        params.put("page", Integer.toString(param.page));
        params.put("keyword", param.keyword);
        params.put("etalase_id", param.etalaseId);
        params.put("order_by", param.orderBy);
        params.put("per_page", "12");
        params.put("wholesale", "1");
        return params;
    }

}
