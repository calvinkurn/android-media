package viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class ProductArViewModel @Inject constructor(dispatchers: CoroutineDispatchers)
    : BaseViewModel(dispatchers.io) {



}