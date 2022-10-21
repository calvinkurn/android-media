package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.ImageUploadDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

data class OrderData(
        val ticker: TickerData? = null,
        var onboarding: OccOnboarding = OccOnboarding(),
        var cart: OrderCart = OrderCart(),
        var preference: OrderProfile = OrderProfile(),
        var promo: OrderPromo = OrderPromo(),
        var payment: OrderPayment = OrderPayment(),
        var prompt: OccPrompt = OccPrompt(),
        // LCA
        val errorCode: String = "",
        val popUpMessage: String = "",
        // AFPB
        val totalProductPrice: String = "",
        val profileCode: String = "",
        // Gifting
        val popUp: PopUpData = PopUpData(),
        // Epharmacy
        val imageUpload: ImageUploadDataModel = ImageUploadDataModel()
)
