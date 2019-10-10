package com.tokopedia.wallet.ovoactivation.view

//@RunWith(MockitoJUnitRunner::class)
class IntroOvoPresenterTest {

//    @Mock
//    internal var view: IntroOvoContract.View? = null
//    @Mock
//    internal var checkNumberOvoUseCase: CheckNumberOvoUseCase? = null
//
//    private var presenter: IntroOvoPresenter? = null
//    private var checkRegisteredPhoneOvoModel: CheckPhoneOvoModel? = null
//    private var checkNotRegisteredPhoneOvoModel: CheckPhoneOvoModel? = null
//
//    @Before
//    @Throws(Exception::class)
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//        presenter = IntroOvoPresenter(checkNumberOvoUseCase, WalletTestScheduler())
//        presenter!!.attachView(view)
//    }
//
//    @Test
//    fun checkPhoneNumber_UserRegistered_Success() {
//        //given
//        setCheckRegisteredNumberModel()
//        Mockito.`when`(checkNumberOvoUseCase!!.createObservable(Mockito.anyObject()))
//                .thenReturn(Observable.just(checkRegisteredPhoneOvoModel))
//        //when
//        presenter!!.checkPhoneNumber()
//
//        //then
//        Mockito.verify(view).directPageWithApplink(checkRegisteredPhoneOvoModel!!.registeredApplink)
//
//    }
//
//    @Test
//    fun checkPhoneNumber_UserUnregistered_Success() {
//        //given
//        setCheckNotRegisteredNumberModel()
//        Mockito.`when`(checkNumberOvoUseCase!!.createObservable(Mockito.anyObject()))
//                .thenReturn(Observable.just(checkNotRegisteredPhoneOvoModel))
//        //when
//        presenter!!.checkPhoneNumber()
//
//        //then
//        Mockito.verify(view).directPageWithApplink(checkNotRegisteredPhoneOvoModel!!.notRegisteredApplink)
//
//    }
//
//    @Test
//    fun checkPhoneNumber_UserNotYetHavePhoneNumber_Success() {
//        //        //given
//        //        setCheckNumberModel();
//        //        Mockito.when(checkNumberOvoUseCase.createObservable(Mockito.anyObject()))
//        //                .thenReturn(Observable.just(setCheckNumberModel()));
//        //        //when
//        //        presenter.checkPhoneNumber();
//        //
//        //        //then
//        //        Mockito.verify(view).directPageToOtpPage();
//
//    }
//
//    @Test
//    fun checkPhoneNumber_UserRegistered_Failed() {
//        //given
//        val message = "Error"
//        Mockito.`when`(checkNumberOvoUseCase!!.createObservable(Mockito.anyObject()))
//                .thenReturn(Observable.error(MessageErrorException(message)))
//        Mockito.`when`(view!!.getErrorMessage(Throwable())).thenReturn(message)
//        //when
//        presenter!!.checkPhoneNumber()
//
//        //then
//        Mockito.verify(view).showSnackbarErrorMessage(Mockito.anyObject())
//    }
//
//
//    private fun setCheckRegisteredNumberModel() {
//        checkRegisteredPhoneOvoModel = CheckPhoneOvoModel()
//        checkRegisteredPhoneOvoModel!!.phoneNumber = "628572257969"
//        checkRegisteredPhoneOvoModel!!.isRegistered = true
//        checkRegisteredPhoneOvoModel!!.registeredApplink = "tokopedia://webview?url=https://www.tokopedia.com/api/v1/activate"
//        checkRegisteredPhoneOvoModel!!.notRegisteredApplink = "tokopedia://ovo/activation"
//        checkRegisteredPhoneOvoModel!!.changeMsisdnApplink = "https://m.tokopedia.com/user/profile/edit"
//    }
//
//    private fun setCheckNotRegisteredNumberModel() {
//        checkNotRegisteredPhoneOvoModel = CheckPhoneOvoModel()
//        checkNotRegisteredPhoneOvoModel!!.phoneNumber = "628572257969"
//        checkNotRegisteredPhoneOvoModel!!.isRegistered = false
//        checkNotRegisteredPhoneOvoModel!!.registeredApplink = "tokopedia://webview?url=https://www.tokopedia.com/api/v1/activate"
//        checkNotRegisteredPhoneOvoModel!!.notRegisteredApplink = "tokopedia://ovo/activation"
//        checkNotRegisteredPhoneOvoModel!!.changeMsisdnApplink = "https://m.tokopedia.com/user/profile/edit"
//    }
}
