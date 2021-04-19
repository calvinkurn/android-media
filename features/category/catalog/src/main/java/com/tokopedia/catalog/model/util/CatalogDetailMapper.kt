package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.*
import com.tokopedia.catalog.model.raw.*

object CatalogDetailMapper {

    fun mapIntoVisitable(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = CatalogConstant.CATALOG_INFO_NAME, type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, tag = tag,
                    priceRange = "${marketPrice?.firstOrNull()?.minFmt} - ${marketPrice?.firstOrNull()?.maxFmt}" ,
                    description = description, shortDescription = shortDescription,
                    images = catalogGetDetailModular.basicInfo.catalogImage))
        }

        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when(component.type){
                CatalogConstant.TOP_SPECIFICATIONS -> {
                    val crudeTopSpecificationsData = component.data
                    val topSpecsArray = arrayListOf<TopSpecificationsComponentData>()
                    crudeTopSpecificationsData?.forEachIndexed { _, componentData ->
                        topSpecsArray.add(TopSpecificationsComponentData(componentData.key,
                                componentData.value, componentData.icon))
                    }
                    listOfComponents.add(CatalogTopSpecificationDataModel(name = component.name, type = component.type , topSpecificationsList = topSpecsArray))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {
                    listOfComponents.add(CatalogProductsContainerDataModel(name = component.name, type = component.type, catalogId = catalogGetDetailModular.basicInfo.id))
                }

                CatalogConstant.VIDEO -> {
                    val crudeVideoData = component.data
                    val videoArray = arrayListOf<VideoComponentData>()
                    crudeVideoData?.forEachIndexed { _, componentData ->
                        videoArray.add(VideoComponentData(componentData.url,
                                componentData.type,componentData.videoId,componentData.title,componentData.author))
                    }
                    listOfComponents.add(CatalogVideoDataModel(name = component.name, type = component.type , videosList = videoArray ))
                }
            }
        }

        return listOfComponents
    }

    fun getFullSpecificationsModel(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular) : CatalogFullSpecificationDataModel{
        var catalogFullSpecificationDataModel = CatalogFullSpecificationDataModel(arrayListOf())
        catalogGetDetailModular.components?.forEachIndexed { _, component ->
            when (component.type) {
                CatalogConstant.FULL_CATALOG_SPECIFICATION -> {
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<FullSpecificationsComponentData>()
                    crudeSpecificationsData?.forEachIndexed { _, componentData ->
                        specifications.add(FullSpecificationsComponentData(componentData.name,
                                componentData.icon, componentData.specificationsRow
                                ?: arrayListOf()))
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
        val icon = "https://image.flaticon.com/icons/png/128/3524/3524636.png"
        topsSpecifications.add(ComponentData(null, "Network", "HSDPA 850 / 900 / 1900 / 2100, GSM 850 / 900 / 1800 / 1900", icon, null,null,null,null,null,null))
        topsSpecifications.add(ComponentData(null, "Operating System", "iOS 2.0", icon, null,null,null,null,null,null))
        topsSpecifications.add(ComponentData(null, "Network", "HSDPA 850 / 900 / 1900 / 2100, GSM 850 / 900 / 1800 / 1900", icon, null,null, null,null,null,null))
        topsSpecifications.add(ComponentData(null, "Operating System", "iOS 2.0", icon, null,null,null,null,null,null))
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                "1",
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
                null,
                null,null,null,null
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
                rows2,
                null,
                null,
                null,
                null,
                null
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
                null,
                null,null,null,null
        )

        val specifications = arrayListOf<ComponentData>()
        specifications.add(specification)
        specifications.add(specification2)
        specifications.add(specification3)
        specifications.add(specification)
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                "2",
                "Catalog Spec",
                "catalog-spec-full-data",
                false,
                specifications
        ))

        val videos = arrayListOf<ComponentData>()
        val video1 = ComponentData(
                null,
                null,
                null,
                null,
                null,
                "https://www.youtube.com/watch?v=YKOXswCBYq0",
                "youtube","YKOXswCBYq0","CUAN BANYAK NIH? Hasil PO Samsung Galaxy A52","Arrinish"
        )

        val video2 = ComponentData(
                null,
                null,
                null,
                null,
                null,
                "https://www.youtube.com/watch?v=YKOXswCBYq0",
                "youtube","YKOXswCBYq0","CUAN BANYAK NIH? Hasil PO Samsung Galaxy A52","Arrinish"
        )

        val video3 = ComponentData(
                null,
                null,
                null,
                null,
                null,
                "https://www.youtube.com/watch?v=YKOXswCBYq0",
                "youtube","YKOXswCBYq0","CUAN BANYAK NIH? Hasil PO Samsung Galaxy A52","Arrinish"
        )

        videos.add(video1)
        videos.add(video2)
        videos.add(video3)
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                "3",
                "Video",
                "video-horizontal-scroll",
                false,
                videos
        ))

        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                "4",
                "Product List",
                "product-list-infinite-scroll",
                false,
                arrayListOf()
        ))

        val basicInfo  = CatalogResponseData.CatalogGetDetailModular.BasicInfo(
                "64743",
                "3054",
                "iPhone 12 - 64GB",
                "Apple",
                "10 HP Terbaik",
                "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro.",
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

}