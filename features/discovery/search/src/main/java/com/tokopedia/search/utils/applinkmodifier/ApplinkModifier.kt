package com.tokopedia.search.utils.applinkmodifier

interface ApplinkModifier {

    fun modifyApplink(applink: String, enterMethod: String = ""): String
}
