package com.tokopedia.home.account.presentation.view.buyercardview;

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
        buyerCardPresenter.setData(buyerCard);
        mockView();

        // then
        Mockito.verify(view).showProfileProgress(20);
    }
}
