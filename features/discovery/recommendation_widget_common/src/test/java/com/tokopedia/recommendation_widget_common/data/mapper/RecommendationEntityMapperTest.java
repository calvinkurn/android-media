package com.tokopedia.recommendation_widget_common.data.mapper;

import com.tokopedia.recommendation_widget_common.data.RecomendationEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class RecommendationEntityMapperTest {

    private RecommendationEntityMapper recommendationEntityMapper;

    @Before
    public void initData() {
        this.recommendationEntityMapper = new RecommendationEntityMapper();
    }
}