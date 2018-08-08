package com.tokopedia.core.base.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author kulomady on 12/24/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestParamsTest {

    private RequestParams requestParams;

    @Before
    public void setUp() throws Exception {
        requestParams = RequestParams.create();
    }

    @Test
    public void testReturnIntegerWhenGetInt() throws Exception {
        requestParams.putInt("key01",3);
        assertThat(requestParams.getInt("key01", 5)).isEqualTo(3);
    }

    @Test
    public void testReturnStringWhenGetString() throws Exception {
        requestParams.putString("key02","testParams");
        assertThat(requestParams.getString("key02", "defaultParams")).isEqualTo("testParams");
    }

}