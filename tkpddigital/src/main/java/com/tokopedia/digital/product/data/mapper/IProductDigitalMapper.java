package com.tokopedia.digital.product.data.mapper;

import com.tokopedia.digital.exception.MapperDataException;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailData;
import com.tokopedia.digital.product.data.entity.response.ResponseCategoryDetailIncluded;
import com.tokopedia.digital.product.data.entity.response.ResponsePulsaBalance;
import com.tokopedia.digital.product.view.model.CategoryData;
import com.tokopedia.digital.product.view.model.PulsaBalance;

import java.util.List;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public interface IProductDigitalMapper {

    CategoryData transformCategoryData(
            ResponseCategoryDetailData responseCategoryDetailData,
            List<ResponseCategoryDetailIncluded> responseCategoryDetailIncludedList
    ) throws MapperDataException;

    PulsaBalance transformPulsaBalance(
            ResponsePulsaBalance responsePulsaBalance);

}
