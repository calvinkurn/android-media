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