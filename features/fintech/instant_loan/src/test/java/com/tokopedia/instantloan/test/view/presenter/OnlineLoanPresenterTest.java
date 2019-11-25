package com.tokopedia.instantloan.test.view.presenter;


import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase;
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor;
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OnlineLoanPresenter.class})
public class OnlineLoanPresenterTest {

    OnlineLoanPresenter onlineLoanPresenter;
    OnlineLoanContractor.View view;
    GetFilterDataUseCase mGetFilterDataUseCase;


    @Before
    public void setUp() throws Exception {
        view = PowerMockito.mock(OnlineLoanContractor.View.class);
//        mGetFilterDataUseCase = mockk();
        onlineLoanPresenter = new OnlineLoanPresenter(mGetFilterDataUseCase);
    }


    private void mockView() {
        onlineLoanPresenter.attachView(view);
    }


    @Test
    public void get_filter_data_test(){


    }
}
