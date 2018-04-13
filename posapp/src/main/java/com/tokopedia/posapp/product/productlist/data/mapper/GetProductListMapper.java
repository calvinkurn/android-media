package com.tokopedia.posapp.product.productlist.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 10/17/17.
 */

public class GetProductListMapper implements Func1<Response<DataResponse<ProductList>>, List<ProductDomain>> {
    @Inject
    public GetProductListMapper() {
    }

    @Override
    public List<ProductDomain> call(Response<DataResponse<ProductList>> response) {
        if (response.isSuccessful() && response.body() != null) {
            return getProductListDomain(response.body());
        }

        return null;
    }

    private List<ProductDomain> getProductListDomain(DataResponse<ProductList> data) {
        List<ProductDomain> domains = new ArrayList<>();

        for (ProductDetail item : data.getData().getProducts()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setId(item.getId());
            productDomain.setName(item.getName());
            productDomain.setPriceUnformatted(item.getLocalPrice().getPrice());
            productDomain.setPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(item.getLocalPrice().getPrice(), true));
            productDomain.setUrl(item.getUrl());
            if(item.getPictures().size() > 0) {
                productDomain.setImage(item.getPictures().get(0).getUrlOriginal());
                productDomain.setImage300(item.getPictures().get(0).getUrlThumbnail());
                productDomain.setImageFull(item.getPictures().get(0).getUrlOriginal());
            }
            productDomain.setStatus(item.getLocalPrice().getStatus());
            domains.add(productDomain);
        }

        return domains;
    }
}
