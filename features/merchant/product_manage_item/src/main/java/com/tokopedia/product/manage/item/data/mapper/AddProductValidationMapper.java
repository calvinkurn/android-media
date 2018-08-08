package com.tokopedia.product.manage.item.data.mapper;

import com.tokopedia.product.manage.item.data.source.cloud.model.addproductvalidation.AddProductValidationResult;
import com.tokopedia.product.manage.item.data.source.cloud.model.addproductvalidation.AddProductValidationServiceModel;
import com.tokopedia.product.manage.item.domain.model.AddProductValidationDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductValidationMapper implements Func1<AddProductValidationServiceModel, AddProductValidationDomainModel> {
    @Override
    public AddProductValidationDomainModel call(AddProductValidationServiceModel serviceModel) {
        AddProductValidationDomainModel domainModel = new AddProductValidationDomainModel();

        AddProductValidationResult result = serviceModel.getAddProductValidationResult();
        domainModel.setPostKey(result.getPostKey());
        domainModel.setProductDesc(result.getProductDesc());
        domainModel.setProductDest(result.getProductDest());
        domainModel.setproductEtalase(result.getProductEtalase());
        domainModel.setProductId(result.getProductId());
        domainModel.setProductName(result.getProductName());
        domainModel.setProductUrl(result.getProductUrl());

        return domainModel;
    }
}
