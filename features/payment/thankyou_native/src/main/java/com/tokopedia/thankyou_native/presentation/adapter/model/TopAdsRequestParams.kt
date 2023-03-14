package com.tokopedia.thankyou_native.presentation.adapter.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.TopAdsUIModel
import com.tokopedia.thankyou_native.presentation.adapter.factory.BottomContentFactory
import com.tokopedia.thankyou_native.presentation.views.widgettag.WidgetTag

data class TopAdsRequestParams(
    @SerializedName("type")
    var type: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("desc")
    var description: String,
    @SerializedName("section_title")
    var sectionTitle: String,
    @SerializedName("section_desc")
    var sectionDescription: String?,
    @SerializedName("ep")
    var adsType: String,
    @SerializedName("inventory_id")
    var inventoryId: String,
    @SerializedName("item")
    var itemCount: String,
    @SerializedName("dimen_id_mobile")
    var dimen: String,
    @SerializedName("data")
    var topAdsUIModelList: List<TopAdsUIModel>?,
): Visitable<BottomContentFactory>, WidgetTag {

    override fun type(typeFactory: BottomContentFactory): Int {
        return typeFactory.type(this)
    }

    override val tag: String = TAG

    companion object {
        private const val TAG = "tdn"
    }
}
