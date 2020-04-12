package com.tokopedia.reviewseller.common

import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel

/**
 * @author by milhamj on 2020-02-14.
 */

object ReviewSellerConstant {
    //TODO add data static list product review
    val listProductReview = mutableListOf<ProductReviewUiModel>().apply {
        add(ProductReviewUiModel(
                "Adidas NMD Pharell William x Human Made",
                "https://cms-cdn.thesolesupplier.co.uk/2019/05/human-made-adidas-nmd-shoes.jpg",
                "5.0",
                "/ 5 ulasan"))
        add(ProductReviewUiModel(
                "Nike Air Force 1 Low White Red Casual Shoes",
                "https://www.sepsport.com/media/x490/Nike_Air_Force_Shoes/Air_Force_1_Low/Nike_Air_Force_1_Low_White_Red_Casual_Shoes_923027-100.jpg",
                "4.6",
                "/ 5 ulasan"))
        add(ProductReviewUiModel(
                "Nike Moon Racer Qs-Yellow Ochre/Gym Blue-Sail",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/v/bv7779700-depan.jpg",
                "5.0",
                "/ 142 ulasan"))
        add(ProductReviewUiModel(
                "Nike Air Force 1 Low",
                "https://c.static-nike.com/a/images/t_PDP_1280_v1/f_auto/vddy0hfi3ok08gfel1vn/air-force-1-low-retro-shoe-05nKGx.jpg",
                "5.0",
                "/ 5 ulasan"))
        add(ProductReviewUiModel(
                "Nike Zoom 2K-White/Black-Gym Red-White",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/a/o/ao0269107-depan.jpg",
                "4.0",
                "/ 5 ulasan"))
        add(ProductReviewUiModel(
                "Air Max 270 React-White/Dynamic Yellow-Black-Bright Violet",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/a/o/ao4971101-air-max-270-react-depan.jpg",
                "3.9",
                "/ 1.200 ulasan"))
        add(ProductReviewUiModel(
                "AIR VAPORMAX PLUS-BLACK",
                "https://c.static-nike.com/a/images/t_prod_ss/w_640,c_limit,f_auto/g0gt8fhszcdjzhzr5qgc/nike-air-vapormax-plus-black-volt-release-date.jpg",
                "3.6",
                "/ 424 ulasan"))
        add(ProductReviewUiModel(
                "Air Max 720-Cool Grey/Black-Wolf Grey",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/a/o/ao2924002-depan.jpg",
                "3.5",
                "/ 1.230 ulasan"))
        add(ProductReviewUiModel(
                "Nike React Element 55-Spruce Aura/Volt-Spruce Fog-Barely Volt",
                "https://kickz.akamaized.net/fi/media/images/p/1200/nike-REACT_ELEMENT_55-SPRUCE_AURA_VOLT_SPRUCE_FOG_BARELY_VOLT-1.jpg",
                "3.5",
                "/ 1.200 ulasan"))
        add(ProductReviewUiModel(
                "Nike React Element 55-Spruce Aura/Volt-Spruce Fog-Barely Volt",
                "https://images-na.ssl-images-amazon.com/images/I/71bcJKtUjtL._AC_UX395_.jpg",
                "3.4",
                "/ 142 ulasan"))
        add(ProductReviewUiModel(
                "Nike React Element 55-Spruce Aura/Volt-Spruce Fog-Barely Volt",
                "https://uptherestore.com/media/catalog/product/cache/1/thumbnail/2600x/6b9ffbf72458f4fd2d3cb995d92e8889/1/5/1576534354319nike-air-max-90-light-cream-alligator-pale-ivory-black-1.jpg.jpg",
                "3.3",
                "/ 1200 ulasan"))
        add(ProductReviewUiModel(
                "AIR VAPORMAX PLUS-BLACK",
                "https://c.static-nike.com/a/images/t_prod_ss/w_640,c_limit,f_auto/g0gt8fhszcdjzhzr5qgc/nike-air-vapormax-plus-black-volt-release-date.jpg",
                "3.6",
                "/ 424 ulasan"))
        add(ProductReviewUiModel(
                "Nike Moon Racer Qs-Yellow Ochre/Gym Blue-Sail",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/v/bv7779700-depan.jpg",
                "2.0",
                "/ 2 ulasan"))
        add(ProductReviewUiModel(
                "NMD_CS1 PK-CORE BLACK",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/B/D/BD7733-depan.jpg",
                "2.0",
                "/ 2 ulasan"))
        add(ProductReviewUiModel(
                "Nike Air Max 90 Nrg-Light Cream/Alligator-Pale Ivory-Black",
                "https://ourdailydose.net/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/c/i/ci5646200-nike_air_max_90_nrg-depan.jpg",
                "1.0",
                "/ 1 ulasan"))
    }

    val summaryReviewProduct = ProductRatingOverallModel(
            "4.6",
            "1.234",
            "1 Des 2019 - Hari ini")

    val filterAndSortComposition = FilterAndSortModel("7 Hari Terakhir")

    val headerRatingData = HeaderModel("4.6", "1.234 Ulasan")
    val listRatingBarData = mutableListOf<ItemRatingBarModel>().apply {
        add(ItemRatingBarModel("5", "1.200"))
        add(ItemRatingBarModel("4", "24"))
        add(ItemRatingBarModel("3", "6"))
        add(ItemRatingBarModel("2", "0"))
        add(ItemRatingBarModel("1", "4"))
}

const val GQL_GET_PRODUCT_REVIEW_LIST = "GQL_GET_PRODUCT_REVIEW_LIST"
const val GQL_GET_PRODUCT_RATING_OVERALL = "GQL_GET_PRODUCT_RATING_OVERALL"
