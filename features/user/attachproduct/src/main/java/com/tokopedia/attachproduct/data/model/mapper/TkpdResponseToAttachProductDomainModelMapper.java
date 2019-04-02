package com.tokopedia.attachproduct.data.model.mapper;

import com.tokopedia.attachproduct.data.model.AceResponseWrapper;
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class TkpdResponseToAttachProductDomainModelMapper implements Func1<Response<AceResponseWrapper>,
        AttachProductDomainModel> {
    @Inject
    public TkpdResponseToAttachProductDomainModelMapper() {
    }

    @Override
    public AttachProductDomainModel call(Response<AceResponseWrapper>
                                                     tomeResponseWrapperResponse) {
        if(tomeResponseWrapperResponse.isSuccessful() &&
                tomeResponseWrapperResponse.body() != null) {
            AttachProductDomainModel domainModel = new AttachProductDomainModel();
            domainModel.setProducts(tomeResponseWrapperResponse.body().getData().getProducts());
            return domainModel;
        } else {
            throw new RuntimeException("Unknown network error..");
        }
    }
}
