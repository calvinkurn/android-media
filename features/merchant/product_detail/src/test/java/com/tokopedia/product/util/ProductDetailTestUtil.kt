package com.tokopedia.product.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.pdplayout.PdpGetLayout
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.usecase.GetPdpLayoutUseCaseTest
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Created by Yehezkiel on 01/04/20
 */
object ProductDetailTestUtil {

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    fun getMockVariant(): ProductVariant {
        val data = getMockPdpLayout()
        return data.variantData ?: ProductVariant()
    }

    fun getMockPdpLayout() : ProductDetailDataModel{
        val mockData : ProductDetailLayout= createMockGraphqlSuccessResponse(GetPdpLayoutUseCaseTest.GQL_GET_PDP_LAYOUT_JSON, ProductDetailLayout::class.java)
        return mapIntoModel(mockData.data ?: PdpGetLayout())
    }

    fun getMockPdpThatShouldRemoveUnusedComponent() : ProductDetailDataModel {
        val mockData : ProductDetailLayout = createMockGraphqlSuccessResponse(GetPdpLayoutUseCaseTest.GQL_GET_PDP_LAYOUT_REMOVE_COMPONENT_JSON, ProductDetailLayout::class.java)
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

    fun generateMiniCartMock(productId:String): Map<String, MiniCartItem> {
        return mapOf(productId to MiniCartItem(cartId = "111", productId = productId, quantity = 4, notes = "notes gan"))
    }
}

fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}



