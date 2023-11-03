package com.tokopedia.universal_sharing.test

import com.tokopedia.universal_sharing.domain.usecase.ImageGeneratorUseCase
import com.tokopedia.universal_sharing.domain.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.view.sharewidget.UniversalShareWidgetViewModel
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class UniversalShareWidgetViewModelTest {
    @RelaxedMockK
    lateinit var imagePolicyUseCase: ImagePolicyUseCase

    @RelaxedMockK
    lateinit var imageGeneratorUseCase: ImageGeneratorUseCase

    @RelaxedMockK
    lateinit var affiliateEligibilityUseCase: AffiliateEligibilityCheckUseCase

    @RelaxedMockK
    lateinit var

    lateinit var viewModel: UniversalShareWidgetViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = UniversalShareWidgetViewModel(
            affiliateEligibilityUseCase = affiliateEligibilityUseCase,
            imagePolicyUseCase = imagePolicyUseCase,
            imageGeneratorUseCase = imageGeneratorUseCase
        )
    }

    @Test
    fun setInitialDataTest() {

    }

}
