package com.tokopedia.posapp.data.mapper;

import com.tokopedia.core.database.model.ProductDB;
import com.tokopedia.core.shopinfo.models.productmodel.Product;
import com.tokopedia.core.shopinfo.models.productmodel.ProductModel;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by okasurya on 8/30/17.
 */

public class StoreProductMapper {
    public Func1<ShopProductListDomain, List<ProductDB>> mapToProductDb() {
        return new Func1<ShopProductListDomain, List<ProductDB>>() {
            @Override
            public List<ProductDB> call(ShopProductListDomain productListDomain) {
                List<ProductDB> productDBs = new ArrayList<>();
                for (com.tokopedia.core.shopinfo.models.productmodel.List item : productListDomain.getProductList()) {
                    ProductDB productDB = new ProductDB();
                    productDB.setProductId(item.productId);
                    productDB.setNameProd(item.productName);
                    productDB.setPriceFormatted(item.productPrice);
                    productDB.setProductUrl(item.productUrl);
                    productDB.setPriceProd(Integer.parseInt(item.productPriceNoIdr));
                    productDBs.add(productDB);
                }

                return productDBs;
            }
        };
    }


    public Func1<List<ProductDB>, Boolean> mapToSuccessResult() {
        return new Func1<List<ProductDB>, Boolean>() {
            @Override
            public Boolean call(List<ProductDB> productDBs) {
                return true;
            }
        };
    }

    public Func1<Throwable, Observable<Boolean>> mapToErrorResult() {
        return new Func1<Throwable, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Throwable throwable) {
                throwable.printStackTrace();
                return Observable.just(false);
            }
        };
    }
}
