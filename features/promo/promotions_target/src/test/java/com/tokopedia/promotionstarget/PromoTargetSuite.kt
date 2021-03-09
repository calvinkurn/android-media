package com.tokopedia.promotionstarget

import com.tokopedia.promotionstarget.cm.dialog.GratificationDialogHandlerTest
//import com.tokopedia.promotionstarget.presenter.GratificationPresenterSpekTest
import com.tokopedia.promotionstarget.usecase.AutoApplyUseCaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(AutoApplyUseCaseTest::class,
        GratificationDialogHandlerTest::class,
//        GratificationPresenterSpekTest::class,
        TargetPromotionsDialogVMTest::class)
class PromoTargetSuite {
}