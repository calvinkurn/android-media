package com.tokopedia.autocomplete.suggestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldBeInstanceOf
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionTopShop
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineViewModel
import com.tokopedia.autocomplete.suggestion.doubleline.SuggestionDoubleLineWithoutImageViewModel
import com.tokopedia.autocomplete.suggestion.singleline.SuggestionSingleLineViewModel
import com.tokopedia.autocomplete.suggestion.title.SuggestionTitleViewModel
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardViewModel
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopWidgetViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

private const val suggestionCommonResponse = "autocomplete/suggestion/suggestion-common-response.json"
private const val suggestionTopShopResponse = "autocomplete/suggestion/suggestion-top-shop-response.json"
private const val suggestionCampaignResponse = "autocomplete/suggestion/local-global-response.json"
private const val suggestionCampaignAtTopResponse = "autocomplete/suggestion/local-global-at-top-response.json"

internal class SuggestionPresenterTest: SuggestionPresenterTestFixtures() {

    @Test
    fun `test get suggestion data`() {
        val suggestionUniverse = suggestionCommonResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list`(suggestionUniverse)
    }

    private fun `given suggestion use case capture request params`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
    }

    private fun `when presenter get suggestion data (search)`() {
        suggestionPresenter.search()
    }

    private fun `then verify suggestion API is called`() {
        verify { getSuggestionUseCase.execute(any(), any()) }
    }

    private fun `then verify suggestion view will call showInitialStateResult behavior`() {
        verify {
            suggestionView.showSuggestionResult(capture(slotVisitableList))
        }
    }

    private fun `then verify visitable list`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[2].shouldBeInstanceOf<SuggestionTitleViewModel>()
        visitableList[3].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[4].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[5].shouldBeInstanceOf<SuggestionTitleViewModel>()
        visitableList[6].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[7].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse)
    }

    private fun assertVisitableListData(visitableList: List<Visitable<*>>, suggestionUniverse: SuggestionUniverse) {
        var expectedPosition = 0
        visitableList.forEach { visitable ->
            val expectedItem = suggestionUniverse.data.items[expectedPosition]
            when(visitable) {
                is SuggestionTitleViewModel -> {
                    visitable.assertSuggestionTitleViewModel(expectedItem)
                    expectedPosition++
                }
                is SuggestionTopShopWidgetViewModel -> {
                    visitable.assertTopShopWidgetViewModel(expectedItem, suggestionUniverse.topShop)
                    expectedPosition++
                }
                is BaseSuggestionViewModel -> {
                    (visitable as BaseSuggestionViewModel).assertBaseSuggestionViewModel(expectedItem)
                    expectedPosition++
                }
            }
        }
    }

    private fun SuggestionTitleViewModel.assertSuggestionTitleViewModel(item: SuggestionItem) {
        title shouldBe item.title
    }

    private fun SuggestionTopShopWidgetViewModel.assertTopShopWidgetViewModel(item: SuggestionItem, listExpectedData: List<SuggestionTopShop>) {
        template shouldBe item.template
        title shouldBe item.title

        listSuggestionTopShopCard.forEachIndexed { index, suggestionTopShopCardViewModel ->
            suggestionTopShopCardViewModel.assertSuggestionTopShopCardViewModel(listExpectedData[index])
        }
    }

    private fun SuggestionTopShopCardViewModel.assertSuggestionTopShopCardViewModel(suggestionTopShop: SuggestionTopShop) {
        type shouldBe suggestionTopShop.type
        id shouldBe suggestionTopShop.id
        applink shouldBe suggestionTopShop.applink
        url shouldBe suggestionTopShop.url
        title shouldBe suggestionTopShop.title
        subtitle shouldBe suggestionTopShop.subtitle
        iconTitle shouldBe suggestionTopShop.iconTitle
        iconSubtitle shouldBe suggestionTopShop.iconSubtitle
        urlTracker shouldBe suggestionTopShop.urlTracker
        imageUrl shouldBe suggestionTopShop.imageUrl

        products.forEachIndexed { index, suggestionTopShopProductViewModel ->
            suggestionTopShopProductViewModel.imageUrl shouldBe suggestionTopShop.topShopProducts[index].imageUrl
        }
    }

    private fun BaseSuggestionViewModel.assertBaseSuggestionViewModel(item: SuggestionItem) {
        template shouldBe item.template
        type shouldBe item.type
        applink shouldBe item.applink
        url shouldBe item.url
        title shouldBe item.title
        subtitle shouldBe item.subtitle
        iconTitle shouldBe item.iconTitle
        iconSubtitle shouldBe item.iconSubtitle
        shortcutUrl shouldBe item.shortcutUrl
        shortcutImage shouldBe item.shortcutImage
        imageUrl shouldBe item.imageUrl
        label shouldBe item.label
        labelType shouldBe item.labelType
        urlTracker shouldBe item.urlTracker
        trackingCode shouldBe item.tracking.code
    }

    @Test
    fun `test fail to get initial state data`() {
        `given suggestion API will return error`()
        `when presenter get suggestion data (search)`()
        `then verify suggestion API is called`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given suggestion API will return error`() {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onError(testException)
        }
    }

    private fun `then verify initial state view do nothing behavior`() {
        confirmVerified(suggestionView)
    }

    @Test
    fun `test get suggestion data with top shop`() {
        val suggestionUniverse = suggestionTopShopResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params with top shop`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list with top shop`(suggestionUniverse)
    }

    private fun `given suggestion use case capture request params with top shop`(suggestionUniverse: SuggestionUniverse) {
        every { getSuggestionUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<SuggestionUniverse>>().onStart()
            secondArg<Subscriber<SuggestionUniverse>>().onNext(suggestionUniverse)
        }
    }

    private fun `then verify visitable list with top shop`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[2].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[3].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[4].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[5].shouldBeInstanceOf<SuggestionTitleViewModel>()
        visitableList[6].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[7].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[8].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[9].shouldBeInstanceOf<SuggestionTopShopWidgetViewModel>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse)
    }

    @Test
    fun `test get suggestion data with campaign local global component`() {
        val suggestionUniverse = suggestionCampaignResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list should have SuggestionDoubleLineWithoutImageViewModel`(suggestionUniverse)
    }

    private fun `then verify visitable list should have SuggestionDoubleLineWithoutImageViewModel`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[1].shouldBeInstanceOf<SuggestionSingleLineViewModel>()
        visitableList[2].shouldBeInstanceOf<SuggestionTitleViewModel>()
        visitableList[3].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[4].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[5].shouldBeInstanceOf<SuggestionTitleViewModel>()
        visitableList[6].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[7].shouldBeInstanceOf<SuggestionDoubleLineViewModel>()
        visitableList[8].shouldBeInstanceOf<SuggestionSeparatorViewModel>()
        visitableList[9].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageViewModel>()
        visitableList[10].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageViewModel>()
        visitableList.size shouldBe suggestionUniverse.data.items.size + 1

        assertVisitableListData(visitableList, suggestionUniverse)
    }

    @Test
    fun `test get suggestion data with campaign local global component at the top`() {
        val suggestionUniverse = suggestionCampaignAtTopResponse.jsonToObject<SuggestionUniverse>()
        `given suggestion use case capture request params`(suggestionUniverse)

        `when presenter get suggestion data (search)`()

        `then verify suggestion API is called`()
        `then verify suggestion view will call showInitialStateResult behavior`()
        `then verify visitable list should only have SuggestionDoubleLineWithoutImageViewModel`(suggestionUniverse)
    }

    private fun `then verify visitable list should only have SuggestionDoubleLineWithoutImageViewModel`(suggestionUniverse: SuggestionUniverse) {
        val visitableList = slotVisitableList.captured

        visitableList[0].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageViewModel>()
        visitableList[1].shouldBeInstanceOf<SuggestionDoubleLineWithoutImageViewModel>()
        visitableList.size shouldBe suggestionUniverse.data.items.size

        assertVisitableListData(visitableList, suggestionUniverse)
    }
}