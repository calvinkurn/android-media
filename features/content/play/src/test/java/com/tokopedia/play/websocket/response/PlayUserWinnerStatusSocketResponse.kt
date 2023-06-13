package com.tokopedia.play.websocket.response

/**
 * Created By : Jonathan Darwin on October 07, 2021
 */
object PlayUserWinnerStatusSocketResponse {

    const val type = "USER_WINNER_STATUS"
    const val imageUrl = "https://images.tokopedia.net/img/cache/300/default_picture_user/default_toped-15.jpg"
    const val winnerTitle = "Selamat Kamu Pemenangnya!"
    const val winnerText = "Tunggu seller chat kamu untuk konfirmasi"
    const val loserTitle = "Pemenangnya Will!"
    const val loserText = "Coba ikut main lagi nanti, ya. Siapa tahu kamu yang menang."

    fun generateResponse(id: Int = 1, interactiveId: Int = 1, userId: Int = 1) = """
        {
              "type" : "$type",  
              "data" : {
                    "channel_id": $id,
                    "interactive_id": $interactiveId,
                    "user_id" : $userId,
                    "name": "will",
                    "image_url": "$imageUrl",
                    "winner_title" : "$winnerTitle",
                    "winner_text" : "$winnerText",
                    "loser_title" : "$loserTitle",
                    "loser_text" : "$loserText"
              }
        } 
    """.trimIndent()
}
