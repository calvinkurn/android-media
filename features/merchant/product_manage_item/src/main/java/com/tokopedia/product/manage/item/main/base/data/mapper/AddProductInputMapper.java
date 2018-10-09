package com.tokopedia.product.manage.item.main.base.data.mapper;

import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.product.manage.item.main.base.domain.model.AddProductSubmitInputDomainModel;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductInputMapper {

    public static AddProductSubmitInputServiceModel mapSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = new AddProductSubmitInputServiceModel();
        serviceModel.setPostKey(domainModel.getPostKey());
        serviceModel.setFileUploaded(domainModel.getFileUploadedTo());
        serviceModel.setProductEtalseId(domainModel.getProductEtalaseId());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        return serviceModel;
    }

}
