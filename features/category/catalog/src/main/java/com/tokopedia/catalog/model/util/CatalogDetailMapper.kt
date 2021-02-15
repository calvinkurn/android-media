package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.SpecificationsComponentData

object CatalogDetailMapper {

    fun mapIntoVisitable(catalogGetDetailModular : CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        // Adding CatalogInfoDataModel
        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = "Catalog Info",type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, tag = tag,
                    priceRange = "${marketPrice[0].minFmt ?: marketPrice[0].min} - ${marketPrice[0].maxFmt ?: marketPrice[0].max}" ,
                    description = description, images = catalogGetDetailModular.basicInfo.catalogImage))
        }

        catalogGetDetailModular.components.forEachIndexed { index, component ->
            when(component.type){
                CatalogConstant.CATALOG_SPECIFICATION -> {
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<SpecificationsComponentData>()
                    crudeSpecificationsData.forEachIndexed { indexComponentData, componentData ->
                        specifications.add(SpecificationsComponentData(componentData.name,componentData.icon,componentData.specificationsRow))
                    }
                    listOfComponents.add(CatalogSpecificationDataModel(name = component.name, type = component.type , specificationsList = specifications))
                }

                CatalogConstant.CATALOG_PRODUCT_LIST -> {

                }

                CatalogConstant.CATALOG_PRODUCT_FILTER -> {

                }
            }
        }

        return listOfComponents
    }

    fun getDummyCatalogData() : CatalogResponseData{

        val longDesc = "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro."
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
        val values = "23 Oktober 2020"
        val row = ComponentData.SpecificationsRow(
                "Tanggal",
                values
        )
        val rows = arrayListOf<ComponentData.SpecificationsRow>()
        rows.add(row)
        val specification = ComponentData(
                "Rilis",
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
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                rows2,
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
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                rows3,
        )

        val specifications = arrayListOf<ComponentData>()
        specifications.add(specification)
        specifications.add(specification2)
        specifications.add(specification3)
        specifications.add(specification)
        componentList.add(CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                1,
                "Catalog Spec",
                "catalog-spec-horizontal-scroll",
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

}