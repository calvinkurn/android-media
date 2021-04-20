package com.tokopedia.utils.phonenumber

/**
 * @author  by stevenfredian on 10/27/16.
 */
object PhoneNumberUtil {
    @JvmStatic
    fun transform(phoneRawString: String): String {
        var phoneRaw = replace62with0(phoneRawString)
        phoneRaw = phoneRaw.replace("-", "")
        val phoneNumArr = StringBuilder()
        var index = 0
        val limit = 4
        val size = phoneRaw.length
        while (index < phoneRaw.length) {
            if (size > limit + index) {
                phoneNumArr.append(phoneRaw.substring(index, index + limit))
                phoneNumArr.append("-")
            } else {
                phoneNumArr.append(phoneRaw.substring(index, size))
            }
            index += limit
        }
        return phoneNumArr.toString()
    }
    @JvmStatic
    fun replace62with0(phoneRawString: String): String {
        var phoneRaw = phoneRawString
        if (phoneRaw.startsWith("62")) {
            phoneRaw = phoneRaw.replaceFirst("62".toRegex(), "0")
        } else if (phoneRaw.startsWith("+62")) {
            phoneRaw = phoneRaw.replaceFirst("\\+62".toRegex(), "0")
        }
        return phoneRaw
    }
}