package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.Extras
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.ExtrasProduct
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaMessageAttachment
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SrwQuestionViewModelTest: BaseTopChatViewModelTest() {

    @Test
    fun should_mask_user_address_for_generated_srw_question_ui_model() {
        // Given
        val intent = "intent"
        val productName = "productName"
        val attachment = HeaderCtaButtonAttachment(
            HeaderCtaMessageAttachment(
                body = "body",
                extras = Extras(
                    intent = intent,
                    extrasProduct = listOf(ExtrasProduct(
                        product_name = productName
                    ))
                ),
            )
        )
        val userLocation = LocalCacheModel(
            label = "my home"
        )

        // When
        viewModel.initUserLocation(userLocation)
        val question = viewModel.generateSrwQuestionUiModel(attachment)

        // Then
        assertEquals(question.content, "Ubah alamat pengiriman \"$productName\" ke m* ho**")
        assertEquals(question.intent, intent)
    }
}