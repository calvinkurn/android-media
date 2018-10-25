package com.tokopedia.affiliate.feature.dashboard.view.fragment;

import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author by yfsx on 25/10/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class DashboardFragmentTest {

    @Mock
    DashboardContract.View mainView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getDashobardFirst_completeData() {

//        DashboardHeaderViewModel header = mock(DashboardHeaderViewModel.class);
//        List<DashboardItemViewModel> itemList = mock(List.class);
//        DashboardItemViewModel item = mock(DashboardItemViewModel.class);
//        DashboardFloatingButtonViewModel floatingModel = mock(DashboardFloatingButtonViewModel.class);
//
//        when(header.getBuyCount()).thenReturn("10");
//        when(itemList.isEmpty()).thenReturn(false);
//        when(itemList.get(0)).thenReturn(item);
//        when(floatingModel.getCount()).thenReturn(10);
//
//        verify(mainView, times(1)).onSuccessGetDashboardItem(header, itemList, "", floatingModel);

    }
}
