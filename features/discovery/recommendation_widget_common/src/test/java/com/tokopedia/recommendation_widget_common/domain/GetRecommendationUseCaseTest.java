package com.tokopedia.recommendation_widget_common.domain;

import android.content.Context;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GetRecommendationUseCaseTest {

    @Mock
    Context context;

    @Mock
    UserSessionInterface userSessionInterface;

    @Mock
    GraphqlUseCase graphqlUseCase;

    GetRecommendationUseCase getRecommendationUseCase;

    @Before
    public void initData() {
        getRecommendationUseCase = new GetRecommendationUseCase(
                context,
                "",
                graphqlUseCase,
                userSessionInterface
        );
    }

    @Test
    public void getRecomParams_useUserSessionId_whenSetUserIdParams() {
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        int requestParamsId = requestParams.getInt(GetRecommendationUseCase.USER_ID,
                dummyPageNumber);
        Assert.assertEquals((long)requestParamsId, (long)Integer.valueOf(dummyUserId));
    }

    @Test
    public void getRecomParams_useGivenPageNumber_whenSetPageNumber() {
        int dummyPageNumber = 99;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        int requestParamsPageNumber = requestParams.getInt(GetRecommendationUseCase.PAGE_NUMBER,
                0);

        Assert.assertEquals(requestParamsPageNumber, dummyPageNumber);
    }

    @Test
    public void getRecomParams_useGivenXSource_whenSetXSource() {
        String dummyXSource = "test_widget";
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                dummyXSource,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        String requestXSource = requestParams.getString(GetRecommendationUseCase.X_SOURCE,
                "");

        Assert.assertEquals(requestXSource, dummyXSource);
    }

    @Test
    public void getRecomParams_useDefaultXSource_whenXSourceNotDefined() {
        String dummyXSource = "";
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                dummyXSource,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        String requestXSource = requestParams.getString(GetRecommendationUseCase.X_SOURCE,
                "");

        Assert.assertEquals(requestXSource,
                GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE);
    }

    @Test
    public void getRecomParams_useDefaultXDevice_inAnyRecomParams() {
        String dummyXSource = "test_widget";
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                dummyXSource,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        String requestXDevice = requestParams.getString(GetRecommendationUseCase.X_DEVICE,
                "");

        Assert.assertEquals(requestXDevice,
                GetRecommendationUseCase.DEFAULT_VALUE_X_DEVICE);
    }

    @Test
    public void getRecomParams_useGivenPageName_whenSetPageName() {
        String dummyPageName = "test_page";
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                GetRecommendationUseCase.DEFAULT_VALUE_X_SOURCE,
                dummyPageName,
                any(),
                any()
        );
        String requestPageName = requestParams.getString(GetRecommendationUseCase.PAGE_NAME,
                "");

        Assert.assertEquals(requestPageName, dummyPageName);
    }

    @Test
    public void getRecomParams_useDefaultPageName_whenPageNameNotDefined() {
        String dummyPageName = "";
        int dummyPageNumber = 0;
        String dummyUserId = "12345";
        when(userSessionInterface.getUserId()).thenReturn(dummyUserId);

        RequestParams requestParams = getRecommendationUseCase.getRecomParams(
                dummyPageNumber,
                dummyPageName,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME,
                any(),
                any()
        );
        String requestPageName = requestParams.getString(GetRecommendationUseCase.PAGE_NAME,
                "");

        Assert.assertEquals(requestPageName,
                GetRecommendationUseCase.DEFAULT_PAGE_NAME);
    }
}