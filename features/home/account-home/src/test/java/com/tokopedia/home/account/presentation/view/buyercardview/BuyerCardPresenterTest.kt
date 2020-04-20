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
    fun `set data | status incomplete`() {
        buyerCard.progress = 80

        buyerPresenter.setData(buyerCard)

        verify {
            buyerView.showProfileProgress(buyerCard.progress)
            buyerView.showIncompleteAvatar(buyerCard.avatar)
            buyerView.setProfileStatusIncomplete(buyerCard.progress)
        }
        
        verify {
            buyerView.setAvatarImageUrl(buyerCard.progress, buyerCard.avatar)
            buyerView.setName(buyerCard.username)
            buyerView.setTokopoint(buyerCard.tokopointAmount)
            buyerView.setCoupon(buyerCard.couponAmount)
            buyerView.setTokoMemberAmount(buyerCard.tokoMemberAmount)
            buyerView.setEggImage(buyerCard.eggImageUrl)
        }
    }

    @Test
    fun `set data | status completed`() {
        buyerCard.progress = 100

        buyerPresenter.setData(buyerCard)

        verify {
            buyerView.hideProfileProgress()
            buyerView.showCompletedAvatar(buyerCard.avatar)
            buyerView.setProfileStatusCompleted()
        }
        
        verify {
            buyerView.setAvatarImageUrl(buyerCard.progress, buyerCard.avatar)
            buyerView.setName(buyerCard.username)
            buyerView.setTokopoint(buyerCard.tokopointAmount)
            buyerView.setCoupon(buyerCard.couponAmount)
            buyerView.setTokoMemberAmount(buyerCard.tokoMemberAmount)
            buyerView.setEggImage(buyerCard.eggImageUrl)
        }
    }

    @After
    fun tearDown() {
        buyerPresenter.detachView()
    }
}