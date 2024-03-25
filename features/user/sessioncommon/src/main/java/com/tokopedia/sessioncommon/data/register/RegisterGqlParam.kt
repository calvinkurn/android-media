package com.tokopedia.sessioncommon.data.register

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class InputRegisterGqlParam(
    @SerializedName("input")
    private val input: RegisterGqlParam
) : GqlParam

interface RegisterGqlParam : GqlParam

data class RegisterPhoneGqlParam(
    @SerializedName("phone")
    private val phone: String,

    @SerializedName("fullname")
    private val fullname: String,

    @SerializedName("os_type")
    private val osType: String,

    @SerializedName("reg_type")
    private val regType: String
): RegisterGqlParam

data class ShopCreationRegisterGqlParam(
    @SerializedName("phone")
    private val phone: String,

    @SerializedName("fullname")
    private val fullname: String,

    @SerializedName("os_type")
    private val osType: String,

    @SerializedName("reg_type")
    private val regType: String,

    @SerializedName("accounts_type_name")
    private val accountsTypeName: String
) : RegisterGqlParam
