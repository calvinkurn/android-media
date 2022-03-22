package com.tokopedia.affiliate.model.pojo

class AffiliatePortfolioUrlInputData (
    var id : Int?,
    var serviceFormat : String? = "",
    var title:String?,
    var text:String?,
    var successContent:String?,
    var errorContent:String?,
    var isError:Boolean,
    var isFocus : Boolean ? = false,
    var regex : String? = null
    )