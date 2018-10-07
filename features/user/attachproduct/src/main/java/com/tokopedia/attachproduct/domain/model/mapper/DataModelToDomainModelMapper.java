package com.tokopedia.attachproduct.domain.model.mapper;

import com.tokopedia.attachproduct.data.model.DataProductResponse;
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel;
import com.tokopedia.attachproduct.domain.util.DomainModelToViewModelConverter;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class DataModelToDomainModelMapper implements Func1<AttachProductDomainModel, List<AttachProductItemViewModel>> {
    @Inject
    public DataModelToDomainModelMapper() {
    }

    @Override
    public List<AttachProductItemViewModel> call(AttachProductDomainModel attachProductDomainModel) {
        ArrayList<AttachProductItemViewModel> arrayList = new ArrayList<>();
        for(DataProductResponse product:attachProductDomainModel.getProducts()){
            arrayList.add(DomainModelToViewModelConverter.convertProductDomainModel(product));
        }
        return arrayList;
    }
}
