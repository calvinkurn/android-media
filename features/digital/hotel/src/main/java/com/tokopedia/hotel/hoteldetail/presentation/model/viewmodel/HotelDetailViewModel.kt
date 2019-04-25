package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.experimental.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailViewModel @Inject constructor(dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher)