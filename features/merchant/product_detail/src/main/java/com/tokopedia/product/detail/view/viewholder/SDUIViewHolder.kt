package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductShipmentDataModel
import com.tokopedia.product.detail.databinding.ViewShipmentBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.sdui.SDUIManager
import org.json.JSONObject

class SDUIViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductShipmentDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_sdui_container
    }

    private val context = view.context
    private val binding = ItemSduiContainerBinding.bind(view)


    private val viewMainDelegate = lazy {
        ViewShipmentBinding.bind(
            binding.pdpShipmentStateMain.inflate()
        )
    }

    private val sduiManager = lazy {
        SDUIManager().apply {
            initSDUI(context)
        }
    }

    override fun bind(element: ProductShipmentDataModel) {
        loadSduiWidget(element)
    }


    private fun loadSduiWidget(
        element: ProductShipmentDataModel,
    ) {
        createAndAddSDUIView()
    }

    private fun createAndAddSDUIView(){
        val sduiJsonStr = "{\"templates\":{\"header\":{\"type\":\"container\",\"orientation\":\"horizontal\",\"paddings\":{\"top\":12,\"left\":12,\"bottom\":12},\"items\":[{\"type\":\"container\",\"orientation\":\"vertical\",\"items\":[{\"type\":\"text\",\"\$text\":\"header_title\",\"font_size\":20,\"font_weight\":\"bold\"},{\"type\":\"text\",\"\$text\":\"header_subtitle\",\"font_size\":16}]},{\"type\":\"text\",\"\$text\":\"header_see_more\",\"text_alignment_horizontal\":\"right\",\"margins\":{\"right\":12},\"width\":{\"type\":\"wrap_content\"},\"font_size\":16,\"text_color\":\"#008000\",\"actions\":[{\"log_id\":\"see_more_clicked\",\"\$url\":\"see_more_url\"}]}]},\"campaign_banner\":{\"type\":\"container\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":180},\"paddings\":{\"top\":8,\"left\":8,\"right\":8,\"bottom\":8},\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":180},\"\$image_url\":\"promotional_banner\",\"actions\":[{\"log_id\":\"promotional_banner_clicked\",\"\$url\":\"promotional_banner_url\"}]}]},\"product_card\":{\"type\":\"container\",\"width\":{\"type\":\"fixed\",\"value\":130},\"height\":{\"type\":\"wrap_content\"},\"background\":[{\"type\":\"solid\",\"color\":\"#FFFFFF\"}],\"border\":{\"corner_radius\":8,\"has_shadow\":true,\"shadow\":{\"alpha\":2,\"blur\":2,\"color\":\"#0E000000\"},\"stroke\":{\"color\":\"#0E000000\",\"unit\":\"div-stroke\",\"width\":1}},\"paddings\":{\"bottom\":16},\"actions\":[{\"log_id\":\"product_card_clicked\",\"\$url\":\"product_card_url\"}],\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":130},\"height\":{\"type\":\"fixed\",\"value\":130},\"\$image_url\":\"product_image\"},{\"type\":\"text\",\"\$text\":\"product_name\",\"margins\":{\"top\":8,\"left\":8,\"right\":8},\"font_size\":12,\"font_weight\":\"medium\",\"max_lines\":2},{\"type\":\"text\",\"\$text\":\"product_price\",\"margins\":{\"top\":4,\"left\":8,\"right\":8},\"font_size\":14,\"font_weight\":\"bold\",\"max_lines\":2},{\"type\":\"container\",\"orientation\":\"horizontal\",\"content_alignment_vertical\":\"center\",\"width\":{\"type\":\"wrap_content\"},\"margins\":{\"top\":4,\"left\":8,\"right\":8},\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":20},\"height\":{\"type\":\"fixed\",\"value\":20},\"\$image_url\":\"shop_badge\"},{\"type\":\"text\",\"\$text\":\"shop_name\",\"font_size\":12,\"font_weight\":\"medium\",\"width\":{\"type\":\"wrap_content\"}}]}]}},\"card\":{\"log_id\":\"recom_widget_campaign\",\"states\":[{\"state_id\":0,\"div\":{\"type\":\"container\",\"orientation\":\"vertical\",\"height\":{\"type\":\"wrap_content\"},\"items\":[{\"type\":\"header\",\"header_title\":\"Kejar Diskon Spesial\",\"header_subtitle\":\"11.11 Campaign\",\"header_see_more\":\"Lihat Semua\",\"see_more_url\":\"div-action://route?applink=https://www.tokopedia.com/rekomendasi/6797291084/d?ref=pdp_1_os&product_ids=6797291084\"},{\"type\":\"campaign_banner\",\"promotional_banner\":\"https://mir-s3-cdn-cf.behance.net/project_modules/1400/1b23c832616941.568cab27a6aad.jpg\",\"promotional_banner_url\":\"div-action://route?applink=https://www.tokopedia.com/discovery/deals\"},{\"type\":\"gallery\",\"height\":{\"type\":\"fixed\",\"value\":250},\"width\":{\"type\":\"match_parent\"},\"item_spacing\":12,\"margins\":{\"left\":12,\"right\":12},\"items\":[{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/2/15/ab8be3ca-5cf2-48ee-9f5a-0037f79a7df6.jpg\",\"product_name\":\"Apple iPhone 13 Garansi Resmi - 128GB 256GB 512GB - 128 gb, MIDNIGHT BLACK\",\"product_price\":\"Rp11.489.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-13-garansi-resmi-128gb-256gb-512gb-128-gb-midnight-black\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2022/10/29/e2aa67f3-baa1-4f16-8f03-0c27ee8a4735.png\",\"product_name\":\"Apple iPhone 14 Pro Garansi Resmi - 128GB 256GB 512GB 1TB - 128GB, Space Black\",\"product_price\":\"Rp17.279.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-14-pro-garansi-resmi-128gb-256gb-512gb-1tb-128gb-space-black\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2022/10/28/692ed302-4f92-41d2-9b0a-a02097167a36.png\",\"product_name\":\"Apple iPhone 14 Plus Garansi Resmi - 128GB 256GB 512GB - 256GB, Red\",\"product_price\":\"Rp17.089.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"iSmile Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ismileofficial/apple-iphone-14-plus-garansi-resmi-128gb-256gb-512gb-256gb-red\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/8/25/8a31bb36-5817-4db5-8fc9-103a38cf27aa.png\",\"product_name\":\"iPhone 14 Promax Garansi Resmi - Promo 128GB, Deep Purple\",\"product_price\":\"Rp18.818.000\",\"shop_badge\":\"https://images.tokopedia.net/img/ikRAny/2022/9/13/b0a5bec3-cf77-4701-8daa-0260a8f5076c.png\",\"shop_name\":\"PT Pratama Sntra Semesta\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/ptpratamasemesta/iphone-14-promax-garansi-resmi-promo-128gb-deep-purple\"},{\"type\":\"product_card\",\"product_image\":\"https://images.tokopedia.net/img/cache/900/VqbcmM/2023/9/8/b82d3e15-a2e9-460c-bc96-c43a882f62ba.jpg\",\"product_name\":\"Iphone 14 128 GB Garansi Resmi Ibox (INSTAN AREA MALANG)\",\"product_price\":\"Rp13.499.000\",\"shop_badge\":\"https://images.tokopedia.net/img/goldmerchant/pm_activation/badge/Power%20Merchant%20Pro.png\",\"shop_name\":\"Meteor Cell Official Store\",\"product_card_url\":\"div-action://route?applink=https://www.tokopedia.com/meteorcell/iphone-14-128-gb-garansi-resmi-ibox-instan-area-malang\"}]}]}}]}}"
        val sduiJson = JSONObject(sduiJsonStr)
        val templateJson = sduiJson.optJSONObject("templates")
        val cardJson = sduiJson.getJSONObject("card")

        binding.divkitViewContainer.removeAllViews()
        binding.divkitViewContainer.addView(sduiManager.value.createView(context,
            templateJson, "divKitView", cardJson))
    }

}
