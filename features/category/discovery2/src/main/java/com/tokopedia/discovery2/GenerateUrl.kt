package com.tokopedia.discovery2

class GenerateUrl {

    companion object {
        const val url = "https://ace.tokopedia.com/hoth/discovery/api/page/"
//        const val url = "https://ace-staging.tokopedia.com/hoth/discovery/api/page/"

        const val componentURL = "https://ace.tokopedia.com/hoth/discovery/api/component/"

        fun getUrl(endPoint: String) = String.format("$url%s", endPoint)

        fun getComponentUrl(componentId: Int) = String.format("$componentURL%s%s", "sample-intermediary/", componentId)
    }


}