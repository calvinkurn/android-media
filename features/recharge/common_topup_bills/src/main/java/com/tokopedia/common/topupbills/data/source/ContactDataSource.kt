package com.tokopedia.common.topupbills.data.source

import com.tokopedia.common.topupbills.data.TopupBillsContact

interface ContactDataSource {
    fun getContactList(): MutableList<TopupBillsContact>
}