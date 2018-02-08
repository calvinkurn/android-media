package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ShopService;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;

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

    //    private ShopService shopService;
    private ShopService shopService;
    private TomeService tomeService;
    private Context context;
    private String shopId;
    private String shopDomain;

    private OnGetShopProductListener onGetShopProductListener;

    private Subscription onGetShopProductSubs;

    public GetShopProductRetrofit(Context context, String shopId, String shopDomain) {
        this.context = context;
        this.shopId = shopId;
        this.shopDomain = shopDomain;
        shopService = new ShopService(TkpdBaseURL.ACE_DOMAIN);
        tomeService = new TomeService();
    }

    public void setOnGetShopProductListener(OnGetShopProductListener listener) {
        this.onGetShopProductListener = listener;
    }

    public void getShopProduct(GetShopProductParam param) {
        Observable<Response<TkpdResponse>> observable;
        if (param.isUseAce()) {
            observable = shopService.getApi()
                    .getShopProduct(AuthUtil.generateParams(context, paramGetShopProduct(param)));
        } else {
            observable = tomeService.getApi()
                    .getShopProduct(AuthUtil.generateParams(context, paramGetShopProduct(param)));
        }
        onGetShopProductSubs = observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onGetShopProduct());
    }

    public void unsubscribeGetShopProduct() {
        if (onGetShopProductSubs != null) {
            onGetShopProductSubs.unsubscribe();
        }
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
                if (tkpdResponseResponse.isSuccessful()) {
                    TkpdResponse response = tkpdResponseResponse.body();
                    if (!response.isError()) {
                        ProductModel productModel = new Gson().fromJson(response.getStringData(), ProductModel.class);
                        onGetShopProductListener.onSuccess(productModel);
                    } else {
                        if (response.getErrorMessages() != null && response.getErrorMessages().size() > 0) {
                            onGetShopProductListener.onFailure(WS_TYPE_ERROR, response.getErrorMessages().toString().replace("[", "").replace("]", ""));
                        }
                    }
                } else {
                    onGetShopProductListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
                }
            }
        };
    }

    private Map<String, String> paramGetShopProduct(GetShopProductParam param) {
        Map<String, String> params = new ArrayMap<>();
        params.put("shop_id", shopId);
        params.put("shop_domain", shopDomain);
        params.put("page", String.valueOf(param.getPage()));
        params.put("keyword", param.getKeyword());
        params.put("etalase_id", param.getEtalaseId());
        params.put("order_by", param.getOrderBy());
        params.put("per_page", "12");
        params.put("wholesale", "1");
        return params;
    }

}
