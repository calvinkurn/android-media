package com.tokopedia.posapp.product.common.data.source.local;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.database.manager.base.PosDbOperation;
import com.tokopedia.posapp.database.model.ProductDb;
import com.tokopedia.posapp.database.model.ProductDb_Table;
import com.tokopedia.posapp.product.common.domain.model.ProductDomain;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by okasurya on 9/16/17.
 */

public class ProductDbManager extends PosDbOperation<ProductDomain, ProductDb> {
    @Override
    protected ProductDb mapToDb(ProductDomain data) {
        if(data != null) {
            ProductDb productDb = new ProductDb();
            productDb.setProductId(data.getId());
            productDb.setProductName(data.getName());
            productDb.setProductPrice(data.getPrice());
            productDb.setProductPriceUnformatted(data.getPriceUnformatted());
            productDb.setProductDescription(data.getDescription());
            productDb.setProductUrl(data.getUrl());
            productDb.setProductImage(data.getImage());
            productDb.setProductImage300(data.getImage300());
            productDb.setProductImageFull(data.getImageFull());
            productDb.setEtalaseId(Long.toString(data.getEtalaseId()));
            return productDb;
        }

        return null;
    }

    @Override
    protected List<ProductDb> mapToDb(List<ProductDomain> domains) {
        List<ProductDb> productDbs = new ArrayList<>();
        if(domains != null) {
            for (ProductDomain domain : domains) {
                ProductDb productDb = mapToDb(domain);
                if(productDb != null)  productDbs.add(productDb);
            }
        }
        return productDbs;
    }

    @Override
    protected ProductDomain mapToDomain(ProductDb db) {
        if(db != null) {
            ProductDomain productDomain = new ProductDomain();
            productDomain.setId(db.getProductId());
            productDomain.setName(db.getProductName());
            productDomain.setPrice(db.getProductPrice());
            productDomain.setPriceUnformatted(db.getProductPriceUnformatted());
            productDomain.setDescription(db.getProductDescription());
            productDomain.setUrl(db.getProductUrl());
            productDomain.setImage(db.getProductImage());
            productDomain.setImage300(db.getProductImage300());
            productDomain.setImageFull(db.getProductImageFull());
            productDomain.setEtalaseId(Long.parseLong(db.getEtalaseId()));
            return productDomain;
        }

        return null;
    }

    @Override
    protected List<ProductDomain> mapToDomain(List<ProductDb> dbs) {
        List<ProductDomain> productDomains = new ArrayList<>();
        if(dbs != null) {
            for (ProductDb db : dbs) {
                ProductDomain productDomain = mapToDomain(db);
                if(productDomain != null)  productDomains.add(productDomain);
            }
        }
        return productDomains;
    }

    @Override
    protected Class<ProductDb> getDbClass() {
        return ProductDb.class;
    }

    @Override
    public Observable<DataStatus> delete(ProductDomain domain) {
        return executeDelete(
                getDbClass(),
                ConditionGroup.clause().and(ProductDb_Table.productId.eq(domain.getId()))
        );
    }
}
