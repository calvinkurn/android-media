package com.tokopedia.abstraction.common.utils.view

/**
 * Created by Ade Fulki on 2019-07-08.
 * ade.hadian@tokopedia.com
 */

object PhoneNumberUtils {

  fun transform(phone: String): String {
      var phoneRawString = phone
      phoneRawString = checkStart(phoneRawString)
      phoneRawString = phoneRawString.replace("-", "")
      val phoneNumArr = StringBuilder()
      var index = 0
      val limit = 4
      val size = phoneRawString.length
      while (index < phoneRawString.length) {
          if (size > limit + index) {
              phoneNumArr.append(phoneRawString.substring(index, index + limit))
              phoneNumArr.append("-")
          } else {
              phoneNumArr.append(phoneRawString.substring(index, size))
          }
          index += limit
      }
      return phoneNumArr.toString()
  }

  private fun checkStart(phone: String): String {
      var phoneRawString = phone
      if (phoneRawString.startsWith("62")) {
          phoneRawString = phoneRawString.replaceFirst("62".toRegex(), "0")
      } else if (phoneRawString.startsWith("+62")) {
          phoneRawString = phoneRawString.replaceFirst("\\+62".toRegex(), "0")
      }
      return phoneRawString
  }
}