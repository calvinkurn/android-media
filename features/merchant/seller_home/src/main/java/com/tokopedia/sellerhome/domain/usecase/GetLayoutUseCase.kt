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
                DescriptionWidgetUiModel(
                        widgetType = WidgetType.DESCRIPTION,
                        title = "Tampah produk pertamamu!",
                        subtitle = "Maksimalkan penjualanmu dengan upgrade jadi Power Merchant! Tingkatkan penjualan dengan mengakses fitur dan promo khusus untuk Power Merchant",
                        tooltip = null,
                        url = "",
                        appLink = "tokopedia://pesanan-baru/detail",
                        dataKey = "",
                        ctaText = "Tambah produk sekarang!",
                        data = null
                ),
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Penting hari ini",
                        subtitle = "",
                        tooltip = TooltipUiModel(
                                title = "Ringkasan Penjualan",
                                content = "",
                                list = listOf(
                                        TooltipListItemUiModel("New Order", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Ready to ship", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Complaint", "Jumlah pemasukan dari produk terjual beserta ongkos kirim."),
                                        TooltipListItemUiModel("Unread Chat", "Jumlah calon pembeli yang masuk ke halaman produkmu."),
                                        TooltipListItemUiModel("Discussion", "Jumlah produk terjual dari setiap transaksi sukses"),
                                        TooltipListItemUiModel("Product View", "Jumlah pemasukan dari produk terjual beserta ongkos kirim.")
                                ),
                                isShow = true
                        ),
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = null
                ),
                CardWidgetUiModel(
                        widgetType = WidgetType.CARD,
                        title = "New order",
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
                        title = "Ready to ship",
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
                SectionWidgetUiModel(
                        widgetType = WidgetType.SECTION,
                        title = "Performa toko",
                        subtitle = "21 JAN 20 - 27 JAN 20",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "",
                        ctaText = "",
                        data = null
                ),
                ProgressWidgetUiModel(
                        widgetType = WidgetType.PROGRESS,
                        title = "Power Merchant (Aktif)",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "testing",
                        dataKey = "shopScore",
                        ctaText = "Selengkapnya",
                        data = null
                ),
                CarouselWidgetUiModel(
                        widgetType = WidgetType.CAROUSEL,
                        title = "Khusus untukmu",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "testing",
                        dataKey = "",
                        ctaText = "Lihat Semua",
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
                PostListWidgetUiModel(
                        widgetType = WidgetType.POST,
                        title = "Info Seller",
                        subtitle = "",
                        tooltip = TooltipUiModel("Info Seller", "Skor ada little ipsum del amet, yang membawa per minggu", emptyList(), true),
                        url = "",
                        appLink = "tokopedia://pesanan-baru/detail",
                        dataKey = "article",
                        ctaText = "",
                        data = null
                ),
                PostListWidgetUiModel(
                        widgetType = WidgetType.POST,
                        title = "Produk Terlaris",
                        subtitle = "",
                        tooltip = null,
                        url = "",
                        appLink = "",
                        dataKey = "product",
                        ctaText = "",
                        data = null
                )
        )
    }
}