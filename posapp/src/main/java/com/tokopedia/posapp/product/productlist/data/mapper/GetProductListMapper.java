package com.tokopedia.posapp.product.productlist.data.mapper;

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
 * Created by okasurya on 10/17/17.
 */

public class GetProductListMapper implements Func1<Response<PosResponse<ProductListData>>, List<ProductDomain>> {
    @Inject
    public GetProductListMapper() {
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
            productDomain.setOriginalPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(item.getPrice().getValue(), true));
            productDomain.setPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(item.getOutlet().getLocalPrice(), true));
            productDomain.setPriceUnformatted(item.getOutlet().getLocalPrice());
            productDomain.setUrl(item.getProductUrl());
            productDomain.setImage(item.getPrimaryImage().getOriginal());
            productDomain.setImage300(item.getPrimaryImage().getThumbnail());
            productDomain.setImageFull(item.getPrimaryImage().getOriginal());
            productDomain.setStatus(item.getOutlet().getLocalProductStatus());
            domains.add(productDomain);
        }

        return domains;
    }
}
