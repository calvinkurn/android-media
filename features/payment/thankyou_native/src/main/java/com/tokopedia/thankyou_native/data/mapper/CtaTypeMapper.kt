package com.tokopedia.thankyou_native.data.mapper


sealed class CtaType

object Redirect : CtaType()
object ActionCekStatusBayar: CtaType()

object CtaTypeMapper {
    fun getType(type: String): CtaType {
        return when(type) {
            "Redirect" -> Redirect
            "Action-CekStatusBayar" -> ActionCekStatusBayar
            else -> Redirect
        }
    }
}
