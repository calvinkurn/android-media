package com.tokopedia.feed.component.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.feed.component.sample.listener.DefaultDynamicPostListener
import com.tokopedia.feed.component.sample.listener.DefaultGridItemListener
import com.tokopedia.feed.component.sample.listener.DefaultImagePostListener
import com.tokopedia.feed.component.sample.listener.DefaultVideoViewListener
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.*
import com.tokopedia.feedcomponent.view.widget.PostDynamicViewNew
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class FeedPostCampaignTestActivity : AppCompatActivity() {

    private val session: UserSessionInterface by lazy { UserSession(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_asgc_campaign_test)

        val postDynamicViewNew = findViewById<PostDynamicViewNew>(R.id.item_post_dynamic_view)

        postDynamicViewNew.bindData(
            DefaultDynamicPostListener(),
            DefaultGridItemListener(),
            DefaultVideoViewListener(),
            0,
            session,
            dummyData,
            DefaultImagePostListener()
        )
    }

    private val dummyData = FeedXCard(
        id = "217052966",
        title = "Karya Indah Furniture Jambi",
        campaign = FeedXCampaign(
            status = "ongoing",
            name = "Flash Sale Toko",
            startTime = "2023-02-21 12:00:00",
            endTime = "2023-02-21 23:00:00",
            shortName = "Parade Diskon",
        ),
        cta = FeedXCta(
            text = "Cek 5 Produk Flash Sale Lainnya",
            color = "#D74049",
            colorGradient = listOf(
                FeedXCtaColorGradient(
                    color = "#D74049",
                    position = 0f
                ),
                FeedXCtaColorGradient(
                    color = "#F56958",
                    position = 100f
                )
            ),
        ),
        ribbonImageURL = "https://images.tokopedia.net/img/feeds/ribbon-overlay-fst.png"
    ).apply {
        typename = "FeedXCardProductsHighlight"
        author = FeedXAuthor(
            type = 2,
            name = "Karya Indah Furniture Jambi",
            id = "10589950",
            badgeURL = "https://images.tokopedia.net/img/official_store/badge_os.png",
            logoURL = "https://images.tokopedia.net/img/cache/215-square/GAnVPX/2022/3/6/b9d7868f-c9dd-4b32-9518-756a0760b6e7.jpg"
        )
        subTitle = "Produk baru"
        text = "Hai, apa kabar \uD83D\uDE01? 15 Barang baru yang kamu tunggu kini udah ada di toko kami, lho! Cek sekarang, ya!"
        totalProducts = 4
        products = listOf(
            FeedXProduct(
                name = "Skintific 5X Ceramide Low pH Cleanser for Sensitive Skin 80ml - 80ml",
                coverURL = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg",
            ),
            FeedXProduct(
                name = "Skintific 5X Ceramide Low pH Cleanser for Sensitive Skin 80ml - 80ml",
                coverURL = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg",
            ),
            FeedXProduct(
                name = "Skintific 5X Ceramide Low pH Cleanser for Sensitive Skin 80ml - 80ml",
                coverURL = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg",
            ),
            FeedXProduct(
                name = "Skintific 5X Ceramide Low pH Cleanser for Sensitive Skin 80ml - 80ml",
                coverURL = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg",
            )
        )
        media = listOf(
            FeedXMedia(
                type = "image",
                coverUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg",
                mediaUrl =  "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/a6ea3c3b-994f-4a14-a351-8b880d0e6a6f.jpg"
            ),
            FeedXMedia(
                type = "image",
                coverUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
                mediaUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
            ),
            FeedXMedia(
                type = "image",
                coverUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
                mediaUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
            ),
            FeedXMedia(
                type = "image",
                coverUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
                mediaUrl = "https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2022/8/24/f66b8e3f-574a-425d-a616-53dcce37672e.jpg",
            )
        )
        mods = listOf(
            "products:layout_grid_2x3",
            "use_new_design"
        )
    }
}

