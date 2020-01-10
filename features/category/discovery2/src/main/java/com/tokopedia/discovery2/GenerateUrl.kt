package com.tokopedia.discovery2

class GenerateUrl {

    companion object {
//                const val url = "https://ace.tokopedia.com/hoth/discovery/api/page/"
        const val url = "https://ace-staging.tokopedia.com/hoth/discovery/api/page/"

        fun getUrl(endPoint: String) = String.format("$url%s", endPoint)
    }


}