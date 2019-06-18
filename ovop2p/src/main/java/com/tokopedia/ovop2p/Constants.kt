package com.tokopedia.ovop2p

class Constants {
    object Keys {
        val TRANSFER_ID = "transfer_id"
        val AMOUNT = "amount"
        val TO_PHN_NO = "to_phone_number"
        val MESSAGE = "message"
        val RESULT_CODE_CONTACTS_SELECTION = 100
        val USER_NAME = "userName"
        val USER_NUMBER = "userNum"
    }
    object AppLinks {
        const val OVOP2PTRANSFER = "tokopedia://ovop2ptransfer"
        const val OVOP2PTHANKYOUPAGE = "tokopedia://ovop2pthankyoupage/{transfer_id}"
    }
}
