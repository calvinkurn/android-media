package com.tokopedia.settingbank.view.viewModel

internal class AddAccountViewModelTest {
   /* @get:Rule
    val rule = InstantTaskExecutorRule()

    private val useCase: GetHowToPayInstructions = mockk()
    private val appLinkPaymentUseCase: AppLinkPaymentUseCase = mockk()

    private lateinit var viewModel: HowToPayViewModel

    @Before
    fun setup() {
        viewModel = HowToPayViewModel(appLinkPaymentUseCase, useCase)
    }


    @Test
    fun `getMutableLiveData success`() {
        val result = mockk<HowToPayInstruction>()
        val appLinkPaymentInfo = mockk<AppLinkPaymentInfo>()
        coEvery { useCase.getHowToPayInstruction(any(), any(), any()) }
                .coAnswers {
                    secondArg<(HowToPayInstruction) -> Unit>().invoke(result)
                }
        every { useCase.getRequestParam(any()) } returns mockk()
        viewModel.getHowToPayInstruction(appLinkPaymentInfo)
        assert(viewModel.howToPayLiveData.value is Success)
    }

    @Test
    fun `getMutableLiveData fail`() {
        val result = mockk<Throwable>()
        val appLinkPaymentInfo = mockk<AppLinkPaymentInfo>()
        coEvery { useCase.getHowToPayInstruction(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(Throwable) -> Unit>().invoke(result)
                }
        every { useCase.getRequestParam(any()) } returns mockk()
        viewModel.getHowToPayInstruction(appLinkPaymentInfo)
        assert(viewModel.howToPayLiveData.value is Fail)
    }

    @Test
    fun `getAppLinkPaymentInfoData success`() {
        val result = mockk<AppLinkPaymentInfo>()
        val bundle = mockk<Bundle>()
        coEvery { appLinkPaymentUseCase.getAppLinkPaymentInfo(any(), any(), any()) }
                .coAnswers {
                    secondArg<(AppLinkPaymentInfo) -> Unit>().invoke(result)
                }
        every { useCase.getRequestParam(any()) } returns mockk()
        viewModel.getAppLinkPaymentInfoData(bundle)
        assert(viewModel.appLinkPaymentLiveData.value is Success)
    }

    @Test
    fun `getAppLinkPaymentInfoData fail`() {
        val result = mockk<Throwable>()
        val bundle = mockk<Bundle>()
        coEvery { appLinkPaymentUseCase.getAppLinkPaymentInfo(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(Throwable) -> Unit>().invoke(result)
                }
        every { useCase.getRequestParam(any()) } returns mockk()
        viewModel.getAppLinkPaymentInfoData(bundle)
        assert(viewModel.appLinkPaymentLiveData.value is Fail)
    }*/
}