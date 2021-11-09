package com.tokopedia.homecredit.viewModel

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.homecredit.domain.HomeCreditUseCase
import javax.inject.Inject

class HomeCreditViewModel @Inject constructor(
    homeCreditUseCase: HomeCreditUseCase
) : ViewModel()
{


}
