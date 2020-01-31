package com.rahullohra.fakeresponse.presentaiton.viewmodels.data

data class AddGqlData(val gqlQueryName: String?, val javaQueryName: String?=null, val customTag: String?=null, val response: String?)

data class AddRestData(val url: String?, val methodName: String, val response: String?)