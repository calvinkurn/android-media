package com.tokopedia.attachproduct.domain.model.mapper;

import com.tokopedia.attachproduct.data.model.DataProductResponse;
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel;
import com.tokopedia.attachproduct.domain.util.DomainModelToViewModelConverter;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * Created by Hendri on 08/03/18.
 */

public class DataModelToDomainModelMapper implements Func1<AttachProductDomainModel, List<AttachProductItemUiModel>> {
    @Inject
    public DataModelToDomainModelMapper() {
    }

    @Override
    public List<AttachProductItemUiModel> call(AttachProductDomainModel attachProductDomainModel) {
        ArrayList<AttachProductItemUiModel> arrayList = new ArrayList<>();
        for(DataProductResponse product:attachProductDomainModel.getProducts()){
            arrayList.add(DomainModelToViewModelConverter.convertProductDomainModel(product));
        }
        return arrayList;
    }
}
