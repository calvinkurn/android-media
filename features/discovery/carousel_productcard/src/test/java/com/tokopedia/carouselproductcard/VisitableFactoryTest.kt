package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.carouselproductcard.paging.HasGroup
import com.tokopedia.carouselproductcard.paging.VisitableFactory
import com.tokopedia.carouselproductcard.paging.list.ProductCardListDataView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class VisitableFactoryTest {

    @Test
    fun `carousel paging model to visitable list of product card list`() {
        val groupSize = 2
        val productCardSize = 10
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(groupSize) {
                CarouselPagingGroupProductModel(
                    group = carouselPagingGroupModel(it),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val expectedVisitableSize = groupSize * productCardSize
        assertEquals(expectedVisitableSize, visitableList.size)

        visitableList.forEachIndexed { visitableIndex, visitable ->
            assertThat(visitable, `is`(instanceOf(ProductCardListDataView::class.java)))

            val productCardListDataView = visitable as ProductCardListDataView

            val groupIndex = visitableIndex / productCardSize
            val expectedProductIndex = visitableIndex % productCardSize
            val expectedProductCardModel = productCardModel()
            val expectedPageInGroup = (expectedProductIndex / itemPerPage) + 1
            val expectedPageCount = (productCardSize / itemPerPage) + 1

            assertEquals(expectedProductCardModel, productCardListDataView.productCardModel)
            assertEquals(carouselPagingGroupModel(groupIndex), productCardListDataView.group)
            assertEquals(expectedPageInGroup, productCardListDataView.pageInGroup)
            assertEquals(expectedPageCount, productCardListDataView.pageCount)
            assertEquals(expectedProductIndex, productCardListDataView.productIndex)
        }
    }

    private fun carouselPagingGroupModel(it: Int) =
        CarouselPagingGroupModel("Test Group ${it + 1}")

    private fun productCardList(productCardSize: Int) =
        List(productCardSize) { productCardModel() }

    private fun productCardModel() = ProductCardModel(
        productImageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        productName = "1 Line Product here lorem ips...",
        formattedPrice = "Rp10.000",
        slashedPrice = "Rp100.0000",
        discountPercentage = "50%",
        labelGroupList = listOf(
            ProductCardModel.LabelGroup(
                position = "ribbon",
                type = "gold",
                title = "#1"
            ),
            ProductCardModel.LabelGroup(
                position = "integrity",
                title = "Terjual 122",
                type = "#ae31353b"
            ),
        ),
        countSoldRating = "4.5",
        freeOngkir = ProductCardModel.FreeOngkir(
            isActive = true,
            imageUrl = "https://images.tokopedia.net/img/ic_bebas_ongkir.png"
        ),
        cardType = CardUnify2.TYPE_CLEAR,
    )

    @Test
    fun `product card list item will have span size of 1 when page has full item`() {
        val productCardSize = 3
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(1) {
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        visitableList.filterIsInstance<ProductCardListDataView>().forEach {
            assertEquals(1, it.spanSize)
        }
    }

    @Test
    fun `last product card list item in a page will have span size up to item per page (size 2)`() {
        val productCardSize = 2
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(1) {
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val productCardItemList = visitableList.filterIsInstance<ProductCardListDataView>()
        assertEquals(1, productCardItemList.first().spanSize)
        assertEquals(2, productCardItemList.last().spanSize)
    }

    @Test
    fun `last product card list item in a page will have span size up to item per page (size 1)`() {
        val productCardSize = 1
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(1) {
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val productCardItemList = visitableList.filterIsInstance<ProductCardListDataView>()
        assertEquals(3, productCardItemList.first().spanSize)
    }

    @Test
    fun `product card list item page depends on number of items per page`() {
        val productCardSize = 6
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(1) {
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val productCardItemList = visitableList.filterIsInstance<ProductCardListDataView>()
        println("page count : ${pagingModel.productCardGroupList.first().getPageCount(itemPerPage)}")
        productCardItemList.forEachIndexed { index, productItem ->
            assertEquals((index / itemPerPage) + 1, productItem.page)
        }
    }

    @Test
    fun `product card list item page will continuously increase based on group`() {
        val groupSize = 2
        val productCardSize = 6
        val pagingModel = CarouselPagingModel(
            productCardGroupList = List(groupSize) {
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(),
                    productItemList = productCardList(productCardSize),
                )
            }
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val productCardItemList = visitableList.filterIsInstance<ProductCardListDataView>()
        productCardItemList.forEachIndexed { index, productItem ->
            assertEquals((index / itemPerPage) + 1, productItem.page)
        }
    }

    @Test
    fun `group without product item list will have shimmering in visitable list`() {
        val productCardSize = 6
        val emptyGroupModel = CarouselPagingGroupModel(title = "empty group")
        val pagingModel = CarouselPagingModel(
            productCardGroupList = listOf(
                CarouselPagingGroupProductModel(
                    group = CarouselPagingGroupModel(title = "group 1"),
                    productItemList = productCardList(productCardSize),
                ),
                CarouselPagingGroupProductModel(
                    group = emptyGroupModel,
                    productItemList = listOf(),
                )
            )
        )
        val itemPerPage = 3

        val visitableList = VisitableFactory.from(pagingModel, itemPerPage)

        val loadingItemVisitableList = visitableList.last()

        assertThat(loadingItemVisitableList, `is`(instanceOf(HasGroup::class.java)))
        assertEquals(emptyGroupModel, (loadingItemVisitableList as HasGroup).group)
    }
}
