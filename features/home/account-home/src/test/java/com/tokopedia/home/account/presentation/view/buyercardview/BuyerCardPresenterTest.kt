package com.tokopedia.home.account.presentation.view.buyercardview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BuyerCardPresenterTest {

    @RelaxedMockK
    private lateinit var buyerView: BuyerCardContract.View

    private lateinit var buyerCard: BuyerCard
    private val buyerPresenter by lazy {
        BuyerCardPresenter()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        buyerPresenter.attachView(buyerView)
        buyerCard = BuyerCard()
    }

    @Test
    fun `set data | status completed`() {

        buyerPresenter.setData(buyerCard)

        verify {
            buyerView.showCompletedAvatar(buyerCard.avatar)
        }

        verify {
            buyerView.setName(buyerCard.username)
            buyerView.setTokopoint(buyerCard.tokopointAmount)
            buyerView.setCoupon(buyerCard.couponAmount)
            buyerView.setTokoMemberAmount(buyerCard.tokoMemberAmount)
            buyerView.setEggImage(buyerCard.eggImageUrl)
            buyerView.setTokopointImageUrl(buyerCard.tokopointImageUrl)
            buyerView.setTokopointTitle(buyerCard.tokopointTitle)
            buyerView.setCouponImageUrl(buyerCard.couponImageUrl)
            buyerView.setCouponTitle(buyerCard.couponTitle)
            buyerView.setTokomemberImageUrl(buyerCard.tokopointImageUrl)
            buyerView.setTokoMemberTitle(buyerCard.tokomemberTitle)
        }
    }

    @After
    fun tearDown() {
        buyerPresenter.detachView()
    }
}