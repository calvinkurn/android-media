package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

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