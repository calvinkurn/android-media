package com.tokopedia.product.manage.item.main.base.data.mapper;


import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.product.manage.item.main.base.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */
@Deprecated
public class EditProductInputMapper extends AddProductValidationInputMapper{

    @Inject
    public EditProductInputMapper() {
    }

    public void map(EditProductInputServiceModel serviceModel, UploadProductInputDomainModel domainModel){
        map((AddProductValidationInputServiceModel) serviceModel, domainModel);
        serviceModel.setProductChangeCatalog(domainModel.getProductChangeCatalog());
        serviceModel.setProductChangePhoto(domainModel.getProductChangePhoto());
        serviceModel.setProductChangeWholesale(domainModel.getProductChangeWholesale());
        serviceModel.setProductId(domainModel.getProductId());
    }

}
