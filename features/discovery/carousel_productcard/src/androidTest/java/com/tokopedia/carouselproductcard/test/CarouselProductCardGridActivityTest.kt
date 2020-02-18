package com.tokopedia.carouselproductcard.test

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.carouselproductcard.test.R


internal class CarouselProductCardGridActivityTest: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carousel_product_card_grid_activity_test_layout)

        val recyclerView = findViewById<RecyclerView>(R.id.carouselProductCardGridRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = Adapter()
    }

    internal class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val recycledViewPool = RecyclerView.RecycledViewPool()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false)
            return ViewHolder(view, recycledViewPool)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind()
        }
    }

    internal class ViewHolder(itemView: View, private val recycledViewPool: RecyclerView.RecycledViewPool): RecyclerView.ViewHolder(itemView) {

        companion object {
            val LAYOUT = R.layout.carousel_product_card_grid_item_test_layout
        }

        fun bind() {
            val item = itemView.findViewById<CarouselProductCardView>(R.id.carouselProductCard)
            item.bindCarouselProductCardViewGrid(
                    productCardModelList = mutableListOf<ProductCardModel>().also {
                        it.add(createProductCardMaxInfoAndLabel())
                        it.add(createProductCardTwoLinesProductName1())
                        it.add(createProductCardTwoLinesProductName2())
                        it.add(createProductCardTwoLinesProductName3())
                    },
                    recyclerViewPool = recycledViewPool
            )
        }

        private fun createProductCardMaxInfoAndLabel(): ProductCardModel {
            val labelProductStatus = ProductCardModel.LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
            val labelPrice = ProductCardModel.LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
            val labelGimmick = ProductCardModel.LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

            return ProductCardModel(
                    productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                        badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
                    },
                    shopLocation = "DKI Jakarta",
                    ratingString = "4.5",
                    reviewCount = 60,
                    freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
                    isTopAds = true,
                    hasOptions = true,
                    labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                        labelGroups.add(labelProductStatus)
                        labelGroups.add(labelGimmick)
                        labelGroups.add(labelPrice)
                    }
            )
        }

        private fun createProductCardTwoLinesProductName1(): ProductCardModel {
            return ProductCardModel(
                    productName = "1 Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                        badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
                    },
                    shopLocation = "DKI Jakarta",
                    ratingString = "4.5",
                    reviewCount = 60,
                    freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
                    hasOptions = true
            )
        }

        private fun createProductCardTwoLinesProductName2(): ProductCardModel {
            return ProductCardModel(
                    productName = "2 Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                        badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
                    },
                    shopLocation = "DKI Jakarta",
                    ratingString = "4.5",
                    reviewCount = 60,
                    freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
                    hasOptions = true
            )
        }

        private fun createProductCardTwoLinesProductName3(): ProductCardModel {
            return ProductCardModel(
                    productName = "3 Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                        badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
                    },
                    shopLocation = "DKI Jakarta",
                    ratingString = "4.5",
                    reviewCount = 60,
                    freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
                    hasOptions = true
            )
        }
    }
}