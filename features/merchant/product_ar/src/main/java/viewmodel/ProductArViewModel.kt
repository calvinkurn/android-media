package viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import javax.inject.Inject

class ProductArViewModel @Inject constructor(dispatchers: CoroutineDispatchers,
                                             private val chosenAddressRequestHelper: ChosenAddressRequestHelper)
    : BaseViewModel(dispatchers.io) {


}