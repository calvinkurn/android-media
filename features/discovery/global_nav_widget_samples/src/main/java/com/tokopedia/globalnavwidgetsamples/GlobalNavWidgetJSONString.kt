package com.tokopedia.globalnavwidgetsamples

val globalNavWidgetModelItemNoInfo = """
{
    "source": "",
    "keyword": "bali",
    "title": "Beli Tiket",
    "nav_template": "pill",
    "background": "https://ecs7.tokopedia.net/img/green-container.png",
    "logo_url": "",
    "reputation_url": "",
    "icon_badge_url": "",
    "credibility": "",
    "is_official": false,
    "is_power_merchant": false,
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

val globalNavWidgetModelItemWithInfo = """
{
    "source": "recharge",
    "keyword": "pulsa simpati",
    "title": "Beli Pulsa",
    "nav_template": "pill",
    "background": "https://ecs7.tokopedia.net/img/yellow-container.png",
    "logo_url": "",
    "reputation_url": "",
    "icon_badge_url": "",
    "credibility": "",
    "is_official": false,
    "is_power_merchant": false,
    "see_all_applink": "tokopedia://digital/form?category_id=1&operator_id=12",
    "see_all_url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12",
    "list": [
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
            "name": "500.000",
            "info": "Rp 500.000",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=117",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=117"
        },
        {
            "name": "30.000",
            "info": "Rp 30.000",
            "image_url": "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
            "applink": "tokopedia://digital/form?category_id=1&operator_id=12&product_id=3445",
            "url": "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=3445"
        }
    ]
}
""".trimIndent().replace("\n", "")

val globalNavWidgetModelCard = """
{
    "source": "deal",
    "keyword": "hotel",
    "title": "Trending Deals",
    "nav_template": "card",
    "background": "https://ecs7.tokopedia.net/img/red-container.png",
    "logo_url": "",
    "reputation_url": "",
    "icon_badge_url": "",
    "credibility": "",
    "is_official": false,
    "is_power_merchant": false,
    "see_all_applink": "tokopedia://deals/brand/asana-grove-hotel-yogyakarta-2566",
    "see_all_url": "https://www.tokopedia.com/deals/jakarta/t/asana-grove-hotel-yogyakarta-2566/",
    "list": [
        {
            "name": "Sebelum liburan, book hotelmu disini!",
            "info": "",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-7419",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-7419/",
            "subtitle": "",
            "strikethrough": "",
            "background_url": "",
            "logo_url": ""
        },
        {
            "name": "Inaya Putri Bali",
            "info": "Rp 1.500.000",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-7419",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-7419/",
            "subtitle": "Harga /kamar/malam",
            "strikethrough": "",
            "background_url": "",
            "logo_url": ""
        },
        {
            "name": "Singapore River Safari",
            "info": "Rp 550.000",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-7419",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-7419/",
            "subtitle": "Mulai dari",
            "strikethrough": "Rp 750.000",
            "background_url": "",
            "logo_url": ""
        },
        {
            "name": "DWPX - 10th Anniversary Event with a very looooong name and need to be ellipsize because it exceeds three lines",
            "info": "Rp 509.000",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-7419",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-7419/",
            "subtitle": "Mulai dari",
            "strikethrough": "",
            "background_url": "",
            "logo_url": ""
        },
        {
            "name": "Jakarta (CGK) - Surabaya (SUB)",
            "info": "Rp 859.000",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-17695",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-17695/",
            "subtitle": "Mulai dari",
            "strikethrough": "",
            "background_url": "",
            "logo_url": ""
        },
        {
            "name": "Sample super long price",
            "info": "Rp 859.000.000.000.000.000",
            "image_url": "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
            "applink": "tokopedia://deals/test-giftn-17695",
            "url": "https://www.tokopedia.com/deals/i/test-giftn-17695/",
            "subtitle": "Mulai dari",
            "strikethrough": "Rp 858.999.999.999.999.999",
            "background_url": "",
            "logo_url": ""
        }
    ]
}
""".trimIndent().replace("\n", "")