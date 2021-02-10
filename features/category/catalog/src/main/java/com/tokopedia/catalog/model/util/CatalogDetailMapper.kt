package com.tokopedia.catalog.model.util

import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogInfoDataModel
import com.tokopedia.catalog.model.datamodel.CatalogSpecificationDataModel
import com.tokopedia.catalog.model.raw.CatalogResponse
import com.tokopedia.catalog.model.raw.ComponentData
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.SpecificationsComponentData

object CatalogDetailMapper {

    fun mapIntoVisitable(catalogGetDetailModular : CatalogResponse.CatalogResponseData.CatalogGetDetailModular): MutableList<BaseCatalogDataModel> {
        val listOfComponents : MutableList<BaseCatalogDataModel> = mutableListOf()

        // Adding CatalogInfoDataModel
        catalogGetDetailModular.basicInfo.run {
            listOfComponents.add(CatalogInfoDataModel(name = "Catalog Info",type= CatalogConstant.CATALOG_INFO,
                    productName = name, productBrand = brand, tag = tag,
                    priceRange = "${marketPrice[0].minFmt} - ${marketPrice[0].maxFmt}" ,
                    description = description, images = catalogGetDetailModular.basicInfo.catalogImage))
        }

        catalogGetDetailModular.basicInfo.components.forEachIndexed { index, component ->
            when(component.type){
                CatalogConstant.CATALOG_SPECIFICATION -> {
                    // TODO Optimize
                    val crudeSpecificationsData = component.data
                    val specifications = arrayListOf<SpecificationsComponentData>()
                    crudeSpecificationsData.forEachIndexed { indexComponentData, componentData ->
                        specifications.add(SpecificationsComponentData(componentData.name,componentData.specificationsRow))
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


    fun mapToBasicInfo(basicInfo: CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo): CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo {
        return basicInfo
    }


    fun getDummyCatalogData() : CatalogResponse{

        val longDesc = "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro.\n" +
                "\n" +
                "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro.\n" +
                "\n" +
                "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro.\n" +
                "\n" +
                "Apple merilis iPhone 12 sebagai alternatif dari iPhone 12 Pro dan iPhone 12 Pro Max dengan spesifikasi cukup mumpuni dengan harga lebih murah. Berbeda dengan series Pro, iPhone 12 ini hanya mempunyai dua kamera utama pada bagian belakang. Dan juga tidak ada fitur-fitur yang “tidak perlu” dari series Pro."
        val catalogImageList = arrayListOf<CatalogImage>()
        catalogImageList.add(CatalogImage(
                "https://img.etimg.com/thumb/width-400,height-300,msid-79662724,imgsize-/.jpg",
                true
        ))
        catalogImageList.add(CatalogImage(
                "https://static.toiimg.com/photo/79167081.cms",
                false
        ))
        catalogImageList.add(CatalogImage(
                "https://www.gizbot.com/images/2020-04/apple-iphone-12-concept_158711095630.jpg",
                false
        ))
        catalogImageList.add(CatalogImage(
                "https://images.idgesg.net/images/article/2020/10/iphone-12-pro-colors-100862075-large.jpg",
                false
        ))
        val marketPriceList = arrayListOf<CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice>()
        marketPriceList.add((CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.MarketPrice(
                1620340,
                1625900,
                "Rp. 14.620.340",
                "Rp. 15.625.900",
                "2020-03-01",
                "Tokopedia"
        )))
        val longDescList = arrayListOf<CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription>()
        longDescList.add(CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        longDescList.add(CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.LongDescription(
                "Desain kokoh dari iPhone 5s",
                longDesc
        ))
        val componentList = arrayListOf<CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component>()
        val values = arrayListOf<String>()
        values.add("23 Oktober 2020")
        val row = ComponentData.SpecificationsRow(
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/home-house-homepage-building-20.png",
                "Tanggal",
                values
        )
        val rows = arrayListOf<ComponentData.SpecificationsRow>()
        rows.add(row)
        val specification = ComponentData(
                "Rilis",
                rows,
        )
        val values2 = arrayListOf<String>()
        values2.add("GSM / CDMA / HSPA / EVDO / LTE / 5G")
        val row2 = ComponentData.SpecificationsRow(
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                "Teknologi",
                values2
        )
        val rows2 = arrayListOf<ComponentData.SpecificationsRow>()
        rows2.add(row2)
        val specification2 = ComponentData(
                "Jaringan",
                rows2,
        )

        val values3 = arrayListOf<String>()
        values3.add("146.7 x 71.5 x 7.4 mm (5.78 x 2.81 x 0.29 in)")
        val row3 = ComponentData.SpecificationsRow(
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                "Dimensi",
                values3
        )
        val values4 = arrayListOf<String>()
        values4.add("64 gr")
        val row4 = ComponentData.SpecificationsRow(
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                "Berat",
                values4
        )

        val values5 = arrayListOf<String>()
        values5.add("Glass front (Gorilla Glass), glass back (Gorilla Glass), aluminum frame")
        val row5 = ComponentData.SpecificationsRow(
                "https://cdn4.iconfinder.com/data/icons/basic-user-interface-elements/700/exit-enter-leave-out-door-20.png",
                "Built",
                values5
        )
        val rows3 = arrayListOf<ComponentData.SpecificationsRow>()
        rows3.add(row3)
        rows3.add(row4)
        rows3.add(row5)
        val specification3 = ComponentData(
                "Body",
                rows3,
        )

        val specifications = arrayListOf<ComponentData>()
        specifications.add(specification)
        specifications.add(specification2)
        specifications.add(specification3)
        specifications.add(specification)
        componentList.add(CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo.Component(
                1,
                "Catalog Spec",
                "catalog-spec-horizontal-scroll",
                false,
                specifications
        ))

        val header = CatalogResponse.CatalogResponseData.CatalogGetDetailModular.Header(200,"Good")
        val basicInfo  = CatalogResponse.CatalogResponseData.CatalogGetDetailModular.BasicInfo(
                "64743",
                "3054",
                "iPhone 12 - 64GB",
                "Apple",
                "10 HP Terbaik",
                 "<p>\r\nGalaxy A10s terlihat sangat mirip dengan A10 karena dibangun menggunakan bahan baku plastik mengkilap yang sama. Tapi, dengan ukuran 156,9 x 75,8 x 7,8mm yang lebih tinggi, lebih luas, dan lebih ramping dari A10. Smartphone ini memiliki berat 168g yang cukup ringan dan nyaman digenggam.\r\n<br>\r\n</p>\r\n<p>\r\nDi bagian depan, ada tampilan Infinity-V dengan bagian bawah yang berukuran relatif besar. Tombol daya ada di sisi kanan perangkat, sementara tombol volume dan tempat penyimpanan SIM serta slot microSD ada di sebelah kiri. Ada jack headphone, port micro-USB, dan mic di bagian bawah, meninggalkan bagian atas hanya tersisa dengan mic sekunder. Di sisi belakang, ada dual-kamera yang tersusun secara vertikal dan lampu LED di sudut kiri atas, sedangkan terdapat branding Samsung di bagian tengah. Untuk fingerprint, ini tepat terletak di atasnya. Semuanya tampilan bagian belakang sebenarnya terlihat cukup berbeda dari A10.\r\n<br>\r\n</p>\r\n<p>\r\nGalaxy A10s dilengkapi dengan layar HD + Infinity-V 6,2 inci, CPU Mediatek Helio P22, GPU PowerVR GE8320, RAM 2GB, penyimpanan internal 32GB, pengaturan kamera belakang 13MP + 2MP, kamera selfie 5MP, baterai 4.000 mAh, dan Samsung One UI berdasarkan Android Pie. Dibandingkan dengan A10, A10s memiliki kamera tambahan di bagian belakang, baterai lebih besar, dan mungkin chipset yang sedikit lebih baik. Smartphone ini juga memiliki pembaca fingerprint yang cukup responsif. \r\n<br>\r\n</p>\r\n<p>\r\nLayar pada Galaxy A10s tidak jauh berbeda dari A10. Smartphone canggih keluaran Samsung ini dilengkapi layar Infinity-V 6,2 inci dengan resolusi 720 × 1520 piksel, yang memberikan kerapatan piksel ~ 271 PPI. Samsung masih setia menggunakan panel IPS LCD yang berwarna cerah dan kontras layar Super AMOLED sama seperti di perangkat Galaxy lainnya. A10s juga menjadi smartphone paling dasar dari seri Galaxy A yang diluncurkan Samsung. Galaxy A10s membanggakan sistem dual-kamera yang mereka miliki di bagian belakangnya. Kamera ini terdiri dari sensor utama 13 MP f / 1.9 dan sensor kedalaman 2 MP f / 2.4 untuk efek bokeh yang lebih baik. Di bagian depan, ada selfie shooter 5 MP f / 2.0. \r\n<br>\r\n</p>\r\n<p>\r\nKamera belakang utama berperan untuk mendukung pemotretan panorama, HDR, dan dapat merekam video 1080p pada 30fps. Sekali lagi, hardware dan kemampuan kamera nampak identik dengan apa yang bisa kamu temukan pada A10. Perbedaan lain yang bisa kamu temukan adalah A10s memiliki model yang lebih baru dengan sensor kedalaman khusus di bagian belakang. Dengan Galaxy A10s, kamu bisa menjalankan fitur Samsung One berbasiskan Android Pie yang out of the box. \r\n</p>",
                "https://www.tokopedia.com/catalog/64743/samsung-galaxy-a10s",
                "https://m.tokopedia.com/catalog/64743/samsung-galaxy-a10s",
                catalogImageList,
                marketPriceList,
                longDescList,
                componentList
        )
        val catalogGetDetailModular = CatalogResponse.CatalogResponseData.CatalogGetDetailModular(header,basicInfo)
        val catalogDataResponse = CatalogResponse.CatalogResponseData(catalogGetDetailModular)
        return CatalogResponse(catalogDataResponse)
    }

}