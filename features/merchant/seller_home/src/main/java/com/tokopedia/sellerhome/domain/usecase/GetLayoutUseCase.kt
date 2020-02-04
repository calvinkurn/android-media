package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created By @ilhamsuaib on 2020-01-15
 */

class GetLayoutUseCase(
        graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<List<BaseWidgetUiModel<*>>>() {

    override suspend fun executeOnBackground(): List<BaseWidgetUiModel<*>> {

        //handle request here
        return listOf(
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Penting hari ini",
                        subtitle = "",
                        tooltip = TooltipUiModel("", "", emptyList(), false),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "New Order",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "tokopedia://pesanan-baru/detail",
                        dataKey = "newOrder",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Ready To Ship",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "readyToShipOrder",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Complaint",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "complaint",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Unread Chat",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "unreadChat",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Discussion",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "discussion",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Product View",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "productViewStatistic",
                        ctaText = "",
                        data = null
                ),
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Ringkasan penjualan",
                        subtitle = "21 JAN 20 - 27 JAN 20",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Shop Total Revenue",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "shopTotalRevenueStatistic",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "Product Sold",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "productSoldStatistic",
                        ctaText = "",
                        data = null
                ),
                LineGraphWidgetUiModel(
                        widgetType = WidgetType.LINE_GRAPH,
                        title = "Total Pendapatan",
                        subtitle = "",
                        tooltip = TooltipUiModel("Total Pendapatan", "Pendapatan harian bersih per hari", emptyList(), false),
                        url = "",
                        appLink = "tokopedia://pesanan-baru/detail",
                        dataKey = "grossIncome",
                        ctaText = "",
                        data = null
                ),
                ProgressWidgetUiModel(
                        widgetType = WidgetType.PROGRESS,
                        title = "Power Merchant (Aktif)",
                        subtitle = "",
                        tooltip = TooltipUiModel(
                                title = "Ringkasan Penjualan",
                                content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                                list = listOf(
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Produk dilihat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Produk terjual", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Total pendapatan", "Jumlah pemasukan dari produk terjual beserta ongkos kirim.")
                                ),
                                isShow = true
                        ),
                        url = "",
                        appLink = "",
                        dataKey = "shopScore",
                        ctaText = "",
                        data = null
                ),
                CarouselWidgetUiModel(
                        widgetType = WidgetType.CAROUSEL,
                        title = "Carousel",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = CarouselDataUiModel(
                                data = listOf(
                                        CarouselDataModel(
                                                id = "asasd",
                                                url = "http://tokopedia.com/blablablaal",
                                                applink = "https://seller.tokopedia.com/edu/cara-melihat-promosi-affiliate/",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg"
                                        ),
                                        CarouselDataModel(
                                                id = "",
                                                url = "http://placekitten.com/300/101",
                                                applink = "",
                                                featuredMediaURL = "http://placekitten.com/300/102"
                                        ),
                                        CarouselDataModel(
                                                id = "",
                                                url = "http://placekitten.com/300/102",
                                                applink = "",
                                                featuredMediaURL = "http://placekitten.com/300/103"
                                        )
                                ),
                                state = CarouselState.NORMAL
                        )
                ),
                ListWidgetUiModel(
                        widgetType = WidgetType.LIST,
                        title = "Info Seller",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = ListDataUiModel(
                                dataKey = "articles",
                                items = listOf(
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 19"
                                        ),
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 20"
                                        ),
                                        ListItemUiModel(
                                                title = "Lihat Siapa Aja yang Udah Promosiin Produkmu Lewat Affiliate Marketing!",
                                                appLink = "",
                                                url = "",
                                                featuredMediaURL = "https://i1.wp.com/ecs7.tokopedia.net/img/blog/seller/2019/12/Seller-Center-6.jpg?fit=1024%2C439&ssl=1",
                                                subtitle = "FITUR <i> 12 SEP 21"
                                        )
                                )
                        )
                ),
                DescriptionWidgetUiModel(
                        widgetType = WidgetType.DESCRIPTION,
                        title = "Yuk, Daftar Power Merchant",
                        subtitle = "Pertahankan poin minimum 75 untuk tetap menjadi Power Merchant",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "Selengkapnya",
                        data = null
                )
        )
    }
}