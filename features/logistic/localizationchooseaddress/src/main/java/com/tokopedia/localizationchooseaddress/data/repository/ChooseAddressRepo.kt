package com.tokopedia.localizationchooseaddress.data.repository

import com.tokopedia.localizationchooseaddress.domain.response.GetChosenAddressListQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import javax.inject.Inject

interface ChooseAddressRepo {

    suspend fun getChosenAddressList(): GetChosenAddressListQglResponse
}