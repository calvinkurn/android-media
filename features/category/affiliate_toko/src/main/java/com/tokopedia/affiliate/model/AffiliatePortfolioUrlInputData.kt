package com.tokopedia.affiliate.model

class AffiliatePortfolioUrlInputData (
    var id : Int?,
    var title:String?,
    var text:String?,
    var successContent:String?,
    var errorContent:String?,
    var isError:Boolean,
    var isFocus : Boolean ? = false
    )