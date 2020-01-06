package com.tokopedia.play.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.mapper.PlaySocketType
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.websocket.WebSocketResponse
import org.junit.Assert
import org.junit.Test


/**
 * Created by mzennis on 2020-01-06.
 */
class PlaySocketMapperUnitTest {

    private lateinit var playSocketMapper: PlaySocketMapper

    private fun actual(type: String, jsonObject: JsonObject): Any? {
        playSocketMapper = PlaySocketMapper(WebSocketResponse(
                type = type,
                code = 200,
                jsonObject = jsonObject))
        return playSocketMapper.mapping()
    }

    @Test
    fun mappingTotalLike() {
        val type = PlaySocketType.TotalClick.value
        val jsonObject = JsonObject()
        jsonObject.addProperty("total_click", "20")

        val actual = actual(type, jsonObject)
        val expected = TotalLike("20")

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is TotalLike)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingTotalView() {
        val type = PlaySocketType.TotalView.value
        val jsonObject = JsonObject()
        jsonObject.addProperty("total_view", "20")

        val actual = actual(type, jsonObject)
        val expected = TotalView("20")

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is TotalView)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingIncomingChat() {
        val type = PlaySocketType.ChatPeople.value
        val userData = JsonObject()
        userData.addProperty("id", "123")
        userData.addProperty("name", "Robby")
        userData.addProperty("image", "Photo")

        val jsonObject = JsonObject()
        jsonObject.addProperty("channel_id", "80")
        jsonObject.addProperty("msg_id", "123")
        jsonObject.addProperty("message", "This is message")
        jsonObject.add("user", userData)

        val actual = actual(type, jsonObject)
        val expected = PlayChat(channelId = "80",
                messageId = "123",
                message = "This is message",
                user = PlayChat.UserData("123", "Robby", "Photo"))

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is PlayChat)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingPinnedMessage() {
        val type = PlaySocketType.PinnedMessage.value
        val jsonObject = JsonObject()
        jsonObject.addProperty("pinned_message_id", 123)
        jsonObject.addProperty("title", "This is Title")
        jsonObject.addProperty("message", "This is Message")
        jsonObject.addProperty("image_url", "image url")
        jsonObject.addProperty("redirect_url", "redirect url")

        val actual = actual(type, jsonObject)
        val expected = PinnedMessage(
                123,
                "This is Title",
                "This is Message",
                "image url",
                "redirect url")

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is PinnedMessage)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingQuickReply() {
        val type = PlaySocketType.QuickReply.value
        val quickReply = JsonArray()
        quickReply.add("smile")
        quickReply.add("sad")
        quickReply.add("angry")

        val jsonObject = JsonObject()
        jsonObject.add("quick_reply", quickReply)

        val actual = actual(type, jsonObject)
        val expected = QuickReply(
                listOf("smile", "sad", "angry")
        )

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is QuickReply)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingBanned() {
        val type = PlaySocketType.EventBanned.value

        val jsonObject = JsonObject()
        jsonObject.addProperty("is_banned", true)
        jsonObject.addProperty("is_freeze", false)
        jsonObject.addProperty("channel_id", "")
        jsonObject.addProperty("user_id", "123")

        val actual = actual(type, jsonObject)
        val expected = BannedFreeze(
                isBanned = true,
                isFreeze = false,
                channelId = "",
                userId =  "123")


        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is BannedFreeze)
        Assert.assertEquals(actual, expected)
    }

    @Test
    fun mappingFreeze() {
        val type = PlaySocketType.EventFreeze.value

        val jsonObject = JsonObject()
        jsonObject.addProperty("is_banned", false)
        jsonObject.addProperty("is_freeze", true)
        jsonObject.addProperty("channel_id", "123")
        jsonObject.addProperty("user_id", "")

        val actual = actual(type, jsonObject)
        val expected = BannedFreeze(
                isBanned = false,
                isFreeze = true,
                channelId = "123",
                userId =  "")

        Assert.assertNotNull(actual)
        Assert.assertTrue(actual is BannedFreeze)
        Assert.assertEquals(actual, expected)
    }

}