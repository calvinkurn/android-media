package com.tokopedia.globalnavwidgetsamples

val globalNavWidgetItemNoInfo = """
{
    "title": "Beli Tiket",
    "keyword": "bali",
    "nav_template": "1",
    "see_all_applink": "tokopedia://pesawat",
    "see_all_url": "https://tokopedia.com/flight",
    "list": [
        {
            "name": "Pesawat",
            "info": "",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/category/pesawat/colored-active.png",
            "applink": "tokopedia://pesawat",
            "url": "https://tokopedia.com/flight"
        },
        {
            "name": "Tiket Hiburan",
            "info": "",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/category/theme-park/colored-active.png",
            "applink": "tokopedia://webview?url=https://www.tokopedia.com/events/",
            "url": "https://tokopedia.com/events/search?tags=&time=&child_category_ids=3"
        },
        {
            "name": "Kereta Api",
            "info": "",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/category/kai/colored-active.png",
            "applink": "tokopedia://webview?url=https://tiket.tokopedia.com/kereta-api/",
            "url": "https://tiket.tokopedia.com/kereta-api/"
        }
    ]
}
""".trimIndent().replace("\n", "")

val globalNavWidgetItemWithInfo = """
{
    "title": "Beli Pulsa",
    "keyword": "pulsa simpati",
    "nav_template": "1",
    "see_all_applink": "tokopedia://digital/form?category_id=1&operator_id=12",
    "see_all_url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12",
    "list": [
        {
            "name": "500.000",
            "info": "Rp 500.000",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=117",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=117"
        },
        {
            "name": "1.000",
            "info": "Rp 1.800",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=2227",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=2227"
        },
        {
            "name": "20.000",
            "info": "Rp 20.500",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=1126",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=1126"
        },
        {
            "name": "50.000",
            "info": "Rp 50.000",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=69",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=69"
        },
        {
            "name": "15.000",
            "info": "Rp 15.000",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=3443",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=3443"
        }
    ]
}
""".trimIndent().replace("\n", "")