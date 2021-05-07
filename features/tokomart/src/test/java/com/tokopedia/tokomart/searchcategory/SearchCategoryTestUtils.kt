package com.tokopedia.tokomart.searchcategory

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel.Product
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import java.io.File
import org.hamcrest.CoreMatchers.`is` as shouldBe

inline fun <reified T> String.jsonToObject(): T {
    val systemClassLoader = ClassLoader.getSystemClassLoader()
    val urlResource = systemClassLoader.getResource(this)
    val urlResourcePath = urlResource.path
    val fileFromUrlResourcePath = File(urlResourcePath)
    val jsonString = String(fileFromUrlResourcePath.readBytes())

    return Gson().fromJson(jsonString, T::class.java)
}

fun Visitable<*>.assertChooseAddressDataView() {
    assertThat(this, instanceOf(ChooseAddressDataView::class.java))

    //TODO: assert choose address data here
    val chooseAddressDataView = this as ChooseAddressDataView
}

fun Visitable<*>.assertBannerDataView() {
    assertThat(this, instanceOf(BannerDataView::class.java))

    //TODO: assert banner data here
    val bannerDataView = this as BannerDataView
}

fun Visitable<*>.assertTitleDataView(title: String, hasSeeAllCategoryButton: Boolean) {
    assertThat(this, instanceOf(TitleDataView::class.java))

    val titleDataView = this as TitleDataView
    assertThat(titleDataView.title, shouldBe(title))
    assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(hasSeeAllCategoryButton))
}

fun Visitable<*>.assertQuickFilterDataView() {
    assertThat(this, instanceOf(QuickFilterDataView::class.java))

    //TODO: assert quick filter data here
    val quickFilterDataView = this as QuickFilterDataView

}

fun Visitable<*>.assertProductCountDataView(productCount: Int) {
    assertThat(this, instanceOf(ProductCountDataView::class.java))

    val productCountDataView = this as ProductCountDataView
    assertThat(productCountDataView.totalData, shouldBe(productCount))
}

fun verifyProductItemDataViewList(
        expectedProductList: List<Product>,
        actualProductItemDataViewList: List<ProductItemDataView>,
) {
    assertThat(actualProductItemDataViewList.size, shouldBe(expectedProductList.size))

    expectedProductList.forEachIndexed { index, expectedProduct ->
        val actualProductDataView = actualProductItemDataViewList[index]

        assertThat(actualProductDataView.id, shouldBe(expectedProduct.id))
        assertThat(actualProductDataView.imageUrl300, shouldBe(expectedProduct.imageUrl300))
        assertThat(actualProductDataView.name, shouldBe(expectedProduct.name))
        assertThat(actualProductDataView.price, shouldBe(expectedProduct.price))
        assertThat(actualProductDataView.priceInt, shouldBe(expectedProduct.priceInt))
    }
}
