package com.tokopedia.shop_nib.presentation.submission

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import java.util.*

import javax.inject.Inject

class NibSubmissionViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var nibPublishDate = Date()

    fun setNibPublishDate(nibPublishDate: Date) {
        this.nibPublishDate = nibPublishDate
    }

    fun getNibPublishDate(): Date {
        return nibPublishDate
    }
}
