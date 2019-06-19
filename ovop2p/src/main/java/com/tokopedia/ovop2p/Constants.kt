package com.tokopedia.ovop2p

class Constants {
    object Keys {
        val TRANSFER_ID = "transfer_id"
        val AMOUNT = "amount"
        val TO_PHN_NO = "to_phone_number"
        val MESSAGE = "message"
        val NAME = "name"
        val FORMATTED_AMOUNT = "formatted_amt"
        val RESULT_CODE_CONTACTS_SELECTION = 100
        val USER_NAME = "userName"
        val USER_NUMBER = "userNum"
        val NON_OVO_SUCS = "Non ovo sucs"
    }
    object AppLinks {
        const val OVOP2PTRANSFER = "tokopedia://ovop2ptransfer"
        const val OVOP2PTHANKYOUPAGE = "tokopedia://ovop2pthankyoupage/{transfer_id}"
    }

    object Headers{
        val TRANSFER_FORM_HEADER = "Ke Sesama OVO"
        val TRANSFER_SUCCESS = "Transfer Berhasil"
        val LOOK_FOR_NAME_PHONE = "Cari nama atau nomor ponsel"
    }

    object ScreenName{
        val ERROR_FRAGMENT = "Error Fragment"
        val TXN_DTL_FRAGMENT = "Transaction Detail Fragment"
        val FORM_FRAGMENT = "Form Fragment"
        val FRAGMENT_THANKYOU_OVO_USER = "Ovo User Thankyou Fragment"
        val FRAGMENT_THANKYOU_NON_OVO_USER = "Non Ovo User Thankyou Fragment"
    }

    object PlaceHolders{
        val PHONE_NO_PLCHLDR = "{{num_plchldr}}"
        val TRNSFER_ID_PLCHLDR = "{transfer_id}"
    }

    object Messages{
        val NONOVO_USR_SUCS_MSG = "Undangan berhasil dikirim ke {{num_plchldr}}.\n" +
                "Terima kasih telah menggunakan OVO."
    }
}
