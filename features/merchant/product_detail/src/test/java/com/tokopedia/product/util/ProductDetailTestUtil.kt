package com.tokopedia.product.util

import com.tokopedia.graphql.CommonUtils
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.usecase.GetPdpLayoutUseCaseTest
import java.io.File
import java.lang.reflect.Type


/**
 * Created by Yehezkiel on 01/04/20
 */
object ProductDetailTestUtil {

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    fun getMockPdpLayout() : ProductDetailDataModel{
        val mockData : ProductDetailLayout= createMockGraphqlSuccessResponse(GetPdpLayoutUseCaseTest.GQL_GET_PDP_LAYOUT_JSON, ProductDetailLayout::class.java)
        return mapIntoModel(mockData.data ?: PdpGetLayout())
    }

    fun getMockPdpThatShouldRemoveUnusedComponent() : ProductDetailDataModel {
        val mockData : ProductDetailLayout= createMockGraphqlSuccessResponse(GetPdpLayoutUseCaseTest.GQL_GET_PDP_LAYOUT_REMOVE_COMPONENT_JSON, ProductDetailLayout::class.java)
        return mapIntoModel(mockData.data ?: PdpGetLayout())
    }

    fun <T> createMockGraphqlSuccessResponse(jsonLocation: String, typeOfClass: Type): T {
        return CommonUtils.fromJson(
                getJsonFromFile(jsonLocation),
                typeOfClass) as T
    }

    fun mapIntoModel(data: PdpGetLayout): ProductDetailDataModel {
        val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(data.components)
        val getDynamicProductInfoP1 = DynamicProductDetailMapper.mapToDynamicProductDetailP1(data)
        val p1VariantData = DynamicProductDetailMapper.mapVariantIntoOldDataClass(data)
        return ProductDetailDataModel(getDynamicProductInfoP1, initialLayoutData, p1VariantData)
    }
}



