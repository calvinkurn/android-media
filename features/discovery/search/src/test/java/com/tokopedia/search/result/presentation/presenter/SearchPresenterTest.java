package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.common.repository.gql.GqlSpecification;
import com.tokopedia.graphql.domain.GraphqlUseCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class SearchPresenterTest {

    @Mock
    GraphqlUseCase graphqlUseCase;
    @Mock
    GqlSpecification gqlSpecification;

    private SearchPresenter searchPresenter;

    @Before
    public void setUp() throws Exception {
        searchPresenter = new SearchPresenter(graphqlUseCase, gqlSpecification);
    }

    // No expected result here, just making sure no errors happen
    // Should also make sure the GraphQL method is not called. Can only be tested when the Gql calls is not static anymore
    @Test
    public void initiateSearch_GivenNulls_ShouldNotError() {
        searchPresenter.initiateSearch(null, false, null);
    }

    @After
    public void tearDown() throws Exception {
        graphqlUseCase = null;
        gqlSpecification = null;
        searchPresenter = null;
    }
}