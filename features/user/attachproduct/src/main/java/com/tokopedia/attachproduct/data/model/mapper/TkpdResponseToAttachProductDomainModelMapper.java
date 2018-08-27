package com.tokopedia.attachproduct.data.model.mapper;

import com.tokopedia.attachproduct.data.model.TomeResponseWrapper;
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class TkpdResponseToAttachProductDomainModelMapper implements Func1<Response<TomeResponseWrapper>,
        AttachProductDomainModel> {
    @Inject
    public TkpdResponseToAttachProductDomainModelMapper() {
    }
//TODO and delete this if no use anymore
//    @Override
//    public AttachProductDomainModel call(Response<TkpdResponse> tkpdResponseResponse) {
//        AttachProductDomainModel domainModel = new AttachProductDomainModel();
//        TkpdResponse tkpdResponse = tkpdResponseResponse.body();
//        AttachProductAPIResponseWrapper attachProductAPIResponseWrapper = tkpdResponse.convertDataObj(AttachProductAPIResponseWrapper.class);
//        domainModel.setProducts(attachProductAPIResponseWrapper.getProducts());
//        return domainModel;
//    }

    @Override
    public AttachProductDomainModel call(Response<TomeResponseWrapper>
                                                     tomeResponseWrapperResponse) {
        if(tomeResponseWrapperResponse.isSuccessful() &&
                tomeResponseWrapperResponse.body() != null) {
            AttachProductDomainModel domainModel = new AttachProductDomainModel();
            domainModel.setProducts(tomeResponseWrapperResponse.body().getData().getProducts());
            return domainModel;
        } else {
            //TODO change this to proper error message please
            throw new RuntimeException("Unknown network error..");
        }
    }
}
