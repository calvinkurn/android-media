package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val ticker = "searchproduct/ticker/ticker.json"
private const val showAdult = "searchproduct/safesearch/show_adult.json"
private const val productUserDobAdult = "searchproduct/blurredimage/product-user-dob-adult.json"
private const val productUserDobYoung = "searchproduct/blurredimage/product-user-dob-young.json"
private const val productUserDobUnVerified = "searchproduct/blurredimage/product-user-dob-unverified.json"

internal class SearchProductSafeSearchTest : ProductListPresenterTestFixtures() {
    private val showAdultSearchProductModel = showAdult.jsonToObject<SearchProductModel>()
    private val tickerSearchProductModel = ticker.jsonToObject<SearchProductModel>()
    private val productUserDobAdultModel = productUserDobAdult.jsonToObject<SearchProductModel>()
    private val productUserDobYoungModel = productUserDobYoung.jsonToObject<SearchProductModel>()
    private val productUserDobUnVerifiedModel = productUserDobUnVerified.jsonToObject<SearchProductModel>()
    private val keyword = "bawahan wanita remaja"
    private val searchParameter = mapOf<String, Any>(
        SearchApiConst.Q to keyword,
    )
    private val visitableListSlot = slot<List<Visitable<*>>>()
    private val visitableList by lazy { visitableListSlot.captured }

    @Test
    fun `click show_adult ticker should enable showAdult flag`() {
        `Given Search Product API return searchProductModel`(showAdultSearchProductModel)

        val showAdultTicker = getTickerDataView()
        `When show_adult ticker clicked`(showAdultTicker)

        `Then verify showAdult safeSearchPreference is enabled`()
        `Then verify SafeSearchLifecycleHelper is called`()
    }

    @Test
    fun `click other ticker should not enable showAdult flag`() {
        `Given Search Product API return searchProductModel`(tickerSearchProductModel)

        val showAdultTicker = getTickerDataView()
        `When show_adult ticker clicked`(showAdultTicker)

        `Then verify showAdult safeSearchPreference is not enabled`()
        `Then verify SafeSearchLifecycleHelper is not called`()
    }

    @Test
    fun `check is product not blurred when safe search disable, user 21yo and verified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Disable`()
        `Given Search Product API return searchProductModel`(productUserDobAdultModel)
        `Then verify is age, verified dob, enable show adult`(true)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = false
        )
    }

    @Test
    fun `check is product blurred when safe search enable, user 21yo and verified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Enable`()
        `Given Search Product API return searchProductModel`(productUserDobAdultModel)
        `Then verify is age, verified dob, enable show adult`(false)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = true
        )
    }

    @Test
    fun `check is product blurred when safe search disable, user under 21yo and verified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Disable`()
        `Given Search Product API return searchProductModel`(productUserDobYoungModel)
        `Then verify is age, verified dob, enable show adult`(false)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = true
        )
    }

    @Test
    fun `check is product blurred when safe search enable, user under 21yo and verified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Enable`()
        `Given Search Product API return searchProductModel`(productUserDobYoungModel)
        `Then verify is age, verified dob, enable show adult`(false)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = true
        )
    }

    @Test
    fun `check is product blurred when safe search disable, user unverified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Disable`()
        `Given Search Product API return searchProductModel`(productUserDobUnVerifiedModel)
        `Then verify is age, verified dob, enable show adult`(false)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = true
        )
    }

    @Test
    fun `check is product blurred when safe search enable, user unverified the dob`() {
        `Given search reimagine rollence product card will return non control variant`()
        `Given Safe Search Setting is Enable`()
        `Given Search Product API return searchProductModel`(productUserDobUnVerifiedModel)
        `Then verify is age, verified dob, enable show adult`(false)
        `Then verify visitable list with product items for reimagine`(
            visitableListSlot,
            productUserDobAdultModel,
            expectedBlurred = true
        )
    }

    private fun `Given Search Product API return searchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(any(), any()) } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }

        every {
            productListView.setProductList(capture(visitableListSlot))
        } just runs

        productListPresenter.loadData(searchParameter)
    }

    private fun `When show_adult ticker clicked`(tickerDataView: TickerDataView) {
        productListPresenter.showAdultForAdultTicker(tickerDataView)
    }

    private fun getTickerDataView() = visitableList.filterIsInstance<TickerDataView>().first()

    private fun `Then verify showAdult safeSearchPreference is enabled`() {
        verify {
            safeSearchPreference.isShowAdult = true
        }
    }

    private fun `Then verify SafeSearchLifecycleHelper is called`() {
        verify {
            safeSearchView.registerSameSessionListener(safeSearchPreference)
        }
    }

    private fun `Then verify showAdult safeSearchPreference is not enabled`() {
        verify(exactly = 0) {
            safeSearchPreference.isShowAdult = true
        }
    }

    private fun `Then verify SafeSearchLifecycleHelper is not called`() {
        verify(exactly = 0) {
            safeSearchView.registerSameSessionListener(safeSearchPreference)
        }
    }

    private fun `Given Safe Search Setting is Enable`() {
        every { safeSearchPreference.isShowAdult } returns false
    }

    private fun `Given Safe Search Setting is Disable`() {
        every { safeSearchPreference.isShowAdult } returns true
    }

    private fun `Then verify is age, verified dob, enable show adult`(
        isEnableAdultContent : Boolean,
    ) {
        productListPresenter.isShowAdultEnableAndProfileVerify() shouldBe isEnableAdultContent
    }
}
