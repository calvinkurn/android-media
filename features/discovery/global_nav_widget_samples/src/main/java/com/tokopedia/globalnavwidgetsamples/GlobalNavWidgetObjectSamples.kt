package com.tokopedia.globalnavwidgetsamples

import com.tokopedia.globalnavwidget.GlobalNavWidgetModel

internal val globalNavWidgetPillModelItemNoInfo = GlobalNavWidgetModel(
        keyword = "bali",
        title = "Beli Tiket",
        navTemplate = "pill",
        background = "https://ecs7.tokopedia.net/img/green-container.png",
        clickSeeAllApplink = "tokopedia://pesawat",
        clickSeeAllUrl = "https://tokopedia.com/flight",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    name = "pesawat",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/category/pesawat/colored-active.png",
                    clickItemApplink = "tokopedia://pesawat",
                    clickItemUrl = "https://tokopedia.com/flight",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "Tiket Hiburan",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/category/theme-park/colored-active.png",
                    clickItemApplink = "tokopedia://webview?url=https://www.tokopedia.com/events/",
                    clickItemUrl = "https://tokopedia.com/events/search?tags=&time=&child_category_ids=3",
                    position = 2
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "Kereta Api",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/category/kai/colored-active.png",
                    clickItemApplink = "tokopedia://webview?url=https://tiket.tokopedia.com/kereta-api/",
                    clickItemUrl = "https://tiket.tokopedia.com/kereta-api/",
                    position = 3
            ))
        }
)

internal val globalNavWidgetPillModelItemWithInfo = GlobalNavWidgetModel(
        source = "recharge",
        keyword = "pulsa simpati",
        title = "Beli Pulsa",
        navTemplate = "default template pill",
        background = "https://ecs7.tokopedia.net/img/yellow-container.png",
        clickSeeAllApplink = "tokopedia://digital/form?category_id=1&operator_id=12",
        clickSeeAllUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    name = "1.000",
                    info = "Rp 1.800",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=2227",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=2227",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "20.000",
                    info = "Rp 20.500",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=1126",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=1126",
                    position = 2
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "50.000",
                    info = "Rp 50.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=69",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=69",
                    position = 3
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "500.000",
                    info = "Rp 500.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=117",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=117",
                    position = 4
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "30.000",
                    info = "Rp 30.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=3445",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=3445",
                    position = 5
            ))
        }
)

internal val globalNavWidgetPillModelWithoutSeeAllApplink = GlobalNavWidgetModel(
        source = "recharge",
        keyword = "pulsa simpati",
        title = "Beli Pulsa",
        navTemplate = "default template pill",
        background = "https://ecs7.tokopedia.net/img/yellow-container.png",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    name = "1.000",
                    info = "Rp 1.800",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=2227",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=2227",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "20.000",
                    info = "Rp 20.500",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=1126",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=1126",
                    position = 2
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "50.000",
                    info = "Rp 50.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=69",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=69",
                    position = 3
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "500.000",
                    info = "Rp 500.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=117",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=117",
                    position = 4
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "30.000",
                    info = "Rp 30.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=3445",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=3445",
                    position = 5
            ))
        }
)

internal val globalNavWidgetWithoutBackgroundInModel = GlobalNavWidgetModel(
        source = "recharge",
        keyword = "pulsa simpati",
        title = "Beli Pulsa",
        navTemplate = "default template pill",
        background = "",
        clickSeeAllApplink = "tokopedia://digital/form?category_id=1&operator_id=12",
        clickSeeAllUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    name = "1.000",
                    info = "Rp 1.800",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=2227",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=2227",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "20.000",
                    info = "Rp 20.500",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=1126",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=1126",
                    position = 2
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "50.000",
                    info = "Rp 50.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=69",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=69",
                    position = 3
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "500.000",
                    info = "Rp 500.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=117",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=117",
                    position = 4
            ))
            it.add(GlobalNavWidgetModel.Item(
                    name = "30.000",
                    info = "Rp 30.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/recharge/operator/simpati_2.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=1&operator_id=12&product_id=3445",
                    clickItemUrl = "https://pulsa.tokopedia.com/pulsa/?operator_id=12&product_id=3445",
                    position = 5
            ))
        }
)

internal val globalNavWidgetModelCard = GlobalNavWidgetModel(
        source = "deal",
        keyword = "hotel",
        title = "Trending Deals",
        navTemplate = "card",
        background = "https://ecs7.tokopedia.net/img/red-container.png",
        clickSeeAllApplink = "tokopedia://deals/brand/asana-grove-hotel-yogyakarta-2566",
        clickSeeAllUrl = "https://www.tokopedia.com/deals/jakarta/t/asana-grove-hotel-yogyakarta-2566/",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Hotel",
                    name = "Sebelum liburan, book hotelmu disini!",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Deals",
                    name = "Inaya Putri Bali",
                    info = "Rp 1.500.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    subtitle = "Harga /kamar/malam",
                    position = 2
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Deals",
                    name = "Singapore River Safari",
                    info = "Rp 550.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    subtitle = "Mulai dari",
                    strikethrough = "Rp 750.000",
                    position = 3
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Tiket Event",
                    name = "DWPX - 10th Anniversary Event with a very looooong name and need to be ellipsize because it exceeds three lines",
                    info = "Rp 509.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    subtitle = "Mulai dari",
                    position = 4
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Deals",
                    name = "Jakarta (CGK) - Surabaya (SUB)",
                    info = "Rp 859.000",
                    clickItemApplink = "tokopedia://deals/test-giftn-17695",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-17695/",
                    subtitle = "Mulai dari",
                    position = 5
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Deals",
                    name = "Sample super long price",
                    info = "Rp 859.000.000.000.000.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-17695",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-17695/",
                    subtitle = "Mulai dari",
                    strikethrough = "Rp 858.999.999.999.999.999",
                    position = 6
            ))
        }
)

internal val globalNavWidgetModelCard2 = GlobalNavWidgetModel(
        source = "deal",
        keyword = "hotel",
        title = "Trending Deals",
        navTemplate = "card",
        background = "https://ecs7.tokopedia.net/img/red-container.png",
        clickSeeAllApplink = "tokopedia://deals/brand/asana-grove-hotel-yogyakarta-2566",
        clickSeeAllUrl = "https://www.tokopedia.com/deals/jakarta/t/asana-grove-hotel-yogyakarta-2566/",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Hotel",
                    name = "Sebelum liburan, book hotelmu disini!",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    backgroundUrl = "https://ecs7.tokopedia.net/img/autocomplete/image_background_default_widget.png",
                    logoUrl = "https://ecs7.tokopedia.net/img/recharge/category/pesawat/colored-active.png",
                    position = 1
            ))
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Deals",
                    name = "Inaya Putri Bali",
                    info = "Rp 1.500.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/30/5510064/5510064_44d8a048-f210-429d-b2e1-325dcbc6a5a5.png",
                    clickItemApplink = "tokopedia://deals/test-giftn-7419",
                    clickItemUrl = "https://www.tokopedia.com/deals/i/test-giftn-7419/",
                    subtitle = "Harga /kamar/malam",
                    backgroundUrl = "https://ecs7.tokopedia.net/img/red-container.png",
                    logoUrl = "https://ecs7.tokopedia.net/img/recharge/category/pesawat/colored-active.png",
                    position = 2
            ))
        }
)

internal val singleGlobalNavWidgetModelPill = GlobalNavWidgetModel(
        source = "other",
        keyword = "qurban",
        title = "Qurban untuk Indonesia",
        navTemplate = "pill",
        background = "",
        clickSeeAllApplink = "",
        clickSeeAllUrl = "",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Qurban",
                    name = "Qurban",
                    info = "",
                    imageUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/13/51829405/51829405_214934b2-3751-49fd-8288-7e0dd49625ff.png",
                    clickItemApplink = "tokopedia://discovery/qurban",
                    clickItemUrl = "https://www.tokopedia.com/s/qurban/",
                    subtitle = "",
                    backgroundUrl = "https://ecs7.tokopedia.net/img/autocomplete/image_background_default_widget.png",
                    logoUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/13/51829405/51829405_214934b2-3751-49fd-8288-7e0dd49625ff.png",
                    position = 1
            ))
        }
)

internal val singleGlobalNavWidgetModelCardIconWithNoSubtitleInfo = GlobalNavWidgetModel(
        source = "hotel",
        keyword = "hotel jakarta",
        title = "Booking Hotel",
        navTemplate = "card",
        background = "",
        clickSeeAllApplink = "tokopedia://hotel/dashboard",
        clickSeeAllUrl = "https://www.tokopedia.com/hotel/",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Hotel",
                    name = "Booking hotel di Tokopedia!",
                    info = "",
                    imageUrl = "",
                    clickItemApplink = "tokopedia://hotel/dashboard",
                    clickItemUrl = "https://www.tokopedia.com/hotel/",
                    subtitle = "",
                    backgroundUrl = "https://ecs7.tokopedia.net/img/autocomplete/image_background_default_widget.png",
                    logoUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/28/21181130/21181130_14539e75-40c6-49f4-8db6-12c5a001a511.png",
                    position = 1
            ))
        }
)

internal val singleGlobalNavWidgetModelCardIcon = GlobalNavWidgetModel(
        source = "transport",
        keyword = "tiket pesawat bandung",
        title = "Tiket Pesawat",
        navTemplate = "card",
        background = "https://ecs7.tokopedia.net/img/yellow-container.png",
        clickSeeAllApplink = "",
        clickSeeAllUrl = "",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Tiket Pesawat",
                    name = "Jakarta (CGK) - Bandung (BDO)",
                    info = "Rp 1.065.800",
                    imageUrl = "",
                    clickItemApplink = "tokopedia://pesawat/search?dest=CGK_Jakarta_BDO_Bandung_2020-10-15&a=1&c=0&i=0&s=1&auto_search=1",
                    clickItemUrl = "https://www.tokopedia.com/flight/search/?term=CGK-BDO-20201015&a=1&c=0&i=0&k=1",
                    subtitle = "Mulai dari",
                    backgroundUrl = "https://ecs7.tokopedia.net/img/autocomplete/image_background_default_widget.png",
                    logoUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/4/1/3127195/3127195_2258af2c-550d-4b7f-a8d4-f62178070ed8.png",
                    position = 1
            ))
        }
)

internal val singleGlobalNavWidgetModelCardImage = GlobalNavWidgetModel(
        source = "recharge",
        keyword = "ps 3",
        title = "Beli Voucher Game",
        navTemplate = "card",
        background = "https://ecs7.tokopedia.net/img/yellow-container.png",
        clickSeeAllApplink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fvoucher-game%2Fhome%3Fispulsa%3D1%26action%3Dedit_data%26operator_id%3D102",
        clickSeeAllUrl = "https://www.tokopedia.com/voucher-game/?action=edit_data&operator_id=102",
        itemList = mutableListOf<GlobalNavWidgetModel.Item>().also {
            it.add(GlobalNavWidgetModel.Item(
                    categoryName = "Voucher Game",
                    name = "3 Months ",
                    info = "Rp 265.000",
                    imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/10/7/66301108/66301108_c12ded0f-e784-4db4-99f4-45e3ac0d70ba.png",
                    clickItemApplink = "tokopedia://digital/form?category_id=6&menu_id=4&operator_id=102&product_id=514&template=voucher",
                    clickItemUrl = "https://www.tokopedia.com/voucher-game/?action=edit_data&operator_id=102&product_id=514",
                    subtitle = "Mulai dari",
                    backgroundUrl = "",
                    logoUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/10/22/21181130/21181130_fa52cbc3-016e-4b04-ac8b-dbef10c2b923.png",
                    position = 1
            ))
        }
)