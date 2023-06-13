package com.tokopedia.shop.score.cassavatest.base

import androidx.test.espresso.Espresso
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.BaseShopPenaltyTest
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.stub.common.util.clickClickableSpan
import com.tokopedia.shop.score.stub.common.util.getHyperlinkText
import com.tokopedia.shop.score.stub.common.util.getShopPenaltyFragment
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.MatcherAssert
import org.junit.Rule

open class ShopPenaltyCassavaTest : BaseShopPenaltyTest() {

    companion object {
        const val CLICK_LEARN_MORE_PENALTY_PATH =
            "tracker/merchant/shop_score/penalty/penalty_click_learn_more.json"
        const val CLICK_HELP_CENTER_PENALTY_DETAIL_PATH =
            "tracker/merchant/shop_score/penalty/penalty_detail_click_learn_more_help_center.json"
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected fun validate(fileName: String) {
        activityRule.finishActivity()
        Thread.sleep(3000)
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    protected fun clickLearnMorePenalty() {
        onIdView(R.id.tvContentPenalty).isViewDisplayed().perform(
            clickClickableSpan(
                getHyperlinkText(
                    context,
                    context.getString(R.string.content_penalty_label)
                )
            )
        )
        validate(CLICK_LEARN_MORE_PENALTY_PATH)
    }

    protected fun clickHelpCenterPenaltyDetail() {
        val recyclerViewMatcher = RecyclerViewMatcher(R.id.rvPenaltyPage)
        val itemPenaltyPosition =
            activityRule.activity.getShopPenaltyFragment().penaltyPageAdapter.list.indexOfFirst {
                it is ItemPenaltyUiModel
            }
        activityRule.activity.scrollTo<ItemPenaltyUiModel>()
        Espresso.onView(
            recyclerViewMatcher.atPositionOnView(
                itemPenaltyPosition,
                R.id.cardItemPenalty
            )
        ).onClick()
        onIdView(R.id.btnCallHelpCenter).isViewDisplayed().onClick()
        validate(CLICK_HELP_CENTER_PENALTY_DETAIL_PATH)
    }
}