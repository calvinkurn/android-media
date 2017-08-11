package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.InstallmentRule;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.product.model.productdetail.ProductImage;
import com.tokopedia.core.product.model.productdetail.ProductInfo;
import com.tokopedia.core.product.model.productdetail.ProductInstallment;
import com.tokopedia.core.product.model.productdetail.ProductShopInfo;
import com.tokopedia.core.product.model.productdetail.Terms;
import com.tokopedia.posapp.domain.model.product.InstallmentRuleDomain;
import com.tokopedia.posapp.domain.model.product.ProductDomain;
import com.tokopedia.posapp.domain.model.product.ProductImageDomain;
import com.tokopedia.posapp.domain.model.product.ProductInfoDomain;
import com.tokopedia.posapp.domain.model.product.ProductInstallmentDomain;
import com.tokopedia.posapp.domain.model.product.TermsDomain;
import com.tokopedia.posapp.domain.model.shop.ShopInfoDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/9/17.
 */

public class GetProductDomainMapper implements Func1<Response<TkpdResponse>, ProductDomain> {
    @Override
    public ProductDomain call(Response<TkpdResponse> tkpdResponse) {
        if(tkpdResponse.isSuccessful() && tkpdResponse.body() != null) {
            ProductDetailData productDetailData =
                    tkpdResponse.body().convertDataObj(ProductDetailData.class);

            if(productDetailData != null) {
                ProductDomain productDomain = getProductDomain(productDetailData);
                return productDomain;
            }
        }
        return null;
    }

    private ProductDomain getProductDomain(ProductDetailData productDetailData) {
        ProductDomain productDomain = new ProductDomain();
        productDomain.setProductInfo(getProductInfo(productDetailData.getInfo()));
        productDomain.setProductImages(getProductImages(productDetailData.getProductImages()));
        productDomain.setShopInfo(getShopInfo(productDetailData.getShopInfo()));
        return productDomain;
    }

    private ProductInfoDomain getProductInfo(ProductInfo info) {
        ProductInfoDomain productInfoDomain = new ProductInfoDomain();

        productInfoDomain.setProductWeightUnit(info.getProductWeightUnit());
        productInfoDomain.setProductEtalaseId(info.getProductEtalaseId());
        productInfoDomain.setProductAlreadyWishlist(info.getProductAlreadyWishlist());
        productInfoDomain.setProductInsurance(info.getProductInsurance());
        productInfoDomain.setProductCondition(info.getProductCondition());
        productInfoDomain.setProductKey(info.getProductKey());
        productInfoDomain.setProductEtalase(info.getProductEtalase());
        productInfoDomain.setProductStatus(info.getProductStatus());
        productInfoDomain.setProductId(info.getProductId());
        productInfoDomain.setProductPrice(info.getProductPrice());
        productInfoDomain.setProductDescription(info.getProductDescription());
        productInfoDomain.setProductReturnable(info.getProductReturnable());
        productInfoDomain.setProductMinOrder(info.getProductMinOrder());
        productInfoDomain.setProductLastUpdate(info.getProductLastUpdate());
        productInfoDomain.setProductWeight(info.getProductWeight());
        productInfoDomain.setProductPriceAlert(info.getProductPriceAlert());
        productInfoDomain.setProductName(info.getProductName());
        productInfoDomain.setProductUrl(info.getProductUrl());
        productInfoDomain.setProductCatalogId(info.getProductCatalogId());
        productInfoDomain.setProductCatalogName(info.getProductCatalogName());
        productInfoDomain.setProductCatalogUrl(info.getProductCatalogUrl());
        productInfoDomain.setProductStatusMessage(info.getProductStatusMessage());
        productInfoDomain.setProductStatusTitle(info.getProductStatusTitle());
        productInfoDomain.setProductInstallments(getProductInstallments(info.getProductInstallments()));
        productInfoDomain.setWholeSaleMinPrice(info.getWholseSaleMinPrice());
        productInfoDomain.setWholeSaleMinQuantity(info.getWholeSaleMinQuantity());
        productInfoDomain.setInstallmentMinPercentage(info.getInstallmentMinPercentage());
        productInfoDomain.setInstallmentMinPrice(info.getInstallmentMinPrice());

        return productInfoDomain;
    }

    private List<ProductInstallmentDomain> getProductInstallments(List<ProductInstallment> productInstallments) {
        List<ProductInstallmentDomain> productInstallmentDomains = new ArrayList<>();
        for(ProductInstallment productInstallment : productInstallments) {
            ProductInstallmentDomain productInstallmentDomain = new ProductInstallmentDomain();
            productInstallmentDomain.setIcon(productInstallment.getIcon());
            productInstallmentDomain.setTerms(getTerms(productInstallment.getTerms()));
            productInstallmentDomain.setName(productInstallment.getName());
            productInstallmentDomain.setId(productInstallment.getId());
            productInstallmentDomains.add(productInstallmentDomain);
        }
        return productInstallmentDomains;
    }

    private TermsDomain getTerms(Terms terms) {
        TermsDomain termsDomain = new TermsDomain();
        termsDomain.setRule3Months(getInstallmentRule(terms.getRule3Months()));
        termsDomain.setRule6Months(getInstallmentRule(terms.getRule6Months()));
        termsDomain.setRule12Months(getInstallmentRule(terms.getRule12Months()));
        return termsDomain;
    }

    private InstallmentRuleDomain getInstallmentRule(InstallmentRule rule) {
        InstallmentRuleDomain installmentRuleDomain = new InstallmentRuleDomain();
        if(rule != null && rule.getPrice() != null && rule.getMinPurchase() != null) {
            installmentRuleDomain.setPrice(rule.getPrice());
            installmentRuleDomain.setMinPurchase(rule.getMinPurchase());
        }
        return installmentRuleDomain;
    }

    private List<ProductImageDomain> getProductImages(List<ProductImage> productImages) {
        List<ProductImageDomain> productImageDomains = new ArrayList<>();
        for(ProductImage productImage : productImages) {
            ProductImageDomain  productImageDomain = new ProductImageDomain();
            productImageDomain.setImageId(productImage.getImageId());
            productImageDomain.setImageSrc300(productImage.getImageSrc300());
            productImageDomain.setImageId(productImage.getImageStatus());
            productImageDomain.setImageDescription(productImage.getImageDescription());
            productImageDomain.setImagePrimary(productImage.getImagePrimary());
            productImageDomain.setImageSrc(productImage.getImageSrc());
            productImageDomains.add(productImageDomain);
        }
        return productImageDomains;
    }

    private ShopInfoDomain getShopInfo(ProductShopInfo shopInfo) {
        ShopInfoDomain shopInfoDomain = new ShopInfoDomain();
        shopInfoDomain.setShopIsClosedReason(shopInfo.getShopIsClosedReason());
        shopInfoDomain.setShopName(shopInfo.getShopName());
        shopInfoDomain.setShopLucky(shopInfo.getShopLucky());
        shopInfoDomain.setShopId(shopInfo.getShopId());
        shopInfoDomain.setShopTagline(shopInfo.getShopTagline());
        shopInfoDomain.setShopUrl(shopInfo.getShopUrl());
        shopInfoDomain.setShopDescription(shopInfo.getShopDescription());
        shopInfoDomain.setShopCover(shopInfo.getShopCover());
        shopInfoDomain.setShopHasTerms(
                shopInfo.getShopHasTerms() != null && shopInfo.getShopHasTerms() == 1 ? 1 : 0
        );
        shopInfoDomain.setShopAlreadyFavorited(shopInfo.getShopAlreadyFavorited());
        shopInfoDomain.setShopDomain(shopInfo.getShopDomain());

        return shopInfoDomain;
    }
}
