package com.tokopedia.posapp.product.management.data.mapper;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.base.data.pojo.PosData;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/11/18.
 */

public class GetProductManagementMapper implements Func1<Response<PosSimpleResponse<List<ProductDetail>>>, List<ProductDomain>> {
    @Inject
    public GetProductManagementMapper() {
    }

    @Override
    public List<ProductDomain> call(Response<PosSimpleResponse<List<ProductDetail>>> response) {
        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
            return getProductListDomain(response.body().getData());
        }

        return null;
    }

    private List<ProductDomain> getProductListDomain(PosData<List<ProductDetail>> data) {
        List<ProductDomain> domains = new ArrayList<>();

        for (ProductDetail item : data.getData()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setId(item.getId());
            productDomain.setName(item.getName());
            productDomain.setOriginalPriceUnformatted(item.getPrice());
            productDomain.setPriceUnformatted(item.getLocalPrice().getPrice());
            productDomain.setOriginalPrice(getFormattedCurrency(item.getPrice()));
            productDomain.setPrice(getFormattedCurrency(item.getLocalPrice().getPrice()));
            productDomain.setUrl(item.getUrl());
            productDomain.setStatus(item.getLocalPrice().getStatus());
            productDomain.setEtalaseId(item.getEtalase().getId());

            if(item.getPictures() != null && item.getPictures().size() > 0) {
                productDomain.setImage(item.getPictures().get(0).getUrlOriginal());
                productDomain.setImage300(item.getPictures().get(0).getUrlThumbnail());
                productDomain.setImageFull(item.getPictures().get(0).getUrlOriginal());
            }
            domains.add(productDomain);
        }

        return domains;
    }

    private String getFormattedCurrency(double price) {
        if(price == 0) return "";

        return CurrencyFormatUtil.getThousandSeparatorString(price, false, 0).getFormattedString();
    }
}