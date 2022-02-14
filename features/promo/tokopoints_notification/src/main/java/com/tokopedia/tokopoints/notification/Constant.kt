package com.tokopedia.tokopoints.notification

interface Constant {
    companion object {
        const val KEY_TYPE = "type"
        const val API_VERSION = "apiVersion"
        const val API_VERSION_VALUE = "2.0.0"
    }
}

const val POPUP_QUERY = """
    query TokopointsNotification(${'$'}type: String , ${'$'}apiVersion : String) {
  tokopoints {
    popupNotif(type: ${'$'}type , apiVersion : ${'$'}apiVersion) {
      titleHeader
      title
      text
      imageURL
      buttonText
      buttonURL
      appLink
    }
  }
}

"""