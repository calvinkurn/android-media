package com.tokopedia.home.account.presentation.view.buyercardview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author okasurya on 8/30/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BuyerCardPresenter.class})
public class BuyerCardPresenterTest {
    BuyerCardPresenter buyerCardPresenter;
    BuyerCardContract.View view;

    @Before
    public void setUp() throws Exception {
        view = PowerMockito.mock(BuyerCardContract.View.class);
        buyerCardPresenter = new BuyerCardPresenter();
    }

    private void mockView() {
        buyerCardPresenter.attachView(view);
    }

    @Test
    public void renderData_incompleteProfile_showProgress() {
        // given
        BuyerCard buyerCard  = new BuyerCard.Builder().progress(20).build();

        // when
        mockView();
        buyerCardPresenter.setData(buyerCard);

        // then
        Mockito.verify(view).setProfileStatusIncomplete(20);
        Mockito.verify(view).showProfileProgress(20);
    }

    @Test
    public void renderData_incompleteProfile_showIncompleteAvatar() {
        // given
        BuyerCard buyerCard  = new BuyerCard.Builder().progress(20).avatar("http://avatar").build();

        // when
        mockView();
        buyerCardPresenter.setData(buyerCard);

        // then
        Mockito.verify(view).showIncompleteAvatar("http://avatar");
    }

    @Test
    public void renderData_completeProfile_hideProgress() {
        // given
        BuyerCard buyerCard  = new BuyerCard.Builder().progress(100).build();

        // when
        mockView();
        buyerCardPresenter.setData(buyerCard);

        // then
        Mockito.verify(view).setProfileStatusCompleted();
        Mockito.verify(view).hideProfileProgress();
    }

    @After
    public void tearDown() {
        buyerCardPresenter.detachView();
    }
}
