package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogFullSpecificationDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogTopSpecificationDataModel
import com.tokopedia.catalog.model.raw.*
import com.tokopedia.common_category.model.productModel.*
import java.util.*

object CatalogDetailMapper {

    fun mapIntoVisitable(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        // Adding CatalogInfoDataModel
        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = "Catalog Info",type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, tag = tag,
                    priceRange = "${marketPrice[0].minFmt} - ${marketPrice[0].maxFmt}" ,
                    description = description, images = catalogGetDetailModular.basicInfo.catalogImage))
        }

        catalogGetDetailModular.components.forEachIndexed { index, component ->
            when(component.type){
                CatalogConstant.TOP_SPECIFICATIONS -> {
                    val crudeTopSpecificationsData = component.data
                    val topSpecsArray = arrayListOf<TopSpecificationsComponentData>()
                    crudeTopSpecificationsData.forEachIndexed { indexComponentData, componentData ->
                        topSpecsArray.add(TopSpecificationsComponentData(componentData.key,
                                componentData.value, componentData.icon))
                    }
                    listOfComponents.add(CatalogTopSpecificationDataModel(name = component.name, type = component.type , topSpecificationsList = topSpecsArray))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {

                }

                CatalogConstant.CATALOG_PRODUCT_FILTER -> {

                }
            }
        }

        return listOfComponents
    }

    fun getFullSpecificationsModel(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular) : CatalogFullSpecificationDataModel{
        var catalogFullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())
        catalogGetDetailModular.components.forEachIndexed { index, component ->
            when (component.type) {
                CatalogConstant.FULL_CATALOG_SPECIFICATION -> {
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<FullSpecificationsComponentData>()
                    crudeSpecificationsData.forEachIndexed { indexComponentData, componentData ->
                        specifications.add(FullSpecificationsComponentData(componentData.name,
                                componentData.icon, componentData.specificationsRow ?: arrayListOf()))
                    }
                    catalogFullSpecificationDataModel =  CatalogFullSpecificationDataModel(fullSpecificationsList = specifications)
                }
            }
        }
        return catalogFullSpecificationDataModel
    }

    fun getDummyCatalogData() : CatalogResponseData{

        val longDesc = "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro. \n Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro."
        val catalogImageList = arrayListOf<CatalogImage>()
        catalogImageList.add(CatalogImage(
                "https://media.wired.com/photos/5fa5e735ba670daaf8e97a91/1:1/w_1502,h_1502,c_limit/GEAR-MAX-Apple-iPhone-12-Pro-Max-SOURCE-Apple.jpg",
                true
        ))
        catalogImageList.add(CatalogImage(
                "https://www.apple.com/newsroom/images/product/iphone/standard/Apple_announce-iphone12pro_10132020.jpg.news_app_ed.jpg",
                false
        ))
        catalogImageList.add(CatalogImage(
                "https://www.gizmochina.com/wp-content/uploads/2018/09/Apple-iPhone-Xs.jpg",
                false
        ))
        catalogImageList.add(CatalogImage(
                "https://static.compareindia.news18.com/compareindia/gallery/images/2019/oct/iphone11promax7202_151120498928.jpg",
                false
        ))
        val marketPriceList = arrayListOf<CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice>()
        marketPriceList.add((CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice(
                1620340,
                1625900,
                "Rp. 14.620.340",
                "Rp. 15.625.900",
                "2020-03-01",
                "Tokopedia"
        )))
        val longDescList = arrayListOf<CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription>()
        longDescList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))



        val componentList = arrayListOf<CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component>()

        // Top Specifications
        val topsSpecifications = arrayListOf<ComponentData>()
        topsSpecifications.add(ComponentData(null,"Network","HSDPA 850 / 900 / 1900 / 2100, GSM 850 / 900 / 1800 / 1900","https://imagerouter.tokopedia.com/img/300/catalog/2013/4/30/63/63-8b9c93ac-b166-11e2-a736-6ca72523fab8.jpg",null))
        topsSpecifications.add(ComponentData(null,"Operating System","iOS 2.0","https://imagerouter.tokopedia.com/img/300/catalog/2013/4/30/63/63-8b9c93ac-b166-11e2-a736-6ca72523fab8.jpg",null))
        topsSpecifications.add(ComponentData(null,"Network","HSDPA 850 / 900 / 1900 / 2100, GSM 850 / 900 / 1800 / 1900","https://imagerouter.tokopedia.com/img/300/catalog/2013/4/30/63/63-8b9c93ac-b166-11e2-a736-6ca72523fab8.jpg",null))
        topsSpecifications.add(ComponentData(null,"Operating System","iOS 2.0","https://imagerouter.tokopedia.com/img/300/catalog/2013/4/30/63/63-8b9c93ac-b166-11e2-a736-6ca72523fab8.jpg",null))
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                1,
                "Catalog Top Spec",
                "catalog-spec-horizontal-scroll",
                false,
                topsSpecifications
        ))


        // Full Specifications
        val values = "23 Oktober 2020"
        val row = ComponentData.SpecificationsRow(
                "Tanggal",
                values
        )
        val rows = arrayListOf<ComponentData.SpecificationsRow>()
        rows.add(row)
        val specification = ComponentData(
                "Rilis",
                null,
                null,
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/home-house-homepage-building-20.png",
                rows,
        )
        val values2 = "GSM / CDMA / HSPA / EVDO / LTE / 5G"
        val row2 = ComponentData.SpecificationsRow(
                "Teknologi",
                values2
        )
        val rows2 = arrayListOf<ComponentData.SpecificationsRow>()
        rows2.add(row2)
        val specification2 = ComponentData(
                "Jaringan",
                null,
                null,
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                rows2
        )

        val values3 = "146.7 x 71.5 x 7.4 mm (5.78 x 2.81 x 0.29 in)"
        val row3 = ComponentData.SpecificationsRow(
                "Dimensi",
                values3
        )
        val values4 = "64 gr"
        val row4 = ComponentData.SpecificationsRow(
                "Berat",
                values4
        )

        val values5 = "Glass front (Gorilla Glass), glass back (Gorilla Glass), aluminum frame"
        val row5 = ComponentData.SpecificationsRow(
                "Built",
                values5
        )
        val rows3 = arrayListOf<ComponentData.SpecificationsRow>()
        rows3.add(row3)
        rows3.add(row4)
        rows3.add(row5)
        val specification3 = ComponentData(
                "Body",
                null,
                null,
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                rows3,
        )

        val specifications = arrayListOf<ComponentData>()
        specifications.add(specification)
        specifications.add(specification2)
        specifications.add(specification3)
        specifications.add(specification)
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                2,
                "Catalog Spec",
                "catalog-spec-full-data",
                false,
                specifications
        ))

        val basicInfo  = CatalogResponseData.CatalogGetDetailModular.BasicInfo(
                "64743",
                "3054",
                "iPhone 12 - 64GB",
                "Apple",
                "10 HP Terbaik",
                 "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro.",
                "https://www.tokopedia.com/catalog/64743/samsung-galaxy-a10s",
                "https://m.tokopedia.com/catalog/64743/samsung-galaxy-a10s",
                catalogImageList,
                marketPriceList,
                longDescList
        )
        val catalogGetDetailModular = CatalogResponseData.CatalogGetDetailModular(basicInfo,componentList)
        return CatalogResponseData(catalogGetDetailModular)
    }

    fun getDummyProductList(): List<ProductsItem> {
        val array = arrayListOf<ProductsItem>()
        array.add((ProductsItem(
                null,
                "https://images.tokopedia.net/img/cache/700/VqbcmM/2020/12/18/f55e4438-02da-4b01-9a76-a74e6c423bea.jpg\"",
                Shop(false,"Jakarta Pusat","CMP phone","",false,"",
                        false,"",
                        5982341, "https://www.tokopedia.com/cmpphone",false),
                "Rp13.699.000",
                false,
                false,
                5,
                "Handphone  Tablet",
                0,23,"Rp13.699.000",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2020/12/18/f55e4438-02da-4b01-9a76-a74e6c423bea.jpg",
                1432537477,
                "handphone-tablet/handp hone/ios",
                0,
                1213,
                65,
                "",
                31,
                "https://www.tokopedia.com/cmpphone/apple-iphone-12-mini-128gb-grs-resmi-ibox-indonesia?whid=0",
                Arrays.asList(
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback")
                ),
                Arrays.asList(
                        BadgesItem("https://images.tokopedia.net/img/official_store_badge.png", "Official Store",true),
                ),
                0,
                Arrays.asList(
                        LabelGroupsItem("integrity","textDarkGrey","Terjual 6")

                ),
                "APPLE IPHONE 12 MINI 128GB GRS RESMI IBOX INDONESIA",
                0,
                "",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/12/18/f55e4438-02da-4b01-9a76-a74e6c423bea.jpg",
                false,
                false,
                "",
                "",
                "",
                0,
                FreeOngkir(true,"https://images.tokopedia.net/img/ic_bebas_ongkir.png"),
                ""

        )))

        array.add((ProductsItem(
                null,
                "https://images.tokopedia.net/img/cache/700/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                Shop(false,"Jakarta Pusat","CMP phone","",false,"",
                        false,"",
                        5982341, "https://www.tokopedia.com/cmpphone",false),
                "Rp13.699.000",
                false,
                false,
                4,
                "Handphone  Tablet",
                0,
                60,
                "Rp13.699.000",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                1276760028,
                "handphone-tablet/handp hone/ios",
                1,
                12,
                65,
                "",
                21,
                "https://www.tokopedia.com/cmpphone/apple-iphone-12-mini-128gb-grs-resmi-ibox-indonesia?whid=0",
                Arrays.asList(
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback")
                ),
                Arrays.asList(
                        BadgesItem("https://images.tokopedia.net/img/power_merchant_badge.png", "Power Badge",true),
                ),
                1,
                Arrays.asList(
                        LabelGroupsItem("integrity","textDarkGrey","Terjual 6")
                ),
                "Apple iPhone 12 Mini 256GB 128GB 64GB - Blue Green Red White Black",
                0,
                "",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                false,
                true,
                "",
                "",
                "",
                0,
                FreeOngkir(false,"https://images.tokopedia.net/img/ic_bebas_ongkir.png"),
                ""

        )))

        array.add((ProductsItem(
                null,
                "https://images.tokopedia.net/img/cache/700/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                Shop(true,"Jakarta Pusat","CMP phone","Clover",true,
                        "https://inbox.tokopedia.com/reputation/v1/badge/shop/6246981",
                        true,"Jakarta Pusat",
                        5982341, "https://www.tokopedia.com/cmpphone",true),
                "Rp12.999.000",
                false,
                false,
                4,
                "Handphone  Tablet",
                0,
                5,"Rp13.699.000",
                "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                1362477524,
                "handphone-tablet/handp hone/ios",
                1,
                234,
                65,
                "",
                24,
                "https://www.tokopedia.com/cmpphone/apple-iphone-12-mini-128gb-grs-resmi-ibox-indonesia?whid=0",
                Arrays.asList(
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback"),
                        LabelsItem("lightGreen","Cashback")
                ),
                Arrays.asList(
                        BadgesItem("https://images.tokopedia.net/img/official_store_badge.png", "Official Store",true),
                ),
                0,
                Arrays.asList(
                        LabelGroupsItem("integrity","textDarkGrey","Terjual 1")
                ),
                "Apple iPhone 12 Mini 256GB 128GB 64GB - Blue Green Red White Black",
                0,
                "",
                "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/12/28/1ce15d8b-f5c1-42f5-8e19-7e23e7650a6d.png",
                false,
                false,
                "",
                "",
                "",
                0,
                FreeOngkir(true,"https://images.tokopedia.net/img/ic_bebas_ongkir.png"),
                ""

        )))

        return array
    }
}