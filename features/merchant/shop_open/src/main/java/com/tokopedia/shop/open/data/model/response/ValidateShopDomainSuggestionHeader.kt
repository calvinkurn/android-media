package com.tokopedia.shop.open.data.model.response

data class ValidateShopDomainSuggestionHeader(
        var data:ShopDomainSuggestionHeader = ShopDomainSuggestionHeader(),
        var validateDomainShopName:ValidateShopDomainNameResult = ValidateShopDomainNameResult()
)