package com.tokopedia.contactus.home.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.contactus.common.data.BuyerPurchaseList;
import com.tokopedia.contactus.home.data.BuyerPurchaseData;
import com.tokopedia.contactus.home.data.ContactUsArticleResponse;
import com.tokopedia.contactus.home.data.TopBotStatus;
import com.tokopedia.contactus.home.source.api.ContactUsAPI;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sandeepgoyal on 03/04/18.
 */

public class ContactUsArticleDataStore {

    ContactUsAPI contactUsAPI;


    public ContactUsArticleDataStore(ContactUsAPI contactUsAPI) {
        this.contactUsAPI = contactUsAPI;
    }

    public Observable<List<ContactUsArticleResponse>> getPopularArticle() {
        return this.contactUsAPI.getPopularArticle().map(new Func1<Response<List<ContactUsArticleResponse>>, List<ContactUsArticleResponse>>() {
            @Override
            public List<ContactUsArticleResponse> call(Response<List<ContactUsArticleResponse>> dataResponseResponse) {
                return dataResponseResponse.body().subList(0,5);
            }
        });
    }
    public Observable<List<BuyerPurchaseList>> getBuyerPurchaseList() {
        return this.contactUsAPI.getBuyerPurchaseList().map(new Func1<Response<DataResponse<BuyerPurchaseData>>, List<BuyerPurchaseList>>() {
            @Override
            public List<BuyerPurchaseList> call(Response<DataResponse<BuyerPurchaseData>> buyerpurchasedata) {
                List<BuyerPurchaseList> buyerPurchaseList;
                buyerPurchaseList = buyerpurchasedata.body().getData().getBuyerPurchaseList();
                return buyerPurchaseList;
            }
        });
    }

    public Observable<TopBotStatus> getTopBotStatus() {
        return this.contactUsAPI.getTopBotStatus().map(new Func1<Response<DataResponse<TopBotStatus>>, TopBotStatus>() {
            @Override
            public TopBotStatus call(Response<DataResponse<TopBotStatus>> topBotStatus) {
                return topBotStatus.body().getData();
            }
        });



    }

    public Observable<List<BuyerPurchaseList>> getSellerPurchaseList() {
        return this.contactUsAPI.getSellerPurchaseList().map(new Func1<Response<DataResponse<BuyerPurchaseData>>, List<BuyerPurchaseList>>() {
            @Override
            public List<BuyerPurchaseList> call(Response<DataResponse<BuyerPurchaseData>> buyerpurchasedata) {
                List<BuyerPurchaseList> sellerPurchaseList;
                sellerPurchaseList = buyerpurchasedata.body().getData().getBuyerPurchaseList();
                return sellerPurchaseList;
            }
        });
    }
}
