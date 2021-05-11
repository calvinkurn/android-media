package com.tokopedia.play.broadcaster.viewmodel

/**
 * Created by jegul on 25/09/20
class PlaySearchSuggestionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private val playBroadcastMapper = PlayBroadcastUiMapper()

    private val getProductInEtalaseUseCase: GetProductsInEtalaseUseCase = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()
    private val mockProductsInEtalase by lazy { modelBuilder.buildProductsInEtalase() }

    private lateinit var viewModel: PlaySearchSuggestionsViewModel

    @Before
    fun setUp() {
        viewModel = PlaySearchSuggestionsViewModel(
                dispatcherProvider,
                getProductInEtalaseUseCase,
                mockk(relaxed = true),
                playBroadcastMapper
        )
    }

    @Test
    fun `when load suggestion is success, then it should return success`() = runBlockingTest(testDispatcher) {
        coEvery { getProductInEtalaseUseCase.executeOnBackground() } returns mockProductsInEtalase

        val keyword = "123"
        viewModel.loadSuggestionsFromKeyword(keyword)

        advanceUntilIdle()

        val result = viewModel.observableSuggestionList.getOrAwaitValue()

        val typeComparator = TypeComparators()
        typeComparator.put(CharSequence::class.java) { _, _ -> 0 }

        Assertions
                .assertThat(result)
                .usingComparatorForType(
                        RecursiveFieldByFieldComparator(emptyMap(), typeComparator) as Comparator<SearchSuggestionUiModel>,
                        SearchSuggestionUiModel::class.java
                )
                .isEqualToComparingFieldByFieldRecursively(
                        NetworkResult.Success(
                                playBroadcastMapper.mapSearchSuggestionList(keyword, mockProductsInEtalase)
                        )
                )
    }

    @Test
    fun `when load suggestion is failed, then it should return failed`() = runBlockingTest(testDispatcher) {
        val error = IllegalStateException()

        coEvery { getProductInEtalaseUseCase.executeOnBackground() } throws error

        val keyword = "123"
        viewModel.loadSuggestionsFromKeyword(keyword)

        advanceUntilIdle()

        val result = viewModel.observableSuggestionList.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

}
 */