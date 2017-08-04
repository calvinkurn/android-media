package com.tokopedia.core.shopinfo.facades;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.tokopedia.core.R;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.Sort;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
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
 * @author by errysuprayogi on 7/26/17.
 */

public class GetSortRetrofit {

    public static final int CONNECTION_TYPE_ERROR = 1;
    public static final int WS_TYPE_ERROR = 2;
    public static final String DEVICE = "device";
    public static final String SOURCE = "source";
    public static final String DEFAULT_DEVICE = "android";
    public static final String SHOP_PRODUCT = "shop_product";


    public interface OnGetSortFilterListener {
        void onSuccess(List<Sort> sorts);

        void onFailure(int connectionTypeError, String message);
    }

    private SortService sortService;
    private Context context;

    private Subscription onGetShopProductSubs;

    public GetSortRetrofit(Context context) {
        this.context = context;
        sortService = new SortService();
    }

    public void getSort(OnGetSortFilterListener sortFilterListener) {
        Observable<Response<DynamicFilterModel>> observable = sortService.getApi().getDynamicFilter(paramSortProduct());
        onGetShopProductSubs = observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(onGetSortFilter(sortFilterListener));
    }

    public void unsubscribeGetSortFilter() {
        if (onGetShopProductSubs != null) {
            onGetShopProductSubs.unsubscribe();
        }
    }

    private Observer<Response<DynamicFilterModel>> onGetSortFilter(final OnGetSortFilterListener filterListener) {
        return new Observer<Response<DynamicFilterModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                filterListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
            }

            @Override
            public void onNext(Response<DynamicFilterModel> filterModelResponse) {
                if (filterModelResponse.isSuccessful()) {
                    DynamicFilterModel response = filterModelResponse.body();
                    if(response.getData()!=null && response.getData().getSort() != null){
                        filterListener.onSuccess(response.getData().getSort());
                    } else {
                        filterListener.onFailure(WS_TYPE_ERROR, context.getString(R.string.error_data_not_found));
                    }
                } else {
                    filterListener.onFailure(CONNECTION_TYPE_ERROR, context.getString(R.string.error_connection_problem));
                }
            }
        };
    }

    private Map<String, String> paramSortProduct() {
        Map<String, String> params = new ArrayMap<>();
        params.put(DEVICE, DEFAULT_DEVICE);
        params.put(SOURCE, SHOP_PRODUCT);
        return params;
    }

}
