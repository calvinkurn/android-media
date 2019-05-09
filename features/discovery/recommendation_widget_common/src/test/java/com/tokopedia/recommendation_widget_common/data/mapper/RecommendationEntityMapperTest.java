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

    //pass null data, it should not return null object because we already implement NullChecker
    @Test
    public void mapper_isReturningNotNullObject_whenRecommendationDataIsNull() {
        assertThat(
                recommendationEntityMapper.call(getMockRecommendationDataWithNull()), notNullValue());
    }

    @Test
    public void mapper_isReturningNotNullObject_whenRecommendationDataIsNotNull() {
        assertThat(
                recommendationEntityMapper.call(getMockRecommendationData()), notNullValue());
    }

    private RecomendationEntity.RecomendationData getMockRecommendationData() {
        return new RecomendationEntity.RecomendationData();
    }

    private RecomendationEntity.RecomendationData getMockRecommendationDataWithNull() {
        RecomendationEntity.RecomendationData mockData = new RecomendationEntity.RecomendationData();
        mockData.setTitle(null);
        mockData.setForeignTitle(null);
        mockData.setRecommendation(null);
        mockData.setSource(null);
        mockData.setWidgetUrl(null);
        mockData.setTid(null);
        return mockData;
    }
}