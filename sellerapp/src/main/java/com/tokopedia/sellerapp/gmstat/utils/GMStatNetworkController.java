package com.tokopedia.sellerapp.gmstat.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.sellerapp.BuildConfig;
import com.tokopedia.sellerapp.gmstat.apis.GMStatApi;
import com.tokopedia.sellerapp.gmstat.models.GetBuyerData;
import com.tokopedia.sellerapp.gmstat.models.GetKeyword;
import com.tokopedia.sellerapp.gmstat.models.GetPopularProduct;
import com.tokopedia.sellerapp.gmstat.models.GetProductGraph;
import com.tokopedia.sellerapp.gmstat.models.GetShopCategory;
import com.tokopedia.sellerapp.gmstat.models.GetTransactionGraph;
import com.tokopedia.sellerapp.home.utils.BaseNetworkController;
import com.tokopedia.sellerapp.home.utils.ShopNetworkController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.sellerapp.home.utils.ShopNetworkController.onResponseError;

/**
 * Created by normansyahputa on 11/2/16.
 * <p>https://phab.tokopedia.com/w/api/krabs/shopstatistic/</> for more detail
 */

public class GMStatNetworkController extends BaseNetworkController {

    private final static String TAG = "GMStatNetworkController";

    private final static long DEFAULT_SDATE = Long.MAX_VALUE;
    private final static long DEFAULT_EDATE = Long.MIN_VALUE;

    private GMStatApi gmstatApi;

    public GMStatNetworkController(Context context, Gson gson, GMStatApi gmStatApi) {
        super(context, gson);
        this.gmstatApi = gmStatApi;
    }

    public Observable<Response<GetProductGraph>> getProductGraph(
            long shopId
    ){
        return getProductGraph(shopId, DEFAULT_SDATE, DEFAULT_EDATE);
    }

    public Observable<Response<GetProductGraph>> getProductGraph(
            long shopId, long sDate, long eDate
    ){
        return gmstatApi.getProductGraph(
            getGMStatParam(shopId, sDate, eDate));
    }

    public Observable<Response<GetTransactionGraph>> getTransactionGraph(
            long shopId, long sDate, long eDate
    ){
        return gmstatApi.getTransactionGraph(
                getGMStatParam(shopId, sDate, eDate));
    }

    public Observable<Response<GetTransactionGraph>> getTransactionGraph(
            long shopId
    ){
        return getTransactionGraph(shopId, DEFAULT_SDATE, DEFAULT_EDATE);
    }

    private static Response<?> getError1000(){
        Response<Object> error = Response.error(1000, new ResponseBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public long contentLength() {
                return 0;
            }

            @Override
            public BufferedSource source() {
                return null;
            }
        });
        return  error;
    }

    public Observable<Response<GetPopularProduct>> getPopularProduct(long shopId, long sDate, long eDate){
        return gmstatApi.getPopulatProduct(getGMStatParam(shopId, sDate, eDate));
    }

    public Observable<Response<GetPopularProduct>> getPopularProduct(long shopId){
        return gmstatApi.getPopulatProduct(getGMStatParam(shopId, DEFAULT_SDATE, DEFAULT_EDATE));
    }

    public Observable<Response<GetBuyerData>> getBuyerData(long shopId, long sDate, long eDate){
        return gmstatApi.getBuyerData(
                getGMStatParam(shopId, sDate, eDate));
    }

    public Observable<Response<GetBuyerData>> getBuyerData(long shopId){
        return gmstatApi.getBuyerData(
                getGMStatParam(shopId, DEFAULT_SDATE, DEFAULT_EDATE));
    }

    public void getBuyerData(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                getBuyerData(shopId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<Response<GetBuyerData>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Response<GetBuyerData> getBuyerData) {
                                        if (getBuyerData.isSuccessful()) {
                                            GetBuyerData body = getBuyerData.body();
                                            getGMStat.onSuccessBuyerData(body);
                                        } else {
                                            onResponseError(getBuyerData.code(), getGMStat);
                                        }
                                    }
                                }
                        )
        );
    }

    public void getPopularProduct(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                getPopularProduct(shopId).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<Response<GetPopularProduct>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Response<GetPopularProduct> getPopularProductResponse) {
                                        if (getPopularProductResponse.isSuccessful()) {
                                            GetPopularProduct body = getPopularProductResponse.body();
                                            getGMStat.onSuccessPopularProduct(body);
                                        } else {
                                            onResponseError(getPopularProductResponse.code(), getGMStat);
                                        }
                                    }
                                }
                        )
        );
    }

    public Observable<Response<GetShopCategory>> getShopCategory(long shopId){
        return gmstatApi.getShopCategory(getGMStatParam(shopId, DEFAULT_SDATE, DEFAULT_EDATE));
    }

    public void getShopCategory(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                getShopCategory(shopId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<Response<GetShopCategory>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Response<GetShopCategory> response) {
                                        if(response.isSuccessful()){
                                            GetShopCategory body = response.body();
                                            getGMStat.onSuccessGetShopCategory(body);
                                        }else{
                                            onResponseError(response.code(), getGMStat);
                                        }
                                    }
                                }
                        )
        );
    }

    public Observable<KeywordModel> getKeywordModels(final long shopId){

        return getShopCategory(shopId)
                .flatMap(new Func1<Response<GetShopCategory>, Observable<KeywordModel>>() {
                    @Override
                    public Observable<KeywordModel> call(Response<GetShopCategory> response) {
                        if(response.isSuccessful()){
                            KeywordModel keywordModel =
                                    new KeywordModel();
                            keywordModel.getShopCategory = response.body();
                            keywordModel.getShopCategoryResponse = response;

                            Observable<List<Response<GetKeyword>>> getKeywords
                                    = Observable.from(response.body().getShopCategory())
                                    .flatMap(new Func1<Integer, Observable<Response<GetKeyword>>>() {
                                        @Override
                                        public Observable<Response<GetKeyword>> call(Integer catId) {
                                            Observable<Response<GetKeyword>> keyword
                                                    = getKeyword(shopId, catId);
                                            return keyword;
                                        }
                                    })
                                    .toList();

                            return Observable.zip(getKeywords, Observable.just(keywordModel), new Func2<List<Response<GetKeyword>>, KeywordModel, KeywordModel>() {
                                @Override
                                public KeywordModel call(List<Response<GetKeyword>> responses, KeywordModel keywordModel) {
                                    keywordModel.getKeywords = new ArrayList<GetKeyword>();
                                    for (Response<GetKeyword> response : responses) {
                                        if(response.isSuccessful()) {
                                            keywordModel.getKeywords.add(response.body());
                                        }
                                    }

                                    keywordModel.getResponseList = responses;

                                    return keywordModel;
                                }
                            });
                        }else{
                            throw new RuntimeException("failed");
                        }
                    }
                });
    }

    public Observable<Response<GetKeyword>> getKeyword(long shopId, long catId){
        Map<String, String> param = new HashMap<>();
        param.put("shop_id", shopId+"");
        param.put("cat_id", catId+"");

        return gmstatApi.getKeyword(param);
    }


    public static class KeywordModel{
        public List<GetKeyword> getKeywords;
        public GetShopCategory getShopCategory;

        public Response<GetShopCategory> getShopCategoryResponse;
        public List<Response<GetKeyword>> getResponseList;
    }

    public void fetchData(final GetGMStat getGMStat, AssetManager assetManager){

        GetProductGraph body = gson.fromJson(readJson("product_graph.json", assetManager), GetProductGraph.class);
        getGMStat.onSuccessProductnGraph(body);

        GetTransactionGraph body2 = gson.fromJson(readJson("transaction_graph.json", assetManager), GetTransactionGraph.class);
        getGMStat.onSuccessTransactionGraph(body2);


        GetPopularProduct body3 = gson.fromJson(readJson("popular_product.json", assetManager), GetPopularProduct.class);
        getGMStat.onSuccessPopularProduct(body3);


        GetBuyerData body4 = gson.fromJson(readJson("buyer_graph.json", assetManager), GetBuyerData.class);
        getGMStat.onSuccessBuyerData(body4);


        KeywordModel keywordModel = new KeywordModel();
        keywordModel.getShopCategory = gson.fromJson(readJson("shop_category.json", assetManager), GetShopCategory.class);
        List<GetKeyword> getKeywords = new ArrayList<>();
        getKeywords.add(gson.fromJson(readJson("search_keyword.json", assetManager), GetKeyword.class));
        keywordModel.getKeywords = getKeywords;
        GetShopCategory getShopCategory = keywordModel.getShopCategory;

        getGMStat.onSuccessGetShopCategory(getShopCategory);
        getGMStat.onSuccessGetKeyword(getKeywords);
    }

    private String readJson(String fileName, AssetManager assetManager){
        BufferedReader reader = null;
        String total ="";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                total += mLine;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
            Log.i("MNORMANSYAH", total);
            return total;
        }
    }

    public void fetchData(long shopId, long sDate, long eDate, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                Observable.concat(
                        getProductGraph(shopId, sDate, eDate).onErrorResumeNext(
                                new Func1<Throwable, Observable<? extends Response<GetProductGraph>>>() {
                                    @Override
                                    public Observable<? extends Response<GetProductGraph>> call(Throwable throwable) {
                                        Response<GetProductGraph> response = (Response<GetProductGraph>) getError1000();
                                        return Observable.just(response);
                                    }
                                }
                        ),
                        getTransactionGraph(shopId, sDate, eDate).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetTransactionGraph>>>() {
                            @Override
                            public Observable<? extends Response<GetTransactionGraph>> call(Throwable throwable) {
                                Response<GetTransactionGraph> error1000 = (Response<GetTransactionGraph>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getPopularProduct(shopId, sDate, eDate).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetPopularProduct>>>() {
                            @Override
                            public Observable<? extends Response<GetPopularProduct>> call(Throwable throwable) {
                                Response<GetPopularProduct> error1000 = (Response<GetPopularProduct>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getBuyerData(shopId, sDate, eDate).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetBuyerData>>>() {
                            @Override
                            public Observable<? extends Response<GetBuyerData>> call(Throwable throwable) {
                                Response<GetBuyerData> error1000 = (Response<GetBuyerData>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getKeywordModels(shopId)
                )
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<List<Object>>() {
                                    @Override
                                    public void onCompleted() {
                                        getGMStat.onComplete();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if(BuildConfig.DEBUG){
                                            Log.e(TAG, "error : "+e);
                                        }
                                    }

                                    @Override
                                    public void onNext(List<Object> responses) {
                                        Response<GetProductGraph> getProductGraphResponse =
                                                (Response<GetProductGraph>) responses.get(0);
                                        if (getProductGraphResponse.isSuccessful()) {
                                            GetProductGraph body = getProductGraphResponse.body();
                                            getGMStat.onSuccessProductnGraph(body);
                                        } else {
                                            onResponseError(getProductGraphResponse.code(), getGMStat);
                                        }

                                        Response<GetTransactionGraph> getTransactionGraphResponse =
                                                (Response<GetTransactionGraph>) responses.get(1);
                                        if (getTransactionGraphResponse.isSuccessful()) {
                                            GetTransactionGraph body = getTransactionGraphResponse.body();
                                            getGMStat.onSuccessTransactionGraph(body);
                                        } else {
                                            onResponseError(getTransactionGraphResponse.code(), getGMStat);
                                        }

                                        Response<GetPopularProduct> getPopularProductResponse =
                                                (Response<GetPopularProduct>) responses.get(2);

                                        if (getPopularProductResponse.isSuccessful()) {
                                            GetPopularProduct body = getPopularProductResponse.body();
                                            getGMStat.onSuccessPopularProduct(body);
                                        } else {
                                            onResponseError(getPopularProductResponse.code(), getGMStat);
                                        }

                                        Response<GetBuyerData> response =
                                                (Response<GetBuyerData>) responses.get(3);
                                        if(response.isSuccessful()){
                                            GetBuyerData body = response.body();
                                            getGMStat.onSuccessBuyerData(body);
                                        }else{
                                            onResponseError(response.code(), getGMStat);
                                        }

                                        KeywordModel keywordModel = (KeywordModel) responses.get(4);
                                        GetShopCategory getShopCategory = keywordModel.getShopCategory;
                                        getGMStat.onSuccessGetShopCategory(getShopCategory);

                                        List<GetKeyword> getKeywords = keywordModel.getKeywords;
                                        getGMStat.onSuccessGetKeyword(getKeywords);
                                    }
                                }
                        )
        );
    }

    public void fetchData(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat ){
        compositeSubscription.add(
                Observable.concat(
                        getProductGraph(shopId).onErrorResumeNext(
                                new Func1<Throwable, Observable<? extends Response<GetProductGraph>>>() {
                                    @Override
                                    public Observable<? extends Response<GetProductGraph>> call(Throwable throwable) {
                                        Response<GetProductGraph> response = (Response<GetProductGraph>) getError1000();
                                        return Observable.just(response);
                                    }
                                }
                        ),
                        getTransactionGraph(shopId).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetTransactionGraph>>>() {
                            @Override
                            public Observable<? extends Response<GetTransactionGraph>> call(Throwable throwable) {
                                Response<GetTransactionGraph> error1000 = (Response<GetTransactionGraph>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getPopularProduct(shopId).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetPopularProduct>>>() {
                            @Override
                            public Observable<? extends Response<GetPopularProduct>> call(Throwable throwable) {
                                Response<GetPopularProduct> error1000 = (Response<GetPopularProduct>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getBuyerData(shopId).onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<GetBuyerData>>>() {
                            @Override
                            public Observable<? extends Response<GetBuyerData>> call(Throwable throwable) {
                                Response<GetBuyerData> error1000 = (Response<GetBuyerData>) getError1000();
                                return Observable.just(error1000);
                            }
                        }),
                        getKeywordModels(shopId)
                )
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<List<Object>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if(BuildConfig.DEBUG){
                                            Log.e(TAG, "error : "+e);
                                        }
                                    }

                                    @Override
                                    public void onNext(List<Object> responses) {
                                        Response<GetProductGraph> getProductGraphResponse =
                                                (Response<GetProductGraph>) responses.get(0);
                                        if (getProductGraphResponse.isSuccessful()) {
                                            GetProductGraph body = getProductGraphResponse.body();
                                            getGMStat.onSuccessProductnGraph(body);
                                        } else {
                                            onResponseError(getProductGraphResponse.code(), getGMStat);
                                        }

                                        Response<GetTransactionGraph> getTransactionGraphResponse =
                                                (Response<GetTransactionGraph>) responses.get(1);
                                        if (getTransactionGraphResponse.isSuccessful()) {
                                            GetTransactionGraph body = getTransactionGraphResponse.body();
                                            getGMStat.onSuccessTransactionGraph(body);
                                        } else {
                                            onResponseError(getTransactionGraphResponse.code(), getGMStat);
                                        }

                                        Response<GetPopularProduct> getPopularProductResponse =
                                                (Response<GetPopularProduct>) responses.get(2);

                                        if (getPopularProductResponse.isSuccessful()) {
                                            GetPopularProduct body = getPopularProductResponse.body();
                                            getGMStat.onSuccessPopularProduct(body);
                                        } else {
                                            onResponseError(getPopularProductResponse.code(), getGMStat);
                                        }

                                        Response<GetBuyerData> response =
                                                (Response<GetBuyerData>) responses.get(3);
                                        if(response.isSuccessful()){
                                            GetBuyerData body = response.body();
                                            getGMStat.onSuccessBuyerData(body);
                                        }else{
                                            onResponseError(response.code(), getGMStat);
                                        }

                                        KeywordModel keywordModel = (KeywordModel) responses.get(4);
                                        GetShopCategory getShopCategory = keywordModel.getShopCategory;
                                        getGMStat.onSuccessGetShopCategory(getShopCategory);

                                        List<GetKeyword> getKeywords = keywordModel.getKeywords;
                                        getGMStat.onSuccessGetKeyword(getKeywords);
                                    }
                                }
                        )
        );

    }

    public void getProductGraph(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                getProductGraph(shopId) .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<Response<GetProductGraph>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Response<GetProductGraph> response) {
                                        if (response.isSuccessful()) {
                                            GetProductGraph body = response.body();
                                            getGMStat.onSuccessProductnGraph(body);
                                        } else {
                                            onResponseError(response.code(), getGMStat);
                                        }
                                    }
                                }
                        )
        );
    }

    public void getTransactionGraph(long shopId, CompositeSubscription compositeSubscription, final GetGMStat getGMStat){
        compositeSubscription.add(
                getTransactionGraph(shopId) .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(
                                new Subscriber<Response<GetTransactionGraph>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Response<GetTransactionGraph> response) {
                                        if (response.isSuccessful()) {
                                            GetTransactionGraph body = response.body();
                                            getGMStat.onSuccessTransactionGraph(body);
                                        } else {
                                            onResponseError(response.code(), getGMStat);
                                        }
                                    }
                                }
                        )
        );
    }

    public interface GetGMStat extends ShopNetworkController.CommonListener{
        void onSuccessGetShopCategory(GetShopCategory getShopCategory);
        void onSuccessTransactionGraph(GetTransactionGraph getTransactionGraph);
        void onSuccessProductnGraph(GetProductGraph getTransactionGraph);
        void onSuccessPopularProduct(GetPopularProduct getPopularProduct);
        void onSuccessBuyerData(GetBuyerData getBuyerData);
        void onSuccessGetKeyword(List<GetKeyword> getKeywords);
        void onComplete();
    }

    public Map<String, String> getGMStatParam(
            long shopId, long sDate, long eDate
    ){
        Map<String, String> param = new HashMap<>();

        if(shopId <= 0)
            throw new RuntimeException("Shop Id cannot be nulled for "+TAG);

        param.put("shop_id", Long.toString(shopId));

        if(sDate != DEFAULT_SDATE){
            param.put("sDate", getFormattedDate(sDate));
        }

        if(eDate != DEFAULT_EDATE){
            param.put("eDate", getFormattedDate(eDate));
        }

        param.put("timestamp", System.currentTimeMillis()+"");

        return param;
    }

    public String getFormattedDate(long dateLong){
        Date date = new Date(dateLong);
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");// "HH:mm:ss:SSS"
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }
}
