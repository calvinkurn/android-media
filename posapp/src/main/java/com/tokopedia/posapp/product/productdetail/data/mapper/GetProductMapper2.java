package com.tokopedia.posapp.product.productdetail.data.mapper;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductDetail;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductList;
import com.tokopedia.posapp.product.productlist.data.pojo.ProductPicture;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author okasurya on 4/19/18.
 */

public class GetProductMapper2 implements Func1<Response<DataResponse<ProductList>>, ProductDetailData> {
    @Inject
    public GetProductMapper2() {}

    @Override
    public ProductDetailData call(Response<DataResponse<ProductList>> response) {
        if(response.isSuccessful() && response.body() != null && response.body().getData() != null) {
            ProductDetail productResponse = response.body().getData().getProducts().get(0);
            if(productResponse != null) {
                ProductDetailData productDetailData = new ProductDetailData();

                List<ProductImage> images = new ArrayList<>();
                for (ProductPicture picture : productResponse.getPictures()) {
                    ProductImage productImage = new ProductImage();
                    productImage.setImageSrc(picture.getUrlOriginal());
                    productImage.setImageSrc300(picture.getUrlThumbnail());
                    productImage.setImageId(picture.getPicId());
                    images.add(productImage);
                }
                productDetailData.setProductImages(images);

                ProductInfo info = new ProductInfo();
                info.setProductName(productResponse.getName());
                info.setProductPrice(CurrencyFormatUtil.convertPriceValueToIdrFormat(productResponse.getLocalPrice().getPrice(), true));
                info.setProductPriceUnformatted((int) productResponse.getLocalPrice().getPrice());
                info.setProductEtalaseId(Long.toString(productResponse.getEtalase().getId()));
                info.setProductEtalase(productResponse.getEtalase().getName());
                info.setProductDescription(productResponse.getProductDescription());
                productDetailData.setInfo(info);

                return productDetailData;
            }
        }
        return null;
    }
}
