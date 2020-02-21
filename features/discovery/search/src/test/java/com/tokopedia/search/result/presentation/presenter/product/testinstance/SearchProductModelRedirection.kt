package com.tokopedia.search.result.presentation.presenter.product.testinstance

import com.google.gson.Gson
import com.tokopedia.search.result.domain.model.SearchProductModel

private val searchProductModelRedirectionJSON = """
{
    "searchProduct" : {
        "query": "produk wardyah",
        "source": "search",
        "shareUrl": "https://www.tokopedia.com/search?device=android&image_size=200&image_square=true&ob=23&q=produk+wardyah&related=true&rows=8&shipping=,,&source=search&st=product&unique_id=a9e9525d4f5ab7285514eaaf6404ec03&user_id=19016871&xdevice=lite-0.0",
        "isFilter": false,
        "response_code": 6,
        "keyword_process": "0",
        "count": 1048,
        "count_text": "1.048",
        "additional_params": "rf=true&nuq=produk wardah",
        "isQuerySafe": true,
        "autocomplete_applink": "tokopedia://search-autocomplete?q=produk%20wardyah",
        "errorMessage": "",
        "lite_url": "",
        "default_view": 1,
        "redirection": {
            "redirect_applink": "tokopedia://discovery/wardah-1"
        }
    }
}
""".trimIndent().replace("\n", "")

internal val searchProductModelRedirection = Gson().fromJson(searchProductModelRedirectionJSON, SearchProductModel::class.java)