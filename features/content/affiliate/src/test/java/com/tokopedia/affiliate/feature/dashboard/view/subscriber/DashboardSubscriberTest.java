package com.tokopedia.affiliate.feature.dashboard.view.subscriber;


import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardAffiliateCheck;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardHeaderPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardItemPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardPagingPojo;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardProduct;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuery;
import com.tokopedia.affiliate.feature.dashboard.data.pojo.DashboardQuotaStatus;
import com.tokopedia.affiliate.feature.dashboard.view.listener.DashboardContract;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class DashboardSubscriberTest {

    @Mock
    DashboardContract.View mainView;

    private GetDashboardSubscriber getDashboardSubscriber;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getDashboardSubscriber = new GetDashboardSubscriber(mainView);
    }

    @Test
    public void getDashboard_onNext_contains_allData() {
//        GraphqlResponse response = mock(GraphqlResponse.class);
//        DashboardQuery query = mock(DashboardQuery.class);
//        DashboardProduct product = mock(DashboardProduct.class);
//        DashboardPagingPojo pagingPojo = mock(DashboardPagingPojo.class);
//        List<DashboardItemPojo> itemList = mock(List.class);
//        DashboardItemPojo item = mock(DashboardItemPojo.class);
//        DashboardHeaderPojo headerPojo = mock(DashboardHeaderPojo.class);
//        DashboardAffiliateCheck affiliateCheck = mock(DashboardAffiliateCheck.class);
//        DashboardQuotaStatus quotaStatus = mock(DashboardQuotaStatus.class);
//
//        when(response.getData(DashboardQuery.class)).thenReturn(query);
//        when(query.getProduct()).thenReturn(product);
//
//        when(product.getPagination()).thenReturn(pagingPojo);
//        when(pagingPojo.getNextCursor()).thenReturn("");
//        when(itemList.isEmpty()).thenReturn(false);
//        when(itemList.get(0)).thenReturn(item);
//        when(item.getId()).thenReturn("1");
//
//        when(query.getAffiliateStats()).thenReturn(headerPojo);
//        when(headerPojo.getTotalCommission()).thenReturn("commission");
//
//        when(query.getAffiliateCheck()).thenReturn(affiliateCheck);
//        when(affiliateCheck.isAffiliate()).thenReturn(true);
//
//        when(query.getPostQuota()).thenReturn(quotaStatus);
//        when(quotaStatus.getNumber()).thenReturn(10);
//
//        getDashboardSubscriber.onNext(response);
//
//        verify(mainView, times(1)).onSuccessGetDashboardItem(any(), anyList(), anyString(), any());
    }
}
