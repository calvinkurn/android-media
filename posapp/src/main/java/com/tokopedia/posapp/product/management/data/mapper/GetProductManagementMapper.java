package com.tokopedia.posapp.product.management.data.mapper;

import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductItem;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductListData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/11/18.
 */

public class GetProductManagementMapper implements Func1<Response<PosResponse<ProductListData>>, List<ProductDomain>> {
    @Inject
    public GetProductManagementMapper() {
    }

    @Override
    public List<ProductDomain> call(Response<PosResponse<ProductListData>> response) {
        if (response.isSuccessful() && response.body() != null) {
            return getProductListDomain(response.body());
        }

        return null;
    }

    private List<ProductDomain> getProductListDomain(PosResponse<ProductListData> data) {
        List<ProductDomain> domains = new ArrayList<>();

        for (ProductItem item : data.getData().getData()) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setId(item.getProductId());
            productDomain.setName(item.getName());
            productDomain.setOriginalPriceUnformatted(item.getPrice().getValue());
            productDomain.setPriceUnformatted(item.getOutlet().getLocalPrice());
            productDomain.setOriginalPrice(getFormattedCurrency(item.getPrice().getValue()));
            productDomain.setPrice(getFormattedCurrency(item.getOutlet().getLocalPrice()));
            productDomain.setUrl(item.getProductUrl());
            productDomain.setImage(item.getPrimaryImage().getOriginal());
            productDomain.setImage300(item.getPrimaryImage().getThumbnail());
            productDomain.setImageFull(item.getPrimaryImage().getOriginal());
            productDomain.setStatus(item.getOutlet().getLocalProductStatus());
            productDomain.setEtalaseId(item.getShowcase().getId());
            domains.add(productDomain);
        }

        return domains;
    }

    private String getFormattedCurrency(double price) {
        if(price == 0) return "";

        return CurrencyFormatUtil.getThousandSeparatorString(price, false, 0).getFormattedString();
    }
}