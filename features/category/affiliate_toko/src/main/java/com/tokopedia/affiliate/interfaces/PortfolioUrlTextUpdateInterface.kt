package com.tokopedia.affiliate.interfaces

interface PortfolioUrlTextUpdateInterface {
    fun onUrlUpdate(position:Int, text:String)
    fun onError(position: Int)
    fun onUrlSuccess(position: Int)
    fun onNextKeyPressed(position: Int, b: Boolean)
}