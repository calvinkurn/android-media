package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.section

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.SectionUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class SectionViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: SectionViewModel =
        spyk(SectionViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

    }


    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

    }

    @Test
    fun `shouldShowError test`() {
        every { componentsItem.verticalProductFailState } returns true
        assert(viewModel.shouldShowError())
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowError())

    }

    @Test
    fun `reload calls test`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: SectionViewModel =
            spyk(SectionViewModel(application, componentsItem, 99))
        val sectionUseCase: SectionUseCase = mockk()
        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns false
        viewModel.sectionUseCase = sectionUseCase
        componentsItem.noOfPagesLoaded = 1
        viewModel.reload()
        assert(componentsItem.noOfPagesLoaded == 0)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }

    }

    @Test
    fun `onAttachVH flow test`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: SectionViewModel =
            spyk(SectionViewModel(application, componentsItem, 99))
        val sectionUseCase: SectionUseCase = mockk()
        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns false
        viewModel.sectionUseCase = sectionUseCase
        componentsItem.shouldRefreshComponent = true
        viewModel.onAttachToViewHolder()
        assert(componentsItem.shouldRefreshComponent == false)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }
    }

    @Test
    fun `testing positive flow of fetchData`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: SectionViewModel =
            spyk(SectionViewModel(application, componentsItem, 99))
        val sectionUseCase: SectionUseCase = mockk()
        viewModel.sectionUseCase = sectionUseCase

        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns false
        viewModel.onAttachToViewHolder()
        assert(componentsItem.shouldRefreshComponent == null || componentsItem.shouldRefreshComponent == false)
        assert(viewModel.syncData.value == null || viewModel.syncData.value == false)
        assert(viewModel.showErrorState.value == null || viewModel.showErrorState.value == false)
        assert(viewModel.hideShimmerLD.value == null || viewModel.hideShimmerLD.value == false)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }

        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns true
        viewModel.onAttachToViewHolder()
        assert(componentsItem.shouldRefreshComponent == true)
        assert(viewModel.syncData.value == true)
        assert(viewModel.showErrorState.value == null || viewModel.showErrorState.value == false)
        assert(viewModel.hideShimmerLD.value == null || viewModel.hideShimmerLD.value == false)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }

    }

    @Test
    fun `testing error flow of non-Network Exceptions fetchData`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: SectionViewModel =
            spyk(SectionViewModel(application, componentsItem, 99))
        val sectionUseCase: SectionUseCase = mockk()
        viewModel.sectionUseCase = sectionUseCase

        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }.throws(NullPointerException())

        viewModel.onAttachToViewHolder()
        assert(viewModel.syncData.value == null || viewModel.syncData.value == false)
        assert(componentsItem.noOfPagesLoaded == 1)
        assert(viewModel.hideShimmerLD.value == true)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }

    }

    @Test
    fun `testing error flow of Network Exceptions fetchData`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val application: Application = mockk()
        val viewModel: SectionViewModel =
            spyk(SectionViewModel(application, componentsItem, 99))
        val sectionUseCase: SectionUseCase = mockk()
        viewModel.sectionUseCase = sectionUseCase

        coEvery {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }.throws(UnknownHostException())

        viewModel.onAttachToViewHolder()
        assert(viewModel.syncData.value == null || viewModel.syncData.value == false)
        assert(componentsItem.noOfPagesLoaded == 1)
        assert(componentsItem.verticalProductFailState)
        assert(viewModel.showErrorState.value == true)
        coVerify {
            sectionUseCase.getChildComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        }

    }

}