package com.tokopedia.settingbank.util

import java.lang.Exception

class AddBankAccountException(val errorMessage : String) : Exception(errorMessage)
class LoadBankAccountListException(errorMessage: String) : Exception(errorMessage)
class DeleteBankAccountException(val errorMessage: String) : Exception(errorMessage)