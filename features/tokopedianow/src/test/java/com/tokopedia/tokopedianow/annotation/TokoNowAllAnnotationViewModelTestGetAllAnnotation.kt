package com.tokopedia.tokopedianow.annotation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.annotation.domain.mapper.AllAnnotationMapper.mapToAnnotationUiModels
import com.tokopedia.tokopedianow.data.AllAnnotationDataFactory.createFirstPageData
import com.tokopedia.tokopedianow.data.AllAnnotationDataFactory.createFirstPageLoadMoreData
import com.tokopedia.tokopedianow.data.AllAnnotationDataFactory.createSecondPageHasLoadMoreData
import com.tokopedia.tokopedianow.data.AllAnnotationDataFactory.createSecondPageNoLoadMoreData
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class TokoNowAllAnnotationViewModelTestGetAllAnnotation: TokoNowAllAnnotationViewModelTestFixture() {
    @Test
    fun `when hitting all annotation query should get successful result`() {
        val pageLastId = String.EMPTY
        val response = createFirstPageData()

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = response
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        viewModel
            .firstPage
            .verifyValueEquals(
                Success(
                    response.mapToAnnotationUiModels()
                )
            )

        viewModel
            .headerTitle
            .verifyValueEquals(
                Success(
                    response.annotationHeader.title
                )
            )
    }

    @Test
    fun `when hitting all annotation query should get failed result`() {
        val pageLastId = String.EMPTY
        val throwable = Throwable()

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            throwable = throwable
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        viewModel
            .firstPage
            .verifyErrorEquals(
                Fail(
                    throwable
                )
            )

        viewModel
            .headerTitle
            .verifyValueEquals(
                Fail(
                    throwable
                )
            )
    }

    @Test
    fun `when hitting all annotation query to load more but scroll not needed anymore`() {
        val pageLastId = String.EMPTY
        val response = createFirstPageData()

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = response
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        viewModel
            .isOnScrollNotNeeded
            .verifyValueEquals(Unit)
    }

    @Test
    fun `when hitting all annotation query to load more then should add first and second page data with next page is true`() {
        val pageLastId = String.EMPTY
        val firstPageData = createFirstPageLoadMoreData()
        val secondPageData = createSecondPageHasLoadMoreData()
        val expectedLayout = mutableListOf<Visitable<*>>()

        expectedLayout.addAll(firstPageData.mapToAnnotationUiModels() + secondPageData.mapToAnnotationUiModels())

        /**
         * get first page
         */

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = firstPageData
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        /**
         * load more
         */

        stubGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID,
            response = secondPageData
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        verifyGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID
        )

        Assert.assertTrue(viewModel.loadMore.value?.last() is LoadingMoreModel)
    }

    @Test
    fun `when hitting all annotation query to load more but not yet getting first page should do nothing`() {
        val firstPageData = createFirstPageLoadMoreData()
        val secondPageData = createSecondPageHasLoadMoreData()

        stubGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID,
            response = secondPageData
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        viewModel
            .loadMore
            .verifyValueEquals(null)
    }

    @Test
    fun `when hitting all annotation query to load more then should add first and second page data`() {
        val pageLastId = String.EMPTY
        val firstPageData = createFirstPageLoadMoreData()
        val secondPageData = createSecondPageNoLoadMoreData()
        val expectedLayout = mutableListOf<Visitable<*>>()

        expectedLayout.addAll(firstPageData.mapToAnnotationUiModels() + secondPageData.mapToAnnotationUiModels())

        /**
         * get first page
         */

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = firstPageData
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        /**
         * load more
         */

        stubGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID,
            response = secondPageData
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        verifyGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID
        )

        viewModel
            .loadMore
            .verifyValueEquals(expectedLayout)
    }

    @Test
    fun `when hitting all annotation query to load more but annotation list is empty then should get the same data as before`() {
        val pageLastId = String.EMPTY
        val firstPageData = createFirstPageLoadMoreData()
        val secondPageData = createSecondPageNoLoadMoreData(listOf())
        val expectedLayout = firstPageData.mapToAnnotationUiModels()

        /**
         * get first page
         */

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = firstPageData
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        /**
         * load more
         */

        stubGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID,
            response = secondPageData
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        verifyGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID
        )

        viewModel
            .loadMore
            .verifyValueEquals(expectedLayout)
    }

    @Test
    fun `when hitting all annotation query to load more and failed should get the same data as before`() {
        val pageLastId = String.EMPTY
        val throwable = Throwable()
        val firstPageData = createFirstPageLoadMoreData()
        val expectedLayout = firstPageData.mapToAnnotationUiModels()

        /**
         * get first page
         */

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = firstPageData
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        /**
         * load more
         */

        stubGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID,
            throwable = throwable
        )

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = true
        )

        verifyGetAllAnnotation(
            pageLastId = firstPageData.pagination.pageLastID
        )

        viewModel
            .loadMore
            .verifyValueEquals(expectedLayout)
    }

    @Test
    fun `when try to load more but position not at the bottom of the page should not load more data`() {
        val pageLastId = String.EMPTY
        val firstPageData = createFirstPageLoadMoreData()

        /**
         * get first page
         */

        stubGetAllAnnotation(
            pageLastId = pageLastId,
            response = firstPageData
        )

        viewModel.getFirstPage(
            categoryId = categoryId,
            annotationType = annotationType
        )

        verifyGetAllAnnotation(
            pageLastId = pageLastId
        )

        /**
         * load more
         */

        viewModel.loadMore(
            categoryId = categoryId,
            annotationType = annotationType,
            isLastVisibleLoadingMore = false
        )

        viewModel
            .isOnScrollNotNeeded
            .verifyValueEquals(null)

        viewModel
            .loadMore
            .verifyValueEquals(null)
    }
}
